package Dominio;

import java.util.Objects;

public final class Validaciones {
    private Validaciones() {
    }

    public static String texto(String valor, String campo, int max) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        String limpio = valor.trim();
        if (limpio.length() > max) {
            throw new IllegalArgumentException(campo + " no puede superar los " + max + " caracteres.");
        }
        return limpio;
    }

    public static String textoOpcional(String valor, String campo, int max) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        String limpio = valor.trim();
        if (limpio.length() > max) {
            throw new IllegalArgumentException(campo + " no puede superar los " + max + " caracteres.");
        }
        return limpio;
    }

    public static int idPositivo(int valor, String campo) {
        if (valor <= 0) {
            throw new IllegalArgumentException(campo + " debe ser un numero mayor que cero.");
        }
        return valor;
    }

    public static int idPropio(int valor, String campo) {
        if (valor < 0) {
            throw new IllegalArgumentException(campo + " no puede ser negativo.");
        }
        return valor;
    }

    public static Integer idOpcional(Integer valor, String campo) {
        if (valor != null && valor <= 0) {
            throw new IllegalArgumentException(campo + " debe ser un numero mayor que cero o quedar vacio.");
        }
        return valor;
    }

    public static <T> T requerido(T valor, String campo) {
        return Objects.requireNonNull(valor, campo + " es obligatorio.");
    }
}
