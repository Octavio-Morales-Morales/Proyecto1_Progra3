package controladores;

import javafx.application.Application;
import javafx.stage.Stage;

public class AppGenerador extends Application {
    @Override
    public void start(Stage ventana) {
        GeneradorTurnosController generador = Navegacion.mostrar(ventana, "GeneradorTurnos", "Generador de turnos");
        ventana.setOnHidden(e -> generador.detener());
    }
}
