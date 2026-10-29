/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import Dominio.Atencion;
import Repositorios.InterfaceAtencion;
import java.time.LocalDateTime;
/**
 *
 * @author COTO
 */
public class AtencionService {
    private final InterfaceAtencion atencionDAO;

    public AtencionService(InterfaceAtencion atencionDAO) {
        this.atencionDAO = atencionDAO;
    }
    public void llamarCliente(int turnoId, int ventanillaId, int funcionarioId) {
        if (turnoId <= 0) {
            throw new IllegalArgumentException("El ID del turno debe ser un número positivo válido.");
        }
        if (ventanillaId <= 0) {
            throw new IllegalArgumentException("El ID de la ventanilla debe ser un número positivo válido.");
        }
        if (funcionarioId <= 0) {
            throw new IllegalArgumentException("El ID del funcionario debe ser un número positivo válido.");
        }
        
        Atencion atencion = new Atencion();
        atencion.setTurnoId(turnoId);
        atencion.setVentanillaId(ventanillaId);
        atencion.setFuncionarioId(funcionarioId);
        atencion.setHoraLlamado(LocalDateTime.now());
        
        atencionDAO.registrarAtencion(atencion);
    }
    public void comenzarTramite(int atencionId) {
        if (atencionId <= 0) {
            throw new IllegalArgumentException("El ID de la atención a iniciar no es válido.");
        }
        
        atencionDAO.registrarInicio(atencionId);
    }
    public void terminarTramite(int atencionId, String notas) {
        if (atencionId <= 0) {
            throw new IllegalArgumentException("El ID de la atención a finalizar no es válido.");
        }
        String observaciones = (notas == null || notas.trim().isEmpty()) ? "Sin observaciones" : notas;
        atencionDAO.registrarFin(atencionId, observaciones);
    }    
}
