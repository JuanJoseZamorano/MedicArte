package es.medicarte.controller;

import es.medicarte.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import es.medicarte.util.UserSession;
import es.medicarte.model.Usuario;

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
}