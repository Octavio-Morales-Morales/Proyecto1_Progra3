package DataAccesObject;

import Dominio.Usuarios;
import Excepciones.PersistenciaException;
import Repositorios.InterfaceUsuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuariosDAO implements InterfaceUsuarios {
    private static final String COLUMNAS =
            "usuario_id, nombre, usuario, password_hash, rol_id, activo, fecha_creacion";

    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getConexion();
    }

    @Override
    public Usuarios autenticar(String nombreusuario, String Contraseña) {
        String sql = "SELECT " + COLUMNAS + " FROM USUARIO WHERE usuario = ? AND password_hash = ? AND activo = 1";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreusuario);
            ps.setString(2, Contraseña);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo autenticar al usuario " + nombreusuario, e);
        }
    }

    @Override
    public void crear(Usuarios usuario) {
        String sql = "INSERT INTO USUARIO (nombre, usuario, password_hash, rol_id, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getContraseña());
            ps.setInt(4, usuario.getRolId());
            ps.setBoolean(5, usuario.isActivoUsuario());
            ps.executeUpdate();
            usuario.setUsuarioId(UtilJdbc.idGenerado(ps));
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo crear el usuario " + usuario.getUsuario(), e);
        }
    }

    @Override
    public void actualizar(Usuarios usuario) {
        String sql = "UPDATE USUARIO SET nombre = ?, usuario = ?, password_hash = ?, rol_id = ?, activo = ? "
                   + "WHERE usuario_id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getContraseña());
            ps.setInt(4, usuario.getRolId());
            ps.setBoolean(5, usuario.isActivoUsuario());
            ps.setInt(6, usuario.getUsuarioId());
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe el usuario " + usuario.getUsuarioId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el usuario " + usuario.getUsuarioId(), e);
        }
    }

    @Override
    public Optional<Usuarios> buscarPorId(int usuarioId) {
        return buscarUno("SELECT " + COLUMNAS + " FROM USUARIO WHERE usuario_id = ?", usuarioId,
                "No se pudo buscar el usuario " + usuarioId);
    }

    @Override
    public Optional<Usuarios> buscarPorUsuario(String nombreUsuario) {
        return buscarUno("SELECT " + COLUMNAS + " FROM USUARIO WHERE usuario = ?",
                nombreUsuario == null ? null : nombreUsuario.trim(),
                "No se pudo buscar el usuario " + nombreUsuario);
    }

    @Override
    public List<Usuarios> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM USUARIO ORDER BY nombre";
        List<Usuarios> usuarios = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapear(rs));
            }
            return usuarios;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar los usuarios", e);
        }
    }

    private Optional<Usuarios> buscarUno(String sql, Object parametro, String mensajeError) {
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

    private Usuarios mapear(ResultSet rs) throws SQLException {
        return new Usuarios(
                rs.getInt("usuario_id"),
                rs.getString("nombre"),
                rs.getString("usuario"),
                rs.getString("password_hash"),
                rs.getInt("rol_id"),
                rs.getBoolean("activo"),
                UtilJdbc.getFechaHora(rs, "fecha_creacion"));
    }
}
