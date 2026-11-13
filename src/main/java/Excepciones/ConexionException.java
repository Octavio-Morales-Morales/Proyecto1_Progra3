package Excepciones;

public class ConexionException extends RuntimeException {
    public ConexionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
