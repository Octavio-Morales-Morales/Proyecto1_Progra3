package Repositorios;

import Dominio.ConteoReporte;
import Dominio.DetalleAtencion;
import java.time.LocalDate;
import java.util.List;

public interface InterfaceReportes {
    List<DetalleAtencion> turnosAtendidos(LocalDate desde, LocalDate hasta);

    List<ConteoReporte> atencionPorFuncionario(LocalDate desde, LocalDate hasta);

    List<ConteoReporte> turnosPorServicio(LocalDate desde, LocalDate hasta);

    List<DetalleAtencion> historialVentanilla(int ventanillaId, int limite);
}
