package Dominio;

import java.time.LocalDateTime;

public class Atencion {
    private int atencionId;
    private int turnoId;
    private int ventanillaId;
    private int funcionarioId;
    private LocalDateTime horaLlamado;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFinalizacion;
    private String observaciones;

    public Atencion() {
    }

    public Atencion(int atencionId, int funcionarioId, LocalDateTime horaFinalizacion, LocalDateTime horaInicio,
                    LocalDateTime horaLlamado, String observaciones, int turnoId, int ventanillaId) {
        setAtencionId(atencionId);
        setFuncionarioId(funcionarioId);
        setTurnoId(turnoId);
        setVentanillaId(ventanillaId);
        setObservaciones(observaciones);
        validarHoras(horaLlamado, horaInicio, horaFinalizacion);
        this.horaLlamado = horaLlamado;
        this.horaInicio = horaInicio;
        this.horaFinalizacion = horaFinalizacion;
    }

    public int getAtencionId() {
        return atencionId;
    }

    public void setAtencionId(int atencionId) {
        this.atencionId = Validaciones.idPropio(atencionId, "El id de la atencion");
    }

    public int getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(int turnoId) {
        this.turnoId = Validaciones.idPositivo(turnoId, "El turno");
    }

    public int getVentanillaId() {
        return ventanillaId;
    }

    public void setVentanillaId(int ventanillaId) {
        this.ventanillaId = Validaciones.idPositivo(ventanillaId, "La ventanilla");
    }

    public int getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(int funcionarioId) {
        this.funcionarioId = Validaciones.idPositivo(funcionarioId, "El funcionario");
    }

    public LocalDateTime getHoraLlamado() {
        return horaLlamado;
    }

    public void setHoraLlamado(LocalDateTime horaLlamado) {
        validarHoras(horaLlamado, horaInicio, horaFinalizacion);
        this.horaLlamado = horaLlamado;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        validarHoras(horaLlamado, horaInicio, horaFinalizacion);
        this.horaInicio = horaInicio;
    }

    public LocalDateTime getHoraFinalizacion() {
        return horaFinalizacion;
    }

    public void setHoraFinalizacion(LocalDateTime horaFinalizacion) {
        validarHoras(horaLlamado, horaInicio, horaFinalizacion);
        this.horaFinalizacion = horaFinalizacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = Validaciones.textoOpcional(observaciones, "Las observaciones", 65535);
    }

    public boolean estaFinalizada() {
        return horaFinalizacion != null;
    }

    public boolean estaEnCurso() {
        return horaInicio != null && horaFinalizacion == null;
    }

    private static void validarHoras(LocalDateTime llamado, LocalDateTime inicio, LocalDateTime fin) {
        if (inicio != null && llamado != null && inicio.isBefore(llamado)) {
            throw new IllegalArgumentException("La hora de inicio no puede ser anterior a la hora de llamado.");
        }
        if (fin != null && inicio == null) {
            throw new IllegalArgumentException("No se puede finalizar una atencion que no ha iniciado.");
        }
        if (fin != null && fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La hora de finalizacion no puede ser anterior a la hora de inicio.");
        }
    }
}
