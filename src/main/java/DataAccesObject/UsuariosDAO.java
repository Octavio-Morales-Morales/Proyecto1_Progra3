/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccesObject;

import Dominio.Usuarios;
import Repositorios.InterfaceUsuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author COTO
 */
public class UsuariosDAO implements InterfaceUsuarios {

    private Connection obtenerConexion() throws SQLException {
        return DataAccesObject.ConexionBD.getConexion();
    }
    
    @Override
    public Usuarios autenticar(String nombreusuario, String Contraseña) {
        String sql = "SELECT * FROM USUARIO WHERE usuario = ? AND password_hash = ? AND activo = 1";
        try(Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, nombreusuario);
            ps.setString(2, Contraseña);
            
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                 return transformarResultado(rs);   
                }
            }
        } catch(SQLException e){
            System.out.println("Error en la autenticacion de usuario: "+ e.getMessage());
        }
        return null;
    }

    @Override
    public void crear(Usuarios usuario) {
        String sql ="INSERT INTO USUARIO (nombre, usuario, password_hash, rol_id, activo) VALUES (?,?,?,?,?)";
        
        try(Connection con = obtenerConexion();
            PreparedStatement ps = con.prepareStatement(sql)){
                    
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getContraseña());
            ps.setInt(4, usuario.getRolId());
            ps.setInt(5, usuario.isActivoUsuario() ? 1 : 0);
            
            ps.executeUpdate();
            System.out.println("Usuario creado exitosamente.");
            
        } catch(SQLException e) {
            System.out.println("Error al crear el ususario: " + e.getMessage()); 
        }
    }

    @Override
    public void actualizar(Usuarios usuario) {
        String sql = "UPDATE USUARIO SET nombre = ?, usuario = ?, password_hash = ?, rol_id = ?, activo = ? WHERE usuario_id = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getContraseña());
            ps.setInt(4, usuario.getRolId());
            ps.setInt(5, usuario.isActivoUsuario() ? 1 : 0);
            ps.setInt(6, usuario.getUsuarioId()); 
            
            ps.executeUpdate();
            System.out.println("Usuario actualizado exitosamente.");
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar el usuario: " + e.getMessage());
        }    
    }
    private Usuarios transformarResultado(ResultSet rs) throws SQLException {
        java.time.LocalDateTime fechaCreacion = null;
        Timestamp ts = rs.getTimestamp("fecha_creacion");
        if (ts != null) {
            fechaCreacion = ts.toLocalDateTime();
        }
        return new Usuarios(
            rs.getInt("usuario_id"),
            rs.getString("nombre"),
            rs.getString("usuario"),
            rs.getString("password_hash"),
            rs.getInt("rol_id"),
            rs.getInt("activo") == 1,
            fechaCreacion
        );
    }
    
}
