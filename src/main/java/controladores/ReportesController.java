package controladores;

import Dominio.ConteoReporte;
import Dominio.DetalleAtencion;
import Dominio.Sesion;
import Red.ClienteTurnos;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ReportesController implements Initializable {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private Label lblUsuario;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private DatePicker dpDesde;
    @FXML
    private DatePicker dpHasta;
    @FXML
    private Button btnConsultar;
    @FXML
    private Label lblMensaje;
    @FXML
    private Label lblTotalAtendidos;
    @FXML
    private Label lblPromedioEspera;
    @FXML
    private Label lblPromedioAtencion;
    @FXML
    private TableView<DetalleAtencion> tblAtendidos;
    @FXML
    private TableView<ConteoReporte> tblFuncionarios;
    @FXML
    private BarChart<String, Number> chartFuncionarios;
    @FXML
    private TableView<ConteoReporte> tblServicios;
    @FXML
    private BarChart<String, Number> chartServicios;

    private ClienteTurnos cliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dpDesde.setValue(LocalDate.now().withDayOfMonth(1));
        dpHasta.setValue(LocalDate.now());

        tblAtendidos.getColumns().setAll(List.of(
                columna("Turno", DetalleAtencion::getCodigoTurno),
                columna("Servicio", DetalleAtencion::getServicio),
                columna("Fecha", d -> formato(d.getFechaGeneracion(), FECHA)),
                columna("Generado", d -> formato(d.getHoraGeneracion(), HORA)),
                columna("Llamado", d -> formato(d.getHoraLlamado(), HORA)),
                columna("Inicio", d -> formato(d.getHoraInicio(), HORA)),
                columna("Fin", d -> formato(d.getHoraFinalizacion(), HORA)),
                columna("Ventanilla", d -> String.valueOf(d.getNumeroVentanilla())),
                columna("Funcionario", DetalleAtencion::getFuncionario),
                columna("Estado", DetalleAtencion::getEstadoFinal),
                columna("Espera (min)", d -> numero(d.getMinutosEspera())),
                columna("Atención (min)", d -> numero(d.getMinutosAtencion()))));

        tblFuncionarios.getColumns().setAll(List.of(
                columna("Funcionario", ConteoReporte::getNombre),
                columna("Personas atendidas", c -> String.valueOf(c.getCantidad()))));

        tblServicios.getColumns().setAll(List.of(
                columna("Servicio", ConteoReporte::getNombre),
                columna("Turnos", c -> String.valueOf(c.getCantidad()))));

        tblAtendidos.setPlaceholder(new Label("Sin datos para el rango seleccionado."));
        tblFuncionarios.setPlaceholder(new Label("Sin datos."));
        tblServicios.setPlaceholder(new Label("Sin datos."));

        btnConsultar.setOnAction(e -> consultar());
        btnCerrarSesion.setOnAction(e -> cerrarSesion());
    }

    public void iniciar(Sesion sesion, ClienteTurnos cliente) {
        this.cliente = cliente;
        lblUsuario.setText("Administrador: " + sesion.getNombreVisible());
        consultar();
    }

    private static class Resultado {
        List<DetalleAtencion> atendidos;
        List<ConteoReporte> porFuncionario;
        List<ConteoReporte> porServicio;
    }

    private void consultar() {
        LocalDate desde = dpDesde.getValue();
        LocalDate hasta = dpHasta.getValue();
        lblMensaje.setText("Consultando...");
        btnConsultar.setDisable(true);

        Ui.enSegundoPlano(() -> {
            Resultado r = new Resultado();
            r.atendidos = cliente.turnosAtendidos(desde, hasta);
            r.porFuncionario = cliente.atencionPorFuncionario(desde, hasta);
            r.porServicio = cliente.atencionPorServicio(desde, hasta);
            return r;
        }, this::mostrar, mensaje -> {
            lblMensaje.setText(mensaje);
            btnConsultar.setDisable(false);
        });
    }

    private void mostrar(Resultado r) {
        lblMensaje.setText("");
        btnConsultar.setDisable(false);

        tblAtendidos.getItems().setAll(r.atendidos);
        lblTotalAtendidos.setText("Atendidos: " + r.atendidos.size());
        lblPromedioEspera.setText("Espera promedio: " + minutos(DetalleAtencion.promedioEspera(r.atendidos)));
        lblPromedioAtencion.setText("Atencion promedio: " + minutos(DetalleAtencion.promedioAtencion(r.atendidos)));

        tblFuncionarios.getItems().setAll(r.porFuncionario);
        llenarGrafico(chartFuncionarios, r.porFuncionario);

        tblServicios.getItems().setAll(r.porServicio);
        llenarGrafico(chartServicios, r.porServicio);
    }

    private void cerrarSesion() {
        Stage ventana = (Stage) btnCerrarSesion.getScene().getWindow();
        ventana.setOnHidden(null);
        cliente.cerrar();
        Navegacion.mostrar(ventana, "Login", "Sistema de turnos - Ingreso");
    }

    private static <T> TableColumn<T, String> columna(String titulo, Function<T, String> valor) {
        TableColumn<T, String> columna = new TableColumn<>(titulo);
        columna.setCellValueFactory(celda -> new ReadOnlyObjectWrapper<>(valor.apply(celda.getValue())));
        return columna;
    }

    private static void llenarGrafico(BarChart<String, Number> grafico, List<ConteoReporte> datos) {
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for (ConteoReporte c : datos) {
            serie.getData().add(new XYChart.Data<>(c.getNombre(), c.getCantidad()));
        }
        grafico.getData().setAll(List.of(serie));
    }

    private static String formato(TemporalAccessor valor, DateTimeFormatter formato) {
        return valor == null ? "-" : formato.format(valor);
    }

    private static String numero(Long valor) {
        return valor == null ? "-" : String.valueOf(valor);
    }

    private static String minutos(Double promedio) {
        return promedio == null ? "-" : String.format("%.1f min", promedio);
    }
}
