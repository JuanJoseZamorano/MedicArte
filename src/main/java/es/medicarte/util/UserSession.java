package es.medicarte.util;

// Clase utilitaria para controlar el usuario que está logueado
// actualmente en la aplicación durante la ejecución

import es.medicarte.model.Usuario;

/**
 * Esta clase se utiliza para mantener en memoria
 * la información del usuario autenticado.
 *
 * Permite acceder al usuario logueado desde cualquier
 * controlador sin necesidad de pasarlo por parámetros.
 */
public class UserSession {

    // Usuario que se encuentra actualmente autenticado
    private static Usuario usuarioActual;

    /**
     * Constructor privado para evitar instanciación.
     * Esta clase se usa únicamente de forma estática.
     */
    private UserSession() {
        // No instanciable
    }

    /**
     * Establece el usuario actual tras un login correcto.
     *
     * @param usuario Usuario autenticado
     */
    public static void setUsuario(Usuario usuario) {
        usuarioActual = usuario;
    }

    /**
     * Devuelve el usuario actualmente logueado.
     *
     * @return Usuario actual o null si no hay sesión
     */
    public static Usuario getUsuario() {
        return usuarioActual;
    }

    /**
     * Limpia la sesión actual.
     * Se utiliza, por ejemplo, al cerrar sesión.
     */
    public static void clear() {
        usuarioActual = null;
    }
}
