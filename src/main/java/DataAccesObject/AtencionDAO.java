/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccesObject;

import Dominio.Atencion;
import Repositorios.InterfaceAtencion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author COTO
 */
public class AtencionDAO implements InterfaceAtencion{
    private Connection obtenerConexion() throws SQLException {
        return DataAccesObject.ConexionBD.getConexion();
    } 

    @Override
    public void registrarAtencion(Atencion atencion) {
        String sql = "INSERT INTO ATENCION (turno_id, ventanilla_id, funcionario_id, hora_llamado) VALUES (?,?,?,?)";
        try (Connection con = obtenerConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, atencion.getTurnoId());
            ps.setInt(2, atencion.getVentanillaId());
            ps.setInt(3, atencion.getFuncionarioId());
            
            if (atencion.getHoraLlamado() != null) {
             ps.setTimestamp(4, Timestamp.valueOf(atencion.getHoraLlamado()));
            } else {
             ps.setNull(4, java.sql.Types.TIMESTAMP);
            }
            
            ps.executeUpdate();
            System.out.println("Atención registrada con éxito.");
            
        } catch (SQLException e) {
            System.err.println("Error al registrar atención: " + e.getMessage());
        }
    }

    @Override
    public void registrarInicio(int atencionId) {
        String sql = "UPDATE ATENCION SET hora_inicio = NOW() WHERE atencion_id = ?";
        
        try (Connection con = obtenerConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, atencionId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error al registrar inicio: " + e.getMessage());
        }
    }

    @Override
    public void registrarFin(int atencionId, String observaciones) {
        String sql = "UPDATE ATENCION SET hora_finalizacion = NOW(), observaciones = ? WHERE atencion_id = ?";
        
        try (Connection con = obtenerConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, observaciones);
            ps.setInt(2, atencionId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error al registrar fin: " + e.getMessage());
        }
    }

    @Override
    public int AtendidosPorFuncionarios(int funcionarioId) {
        String sql = "SELECT COUNT(*) FROM ATENCION WHERE funcionario_id = ? AND hora_finalizacion IS NOT NULL";
        
        try (Connection con = obtenerConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, funcionarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                 return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en conteo por funcionario: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int TurnosPorServicio(int servicioId) {
        String sql = "SELECT COUNT(*) FROM ATENCION a " 
                + "INNER JOIN TURNO t ON a.turno_id = t.turno_id " 
                + "WHERE t.servicio_id = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, servicioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en conteo por servicio: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public double PromedioDeEspera() {
        String sql = "SELECT AVG(TIMESTAMPDIFF(MINUTE, t.generacion, a.hora_inicio)) FROM ATENCION a " 
                + "INNER JOIN TURNO t ON a.turno_id = t.turno_id "
                + "WHERE a.hora_inicio IS NOT NULL";
        
        try (Connection con = obtenerConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
            return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error en promedio de espera: " + e.getMessage());
        }
        return 0.0;
    }


    @Override
    public double PromedioAtencio() {
        String sql = "SELECT AVG(TIMESTAMPDIFF(MINUTE, hora_inicio, hora_finalizacion)) FROM ATENCION " 
                + "WHERE hora_inicio IS NOT NULL AND hora_finalizacion IS NOT NULL";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error en promedio de atención: " + e.getMessage());
        }
        return 0.0;
    }
}
