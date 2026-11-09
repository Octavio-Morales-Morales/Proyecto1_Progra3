package controladores;

import Red.ClienteSocket;
import java.io.BufferedReader;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class PantallaPublicaController implements Initializable {

    @FXML private Label lblHora;
    @FXML private Label lblUltimoTurno;
    @FXML private Label lblVentanilla;
    @FXML private ListView<String> lstRecientes;

    private ClienteSocket cliente;
    private volatile boolean escuchando = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cliente = App.getClienteSocket();
        actualizarHora();

        // Registrar como pantalla pública
        new Thread(() -> {
            try {
                String resp = cliente.enviarMensaje("REGISTRAR_PANTALLA");
                if (resp != null && resp.startsWith("OK")) {
                    System.out.println("Pantalla pública registrada");
                    iniciarEscucha();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Reloj
        new Thread(() -> {
            while (escuchando) {
                Platform.runLater(this::actualizarHora);
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private void iniciarEscucha() {
        new Thread(() -> {
            try {
                BufferedReader entrada = cliente.getEntrada();
                String linea;
                while (escuchando && (linea = entrada.readLine()) != null) {
                    if (linea.startsWith("Pantalla_Actualizar;")) {
                        String[] p = linea.split(";");
                        String turno = p.length > 1 ? p[1].trim() : "---";
                        String vent = p.length > 2 ? p[2].trim() : "---";

                        Platform.runLater(() -> {
                            lblUltimoTurno.setText(turno);
                            lblVentanilla.setText("→  VENTANILLA " + vent);
                            lstRecientes.getItems().add(0, turno + "  →  Ventanilla " + vent);
                            if (lstRecientes.getItems().size() > 10) {
                                lstRecientes.getItems().remove(10);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                System.err.println("Error escuchando pantalla: " + e.getMessage());
            }
        }).start();
    }

    private void actualizarHora() {
        lblHora.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    public void detener() {
        escuchando = false;
    }
}