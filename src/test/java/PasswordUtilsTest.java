import es.medicarte.util.PasswordUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilsTest {

    @Test
    void hashYCheckPasswordFunciona() {

        String password = "medico123";

        String hash = PasswordUtils.hashPassword(password);

        assertNotNull(hash);
        assertTrue(PasswordUtils.checkPassword(password, hash));
        assertFalse(PasswordUtils.checkPassword("otroPassword", hash));
    }

}