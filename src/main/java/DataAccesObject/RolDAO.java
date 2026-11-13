package DataAccesObject;

import Dominio.Rol;
import Excepciones.PersistenciaException;
import Repositorios.InterfaceRol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RolDAO implements InterfaceRol {
    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getConexion();
    }

    @Override
    public String obtenerRol(int rolId) {
        return buscarPorId(rolId).map(Rol::getNombre).orElse(null);
    }

    @Override
    public Optional<Rol> buscarPorId(int rolId) {
        String sql = "SELECT rol_id, nombre, descripcion FROM ROL WHERE rol_id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rolId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo obtener el rol " + rolId, e);
        }
    }

    @Override
    public List<Rol> listar() {
        String sql = "SELECT rol_id, nombre, descripcion FROM ROL ORDER BY rol_id";
        List<Rol> roles = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                roles.add(mapear(rs));
            }
            return roles;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar los roles", e);
        }
    }

    private Rol mapear(ResultSet rs) throws SQLException {
        return new Rol(rs.getInt("rol_id"), rs.getString("nombre"), rs.getString("descripcion"));
    }
}
