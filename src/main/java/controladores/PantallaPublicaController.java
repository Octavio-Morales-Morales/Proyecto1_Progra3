package controladores;

import Dominio.LlamadoTurno;
import Red.ClienteTurnos;
import Red.EscuchaPantalla;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Duration;

public class PantallaPublicaController implements Initializable {
    private static final DateTimeFormatter RELOJ = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final int CANTIDAD_RECIENTES = 8;

    @FXML
    private Label lblHora;
    @FXML
    private ListView<String> lstRecientes;
    @FXML
    private Label lblUltimoTurno;
    @FXML
    private Label lblVentanilla;

    private final ClienteTurnos cliente = new ClienteTurnos();
    private EscuchaPantalla escucha;
    private Timeline reloj;
    private boolean conectada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUltimoTurno.setText("---");
        lblVentanilla.setText("Conectando con el servidor...");
        iniciarReloj();

        escucha = new EscuchaPantalla(
                llamado -> Platform.runLater(() -> mostrarLlamado(llamado)),
                conectado -> Platform.runLater(() -> cambioDeConexion(conectado)));
        escucha.iniciar();
    }

    public void detener() {
        if (escucha != null) {
            escucha.detener();
        }
        if (reloj != null) {
            reloj.stop();
        }
        cliente.cerrar();
    }

    private void iniciarReloj() {
        lblHora.setText(LocalTime.now().format(RELOJ));
        reloj = new Timeline(new KeyFrame(Duration.seconds(1), e -> lblHora.setText(LocalTime.now().format(RELOJ))));
        reloj.setCycleCount(Timeline.INDEFINITE);
        reloj.play();
    }

    private void cambioDeConexion(boolean conectado) {
        if (conectado == conectada) {
            return;
        }
        conectada = conectado;
        if (!conectado) {
            lblVentanilla.setText("Sin conexion con el servidor. Reconectando...");
            return;
        }
        Ui.enSegundoPlano(
                () -> cliente.ultimosLlamados(CANTIDAD_RECIENTES),
                this::mostrarIniciales,
                mensaje -> lblVentanilla.setText(mensaje));
    }

    private void mostrarIniciales(List<LlamadoTurno> llamados) {
        lstRecientes.getItems().clear();
        for (LlamadoTurno l : llamados) {
            lstRecientes.getItems().add(l.toString());
        }
        if (llamados.isEmpty()) {
            lblUltimoTurno.setText("---");
            lblVentanilla.setText("");
        } else {
            LlamadoTurno ultimo = llamados.get(0);
            lblUltimoTurno.setText(ultimo.getCodigoTurno());
            lblVentanilla.setText("→  VENTANILLA " + ultimo.getNumeroVentanilla());
        }
    }

    private void mostrarLlamado(LlamadoTurno llamado) {
        lblUltimoTurno.setText(llamado.getCodigoTurno());
        lblVentanilla.setText("→  VENTANILLA " + llamado.getNumeroVentanilla());

        lstRecientes.getItems().remove(llamado.toString());
        lstRecientes.getItems().add(0, llamado.toString());
        while (lstRecientes.getItems().size() > CANTIDAD_RECIENTES) {
            lstRecientes.getItems().remove(lstRecientes.getItems().size() - 1);
        }
        llamarAtencion();
    }

    private void llamarAtencion() {
        FadeTransition parpadeo = new FadeTransition(Duration.millis(350), lblUltimoTurno);
        parpadeo.setFromValue(1.0);
        parpadeo.setToValue(0.2);
        parpadeo.setCycleCount(6);
        parpadeo.setAutoReverse(true);
        parpadeo.play();
        try {
            java.awt.Toolkit.getDefaultToolkit().beep();
        } catch (Throwable sinSonido) {
        }
    }
}
