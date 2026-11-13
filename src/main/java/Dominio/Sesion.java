package Dominio;

public class Sesion {
    private final Usuarios usuario;
    private final Funcionarios funcionario;

    public Sesion(Usuarios usuario, Funcionarios funcionario) {
        this.usuario = Validaciones.requerido(usuario, "El usuario");
        this.funcionario = funcionario;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public Funcionarios getFuncionario() {
        return funcionario;
    }

    public boolean esAdministrador() {
        return usuario.esAdministrador();
    }

    public String getNombreVisible() {
        return funcionario != null ? funcionario.getNombreCompleto() : usuario.getNombre();
    }
}
