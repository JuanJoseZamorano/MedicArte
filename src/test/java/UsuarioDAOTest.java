import es.medicarte.model.UsuarioDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOTest {

    @Test
    void existeUsuarioAdmin() {

        UsuarioDAO dao = new UsuarioDAO();

        boolean existe = dao.existsByUsername("admin");

        assertTrue(existe);
    }
}