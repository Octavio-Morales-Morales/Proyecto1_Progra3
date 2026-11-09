/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;


import Red.ClienteSocket;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javafx.scene.control.Alert;

import javafx.scene.control.Alert;

public class GeneradorTurnosController implements Initializable {

    @FXML private Button btnServicioA;
    @FXML private Button btnServicioB;
    @FXML private Button btnServicioC;
    @FXML private Button btnServicioD;
    @FXML private VBox panelResultado;
    @FXML private Label lblNumeroTurno;
    @FXML private Label lblServicio;
    @FXML private Label lblFechaHora;
    @FXML private Button btnNuevoTurno;

    private ClienteSocket cliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cliente = App.getClienteSocket();

        panelResultado.setVisible(false);
        btnNuevoTurno.setVisible(false);

        btnServicioA.setOnAction(e -> generarTurno("A", 1, "Información General", 1));
        btnServicioB.setOnAction(e -> generarTurno("B", 2, "Trámites", 1));
        btnServicioC.setOnAction(e -> generarTurno("C", 3, "Pagos", 1));
        btnServicioD.setOnAction(e -> generarTurno("D", 4, "Atención Preferencial", 2));

        btnNuevoTurno.setOnAction(e -> reiniciarPantalla());
    }

    private void generarTurno(String letra, int servicioId, String nombreServicio, int prioridad) {
        if (cliente == null) {
            mostrarError("No hay conexión con el Servidor Central.\nAsegúrate de que esté corriendo.");
            return;
        }

        setBotonesEnabled(false);

        new Thread(() -> {
            try {
                String comando = "GENERAR_TURNO;" + letra + ";" + servicioId + ";" + prioridad;
                String respuesta = cliente.enviarMensaje(comando);

                Platform.runLater(() -> {
                    if (respuesta != null && respuesta.startsWith("OK;")) {
                        String[] partes = respuesta.split(";");
                        String codigoTurno = partes[1];

                        lblNumeroTurno.setText(codigoTurno);
                        lblServicio.setText("Servicio: " + nombreServicio);
                        lblFechaHora.setText(LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

                        panelResultado.setVisible(true);
                        btnNuevoTurno.setVisible(true);
                    } else {
                        mostrarError("Error al generar turno:\n" + respuesta);
                        setBotonesEnabled(true);
                    }
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    mostrarError("Error de comunicación:\n" + ex.getMessage());
                    setBotonesEnabled(true);
                });
            }
        }).start();
    }

    private void reiniciarPantalla() {
        panelResultado.setVisible(false);
        btnNuevoTurno.setVisible(false);
        lblNumeroTurno.setText("");
        lblServicio.setText("");
        lblFechaHora.setText("");
        setBotonesEnabled(true);
    }

    private void setBotonesEnabled(boolean enabled) {
        btnServicioA.setDisable(!enabled);
        btnServicioB.setDisable(!enabled);
        btnServicioC.setDisable(!enabled);
        btnServicioD.setDisable(!enabled);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}