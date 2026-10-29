/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccesObject;

import Repositorios.InterfaceRol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author COTO
 */
public class RolDAO implements InterfaceRol{
    
    private Connection obtenerConexion() throws SQLException {
        return DataAccesObject.ConexionBD.getConexion();
    }    
    
    @Override
    public String obtenerRol(int rolId) {
        String sql = "SELECT descripcion FROM ROL WHERE rol_id = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, rolId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("descripcion"); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el rol: " + e.getMessage());
        }
        
        return "Rol Desconocido"; 
    }
    
}
