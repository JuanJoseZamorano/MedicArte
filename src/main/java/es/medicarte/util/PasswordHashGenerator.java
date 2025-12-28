package es.medicarte.util;

public class PasswordHashGenerator {
// esta es una clase creada para pasar los pass planos a hash.
    // solo la hemos usado para ejecutarla una vez

    static void main(String[] args) {

        System.out.println("admin -> " + PasswordUtils.hashPassword("admin"));
        System.out.println("medico1 -> " + PasswordUtils.hashPassword("medico1"));
        System.out.println("medico2 -> " + PasswordUtils.hashPassword("medico2"));

    }
}
