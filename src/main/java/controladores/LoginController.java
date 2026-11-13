package controladores;

import Dominio.Sesion;
import Dominio.Ventanillas;
import Red.ClienteTurnos;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnIngresar;
    @FXML
    private Label lblMensaje;

    private final ClienteTurnos cliente = new ClienteTurnos();

    @FXML
    private void initialize() {
        btnIngresar.setOnAction(e -> ingresar());
        txtUsuario.setOnAction(e -> txtPassword.requestFocus());
        txtPassword.setOnAction(e -> ingresar());
    }

    private void ingresar() {
        String usuario = txtUsuario.getText();
        String contrasenia = txtPassword.getText();

        if (usuario == null || usuario.trim().isEmpty()) {
            lblMensaje.setText("Digite su usuario.");
            txtUsuario.requestFocus();
            return;
        }
        if (contrasenia == null || contrasenia.isEmpty()) {
            lblMensaje.setText("Digite su contraseña.");
            txtPassword.requestFocus();
            return;
        }

        lblMensaje.setText("Verificando...");
        btnIngresar.setDisable(true);
        Ui.enSegundoPlano(
                () -> cliente.iniciarSesion(usuario.trim(), contrasenia),
                this::entrar,
                this::mostrarError);
    }

    private void entrar(Sesion sesion) {
        lblMensaje.setText("");
        Stage ventana = (Stage) btnIngresar.getScene().getWindow();
        ventana.setOnHidden(e -> cliente.cerrar());

        if (sesion.esAdministrador()) {
            ReportesController reportes = Navegacion.mostrar(ventana, "Reportes", "Reportes - Administrador");
            reportes.iniciar(sesion, cliente);
            return;
        }

        Ui.enSegundoPlano(
                cliente::ventanillasActivas,
                ventanillas -> elegirVentanilla(ventana, sesion, ventanillas),
                this::mostrarError);
    }

    private void elegirVentanilla(Stage ventana, Sesion sesion, List<Ventanillas> ventanillas) {
        if (ventanillas.isEmpty()) {
            mostrarError("No hay ventanillas activas. Contacte al administrador.");
            return;
        }

        ChoiceDialog<Ventanillas> dialogo = new ChoiceDialog<>(ventanillas.get(0), ventanillas);
        dialogo.setTitle("Ventanilla");
        dialogo.setHeaderText("Hola, " + sesion.getNombreVisible());
        dialogo.setContentText("¿En que ventanilla va a trabajar?");
        Optional<Ventanillas> elegida = dialogo.showAndWait();

        if (elegida.isEmpty()) {
            lblMensaje.setText("");
            btnIngresar.setDisable(false);
            return;
        }

        Ui.enSegundoPlano(
                () -> cliente.abrirVentanilla(elegida.get().getVentanillaId()),
                abierta -> {
                    OperadorVentanillaController operador = Navegacion.mostrar(ventana, "OperadorVentanilla",
                            "Operador - " + abierta);
                    operador.iniciar(sesion, abierta, cliente);
                },
                mensaje -> {
                    Ui.aviso(mensaje);
                    elegirVentanilla(ventana, sesion, ventanillas);
                });
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        txtPassword.clear();
        btnIngresar.setDisable(false);
    }
}
