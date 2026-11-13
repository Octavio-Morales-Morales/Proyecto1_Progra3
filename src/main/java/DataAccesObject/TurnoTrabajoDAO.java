package DataAccesObject;

import Dominio.TurnoTrabajo;
import Excepciones.PersistenciaException;
import Repositorios.InterfaceTurnoTrabajo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TurnoTrabajoDAO implements InterfaceTurnoTrabajo {
    private static final String COLUMNAS = "TURNOTRABAJO_Id, funcionario_id, Turno_entrada, Turno_Salida";

    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getConexion();
    }

    @Override
    public void crear(TurnoTrabajo turnoTrabajo) {
        validarSinTraslape(turnoTrabajo);
        String sql = "INSERT INTO TURNOTRABAJO (funcionario_id, Turno_entrada, Turno_Salida) VALUES (?, ?, ?)";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            UtilJdbc.setEnteroNulo(ps, 1, turnoTrabajo.getFuncionarioId());
            UtilJdbc.setFechaHora(ps, 2, turnoTrabajo.getTurnoEntrada());
            UtilJdbc.setFechaHora(ps, 3, turnoTrabajo.getTurnoSalida());
            ps.executeUpdate();
            turnoTrabajo.setTurnoTrabajoId(UtilJdbc.idGenerado(ps));
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo crear el turno de trabajo", e);
        }
    }

    @Override
    public void actualizar(TurnoTrabajo turnoTrabajo) {
        validarSinTraslape(turnoTrabajo);
        String sql = "UPDATE TURNOTRABAJO SET funcionario_id = ?, Turno_entrada = ?, Turno_Salida = ? "
                   + "WHERE TURNOTRABAJO_Id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            UtilJdbc.setEnteroNulo(ps, 1, turnoTrabajo.getFuncionarioId());
            UtilJdbc.setFechaHora(ps, 2, turnoTrabajo.getTurnoEntrada());
            UtilJdbc.setFechaHora(ps, 3, turnoTrabajo.getTurnoSalida());
            ps.setInt(4, turnoTrabajo.getTurnoTrabajoId());
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe el turno de trabajo " + turnoTrabajo.getTurnoTrabajoId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el turno de trabajo " + turnoTrabajo.getTurnoTrabajoId(), e);
        }
    }

    @Override
    public Optional<TurnoTrabajo> buscarPorId(int turnoTrabajoId) {
        String sql = "SELECT " + COLUMNAS + " FROM TURNOTRABAJO WHERE TURNOTRABAJO_Id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, turnoTrabajoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo buscar el turno de trabajo " + turnoTrabajoId, e);
        }
    }

    @Override
    public Optional<TurnoTrabajo> buscarVigente(int funcionarioId, LocalDateTime momento) {
        String sql = "SELECT " + COLUMNAS + " FROM TURNOTRABAJO "
                   + "WHERE funcionario_id = ? AND ? BETWEEN Turno_entrada AND Turno_Salida "
                   + "ORDER BY Turno_entrada DESC LIMIT 1";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, funcionarioId);
            UtilJdbc.setFechaHora(ps, 2, momento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo buscar la jornada vigente del funcionario " + funcionarioId, e);
        }
    }

    @Override
    public List<TurnoTrabajo> listarPorFuncionario(int funcionarioId) {
        String sql = "SELECT " + COLUMNAS + " FROM TURNOTRABAJO WHERE funcionario_id = ? ORDER BY Turno_entrada DESC";
        List<TurnoTrabajo> lista = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, funcionarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar las jornadas del funcionario " + funcionarioId, e);
        }
    }

    @Override
    public boolean existeTraslape(int funcionarioId, LocalDateTime entrada, LocalDateTime salida, int excluirId) {
        String sql = "SELECT COUNT(*) FROM TURNOTRABAJO "
                   + "WHERE funcionario_id = ? AND TURNOTRABAJO_Id <> ? AND Turno_entrada < ? AND Turno_Salida > ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, funcionarioId);
            ps.setInt(2, excluirId);
            UtilJdbc.setFechaHora(ps, 3, salida);
            UtilJdbc.setFechaHora(ps, 4, entrada);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo revisar el horario del funcionario " + funcionarioId, e);
        }
    }

    private void validarSinTraslape(TurnoTrabajo t) {
        if (t.getFuncionarioId() != null
                && existeTraslape(t.getFuncionarioId(), t.getTurnoEntrada(), t.getTurnoSalida(), t.getTurnoTrabajoId())) {
            throw new IllegalStateException("El funcionario ya tiene una jornada que se cruza con ese horario.");
        }
    }

    private TurnoTrabajo mapear(ResultSet rs) throws SQLException {
        return new TurnoTrabajo(
                rs.getInt("TURNOTRABAJO_Id"),
                UtilJdbc.getEnteroNulo(rs, "funcionario_id"),
                UtilJdbc.getFechaHora(rs, "Turno_entrada"),
                UtilJdbc.getFechaHora(rs, "Turno_Salida"));
    }
}
