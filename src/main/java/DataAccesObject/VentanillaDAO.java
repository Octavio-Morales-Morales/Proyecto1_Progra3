package DataAccesObject;

import Dominio.Validaciones;
import Dominio.Ventanillas;
import Excepciones.PersistenciaException;
import Repositorios.InterfaceVentanilla;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VentanillaDAO implements InterfaceVentanilla {
    private static final String COLUMNAS = "ventanilla_id, numero, estado, ubicacion, activo";

    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getConexion();
    }

    @Override
    public void cambiarEstado(int ventanillaId, String estado) {
        String limpio = Validaciones.texto(estado, "El estado de la ventanilla", 30).toUpperCase();
        String sql = "UPDATE VENTANILLA SET estado = ? WHERE ventanilla_id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, limpio);
            ps.setInt(2, ventanillaId);
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe la ventanilla " + ventanillaId);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo cambiar el estado de la ventanilla " + ventanillaId, e);
        }
    }

    @Override
    public List<Integer> ListaVentanillasActivas() {
        String sql = "SELECT ventanilla_id FROM VENTANILLA WHERE activo = 1 ORDER BY numero";
        List<Integer> activas = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                activas.add(rs.getInt("ventanilla_id"));
            }
            return activas;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar las ventanillas activas", e);
        }
    }

    @Override
    public void crear(Ventanillas ventanilla) {
        String sql = "INSERT INTO VENTANILLA (numero, estado, ubicacion, activo) VALUES (?, ?, ?, ?)";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ventanilla.getNumero());
            ps.setString(2, ventanilla.getEstado());
            UtilJdbc.setTextoNulo(ps, 3, ventanilla.getUbicacion());
            ps.setBoolean(4, ventanilla.isActivo());
            ps.executeUpdate();
            ventanilla.setVentanillaId(UtilJdbc.idGenerado(ps));
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo crear la ventanilla " + ventanilla.getNumero(), e);
        }
    }

    @Override
    public void actualizar(Ventanillas ventanilla) {
        String sql = "UPDATE VENTANILLA SET numero = ?, estado = ?, ubicacion = ?, activo = ? WHERE ventanilla_id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ventanilla.getNumero());
            ps.setString(2, ventanilla.getEstado());
            UtilJdbc.setTextoNulo(ps, 3, ventanilla.getUbicacion());
            ps.setBoolean(4, ventanilla.isActivo());
            ps.setInt(5, ventanilla.getVentanillaId());
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe la ventanilla " + ventanilla.getVentanillaId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar la ventanilla " + ventanilla.getVentanillaId(), e);
        }
    }

    @Override
    public Optional<Ventanillas> buscarPorId(int ventanillaId) {
        return buscarUno("SELECT " + COLUMNAS + " FROM VENTANILLA WHERE ventanilla_id = ?", ventanillaId);
    }

    @Override
    public Optional<Ventanillas> buscarPorNumero(int numero) {
        return buscarUno("SELECT " + COLUMNAS + " FROM VENTANILLA WHERE numero = ?", numero);
    }

    @Override
    public List<Ventanillas> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM VENTANILLA ORDER BY numero";
        List<Ventanillas> ventanillas = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ventanillas.add(mapear(rs));
            }
            return ventanillas;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar las ventanillas", e);
        }
    }

    private Optional<Ventanillas> buscarUno(String sql, int parametro) {
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo buscar la ventanilla " + parametro, e);
        }
    }

    private Ventanillas mapear(ResultSet rs) throws SQLException {
        return new Ventanillas(
                rs.getInt("ventanilla_id"),
                rs.getInt("numero"),
                rs.getString("estado"),
                rs.getString("ubicacion"),
                rs.getBoolean("activo"));
    }
}
