package es.medicarte.controller;


import es.medicarte.util.SceneManager;
import es.medicarte.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.layout.StackPane;

public class AdminDashboardController {

    @FXML
    private StackPane contentPane;

    @FXML
    private void initialize() {
        mostrarConfiguracion();
    }

    @FXML
    private void mostrarConfiguracion() {
        cargarVista("/es/medicarte/view/admin_configuracion.fxml");
    }

    @FXML
    private void mostrarBackups() {
        cargarVista("/es/medicarte/view/admin_backups.fxml");
    }

    private void cargarVista(String fxml) {
        try {
            contentPane.getChildren().clear();
            contentPane.getChildren().add(
                    FXMLLoader.load(getClass().getResource(fxml))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void cerrarSesion() {
        UserSession.clear();
        SceneManager.loadScene(
                "/es/medicarte/view/login.fxml",
                "MedicArte - Login"
        );
    }
}


