/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.time.LocalDateTime;
/**
 *
 * @author COTO
 */
public class Atencion {
   private int atencionId;
   private int funcionarioId;
   private LocalDateTime  horaFinalizacion;
   private LocalDateTime  horaInicio;
   private LocalDateTime  horaLlamado;
   private String observaciones;
   private int turnoId;
   private int ventanillaId;
    
    public Atencion(){
        
    }
    
    public Atencion(int atencionId, int funcionarioId, LocalDateTime horaFinalizacion, LocalDateTime horaInicio,LocalDateTime horaLlamado,
    String observaciones,int turnoId,int ventanillaId){
    
    this.atencionId = atencionId;
    this.funcionarioId = funcionarioId;
    this.horaFinalizacion = horaFinalizacion;
    this.horaInicio = horaInicio;
    this.horaLlamado = horaLlamado;
    this.observaciones = observaciones;
    this.turnoId = turnoId;
    this.ventanillaId = ventanillaId;
    
    }
    
public int getAtencionId(){ return atencionId; }
    
public void setAtencionId(int atencionId){
    this.atencionId = atencionId; }

    
public int getTurnoId() { return turnoId; }
   
public void setTurnoId(int turnoId){ 
    this.turnoId = turnoId; }

    
public int getVentanillaId() { return ventanillaId; }
    
public void setVentanillaId(int ventanillaId){
    this.ventanillaId = ventanillaId; }

   
public int getFuncionarioId() { return funcionarioId; }
   
public void setFuncionarioId(int funcionarioId){ 
    this.funcionarioId = funcionarioId; }

   
public LocalDateTime getHoraLlamado() { return horaLlamado; }
   
public void setHoraLlamado(LocalDateTime  horaLlamado){ 
    this.horaLlamado = horaLlamado; }

    
public LocalDateTime getHoraInicio() { return horaInicio; }
    
public void setHoraInicio(LocalDateTime horaInicio){ 
    this.horaInicio = horaInicio; }

    
public LocalDateTime  getHoraFinalizacion() { return horaFinalizacion; }
    
public void setHoraFinalizacion(LocalDateTime  horaFinalizacion){ 
    this.horaFinalizacion = horaFinalizacion; }

   
public String getObservaciones() { return observaciones; }
    
public void setObservaciones(String observaciones){ 
    this.observaciones = observaciones; }
}
            
    

