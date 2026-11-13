package DataAccesObject;

import Dominio.EstadoTurno;
import Dominio.LlamadoTurno;
import Dominio.Turnos;
import Excepciones.PersistenciaException;
import Excepciones.RegistroDuplicadoException;
import Repositorios.InterfaceTurno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TurnoDAO implements InterfaceTurno {
    private static final String COLUMNAS =
            "t.turno_id, t.codigo_turno, t.servicio_id, t.generacion, t.hora_generacion, t.estado_id, t.prioridad, t.ventanilla_id";

    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getConexion();
    }

    @Override
    public void crearTurno(Turnos turno) {
        if (turno.getGeneracion() == null) {
            turno.setGeneracion(LocalDate.now());
        }
        if (turno.getHoraGeneracion() == null) {
            turno.setHoraGeneracion(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
        }

        String sql = "INSERT INTO TURNO (codigo_turno, servicio_id, generacion, hora_generacion, estado_id, prioridad, ventanilla_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, turno.getCodigoTurno());
            ps.setInt(2, turno.getServicioId());
            UtilJdbc.setFecha(ps, 3, turno.getGeneracion());
            UtilJdbc.setHora(ps, 4, turno.getHoraGeneracion());
            ps.setInt(5, turno.getEstadoId());
            ps.setInt(6, turno.getPrioridad());
            UtilJdbc.setEnteroNulo(ps, 7, turno.getVentanillaId());
            ps.executeUpdate();
            turno.setTurnoId(UtilJdbc.idGenerado(ps));
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RegistroDuplicadoException("Ya existe el turno " + turno.getCodigoTurno(), e);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo crear el turno " + turno.getCodigoTurno(), e);
        }
    }

    @Override
    public boolean tomarTurno(int turnoId, int ventanillaId) {
        String sql = "UPDATE TURNO SET estado_id = ?, ventanilla_id = ? WHERE turno_id = ? AND estado_id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, EstadoTurno.LLAMADO);
            ps.setInt(2, ventanillaId);
            ps.setInt(3, turnoId);
            ps.setInt(4, EstadoTurno.EN_ESPERA);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo tomar el turno " + turnoId, e);
        }
    }

    @Override
    public int contarTurnosDelDia(int servicioId, LocalDate fecha) {
        String sql = "SELECT COUNT(*) FROM TURNO WHERE servicio_id = ? AND generacion = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, servicioId);
            UtilJdbc.setFecha(ps, 2, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo contar los turnos del servicio " + servicioId, e);
        }
    }

    @Override
    public void ActualizarTurno(int turnoId, int estadoId) {
        String sql = "UPDATE TURNO SET estado_id = ? WHERE turno_id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estadoId);
            ps.setInt(2, turnoId);
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe el turno " + turnoId);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el estado del turno " + turnoId, e);
        }
    }

    @Override
    public Turnos ObtenerSiguienteTurno(List<Integer> ServicioId) {
        if (ServicioId == null || ServicioId.isEmpty()) {
            return null;
        }

        StringBuilder signos = new StringBuilder();
        for (int i = 0; i < ServicioId.size(); i++) {
            signos.append(i == 0 ? "?" : ", ?");
        }

        String sql = "SELECT " + COLUMNAS + " FROM TURNO t "
                   + "WHERE t.estado_id = ? AND t.servicio_id IN (" + signos + ") "
                   + "ORDER BY t.prioridad DESC, t.generacion ASC, t.hora_generacion ASC, t.turno_id ASC LIMIT 1";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, EstadoTurno.EN_ESPERA);
            for (int i = 0; i < ServicioId.size(); i++) {
                ps.setInt(i + 2, ServicioId.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo obtener el siguiente turno", e);
        }
    }

    @Override
    public List<Turnos> ObtenerTurnoPorFecha(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT " + COLUMNAS + " FROM TURNO t WHERE t.generacion BETWEEN ? AND ? "
                   + "ORDER BY t.generacion ASC, t.hora_generacion ASC";
        List<Turnos> lista = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            UtilJdbc.setFecha(ps, 1, inicio);
            UtilJdbc.setFecha(ps, 2, fin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo obtener los turnos entre " + inicio + " y " + fin, e);
        }
    }

    @Override
    public Optional<Turnos> buscarPorId(int turnoId) {
        return buscarUno("SELECT " + COLUMNAS + " FROM TURNO t WHERE t.turno_id = ?", turnoId);
    }

    @Override
    public Optional<Turnos> buscarPorCodigo(String codigoTurno) {
        return buscarUno("SELECT " + COLUMNAS + " FROM TURNO t WHERE t.codigo_turno = ?",
                codigoTurno == null ? null : codigoTurno.trim().toUpperCase());
    }

    @Override
    public void asignarVentanilla(int turnoId, Integer ventanillaId) {
        String sql = "UPDATE TURNO SET ventanilla_id = ? WHERE turno_id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            UtilJdbc.setEnteroNulo(ps, 1, ventanillaId);
            ps.setInt(2, turnoId);
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe el turno " + turnoId);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo asignar la ventanilla al turno " + turnoId, e);
        }
    }

    @Override
    public List<Turnos> listarPorEstado(int estadoId) {
        String sql = "SELECT " + COLUMNAS + " FROM TURNO t WHERE t.estado_id = ? "
                   + "ORDER BY t.prioridad DESC, t.generacion ASC, t.hora_generacion ASC";
        List<Turnos> lista = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estadoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar los turnos en estado " + estadoId, e);
        }
    }

    @Override
    public List<LlamadoTurno> listarUltimosLlamados(LocalDate fecha, int limite) {
        if (limite <= 0) {
            throw new IllegalArgumentException("El limite debe ser mayor que cero.");
        }
        String sql = "SELECT t.codigo_turno, v.numero, a.hora_llamado FROM ATENCION a "
                   + "INNER JOIN TURNO t ON t.turno_id = a.turno_id "
                   + "INNER JOIN VENTANILLA v ON v.ventanilla_id = a.ventanilla_id "
                   + "WHERE a.hora_llamado >= ? AND a.hora_llamado < ? "
                   + "ORDER BY a.hora_llamado DESC, a.atencion_id DESC LIMIT ?";
        List<LlamadoTurno> lista = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            UtilJdbc.setFechaHora(ps, 1, fecha.atStartOfDay());
            UtilJdbc.setFechaHora(ps, 2, fecha.plusDays(1).atStartOfDay());
            ps.setInt(3, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new LlamadoTurno(
                            rs.getString("codigo_turno"),
                            rs.getInt("numero"),
                            UtilJdbc.getFechaHora(rs, "hora_llamado")));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar los ultimos turnos llamados", e);
        }
    }

    private Optional<Turnos> buscarUno(String sql, Object parametro) {
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo buscar el turno " + parametro, e);
        }
    }

    private Turnos mapear(ResultSet rs) throws SQLException {
        return new Turnos(
                rs.getInt("turno_id"),
                rs.getString("codigo_turno"),
                rs.getInt("servicio_id"),
                UtilJdbc.getFecha(rs, "generacion"),
                UtilJdbc.getHora(rs, "hora_generacion"),
                rs.getInt("estado_id"),
                rs.getInt("prioridad"),
                UtilJdbc.getEnteroNulo(rs, "ventanilla_id"));
    }
}
