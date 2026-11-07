/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Repositorios;

import java.util.List;
import Dominio.TurnoTrabajo;
/**
 *
 * @author moral
 */
public interface InterfaceTurnoTrabajo {
    int registrarEntrada(int funcionarioId);
    void registrarSalida(int turnoTrabajoId);
    List<TurnoTrabajo> consultarJornadasPorFuncionario(int funcionarioId);

}
