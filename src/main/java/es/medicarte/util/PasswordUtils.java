package es.medicarte.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase utilitaria encargada de la gestión segura de contraseñas.
 * Se utiliza para generar y comprobar contraseñas cifradas
 * mediante el algoritmo BCrypt.
 */
public class PasswordUtils {

    // Coste del algoritmo BCrypt.
    // Un valor de 10 ofrece un buen equilibrio entre seguridad y rendimiento.
    private static final int BCRYPT_COST = 10;

    /**
     * Genera un hash BCrypt a partir de una contraseña en texto plano.
     * Este método se utiliza al crear o modificar usuarios en el sistema.
     *
     * @param plainPassword Contraseña en texto plano
     * @return Hash cifrado de la contraseña
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_COST));
    }

    /**
     * Comprueba si una contraseña en texto plano coincide con su hash almacenado.
     * Se utiliza durante el proceso de autenticación de usuarios.
     *
     * @param plainPassword  Contraseña introducida por el usuario
     * @param hashedPassword Hash almacenado en la base de datos
     * @return true si la contraseña es correcta, false en caso contrario
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {

        // Validación básica para evitar errores si el hash no existe
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }

        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
