package es.medicarte.app;

import es.medicarte.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import es.medicarte.util.DatabaseConnection;
import java.sql.Connection;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        SceneManager.setStage(stage);
        SceneManager.loadScene(
                "/es/medicarte/view/login.fxml",
                "MedicArte - Login"
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}

