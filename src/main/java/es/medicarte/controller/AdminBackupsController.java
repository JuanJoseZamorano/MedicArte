package es.medicarte.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class AdminBackupsController {

    @FXML
    private ComboBox<String> cmbBackups;

    @FXML
    private void initialize() {
        // Datos ficticios de ejemplo
        cmbBackups.getItems().addAll(
                "Backup - 25/09/2025",
                "Backup - 18/09/2025",
                "Backup - 11/09/2025"
        );
    }
}
