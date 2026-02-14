package es.medicarte.controller;

import es.medicarte.model.*;
import es.medicarte.util.LogoUtils;
import es.medicarte.util.SceneManager;
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

/**
 * Controlador del Dashboard del Médico.
 * Esta clase gestiona la pantalla principal que ve el médico tras iniciar sesión.
 * Desde aquí puede acceder a pacientes, citas, consultar información resumida
 * y cerrar sesión.
 */
public class MedicoDashboardController {

    // ===== COMPONENTES DE LA VISTA =====

    @FXML
    private Label lblUserInfo; // Muestra el nombre del médico logueado

    @FXML
    private TextFlow txtFechaHora; // Muestra fecha y hora en tiempo real

    @FXML
    private Label lblProximoPaciente; // Muestra el próximo paciente con cita pendiente

    @FXML
    private Label lblProximaCitaHora; // Muestra fecha y hora de la próxima cita

    @FXML
    private Label lblCitasHoy; // Número de citas pendientes para hoy

    @FXML
    private Label lblUltimoBackup; // Fecha del último backup realizado

    @FXML
    private ImageView imgLogo; // Logo de la clínica

    // ===== DAOs =====

    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final CitaDAO citaDAO = new CitaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    /**
     * Método que se ejecuta automáticamente al cargar la vista.
     * Aquí inicializamos todos los datos del dashboard.
     */
    @FXML
    private void initialize() {

        // Cargar número de citas pendientes para hoy
        int citasHoy = citaDAO.countCitasPendientesHoy();
        lblCitasHoy.setText(String.valueOf(citasHoy));

        //
        //  Iniciar reloj en tiempo real
        iniciarReloj();

        // Cargar logo de la clínica (si existe)
        Image logo = LogoUtils.getLogo(120);
        if (logo != null) {
            imgLogo.setImage(logo);
        }

        // Mostrar nombre del médico logueado
        Usuario u = UserSession.getUsuario();
        Medico medico = medicoDAO.findById(u.getIdMedico());
        if (u != null) {
            lblUserInfo.setText("Bienvenido, " + medico.getNombreApellidos());
        }

        // Mostrar fecha del último backup
        ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();
        String ultimoBackup = configuracionDAO.getValor("ULTIMA_FECHA_BKP");

        if (ultimoBackup != null) {
            lblUltimoBackup.setText("Último backup: \n" + ultimoBackup);
        } else {
            lblUltimoBackup.setText("Último backup: \n no disponible");
        }

        // Cargar próxima cita pendiente
        cargarProximaCita();
    }

    /**
     * Navega a la vista de pacientes.
     */
    @FXML
    private void abrirPacientes() {
        SceneManager.loadScene(
                "/es/medicarte/view/pacientes2.fxml",
                "MedicArte - Pacientes"
        );
    }

    /**
     * Navega a la vista de citas.
     */
    @FXML
    private void abrirCitas() {
        SceneManager.loadScene(
                "/es/medicarte/view/citas.fxml",
                "MedicArte - Citas"
        );
    }

    /**
     * Cierra la sesión actual del usuario.
     * Se muestra una confirmación antes de cerrar sesión.
     */
    @FXML
    private void cerrarSesion() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar cierre de sesión");
        alert.setContentText("Se cerrará la sesión actual y volverás al login.¿ Está seguro?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            // Limpiamos la sesión del usuario actual
            UserSession.clear();

            // Volvemos a la pantalla de login
            SceneManager.loadScene(
                    "/es/medicarte/view/login.fxml",
                    "MedicArte - Login"
            );
        }
    }

    /**
     * Inicia un reloj en tiempo real que se actualiza cada segundo.
     * Se utiliza un Timeline de JavaFX para refrescar la hora automáticamente.
     */
    private void iniciarReloj() {

        DateTimeFormatter fechaFormatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        DateTimeFormatter horaFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        Timeline reloj = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {

                    // Creamos texto para fecha
                    Text fecha = new Text(
                            LocalDateTime.now().format(fechaFormatter) + "\n"
                    );
                    fecha.setStyle("-fx-font-size: 12px;");

                    // Creamos texto para hora
                    Text hora = new Text(
                            LocalDateTime.now().format(horaFormatter)
                    );
                    hora.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                    // Actualizamos el TextFlow
                    txtFechaHora.getChildren().setAll(fecha, hora);
                }),
                new KeyFrame(Duration.seconds(1))
        );

        reloj.setCycleCount(Timeline.INDEFINITE);
        reloj.play();
    }

    /**
     * Carga la próxima cita pendiente cuya fecha sea posterior a la actual.
     * Si no hay citas pendientes, se muestra un mensaje informativo.
     */
    private void cargarProximaCita() {

        Cita cita = citaDAO.findProximaCitaPendiente();

        if (cita == null) {
            lblProximoPaciente.setText("No hay citas pendientes");
            lblProximaCitaHora.setText("—");
            return;
        }

        Paciente p = pacienteDAO.findById(cita.getIdPaciente());

        if (p != null) {
            lblProximoPaciente.setText(
                    p.getApellidos() + ", " + p.getNombre()
            );
        } else {
            lblProximoPaciente.setText("Paciente desconocido");
        }

        lblProximaCitaHora.setText(
                cita.getFechaHora()
                        .toLocalDate() + " " +
                        cita.getFechaHora()
                                .toLocalTime().withSecond(0)
        );
    }
}
