package DataAccesObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

final class UtilJdbc {
    private UtilJdbc() {
    }

    static void setFechaHora(PreparedStatement ps, int i, LocalDateTime valor) throws SQLException {
        if (valor == null) {
            ps.setNull(i, Types.TIMESTAMP);
        } else {
            ps.setObject(i, valor);
        }
    }

    static void setFecha(PreparedStatement ps, int i, LocalDate valor) throws SQLException {
        if (valor == null) {
            ps.setNull(i, Types.DATE);
        } else {
            ps.setObject(i, valor);
        }
    }

    static void setHora(PreparedStatement ps, int i, LocalTime valor) throws SQLException {
        if (valor == null) {
            ps.setNull(i, Types.TIME);
        } else {
            ps.setObject(i, valor);
        }
    }

    static void setEnteroNulo(PreparedStatement ps, int i, Integer valor) throws SQLException {
        if (valor == null) {
            ps.setNull(i, Types.INTEGER);
        } else {
            ps.setInt(i, valor);
        }
    }

    static void setTextoNulo(PreparedStatement ps, int i, String valor) throws SQLException {
        if (valor == null) {
            ps.setNull(i, Types.VARCHAR);
        } else {
            ps.setString(i, valor);
        }
    }

    static LocalDateTime getFechaHora(ResultSet rs, String columna) throws SQLException {
        return rs.getObject(columna, LocalDateTime.class);
    }

    static LocalDate getFecha(ResultSet rs, String columna) throws SQLException {
        return rs.getObject(columna, LocalDate.class);
    }

    static LocalTime getHora(ResultSet rs, String columna) throws SQLException {
        return rs.getObject(columna, LocalTime.class);
    }

    static Integer getEnteroNulo(ResultSet rs, String columna) throws SQLException {
        int valor = rs.getInt(columna);
        return rs.wasNull() ? null : valor;
    }

    static int idGenerado(PreparedStatement ps) throws SQLException {
        try (ResultSet llaves = ps.getGeneratedKeys()) {
            return llaves.next() ? llaves.getInt(1) : 0;
        }
    }
}
