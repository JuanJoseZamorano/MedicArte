package es.medicarte.controller;

import es.medicarte.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import es.medicarte.util.UserSession;
import es.medicarte.model.Usuario;

import java.util.Optional;

public class MedicoDashboardController {

    @FXML
    private Label lblUserInfo;

    @FXML
    private void initialize() {
        Usuario u = UserSession.getUsuario();
        if (u != null) {
            lblUserInfo.setText("Bienvenido, " + u.getUsername() + " (MEDICO)");
        }

    }

    @FXML
    private void abrirPacientes() {
        SceneManager.loadScene(
                "/es/medicarte/view/pacientes2.fxml",
                "MedicArte - Pacientes"
        );
    }

    @FXML
    private void abrirCitas() {
        SceneManager.loadScene(
                "/es/medicarte/view/citas.fxml",
                "MedicArte - Citas"
        );
    }

    @FXML
    private void cerrarSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar cierre de sesión");
        alert.setContentText("Se cerrará la sesión actual y volverás al login.¿ Está seguro?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Limpiamos la sesión del usuario
            UserSession.clear();

            // Volvemos a la pantalla de login
            SceneManager.loadScene(
                    "/es/medicarte/view/login.fxml",
                    "MedicArte - Login"
            );
        }
    }

}