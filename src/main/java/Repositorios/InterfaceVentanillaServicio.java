package Repositorios;

import Dominio.VentanillaServicio;
import java.util.List;

public interface InterfaceVentanillaServicio {
    void asignarServicioVentanilla(int ventanilla, int servicioId);

    List<Integer> ObtenerServiciosVentanilla(int ventanillaId);

    void quitarServicioVentanilla(int ventanillaId, int servicioId);

    List<Integer> obtenerVentanillasPorServicio(int servicioId);

    List<VentanillaServicio> listarPorVentanilla(int ventanillaId);
}
