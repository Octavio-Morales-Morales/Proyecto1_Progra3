package controladores;

import Red.ClienteSocket;
import Dominio.Usuarios;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class OperadorVentanillaController implements Initializable {

    @FXML private Label lblTitulo;
    @FXML private Label lblUsuario;
    @FXML private Label lblEstadoVentanilla;
    @FXML private Label lblTurnoActual;
    @FXML private Label lblServicioActual;
    @FXML private Button btnSiguiente;
    @FXML private Button btnFinalizar;
    @FXML private Button btnVolverLlamar;
    @FXML private Button btnNoDisponible;
    @FXML private ListView<String> lstHistorial;

    private ClienteSocket cliente;
    private int atencionActualId = -1;
    private int turnoActualId = -1;
    private int ventanillaId = 1;          // Temporal: cambia según el operador
    private int funcionarioId = 1;         // Temporal

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cliente = App.getClienteSocket();
        
        if (Sesion.getNombre() != null) {
    lblUsuario.setText("Usuario: " + Sesion.getNombre());
    lblTitulo.setText("OPERADOR - Ventanilla " + ventanillaId);
    }
        lblEstadoVentanilla.setText("● Disponible");
        lblTurnoActual.setText("---");

        btnSiguiente.setOnAction(e -> solicitarSiguiente());
        btnFinalizar.setOnAction(e -> finalizarAtencion());
        btnVolverLlamar.setOnAction(e -> volverALlamar());
        btnNoDisponible.setOnAction(e -> marcarNoDisponible());
    }

    private void solicitarSiguiente() {
        if (cliente == null) return;
        new Thread(() -> {
            try {
                // Servicios que puede atender esta ventanilla (ajusta según BD)
                String cmd = "SOLICITAR_SIGUIENTE;" + ventanillaId + ";" + funcionarioId + ";1,2,3,4";
                String resp = cliente.enviarMensaje(cmd);

                Platform.runLater(() -> {
                    if (resp != null && resp.startsWith("OK;")) {
                        String[] p = resp.split(";");
                        String codigo = p[1];
                        turnoActualId = Integer.parseInt(p[2]);
                        atencionActualId = Integer.parseInt(p[3]);

                        lblTurnoActual.setText(codigo);
                        lblServicioActual.setText("En atención");
                        lstHistorial.getItems().add(0, codigo + " - Llamado");
                    } else {
                        mostrarError("No hay turnos o error: " + resp);
                    }
                });
            } catch (Exception ex) {
                Platform.runLater(() -> mostrarError(ex.getMessage()));
            }
        }).start();
    }

    private void finalizarAtencion() {
        if (atencionActualId <= 0 || turnoActualId <= 0) {
            mostrarError("No hay turno en atención.");
            return;
        }
        new Thread(() -> {
            try {
                String cmd = "TERMINAR_TRAMITE;" + atencionActualId + ";" + turnoActualId + ";Finalizado";
                String resp = cliente.enviarMensaje(cmd);
                Platform.runLater(() -> {
                    if (resp != null && resp.startsWith("OK")) {
                        lstHistorial.getItems().add(0, lblTurnoActual.getText() + " - Finalizado");
                        lblTurnoActual.setText("---");
                        lblServicioActual.setText("-");
                        atencionActualId = -1;
                        turnoActualId = -1;
                    } else {
                        mostrarError(resp);
                    }
                });
            } catch (Exception ex) {
                Platform.runLater(() -> mostrarError(ex.getMessage()));
            }
        }).start();
    }

    private void volverALlamar() {
        // Por ahora solo muestra mensaje. Se puede mejorar después.
        if (lblTurnoActual.getText().equals("---")) {
            mostrarError("No hay turno para volver a llamar.");
            return;
        }
        mostrarInfo("Turno " + lblTurnoActual.getText() + " vuelto a llamar.");
    }

    private void marcarNoDisponible() {
        if (lblEstadoVentanilla.getText().contains("Disponible")) {
            lblEstadoVentanilla.setText("● No disponible");
            lblEstadoVentanilla.setStyle("-fx-text-fill: #c62828;");
        } else {
            lblEstadoVentanilla.setText("● Disponible");
            lblEstadoVentanilla.setStyle("-fx-text-fill: #2e7d32;");
        }
    }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
