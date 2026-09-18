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
public interface InterfaceVentanilla {
    void cambiarEstado(int ventanillaId, String estado);
    List<Integer> ListaVentanillasActivas();
}
