/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccesObject;

import Repositorios.InterfaceEstadoTurno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author COTO
 */
public class EstadoTurnoDAO implements InterfaceEstadoTurno{

    private Connection obtenerConexion() throws SQLException {
        return DataAccesObject.ConexionBD.getConexion();
    }
    
    @Override
    public String obtenerEstado(int estadoId) {
        String sql = "SELECT descripcion FROM ESTADO_TURNO WHERE estado_turno_id = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, estadoId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("descripcion"); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el estado del turno: " + e.getMessage());
        }
        
        return "Estado Desconocido"; 
    }
    
}
