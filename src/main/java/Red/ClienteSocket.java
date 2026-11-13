package Red;

import Excepciones.ConexionException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ClienteSocket {
    private static final String HOST = System.getProperty("servidor.host", "localhost");
    private static final int PUERTO = Integer.getInteger("servidor.puerto", Protocolo.PUERTO);
    private static final int ESPERA_CONEXION_MS = 3000;

    public interface AccionReconexion {
        void ejecutar(ClienteSocket cliente) throws IOException;
    }

    private final String host;
    private final int puerto;
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private boolean conectoAntes;
    private AccionReconexion alReconectar;

    public ClienteSocket() {
        this(HOST, PUERTO);
    }

    public ClienteSocket(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public synchronized void conectar() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, puerto), ESPERA_CONEXION_MS);
        salida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        System.out.println("Conectado al Servidor Central en " + host + ":" + puerto);
    }

    public synchronized boolean estaConectado() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public synchronized String enviarMensaje(String comando) throws IOException {
        if (salida == null || entrada == null) {
            throw new IOException("No hay una conexión activa con el servidor.");
        }
        salida.println(comando);
        if (salida.checkError()) {
            throw new IOException("Se perdió la conexión con el servidor.");
        }
        String respuesta = entrada.readLine();
        if (respuesta == null) {
            throw new IOException("El servidor cerró la conexión.");
        }
        return respuesta;
    }

    public synchronized String[] enviar(Object... campos) {
        try {
            asegurarConexion();
            String[] respuesta = Protocolo.separar(enviarMensaje(Protocolo.armar(campos)));
            return revisar(respuesta);
        } catch (IOException e) {
            cerrarSilencioso();
            throw new ConexionException("No hay conexión con el servidor central.", e);
        }
    }

    public synchronized List<String[]> enviarLista(Object... campos) {
        try {
            asegurarConexion();
            String[] cabecera = revisar(Protocolo.separar(enviarMensaje(Protocolo.armar(campos))));
            int cantidad = Protocolo.entero(cabecera[0]);
            List<String[]> filas = new ArrayList<>(cantidad);
            for (int i = 0; i < cantidad; i++) {
                String linea = entrada.readLine();
                if (linea == null) {
                    throw new IOException("El servidor cerró la conexión a mitad de la respuesta.");
                }
                filas.add(Protocolo.separar(linea));
            }
            return filas;
        } catch (IOException e) {
            cerrarSilencioso();
            throw new ConexionException("No hay conexión con el servidor central.", e);
        }
    }

    public synchronized void desconectar() {
        try {
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
        } finally {
            socket = null;
            salida = null;
            entrada = null;
        }
    }

    public synchronized BufferedReader getEntrada() {
        return this.entrada;
    }

    public synchronized void setAlReconectar(AccionReconexion accion) {
        this.alReconectar = accion;
    }

    private void asegurarConexion() throws IOException {
        if (!estaConectado()) {
            conectar();
            if (conectoAntes && alReconectar != null) {
                alReconectar.ejecutar(this);
            }
            conectoAntes = true;
        }
    }

    private static String[] revisar(String[] respuesta) {
        if (Protocolo.OK.equals(respuesta[0])) {
            String[] datos = new String[respuesta.length - 1];
            System.arraycopy(respuesta, 1, datos, 0, datos.length);
            return datos;
        }
        if (Protocolo.ERROR.equals(respuesta[0])) {
            throw new IllegalStateException(respuesta.length > 1 ? respuesta[1] : "El servidor rechazó la operación.");
        }
        throw new IllegalStateException("Respuesta desconocida del servidor: " + respuesta[0]);
    }

    private void cerrarSilencioso() {
        desconectar();
    }
}
