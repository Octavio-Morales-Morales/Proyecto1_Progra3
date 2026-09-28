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
public class Usuarios {
    private int usuarioId;
    private String nombre;
    private String usuario;
    private String contraseña;
    private int rolId;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    
    public Usuarios(){
        
    }
    public Usuarios(int usuarioId, String nombre, String usuario, String contraseña, int rolId,
    boolean activo, LocalDateTime fechaCreacion){
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rolId = rolId;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        
    }
    
    public int getUsuarioId(){
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId){
        this.usuarioId = usuarioId;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public String getUsuario(){
        return usuario;
    }
    public void setUsuario(String usuario){
        this.usuario = usuario;
    }
    public String getContraseña(){
        return contraseña;
    }
    public void setContraseña(String contraseña){
        this.contraseña = contraseña;
    }
    public int getRolId(){
        return rolId;
    }
    public void setRolId(int rolId){
        this.rolId = rolId;
    }
    public boolean isActivoUsuario(){
        return activo;
    }
    public void setActivoUsuario(boolean activo){
        this.activo = activo;
    }
    public LocalDateTime getFechaCreacion(){
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion){
        this.fechaCreacion = fechaCreacion;
    }
    
}
