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
import javafx.scene.layout.VBox;
/**
 * FXML Controller class
 *
 * @author tadan
 */
public class GeneradorTurnosController implements Initializable {


    @FXML
    private Button btnServicioA;
    @FXML
    private Button btnServicioB;
    @FXML
    private Button btnServicioC;
    @FXML
    private Button btnServicioD;
    @FXML
    private VBox panelResultado;
    @FXML
    private Label lblNumeroTurno;
    @FXML
    private Label lblServicio;
    @FXML
    private Label lblFechaHora;
    @FXML
    private Button btnNuevoTurno;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
