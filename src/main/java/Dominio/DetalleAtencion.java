package Dominio;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

public class DetalleAtencion {
    private final String codigoTurno;
    private final String servicio;
    private final LocalDate fechaGeneracion;
    private final LocalTime horaGeneracion;
    private final LocalDateTime horaLlamado;
    private final LocalDateTime horaInicio;
    private final LocalDateTime horaFinalizacion;
    private final int numeroVentanilla;
    private final String funcionario;
    private final String estadoFinal;

    public DetalleAtencion(String codigoTurno, String servicio, LocalDate fechaGeneracion, LocalTime horaGeneracion,
                           LocalDateTime horaLlamado, LocalDateTime horaInicio, LocalDateTime horaFinalizacion,
                           int numeroVentanilla, String funcionario, String estadoFinal) {
        this.codigoTurno = codigoTurno;
        this.servicio = servicio;
        this.fechaGeneracion = fechaGeneracion;
        this.horaGeneracion = horaGeneracion;
        this.horaLlamado = horaLlamado;
        this.horaInicio = horaInicio;
        this.horaFinalizacion = horaFinalizacion;
        this.numeroVentanilla = numeroVentanilla;
        this.funcionario = funcionario;
        this.estadoFinal = estadoFinal;
    }

    public String getCodigoTurno() { return codigoTurno; }
    public String getServicio() { return servicio; }
    public LocalDate getFechaGeneracion() { return fechaGeneracion; }
    public LocalTime getHoraGeneracion() { return horaGeneracion; }
    public LocalDateTime getHoraLlamado() { return horaLlamado; }
    public LocalDateTime getHoraInicio() { return horaInicio; }
    public LocalDateTime getHoraFinalizacion() { return horaFinalizacion; }
    public int getNumeroVentanilla() { return numeroVentanilla; }
    public String getFuncionario() { return funcionario; }
    public String getEstadoFinal() { return estadoFinal; }

    public Long getMinutosEspera() {
        if (fechaGeneracion == null || horaGeneracion == null || horaInicio == null) {
            return null;
        }
        return Duration.between(LocalDateTime.of(fechaGeneracion, horaGeneracion), horaInicio).toMinutes();
    }

    public Long getMinutosAtencion() {
        if (horaInicio == null || horaFinalizacion == null) {
            return null;
        }
        return Duration.between(horaInicio, horaFinalizacion).toMinutes();
    }

    public static Double promedioEspera(List<DetalleAtencion> atenciones) {
        return promedio(atenciones, DetalleAtencion::getMinutosEspera);
    }

    public static Double promedioAtencion(List<DetalleAtencion> atenciones) {
        return promedio(atenciones, DetalleAtencion::getMinutosAtencion);
    }

    private static Double promedio(List<DetalleAtencion> atenciones, Function<DetalleAtencion, Long> minutos) {
        if (atenciones == null) {
            return null;
        }
        long suma = 0;
        int cantidad = 0;
        for (DetalleAtencion d : atenciones) {
            Long m = minutos.apply(d);
            if (m != null) {
                suma += m;
                cantidad++;
            }
        }
        return cantidad == 0 ? null : (double) suma / cantidad;
    }
}
