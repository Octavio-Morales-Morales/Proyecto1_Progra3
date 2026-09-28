/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccesObject;
import Repositorios.InterfaceVentanillaServicio;
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
public class VentanillaServicioDAO implements InterfaceVentanillaServicio {
    private Connection obtenerConexion() throws SQLException {
        return DataAccesObject.ConexionBD.getConexion();
    }

    @Override
    public void asignarServicioVentanilla(int ventanilla, int servicioId) {
        
        String sql = "INSERT INTO VENTANILLA_SERVICIO (ventanilla_id, servicio_id, activo) VALUES (?, ?, 1) "
                   + "ON DUPLICATE KEY UPDATE activo = 1";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, ventanilla);
            ps.setInt(2, servicioId);
            
            ps.executeUpdate();
            System.out.println("Servicio " + servicioId + " asignado con éxito a la ventanilla " + ventanilla);
            
        } catch (SQLException e) {
            System.err.println("Error al asignar servicio a ventanilla: " + e.getMessage());
        }
    }

    @Override
    public List<Integer> ObtenerServiciosVentanilla(int ventanillaId) {
        List<Integer> servicios = new ArrayList<>();
        String sql = "SELECT servicio_id FROM VENTANILLA_SERVICIO WHERE ventanilla_id = ? AND activo = 1";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, ventanillaId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    servicios.add(rs.getInt("servicio_id"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener los servicios de la ventanilla: " + e.getMessage());
        }
        return servicios;
    }
    
}
