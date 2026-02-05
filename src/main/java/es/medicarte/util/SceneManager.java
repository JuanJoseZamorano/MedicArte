package es.medicarte.util;

import es.medicarte.controller.ConsultaController;
import es.medicarte.controller.HistorialController;
import es.medicarte.model.Cita;
import es.medicarte.model.Consulta;
import es.medicarte.model.Paciente;
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
            stage.sizeToScene();      // ajusta tamaño a la nueva vista
            stage.centerOnScreen();   // centra en pantalla
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
            stage.sizeToScene();      // ajusta tamaño a la nueva vista
            stage.centerOnScreen();   // centra en pantalla
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ======================================
    // MÉTODO NUEVO (SOBRECARGA, PARA CONSULTA)
    // ======================================
    public static void loadScene(String fxml, String title, Cita cita) {

        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof ConsultaController) {
                ((ConsultaController) controller).setCita(cita);
            }

            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.sizeToScene();      // ajusta tamaño a la nueva vista
            stage.centerOnScreen();   // centra en pantalla
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadScene(String fxml, String title, Paciente paciente) {

        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof HistorialController) {
                ((HistorialController) controller).setPaciente(paciente);
            }

            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.sizeToScene();      // ajusta tamaño a la nueva vista
            stage.centerOnScreen();   // centra en pantalla
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadScene(String fxml, String title, Consulta consulta) {

        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ConsultaController) {
                ((ConsultaController) controller).setConsultaSoloLectura(consulta);
            }

            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.sizeToScene();      // ajusta tamaño a la nueva vista
            stage.centerOnScreen();   // centra en pantalla
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadScene(
            String fxml,
            String title,
            java.util.function.Consumer<Object> controllerConsumer
    ) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource(fxml)
            );
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controllerConsumer != null) {
                controllerConsumer.accept(controller);
            }

            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
