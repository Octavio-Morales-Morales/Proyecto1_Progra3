/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import Dominio.Atencion;
import Repositorios.InterfaceAtencion;
import Repositorios.InterfaceTurno;
import java.time.LocalDateTime;
import Dominio.EstadoTurno;
/**
 *
 * @author COTO
 */
public class AtencionService {
    private final InterfaceAtencion atencionDAO;
    private final InterfaceTurno turnoDAO;

    public AtencionService(InterfaceAtencion atencionDAO, InterfaceTurno turnoDAO) {
        this.atencionDAO = atencionDAO;
        this.turnoDAO = turnoDAO;
    }
    public int llamarCliente(int turnoId, int ventanillaId, int funcionarioId) {
        if (turnoId <= 0 || ventanillaId <= 0 || funcionarioId <= 0) {
            throw new IllegalArgumentException("Los IDs proporcionados deben ser positivos y válidos.");
        }
        
        Atencion atencion = new Atencion();
        atencion.setTurnoId(turnoId);
        atencion.setVentanillaId(ventanillaId);
        atencion.setFuncionarioId(funcionarioId);
        atencion.setHoraLlamado(LocalDateTime.now());
        
        int atencionIdGenerado = atencionDAO.registrarAtencion(atencion);
        int idEstadoProcesado = EstadoTurno.LLAMADO.getId();
        turnoDAO.ActualizarTurno(turnoId, idEstadoProcesado);
        return atencionIdGenerado;
    }
    public void comenzarTramite(int atencionId, int turnoId) {
        if (atencionId <= 0 || turnoId <= 0) {
            throw new IllegalArgumentException("El ID de la atención a iniciar no es válido.");
        }
        
        atencionDAO.registrarInicio(atencionId);
        int idEstadoProcesado = EstadoTurno.EN_ATENCION.getId();
        
        turnoDAO.ActualizarTurno(turnoId, idEstadoProcesado);
        
    }
    public void terminarTramite(int atencionId, int turnoId, String notas) {
        if (atencionId <= 0 || turnoId <= 0) {
            throw new IllegalArgumentException("Los IDs de atención y turno a finalizar no son válidos.");
        }
        
        String observaciones = (notas == null || notas.trim().isEmpty()) ? "Sin observaciones" : notas;
        atencionDAO.registrarFin(atencionId, observaciones);
        int idEstadoProcesado = EstadoTurno.FINALIZADO.getId();
        turnoDAO.ActualizarTurno(turnoId, idEstadoProcesado);
    }    
}
