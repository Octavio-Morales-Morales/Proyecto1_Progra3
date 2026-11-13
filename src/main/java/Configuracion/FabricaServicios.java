package Configuracion;

import DataAccesObject.AtencionDAO;
import DataAccesObject.FuncionariosDAO;
import DataAccesObject.ReporteDAO;
import DataAccesObject.ServicioDAO;
import DataAccesObject.TurnoDAO;
import DataAccesObject.UsuariosDAO;
import DataAccesObject.VentanillaDAO;
import DataAccesObject.VentanillaServicioDAO;
import Servicios.AtencionService;
import Servicios.OperacionVentanillaService;
import Servicios.ReporteService;
import Servicios.ServicioService;
import Servicios.SesionService;
import Servicios.TurnoService;
import Servicios.UsuarioService;

public final class FabricaServicios {
    private static FabricaServicios instancia;

    private final TurnoService turnoService;
    private final ServicioService servicioService;
    private final SesionService sesionService;
    private final OperacionVentanillaService operacionVentanillaService;
    private final ReporteService reporteService;

    private FabricaServicios() {
        TurnoDAO turnoDAO = new TurnoDAO();
        AtencionDAO atencionDAO = new AtencionDAO();
        ServicioDAO servicioDAO = new ServicioDAO();
        VentanillaDAO ventanillaDAO = new VentanillaDAO();
        VentanillaServicioDAO ventanillaServicioDAO = new VentanillaServicioDAO();
        UsuariosDAO usuariosDAO = new UsuariosDAO();
        FuncionariosDAO funcionariosDAO = new FuncionariosDAO();
        ReporteDAO reporteDAO = new ReporteDAO();

        turnoService = new TurnoService(turnoDAO);
        servicioService = new ServicioService(servicioDAO);
        sesionService = new SesionService(new UsuarioService(usuariosDAO), funcionariosDAO);
        operacionVentanillaService = new OperacionVentanillaService(turnoService, new AtencionService(atencionDAO),
                ventanillaDAO, ventanillaServicioDAO, servicioDAO, reporteDAO);
        reporteService = new ReporteService(reporteDAO);
    }

    private FabricaServicios(TurnoService turnos, ServicioService servicios, SesionService sesion,
                             OperacionVentanillaService ventanillas, ReporteService reportes) {
        this.turnoService = turnos;
        this.servicioService = servicios;
        this.sesionService = sesion;
        this.operacionVentanillaService = ventanillas;
        this.reporteService = reportes;
    }

    public static FabricaServicios con(TurnoService turnos, ServicioService servicios, SesionService sesion,
                                       OperacionVentanillaService ventanillas, ReporteService reportes) {
        return new FabricaServicios(turnos, servicios, sesion, ventanillas, reportes);
    }

    public static synchronized FabricaServicios get() {
        if (instancia == null) {
            instancia = new FabricaServicios();
        }
        return instancia;
    }

    public TurnoService turnos() {
        return turnoService;
    }

    public ServicioService servicios() {
        return servicioService;
    }

    public SesionService sesion() {
        return sesionService;
    }

    public OperacionVentanillaService ventanillas() {
        return operacionVentanillaService;
    }

    public ReporteService reportes() {
        return reporteService;
    }
}
