package controladores;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage ventana) {
        Navegacion.mostrar(ventana, "Login", "Sistema de turnos - Ingreso");
    }
}
