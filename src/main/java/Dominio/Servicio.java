package Dominio;

public class Servicio {
    private int servicioId;
    private String codigo;
    private String nombre;
    private String descripcion;
    private boolean activo = true;

    public Servicio() {
    }

    public Servicio(int servicioId, String codigo, String nombre, String descripcion, boolean activo) {
        setServicioId(servicioId);
        setCodigo(codigo);
        setNombre(nombre);
        setDescripcion(descripcion);
        setActivo(activo);
    }

    public int getServicioId() {
        return servicioId;
    }

    public void setServicioId(int servicioId) {
        this.servicioId = Validaciones.idPropio(servicioId, "El id del servicio");
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        String limpio = Validaciones.texto(codigo, "El codigo del servicio", 50).toUpperCase();
        if (limpio.contains(" ")) {
            throw new IllegalArgumentException("El codigo del servicio no puede tener espacios.");
        }
        this.codigo = limpio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Validaciones.texto(nombre, "El nombre del servicio", 100);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = Validaciones.textoOpcional(descripcion, "La descripcion del servicio", 255);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
