package Dominio;

public class VentanillaServicio {
    private int ventanillaId;
    private int servicioId;
    private boolean activo = true;

    public VentanillaServicio() {
    }

    public VentanillaServicio(int ventanillaId, int servicioId, boolean activo) {
        setVentanillaId(ventanillaId);
        setServicioId(servicioId);
        setActivo(activo);
    }

    public int getVentanillaId() {
        return ventanillaId;
    }

    public void setVentanillaId(int ventanillaId) {
        this.ventanillaId = Validaciones.idPositivo(ventanillaId, "La ventanilla");
    }

    public int getServicioId() {
        return servicioId;
    }

    public void setServicioId(int servicioId) {
        this.servicioId = Validaciones.idPositivo(servicioId, "El servicio");
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
