package es.medicarte.controller;

import es.medicarte.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MainController {

    @FXML
    private void handleLogout() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar sesión");
        alert.setHeaderText(null);
        alert.setContentText("¿Desea cerrar la sesión actual?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                SceneManager.loadScene(
                        "/es/medicarte/view/login.fxml",
                        "MedicArte - Login"
                );
            }
        });
    }
}
