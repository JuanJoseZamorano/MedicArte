import es.medicarte.model.ConfiguracionDAO;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

class AdministracionDAOTest {
    @Test
    void setYGetConfiguracion() {

        ConfiguracionDAO dao = new ConfiguracionDAO();

        dao.setValor("TEST_CLAVE", "valor123");

        String valor = dao.getValor("TEST_CLAVE");

        assertEquals("valor123", valor);
    }

}
