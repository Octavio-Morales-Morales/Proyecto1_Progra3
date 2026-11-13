package Servicios;

import Dominio.Servicio;
import Repositorios.InterfaceServicio;
import java.util.List;
import java.util.Objects;

public class ServicioService {
    private final InterfaceServicio servicioDAO;

    public ServicioService(InterfaceServicio servicioDAO) {
        this.servicioDAO = Objects.requireNonNull(servicioDAO, "El repositorio de servicios es obligatorio.");
    }

    public List<Servicio> listarDisponibles() {
        return servicioDAO.listarActivos();
    }

    public int prioridadPara(Servicio servicio) {
        String nombre = servicio.getNombre() == null ? "" : servicio.getNombre().toLowerCase();
        return nombre.contains("preferencial") ? 2 : 0;
    }

    public Servicio buscar(int servicioId) {
        return servicioDAO.buscarPorId(servicioId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el servicio " + servicioId));
    }
}
