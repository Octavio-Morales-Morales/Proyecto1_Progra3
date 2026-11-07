package controladores;

import Red.ClienteSocket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static ClienteSocket clienteSocket;
    
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Inicializando el entorno visual y la conexión de red...");
        inicializarConexionRed();
        scene = new Scene(loadFXML("Login"), 640, 480);
        stage.setTitle("Sistema Distribuido de Turnos - Ingreso");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    private void inicializarConexionRed(){
        try{
            clienteSocket = new ClienteSocket();
            clienteSocket.conectar();
            System.out.println("Conectado al Servidor Central exitosamente");
        }catch(IOException e){
            System.err.println("El Servidor Central está apagado o fuera de servicio.");
            System.err.println("Aviso: Las pantallas darán error de red si intentas presionar botones.");
        }
    }
    @Override
    public void stop() throws Exception{
        if(clienteSocket != null){
            clienteSocket.desconectar();
        }
        super.stop();
        System.out.println("Aplicación finalizada.");
    }
    public static ClienteSocket getClienteSocket(){
        return clienteSocket;
    }
    public static void main(String[] args) {
        launch();
    }

}