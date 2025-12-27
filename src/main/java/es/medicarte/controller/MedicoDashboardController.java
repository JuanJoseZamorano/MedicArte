package es.medicarte.controller;

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
}