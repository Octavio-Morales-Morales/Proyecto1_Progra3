package DataAccesObject;

import Dominio.Validaciones;
import Dominio.VentanillaServicio;
import Excepciones.PersistenciaException;
import Repositorios.InterfaceVentanillaServicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentanillaServicioDAO implements InterfaceVentanillaServicio {
    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getConexion();
    }

    @Override
    public void asignarServicioVentanilla(int ventanilla, int servicioId) {
        Validaciones.idPositivo(ventanilla, "La ventanilla");
        Validaciones.idPositivo(servicioId, "El servicio");
        String sql = "INSERT INTO VENTANILLA_SERVICIO (ventanilla_id, servicio_id, activo) VALUES (?, ?, 1) "
                   + "ON DUPLICATE KEY UPDATE activo = 1";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ventanilla);
            ps.setInt(2, servicioId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo asignar el servicio " + servicioId + " a la ventanilla " + ventanilla, e);
        }
    }

    @Override
    public void quitarServicioVentanilla(int ventanillaId, int servicioId) {
        String sql = "UPDATE VENTANILLA_SERVICIO SET activo = 0 WHERE ventanilla_id = ? AND servicio_id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ventanillaId);
            ps.setInt(2, servicioId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo quitar el servicio " + servicioId + " de la ventanilla " + ventanillaId, e);
        }
    }

    @Override
    public List<Integer> ObtenerServiciosVentanilla(int ventanillaId) {
        return listarIds("SELECT servicio_id FROM VENTANILLA_SERVICIO WHERE ventanilla_id = ? AND activo = 1",
                ventanillaId, "servicio_id");
    }

    @Override
    public List<Integer> obtenerVentanillasPorServicio(int servicioId) {
        return listarIds("SELECT ventanilla_id FROM VENTANILLA_SERVICIO WHERE servicio_id = ? AND activo = 1",
                servicioId, "ventanilla_id");
    }

    @Override
    public List<VentanillaServicio> listarPorVentanilla(int ventanillaId) {
        String sql = "SELECT ventanilla_id, servicio_id, activo FROM VENTANILLA_SERVICIO WHERE ventanilla_id = ?";
        List<VentanillaServicio> lista = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ventanillaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new VentanillaServicio(
                            rs.getInt("ventanilla_id"),
                            rs.getInt("servicio_id"),
                            rs.getBoolean("activo")));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar los servicios de la ventanilla " + ventanillaId, e);
        }
    }

    private List<Integer> listarIds(String sql, int parametro, String columna) {
        List<Integer> ids = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt(columna));
                }
            }
            return ids;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar VENTANILLA_SERVICIO", e);
        }
    }
}
