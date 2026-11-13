package controladores;

import javafx.application.Application;
import javafx.stage.Stage;

public class AppPantalla extends Application {
    @Override
    public void start(Stage ventana) {
        PantallaPublicaController pantalla = Navegacion.mostrar(ventana, "PantallaPublica", "Pantalla publica");
        ventana.setOnHidden(e -> pantalla.detener());
    }
}
