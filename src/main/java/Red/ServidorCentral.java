package Red;

import Configuracion.FabricaServicios;
import Dominio.LlamadoTurno;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

public class ServidorCentral {
    private static final int PUERTO = Protocolo.PUERTO;

    private final int puerto;
    private final FabricaServicios fabrica;
    private final Set<HiloCliente> pantallasConectadas = new CopyOnWriteArraySet<>();
    private final Set<HiloCliente> clientesConectados = new CopyOnWriteArraySet<>();
    private final Map<Integer, HiloCliente> ventanillasEnUso = new ConcurrentHashMap<>();
    private final AtomicInteger contadorClientes = new AtomicInteger();
    private volatile ServerSocket serverSocket;

    public ServidorCentral() {
        this(PUERTO, null);
    }

    public ServidorCentral(int puerto, FabricaServicios fabrica) {
        this.puerto = puerto;
        this.fabrica = fabrica;
    }

    public void iniciarServidor() {
        System.out.println("Inicializando componentes...");
        FabricaServicios servicios = fabrica != null ? fabrica : FabricaServicios.get();

        servicios.ventanillas().agregarObservador(this::notificarNuevoLlamado);

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            this.serverSocket = servidor;
            System.out.println("Servidor Central escuchando en el puerto " + puerto);

            while (!servidor.isClosed()) {
                Socket socketCliente = servidor.accept();
                int numero = contadorClientes.incrementAndGet();
                System.out.println("Nueva conexion #" + numero + " desde " + socketCliente.getInetAddress());
                HiloCliente manejador = new HiloCliente(socketCliente, this, servicios);
                clientesConectados.add(manejador);
                Thread hilo = new Thread(manejador, "cliente-" + numero);
                hilo.setDaemon(true);
                hilo.start();
            }
        } catch (IOException e) {
            if (serverSocket == null || !serverSocket.isClosed()) {
                System.err.println("Error crítico en el socket del servidor: " + e.getMessage());
            }
        }
    }

    public void detener() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error al detener el servidor: " + e.getMessage());
        }
        for (HiloCliente cliente : clientesConectados) {
            cliente.cerrar();
        }
    }

    public void clienteDesconectado(HiloCliente cliente) {
        clientesConectados.remove(cliente);
    }

    public void registrarPantallaPublica(HiloCliente pantalla) {
        pantallasConectadas.add(pantalla);
        System.out.println("Pantallas conectadas: " + pantallasConectadas.size());
    }

    public void removerPantallaPublica(HiloCliente pantalla) {
        pantallasConectadas.remove(pantalla);
    }

    public void notificarNuevoLlamado(LlamadoTurno llamado) {
        String mensaje = Protocolo.armar(Protocolo.LLAMADO, llamado.getCodigoTurno(),
                llamado.getNumeroVentanilla(), llamado.getHoraLlamado());
        for (HiloCliente pantalla : pantallasConectadas) {
            pantalla.enviarMensajeAsincrono(mensaje);
        }
    }

    public int cantidadPantallas() {
        return pantallasConectadas.size();
    }

    public boolean reservarVentanilla(int ventanillaId, HiloCliente cliente) {
        HiloCliente actual = ventanillasEnUso.putIfAbsent(ventanillaId, cliente);
        return actual == null || actual == cliente;
    }

    public void liberarVentanilla(int ventanillaId, HiloCliente cliente) {
        ventanillasEnUso.remove(ventanillaId, cliente);
    }

    public static boolean estaEscuchando(String host, int puerto) {
        try (Socket prueba = new Socket()) {
            prueba.connect(new java.net.InetSocketAddress(host, puerto), 500);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        new ServidorCentral().iniciarServidor();
    }
}
