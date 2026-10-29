/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author COTO
 */
public class Funcionarios {
   private int funcionarioId;
   private int usuarioId;
   private String nombreCompleto;
   private String identificacion;
   private boolean activo;
   
   
   public Funcionarios(){
       
   }
   
   public Funcionarios(int funcionarioId, int usuarioId, String nombreCompleto, String identificacion, boolean activo){
        this.funcionarioId = funcionarioId;
        this.usuarioId = usuarioId;
        this.nombreCompleto = nombreCompleto;
        this.identificacion = identificacion;
        this.activo = activo;    
   }
   
   public int getFuncionarioId() {
        return funcionarioId;
    }

   public void setFuncionarioId(int funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

   public int getUsuarioId() {
        return usuarioId;
    }

   public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

   public String getNombreCompleto() {
        return nombreCompleto;
    }

   public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

   public String getIdentificacion() {
        return identificacion;
    }

   public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

   public boolean isActivo() {
        return activo;
    }

   public void setActivo(boolean activo) {
        this.activo = activo;
    }   
}
