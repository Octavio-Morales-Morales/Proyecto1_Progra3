package Repositorios;

import Dominio.Usuarios;
import java.util.List;
import java.util.Optional;

public interface InterfaceUsuarios {
    Usuarios autenticar(String nombreusuario, String Contraseña);

    void crear(Usuarios usuario);

    void actualizar(Usuarios usuario);

    Optional<Usuarios> buscarPorId(int usuarioId);

    Optional<Usuarios> buscarPorUsuario(String nombreUsuario);

    List<Usuarios> listar();
}
