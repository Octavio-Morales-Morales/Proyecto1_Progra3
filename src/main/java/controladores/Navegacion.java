package controladores;

import java.io.IOException;
import java.io.UncheckedIOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Navegacion {
    private Navegacion() {
    }

    public static <T> T mostrar(Stage ventana, String vista, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegacion.class.getResource(vista + ".fxml"));
            Parent raiz = loader.load();
            if (ventana.getScene() == null) {
                ventana.setScene(new Scene(raiz));
            } else {
                ventana.getScene().setRoot(raiz);
            }
            ventana.setTitle(titulo);
            ventana.sizeToScene();
            ventana.show();
            return loader.getController();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo abrir la pantalla " + vista, e);
        }
    }
}
