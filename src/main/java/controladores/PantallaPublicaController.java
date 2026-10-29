/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
/**
 * FXML Controller class
 *
 * @author tadan
 */
public class PantallaPublicaController implements Initializable {


    @FXML
    private Label lblHora;
    @FXML
    private ListView<?> lstRecientes;
    @FXML
    private Label lblUltimoTurno;
    @FXML
    private Label lblVentanilla;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
