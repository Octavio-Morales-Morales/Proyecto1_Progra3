/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Red;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author moral
 */
public class ClienteSocket {
    private static final String HOST = "localhost";
    private static final int PUERTO = 12345;
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    
    public void conectar() throws IOException{
        this.socket = new Socket(HOST, PUERTO);
        this.salida = new PrintWriter(socket.getOutputStream(), true);
        this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
        
        System.out.println("Conectado exitosamente al Servidor Central en " + HOST + ":" + PUERTO);
    }
    public String enviarMensaje(String comando) throws IOException{
        if(salida == null || entrada == null){
            throw new IOException("Error: No hay una conexión activa con el servidor.");
            
        }
        salida.println(comando);
        String respuesta = entrada.readLine();
        return respuesta;
    }
    
    public void desconectar(){
        try{
            if(entrada != null) entrada.close();
            if(salida != null) salida.close();
            if(socket != null && socket.isClosed()) socket.close();
            System.out.println("Recursos de red liberados de forma limpia.");
            
        } catch(IOException e){
            System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
        }
    }
    
    public BufferedReader getEntrada(){
        return this.entrada;
    }
}
