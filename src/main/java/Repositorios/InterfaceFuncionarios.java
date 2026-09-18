/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Repositorios;
import Dominio.Funcionarios;
import java.util.List;

/**
 *
 * @author COTO
 */
public interface InterfaceFuncionarios {
    void crearFuncionario(Funcionarios funcionario);
    void actualizarFuncionario(Funcionarios funcionario);
    Funcionarios obtenerFuncionarioPorId(int funcionarioId);
    Funcionarios ObtenerFuncionarioPorUsuarioId(int UsuarioId);
    List<Funcionarios> listarFuncionarios();
    
    
}
