/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Dominio;

/**
 *
 * @author moral
 */
public enum EstadoTurno {
    EN_ESPERA(1),
    LLAMADO(2),
    EN_ATENCION(3),
    FINALIZADO(4);

    private final int id;

    EstadoTurno(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }    
}
