/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccesObject;
import Dominio.Funcionarios;
import Repositorios.InterfaceFuncionarios;
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
public class FuncionariosDAO implements InterfaceFuncionarios {
    
    private Connection obtenerConexion() throws SQLException {
        return DataAccesObject.ConexionBD.getConexion();
    }
    
    @Override
    public void crearFuncionario(Funcionarios funcionario) {
        String sql = "INSERT INTO FUNCIONARIO (usuario_id, nombre_completo, identificacion, activo) VALUES (?, ?, ?, ?)";
        
        try (Connection con = obtenerConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, funcionario.getUsuarioId());
            ps.setString(2, funcionario.getNombreCompleto());
            ps.setString(3, funcionario.getIdentificacion());
            ps.setInt(4, funcionario.isActivo() ? 1 : 0);
            
            ps.executeUpdate();
            System.out.println("Funcionario creado con éxito.");
            
        } catch (SQLException e) {
            System.err.println("Error al crear funcionario: " + e.getMessage());
        }
    }

    @Override
    public void actualizarFuncionario(Funcionarios funcionario) {
     String sql = "UPDATE FUNCIONARIO SET usuario_id = ?, nombre_completo = ?, identificacion = ?, activo = ? WHERE funcionario_id = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, funcionario.getUsuarioId());
            ps.setString(2, funcionario.getNombreCompleto());
            ps.setString(3, funcionario.getIdentificacion());
            ps.setInt(4, funcionario.isActivo() ? 1 : 0);
            ps.setInt(5, funcionario.getFuncionarioId()); 
            
            ps.executeUpdate();
            System.out.println("Funcionario actualizado con éxito.");
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar funcionario: " + e.getMessage());
        }
    }

    @Override
    public Funcionarios obtenerFuncionarioPorId(int funcionarioId) {
        String sql = "SELECT * FROM FUNCIONARIO WHERE funcionario_id = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, funcionarioId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                return transformarResultado(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener funcionario por ID: " + e.getMessage());
        }
        return null;
    }
    private Funcionarios transformarResultado(ResultSet rs) throws SQLException {
        return new Funcionarios(
            rs.getInt("funcionario_id"),
            rs.getInt("usuario_id"),
            rs.getString("nombre_completo"),
            rs.getString("identificacion"),
            rs.getInt("activo") == 1 
        );
    }

    @Override
    public Funcionarios ObtenerFuncionarioPorUsuarioId(int UsuarioId) {
        String sql = "SELECT * FROM FUNCIONARIO WHERE usuario_id = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, UsuarioId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                return transformarResultado(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener funcionario por su ID de usuario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Funcionarios> listarFuncionarios() {
       List<Funcionarios> lista = new ArrayList<>();
        String sql = "SELECT * FROM FUNCIONARIO";
        
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(transformarResultado(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar funcionarios: " + e.getMessage());
        }
        return lista;
    }
    
}
