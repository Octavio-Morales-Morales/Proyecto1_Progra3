/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
/**
 * FXML Controller class
 *
 * @author tadan
 */
public class OperadorVentanillaController implements Initializable {


    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblUsuario;
    @FXML
    private Label lblEstadoVentanilla;
    @FXML
    private Label lblTurnoActual;
    @FXML
    private Label lblServicioActual;
    @FXML
    private Button btnSiguiente;
    @FXML
    private Button btnFinalizar;
    @FXML
    private Button btnVolverLlamar;
    @FXML
    private Button btnNoDisponible;
    @FXML
    private ListView<?> lstHistorial;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
