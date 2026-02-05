package es.medicarte.controller;

import es.medicarte.model.*;
import es.medicarte.util.SceneManager;
import es.medicarte.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class NuevaCitaController {

    // =========================
    // BÚSQUEDA
    // =========================
    @FXML private TextField txtBuscarDni;

    // =========================
    // FICHA PACIENTE (SOLO LECTURA)
    // =========================
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNombre;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private ComboBox<String> cmbSexo;
    @FXML private TextField txtDni;
    @FXML private TextField txtNhc;
    @FXML private TextField txtNuhsa;
    @FXML private TextField txtNuss;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtProvincia;
    @FXML private TextField txtCp;
    @FXML private TextField txtAseguradora;
    @FXML private TextField txtPoliza;
    @FXML private ImageView imgFoto;

    // =========================
    // DATOS DE LA CITA
    // =========================
    @FXML private DatePicker dpFechaCita;
    @FXML private ComboBox<String> cmbMedico;
    @FXML private Spinner<Integer> spHora;
    @FXML private TextArea txtObservaciones;

    // =========================
    // DAOs
    // =========================
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    // =========================
    // ESTADO
    // =========================
    private Paciente pacienteSeleccionado;

    // =========================
    // INITIALIZE
    // =========================
    @FXML
    private void initialize() {

        // Spinner de hora (0–23)
        spHora.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9)
        );

        // Todos los campos del paciente deben estar bloqueados
        bloquearCamposPaciente();

    }

    // metodo que busca el dni si viene de citas de pacientes -> ver citas
    public void setDniInicial(String dni) {
        if (dni != null && !dni.isBlank()) {
            txtBuscarDni.setText(dni);
            buscarPaciente();
        }
    }
    private void bloquearCamposPaciente() {
        txtApellidos.setEditable(false);
        txtNombre.setEditable(false);
        txtDni.setEditable(false);
        txtNhc.setEditable(false);
        txtNuhsa.setEditable(false);
        txtNuss.setEditable(false);
        txtTelefono.setEditable(false);
        txtEmail.setEditable(false);
        txtDireccion.setEditable(false);
        txtProvincia.setEditable(false);
        txtCp.setEditable(false);
        txtAseguradora.setEditable(false);
        txtPoliza.setEditable(false);

        dpFechaNacimiento.setDisable(true);
        cmbSexo.setDisable(true);
    }

    // =========================
    // CARGA PACIENTE
    // =========================
    private void cargarPacienteEnFormulario(Paciente p) {

        pacienteSeleccionado = p;

        txtApellidos.setText(p.getApellidos());
        txtNombre.setText(p.getNombre());
        dpFechaNacimiento.setValue(p.getFechaNacimiento());
        cmbSexo.setValue(p.getSexo());
        txtDni.setText(p.getDni());
        txtNhc.setText(p.getNhc());
        txtNuhsa.setText(p.getNuhsa());
        txtNuss.setText(p.getNuss());
        txtTelefono.setText(p.getTelefono());
        txtEmail.setText(p.getEmail());
        txtDireccion.setText(p.getDireccion());
        txtProvincia.setText(p.getProvincia());
        txtCp.setText(p.getCp());
        txtAseguradora.setText(p.getAseguradora());
        txtPoliza.setText(p.getNumPoliza());

        // Foto (si existe)
        if (p.getFotoPath() != null) {
            try {
                imgFoto.setImage(new javafx.scene.image.Image(
                        new java.io.File(p.getFotoPath()).toURI().toString()
                ));
            } catch (Exception ignored) {}
        }
    }

    // =========================
    // ACCIONES
    // =========================
    @FXML
    private void buscarPaciente() {

        String dni = txtBuscarDni.getText();

        if (dni == null || dni.isBlank()) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Introduzca un DNI para buscar el paciente."
            ).showAndWait();
            return;
        }

        Paciente p = pacienteDAO.findByDni(dni);

        if (p == null) {
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "No se ha encontrado ningún paciente con ese DNI."
            ).showAndWait();
            limpiarFormularioPaciente();
            return;
        }

        cargarPacienteEnFormulario(p);
    }
    private void limpiarFormularioPaciente() {

        pacienteSeleccionado = null;

        txtApellidos.clear();
        txtNombre.clear();
        dpFechaNacimiento.setValue(null);
        cmbSexo.setValue(null);
        txtDni.clear();
        txtNhc.clear();
        txtNuhsa.clear();
        txtNuss.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtDireccion.clear();
        txtProvincia.clear();
        txtCp.clear();
        txtAseguradora.clear();
        txtPoliza.clear();

        imgFoto.setImage(null);
    }




    @FXML
    private void volver() {
        SceneManager.loadScene(
                "/es/medicarte/view/citas.fxml",
                "MedicArte - Citas"
        );
    }

    @FXML
    private void guardarCita() {

        if (pacienteSeleccionado == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Debe seleccionar un paciente antes de guardar la cita.")
                    .showAndWait();
            return;
        }

        if (dpFechaCita.getValue() == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Debe seleccionar una fecha para la cita.")
                    .showAndWait();
            return;
        }

        Integer hora = spHora.getValue();
        if (hora == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Debe seleccionar una hora para la cita.")
                    .showAndWait();
            return;
        }

        Usuario usuario = UserSession.getUsuario();
        if (usuario == null || usuario.getIdMedico() == null) {
            new Alert(Alert.AlertType.ERROR,
                    "No se ha podido identificar al médico logueado.")
                    .showAndWait();
            return;
        }

        LocalDateTime fechaHora = LocalDateTime.of(
                dpFechaCita.getValue(),
                LocalTime.of(hora, 0)
        );

        Cita cita = new Cita();
        cita.setIdPaciente(pacienteSeleccionado.getIdPaciente());
        cita.setIdMedico(usuario.getIdMedico());
        cita.setFechaHora(fechaHora);
        cita.setEstado("PENDIENTE");
        cita.setOrigen("CLINICA");
        cita.setObservaciones(txtObservaciones.getText());

        CitaDAO citaDAO = new CitaDAO();
        boolean ok = citaDAO.insert(cita);

        if (!ok) {
            new Alert(Alert.AlertType.ERROR,
                    "No se pudo guardar la cita.")
                    .showAndWait();
            return;
        }

        SceneManager.loadScene(
                "/es/medicarte/view/citas.fxml",
                "MedicArte - Citas"
        );
    }

}
