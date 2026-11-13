package DataAccesObject;

import Dominio.Atencion;
import Excepciones.PersistenciaException;
import Repositorios.InterfaceAtencion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AtencionDAO implements InterfaceAtencion {
    private static final String COLUMNAS =
            "atencion_id, turno_id, ventanilla_id, funcionario_id, hora_llamado, hora_inicio, hora_finalizacion, observaciones";

    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getConexion();
    }

    private static LocalDateTime ahora() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @Override
    public void registrarAtencion(Atencion atencion) {
        String sql = "INSERT INTO ATENCION (turno_id, ventanilla_id, funcionario_id, hora_llamado, hora_inicio, "
                   + "hora_finalizacion, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, atencion.getTurnoId());
            ps.setInt(2, atencion.getVentanillaId());
            ps.setInt(3, atencion.getFuncionarioId());
            UtilJdbc.setFechaHora(ps, 4, atencion.getHoraLlamado());
            UtilJdbc.setFechaHora(ps, 5, atencion.getHoraInicio());
            UtilJdbc.setFechaHora(ps, 6, atencion.getHoraFinalizacion());
            UtilJdbc.setTextoNulo(ps, 7, atencion.getObservaciones());
            ps.executeUpdate();
            atencion.setAtencionId(UtilJdbc.idGenerado(ps));
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo registrar la atencion del turno " + atencion.getTurnoId(), e);
        }
    }

    @Override
    public void registrarInicio(int atencionId) {
        Atencion atencion = buscarPorId(atencionId)
                .orElseThrow(() -> new IllegalArgumentException("No existe la atencion " + atencionId));
        if (atencion.getHoraInicio() != null) {
            throw new IllegalStateException("La atencion " + atencionId + " ya fue iniciada.");
        }
        atencion.setHoraInicio(ahora());

        String sql = "UPDATE ATENCION SET hora_inicio = ? WHERE atencion_id = ?";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            UtilJdbc.setFechaHora(ps, 1, atencion.getHoraInicio());
            ps.setInt(2, atencionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo registrar el inicio de la atencion " + atencionId, e);
        }
    }

    @Override
    public void registrarFin(int atencionId, String observaciones) {
        Atencion atencion = buscarPorId(atencionId)
                .orElseThrow(() -> new IllegalArgumentException("No existe la atencion " + atencionId));
        if (atencion.getHoraFinalizacion() != null) {
            throw new IllegalStateException("La atencion " + atencionId + " ya fue finalizada.");
        }
        atencion.setHoraFinalizacion(ahora());
        atencion.setObservaciones(observaciones);

        String sql = "UPDATE ATENCION SET hora_finalizacion = ?, observaciones = ? WHERE atencion_id = ?";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            UtilJdbc.setFechaHora(ps, 1, atencion.getHoraFinalizacion());
            UtilJdbc.setTextoNulo(ps, 2, atencion.getObservaciones());
            ps.setInt(3, atencionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo registrar el fin de la atencion " + atencionId, e);
        }
    }

    @Override
    public int AtendidosPorFuncionarios(int funcionarioId) {
        return contar("SELECT COUNT(*) FROM ATENCION WHERE funcionario_id = ? AND hora_finalizacion IS NOT NULL",
                funcionarioId);
    }

    @Override
    public int TurnosPorServicio(int servicioId) {
        return contar("SELECT COUNT(*) FROM ATENCION a INNER JOIN TURNO t ON a.turno_id = t.turno_id "
                    + "WHERE t.servicio_id = ?", servicioId);
    }

    @Override
    public double PromedioDeEspera() {
        return promedio("SELECT AVG(TIMESTAMPDIFF(MINUTE, TIMESTAMP(t.generacion, t.hora_generacion), a.hora_inicio)) "
                      + "FROM ATENCION a INNER JOIN TURNO t ON a.turno_id = t.turno_id "
                      + "WHERE a.hora_inicio IS NOT NULL");
    }

    @Override
    public double PromedioAtencio() {
        return promedio("SELECT AVG(TIMESTAMPDIFF(MINUTE, hora_inicio, hora_finalizacion)) FROM ATENCION "
                      + "WHERE hora_inicio IS NOT NULL AND hora_finalizacion IS NOT NULL");
    }

    @Override
    public Optional<Atencion> buscarPorId(int atencionId) {
        return buscarUno("SELECT " + COLUMNAS + " FROM ATENCION WHERE atencion_id = ?", atencionId);
    }

    @Override
    public Optional<Atencion> buscarUltimaPorTurno(int turnoId) {
        return buscarUno("SELECT " + COLUMNAS + " FROM ATENCION WHERE turno_id = ? "
                       + "ORDER BY atencion_id DESC LIMIT 1", turnoId);
    }

    @Override
    public Optional<Atencion> buscarActivaPorVentanilla(int ventanillaId) {
        return buscarUno("SELECT " + COLUMNAS + " FROM ATENCION WHERE ventanilla_id = ? AND hora_finalizacion IS NULL "
                       + "ORDER BY atencion_id DESC LIMIT 1", ventanillaId);
    }

    @Override
    public List<Atencion> listarPorFuncionario(int funcionarioId) {
        String sql = "SELECT " + COLUMNAS + " FROM ATENCION WHERE funcionario_id = ? ORDER BY atencion_id DESC";
        List<Atencion> lista = new ArrayList<>();

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
            throw new PersistenciaException("No se pudo listar las atenciones del funcionario " + funcionarioId, e);
        }
    }

    private Optional<Atencion> buscarUno(String sql, int parametro) {
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo buscar la atencion", e);
        }
    }

    private int contar(String sql, int parametro) {
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo calcular el conteo de atenciones", e);
        }
    }

    private double promedio(String sql) {
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo calcular el promedio", e);
        }
    }

    private Atencion mapear(ResultSet rs) throws SQLException {
        return new Atencion(
                rs.getInt("atencion_id"),
                rs.getInt("funcionario_id"),
                UtilJdbc.getFechaHora(rs, "hora_finalizacion"),
                UtilJdbc.getFechaHora(rs, "hora_inicio"),
                UtilJdbc.getFechaHora(rs, "hora_llamado"),
                rs.getString("observaciones"),
                rs.getInt("turno_id"),
                rs.getInt("ventanilla_id"));
    }
}
