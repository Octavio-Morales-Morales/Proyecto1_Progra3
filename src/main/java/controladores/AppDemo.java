package controladores;

import Red.Protocolo;
import Red.ServidorCentral;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppDemo extends Application {
    @Override
    public void init() throws Exception {
        if (!ServidorCentral.estaEscuchando("localhost", Protocolo.PUERTO)) {
            Thread hiloServidor = new Thread(() -> new ServidorCentral().iniciarServidor(), "servidor-central");
            hiloServidor.setDaemon(true);
            hiloServidor.start();
            for (int i = 0; i < 50 && !ServidorCentral.estaEscuchando("localhost", Protocolo.PUERTO); i++) {
                Thread.sleep(100);
            }
        }
    }

    @Override
    public void start(Stage ventanaPrincipal) {
        GeneradorTurnosController generador = Navegacion.mostrar(ventanaPrincipal, "GeneradorTurnos", "Generador de turnos");
        ventanaPrincipal.setOnHidden(e -> generador.detener());
        ventanaPrincipal.setX(20);
        ventanaPrincipal.setY(20);

        Stage pantalla = new Stage();
        PantallaPublicaController controladorPantalla = Navegacion.mostrar(pantalla, "PantallaPublica", "Pantalla publica");
        pantalla.setOnHidden(e -> controladorPantalla.detener());
        pantalla.setX(480);
        pantalla.setY(20);

        Stage operador1 = new Stage();
        Navegacion.mostrar(operador1, "Login", "Operador 1 - Ingreso");
        operador1.setX(40);
        operador1.setY(420);

        Stage operador2 = new Stage();
        Navegacion.mostrar(operador2, "Login", "Operador 2 - Ingreso");
        operador2.setX(720);
        operador2.setY(420);
    }
}
