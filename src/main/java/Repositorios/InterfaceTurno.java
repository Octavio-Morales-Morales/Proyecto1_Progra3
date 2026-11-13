package Repositorios;

import Dominio.LlamadoTurno;
import Dominio.Turnos;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InterfaceTurno {
    void crearTurno(Turnos turno);

    void ActualizarTurno(int turnoId, int estadoId);

    Turnos ObtenerSiguienteTurno(List<Integer> ServicioId);

    List<Turnos> ObtenerTurnoPorFecha(LocalDate inicio, LocalDate fin);

    Optional<Turnos> buscarPorId(int turnoId);

    Optional<Turnos> buscarPorCodigo(String codigoTurno);

    void asignarVentanilla(int turnoId, Integer ventanillaId);

    List<Turnos> listarPorEstado(int estadoId);

    List<LlamadoTurno> listarUltimosLlamados(LocalDate fecha, int limite);

    boolean tomarTurno(int turnoId, int ventanillaId);

    int contarTurnosDelDia(int servicioId, LocalDate fecha);
}
