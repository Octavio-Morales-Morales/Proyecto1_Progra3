package controladores;

import Red.ClienteSocket;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnIngresar;
    @FXML private Label lblMensaje;

    private ClienteSocket cliente;

    @FXML
    public void initialize() {
        cliente = App.getClienteSocket();
        btnIngresar.setOnAction(e -> intentarLogin());
    }

    private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        String clave = txtPassword.getText();

        if (usuario.isEmpty() || clave.isEmpty()) {
            lblMensaje.setText("Usuario y contraseña son obligatorios.");
            return;
        }

        if (cliente == null) {
            lblMensaje.setText("No hay conexión con el servidor.");
            return;
        }

        new Thread(() -> {
            try {
                String comando = "LOGIN;" + usuario + ";" + clave;
                String respuesta = cliente.enviarMensaje(comando);

                Platform.runLater(() -> {
                    if (respuesta != null && respuesta.startsWith("OK;")) {
                        String[] partes = respuesta.split(";");
                        int usuarioId = Integer.parseInt(partes[1]);
                        String nombre = partes[2];
                        int rolId = Integer.parseInt(partes[3]);

                        // Guardar sesión básica
                        Sesion.setUsuarioActual(usuarioId, nombre, rolId);

                        try {
                            App.setRoot("OperadorVentanilla");
                        } catch (IOException ex) {
                            lblMensaje.setText("Error al cargar pantalla.");
                        }
                    } else {
                        lblMensaje.setText("Usuario o contraseña incorrectos.");
                    }
                });
            } catch (Exception ex) {
                Platform.runLater(() -> lblMensaje.setText("Error de conexión: " + ex.getMessage()));
            }
        }).start();
    }
}