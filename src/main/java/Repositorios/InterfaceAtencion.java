package Repositorios;

import Dominio.Atencion;
import java.util.List;
import java.util.Optional;

public interface InterfaceAtencion {
    void registrarAtencion(Atencion atencion);

    void registrarInicio(int atencionId);

    void registrarFin(int atencionId, String observaciones);

    int AtendidosPorFuncionarios(int funcionarioId);

    int TurnosPorServicio(int servicioId);

    double PromedioDeEspera();

    double PromedioAtencio();

    Optional<Atencion> buscarPorId(int atencionId);

    Optional<Atencion> buscarUltimaPorTurno(int turnoId);

    Optional<Atencion> buscarActivaPorVentanilla(int ventanillaId);

    List<Atencion> listarPorFuncionario(int funcionarioId);
}
