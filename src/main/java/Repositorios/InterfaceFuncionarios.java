package Repositorios;

import Dominio.Funcionarios;
import java.util.List;
import java.util.Optional;

public interface InterfaceFuncionarios {
    void crearFuncionario(Funcionarios funcionario);

    void actualizarFuncionario(Funcionarios funcionario);

    Funcionarios obtenerFuncionarioPorId(int funcionarioId);

    Funcionarios ObtenerFuncionarioPorUsuarioId(int UsuarioId);

    List<Funcionarios> listarFuncionarios();

    Optional<Funcionarios> buscarPorIdentificacion(String identificacion);

    List<Funcionarios> listarActivos();
}
