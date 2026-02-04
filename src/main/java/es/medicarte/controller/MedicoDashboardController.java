package es.medicarte.controller;

import es.medicarte.model.*;
import es.medicarte.util.LogoUtils;
import es.medicarte.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import es.medicarte.util.UserSession;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Optional;

public class MedicoDashboardController {

    @FXML
    private Label lblUserInfo;
    @FXML
    private TextFlow txtFechaHora;
    @FXML
    private Label lblCitasHoy;
    @FXML
    private Label lblUltimoBackup;
    @FXML
    private ImageView imgLogo;
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final CitaDAO citaDAO = new CitaDAO();
    @FXML
    private void initialize() {
        int citasHoy = citaDAO.countCitasPendientesHoy();
        lblCitasHoy.setText(String.valueOf(citasHoy));
        iniciarReloj();
        Image logo = LogoUtils.getLogo(120);
        if (logo != null) {
            imgLogo.setImage(logo);
        }
        Usuario u = UserSession.getUsuario();
        Medico medico = medicoDAO.findById(u.getIdMedico());
        if (u != null) {
            lblUserInfo.setText( "Bienvenido, " + medico.getNombreApellidos());
        }
        ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();
        String ultimoBackup = configuracionDAO.getValor("ULTIMA_FECHA_BKP");

        if (ultimoBackup != null) {
            lblUltimoBackup.setText("Último backup: \n" + ultimoBackup);
        } else {
            lblUltimoBackup.setText("Último backup: \n no disponible");
        }

    }

    @FXML
    private void abrirPacientes() {
        SceneManager.loadScene(
                "/es/medicarte/view/pacientes2.fxml",
                "MedicArte - Pacientes"
        );
    }

    @FXML
    private void abrirCitas() {
        SceneManager.loadScene(
                "/es/medicarte/view/citas.fxml",
                "MedicArte - Citas"
        );
    }

    @FXML
    private void cerrarSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar cierre de sesión");
        alert.setContentText("Se cerrará la sesión actual y volverás al login.¿ Está seguro?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Limpiamos la sesión del usuario
            UserSession.clear();

            // Volvemos a la pantalla de login
            SceneManager.loadScene(
                    "/es/medicarte/view/login.fxml",
                    "MedicArte - Login"
            );
        }
    }

    private void iniciarReloj() {

        DateTimeFormatter fechaFormatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        DateTimeFormatter horaFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        Timeline reloj = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {

                    Text fecha = new Text(
                            LocalDateTime.now().format(fechaFormatter) + "\n"
                    );
                    fecha.setStyle("-fx-font-size: 12px;");

                    Text hora = new Text(
                            LocalDateTime.now().format(horaFormatter)
                    );
                    hora.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                    txtFechaHora.getChildren().setAll(fecha, hora);
                }),
                new KeyFrame(Duration.seconds(1))
        );

        reloj.setCycleCount(Timeline.INDEFINITE);
        reloj.play();
    }



}