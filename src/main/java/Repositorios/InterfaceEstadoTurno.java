package Repositorios;

import Dominio.EstadoTurno;
import java.util.List;
import java.util.Optional;

public interface InterfaceEstadoTurno {
    String obtenerEstado(int estadoId);

    Optional<EstadoTurno> buscarPorNombre(String nombre);

    List<EstadoTurno> listar();
}
