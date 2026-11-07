/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccesObject;
import Repositorios.InterfaceVentanilla;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Dominio.Ventanillas;
/**
 *
 * @author COTO
 */
public class VentanillaDAO implements InterfaceVentanilla {
    
    private Connection obtenerConexion() throws SQLException {
        return DataAccesObject.ConexionBD.getConexion();
    }
    
    @Override
    public void cambiarEstado(int ventanillaId, String estado) {
        
        String sql = "UPDATE VENTANILLA SET estado = ? WHERE ventanilla_id = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, estado);
            ps.setInt(2, ventanillaId);
            
            ps.executeUpdate();
            System.out.println("Estado de la ventanilla " + ventanillaId + " cambiado a: " + estado);
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de la ventanilla: " + e.getMessage());
        }
    }

    @Override
    public List<Ventanillas> ListaVentanillasActivas() {
        List<Ventanillas> activas = new ArrayList<>();
        String sql = "SELECT ventanilla_id FROM VENTANILLA WHERE activo = 1";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Ventanillas ven = new Ventanillas();
                ven.setVentanillaId(rs.getInt("ventanilla_id"));
                ven.setNumero(rs.getInt("numero_ventanilla"));
                ven.setEstado(rs.getString("estado"));
                ven.setActivoVentanilla(rs.getInt("activo") == 1);
                
                activas.add(ven);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar ventanillas activas: " + e.getMessage());
        }
        return activas;
    }
    
}
    

