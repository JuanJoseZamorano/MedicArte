import es.medicarte.model.Cita;
import es.medicarte.model.CitaDAO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CitaDAOTest {
    @Test
    void insertarCita() {

        CitaDAO dao = new CitaDAO();

        Cita cita = new Cita();
        cita.setIdPaciente(1);
        cita.setIdMedico(1);
        cita.setFechaHora(LocalDateTime.now().plusDays(1));
        cita.setEstado("PENDIENTE");
        cita.setOrigen("CLINICA");

        boolean ok = dao.insert(cita);

        assertTrue(ok);
    }

    @Test
    void cancelarCita() {

        CitaDAO dao = new CitaDAO();

        boolean ok = dao.cancelarCita(1);

        assertTrue(ok);
    }

    @Test
    void contarCitasPendientesHoy() {

        CitaDAO dao = new CitaDAO();

        int total = dao.countCitasPendientesHoy();

        assertTrue(total >= 0);
    }
}
