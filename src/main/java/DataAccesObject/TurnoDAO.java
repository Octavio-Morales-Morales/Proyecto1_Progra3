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
    if (ServicioId == null || ServicioId.isEmpty()){
        return null;
    }
    StringBuilder sb = new StringBuilder();
    for (int i =0; i < ServicioId.size(); i++){
        sb.append("?");
        if (i < ServicioId.size() -1){
            sb.append(",");
        }
    }
    String sql = "SELECT * FROM TURNO WHERE estado_id = 1 AND servicio_ IN ( " + sb.toString() + ") "
            + "ORDER BY prioridad DESC, generacion ASC, hora generacion ASC LIMIT 1";
     try( Connection con = obtenerConexion();
          PreparedStatement ps = con.prepareStatement(sql)){
         for(int i = 0; i < ServicioId.size(); i++){
             ps.setInt(i+1, ServicioId.get(i));
             
           try( ResultSet rs  = ps.executeQuery()){
               if (rs.next()){
                   return transformarResultado(rs);
               }
           }
         }
         
     }catch (SQLException e){
          System.err.println("Error al obtener el siguiente turno de la fila: " + e.getMessage());
     }
    return null;
    }

    @Override
    public List<Turnos> ObtenerTurnoPorFecha(LocalDate inicio, LocalDate fin) {
        List<Turnos> lista = new ArrayList<>();
        String sql = "SELECT * FROM TURNO WHERE generacion BETWEEN ? AND ? ORDER BY generacion ASC, hora_generacion ASC";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(transformarResultado(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener turnos por rango de fechas: " + e.getMessage());
        }
        return lista;
    }

    
    private Turnos transformarResultado(ResultSet rs) throws SQLException{
    LocalDate fechaGen = rs.getDate("generacion") != null  ? rs.getDate("generacion").toLocalDate() : null;
    java.time.LocalTime horaGen = rs.getTime("hora_generacion") != null ? rs.getTime("hora_generacion").toLocalTime() : null;
    
    Integer ventanillaId = rs.getInt("ventanilla_id");
    if(rs.wasNull()){
        ventanillaId = null;
    }
    return new Turnos(
            rs.getInt("turno_id"),
            rs.getString("codigo_turno"),
            rs.getInt("servicio_id"),
            fechaGen,
            horaGen,
            rs.getInt("estado_id"),
            rs.getInt("prioridad"),
            ventanillaId );
    
}
}
