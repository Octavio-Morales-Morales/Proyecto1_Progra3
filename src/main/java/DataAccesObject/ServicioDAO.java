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
import Dominio.Servicio;
/**
 *
 * @author COTO
 */
public class ServicioDAO implements InterfaceServicio {

    private Connection obtenerConexion() throws SQLException {
        return DataAccesObject.ConexionBD.getConexion();
    }
    
    @Override
    public List<Servicio> ListarServiciosDisponibles() {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT servicio_id, descripcion, letra_codigo, activo FROM SERVICIO WHERE activo = 1";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                
                Servicio svc = new Servicio();
                svc.setServicioId(rs.getInt("servicio_id"));
                svc.setNombre(rs.getString("descripcion"));
                svc.setLetraCodigo(rs.getString("letra_codigo"));
                
                svc.setActivo(rs.getInt("activo") == 1);
                
                servicios.add(svc);
            
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
