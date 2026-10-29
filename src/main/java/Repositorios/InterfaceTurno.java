/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Repositorios;
import Dominio.Turnos;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author COTO
 */
public interface InterfaceTurno {
    void crearTurno(Turnos turno);
    void ActualizarTurno(int turnoId, int estadoId);
    Turnos ObtenerSiguienteTurno(List<Integer> ServicioId);
    List<Turnos> ObtenerTurnoPorFecha(LocalDate inicio, LocalDate fin);
    
}
