package Dominio;

public class Ventanillas {
    public static final String DISPONIBLE = "DISPONIBLE";
    public static final String OCUPADA = "OCUPADA";
    public static final String NO_DISPONIBLE = "NO_DISPONIBLE";

    private int ventanillaId;
    private int numero;
    private String estado = DISPONIBLE;
    private String ubicacion;
    private boolean activo = true;

    public Ventanillas() {
    }

    public Ventanillas(int ventanillaId, int numero, String estado, String ubicacion, boolean activo) {
        setVentanillaId(ventanillaId);
        setNumero(numero);
        setEstado(estado);
        setUbicacion(ubicacion);
        setActivo(activo);
    }

    public int getVentanillaId() {
        return ventanillaId;
    }

    public void setVentanillaId(int ventanillaId) {
        this.ventanillaId = Validaciones.idPropio(ventanillaId, "El id de la ventanilla");
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El numero de ventanilla debe ser mayor que cero.");
        }
        this.numero = numero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = Validaciones.texto(estado, "El estado de la ventanilla", 30).toUpperCase();
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = Validaciones.textoOpcional(ubicacion, "La ubicacion", 100);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Ventanilla " + numero;
    }
}
