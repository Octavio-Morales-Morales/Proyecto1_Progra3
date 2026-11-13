package DataAccesObject;

import Dominio.Funcionarios;
import Excepciones.PersistenciaException;
import Repositorios.InterfaceFuncionarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FuncionariosDAO implements InterfaceFuncionarios {
    private static final String COLUMNAS = "funcionario_id, usuario_id, nombre_completo, identificacion, activo";

    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getConexion();
    }

    @Override
    public void crearFuncionario(Funcionarios funcionario) {
        String sql = "INSERT INTO FUNCIONARIO (usuario_id, nombre_completo, identificacion, activo) VALUES (?, ?, ?, ?)";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, funcionario.getUsuarioId());
            ps.setString(2, funcionario.getNombreCompleto());
            ps.setString(3, funcionario.getIdentificacion());
            ps.setBoolean(4, funcionario.isActivo());
            ps.executeUpdate();
            funcionario.setFuncionarioId(UtilJdbc.idGenerado(ps));
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo crear el funcionario " + funcionario.getIdentificacion(), e);
        }
    }

    @Override
    public void actualizarFuncionario(Funcionarios funcionario) {
        String sql = "UPDATE FUNCIONARIO SET usuario_id = ?, nombre_completo = ?, identificacion = ?, activo = ? "
                   + "WHERE funcionario_id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, funcionario.getUsuarioId());
            ps.setString(2, funcionario.getNombreCompleto());
            ps.setString(3, funcionario.getIdentificacion());
            ps.setBoolean(4, funcionario.isActivo());
            ps.setInt(5, funcionario.getFuncionarioId());
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe el funcionario " + funcionario.getFuncionarioId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el funcionario " + funcionario.getFuncionarioId(), e);
        }
    }

    @Override
    public Funcionarios obtenerFuncionarioPorId(int funcionarioId) {
        return buscarUno("SELECT " + COLUMNAS + " FROM FUNCIONARIO WHERE funcionario_id = ?", funcionarioId,
                "No se pudo obtener el funcionario " + funcionarioId).orElse(null);
    }

    @Override
    public Funcionarios ObtenerFuncionarioPorUsuarioId(int UsuarioId) {
        return buscarUno("SELECT " + COLUMNAS + " FROM FUNCIONARIO WHERE usuario_id = ?", UsuarioId,
                "No se pudo obtener el funcionario del usuario " + UsuarioId).orElse(null);
    }

    @Override
    public Optional<Funcionarios> buscarPorIdentificacion(String identificacion) {
        return buscarUno("SELECT " + COLUMNAS + " FROM FUNCIONARIO WHERE identificacion = ?",
                identificacion == null ? null : identificacion.trim(),
                "No se pudo buscar el funcionario " + identificacion);
    }

    @Override
    public List<Funcionarios> listarFuncionarios() {
        return listar("SELECT " + COLUMNAS + " FROM FUNCIONARIO ORDER BY nombre_completo");
    }

    @Override
    public List<Funcionarios> listarActivos() {
        return listar("SELECT " + COLUMNAS + " FROM FUNCIONARIO WHERE activo = 1 ORDER BY nombre_completo");
    }

    private Optional<Funcionarios> buscarUno(String sql, Object parametro, String mensajeError) {
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException(mensajeError, e);
        }
    }

    private List<Funcionarios> listar(String sql) {
        List<Funcionarios> lista = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar los funcionarios", e);
        }
    }

    private Funcionarios mapear(ResultSet rs) throws SQLException {
        return new Funcionarios(
                rs.getInt("funcionario_id"),
                rs.getInt("usuario_id"),
                rs.getString("nombre_completo"),
                rs.getString("identificacion"),
                rs.getBoolean("activo"));
    }
}
