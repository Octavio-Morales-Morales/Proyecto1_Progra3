package octavioadriantada.proyectop3;

import Red.ServidorCentral;

public class ProyectoP3 {
    public static void main(String[] args) {
        System.out.println("SISTEMA DISTRIBUIDO DE GESTION DE TURNOS");
        try {
            ServidorCentral servidor = new ServidorCentral();
            servidor.iniciarServidor();
        } catch (Exception e) {
            System.err.println("[CRÍTICO] Fallo al inicializar el Servidor Central: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
