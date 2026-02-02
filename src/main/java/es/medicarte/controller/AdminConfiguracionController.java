package es.medicarte.controller;

import es.medicarte.model.*;
import es.medicarte.util.PasswordUtils;
import es.medicarte.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;


public class AdminConfiguracionController {

    // =========================
    // ALTA DE USUARIO
    // =========================
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cmbRol;

    // DATOS MÉDICO (condicionales)
    @FXML private TextField txtNombreMedico;
    @FXML private TextField txtNumColegiado;
    @FXML private ComboBox<Especialidad> cmbEspecialidad;

    // =========================
    // ELIMINAR USUARIO
    // =========================
    @FXML private ComboBox<Usuario> cmbUsuarios;

    // =========================
    // CONFIGURACIÓN CLÍNICA
    // =========================
    @FXML private TextField txtNombreClinica;

    // =========================
    // DAOs
    // =========================
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final EspecialidadDAO especialidadDAO = new EspecialidadDAO();
    private final ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();

    // =========================
    // INITIALIZE
    // =========================
    @FXML
    private void initialize() {

        // Cargar roles
        cmbRol.getItems().addAll("ADMIN", "MEDICO");

        // Cargar especialidades
        cmbEspecialidad.getItems().addAll(
                especialidadDAO.findAll()
        );

        // Cargar usuarios existentes
        cmbUsuarios.getItems().addAll(
                usuarioDAO.findAll()
        );

        // Listener del rol
        cmbRol.valueProperty().addListener(
                (obs, oldVal, newVal) -> actualizarCamposMedico(newVal)
        );

        // Por defecto, deshabilitar campos de médico
        actualizarCamposMedico(null);

        // Cargar nombre de la clínica
        txtNombreClinica.setText(
                configuracionDAO.getValor("NOMBRE_CLINICA")
        );
    }

    // =========================
    // ACTIVAR / DESACTIVAR CAMPOS MÉDICO
    // =========================
    private void actualizarCamposMedico(String rol) {

        boolean esMedico = "MEDICO".equals(rol);

        txtNombreMedico.setDisable(!esMedico);
        txtNumColegiado.setDisable(!esMedico);
        cmbEspecialidad.setDisable(!esMedico);

        if (!esMedico) {
            txtNombreMedico.clear();
            txtNumColegiado.clear();
            cmbEspecialidad.setValue(null);
        }
    }

    // =========================
    // CREAR USUARIO
    // =========================
    @FXML
    private void crearUsuario() {

        String username = txtUsuario.getText();
        String password = txtPassword.getText();
        String rol = cmbRol.getValue();

        if (username == null || username.isBlank()
                || password == null || password.isBlank()
                || rol == null) {

            mostrarError("Debe rellenar usuario, contraseña y rol.");
            return;
        }

        if (usuarioDAO.existsByUsername(username)) {
            mostrarError("El nombre de usuario ya existe.");
            return;
        }

        // Cifrar contraseña
        String passwordHash = PasswordUtils.hashPassword(password);

        Integer idMedico = null;

        // Si es médico, crear médico primero
        if ("MEDICO".equals(rol)) {

            if (txtNombreMedico.getText().isBlank()
                    || txtNumColegiado.getText().isBlank()
                    || cmbEspecialidad.getValue() == null) {

                mostrarError("Debe rellenar todos los datos del médico.");
                return;
            }

            Medico medico = new Medico();
            medico.setNombreApellidos(txtNombreMedico.getText());
            medico.setNumColegiado(txtNumColegiado.getText());
            medico.setIdEspecialidad(
                    cmbEspecialidad.getValue().getIdEspecialidad()
            );
            medico.setActivo(true);

            idMedico = medicoDAO.insert(medico);
        }

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPasswordHash(passwordHash);
        usuario.setRol(rol);
        usuario.setIdMedico(idMedico);
        usuario.setActivo(true);

        usuarioDAO.insert(usuario);

        mostrarInfo("Usuario creado correctamente.");

        limpiarFormularioUsuario();
        refrescarUsuarios();
    }

    // =========================
    // ELIMINAR USUARIO
    // =========================
    @FXML
    private void eliminarUsuario() {

        Usuario seleccionado = cmbUsuarios.getValue();

        if (seleccionado == null) {
            mostrarError("Debe seleccionar un usuario.");
            return;
        }

        // No permitir borrar el usuario logueado
        if (seleccionado.getIdUsuario()
                == UserSession.getUsuario().getIdUsuario()) {

            mostrarError("No puede eliminar el usuario con el que está conectado.");
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Está seguro de eliminar el usuario seleccionado?",
                ButtonType.YES, ButtonType.NO
        );

        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            usuarioDAO.delete(seleccionado.getIdUsuario());
            refrescarUsuarios();
            mostrarInfo("Usuario eliminado.");
        }
    }

    // =========================
    // CONFIGURACIÓN CLÍNICA
    // =========================
    @FXML
    private void cambiarNombreClinica() {

        String nombre = txtNombreClinica.getText();

        if (nombre == null || nombre.isBlank()) {
            mostrarError("El nombre de la clínica no puede estar vacío.");
            return;
        }

        configuracionDAO.setValor("NOMBRE_CLINICA", nombre);
        mostrarInfo("Nombre de la clínica actualizado.");
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================
    private void limpiarFormularioUsuario() {
        txtUsuario.clear();
        txtPassword.clear();
        cmbRol.setValue(null);
        actualizarCamposMedico(null);
    }

    private void refrescarUsuarios() {
        cmbUsuarios.getItems().clear();
        cmbUsuarios.getItems().addAll(
                usuarioDAO.findAll()
        );
    }

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void mostrarInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    @FXML
    private void cambiarLogoClinica() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar logo de la clínica");

        // Filtros de imagen
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Imágenes",
                        "*.png", "*.jpg", "*.jpeg"
                )
        );

        // Abrimos el selector
        File file = fileChooser.showOpenDialog(
                txtNombreClinica.getScene().getWindow()
        );

        if (file == null) {
            // El usuario canceló
            return;
        }

        // Guardamos la ruta en la configuración
        configuracionDAO.setValor(
                "LOGO_CLINICA",
                file.getAbsolutePath()
        );

        mostrarInfo("Logo de la clínica actualizado correctamente.");
    }
// FORMA DE RECUPERAR EL LOGO CUANOD LO QUIERA USAR


/*    String logoPath = configuracionDAO.getValor("LOGO_CLINICA");

if (logoPath != null) {
        imgLogo.setImage(
                new Image(new File(logoPath).toURI().toString())
        );
    }*/



}
