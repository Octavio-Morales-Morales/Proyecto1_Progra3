package DataAccesObject;

import Dominio.ConteoReporte;
import Dominio.DetalleAtencion;
import Dominio.EstadoTurno;
import Excepciones.PersistenciaException;
import Repositorios.InterfaceReportes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO implements InterfaceReportes {
    private static final String SELECT_DETALLE =
            "SELECT t.codigo_turno, s.nombre AS servicio, t.generacion, t.hora_generacion, "
          + "a.hora_llamado, a.hora_inicio, a.hora_finalizacion, v.numero AS ventanilla, "
          + "f.nombre_completo AS funcionario, e.nombre AS estado "
          + "FROM ATENCION a "
          + "INNER JOIN TURNO t ON t.turno_id = a.turno_id "
          + "INNER JOIN SERVICIO s ON s.servicio_id = t.servicio_id "
          + "INNER JOIN VENTANILLA v ON v.ventanilla_id = a.ventanilla_id "
          + "INNER JOIN FUNCIONARIO f ON f.funcionario_id = a.funcionario_id "
          + "INNER JOIN ESTADO_TURNO e ON e.estado_id = t.estado_id ";

    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getConexion();
    }

    @Override
    public List<DetalleAtencion> turnosAtendidos(LocalDate desde, LocalDate hasta) {
        String sql = SELECT_DETALLE
                   + "WHERE t.generacion BETWEEN ? AND ? AND t.estado_id = ? AND a.hora_finalizacion IS NOT NULL "
                   + "ORDER BY t.generacion, t.hora_generacion";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            UtilJdbc.setFecha(ps, 1, desde);
            UtilJdbc.setFecha(ps, 2, hasta);
            ps.setInt(3, EstadoTurno.FINALIZADO);
            return leerDetalles(ps);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar los turnos atendidos", e);
        }
    }

    @Override
    public List<ConteoReporte> atencionPorFuncionario(LocalDate desde, LocalDate hasta) {
        String sql = "SELECT f.nombre_completo AS nombre, COUNT(a.atencion_id) AS cantidad "
                   + "FROM FUNCIONARIO f "
                   + "LEFT JOIN ATENCION a ON a.funcionario_id = f.funcionario_id "
                   + "  AND a.hora_finalizacion >= ? AND a.hora_finalizacion < ? "
                   + "GROUP BY f.funcionario_id, f.nombre_completo "
                   + "ORDER BY cantidad DESC, nombre";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            UtilJdbc.setFechaHora(ps, 1, desde.atStartOfDay());
            UtilJdbc.setFechaHora(ps, 2, hasta.plusDays(1).atStartOfDay());
            return leerConteos(ps);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar la atencion por funcionario", e);
        }
    }

    @Override
    public List<ConteoReporte> turnosPorServicio(LocalDate desde, LocalDate hasta) {
        String sql = "SELECT CONCAT(s.codigo, ' - ', s.nombre) AS nombre, COUNT(t.turno_id) AS cantidad "
                   + "FROM SERVICIO s "
                   + "LEFT JOIN TURNO t ON t.servicio_id = s.servicio_id AND t.generacion BETWEEN ? AND ? "
                   + "GROUP BY s.servicio_id, s.codigo, s.nombre "
                   + "ORDER BY s.codigo";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            UtilJdbc.setFecha(ps, 1, desde);
            UtilJdbc.setFecha(ps, 2, hasta);
            return leerConteos(ps);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar los turnos por servicio", e);
        }
    }

    @Override
    public List<DetalleAtencion> historialVentanilla(int ventanillaId, int limite) {
        String sql = SELECT_DETALLE + "WHERE a.ventanilla_id = ? ORDER BY a.atencion_id DESC LIMIT ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ventanillaId);
            ps.setInt(2, limite);
            return leerDetalles(ps);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar el historial de la ventanilla " + ventanillaId, e);
        }
    }

    private List<DetalleAtencion> leerDetalles(PreparedStatement ps) throws SQLException {
        List<DetalleAtencion> lista = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new DetalleAtencion(
                        rs.getString("codigo_turno"),
                        rs.getString("servicio"),
                        UtilJdbc.getFecha(rs, "generacion"),
                        UtilJdbc.getHora(rs, "hora_generacion"),
                        UtilJdbc.getFechaHora(rs, "hora_llamado"),
                        UtilJdbc.getFechaHora(rs, "hora_inicio"),
                        UtilJdbc.getFechaHora(rs, "hora_finalizacion"),
                        rs.getInt("ventanilla"),
                        rs.getString("funcionario"),
                        rs.getString("estado")));
            }
        }
        return lista;
    }

    private List<ConteoReporte> leerConteos(PreparedStatement ps) throws SQLException {
        List<ConteoReporte> lista = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new ConteoReporte(rs.getString("nombre"), rs.getInt("cantidad")));
            }
        }
        return lista;
    }
}
