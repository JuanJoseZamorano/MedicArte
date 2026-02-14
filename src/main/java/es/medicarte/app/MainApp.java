package es.medicarte.app;

import es.medicarte.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación.
 * Extiende de Application, que es la clase base obligatoria
 * para cualquier aplicación JavaFX.
 * Desde aquí se inicializa la aplicación y se carga
 * la primera vista (login).
 */
public class MainApp extends Application {

    /**
     * Método start().
     * Es el punto de entrada real de JavaFX.
     * El framework lo ejecuta automáticamente al lanzar la aplicación.
     * Aquí configuramos el Stage principal y delegamos
     * la gestión de escenas al SceneManager.
     */
    @Override
    public void start(Stage stage) {

        // Guardamos el Stage principal en SceneManager
        // para poder reutilizarlo desde cualquier controlador.
        SceneManager.setStage(stage);

        // Cargamos la primera vista del sistema: el login.
        // Desde ahí se gestionará la autenticación
        // y el acceso al dashboard correspondiente.
        SceneManager.loadScene(
                "/es/medicarte/view/login.fxml",
                "MedicArte - Login"
        );
    }

    /**
     * Método main().
     * Es el punto de entrada estándar de cualquier aplicación Java.
     * Llama a launch(), que inicia el entorno JavaFX
     * y posteriormente ejecuta el método start().
     */
    public static void main(String[] args) {
        launch(args);
    }
}
