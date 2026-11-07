/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Repositorios;

import java.util.List;

/**
 *
 * @author moral
 */
public interface InterfaceTurnoTTrabajo {
    void vincularTurnoAJornada(int turnoId, int turnoTrabajoId);
    List<Integer> obtenerTurnosAtendidosEnJornada(int turnoTrabajoId);
}
