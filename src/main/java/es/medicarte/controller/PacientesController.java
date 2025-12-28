package es.medicarte.controller;

import es.medicarte.model.Paciente;
import es.medicarte.model.PacienteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import es.medicarte.util.SceneManager;

public class PacientesController {

    // ===== LISTA =====
    @FXML
    private ListView<Paciente> listPacientes;

    // ===== BÚSQUEDA =====
    @FXML private TextField txtBuscarApellidos;
    @FXML private TextField txtBuscarNombre;
    @FXML private TextField txtBuscarDni;

    // ===== FORMULARIO =====
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

    // ===== DATOS CLÍNICOS =====
    @FXML private TextArea txtAntPersonales;
    @FXML private TextArea txtAntFamiliares;
    @FXML private TextArea txtTratamiento;
    @FXML private TextArea txtAlergias;

    // ===== FOTO =====
    @FXML private ImageView imgFoto;

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final ObservableList<Paciente> pacientes = FXCollections.observableArrayList();

    private Paciente pacienteSeleccionado;

    @FXML
    private void initialize() {
        configurarListView();
        configurarComboSexo();
        cargarPacientes();
        configurarSeleccion();
    }

    // =================== CONFIGURACIONES ===================

    private void configurarListView() {
        listPacientes.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Paciente p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null :
                        p.getApellidos() + ", " + p.getNombre() + " (" + p.getDni() + ")");
            }
        });
    }

    private void configurarComboSexo() {
        cmbSexo.getItems().addAll("Hombre", "Mujer", "Otro");
    }

    private void configurarSeleccion() {
        listPacientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        pacienteSeleccionado = newVal;
                        cargarPacienteEnFormulario(newVal);
                    }
                }
        );
    }

    // =================== CARGA DE DATOS ===================

    private void cargarPacientes() {
        pacientes.setAll(pacienteDAO.findAll());
        listPacientes.setItems(pacientes);
    }

    private void cargarPacienteEnFormulario(Paciente p) {

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

        txtAntPersonales.setText(p.getAntecedentesPersonales());
        txtAntFamiliares.setText(p.getAntecedentesFamiliares());
        txtTratamiento.setText(p.getTratamientoActual());
        txtAlergias.setText(p.getAlergias());

        // Foto se implementará más adelante
        imgFoto.setImage(null);
    }

    @FXML
    private void guardarPaciente() {

        String dniIntroducido = txtDni.getText();

        // === CASO INSERT ===
        if (pacienteSeleccionado == null) {

            if (pacienteDAO.existsByDni(dniIntroducido)) {
                new Alert(
                        Alert.AlertType.WARNING,
                        "Ya existe un paciente con ese DNI.\nNo se puede crear el paciente."
                ).showAndWait();
                return;
            }

            Paciente p = new Paciente();
            rellenarPacienteDesdeFormulario(p);

            boolean ok = pacienteDAO.insert(p);

            if (ok) {
                cargarPacientes();
                listPacientes.getSelectionModel().select(p);
            } else {
                new Alert(Alert.AlertType.ERROR, "No se pudo guardar el paciente").showAndWait();
            }

            return;
        }

        // === CASO UPDATE ===
        String dniOriginal = pacienteSeleccionado.getDni();

        if (!dniIntroducido.equals(dniOriginal)
                && pacienteDAO.existsByDni(dniIntroducido)) {

            new Alert(
                    Alert.AlertType.WARNING,
                    "No se puede modificar el DNI.\nYa existe otro paciente con ese DNI."
            ).showAndWait();
            return;
        }

        rellenarPacienteDesdeFormulario(pacienteSeleccionado);

        boolean ok = pacienteDAO.update(pacienteSeleccionado);

        if (ok) {
            cargarPacientes();
            listPacientes.getSelectionModel().select(pacienteSeleccionado);
        } else {
            new Alert(Alert.AlertType.ERROR, "No se pudo actualizar el paciente").showAndWait();
        }
    }

    private void rellenarPacienteDesdeFormulario(Paciente p) {

        p.setApellidos(txtApellidos.getText());
        p.setNombre(txtNombre.getText());
        p.setFechaNacimiento(dpFechaNacimiento.getValue());
        p.setSexo(cmbSexo.getValue());
        p.setDni(txtDni.getText());
        p.setNhc(txtNhc.getText());
        p.setNuhsa(txtNuhsa.getText());
        p.setNuss(txtNuss.getText());
        p.setTelefono(txtTelefono.getText());
        p.setEmail(txtEmail.getText());
        p.setDireccion(txtDireccion.getText());
        p.setProvincia(txtProvincia.getText());
        p.setCp(txtCp.getText());
        p.setAseguradora(txtAseguradora.getText());
        p.setNumPoliza(txtPoliza.getText());

        p.setAntecedentesPersonales(txtAntPersonales.getText());
        p.setAntecedentesFamiliares(txtAntFamiliares.getText());
        p.setTratamientoActual(txtTratamiento.getText());
        p.setAlergias(txtAlergias.getText());
    }


    @FXML
    private void limpiarFormulario() {

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

        txtAntPersonales.clear();
        txtAntFamiliares.clear();
        txtTratamiento.clear();
        txtAlergias.clear();

        imgFoto.setImage(null);
        listPacientes.getSelectionModel().clearSelection();
    }
    @FXML
    private void buscarPacientes() {

        String apellidos = txtBuscarApellidos.getText();
        String nombre = txtBuscarNombre.getText();
        String dni = txtBuscarDni.getText();

        pacientes.setAll(
                pacienteDAO.buscar(apellidos, nombre, dni)
        );

        listPacientes.getSelectionModel().clearSelection();
        pacienteSeleccionado = null;
    }
    @FXML
    private void eliminarPaciente() {

        if (pacienteSeleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "No hay paciente seleccionado").showAndWait();
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Seguro que desea eliminar el paciente?",
                ButtonType.YES, ButtonType.NO
        );

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {

                boolean ok = pacienteDAO.delete(pacienteSeleccionado.getIdPaciente());

                if (ok) {
                    limpiarFormulario();
                    cargarPacientes();
                } else {
                    new Alert(Alert.AlertType.ERROR, "No se pudo eliminar el paciente").showAndWait();
                }
            }
        });
    }

    @FXML
    private void cancelar() {
        SceneManager.loadScene(
                "/es/medicarte/view/medico_dashboard.fxml",
                "MedicArte - Área Médica"
        );
    }


}
