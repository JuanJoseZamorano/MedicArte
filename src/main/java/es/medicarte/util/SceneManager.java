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

/**
 * Clase encargada de gestionar la navegación entre las distintas vistas
 * de la aplicación JavaFX.
 * Centraliza la carga de escenas y mantiene un historial de navegación
 * para permitir volver a la vista anterior sin perder el estado.
 */
public class SceneManager {

    // Escenario principal de la aplicación
    private static Stage stage;

    // Pila de navegación que almacena las escenas anteriores
    // Se utiliza para implementar la funcionalidad de "volver atrás"
    private static final Deque<Scene> history = new ArrayDeque<>();

    /**
     * Establece el Stage principal de la aplicación.
     * Se invoca normalmente desde la clase Main al iniciar la app.
     *
     * @param stage Stage principal
     */
    public static void setStage(Stage stage) {
        SceneManager.stage = stage;
    }

    // =========================
    // MÉTODO ORIGINAL (NO TOCAR)
    // =========================

    /**
     * Carga una nueva vista sin pasar datos adicionales al controlador.
     * Antes de cambiar de escena, se guarda la escena actual en el historial
     * para permitir volver atrás posteriormente.
     *
     * @param fxml  Ruta del archivo FXML
     * @param title Título de la ventana
     */
    public static void loadScene(String fxml, String title) {

        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
            Parent root = loader.load();

            // Guardamos la escena actual en la pila de navegación
            if (stage.getScene() != null) {
                history.push(stage.getScene());
            }

            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.sizeToScene();      // Ajusta el tamaño a la vista cargada
            stage.centerOnScreen();   // Centra la ventana en pantalla
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ======================================
    // MÉTODO SOBRECARGADO (PARA CITAS)
    // ======================================

    /**
     * Carga una vista de citas aplicando un filtro por paciente.
     * Se utiliza, por ejemplo, al acceder a las citas desde la ficha
     * de un paciente concreto.
     *
     * @param fxml       Ruta del archivo FXML
     * @param title      Título de la ventana
     * @param idPaciente Identificador del paciente a filtrar
     */
    public static void loadScene(String fxml, String title, Integer idPaciente) {

        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();

            // Si el controlador es de tipo CitasController,
            // se aplica el filtro por paciente
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
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ======================================
    // MÉTODO SOBRECARGADO (PARA CONSULTA)
    // ======================================

    /**
     * Carga la vista de consulta pasando una cita concreta.
     * Se utiliza al pasar una cita a consulta clínica.
     *
     * @param fxml  Ruta del archivo FXML
     * @param title Título de la ventana
     * @param cita  Cita seleccionada
     */
    public static void loadScene(String fxml, String title, Cita cita) {

        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();

            // Se pasa la cita al controlador de consulta
            if (controller instanceof ConsultaController) {
                ((ConsultaController) controller).setCita(cita);
            }

            if (stage.getScene() != null) {
                history.push(stage.getScene());
            }

            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga la vista del historial clínico de un paciente concreto.
     *
     * @param fxml     Ruta del archivo FXML
     * @param title    Título de la ventana
     * @param paciente Paciente seleccionado
     */
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
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga una consulta en modo solo lectura.
     * Se utiliza, por ejemplo, al visualizar consultas
     * desde el historial clínico.
     *
     * @param fxml     Ruta del archivo FXML
     * @param title    Título de la ventana
     * @param consulta Consulta a visualizar
     */
    public static void loadScene(String fxml, String title, Consulta consulta) {

        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof ConsultaController) {
                ((ConsultaController) controller)
                        .setConsultaSoloLectura(consulta);
            }

            if (stage.getScene() != null) {
                history.push(stage.getScene());
            }

            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método genérico que permite ejecutar lógica personalizada
     * sobre el controlador antes de mostrar la vista.
     * Se utiliza para pasar datos de forma flexible entre vistas.
     *
     * @param fxml               Ruta del archivo FXML
     * @param title              Título de la ventana
     * @param controllerConsumer Lógica a ejecutar sobre el controlador
     */
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

    /**
     * Vuelve a la escena anterior almacenada en el historial.
     * Permite una navegación coherente entre vistas,
     * manteniendo el estado previo de cada pantalla.
     */
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
