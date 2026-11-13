package Dominio;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turnos {
    private int turnoId;
    private String codigoTurno;
    private int servicioId;
    private LocalDate generacion;
    private LocalTime horaGeneracion;
    private int estadoId = EstadoTurno.EN_ESPERA;
    private int prioridad;
    private Integer ventanillaId;

    public Turnos() {
    }

    public Turnos(int turnoId, String codigoTurno, int servicioId, LocalDate generacion,
                  LocalTime horaGeneracion, int estadoId, int prioridad, Integer ventanillaId) {
        setTurnoId(turnoId);
        setCodigoTurno(codigoTurno);
        setServicioId(servicioId);
        setGeneracion(generacion);
        sethoraGeneracion(horaGeneracion);
        setEstadoId(estadoId);
        setPrioridad(prioridad);
        setVentanillaId(ventanillaId);
    }

    public int getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(int turnoId) {
        this.turnoId = Validaciones.idPropio(turnoId, "El id del turno");
    }

    public String getCodigoTurno() {
        return codigoTurno;
    }

    public void setCodigoTurno(String codigoTurno) {
        String limpio = Validaciones.texto(codigoTurno, "El codigo del turno", 20).toUpperCase();
        if (limpio.contains(" ")) {
            throw new IllegalArgumentException("El codigo del turno no puede tener espacios.");
        }
        this.codigoTurno = limpio;
    }

    public int getServicioId() {
        return servicioId;
    }

    public void setServicioId(int servicioId) {
        this.servicioId = Validaciones.idPositivo(servicioId, "El servicio del turno");
    }

    public LocalDate getGeneracion() {
        return generacion;
    }

    public void setGeneracion(LocalDate generacion) {
        this.generacion = generacion;
    }

    public LocalTime getHoraGeneracion() {
        return horaGeneracion;
    }

    public void sethoraGeneracion(LocalTime horaGeneracion) {
        this.horaGeneracion = horaGeneracion;
    }

    public void setHoraGeneracion(LocalTime horaGeneracion) {
        sethoraGeneracion(horaGeneracion);
    }

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        if (estadoId < EstadoTurno.EN_ESPERA || estadoId > EstadoTurno.CANCELADO) {
            throw new IllegalArgumentException("El estado del turno no es valido: " + estadoId);
        }
        this.estadoId = estadoId;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        if (prioridad < 0) {
            throw new IllegalArgumentException("La prioridad no puede ser negativa.");
        }
        this.prioridad = prioridad;
    }

    public Integer getVentanillaId() {
        return ventanillaId;
    }

    public void setVentanillaId(Integer ventanillaId) {
        this.ventanillaId = Validaciones.idOpcional(ventanillaId, "La ventanilla del turno");
    }

    public boolean estaEnEspera() {
        return estadoId == EstadoTurno.EN_ESPERA;
    }

    public boolean estaCerrado() {
        return estadoId == EstadoTurno.FINALIZADO || estadoId == EstadoTurno.CANCELADO;
    }

    @Override
    public String toString() {
        return codigoTurno;
    }
}
