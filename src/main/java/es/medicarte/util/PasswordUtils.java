package es.medicarte.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Coste del algoritmo sera 10.
    private static final int BCRYPT_COST = 10;

    /**
     * Genera un hash BCrypt a partir de una contraseña en texto plano
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_COST));
    }

    /**
     * Comprueba si una contraseña en texto plano coincide con su hash
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
