package Servicios;

import Dominio.Atencion;
import Dominio.DetalleAtencion;
import Dominio.EstadoTurno;
import Dominio.LlamadoTurno;
import Dominio.Servicio;
import Dominio.Turnos;
import Dominio.TurnoEnVentanilla;
import Dominio.Ventanillas;
import Repositorios.InterfaceReportes;
import Repositorios.InterfaceServicio;
import Repositorios.InterfaceVentanilla;
import Repositorios.InterfaceVentanillaServicio;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class OperacionVentanillaService {
    private final TurnoService turnoService;
    private final AtencionService atencionService;
    private final InterfaceVentanilla ventanillaDAO;
    private final InterfaceVentanillaServicio ventanillaServicioDAO;
    private final InterfaceServicio servicioDAO;
    private final InterfaceReportes reportesDAO;

    private final List<ObservadorLlamados> observadores = new CopyOnWriteArrayList<>();

    public OperacionVentanillaService(TurnoService turnoService, AtencionService atencionService,
                                      InterfaceVentanilla ventanillaDAO, InterfaceVentanillaServicio ventanillaServicioDAO,
                                      InterfaceServicio servicioDAO, InterfaceReportes reportesDAO) {
        this.turnoService = Objects.requireNonNull(turnoService);
        this.atencionService = Objects.requireNonNull(atencionService);
        this.ventanillaDAO = Objects.requireNonNull(ventanillaDAO);
        this.ventanillaServicioDAO = Objects.requireNonNull(ventanillaServicioDAO);
        this.servicioDAO = Objects.requireNonNull(servicioDAO);
        this.reportesDAO = Objects.requireNonNull(reportesDAO);
    }

    public void agregarObservador(ObservadorLlamados observador) {
        observadores.add(Objects.requireNonNull(observador));
    }

    public void quitarObservador(ObservadorLlamados observador) {
        observadores.remove(observador);
    }

    private void notificar(LlamadoTurno llamado) {
        for (ObservadorLlamados o : observadores) {
            try {
                o.alLlamarTurno(llamado);
            } catch (RuntimeException e) {
            }
        }
    }

    public List<Ventanillas> ventanillasActivas() {
        List<Ventanillas> activas = new ArrayList<>();
        for (Ventanillas v : ventanillaDAO.listar()) {
            if (v.isActivo()) {
                activas.add(v);
            }
        }
        return activas;
    }

    public Ventanillas consultarVentanilla(int ventanillaId) {
        return ventanillaDAO.buscarPorId(ventanillaId)
                .orElseThrow(() -> new IllegalArgumentException("No existe la ventanilla " + ventanillaId));
    }

    public void marcarNoDisponible(int ventanillaId) {
        if (atencionService.atencionActiva(ventanillaId).isPresent()) {
            throw new IllegalStateException("Finalice el turno actual antes de marcar la ventanilla como no disponible.");
        }
        ventanillaDAO.cambiarEstado(ventanillaId, Ventanillas.NO_DISPONIBLE);
    }

    public void marcarDisponible(int ventanillaId) {
        Ventanillas v = consultarVentanilla(ventanillaId);
        if (!v.isActivo()) {
            throw new IllegalStateException("La ventanilla está inactiva. Solo el administrador puede activarla.");
        }
        ventanillaDAO.cambiarEstado(ventanillaId, Ventanillas.DISPONIBLE);
    }

    public Optional<TurnoEnVentanilla> turnoActual(int ventanillaId) {
        Optional<Atencion> activa = atencionService.atencionActiva(ventanillaId);
        if (activa.isEmpty()) {
            return Optional.empty();
        }
        Turnos turno = turnoService.buscarTurno(activa.get().getTurnoId());
        return Optional.of(armar(activa.get(), turno));
    }

    public synchronized TurnoEnVentanilla llamarSiguiente(int ventanillaId, int funcionarioId) {
        Ventanillas ventanilla = consultarVentanilla(ventanillaId);
        if (!ventanilla.isActivo()) {
            throw new IllegalStateException("La ventanilla " + ventanilla.getNumero() + " está inactiva.");
        }
        if (Ventanillas.NO_DISPONIBLE.equals(ventanilla.getEstado())) {
            throw new IllegalStateException("La ventanilla está marcada como no disponible.");
        }
        if (atencionService.atencionActiva(ventanillaId).isPresent()) {
            throw new IllegalStateException("La ventanilla ya tiene un turno. Finalícelo antes de llamar otro.");
        }
        List<Integer> servicios = ventanillaServicioDAO.ObtenerServiciosVentanilla(ventanillaId);
        if (servicios.isEmpty()) {
            throw new IllegalStateException("La ventanilla no tiene servicios asignados. Contacte al administrador.");
        }

        Turnos turno = turnoService.tomarSiguiente(servicios, ventanillaId);
        Atencion atencion = atencionService.llamarCliente(turno.getTurnoId(), ventanillaId, funcionarioId);
        ventanillaDAO.cambiarEstado(ventanillaId, Ventanillas.OCUPADA);

        notificar(new LlamadoTurno(turno.getCodigoTurno(), ventanilla.getNumero(), atencion.getHoraLlamado()));
        return armar(atencion, turno);
    }

    public synchronized TurnoEnVentanilla iniciarAtencion(int ventanillaId) {
        Atencion atencion = atencionActivaObligatoria(ventanillaId);
        Turnos turno = turnoService.buscarTurno(atencion.getTurnoId());
        turnoService.cambiarEstadoTurno(turno, EstadoTurno.EN_ATENCION);
        atencionService.comenzarTramite(atencion.getAtencionId());
        return armar(atencion, turno);
    }

    public synchronized void finalizarAtencion(int ventanillaId, String observaciones) {
        Atencion atencion = atencionActivaObligatoria(ventanillaId);
        Turnos turno = turnoService.buscarTurno(atencion.getTurnoId());
        if (turno.getEstadoId() == EstadoTurno.LLAMADO) {
            throw new IllegalStateException("Primero inicie la atención del turno " + turno.getCodigoTurno() + ".");
        }
        turnoService.cambiarEstadoTurno(turno, EstadoTurno.FINALIZADO);
        atencionService.terminarTramite(atencion.getAtencionId(), observaciones);
        ventanillaDAO.cambiarEstado(ventanillaId, Ventanillas.DISPONIBLE);
    }

    public void volverALlamar(int ventanillaId) {
        Atencion atencion = atencionActivaObligatoria(ventanillaId);
        Turnos turno = turnoService.buscarTurno(atencion.getTurnoId());
        if (turno.getEstadoId() != EstadoTurno.LLAMADO) {
            throw new IllegalStateException("Solo se puede volver a llamar un turno que todavía no se está atendiendo.");
        }
        Ventanillas ventanilla = consultarVentanilla(ventanillaId);
        notificar(new LlamadoTurno(turno.getCodigoTurno(), ventanilla.getNumero(), java.time.LocalDateTime.now()));
    }

    public List<DetalleAtencion> historialVentanilla(int ventanillaId, int limite) {
        return reportesDAO.historialVentanilla(ventanillaId, limite);
    }

    public List<LlamadoTurno> ultimosLlamados(int limite) {
        return turnoService.ultimosLlamadosDeHoy(limite);
    }

    private Atencion atencionActivaObligatoria(int ventanillaId) {
        return atencionService.atencionActiva(ventanillaId)
                .orElseThrow(() -> new IllegalStateException("La ventanilla no tiene ningún turno en este momento."));
    }

    private TurnoEnVentanilla armar(Atencion atencion, Turnos turno) {
        String nombreServicio = servicioDAO.buscarPorId(turno.getServicioId())
                .map(Servicio::getNombre).orElse("-");
        return new TurnoEnVentanilla(atencion.getAtencionId(), turno.getTurnoId(), turno.getCodigoTurno(),
                nombreServicio, turno.getEstadoId());
    }
}
