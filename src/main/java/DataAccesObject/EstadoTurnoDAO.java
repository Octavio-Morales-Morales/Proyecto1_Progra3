package DataAccesObject;

import Dominio.EstadoTurno;
import Excepciones.PersistenciaException;
import Repositorios.InterfaceEstadoTurno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EstadoTurnoDAO implements InterfaceEstadoTurno {
    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getConexion();
    }

    @Override
    public String obtenerEstado(int estadoId) {
        String sql = "SELECT nombre FROM ESTADO_TURNO WHERE estado_id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estadoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("nombre") : null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo obtener el estado " + estadoId, e);
        }
    }

    @Override
    public Optional<EstadoTurno> buscarPorNombre(String nombre) {
        String sql = "SELECT estado_id, nombre FROM ESTADO_TURNO WHERE nombre = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre == null ? null : nombre.trim().toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo buscar el estado " + nombre, e);
        }
    }

    @Override
    public List<EstadoTurno> listar() {
        String sql = "SELECT estado_id, nombre FROM ESTADO_TURNO ORDER BY estado_id";
        List<EstadoTurno> estados = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                estados.add(mapear(rs));
            }
            return estados;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar los estados de turno", e);
        }
    }

    private EstadoTurno mapear(ResultSet rs) throws SQLException {
        return new EstadoTurno(rs.getInt("estado_id"), rs.getString("nombre"));
    }
}
