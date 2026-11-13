package Dominio;

public class Rol {
    public static final int ADMINISTRADOR = 1;
    public static final int OPERADOR = 2;

    private int rolId;
    private String nombre;
    private String descripcion;

    public Rol() {
    }

    public Rol(int rolId, String nombre, String descripcion) {
        setRolId(rolId);
        setNombre(nombre);
        setDescripcion(descripcion);
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = Validaciones.idPropio(rolId, "El id del rol");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Validaciones.texto(nombre, "El nombre del rol", 50).toUpperCase();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = Validaciones.textoOpcional(descripcion, "La descripcion del rol", 255);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
