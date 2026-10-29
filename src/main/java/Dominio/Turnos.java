/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author COTO
 */
public class Turnos {
    private int turnoId;
    private String codigoTurno;
    private int servicioId;
    private LocalDate generacion;
    private LocalTime horaGeneracion;
    private int estadoId;
    private int prioridad;
    private Integer ventanillaId;  
    
    public Turnos(){
        
    }
    
    public Turnos(int turnoId, String codigoTurno, int servicioId, LocalDate generacion, 
    LocalTime horaGeneracion, int estadoId, int prioridad, Integer ventanillaId){
        this.turnoId = turnoId;
        this.codigoTurno = codigoTurno;
        this.estadoId = estadoId;
        this.generacion = generacion;
        this.horaGeneracion = horaGeneracion;
        this.estadoId = estadoId;
        this.prioridad = prioridad;
        this.ventanillaId = ventanillaId;
    
    }
    
    public int getTurnoId(){
        return turnoId;
    }
    public void setTurnoId(int turnoId){
        this.turnoId =turnoId;
    }
    
    public String getCodigoTurno(){
        return codigoTurno;
    }
    public void setCodigoTurno(String codigoTurno){
        this.codigoTurno = codigoTurno;
    }
    public int getServicioId(){
        return servicioId;
    }
    public void setServicioId(int servicioId){
        this.servicioId = servicioId;
    }
    public LocalDate getGeneracion(){
        return generacion;
    }
    public void setGeneracion(LocalDate generacion){
        this.generacion = generacion;
    }
    public LocalTime getHoraGeneracion(){
        return horaGeneracion;
    }
    public void sethoraGeneracion(LocalTime horaGeneracion){
        this.horaGeneracion = horaGeneracion;
    }
    public int getEstadoId(){
        return estadoId;
    }
    public void setEstadoId(int estadoId){
        this.estadoId = estadoId;
    }
    public int getPrioridad(){
        return prioridad;
    }
    public void setPrioridad(int prioridad){
        this.prioridad = prioridad;
    }
    public Integer getVentanillaId(){
        return ventanillaId;
    }
    public void setVentanillaId(Integer ventanillaId){
        this.ventanillaId = ventanillaId;
    }
}
