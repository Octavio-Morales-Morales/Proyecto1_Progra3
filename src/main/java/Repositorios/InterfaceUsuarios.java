/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Repositorios;
import Dominio.Usuarios;
/**
 *
 * @author COTO
 */
public interface InterfaceUsuarios {
   Usuarios autenticar(String nombreusuario, String Contraseña);
   void crear(Usuarios usuario);
   void actualizar(Usuarios usuario);
   
}
