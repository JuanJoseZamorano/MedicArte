package es.medicarte.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private static Stage stage;

    public static void setStage(Stage stage) {
        SceneManager.stage = stage;
    }

    // =========================
    // MÉTODO ORIGINAL (NO TOCAR)
    // =========================
    public static void loadScene(String fxml, String title) {

        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
            Parent root = loader.load();

            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ======================================
    // MÉTODO NUEVO (SOBRECARGA, PARA CITAS)
    // ======================================
    public static void loadScene(String fxml, String title, Integer idPaciente) {

        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof es.medicarte.controller.CitasController
                    && idPaciente != null) {
                ((es.medicarte.controller.CitasController) controller)
                        .setIdPacienteFiltro(idPaciente);
            }

            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
