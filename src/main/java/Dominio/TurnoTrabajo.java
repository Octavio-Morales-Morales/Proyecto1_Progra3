/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author moral
 */
public class TurnoTrabajo {
    private static final long serialVersionUID = 1L;
    
    private int turnoTrabajoId;
    private int funcionarioId; 
    private LocalDateTime turnoEntrada;
    private LocalDateTime turnoSalida;
    
    public TurnoTrabajo(){
        
    }
    
    public TurnoTrabajo( int turnoTrabajoId, int funcionarioId, LocalDateTime turnoEntrada, LocalDateTime turnoSalida) {
        this.turnoTrabajoId = turnoTrabajoId;
        this.funcionarioId = funcionarioId;
        this.turnoEntrada = turnoEntrada;
        this.turnoSalida = turnoSalida;       
    }
    public int getTurnoTrabajoId() {
        return turnoTrabajoId;
    }

    public void setTurnoTrabajoId(int turnoTrabajoId) {
        this.turnoTrabajoId = turnoTrabajoId;
    }

    public int getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(int funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public LocalDateTime getTurnoEntrada() {
        return turnoEntrada;
    }

    public void setTurnoEntrada(LocalDateTime turnoEntrada) {
        this.turnoEntrada = turnoEntrada;
    }

    public LocalDateTime getTurnoSalida() {
        return turnoSalida;
    }

    public void setTurnoSalida(LocalDateTime turnoSalida) {
        this.turnoSalida = turnoSalida;
    }  
       
}
