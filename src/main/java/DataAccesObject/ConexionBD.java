/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAccesObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author COTO
 */
public class ConexionBD {
    private static final String URL = "jdbc:mysql://pma.us.cloudlogin.co:3306/gamabasis_p3g1?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "TU_USUARIO_DE_BASE_DE_DATOS"; 
    private static final String CONTRASENIA = "TU_CONTRASENIA_DE_BASE_DE_DATOS";
    
    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver no encontrado: " + e.getMessage());
        }
        return DriverManager.getConnection(URL, USUARIO, CONTRASENIA);
    }
}
