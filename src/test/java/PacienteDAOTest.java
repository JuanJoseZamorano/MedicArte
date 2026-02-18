import es.medicarte.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PacienteDAOTest {
    @Test
    void insertarPaciente() {

        PacienteDAO dao = new PacienteDAO();

        Paciente p = new Paciente();
        p.setDni("99999999Z");
        p.setNombre("Test");
        p.setApellidos("JUnit");

        boolean insertado = dao.insert(p);

        assertTrue(insertado);
    }

    @Test
    void buscarPacientePorDni() {

        PacienteDAO dao = new PacienteDAO();

        Paciente p = dao.findByDni("99999999Z");

        assertNotNull(p);
        assertEquals("JUnit", p.getApellidos());
    }

    @Test
    void insertarEpisodio() {

        EpisodioDAO dao = new EpisodioDAO();

        Episodio e = new Episodio();
        e.setIdHistoria(1);
        e.setIdEspecialidad(1);
        e.setMotivo("Prueba JUnit");

        int id = dao.insertar(e);

        assertTrue(id > 0);
    }
}
