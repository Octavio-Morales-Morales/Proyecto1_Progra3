package Red;

import Dominio.ConteoReporte;
import Dominio.DetalleAtencion;
import Dominio.Funcionarios;
import Dominio.LlamadoTurno;
import Dominio.Servicio;
import Dominio.Sesion;
import Dominio.TurnoEnVentanilla;
import Dominio.Usuarios;
import Dominio.Ventanillas;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteTurnos {
    public static class TurnoGenerado {
        public final int turnoId;
        public final String codigo;
        public final String servicio;
        public final LocalDateTime fechaHora;

        TurnoGenerado(int turnoId, String codigo, String servicio, LocalDateTime fechaHora) {
            this.turnoId = turnoId;
            this.codigo = codigo;
            this.servicio = servicio;
            this.fechaHora = fechaHora;
        }
    }

    private final ClienteSocket conexion;

    private String usuario;
    private String clave;
    private Integer ventanillaId;

    public ClienteTurnos() {
        this(new ClienteSocket());
    }

    public ClienteTurnos(ClienteSocket conexion) {
        this.conexion = conexion;
        this.conexion.setAlReconectar(this::restablecerSesion);
    }

    public void cerrar() {
        conexion.desconectar();
    }

    public List<Servicio> serviciosDisponibles() {
        List<Servicio> lista = new ArrayList<>();
        for (String[] fila : conexion.enviarLista(Protocolo.SERVICIOS)) {
            lista.add(Protocolo.servicio(fila, 0));
        }
        return lista;
    }

    public TurnoGenerado generarTurno(int servicioId) {
        String[] r = conexion.enviar(Protocolo.GENERAR_TURNO, servicioId);
        return new TurnoGenerado(Protocolo.entero(r[0]), r[1], r[2],
                LocalDateTime.of(LocalDate.parse(r[3]), LocalTime.parse(r[4])));
    }

    public List<LlamadoTurno> ultimosLlamados(int limite) {
        List<LlamadoTurno> lista = new ArrayList<>();
        for (String[] fila : conexion.enviarLista(Protocolo.ULTIMOS_LLAMADOS, limite)) {
            lista.add(Protocolo.llamado(fila, 0));
        }
        return lista;
    }

    public Sesion iniciarSesion(String nombreUsuario, String contrasenia) {
        String[] r = conexion.enviar(Protocolo.LOGIN, nombreUsuario, contrasenia);
        this.usuario = nombreUsuario;
        this.clave = contrasenia;

        Usuarios u = new Usuarios(Protocolo.entero(r[0]), r[1], r[2], "(protegida)",
                Protocolo.entero(r[3]), true, null);
        int funcionarioId = Protocolo.entero(r[4]);
        Funcionarios f = funcionarioId == 0 ? null
                : new Funcionarios(funcionarioId, u.getUsuarioId(), r[5], r[6], true);
        return new Sesion(u, f);
    }

    public List<Ventanillas> ventanillasActivas() {
        List<Ventanillas> lista = new ArrayList<>();
        for (String[] fila : conexion.enviarLista(Protocolo.VENTANILLAS)) {
            lista.add(Protocolo.ventanilla(fila, 0));
        }
        return lista;
    }

    public Ventanillas abrirVentanilla(int ventanillaId) {
        Ventanillas v = Protocolo.ventanilla(conexion.enviar(Protocolo.ABRIR_VENTANILLA, ventanillaId), 0);
        this.ventanillaId = ventanillaId;
        return v;
    }

    public Ventanillas consultarVentanilla() {
        return Protocolo.ventanilla(conexion.enviar(Protocolo.ESTADO_VENTANILLA), 0);
    }

    public Optional<TurnoEnVentanilla> turnoActual() {
        String[] r = conexion.enviar(Protocolo.TURNO_ACTUAL);
        if (Protocolo.NINGUNO.equals(r[0])) {
            return Optional.empty();
        }
        return Optional.of(Protocolo.turnoEnVentanilla(r, 0));
    }

    public TurnoEnVentanilla llamarSiguiente() {
        return Protocolo.turnoEnVentanilla(conexion.enviar(Protocolo.SOLICITAR_SIGUIENTE), 0);
    }

    public TurnoEnVentanilla iniciarAtencion() {
        return Protocolo.turnoEnVentanilla(conexion.enviar(Protocolo.COMENZAR_TRAMITE), 0);
    }

    public void finalizarAtencion(String observaciones) {
        conexion.enviar(Protocolo.TERMINAR_TRAMITE, observaciones);
    }

    public void volverALlamar() {
        conexion.enviar(Protocolo.VOLVER_A_LLAMAR);
    }

    public void marcarNoDisponible() {
        conexion.enviar(Protocolo.NO_DISPONIBLE);
    }

    public void marcarDisponible() {
        conexion.enviar(Protocolo.DISPONIBLE);
    }

    public List<DetalleAtencion> historial(int limite) {
        return detalles(conexion.enviarLista(Protocolo.HISTORIAL, limite));
    }

    public List<DetalleAtencion> turnosAtendidos(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);
        return detalles(conexion.enviarLista(Protocolo.REPORTE_ATENDIDOS, desde, hasta));
    }

    public List<ConteoReporte> atencionPorFuncionario(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);
        return conteos(conexion.enviarLista(Protocolo.REPORTE_FUNCIONARIOS, desde, hasta));
    }

    public List<ConteoReporte> atencionPorServicio(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);
        return conteos(conexion.enviarLista(Protocolo.REPORTE_SERVICIOS, desde, hasta));
    }

    private void restablecerSesion(ClienteSocket c) throws IOException {
        if (usuario != null) {
            c.enviarMensaje(Protocolo.armar(Protocolo.LOGIN, usuario, clave));
        }
        if (ventanillaId != null) {
            c.enviarMensaje(Protocolo.armar(Protocolo.ABRIR_VENTANILLA, ventanillaId));
        }
    }

    private static List<DetalleAtencion> detalles(List<String[]> filas) {
        List<DetalleAtencion> lista = new ArrayList<>();
        for (String[] fila : filas) {
            lista.add(Protocolo.detalle(fila, 0));
        }
        return lista;
    }

    private static List<ConteoReporte> conteos(List<String[]> filas) {
        List<ConteoReporte> lista = new ArrayList<>();
        for (String[] fila : filas) {
            lista.add(Protocolo.conteo(fila, 0));
        }
        return lista;
    }

    private static void validarRango(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("Seleccione la fecha inicial y la fecha final.");
        }
        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha inicial no puede ser posterior a la fecha final.");
        }
    }
}
