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
import java.util.ArrayDeque;
import java.util.Deque;

public class SceneManager {

    private static Stage stage;

    // pila de navegacion
    private static final Deque<Scene> history = new ArrayDeque<>();

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

            if (stage.getScene() != null) {
                history.push(stage.getScene());
            }
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
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
            if (stage.getScene() != null) {
                history.push(stage.getScene());
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
            if (stage.getScene() != null) {
                history.push(stage.getScene());
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
            if (stage.getScene() != null) {
                history.push(stage.getScene());
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
            if (stage.getScene() != null) {
                history.push(stage.getScene());
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
            if (stage.getScene() != null) {
                history.push(stage.getScene());
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
    public static void goBack() {

        if (!history.isEmpty()) {
            Scene previous = history.pop();
            stage.setScene(previous);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        }
    }


}
