/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Repositorios;
import Dominio.Atencion;
import java.util.List;
/**
 *
 * @author COTO
 */
public interface InterfaceAtencion {
    void registrarAtencion(Atencion atencion);
    void registrarInicio(int atencionId);
    void registrarFin(int atencionId, String observaciones);
    int AtendidosPorFuncionarios(int funcionarioId);
    int TurnosPorServicio(int servicioId);
    double PromedioDeEspera();
    double PromedioAtencio();
}
