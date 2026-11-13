package Servicios;

import Dominio.Funcionarios;
import Dominio.Sesion;
import Dominio.Usuarios;
import Repositorios.InterfaceFuncionarios;
import java.util.Objects;

public class SesionService {
    private final UsuarioService usuarioService;
    private final InterfaceFuncionarios funcionarioDAO;

    public SesionService(UsuarioService usuarioService, InterfaceFuncionarios funcionarioDAO) {
        this.usuarioService = Objects.requireNonNull(usuarioService, "El servicio de usuarios es obligatorio.");
        this.funcionarioDAO = Objects.requireNonNull(funcionarioDAO, "El repositorio de funcionarios es obligatorio.");
    }

    public Sesion iniciarSesion(String nombreUsuario, String contrasenia) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("Digite su usuario.");
        }
        if (contrasenia == null || contrasenia.isEmpty()) {
            throw new IllegalArgumentException("Digite su contraseña.");
        }

        Usuarios usuario = usuarioService.login(nombreUsuario.trim(), contrasenia);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos, o el usuario está inactivo.");
        }

        Funcionarios funcionario = funcionarioDAO.ObtenerFuncionarioPorUsuarioId(usuario.getUsuarioId());
        if (!usuario.esAdministrador()) {
            if (funcionario == null) {
                throw new IllegalArgumentException("El usuario no tiene un funcionario asociado. Contacte al administrador.");
            }
            if (!funcionario.isActivo()) {
                throw new IllegalArgumentException("El funcionario está inactivo. Contacte al administrador.");
            }
        }
        return new Sesion(usuario, funcionario);
    }
}
