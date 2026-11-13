package Dominio;

public class Funcionarios {
    private int funcionarioId;
    private int usuarioId;
    private String nombreCompleto;
    private String identificacion;
    private boolean activo = true;

    public Funcionarios() {
    }

    public Funcionarios(int funcionarioId, int usuarioId, String nombreCompleto, String identificacion, boolean activo) {
        setFuncionarioId(funcionarioId);
        setUsuarioId(usuarioId);
        setNombreCompleto(nombreCompleto);
        setIdentificacion(identificacion);
        setActivo(activo);
    }

    public int getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(int funcionarioId) {
        this.funcionarioId = Validaciones.idPropio(funcionarioId, "El id del funcionario");
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = Validaciones.idPositivo(usuarioId, "El usuario del funcionario");
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = Validaciones.texto(nombreCompleto, "El nombre completo", 150);
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        String limpio = Validaciones.texto(identificacion, "La identificacion", 30);
        if (limpio.contains(" ")) {
            throw new IllegalArgumentException("La identificacion no puede tener espacios.");
        }
        this.identificacion = limpio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return nombreCompleto + " (" + identificacion + ")";
    }
}
