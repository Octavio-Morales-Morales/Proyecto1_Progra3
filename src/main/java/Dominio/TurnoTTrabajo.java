package Dominio;

public class TurnoTTrabajo {
    private int turnoTrabajoId;
    private int turnoId;

    public TurnoTTrabajo() {
    }

    public TurnoTTrabajo(int turnoTrabajoId, int turnoId) {
        setTurnoTrabajoId(turnoTrabajoId);
        setTurnoId(turnoId);
    }

    public int getTurnoTrabajoId() {
        return turnoTrabajoId;
    }

    public void setTurnoTrabajoId(int turnoTrabajoId) {
        this.turnoTrabajoId = Validaciones.idPositivo(turnoTrabajoId, "El turno de trabajo");
    }

    public int getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(int turnoId) {
        this.turnoId = Validaciones.idPositivo(turnoId, "El turno");
    }
}
