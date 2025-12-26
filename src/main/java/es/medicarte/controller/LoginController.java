package es.medicarte.controller;

import es.medicarte.util.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import es.medicarte.util.SceneManager;
import es.medicarte.model.Usuario;
import es.medicarte.model.UsuarioDAO;

public class LoginController {

    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private void handleLogin() {

        String user = txtUser.getText();
        String pass = txtPassword.getText();

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.findByUsername(user);
        // Validación de campos vacíos
        if (usuario == null) {
            showAlert(
                    "Error de autenticación",
                    "Usuario o contraseña incorrectos",
                    Alert.AlertType.ERROR
            );
            return;
        }
        if (!usuario.isActivo()) {
            showAlert(
                    "Usuario inactivo",
                    "El usuario no está activo en el sistema",
                    Alert.AlertType.WARNING
            );
            return;
        }
        // COMPARACIÓN SIMPLE (por ahora)
        if (!PasswordUtils.checkPassword(pass, usuario.getPasswordHash())) {
            showAlert(
                    "Error de autenticación",
                    "Usuario o contraseña incorrectos",
                    Alert.AlertType.ERROR
            );
            return;
        }
        // Login correcto
        SceneManager.loadScene(
                "/es/medicarte/view/main.fxml",
                "MedicArte - Principal"
        );
    }
    // falta hacer la base de datos con el script

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
