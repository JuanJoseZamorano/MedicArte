package es.medicarte.controller;

// Importamos utilidades propias del proyecto
import es.medicarte.util.LogoUtils;
import es.medicarte.util.PasswordUtils;
import es.medicarte.util.SceneManager;
import es.medicarte.util.UserSession;

// Importamos clases del modelo
import es.medicarte.model.Usuario;
import es.medicarte.model.UsuarioDAO;

// Importaciones JavaFX necesarias para la vista
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controlador de la vista de Login.
 *
 * Esta clase se encarga de:
 * - Validar las credenciales introducidas por el usuario.
 * - Comprobar el estado del usuario (activo o no).
 * - Verificar la contraseña utilizando BCrypt.
 * - Redirigir al dashboard correspondiente según el rol.
 */
public class LoginController {

    // Campo de texto donde el usuario introduce el nombre de usuario
    @FXML
    private TextField txtUser;

    // Campo de contraseña (oculta los caracteres)
    @FXML
    private PasswordField txtPassword;

    // ImageView donde se mostrará el logo de la clínica
    @FXML
    private ImageView imgLogo;

    /**
     * Método initialize() que se ejecuta automáticamente
     * al cargarse el FXML.
     *
     * Aquí cargamos el logo desde la configuración (si existe).
     */
    @FXML
    private void initialize() {

        // Obtenemos el logo mediante clase utilitaria
        Image logo = LogoUtils.getLogo(120);

        // Si existe un logo configurado, lo mostramos
        if (logo != null) {
            imgLogo.setImage(logo);
        }
    }

    /**
     * Método que se ejecuta al pulsar el botón de Login.
     *
     * Realiza:
     * 1. Obtención de datos introducidos.
     * 2. Búsqueda del usuario en base de datos.
     * 3. Validaciones (existencia, activo, contraseña).
     * 4. Redirección según rol.
     */
    @FXML
    private void handleLogin() {

        // Recuperamos los datos introducidos en los campos
        String user = txtUser.getText();
        String pass = txtPassword.getText();

        // Accedemos a la capa DAO para buscar el usuario
        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.findByUsername(user);

        // =========================
        // Validación 1: Usuario existe
        // =========================
        if (usuario == null) {
            showAlert(
                    "Error de autenticación",
                    "Usuario o contraseña incorrectos",
                    Alert.AlertType.ERROR
            );
            return;
        }

        // =========================
        // Validación 2: Usuario activo
        // =========================
        if (!usuario.isActivo()) {
            showAlert(
                    "Usuario inactivo",
                    "El usuario no está activo en el sistema",
                    Alert.AlertType.WARNING
            );
            return;
        }

        // =========================
        // Validación 3: Contraseña correcta
        // =========================
        // Se utiliza BCrypt mediante PasswordUtils para comparar
        // la contraseña introducida con el hash almacenado.
        if (!PasswordUtils.checkPassword(pass, usuario.getPasswordHash())) {
            showAlert(
                    "Error de autenticación",
                    "Usuario o contraseña incorrectos",
                    Alert.AlertType.ERROR
            );
            return;
        }

        // =========================
        // Login correcto
        // =========================

        // Guardamos el usuario en sesión para poder usarlo
        // en el resto de la aplicación.
        UserSession.setUsuario(usuario);

        // Redirigimos según el rol
        switch (usuario.getRol()) {

            case "ADMIN":
                SceneManager.loadScene(
                        "/es/medicarte/view/admin_dashboard.fxml",
                        "MedicArte - Administración"
                );
                break;

            case "MEDICO":
                SceneManager.loadScene(
                        "/es/medicarte/view/medico_dashboard.fxml",
                        "MedicArte - Área Médica"
                );
                break;

            default:
                // Si el rol no es reconocido, mostramos error
                showAlert(
                        "Error de rol",
                        "Rol de usuario no reconocido",
                        Alert.AlertType.ERROR
                );
        }
    }

    /**
     * Método auxiliar para mostrar alertas.
     *
     * Se centraliza aquí para no repetir código
     * cada vez que necesitamos mostrar un mensaje.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
