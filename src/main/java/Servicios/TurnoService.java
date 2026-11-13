package Servicios;

import Dominio.EstadoTurno;
import Dominio.LlamadoTurno;
import Dominio.Turnos;
import Excepciones.RegistroDuplicadoException;
import Repositorios.InterfaceTurno;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class TurnoService {
    private static final int INTENTOS = 5;

    private final InterfaceTurno turnoDAO;

    public TurnoService(InterfaceTurno turnoDAO) {
        this.turnoDAO = Objects.requireNonNull(turnoDAO, "El repositorio de turnos es obligatorio.");
    }

    public synchronized Turnos generarNuevoTurno(String letraServicio, int servicioId, int prioridad) {
        if (letraServicio == null || letraServicio.trim().isEmpty()) {
            throw new IllegalArgumentException("La letra identificadora del servicio es obligatoria.");
        }
        if (servicioId <= 0) {
            throw new IllegalArgumentException("El ID del servicio debe ser un número positivo válido.");
        }
        String prefijo = letraServicio.trim().toUpperCase();
        if (prefijo.length() > 10) {
            throw new IllegalArgumentException("El codigo del servicio es demasiado largo para formar el turno.");
        }

        for (int intento = 0; intento < INTENTOS; intento++) {
            LocalDate hoy = LocalDate.now();
            int siguiente = turnoDAO.contarTurnosDelDia(servicioId, hoy) + 1;
            Turnos nuevo = new Turnos(0, String.format("%s%03d", prefijo, siguiente), servicioId,
                    hoy, LocalTime.now().truncatedTo(ChronoUnit.SECONDS),
                    EstadoTurno.EN_ESPERA, prioridad, null);
            try {
                turnoDAO.crearTurno(nuevo);
                return nuevo;
            } catch (RegistroDuplicadoException e) {
            }
        }
        throw new IllegalStateException("No se pudo generar el turno. Intente de nuevo.");
    }

    public synchronized Turnos tomarSiguiente(List<Integer> serviciosPermitidos, int ventanillaId) {
        if (serviciosPermitidos == null || serviciosPermitidos.isEmpty()) {
            throw new IllegalArgumentException("La ventanilla debe tener al menos un servicio autorizado.");
        }
        for (int intento = 0; intento < INTENTOS; intento++) {
            Turnos siguiente = turnoDAO.ObtenerSiguienteTurno(serviciosPermitidos);
            if (siguiente == null) {
                throw new IllegalStateException("No existen turnos pendientes en la fila para tus servicios.");
            }
            if (turnoDAO.tomarTurno(siguiente.getTurnoId(), ventanillaId)) {
                siguiente.setEstadoId(EstadoTurno.LLAMADO);
                siguiente.setVentanillaId(ventanillaId);
                return siguiente;
            }
        }
        throw new IllegalStateException("Hay muchas ventanillas llamando a la vez. Intente de nuevo.");
    }

    public void cambiarEstadoTurno(Turnos turno, int nuevoEstadoId) {
        if (turno == null) {
            throw new IllegalArgumentException("El objeto turno no puede ser nulo.");
        }

        int estadoActual = turno.getEstadoId();

        if (turno.estaCerrado()) {
            throw new IllegalStateException("Un turno finalizado o cancelado no puede volver a asignarse ni modificarse.");
        }
        if (nuevoEstadoId == EstadoTurno.EN_ATENCION && estadoActual != EstadoTurno.LLAMADO) {
            throw new IllegalStateException("El turno " + turno.getCodigoTurno() + " debe ser llamado antes de ser atendido.");
        }
        if (nuevoEstadoId == EstadoTurno.FINALIZADO && estadoActual != EstadoTurno.EN_ATENCION) {
            throw new IllegalStateException("No se puede finalizar un turno que no está en proceso de atención.");
        }
        turnoDAO.ActualizarTurno(turno.getTurnoId(), nuevoEstadoId);
        turno.setEstadoId(nuevoEstadoId);
    }

    public Turnos buscarTurno(int turnoId) {
        return turnoDAO.buscarPorId(turnoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el turno " + turnoId));
    }

    public List<LlamadoTurno> ultimosLlamadosDeHoy(int limite) {
        return turnoDAO.listarUltimosLlamados(LocalDate.now(), limite);
    }
}
