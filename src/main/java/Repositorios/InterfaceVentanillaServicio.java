/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Repositorios;
import java.util.List;
/**
 *
 * @author COTO
 */
public interface InterfaceVentanillaServicio {
 void asignarServicioVentanilla(int ventanilla, int servicioId);
 List<Integer> ObtenerServiciosVentanilla(int ventanillaId);
 
}
