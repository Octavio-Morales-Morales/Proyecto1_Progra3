package Red;

import Configuracion.FabricaServicios;
import Dominio.ConteoReporte;
import Dominio.DetalleAtencion;
import Dominio.LlamadoTurno;
import Dominio.Servicio;
import Dominio.Sesion;
import Dominio.Turnos;
import Dominio.TurnoEnVentanilla;
import Dominio.Ventanillas;
import Excepciones.PersistenciaException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HiloCliente implements Runnable {
    private static final int MAXIMO_LISTA = 200;

    private final Socket socket;
    private final ServidorCentral servidor;
    private final FabricaServicios servicios;

    private PrintWriter salida;
    private BufferedReader entrada;
    private final Object candadoSalida = new Object();

    private boolean esPantallaPublica = false;
    private Sesion sesion;
    private Integer ventanillaAbierta;

    public HiloCliente(Socket socket, ServidorCentral servidor, FabricaServicios servicios) {
        this.socket = socket;
        this.servidor = servidor;
        this.servicios = servicios;
    }

    @Override
    public void run() {
        try {
            this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            this.salida = new PrintWriter(new java.io.OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            String lineaRecibida;
            while ((lineaRecibida = entrada.readLine()) != null) {
                if (lineaRecibida.trim().isEmpty()) {
                    continue;
                }
                procesarProtocolo(lineaRecibida);
            }
        } catch (IOException e) {
        } finally {
            cerrarConexion();
        }
    }

    private void procesarProtocolo(String linea) {
        String[] partes = Protocolo.separar(linea);
        String comando = partes[0].trim().toUpperCase();

        try {
            switch (comando) {
                case Protocolo.REGISTRAR_PANTALLA:
                    esPantallaPublica = true;
                    servidor.registrarPantallaPublica(this);
                    responder(Protocolo.OK, "Registrada");
                    break;

                case Protocolo.SERVICIOS:
                    List<Object[]> filasServicios = new ArrayList<>();
                    for (Servicio s : servicios.servicios().listarDisponibles()) {
                        filasServicios.add(Protocolo.campos(s));
                    }
                    responderLista(filasServicios);
                    break;

                case Protocolo.GENERAR_TURNO: {
                    Servicio servicio = servicios.servicios().buscar(Protocolo.entero(partes[1]));
                    if (!servicio.isActivo()) {
                        throw new IllegalStateException("El servicio " + servicio.getNombre() + " no está disponible.");
                    }
                    Turnos turno = servicios.turnos().generarNuevoTurno(servicio.getCodigo(), servicio.getServicioId(),
                            servicios.servicios().prioridadPara(servicio));
                    responder(Protocolo.OK, turno.getTurnoId(), turno.getCodigoTurno(), servicio.getNombre(),
                            turno.getGeneracion(), turno.getHoraGeneracion());
                    break;
                }

                case Protocolo.ULTIMOS_LLAMADOS: {
                    List<Object[]> filas = new ArrayList<>();
                    for (LlamadoTurno l : servicios.ventanillas().ultimosLlamados(limite(partes, 1))) {
                        filas.add(Protocolo.campos(l));
                    }
                    responderLista(filas);
                    break;
                }

                case Protocolo.LOGIN:
                    sesion = servicios.sesion().iniciarSesion(partes[1], partes[2]);
                    responder(Protocolo.OK,
                            sesion.getUsuario().getUsuarioId(), sesion.getUsuario().getNombre(),
                            sesion.getUsuario().getUsuario(), sesion.getUsuario().getRolId(),
                            sesion.getFuncionario() == null ? 0 : sesion.getFuncionario().getFuncionarioId(),
                            sesion.getFuncionario() == null ? "" : sesion.getFuncionario().getNombreCompleto(),
                            sesion.getFuncionario() == null ? "" : sesion.getFuncionario().getIdentificacion());
                    break;

                case Protocolo.VENTANILLAS: {
                    exigirOperador();
                    List<Object[]> filas = new ArrayList<>();
                    for (Ventanillas v : servicios.ventanillas().ventanillasActivas()) {
                        filas.add(Protocolo.campos(v));
                    }
                    responderLista(filas);
                    break;
                }

                case Protocolo.ABRIR_VENTANILLA: {
                    exigirOperador();
                    int ventanillaId = Protocolo.entero(partes[1]);
                    Ventanillas v = servicios.ventanillas().consultarVentanilla(ventanillaId);
                    if (!v.isActivo()) {
                        throw new IllegalStateException("La ventanilla " + v.getNumero() + " está inactiva.");
                    }
                    if (!servidor.reservarVentanilla(ventanillaId, this)) {
                        throw new IllegalStateException("La ventanilla " + v.getNumero() + " ya está siendo usada por otro operador.");
                    }
                    if (ventanillaAbierta != null && ventanillaAbierta != ventanillaId) {
                        servidor.liberarVentanilla(ventanillaAbierta, this);
                    }
                    ventanillaAbierta = ventanillaId;
                    responder(Protocolo.OK, Protocolo.campos(v));
                    break;
                }

                case Protocolo.ESTADO_VENTANILLA:
                    responder(Protocolo.OK, Protocolo.campos(servicios.ventanillas().consultarVentanilla(exigirVentanilla())));
                    break;

                case Protocolo.TURNO_ACTUAL: {
                    Optional<TurnoEnVentanilla> actual = servicios.ventanillas().turnoActual(exigirVentanilla());
                    if (actual.isPresent()) {
                        responder(Protocolo.OK, Protocolo.campos(actual.get()));
                    } else {
                        responder(Protocolo.OK, Protocolo.NINGUNO);
                    }
                    break;
                }

                case Protocolo.SOLICITAR_SIGUIENTE: {
                    int ventanillaId = exigirVentanilla();
                    TurnoEnVentanilla t = servicios.ventanillas()
                            .llamarSiguiente(ventanillaId, sesion.getFuncionario().getFuncionarioId());
                    responder(Protocolo.OK, Protocolo.campos(t));
                    break;
                }

                case Protocolo.COMENZAR_TRAMITE:
                    responder(Protocolo.OK, Protocolo.campos(servicios.ventanillas().iniciarAtencion(exigirVentanilla())));
                    break;

                case Protocolo.TERMINAR_TRAMITE: {
                    String notas = partes.length > 1 ? partes[1] : "";
                    servicios.ventanillas().finalizarAtencion(exigirVentanilla(), notas);
                    responder(Protocolo.OK, "Tramite finalizado");
                    break;
                }

                case Protocolo.VOLVER_A_LLAMAR:
                    servicios.ventanillas().volverALlamar(exigirVentanilla());
                    responder(Protocolo.OK, "Llamado repetido");
                    break;

                case Protocolo.NO_DISPONIBLE:
                    servicios.ventanillas().marcarNoDisponible(exigirVentanilla());
                    responder(Protocolo.OK, "No disponible");
                    break;

                case Protocolo.DISPONIBLE:
                    servicios.ventanillas().marcarDisponible(exigirVentanilla());
                    responder(Protocolo.OK, "Disponible");
                    break;

                case Protocolo.HISTORIAL: {
                    List<Object[]> filas = new ArrayList<>();
                    for (DetalleAtencion d : servicios.ventanillas().historialVentanilla(exigirVentanilla(), limite(partes, 1))) {
                        filas.add(Protocolo.campos(d));
                    }
                    responderLista(filas);
                    break;
                }

                case Protocolo.REPORTE_ATENDIDOS: {
                    exigirAdministrador();
                    List<Object[]> filas = new ArrayList<>();
                    for (DetalleAtencion d : servicios.reportes().turnosAtendidos(fecha(partes, 1), fecha(partes, 2))) {
                        filas.add(Protocolo.campos(d));
                    }
                    responderLista(filas);
                    break;
                }

                case Protocolo.REPORTE_FUNCIONARIOS:
                    exigirAdministrador();
                    responderConteos(servicios.reportes().atencionPorFuncionario(fecha(partes, 1), fecha(partes, 2)));
                    break;

                case Protocolo.REPORTE_SERVICIOS:
                    exigirAdministrador();
                    responderConteos(servicios.reportes().atencionPorServicio(fecha(partes, 1), fecha(partes, 2)));
                    break;

                default:
                    responder(Protocolo.ERROR, "Comando no soportado por el protocolo: " + comando);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            responder(Protocolo.ERROR, "Faltan datos en el mensaje " + comando + ".");
        } catch (IllegalArgumentException | IllegalStateException e) {
            responder(Protocolo.ERROR, e.getMessage());
        } catch (PersistenciaException e) {
            System.err.println("[" + comando + "] " + e.getMessage()
                    + (e.getCause() != null ? " -> " + e.getCause().getMessage() : ""));
            responder(Protocolo.ERROR, "El servidor no pudo comunicarse con la base de datos. Intente de nuevo.");
        } catch (RuntimeException e) {
            System.err.println("[" + comando + "] Error inesperado: " + e);
            responder(Protocolo.ERROR, "Error interno del servidor.");
        }
    }

    private void exigirOperador() {
        if (sesion == null) {
            throw new IllegalStateException("Debe iniciar sesión.");
        }
        if (sesion.getFuncionario() == null) {
            throw new IllegalStateException("Esta operación es solo para operadores.");
        }
    }

    private int exigirVentanilla() {
        exigirOperador();
        if (ventanillaAbierta == null) {
            throw new IllegalStateException("Primero seleccione su ventanilla.");
        }
        return ventanillaAbierta;
    }

    private void exigirAdministrador() {
        if (sesion == null) {
            throw new IllegalStateException("Debe iniciar sesión.");
        }
        if (!sesion.esAdministrador()) {
            throw new IllegalStateException("Solo el administrador puede consultar los reportes.");
        }
    }

    private void responder(Object... campos) {
        enviarMensajeAsincrono(Protocolo.armar(aplanar(campos)));
    }

    private void responderLista(List<Object[]> filas) {
        synchronized (candadoSalida) {
            salida.println(Protocolo.armar(Protocolo.OK, filas.size()));
            for (Object[] fila : filas) {
                salida.println(Protocolo.armar(fila));
            }
        }
    }

    private void responderConteos(List<ConteoReporte> conteos) {
        List<Object[]> filas = new ArrayList<>();
        for (ConteoReporte c : conteos) {
            filas.add(Protocolo.campos(c));
        }
        responderLista(filas);
    }

    public void enviarMensajeAsincrono(String mensaje) {
        synchronized (candadoSalida) {
            if (salida != null) {
                salida.println(mensaje);
            }
        }
    }

    private static Object[] aplanar(Object[] campos) {
        List<Object> lista = new ArrayList<>();
        for (Object c : campos) {
            if (c instanceof Object[]) {
                for (Object interno : (Object[]) c) {
                    lista.add(interno);
                }
            } else {
                lista.add(c);
            }
        }
        return lista.toArray();
    }

    private static int limite(String[] partes, int indice) {
        int limite = partes.length > indice ? Protocolo.entero(partes[indice]) : 10;
        if (limite <= 0 || limite > MAXIMO_LISTA) {
            throw new IllegalArgumentException("La cantidad solicitada debe estar entre 1 y " + MAXIMO_LISTA + ".");
        }
        return limite;
    }

    private static LocalDate fecha(String[] partes, int indice) {
        try {
            return LocalDate.parse(partes[indice]);
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Fecha inválida: " + partes[indice]);
        }
    }

    public void cerrar() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    private void cerrarConexion() {
        servidor.clienteDesconectado(this);
        if (esPantallaPublica) {
            servidor.removerPantallaPublica(this);
        }
        if (ventanillaAbierta != null) {
            servidor.liberarVentanilla(ventanillaAbierta, this);
        }
        try {
            if (entrada != null) {
                entrada.close();
            }
            synchronized (candadoSalida) {
                if (salida != null) {
                    salida.close();
                }
            }
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar sockets: " + e.getMessage());
        }
    }
}
