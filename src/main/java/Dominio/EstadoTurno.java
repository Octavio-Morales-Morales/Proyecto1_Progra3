package Dominio;

public class EstadoTurno {
    public static final int EN_ESPERA = 1;
    public static final int LLAMADO = 2;
    public static final int EN_ATENCION = 3;
    public static final int FINALIZADO = 4;
    public static final int CANCELADO = 5;

    private int estadoId;
    private String nombre;

    public EstadoTurno() {
    }

    public EstadoTurno(int estadoId, String nombre) {
        setEstadoId(estadoId);
        setNombre(nombre);
    }

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = Validaciones.idPropio(estadoId, "El id del estado");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Validaciones.texto(nombre, "El nombre del estado", 30).toUpperCase();
    }

    @Override
    public String toString() {
        return nombre;
    }
}
