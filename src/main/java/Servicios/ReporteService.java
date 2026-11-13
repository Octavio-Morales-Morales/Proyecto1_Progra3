package Servicios;

import Dominio.ConteoReporte;
import Dominio.DetalleAtencion;
import Repositorios.InterfaceReportes;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ReporteService {
    private final InterfaceReportes reportesDAO;

    public ReporteService(InterfaceReportes reportesDAO) {
        this.reportesDAO = Objects.requireNonNull(reportesDAO, "El repositorio de reportes es obligatorio.");
    }

    public List<DetalleAtencion> turnosAtendidos(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);
        return reportesDAO.turnosAtendidos(desde, hasta);
    }

    public List<ConteoReporte> atencionPorFuncionario(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);
        return reportesDAO.atencionPorFuncionario(desde, hasta);
    }

    public List<ConteoReporte> atencionPorServicio(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);
        return reportesDAO.turnosPorServicio(desde, hasta);
    }

    public Double promedioEspera(List<DetalleAtencion> atenciones) {
        return DetalleAtencion.promedioEspera(atenciones);
    }

    public Double promedioAtencion(List<DetalleAtencion> atenciones) {
        return DetalleAtencion.promedioAtencion(atenciones);
    }

    private static void validarRango(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("Seleccione la fecha inicial y la fecha final.");
        }
        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha inicial no puede ser posterior a la fecha final.");
        }
    }
}
