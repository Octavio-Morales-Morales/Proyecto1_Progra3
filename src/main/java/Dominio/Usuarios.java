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
    
    public int setUsuarioId(){
        return usuarioId;
    }
    public void getUsuarioId(int usuarioId){
        this.usuarioId = usuarioId;
    }
    public String setNombre(){
        return nombre;
    }
    public void getNombre(String nombre){
        this.nombre = nombre;
    }
    public String setUsuario(){
        return usuario;
    }
    public void getUsuario(String usuario){
        this.usuario = usuario;
    }
    public String setContraseña(){
        return contraseña;
    }
    public void getContraseña(String contraseña){
        this.contraseña = contraseña;
    }
    public int setRolId(){
        return rolId;
    }
    public void getRolId(int rolId){
        this.rolId = rolId;
    }
    public boolean isActivoUsuario(){
        return activo;
    }
    public void getActivoUsuario(boolean activo){
        this.activo = activo;
    }
    public LocalDateTime setFechaCreacion(){
        return fechaCreacion;
    }
    public void getFechaCreacion(LocalDateTime fechaCreacion){
        this.fechaCreacion = fechaCreacion;
    }
    
}
