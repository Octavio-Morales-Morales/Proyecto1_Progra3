/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccesObject;

import Repositorios.InterfaceServicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author COTO
 */
public class ServicioDAO implements InterfaceServicio {
    private Connection obtenerConexion() throws SQLException {
        return DataAccesObject.ConexionBD.getConexion();
    }
    
    @Override
    public List<String> ListarServiciosDisponibles() {
        List<String> servicios = new ArrayList<>();
        String sql = "SELECT descripcion FROM SERVICIO";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                servicios.add(rs.getString("descripcion"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar servicios disponibles: " + e.getMessage());
        }
        return servicios;
    }

    @Override
    public int obtenerNombreServicio(String nombreServicio) {
        String sql = "SELECT servicio_id FROM SERVICIO WHERE descripcion = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombreServicio);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                return rs.getInt("servicio_id");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ID del servicio por nombre: " + e.getMessage());
        }
        return 0; 
    }
    
}
