package Repositorios;

import java.util.List;
import java.util.Optional;

public interface InterfaceTurnoTTrabajo {
    void asociar(int turnoTrabajoId, int turnoId);

    Optional<Integer> obtenerTurnoTrabajoDeTurno(int turnoId);

    List<Integer> listarTurnosDeTurnoTrabajo(int turnoTrabajoId);
}
