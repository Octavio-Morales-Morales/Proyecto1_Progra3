package Repositorios;

import Dominio.Servicio;
import java.util.List;
import java.util.Optional;

public interface InterfaceServicio {
    List<String> ListarServiciosDisponibles();

    int obtenerNombreServicio(String nombreServicio);

    void crear(Servicio servicio);

    void actualizar(Servicio servicio);

    Optional<Servicio> buscarPorId(int servicioId);

    Optional<Servicio> buscarPorCodigo(String codigo);

    List<Servicio> listarActivos();

    List<Servicio> listarTodos();
}
