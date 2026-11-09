package controladores;

import DataAccesObject.UsuariosDAO;
import Dominio.Usuarios;
import Servicios.UsuarioService;
import java.io.IOException;
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

    private UsuarioService usuarioService;

    @FXML
    public void initialize() {
        usuarioService = new UsuarioService(new UsuariosDAO());
        btnIngresar.setOnAction(e -> intentarLogin());
    }

    private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        String clave = txtPassword.getText();

        if (usuario.isEmpty() || clave.isEmpty()) {
            lblMensaje.setText("Usuario y contraseña son obligatorios.");
            return;
        }

        try {
            Usuarios u = usuarioService.login(usuario, clave);
            if (u == null) {
                lblMensaje.setText("Usuario o contraseña incorrectos.");
                return;
            }

            // Guardar quién inició sesión
            Sesion.setUsuarioActual(u);

            // Ir a la pantalla de operador
            App.setRoot("OperadorVentanilla");

        } catch (Exception ex) {
            lblMensaje.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
