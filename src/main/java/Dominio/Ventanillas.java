/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author COTO
 */
public class Ventanillas {
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
   
   public int setVentanillaId(){
       return ventanillaId;
   }
   public void getVentanillaId(int ventanillaId){
       this.ventanillaId = ventanillaId;
   }
   public int setNumero(){
       return numero;
   }
   public void getNumero(int numero){
       this.numero = numero;
   }
   public String setEstado(){
       return estado;
   }
   public void getEstado(String estado){
       this.estado = estado;
   }
   public String setUbicacion(){
       return ubicacion;
   }
   public void getUbicacion(String ubicacion){
       this.ubicacion = ubicacion;
   }
   public boolean isActivoVentanilla(){
       return activo;
   }
   public void getActivoVentanilla(boolean activo){
       this.activo = activo;
   }
   
}
