package Dominio;

import java.time.LocalDateTime;

public class Usuarios {
    private int usuarioId;
    private String nombre;
    private String usuario;
    private String contraseña;
    private int rolId;
    private boolean activo = true;
    private LocalDateTime fechaCreacion;

    public Usuarios() {
    }

    public Usuarios(int usuarioId, String nombre, String usuario, String contraseña, int rolId,
                    boolean activo, LocalDateTime fechaCreacion) {
        setUsuarioId(usuarioId);
        setNombre(nombre);
        setUsuario(usuario);
        setContraseña(contraseña);
        setRolId(rolId);
        setActivoUsuario(activo);
        setFechaCreacion(fechaCreacion);
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = Validaciones.idPropio(usuarioId, "El id del usuario");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Validaciones.texto(nombre, "El nombre", 100);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        String limpio = Validaciones.texto(usuario, "El nombre de usuario", 50);
        if (limpio.contains(" ")) {
            throw new IllegalArgumentException("El nombre de usuario no puede tener espacios.");
        }
        this.usuario = limpio;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        if (contraseña == null || contraseña.isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if (contraseña.length() > 255) {
            throw new IllegalArgumentException("La contraseña no puede superar los 255 caracteres.");
        }
        this.contraseña = contraseña;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = Validaciones.idPositivo(rolId, "El rol");
    }

    public boolean isActivoUsuario() {
        return activo;
    }

    public void setActivoUsuario(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean esAdministrador() {
        return rolId == Rol.ADMINISTRADOR;
    }

    @Override
    public String toString() {
        return usuario + " (" + nombre + ")";
    }
}
