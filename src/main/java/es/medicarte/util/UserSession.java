package es.medicarte.util;

// clase para controlar el usuario que esta logueado en el momento actual

import es.medicarte.model.Usuario;

public class UserSession {

    private static Usuario usuarioActual;

    private UserSession() {
        // No instanciable
    }

    public static void setUsuario(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuario() {
        return usuarioActual;
    }

    public static void clear() {
        usuarioActual = null;
    }
}
