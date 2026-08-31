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
        
        this.codigoTurno = codigoTurno;
        this.estadoId = estadoId;
        this.generacion = generacion;
        this.horaGeneracion = horaGeneracion;
        this.estadoId = estadoId;
        this.prioridad = prioridad;
        this.ventanillaId = ventanillaId;
    
    }
    
    public int setTurnoId(){
        return turnoId;
    }
    public void getTurnoId(int turnoId){
        this.turnoId =turnoId;
    }
    
    public String setCodigoTurno(){
        return codigoTurno;
    }
    public void getCodigoTurno(String codigoTurno){
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
    public int setEstadoId(){
        return estadoId;
    }
    public void getEstadoId(int estadoId){
        this.estadoId = estadoId;
    }
    public int setPrioridad(){
        return prioridad;
    }
    public void getPrioridad(int prioridad){
        this.prioridad = prioridad;
    }
    public Integer setVentanillaId(){
        return ventanillaId;
    }
    public void getVentanillaId(Integer ventanillaId){
        this.ventanillaId = ventanillaId;
    }
}
