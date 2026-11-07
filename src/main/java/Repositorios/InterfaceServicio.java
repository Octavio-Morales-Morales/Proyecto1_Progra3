/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Repositorios;
import java.util.List;
import Dominio.Servicio;
/**
 *
 * @author COTO
 */
public interface InterfaceServicio {
    List<Servicio> ListarServiciosDisponibles();
    int obtenerNombreServicio(String nombreServicio);
}
