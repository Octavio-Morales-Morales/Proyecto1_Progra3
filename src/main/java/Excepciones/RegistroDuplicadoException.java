package Excepciones;

public class RegistroDuplicadoException extends PersistenciaException {
    public RegistroDuplicadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
