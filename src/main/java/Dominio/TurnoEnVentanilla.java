package Dominio;

public class TurnoEnVentanilla {
    private final int atencionId;
    private final int turnoId;
    private final String codigoTurno;
    private final String nombreServicio;
    private final int estadoId;

    public TurnoEnVentanilla(int atencionId, int turnoId, String codigoTurno, String nombreServicio, int estadoId) {
        this.atencionId = Validaciones.idPositivo(atencionId, "La atencion");
        this.turnoId = Validaciones.idPositivo(turnoId, "El turno");
        this.codigoTurno = Validaciones.texto(codigoTurno, "El codigo del turno", 20);
        this.nombreServicio = nombreServicio;
        this.estadoId = estadoId;
    }

    public int getAtencionId() {
        return atencionId;
    }

    public int getTurnoId() {
        return turnoId;
    }

    public String getCodigoTurno() {
        return codigoTurno;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public boolean estaLlamado() {
        return estadoId == EstadoTurno.LLAMADO;
    }

    public boolean estaEnAtencion() {
        return estadoId == EstadoTurno.EN_ATENCION;
    }
}
