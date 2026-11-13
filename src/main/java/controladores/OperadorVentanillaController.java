package controladores;

import Dominio.DetalleAtencion;
import Dominio.Sesion;
import Dominio.TurnoEnVentanilla;
import Dominio.Ventanillas;
import Red.ClienteTurnos;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;

public class OperadorVentanillaController implements Initializable {
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final int TAMANO_HISTORIAL = 10;

    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblUsuario;
    @FXML
    private Label lblEstadoVentanilla;
    @FXML
    private Label lblTurnoActual;
    @FXML
    private Label lblServicioActual;
    @FXML
    private Button btnSiguiente;
    @FXML
    private Button btnFinalizar;
    @FXML
    private Button btnVolverLlamar;
    @FXML
    private Button btnNoDisponible;
    @FXML
    private ListView<String> lstHistorial;

    private ClienteTurnos cliente;
    private Ventanillas ventanilla;
    private TurnoEnVentanilla turnoActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnSiguiente.setOnAction(e -> llamarSiguiente());
        btnFinalizar.setOnAction(e -> iniciarOFinalizar());
        btnVolverLlamar.setOnAction(e -> volverALlamar());
        btnNoDisponible.setOnAction(e -> cambiarDisponibilidad());
        bloquearBotones();
    }

    public void iniciar(Sesion sesion, Ventanillas ventanillaAbierta, ClienteTurnos cliente) {
        this.cliente = cliente;
        this.ventanilla = ventanillaAbierta;
        lblTitulo.setText("OPERADOR - Ventanilla " + ventanillaAbierta.getNumero());
        lblUsuario.setText("Usuario: " + sesion.getNombreVisible());
        refrescar();
    }

    private void llamarSiguiente() {
        ejecutar(cliente::llamarSiguiente);
    }

    private void iniciarOFinalizar() {
        if (turnoActual == null) {
            return;
        }
        if (turnoActual.estaLlamado()) {
            ejecutar(cliente::iniciarAtencion);
            return;
        }
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Finalizar atencion");
        dialogo.setHeaderText("Turno " + turnoActual.getCodigoTurno());
        dialogo.setContentText("Observaciones (opcional):");
        Optional<String> observaciones = dialogo.showAndWait();
        if (observaciones.isPresent()) {
            ejecutar(() -> {
                cliente.finalizarAtencion(observaciones.get());
                return null;
            });
        }
    }

    private void volverALlamar() {
        ejecutar(() -> {
            cliente.volverALlamar();
            return null;
        });
    }

    private void cambiarDisponibilidad() {
        boolean noDisponible = ventanilla != null && Ventanillas.NO_DISPONIBLE.equals(ventanilla.getEstado());
        ejecutar(() -> {
            if (noDisponible) {
                cliente.marcarDisponible();
            } else {
                cliente.marcarNoDisponible();
            }
            return null;
        });
    }

    private void ejecutar(Callable<?> accion) {
        bloquearBotones();
        Ui.enSegundoPlano(accion,
                resultado -> refrescar(),
                mensaje -> {
                    Ui.aviso(mensaje);
                    refrescar();
                });
    }

    private static class EstadoPantalla {
        Ventanillas ventanilla;
        TurnoEnVentanilla turno;
        List<DetalleAtencion> historial;
    }

    private void refrescar() {
        Ui.enSegundoPlano(() -> {
            EstadoPantalla estado = new EstadoPantalla();
            estado.ventanilla = cliente.consultarVentanilla();
            estado.turno = cliente.turnoActual().orElse(null);
            estado.historial = cliente.historial(TAMANO_HISTORIAL);
            return estado;
        }, this::mostrar, mensaje -> {
            pintarEstado("● " + mensaje, Color.web("#c62828"));
            bloquearBotones();
            btnSiguiente.setText("REINTENTAR");
            btnSiguiente.setDisable(false);
            btnSiguiente.setOnAction(e -> {
                btnSiguiente.setText("SIGUIENTE TURNO");
                btnSiguiente.setOnAction(ev -> llamarSiguiente());
                refrescar();
            });
        });
    }

    private void mostrar(EstadoPantalla estado) {
        ventanilla = estado.ventanilla;
        turnoActual = estado.turno;
        boolean noDisponible = Ventanillas.NO_DISPONIBLE.equals(ventanilla.getEstado());

        if (!ventanilla.isActivo()) {
            pintarEstado("● Inactiva", Color.web("#c62828"));
        } else if (noDisponible) {
            pintarEstado("● No disponible", Color.web("#c62828"));
        } else if (turnoActual != null) {
            pintarEstado(turnoActual.estaLlamado() ? "● Llamando" : "● Atendiendo", Color.web("#ef6c00"));
        } else {
            pintarEstado("● Disponible", Color.web("#2e7d32"));
        }

        if (turnoActual == null) {
            lblTurnoActual.setText("---");
            lblServicioActual.setText("Servicio: -");
        } else {
            lblTurnoActual.setText(turnoActual.getCodigoTurno());
            lblServicioActual.setText("Servicio: " + turnoActual.getNombreServicio()
                    + (turnoActual.estaLlamado() ? "   (esperando a la persona)" : "   (en atencion)"));
        }

        btnSiguiente.setDisable(turnoActual != null || noDisponible || !ventanilla.isActivo());
        btnFinalizar.setDisable(turnoActual == null);
        btnFinalizar.setText(turnoActual != null && turnoActual.estaLlamado() ? "INICIAR ATENCION" : "FINALIZAR");
        btnVolverLlamar.setDisable(turnoActual == null || !turnoActual.estaLlamado());
        btnNoDisponible.setText(noDisponible ? "Marcar Disponible" : "Marcar No Disponible");
        btnNoDisponible.setDisable(turnoActual != null || !ventanilla.isActivo());

        List<String> lineas = new ArrayList<>();
        for (DetalleAtencion d : estado.historial) {
            lineas.add(describir(d));
        }
        lstHistorial.getItems().setAll(lineas);
    }

    private void pintarEstado(String texto, Color color) {
        lblEstadoVentanilla.setText(texto);
        lblEstadoVentanilla.setTextFill(color);
    }

    private static String describir(DetalleAtencion d) {
        StringBuilder sb = new StringBuilder(d.getCodigoTurno()).append("  ·  ").append(d.getServicio());
        if (d.getHoraLlamado() != null) {
            sb.append("  ·  llamado ").append(d.getHoraLlamado().format(HORA));
        }
        if (d.getMinutosAtencion() != null) {
            sb.append("  ·  atendido en ").append(d.getMinutosAtencion()).append(" min");
        } else {
            sb.append("  ·  ").append(d.getEstadoFinal());
        }
        return sb.toString();
    }

    private void bloquearBotones() {
        btnSiguiente.setDisable(true);
        btnFinalizar.setDisable(true);
        btnVolverLlamar.setDisable(true);
        btnNoDisponible.setDisable(true);
    }
}
