package es.medicarte.test;

import es.medicarte.model.Paciente;
import es.medicarte.model.PacienteDAO;

import java.time.LocalDate;

public class TestPacienteDAO {

    public static void main(String[] args) {

        PacienteDAO dao = new PacienteDAO();

        Paciente p = new Paciente(
                "12345678A",
                "Juan",
                "Pérez López",
                LocalDate.of(1985, 5, 10),
                "M",
                "600123456",
                "juan@email.com",
                "Calle Mayor 1",
                "Sevilla",
                "41001",
                "SegurVida",
                "POL123",
                "NUHSA001",
                "NUSS001",
                "NHC001",
                "O+",
                "Ninguna",
                "Ninguno",
                "Padre diabético",
                "Ninguno"
        );

        boolean insertado = dao.insert(p);
        System.out.println("Insertado: " + insertado);
    }
}
