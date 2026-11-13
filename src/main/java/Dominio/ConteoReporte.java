package Dominio;

public class ConteoReporte {
    private final String nombre;
    private final int cantidad;

    public ConteoReporte(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }
}
