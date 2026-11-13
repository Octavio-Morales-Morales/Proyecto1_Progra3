package Repositorios;

import Dominio.Rol;
import java.util.List;
import java.util.Optional;

public interface InterfaceRol {
    String obtenerRol(int rolId);

    Optional<Rol> buscarPorId(int rolId);

    List<Rol> listar();
}
