package es.medicarte.util;

public class PasswordHashGenerator {
// esta es una clase creada para pasar los pass planos a hash.
    // solo la hemos usado para ejecutarla una vez

    static void main(String[] args) {

        System.out.println("admin -> " + PasswordUtils.hashPassword("admin"));
        System.out.println("medico123 -> " + PasswordUtils.hashPassword("medico123"));
        System.out.println("recep123 -> " + PasswordUtils.hashPassword("recep123"));
        System.out.println("aux123 -> " + PasswordUtils.hashPassword("aux123"));
        System.out.println("inactivo123 -> " + PasswordUtils.hashPassword("inactivo123"));
    }
}
