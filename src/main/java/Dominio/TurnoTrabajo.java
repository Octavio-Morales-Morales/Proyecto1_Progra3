package Dominio;

import java.time.LocalDateTime;

public class TurnoTrabajo {
    private int turnoTrabajoId;
    private Integer funcionarioId;
    private LocalDateTime turnoEntrada;
    private LocalDateTime turnoSalida;

    public TurnoTrabajo() {
    }

    public TurnoTrabajo(int turnoTrabajoId, Integer funcionarioId, LocalDateTime turnoEntrada, LocalDateTime turnoSalida) {
        setTurnoTrabajoId(turnoTrabajoId);
        setFuncionarioId(funcionarioId);
        setHorario(turnoEntrada, turnoSalida);
    }

    public int getTurnoTrabajoId() {
        return turnoTrabajoId;
    }

    public void setTurnoTrabajoId(int turnoTrabajoId) {
        this.turnoTrabajoId = Validaciones.idPropio(turnoTrabajoId, "El id del turno de trabajo");
    }

    public Integer getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Integer funcionarioId) {
        this.funcionarioId = Validaciones.idOpcional(funcionarioId, "El funcionario");
    }

    public LocalDateTime getTurnoEntrada() {
        return turnoEntrada;
    }

    public LocalDateTime getTurnoSalida() {
        return turnoSalida;
    }

    public void setHorario(LocalDateTime turnoEntrada, LocalDateTime turnoSalida) {
        Validaciones.requerido(turnoEntrada, "La hora de entrada");
        Validaciones.requerido(turnoSalida, "La hora de salida");
        if (!turnoSalida.isAfter(turnoEntrada)) {
            throw new IllegalArgumentException("La hora de salida debe ser posterior a la hora de entrada.");
        }
        this.turnoEntrada = turnoEntrada;
        this.turnoSalida = turnoSalida;
    }

    public boolean incluye(LocalDateTime momento) {
        return momento != null && !momento.isBefore(turnoEntrada) && !momento.isAfter(turnoSalida);
    }
}
