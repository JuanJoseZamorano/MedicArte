package es.medicarte.controller;

import es.medicarte.model.*;
import es.medicarte.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

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
    @FXML private ComboBox<Medico> cmbMedico;
    @FXML private Spinner<LocalTime> spHora;
    @FXML private TextArea txtObservaciones;
    @FXML private TextField txtDuracion;

    // =========================
    // DAOs
    // =========================
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final CitaDAO citaDAO = new CitaDAO();
    // =========================
    // ESTADO
    // =========================
    private Paciente pacienteSeleccionado;
    private Cita citaEnEdicion = null;
    // =========================
    // INITIALIZE
    // =========================
    @FXML
    private void initialize() {

        // Spinner de hora (0–23)
        SpinnerValueFactory<LocalTime> valueFactory =
                new SpinnerValueFactory<>() {

                    {
                        setValue(LocalTime.of(9, 0));
                    }

                    @Override
                    public void decrement(int steps) {
                        setValue(getValue().minusMinutes(15));
                    }

                    @Override
                    public void increment(int steps) {
                        setValue(getValue().plusMinutes(15));
                    }
                };

        spHora.setValueFactory(valueFactory);
        spHora.setEditable(true);

        // Todos los campos del paciente deben estar bloqueados
        bloquearCamposPaciente();
        // Cargar médicos
        cmbMedico.setItems(
                FXCollections.observableArrayList(
                        medicoDAO.findAllActivos()
                )
        );
        // Cómo se muestran los médicos (texto visible)
        cmbMedico.setConverter(new StringConverter<>() {
            @Override
            public String toString(Medico medico) {
                return medico != null ? medico.getNombreApellidos() : "";
            }

            @Override
            public Medico fromString(String string) {
                return null; // no se usa
            }
        });

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



// Volver anterior antes de crear la pila de navegacion.
//    @FXML
//    private void volver() {
//        SceneManager.loadScene(
//                "/es/medicarte/view/citas.fxml",
//                "MedicArte - Citas"
//        );
//    }

    @FXML
    private void volver() {
        SceneManager.goBack();
    }

    @FXML
    private void guardarCita() {

        if (!validarFormulario()) {
            return;
        }
        if (citaEnEdicion == null) {
            crearNuevaCita();
        } else {
            actualizarCitaExistente();
        }
    }

    public void setCitaParaEdicion(Cita cita) {

        this.citaEnEdicion = cita;

        // Cargar paciente
        Paciente p = pacienteDAO.findById(cita.getIdPaciente());
        if (p != null) {
            txtBuscarDni.setText(p.getDni());
            cargarPacienteEnFormulario(p);
            txtBuscarDni.setDisable(true);
        }

        // Cargar fecha y hora
        dpFechaCita.setValue(
                cita.getFechaHora().toLocalDate()
        );

        // hora

        LocalTime hora = cita.getFechaHora()
                .toLocalTime()
                .withSecond(0)
                .withNano(0);

        spHora.getValueFactory().setValue(hora);

        // Duración
        if (cita.getDuracionMin() != null) {
            txtDuracion.setText(String.valueOf(cita.getDuracionMin()));
        }

        // Observaciones
        txtObservaciones.setText(cita.getObservaciones());

        // Médico
        Medico medico = medicoDAO.findById(cita.getIdMedico());
        if (medico != null) {
            cmbMedico.setValue(medico);
        }
    }

    private boolean validarFormulario() {

        if (pacienteSeleccionado == null) {
            mostrarWarning("Debe seleccionar un paciente.");
            return false;
        }

        if (cmbMedico.getValue() == null) {
            mostrarWarning("Debe seleccionar un médico para la cita.");
            return false;
        }

        if (dpFechaCita.getValue() == null) {
            mostrarWarning("Debe seleccionar una fecha para la cita.");
            return false;
        }

        if (spHora.getValue() == null) {
            mostrarWarning("Debe seleccionar una hora para la cita.");
            return false;
        }



        try {
            int duracion = Integer.parseInt(txtDuracion.getText());
            if (duracion <= 0) {
                throw new NumberFormatException();
            }
        } catch (Exception e) {
            mostrarWarning("La duración debe ser un número de minutos válido.");
            return false;
        }

        return true;
    }

    private void mostrarWarning(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }

    private void crearNuevaCita() {

        Cita cita = new Cita();
        cita.setIdPaciente(pacienteSeleccionado.getIdPaciente());
        cita.setIdMedico(cmbMedico.getValue().getIdMedico());
        cita.setFechaHora(
                LocalDateTime.of(dpFechaCita.getValue(), spHora.getValue())
        );
        cita.setDuracionMin(Integer.parseInt(txtDuracion.getText()));
        cita.setObservaciones(txtObservaciones.getText());
        cita.setEstado("PENDIENTE");
        cita.setOrigen("CLINICA");

        boolean ok = citaDAO.insert(cita);

        if (ok) {
            new Alert(Alert.AlertType.INFORMATION,
                    "La cita ha sido creada correctamente.")
                    .showAndWait();
            SceneManager.goBack();
        }
    }
    private void actualizarCitaExistente() {

        citaEnEdicion.setIdMedico(cmbMedico.getValue().getIdMedico());
        citaEnEdicion.setFechaHora(
                LocalDateTime.of(dpFechaCita.getValue(), spHora.getValue())
        );
        citaEnEdicion.setDuracionMin(Integer.parseInt(txtDuracion.getText()));
        citaEnEdicion.setObservaciones(txtObservaciones.getText());

        boolean ok = citaDAO.update(citaEnEdicion);

        if (ok) {
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "La cita ha sido modificada correctamente."
            ).showAndWait();
            SceneManager.goBack();
        }
    }



}
