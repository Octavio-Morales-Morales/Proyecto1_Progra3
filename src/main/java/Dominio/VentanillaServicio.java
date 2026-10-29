/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author COTO
 */
public class VentanillaServicio {
    private int ventanillaId;
    private int servicioId;
    private boolean activo;
    
    public VentanillaServicio(){
        
    }
    public VentanillaServicio(int ventanillaId, int servicioId, boolean activo){
        this.ventanillaId = ventanillaId;
        this.servicioId = servicioId;
        this.activo =activo;
    }
        public int getVentanillaId() {
        return ventanillaId;
    }

    public void setVentanillaId(int ventanillaId) {
        this.ventanillaId = ventanillaId;
    }

    public int getServicioId() {
        return servicioId;
    }

    public void setServicioId(int servicioId) {
        this.servicioId = servicioId;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
