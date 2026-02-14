package es.medicarte.controller;

import es.medicarte.model.*;
import es.medicarte.util.PasswordUtils;
import es.medicarte.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Controlador de la vista de configuración del administrador.
 *
 * Desde esta vista el usuario con rol ADMIN puede:
 * - Crear nuevos usuarios (ADMIN o MEDICO).
 * - Eliminar usuarios existentes.
 * - Cambiar el nombre de la clínica.
 * - Cambiar el logo de la clínica.
 *
 * Esta clase centraliza la lógica de administración del sistema.
 */
public class AdminConfiguracionController {

    // =========================
    // ALTA DE USUARIO
    // =========================

    // Campos del formulario para crear usuario
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<String> cmbRol;

    // Campos adicionales que solo se activan si el rol es MEDICO
    @FXML
    private TextField txtNombreMedico;
    @FXML
    private TextField txtNumColegiado;
    @FXML
    private ComboBox<Especialidad> cmbEspecialidad;

    // =========================
    // ELIMINAR USUARIO
    // =========================

    // ComboBox con los usuarios existentes
    @FXML
    private ComboBox<Usuario> cmbUsuarios;

    // =========================
    // CONFIGURACIÓN CLÍNICA
    // =========================

    // Campo para modificar el nombre de la clínica
    @FXML
    private TextField txtNombreClinica;

    // =========================
    // DAOs
    // =========================

    // DAOs necesarios para interactuar con base de datos
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final EspecialidadDAO especialidadDAO = new EspecialidadDAO();
    private final ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();

    // =========================
    // INITIALIZE
    // =========================

    /**
     * Método que se ejecuta automáticamente al cargar la vista.
     * Aquí inicializamos todos los datos necesarios.
     */
    @FXML
    private void initialize() {

        // Cargamos los roles disponibles en el sistema
        cmbRol.getItems().addAll("ADMIN", "MEDICO");

        // Cargamos las especialidades desde base de datos
        cmbEspecialidad.getItems().addAll(
                especialidadDAO.findAll()
        );

        // Cargamos los usuarios existentes en el combo para poder eliminarlos
        cmbUsuarios.getItems().addAll(
                usuarioDAO.findAll()
        );

        // Listener para activar o desactivar los campos del médico según el rol seleccionado
        cmbRol.valueProperty().addListener(
                (obs, oldVal, newVal) -> actualizarCamposMedico(newVal)
        );

        // Por defecto deshabilitamos los campos del médico
        actualizarCamposMedico(null);

        // Cargamos el nombre actual de la clínica desde la tabla configuración
        txtNombreClinica.setText(
                configuracionDAO.getValor("NOMBRE_CLINICA")
        );
    }

    // =========================
    // ACTIVAR / DESACTIVAR CAMPOS MÉDICO
    // =========================

    /**
     * Activa o desactiva los campos específicos del médico
     * en función del rol seleccionado.
     * <p>
     * Si el rol es MEDICO, se habilitan.
     * Si no, se deshabilitan y se limpian.
     */
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

    /**
     * Crea un nuevo usuario en el sistema.
     * Si el rol es MEDICO, también crea previamente el médico
     * en su tabla correspondiente y enlaza el id_medico.
     */
    @FXML
    private void crearUsuario() {

        String username = txtUsuario.getText();
        String password = txtPassword.getText();
        String rol = cmbRol.getValue();

        // Validación básica de campos obligatorios
        if (username == null || username.isBlank()
                || password == null || password.isBlank()
                || rol == null) {

            mostrarError("Debe rellenar usuario, contraseña y rol.");
            return;
        }

        // Comprobamos que el username no exista ya
        if (usuarioDAO.existsByUsername(username)) {
            mostrarError("El nombre de usuario ya existe.");
            return;
        }

        // Ciframos la contraseña antes de guardarla en base de datos
        String passwordHash = PasswordUtils.hashPassword(password);

        Integer idMedico = null;

        // Si el rol es MEDICO, creamos primero el médico
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

            // Insertamos médico y obtenemos su id
            idMedico = medicoDAO.insert(medico);
        }

        // Creamos el usuario
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

    /**
     * Elimina un usuario seleccionado.
     * No permite eliminar el usuario actualmente logueado.
     */
    @FXML
    private void eliminarUsuario() {

        Usuario seleccionado = cmbUsuarios.getValue();

        if (seleccionado == null) {
            mostrarError("Debe seleccionar un usuario.");
            return;
        }

        // Evitamos que el admin borre su propio usuario
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

    /**
     * Actualiza el nombre de la clínica en la tabla configuración.
     */
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

    /**
     * Limpia el formulario de creación de usuario.
     */
    private void limpiarFormularioUsuario() {
        txtUsuario.clear();
        txtPassword.clear();
        cmbRol.setValue(null);
        actualizarCamposMedico(null);
    }

    /**
     * Recarga la lista de usuarios en el ComboBox.
     */
    private void refrescarUsuarios() {
        cmbUsuarios.getItems().clear();
        cmbUsuarios.getItems().addAll(
                usuarioDAO.findAll()
        );
    }

    /**
     * Muestra un mensaje de error.
     */
    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    /**
     * Muestra un mensaje informativo.
     */
    private void mostrarInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    /**
     * Permite cambiar el logo de la clínica.
     * Guarda la ruta en la tabla configuración.
     */
    @FXML
    private void cambiarLogoClinica() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar logo de la clínica");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Imágenes",
                        "*.png", "*.jpg", "*.jpeg"
                )
        );

        File file = fileChooser.showOpenDialog(
                txtNombreClinica.getScene().getWindow()
        );

        if (file == null) {
            return;
        }

        configuracionDAO.setValor(
                "LOGO_CLINICA",
                file.getAbsolutePath()
        );

        mostrarInfo("Logo de la clínica actualizado correctamente.");
    }
}


// FORMA DE RECUPERAR EL LOGO CUANOD LO QUIERA USAR


/*    String logoPath = configuracionDAO.getValor("LOGO_CLINICA");

if (logoPath != null) {
        imgLogo.setImage(
                new Image(new File(logoPath).toURI().toString())
        );
    }*/




