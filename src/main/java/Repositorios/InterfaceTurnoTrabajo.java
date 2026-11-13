package Repositorios;

import Dominio.TurnoTrabajo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InterfaceTurnoTrabajo {
    void crear(TurnoTrabajo turnoTrabajo);

    void actualizar(TurnoTrabajo turnoTrabajo);

    Optional<TurnoTrabajo> buscarPorId(int turnoTrabajoId);

    Optional<TurnoTrabajo> buscarVigente(int funcionarioId, LocalDateTime momento);

    List<TurnoTrabajo> listarPorFuncionario(int funcionarioId);

    boolean existeTraslape(int funcionarioId, LocalDateTime entrada, LocalDateTime salida, int excluirId);
}
