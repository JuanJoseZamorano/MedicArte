package es.medicarte.controller;

import es.medicarte.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Controlador principal básico.
 * Esta clase se encarga de gestionar acciones generales
 * que no pertenecen a un módulo concreto, como por ejemplo
 * el cierre de sesión desde una vista común.
 */
public class MainController {

    /**
     * Método asociado al botón "Cerrar sesión".
     * Muestra un cuadro de confirmación antes de volver
     * a la pantalla de login. Se utiliza un Alert de tipo
     * CONFIRMATION para evitar cierres accidentales.
     */
    @FXML
    private void handleLogout() {

        // Creamos un cuadro de diálogo de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar sesión");
        alert.setHeaderText(null);
        alert.setContentText("¿Desea cerrar la sesión actual?");

        // Esperamos la respuesta del usuario
        alert.showAndWait().ifPresent(response -> {

            // Si el usuario confirma, volvemos a la pantalla de login
            if (response == ButtonType.OK) {

                /*
                 * Utilizamos SceneManager para centralizar
                 * la navegación entre vistas, en lugar de
                 * cargar directamente el FXMLLoader aquí.
                 *
                 * Esto mantiene una arquitectura más limpia
                 * y desacoplada.
                 */
                SceneManager.loadScene(
                        "/es/medicarte/view/login.fxml",
                        "MedicArte - Login"
                );
            }
        });
    }
}
