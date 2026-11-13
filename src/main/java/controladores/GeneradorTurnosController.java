package controladores;

import Dominio.Servicio;
import Red.ClienteTurnos;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GeneradorTurnosController implements Initializable {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final int SEGUNDOS_RESULTADO = 15;

    @FXML
    private Button btnServicioA;
    @FXML
    private Button btnServicioB;
    @FXML
    private Button btnServicioC;
    @FXML
    private Button btnServicioD;
    @FXML
    private VBox panelResultado;
    @FXML
    private Label lblNumeroTurno;
    @FXML
    private Label lblServicio;
    @FXML
    private Label lblFechaHora;
    @FXML
    private Button btnNuevoTurno;

    private final ClienteTurnos cliente = new ClienteTurnos();
    private final PauseTransition reinicio = new PauseTransition(Duration.seconds(SEGUNDOS_RESULTADO));
    private List<Button> botones;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botones = List.of(btnServicioA, btnServicioB, btnServicioC, btnServicioD);
        panelResultado.setVisible(false);
        btnNuevoTurno.setVisible(false);
        btnNuevoTurno.setOnAction(e -> reiniciar());
        reinicio.setOnFinished(e -> reiniciar());

        for (Button b : botones) {
            b.setDisable(true);
        }
        cargarServicios();
    }

    public void detener() {
        reinicio.stop();
        cliente.cerrar();
    }

    private void cargarServicios() {
        Ui.enSegundoPlano(
                cliente::serviciosDisponibles,
                this::mostrarServicios,
                mensaje -> {
                    for (Button b : botones) {
                        b.setText("Sin conexion con el servidor. Reintentando...");
                        b.setDisable(true);
                    }
                    PauseTransition reintento = new PauseTransition(Duration.seconds(3));
                    reintento.setOnFinished(e -> cargarServicios());
                    reintento.play();
                });
    }

    private void mostrarServicios(List<Servicio> servicios) {
        if (servicios.isEmpty()) {
            Ui.aviso("No hay servicios disponibles en este momento.");
        }
        for (int i = 0; i < botones.size(); i++) {
            Button boton = botones.get(i);
            if (i < servicios.size()) {
                Servicio servicio = servicios.get(i);
                boton.setText(servicio.getCodigo() + " - " + servicio.getNombre());
                boton.setOnAction(e -> generar(servicio));
                boton.setDisable(false);
                boton.setVisible(true);
                boton.setManaged(true);
            } else {
                boton.setVisible(false);
                boton.setManaged(false);
            }
        }
    }

    private void generar(Servicio servicio) {
        habilitarBotones(false);
        Ui.enSegundoPlano(
                () -> cliente.generarTurno(servicio.getServicioId()),
                this::mostrarTurno,
                mensaje -> {
                    Ui.error("No se pudo generar el turno.\n" + mensaje);
                    habilitarBotones(true);
                });
    }

    private void mostrarTurno(ClienteTurnos.TurnoGenerado turno) {
        lblNumeroTurno.setText(turno.codigo);
        lblServicio.setText("Servicio: " + turno.servicio);
        lblFechaHora.setText(turno.fechaHora.format(FORMATO));
        panelResultado.setVisible(true);
        btnNuevoTurno.setVisible(true);
        reinicio.playFromStart();
    }

    private void reiniciar() {
        reinicio.stop();
        panelResultado.setVisible(false);
        btnNuevoTurno.setVisible(false);
        habilitarBotones(true);
    }

    private void habilitarBotones(boolean habilitar) {
        for (Button b : botones) {
            b.setDisable(!habilitar);
        }
    }
}
