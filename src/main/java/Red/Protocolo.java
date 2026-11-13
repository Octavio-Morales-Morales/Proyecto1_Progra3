package Red;

import Dominio.ConteoReporte;
import Dominio.DetalleAtencion;
import Dominio.LlamadoTurno;
import Dominio.Servicio;
import Dominio.TurnoEnVentanilla;
import Dominio.Ventanillas;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public final class Protocolo {
    public static final int PUERTO = 12345;
    public static final char SEPARADOR = ';';

    public static final String OK = "OK";
    public static final String ERROR = "ERROR";
    public static final String LLAMADO = "LLAMADO";

    public static final String LOGIN = "LOGIN";
    public static final String SERVICIOS = "SERVICIOS";
    public static final String VENTANILLAS = "VENTANILLAS";
    public static final String ABRIR_VENTANILLA = "ABRIR_VENTANILLA";
    public static final String ESTADO_VENTANILLA = "ESTADO_VENTANILLA";
    public static final String TURNO_ACTUAL = "TURNO_ACTUAL";
    public static final String GENERAR_TURNO = "GENERAR_TURNO";
    public static final String SOLICITAR_SIGUIENTE = "SOLICITAR_SIGUIENTE";
    public static final String COMENZAR_TRAMITE = "COMENZAR_TRAMITE";
    public static final String TERMINAR_TRAMITE = "TERMINAR_TRAMITE";
    public static final String VOLVER_A_LLAMAR = "VOLVER_A_LLAMAR";
    public static final String NO_DISPONIBLE = "NO_DISPONIBLE";
    public static final String DISPONIBLE = "DISPONIBLE";
    public static final String HISTORIAL = "HISTORIAL";
    public static final String ULTIMOS_LLAMADOS = "ULTIMOS_LLAMADOS";
    public static final String REPORTE_ATENDIDOS = "REPORTE_ATENDIDOS";
    public static final String REPORTE_FUNCIONARIOS = "REPORTE_FUNCIONARIOS";
    public static final String REPORTE_SERVICIOS = "REPORTE_SERVICIOS";
    public static final String REGISTRAR_PANTALLA = "REGISTRAR_PANTALLA";
    public static final String NINGUNO = "NINGUNO";

    private Protocolo() {
    }

    public static String armar(Object... campos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) {
                sb.append(SEPARADOR);
            }
            String texto = campos[i] == null ? "" : campos[i].toString();
            for (char c : texto.toCharArray()) {
                if (c == '\\' || c == SEPARADOR) {
                    sb.append('\\').append(c);
                } else if (c == '\n') {
                    sb.append("\\n");
                } else if (c != '\r') {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String[] separar(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (c == '\\' && i + 1 < linea.length()) {
                char siguiente = linea.charAt(++i);
                actual.append(siguiente == 'n' ? '\n' : siguiente);
            } else if (c == SEPARADOR) {
                campos.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(c);
            }
        }
        campos.add(actual.toString());
        return campos.toArray(new String[0]);
    }

    public static Object[] campos(Servicio s) {
        return new Object[]{s.getServicioId(), s.getCodigo(), s.getNombre(), s.getDescripcion(), s.isActivo()};
    }

    public static Servicio servicio(String[] c, int i) {
        return new Servicio(entero(c[i]), c[i + 1], c[i + 2], vacioANulo(c[i + 3]), Boolean.parseBoolean(c[i + 4]));
    }

    public static Object[] campos(Ventanillas v) {
        return new Object[]{v.getVentanillaId(), v.getNumero(), v.getEstado(), v.getUbicacion(), v.isActivo()};
    }

    public static Ventanillas ventanilla(String[] c, int i) {
        return new Ventanillas(entero(c[i]), entero(c[i + 1]), c[i + 2], vacioANulo(c[i + 3]), Boolean.parseBoolean(c[i + 4]));
    }

    public static Object[] campos(TurnoEnVentanilla t) {
        return new Object[]{t.getAtencionId(), t.getTurnoId(), t.getCodigoTurno(), t.getNombreServicio(), t.getEstadoId()};
    }

    public static TurnoEnVentanilla turnoEnVentanilla(String[] c, int i) {
        return new TurnoEnVentanilla(entero(c[i]), entero(c[i + 1]), c[i + 2], c[i + 3], entero(c[i + 4]));
    }

    public static Object[] campos(LlamadoTurno l) {
        return new Object[]{l.getCodigoTurno(), l.getNumeroVentanilla(), l.getHoraLlamado()};
    }

    public static LlamadoTurno llamado(String[] c, int i) {
        return new LlamadoTurno(c[i], entero(c[i + 1]), LocalDateTime.parse(c[i + 2]));
    }

    public static Object[] campos(DetalleAtencion d) {
        return new Object[]{d.getCodigoTurno(), d.getServicio(), d.getFechaGeneracion(), d.getHoraGeneracion(),
            d.getHoraLlamado(), d.getHoraInicio(), d.getHoraFinalizacion(), d.getNumeroVentanilla(),
            d.getFuncionario(), d.getEstadoFinal()};
    }

    public static DetalleAtencion detalle(String[] c, int i) {
        return new DetalleAtencion(c[i], c[i + 1], fecha(c[i + 2]), hora(c[i + 3]),
                fechaHora(c[i + 4]), fechaHora(c[i + 5]), fechaHora(c[i + 6]),
                entero(c[i + 7]), c[i + 8], c[i + 9]);
    }

    public static Object[] campos(ConteoReporte r) {
        return new Object[]{r.getNombre(), r.getCantidad()};
    }

    public static ConteoReporte conteo(String[] c, int i) {
        return new ConteoReporte(c[i], entero(c[i + 1]));
    }

    public static int entero(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Dato numérico inválido en el mensaje: '" + texto + "'");
        }
    }

    public static LocalDate fecha(String texto) {
        return texto == null || texto.isEmpty() ? null : LocalDate.parse(texto);
    }

    public static LocalTime hora(String texto) {
        return texto == null || texto.isEmpty() ? null : LocalTime.parse(texto);
    }

    public static LocalDateTime fechaHora(String texto) {
        return texto == null || texto.isEmpty() ? null : LocalDateTime.parse(texto);
    }

    public static String vacioANulo(String texto) {
        return texto == null || texto.isEmpty() ? null : texto;
    }
}
