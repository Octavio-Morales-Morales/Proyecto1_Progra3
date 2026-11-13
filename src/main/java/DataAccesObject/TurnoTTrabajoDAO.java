package DataAccesObject;

import Dominio.TurnoTTrabajo;
import Excepciones.PersistenciaException;
import Repositorios.InterfaceTurnoTTrabajo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TurnoTTrabajoDAO implements InterfaceTurnoTTrabajo {
    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getConexion();
    }

    @Override
    public void asociar(int turnoTrabajoId, int turnoId) {
        TurnoTTrabajo relacion = new TurnoTTrabajo(turnoTrabajoId, turnoId);
        String sql = "INSERT INTO TURNO_TTRABAJO (TURNO_TRABAJO_Id, TURNO_Id) VALUES (?, ?)";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, relacion.getTurnoTrabajoId());
            ps.setInt(2, relacion.getTurnoId());
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalStateException(
                    "El turno " + turnoId + " ya esta asociado a una jornada, o la jornada/turno no existe.", e);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo asociar el turno " + turnoId + " a la jornada " + turnoTrabajoId, e);
        }
    }

    @Override
    public Optional<Integer> obtenerTurnoTrabajoDeTurno(int turnoId) {
        String sql = "SELECT TURNO_TRABAJO_Id FROM TURNO_TTRABAJO WHERE TURNO_Id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, turnoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(rs.getInt(1)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar la jornada del turno " + turnoId, e);
        }
    }

    @Override
    public List<Integer> listarTurnosDeTurnoTrabajo(int turnoTrabajoId) {
        String sql = "SELECT TURNO_Id FROM TURNO_TTRABAJO WHERE TURNO_TRABAJO_Id = ? ORDER BY TURNO_Id";
        List<Integer> turnos = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, turnoTrabajoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    turnos.add(rs.getInt(1));
                }
            }
            return turnos;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar los turnos de la jornada " + turnoTrabajoId, e);
        }
    }
}
