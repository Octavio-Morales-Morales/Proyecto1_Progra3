/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.io.Serializable;

/**
 *
 * @author COTO
 */
public class Ventanillas implements Serializable{
    
   private static final long serialVersionIUD = 1L;
   
   private int ventanillaId;
   private int numero;
   private String estado;
   private String ubicacion;
   private boolean activo;
   
   public Ventanillas(){
       
   }
   
   public Ventanillas(int ventanillaId, int numero, String estado, String ubicacion, boolean activo){
       this.ventanillaId = ventanillaId;
       this.numero = numero;
       this.estado = estado;
       this.ubicacion = ubicacion;
       this.activo = activo;
   }
   
   public int getVentanillaId(){
       return ventanillaId;
   }
   public void setVentanillaId(int ventanillaId){
       this.ventanillaId = ventanillaId;
   }
   public int getNumero(){
       return numero;
   }
   public void setNumero(int numero){
       this.numero = numero;
   }
   public String getEstado(){
       return estado;
   }
   public void setEstado(String estado){
       this.estado = estado;
   }
   public String getUbicacion(){
       return ubicacion;
   }
   public void setUbicacion(String ubicacion){
       this.ubicacion = ubicacion;
   }
   public boolean isActivoVentanilla(){
       return activo;
   }
   public void setActivoVentanilla(boolean activo){
       this.activo = activo;
   }
   
}
