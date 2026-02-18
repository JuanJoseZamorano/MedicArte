import es.medicarte.model.*;
import es.medicarte.util.PasswordUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class IntegracionFlujoTest {

    @Test
    void flujoLoginPacienteYCitas() {

        // 1️⃣ LOGIN
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.findByUsername("medico1");

        assertNotNull(usuario, "El usuario debería existir");
        assertTrue(usuario.isActivo(), "El usuario debería estar activo");

        boolean passwordOk = PasswordUtils.checkPassword(
                "medico1",
                usuario.getPasswordHash()
        );

        assertTrue(passwordOk, "La contraseña debería ser válida");

        // 2️⃣ OBTENER PACIENTES
        PacienteDAO pacienteDAO = new PacienteDAO();
        List<Paciente> pacientes = pacienteDAO.findAll();

        assertNotNull(pacientes);
        assertFalse(pacientes.isEmpty(), "Debe haber al menos un paciente");

        Paciente paciente = pacientes.get(0);
        assertNotNull(paciente.getIdPaciente());

        // 3️⃣ OBTENER CITAS DEL PACIENTE
        CitaDAO citaDAO = new CitaDAO();
        List<Cita> citas = citaDAO.findByPaciente(paciente.getIdPaciente());

        assertNotNull(citas, "La lista de citas no debe ser null");

        // No obligamos a que tenga citas
        // solo comprobamos que el sistema responde correctamente
    }
}
