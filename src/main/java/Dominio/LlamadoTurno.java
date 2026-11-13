package Dominio;

import java.time.LocalDateTime;

public class LlamadoTurno {
    private final String codigoTurno;
    private final int numeroVentanilla;
    private final LocalDateTime horaLlamado;

    public LlamadoTurno(String codigoTurno, int numeroVentanilla, LocalDateTime horaLlamado) {
        this.codigoTurno = Validaciones.texto(codigoTurno, "El codigo del turno", 20);
        if (numeroVentanilla <= 0) {
            throw new IllegalArgumentException("El numero de ventanilla debe ser mayor que cero.");
        }
        this.numeroVentanilla = numeroVentanilla;
        this.horaLlamado = Validaciones.requerido(horaLlamado, "La hora de llamado");
    }

    public String getCodigoTurno() {
        return codigoTurno;
    }

    public int getNumeroVentanilla() {
        return numeroVentanilla;
    }

    public LocalDateTime getHoraLlamado() {
        return horaLlamado;
    }

    @Override
    public String toString() {
        return codigoTurno + "  ->  Ventanilla " + numeroVentanilla;
    }
}
