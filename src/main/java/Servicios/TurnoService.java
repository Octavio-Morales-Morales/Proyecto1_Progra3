/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import Dominio.Turnos;
import Repositorios.InterfaceTurno;
import java.util.List;

/**
 *
 * @author COTO
 */
public class TurnoService {
    private final InterfaceTurno turnoDAO;
    public TurnoService(InterfaceTurno turnoDAO) {
        this.turnoDAO = turnoDAO;
    }
    public Turnos generarNuevoTurno(String letraServicio, int servicioId, int prioridad) {
        if (letraServicio == null || letraServicio.trim().isEmpty()) {
            throw new IllegalArgumentException("La letra identificadora del servicio es obligatoria.");
        }
        if (servicioId <= 0) {
            throw new IllegalArgumentException("El ID del servicio debe ser un número positivo válido.");
        }
        
        List<Turnos> todos = turnoDAO.ObtenerTurnoPorFecha(java.time.LocalDate.now(), java.time.LocalDate.now());
        
        long conteoServicio = todos.stream().filter(t -> t.getServicioId() == servicioId).count();
        
        long siguienteNumero = conteoServicio + 1;
        String codigoTurno = String.format("%s%03d", letraServicio.toUpperCase(), siguienteNumero);

        Turnos nuevoTurno = new Turnos(0, codigoTurno, servicioId, null, null, 1, prioridad, null);
        
        turnoDAO.crearTurno(nuevoTurno);
        return nuevoTurno;
    }
    public synchronized Turnos solicitarSiguiente(List<Integer> serviciosPermitidos) {
        
        if (serviciosPermitidos == null || serviciosPermitidos.isEmpty()) {
            throw new IllegalArgumentException("La ventanilla debe tener al menos un servicio autorizado.");
        }
                
        Turnos siguiente = turnoDAO.ObtenerSiguienteTurno(serviciosPermitidos);
        
        if (siguiente == null) {
            throw new IllegalStateException("Aviso: No existen turnos pendientes en la fila para tus servicios.");
        }
            turnoDAO.ActualizarTurno(siguiente.getTurnoId(), 2);
            siguiente.setEstadoId(2);
            
            return siguiente;
    }  
   public void cambiarEstadoTurno(Turnos turno, int nuevoEstadoId) {
        if (turno == null) {
            throw new IllegalArgumentException("El objeto turno no puede ser nulo.");
        }
        
        int estadoActual = turno.getEstadoId();
        
        if (estadoActual == 4) {
            throw new IllegalStateException("Un turno finalizado no puede volver a asignarse ni modificarse.");
        }
        
        if (nuevoEstadoId == 3 && estadoActual != 2) {
            throw new IllegalStateException("El turno número " + turno.getCodigoTurno() + " debe ser llamado antes de ser atendido.");
        }
        
        if (nuevoEstadoId == 4 && estadoActual != 3) {
            throw new IllegalStateException("No se puede finalizar un turno que no está en proceso de atención.");
        }
        turnoDAO.ActualizarTurno(turno.getTurnoId(), nuevoEstadoId);
        turno.setEstadoId(nuevoEstadoId);
    }    
}
