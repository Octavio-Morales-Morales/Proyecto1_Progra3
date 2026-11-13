package DataAccesObject;

import Dominio.Servicio;
import Excepciones.PersistenciaException;
import Repositorios.InterfaceServicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicioDAO implements InterfaceServicio {
    private static final String COLUMNAS = "servicio_id, codigo, nombre, descripcion, activo";

    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getConexion();
    }

    @Override
    public List<String> ListarServiciosDisponibles() {
        List<String> nombres = new ArrayList<>();
        for (Servicio s : listarActivos()) {
            nombres.add(s.getNombre());
        }
        return nombres;
    }

    @Override
    public int obtenerNombreServicio(String nombreServicio) {
        String sql = "SELECT servicio_id FROM SERVICIO WHERE nombre = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreServicio == null ? null : nombreServicio.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("servicio_id") : 0;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo obtener el servicio " + nombreServicio, e);
        }
    }

    @Override
    public void crear(Servicio servicio) {
        String sql = "INSERT INTO SERVICIO (codigo, nombre, descripcion, activo) VALUES (?, ?, ?, ?)";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, servicio.getCodigo());
            ps.setString(2, servicio.getNombre());
            UtilJdbc.setTextoNulo(ps, 3, servicio.getDescripcion());
            ps.setBoolean(4, servicio.isActivo());
            ps.executeUpdate();
            servicio.setServicioId(UtilJdbc.idGenerado(ps));
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo crear el servicio " + servicio.getCodigo(), e);
        }
    }

    @Override
    public void actualizar(Servicio servicio) {
        String sql = "UPDATE SERVICIO SET codigo = ?, nombre = ?, descripcion = ?, activo = ? WHERE servicio_id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, servicio.getCodigo());
            ps.setString(2, servicio.getNombre());
            UtilJdbc.setTextoNulo(ps, 3, servicio.getDescripcion());
            ps.setBoolean(4, servicio.isActivo());
            ps.setInt(5, servicio.getServicioId());
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe el servicio " + servicio.getServicioId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el servicio " + servicio.getServicioId(), e);
        }
    }

    @Override
    public Optional<Servicio> buscarPorId(int servicioId) {
        return buscarUno("SELECT " + COLUMNAS + " FROM SERVICIO WHERE servicio_id = ?", servicioId,
                "No se pudo buscar el servicio " + servicioId);
    }

    @Override
    public Optional<Servicio> buscarPorCodigo(String codigo) {
        return buscarUno("SELECT " + COLUMNAS + " FROM SERVICIO WHERE codigo = ?",
                codigo == null ? null : codigo.trim().toUpperCase(),
                "No se pudo buscar el servicio " + codigo);
    }

    @Override
    public List<Servicio> listarActivos() {
        return listar("SELECT " + COLUMNAS + " FROM SERVICIO WHERE activo = 1 ORDER BY codigo");
    }

    @Override
    public List<Servicio> listarTodos() {
        return listar("SELECT " + COLUMNAS + " FROM SERVICIO ORDER BY codigo");
    }

    private Optional<Servicio> buscarUno(String sql, Object parametro, String mensajeError) {
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

    private List<Servicio> listar(String sql) {
        List<Servicio> servicios = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                servicios.add(mapear(rs));
            }
            return servicios;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar los servicios", e);
        }
    }

    private Servicio mapear(ResultSet rs) throws SQLException {
        return new Servicio(
                rs.getInt("servicio_id"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBoolean("activo"));
    }
}
