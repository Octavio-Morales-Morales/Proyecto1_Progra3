/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.io.Serializable;
/**
 *
 * @author moral
 */
public class Servicio implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int servicioId;
    private String nombre;    
    private String letraCodigo;
    private boolean activo;
    
    public Servicio() {
    }
    
    public Servicio(int servicioId, String nombre, String letraCodigo, boolean activo) {
        this.servicioId = servicioId;
        this.nombre = nombre;
        this.letraCodigo = letraCodigo;
        this.activo = activo;
    }
    
    public int getServicioId() {
        return servicioId;
    }

    public void setServicioId(int servicioId) {
        this.servicioId = servicioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLetraCodigo() {
        return letraCodigo;
    }

    public void setLetraCodigo(String letraCodigo) {
        this.letraCodigo = letraCodigo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
