/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccesObject;

import Dominio.Turnos;
import Repositorios.InterfaceTurno;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author COTO
 */
public class TurnoDAO implements InterfaceTurno {

    private Connection obtenerConexion() throws SQLException {
        return DataAccesObject.ConexionBD.getConexion();
    }
    
    @Override
    public void crearTurno(Turnos turno) {
        String sql = "INSERT INTO TURNO (codigo_turno, estado_id, generacion, hora_generacion, prioridad, servicio_id, ventanilla_id) "
                   + "VALUES (?, ?, CURDATE(), CURTIME(), ?, ?, ?)";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, turno.getCodigoTurno());
            ps.setInt(2, turno.getEstadoId());
            ps.setInt(3, turno.getPrioridad());
            ps.setInt(4, turno.getServicioId());
            
            if (turno.getVentanillaId() != null) {
                ps.setInt(5, turno.getVentanillaId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            
            ps.executeUpdate();
            System.out.println("Turno creado correctamente en la ticketera.");
            
        } catch (SQLException e) {
            System.err.println("Error al crear turno: " + e.getMessage());
        }
    }

    @Override
    public void ActualizarTurno(int turnoId, int estadoId) {
        String sql = "UPDATE TURNO SET estado_id = ? WHERE turno_id = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, estadoId);
            ps.setInt(2, turnoId);
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del turno: " + e.getMessage());
        }
    }

    @Override
    public Turnos ObtenerSiguienteTurno(List<Integer> ServicioId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Turnos> ObtenerTurnoPorFecha(LocalDate inicio, LocalDate fin) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
