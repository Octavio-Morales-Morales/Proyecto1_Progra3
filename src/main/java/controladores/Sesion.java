/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

import Dominio.Usuarios;

public class Sesion {
    private static Usuarios usuarioActual;

    public static void setUsuarioActual(Usuarios u) {
        usuarioActual = u;
    }

    public static Usuarios getUsuarioActual() {
        return usuarioActual;
    }

    public static void cerrar() {
        usuarioActual = null;
    }
}