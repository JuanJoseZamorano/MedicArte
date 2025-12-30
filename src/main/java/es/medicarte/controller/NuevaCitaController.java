package es.medicarte.controller;

import es.medicarte.model.Paciente;
import es.medicarte.model.PacienteDAO;
import es.medicarte.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

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
    private void guardarCita() {
        // Se implementará más adelante
    }
    @FXML
    private void volver() {
        SceneManager.loadScene(
                "/es/medicarte/view/citas.fxml",
                "MedicArte - Citas"
        );
    }

}
