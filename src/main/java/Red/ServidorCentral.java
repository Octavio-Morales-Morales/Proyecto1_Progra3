/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Red;

import DataAccesObject.AtencionDAO;
import DataAccesObject.TurnoDAO;
import Repositorios.InterfaceAtencion;
import Repositorios.InterfaceTurno;
import Servicios.AtencionService;
import Servicios.TurnoService;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author moral
 */
public class ServidorCentral {
    private static final int PUERTO = 12345;
    private TurnoService turnoService;
    private AtencionService atencionService;
    private final Set<HiloCliente> pantallasConectadas = Collections.synchronizedSet(new HashSet<>());
    
    public void iniciarServidor(){
        System.out.println("Inicializando componentes ");
    
        try{
            
            InterfaceTurno turnoDAO = new TurnoDAO();
            InterfaceAtencion atencionDAO = new AtencionDAO();
            this.turnoService = new TurnoService(turnoDAO);
            this.atencionService = new AtencionService(atencionDAO, turnoDAO);
            
            System.out.println("Datos y servicios cargados ");
            
        }catch (Exception e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            return;
        }
        
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)){
            System.err.println("Escuchando en el puerto: " + PUERTO + " Y esperando conexiones de clientes");
            
            while(true){
                Socket socketCliente = serverSocket.accept();
                System.out.println("Nueva conexion recibida desde "+ socketCliente.getInetAddress());
                HiloCliente manejador = new HiloCliente(socketCliente, this, turnoService, atencionService);
                Thread hilo = new Thread(manejador);
                hilo.start();
            }
            
        }catch(IOException e){
            System.err.println("Error crítico en el socket del servidor: " + e.getMessage());
            
        }
    }
    
    public void registrarPantallaPublica(HiloCliente pantalla){
        pantallasConectadas.add(pantalla);
        
    }
    public void removerPantallaPublica(HiloCliente pantalla){
        pantallasConectadas.remove(pantalla);
        
    }
    public void notificarNuevoLlamado(String codigoTurno, int numeroVentanilla){
        String mensajeBroadcast = "Pantalla_Actualizar; "+ codigoTurno + ";" + numeroVentanilla;
        
        synchronized(pantallasConectadas){
        for(HiloCliente pantalla : pantallasConectadas){
            pantalla.enviarMensajeAsincrono(mensajeBroadcast);
            
        }
    }
    }
    public static void main (String[] args){
        new ServidorCentral().iniciarServidor();
    }
}
