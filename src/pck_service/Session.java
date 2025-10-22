package pck_service;

import pck_model.Usuario;

public class Session {

    private static Usuario actual = null;

    public static void login(Usuario usuario) {
        actual = usuario;
    }

    //Cierra sesion
    public static void logout() {
        actual = null;
    }

    //Saber Hay alguien logueado
    public static boolean isLogged() {
        return actual != null;
    }

    //Obtener el usuario actual
    public static Usuario get() {
        return actual;
    }

    //Obtener informacion desde UI sin hacer consulta a DB
    public static String getRol() {
        return (actual != null) ? actual.getRolNombre() : null;
    }

    public static int getUsuarioId() {
        return (actual != null) ? actual.getIdUsuario() : -1;
    }

    public static String getNombre() {
        return (actual != null) ? actual.getNombre() : null;
    }

    public static String getCorreo() {
        return (actual != null) ? actual.getCorreo() : null;
    }
}
