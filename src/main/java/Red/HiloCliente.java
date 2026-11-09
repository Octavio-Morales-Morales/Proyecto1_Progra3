/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Red;

import Dominio.Turnos;
import Servicios.AtencionService;
import Servicios.TurnoService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author moral
 */
public class HiloCliente implements Runnable{
private Socket socket;
private ServidorCentral servidor;
private TurnoService turnoService;
private AtencionService atencionService;

private PrintWriter salida;
private BufferedReader entrada;
private boolean esPantallaPublica = false;

public HiloCliente(Socket socket, ServidorCentral servidor, TurnoService turnoService, AtencionService atencionService){
    this.socket = socket;
    this.servidor = servidor;
    this.turnoService = turnoService;
    this.atencionService = atencionService;
}

    @Override
    public void run() {
        try{
            this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.salida = new PrintWriter(socket.getOutputStream(), true);
            
            String lineaRecibida;
            while((lineaRecibida = entrada.readLine()) != null){
                if(lineaRecibida.trim().isEmpty())continue;
                System.out.println("Procesando protocolo: " + lineaRecibida);
                procesarProtocolo(lineaRecibida);
            }
        }catch(IOException e){
            
        }finally {
            cerrarConexion();
        }

    }
    private void procesarProtocolo(String linea){
        String[] partes = linea.split(";");
        String comando = partes[0].toUpperCase();
        
        try{
            switch(comando){
                
                case "REGISTRAR_PANTALLA":
                   this.esPantallaPublica = true;
                   servidor.registrarPantallaPublica(this);
                   salida.println("OK;Registrada");
                   break;
                
                case "GENERAR_TURNO":
                    String letra = partes[1];
                    int servicioId = Integer.parseInt(partes[2]);
                    int prioridad = Integer.parseInt(partes[3]);
                    
                    Turnos nuevoTurno = turnoService.generarNuevoTurno(letra, servicioId, prioridad);
                    salida.println("OK;" + nuevoTurno.getCodigoTurno() + ";" + nuevoTurno.getTurnoId());
                    break;
                    
                case "SOLICITAR_SIGUIENTE":
                    int ventanillaId = Integer.parseInt(partes[1]);
    int funcionarioId = Integer.parseInt(partes[2]);

    // Los servicios permitidos vienen como una lista separada por comas en el campo 3
    // Ejemplo de comando: SOLICITAR_SIGUIENTE;1;2;1,2,3,4
    String serviciosStr = partes.length > 3 ? partes[3] : "1,2,3,4";
    List<Integer> serviciosPermitidos = Arrays.stream(serviciosStr.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .collect(Collectors.toList());

    Turnos proximoTurno = turnoService.solicitarSiguiente(serviciosPermitidos);
    int atencionId = atencionService.llamarCliente(
            proximoTurno.getTurnoId(), ventanillaId, funcionarioId);

    // Notificar a todas las pantallas públicas (esto es obligatorio)
    servidor.notificarNuevoLlamado(proximoTurno.getCodigoTurno(), ventanillaId);

    salida.println("OK;" + proximoTurno.getCodigoTurno() + ";" 
                   + proximoTurno.getTurnoId() + ";" + atencionId);
                   break;
                   
                case "COMENZAR_TRAMITE":
                    int atencionIdInicio = Integer.parseInt(partes[1]);
                    int turnoIdInicio = Integer.parseInt(partes[2]);
                    
                    atencionService.comenzarTramite(atencionIdInicio, turnoIdInicio);
                    salida.println("OK;Tramite Iniciado");
                    break;
                    
                case "TERMINAR_TRAMITE":
                    int atencionIdFin = Integer.parseInt(partes[1]);
                    int turnoIdFin = Integer.parseInt(partes[2]);
                    String notas = partes.length > 3 ? partes[3] : "Sin observaciones";
                    
                    atencionService.terminarTramite(atencionIdFin, turnoIdFin, notas);
                    salida.println("OK;Tramite Finalizado");
                    break;
                    
                default:
                    salida.println("Error;Comando no soportado por el protocolo.");
            }
        }catch (IllegalArgumentException | IllegalStateException e){
            salida.println("Error " + e.getMessage());
        } catch(Exception e) {
            salida.println("Error;Error interno del servidor: " + e.getMessage());
        }
    }
    
    public void enviarMensajeAsincrono(String mensaje){
        if(salida != null){
            salida.println(mensaje);
        }
    }

    private void cerrarConexion() {
        if(esPantallaPublica){
            servidor.removerPantallaPublica(this);
        }
        try {
            if(entrada != null) entrada.close();
            if(salida != null) salida.close();
            if(socket != null && !socket.isClosed()) socket.close();
            System.out.println("Recursos liberados de forma segura");
            
        } catch (IOException e){
            System.err.println("Error al cerrar sockets: " + e.getMessage());            
        }
    }
    
}
