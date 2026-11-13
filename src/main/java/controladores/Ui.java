package controladores;

import Excepciones.ConexionException;
import Excepciones.PersistenciaException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public final class Ui {
    private Ui() {
    }

    public static <T> void enSegundoPlano(Callable<T> trabajo, Consumer<T> alTerminar, Consumer<String> alFallar) {
        Thread hilo = new Thread(() -> {
            try {
                T resultado = trabajo.call();
                Platform.runLater(() -> alTerminar.accept(resultado));
            } catch (Exception e) {
                String mensaje = mensajeParaUsuario(e);
                Platform.runLater(() -> alFallar.accept(mensaje));
            }
        }, "tarea-ui");
        hilo.setDaemon(true);
        hilo.start();
    }

    public static String mensajeParaUsuario(Throwable e) {
        if (e instanceof ConexionException) {
            return "No hay conexión con el servidor central. Verifique que este encendido e intente de nuevo.";
        }
        if (e instanceof PersistenciaException) {
            return "No se pudo comunicar con la base de datos. Verifique la conexion e intente de nuevo.";
        }
        if (e instanceof IllegalArgumentException || e instanceof IllegalStateException) {
            return e.getMessage();
        }
        return "Ocurrio un error inesperado: " + e.getMessage();
    }

    public static void error(String mensaje) {
        mostrar(Alert.AlertType.ERROR, "Error", mensaje);
    }

    public static void aviso(String mensaje) {
        mostrar(Alert.AlertType.WARNING, "Aviso", mensaje);
    }

    public static void info(String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, "Informacion", mensaje);
    }

    private static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
