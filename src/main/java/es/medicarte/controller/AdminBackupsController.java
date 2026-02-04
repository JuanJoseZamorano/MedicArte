package es.medicarte.controller;

import es.medicarte.service.BackupService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.io.File;

public class AdminBackupsController {

    @FXML
    private ComboBox<File> cmbBackups;

    @FXML
    private void initialize() {
        cmbBackups.setConverter(new StringConverter<>() {
            @Override
            public String toString(File file) {
                return file != null ? file.getName() : "";
            }

            @Override
            public File fromString(String string) {
                return null;
            }
        });
        cargarBackups();
    }

    @FXML
    private void crearBackup() {
        try {
            File backup = BackupService.crearBackup();
            mostrarInfo("Backup creado:\n" + backup.getName());
            cargarBackups();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }
    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void mostrarInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
    private void cargarBackups() {
        cmbBackups.getItems().clear();
        for (File f : BackupService.listarBackups()) {
            cmbBackups.getItems().add(f);
        }
    }


    @FXML
    private void restaurarBackup() {

        File seleccionado = cmbBackups.getValue();

        if (seleccionado == null) {
            mostrarError("Debe seleccionar una copia de seguridad.");
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Esta acción restaurará la base de datos completa.\n"
                        + "¿Desea continuar?",
                ButtonType.YES,
                ButtonType.NO
        );

        confirm.showAndWait();

        if (confirm.getResult() != ButtonType.YES) {
            return;
        }

        try {
            BackupService.restaurarBackup(seleccionado);

            mostrarInfo(
                    "La base de datos se ha restaurado correctamente.\n"
                            + "La aplicación debe reiniciarse."
            );

            // Opción 1: cerrar aplicación
            System.exit(0);

            // Opción 2 (alternativa): volver al login
            // SceneManager.loadScene("/es/medicarte/view/login.fxml", "MedicArte - Login");

        } catch (Exception e) {
            mostrarInfo(e.getMessage());
        }
    }

}
