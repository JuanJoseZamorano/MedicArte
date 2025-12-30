package es.medicarte.test;

import es.medicarte.model.Cita;
import es.medicarte.model.CitaDAO;

import java.time.LocalDateTime;

public class TestCitaDAO {

    public static void main(String[] args) {

        CitaDAO dao = new CitaDAO();

        // suponiendo que es paciente 1 y medico 1
        int idPaciente = 2;
        int idMedico = 1;

        // Creamos una cita de prueba
        Cita c = new Cita(
                idPaciente,
                idMedico,
                LocalDateTime.now().plusDays(1),
                "Expedicion de medicamentos",
                10
        );

        boolean insertada = dao.insert(c);
        System.out.println("Cita insertada: " + insertada);

        System.out.println("---- LISTADO DE CITAS DEL PACIENTE " + idPaciente + " ----");

        dao.findByPaciente(idPaciente).forEach(ci -> {
            System.out.println(
                    ci.getIdCita() + " | " +
                            ci.getFechaHora() + " | " +
                            ci.getEstado() + " | " +
                            ci.getObservaciones()
            );
        });
    }
}