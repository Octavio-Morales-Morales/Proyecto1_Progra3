
package controladores;

public class Sesion {
    private static int usuarioId;
    private static String nombre;
    private static int rolId;

    public static void setUsuarioActual(int id, String nom, int rol) {
        usuarioId = id;
        nombre = nom;
        rolId = rol;
    }

    public static int getUsuarioId() { return usuarioId; }
    public static String getNombre() { return nombre; }
    public static int getRolId() { return rolId; }

    public static void cerrar() {
        usuarioId = 0;
        nombre = null;
        rolId = 0;
    }
}