package Red;

import Dominio.LlamadoTurno;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class EscuchaPantalla implements Runnable {
    private static final int REINTENTO_MS = 3000;

    private final String host;
    private final int puerto;
    private final Consumer<LlamadoTurno> alLlamar;
    private final Consumer<Boolean> alCambiarConexion;
    private volatile boolean activa = true;
    private volatile Socket socket;

    public EscuchaPantalla(Consumer<LlamadoTurno> alLlamar, Consumer<Boolean> alCambiarConexion) {
        this(System.getProperty("servidor.host", "localhost"),
                Integer.getInteger("servidor.puerto", Protocolo.PUERTO), alLlamar, alCambiarConexion);
    }

    public EscuchaPantalla(String host, int puerto, Consumer<LlamadoTurno> alLlamar, Consumer<Boolean> alCambiarConexion) {
        this.host = host;
        this.puerto = puerto;
        this.alLlamar = alLlamar;
        this.alCambiarConexion = alCambiarConexion;
    }

    public Thread iniciar() {
        Thread hilo = new Thread(this, "escucha-pantalla");
        hilo.setDaemon(true);
        hilo.start();
        return hilo;
    }

    public void detener() {
        activa = false;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        while (activa) {
            try (Socket s = new Socket()) {
                s.connect(new InetSocketAddress(host, puerto), REINTENTO_MS);
                socket = s;
                PrintWriter salida = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));

                salida.println(Protocolo.armar(Protocolo.REGISTRAR_PANTALLA));
                alCambiarConexion.accept(true);

                String linea;
                while (activa && (linea = entrada.readLine()) != null) {
                    String[] campos = Protocolo.separar(linea);
                    if (Protocolo.LLAMADO.equals(campos[0])) {
                        try {
                            alLlamar.accept(Protocolo.llamado(campos, 1));
                        } catch (RuntimeException datoMalo) {
                            System.err.println("Aviso de llamado con formato inválido: " + linea);
                        }
                    }
                }
            } catch (IOException e) {
            }
            if (activa) {
                alCambiarConexion.accept(false);
                dormir();
            }
        }
    }

    private static void dormir() {
        try {
            Thread.sleep(REINTENTO_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
