package Servicios;

import Dominio.Atencion;
import Repositorios.InterfaceAtencion;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

public class AtencionService {
    private final InterfaceAtencion atencionDAO;

    public AtencionService(InterfaceAtencion atencionDAO) {
        this.atencionDAO = Objects.requireNonNull(atencionDAO, "El repositorio de atenciones es obligatorio.");
    }

    public Atencion llamarCliente(int turnoId, int ventanillaId, int funcionarioId) {
        if (turnoId <= 0) {
            throw new IllegalArgumentException("El ID del turno debe ser un número positivo válido.");
        }
        if (ventanillaId <= 0) {
            throw new IllegalArgumentException("El ID de la ventanilla debe ser un número positivo válido.");
        }
        if (funcionarioId <= 0) {
            throw new IllegalArgumentException("El ID del funcionario debe ser un número positivo válido.");
        }

        Atencion atencion = new Atencion();
        atencion.setTurnoId(turnoId);
        atencion.setVentanillaId(ventanillaId);
        atencion.setFuncionarioId(funcionarioId);
        atencion.setHoraLlamado(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        atencionDAO.registrarAtencion(atencion);
        return atencion;
    }

    public void comenzarTramite(int atencionId) {
        if (atencionId <= 0) {
            throw new IllegalArgumentException("El ID de la atención a iniciar no es válido.");
        }
        atencionDAO.registrarInicio(atencionId);
    }

    public void terminarTramite(int atencionId, String notas) {
        if (atencionId <= 0) {
            throw new IllegalArgumentException("El ID de la atención a finalizar no es válido.");
        }
        String observaciones = (notas == null || notas.trim().isEmpty()) ? "Sin observaciones" : notas;
        atencionDAO.registrarFin(atencionId, observaciones);
    }

    public Optional<Atencion> atencionActiva(int ventanillaId) {
        return atencionDAO.buscarActivaPorVentanilla(ventanillaId);
    }
}
