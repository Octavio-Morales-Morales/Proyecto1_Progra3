/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import Dominio.Usuarios;
import Repositorios.InterfaceUsuarios;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author COTO
 */
public class UsuarioService {
    
    private final InterfaceUsuarios usuarioDAO;

    public UsuarioService(InterfaceUsuarios usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
    public Usuarios login(String nombreUsuario, String contrasenia) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() || contrasenia == null) {
            throw new IllegalArgumentException("El usuario y la contraseña son obligatorios.");
        }
        String claveEncriptada = convertirSHA256(contrasenia);
        return usuarioDAO.autenticar(nombreUsuario, claveEncriptada);
    }
    public void registrarUsuario(Usuarios usuario) {
        if (usuario.getUsuario() == null || usuario.getUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario único es obligatorio.");
        }
        String claveProtegida = convertirSHA256(usuario.getContraseña());
        usuario.setContraseña(claveProtegida);
        usuarioDAO.crear(usuario);
    }
    private String convertirSHA256(String textoOriginal) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(textoOriginal.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return textoOriginal;
        }
    }    
}
