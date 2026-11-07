/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccesObject;

import Dominio.TurnoTrabajo;
import Repositorios.InterfaceTurnoTTrabajo;
import Repositorios.InterfaceTurnoTrabajo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author moral
 */
public class TurnosTrabajoDAO implements InterfaceTurnoTrabajo, InterfaceTurnoTTrabajo {
    
    private Connection obtenerConexion() throws SQLException {
        return DataAccesObject.ConexionBD.getConexion();
    }    
    @Override
    public int registrarEntrada(int funcionarioId) {
        String sql = "INSERT INTO TURNOTRABAJO (funcionario_id, Turno_entrada) VALUES (?, CURRENT_TIMESTAMP)";
        
        try(Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, funcionarioId);
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.getLogger(TurnosTrabajoDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
        return 0;
    }

    @Override
    public void registrarSalida(int turnoTrabajoId) {
        String sql = "UPDATE TURNOTRABAJO SET Turno_Salida = CURRENT_TIMESTAMP WHERE TURNOTRABAJO_Id = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, turnoTrabajoId);
            ps.executeUpdate();
            System.out.println("Salida de jornada registrada con éxito para el ID: " + turnoTrabajoId);
            
        } catch (SQLException e) {
            System.err.println("Error al registrar salida de jornada laboral: " + e.getMessage());
        }
    }

    @Override
    public List<TurnoTrabajo> consultarJornadasPorFuncionario(int funcionarioId) {
        List<TurnoTrabajo> lista = new ArrayList<>();
        String sql = "SELECT TURNOTRABAJO_Id, funcionario_id, Turno_entrada, Turno_Salida " +
                     "FROM TURNOTRABAJO WHERE funcionario_id = ? ORDER BY Turno_entrada DESC";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, funcionarioId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TurnoTrabajo tt = new TurnoTrabajo();
                    tt.setTurnoTrabajoId(rs.getInt("TURNOTRABAJO_Id"));
                    tt.setFuncionarioId(rs.getInt("funcionario_id"));
                    
                    Timestamp entrada = rs.getTimestamp("Turno_entrada");
                    if (entrada != null) {
                        tt.setTurnoEntrada(entrada.toLocalDateTime());
                    }
                    
                    Timestamp salida = rs.getTimestamp("Turno_Salida");
                    if (salida != null) {
                        tt.setTurnoSalida(salida.toLocalDateTime());
                    }
                    
                    lista.add(tt);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar jornadas del funcionario: " + e.getMessage());
        }
        return lista;
    
    }
     @Override
    public void vincularTurnoAJornada(int turnoId, int turnoTrabajoId) {
        String sql = "INSERT INTO TURNO_TTRABAJO (TURNO_Id, TURNO_TRABAJO_Id) VALUES (?, ?)";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, turnoId);
            ps.setInt(2, turnoTrabajoId);
            ps.executeUpdate();
            System.out.println("Tiquete " + turnoId + " vinculado a la jornada de trabajo " + turnoTrabajoId);
            
        } catch (SQLException e) {
            System.err.println("Error al vincular tiquete con jornada de trabajo: " + e.getMessage());
        }
    }
    
    @Override
    public List<Integer> obtenerTurnosAtendidosEnJornada(int turnoTrabajoId) {
        List<Integer> turnosIds = new ArrayList<>();
        String sql = "SELECT TURNO_Id FROM TURNO_TTRABAJO WHERE TURNO_TRABAJO_Id = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, turnoTrabajoId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    
                    turnosIds.add(rs.getInt("TURNO_Id"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener los turnos atendidos en la jornada " + turnoTrabajoId + ": " + e.getMessage());
        }
        
        return turnosIds; 
    }
}
