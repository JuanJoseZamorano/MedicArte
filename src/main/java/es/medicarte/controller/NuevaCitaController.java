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

/**
 * Controlador encargado de la gestión de la vista de creación y edición de citas.
 * Esta clase permite:
 *  - Buscar un paciente por DNI
 *  - Mostrar sus datos en modo solo lectura
 *  - Crear una nueva cita
 *  - Editar una cita existente
 * Se reutiliza la misma vista tanto para alta como para edición,
 * diferenciando el comportamiento mediante la variable citaEnEdicion.
 */
public class NuevaCitaController {

    // =========================
    // BÚSQUEDA
    // =========================

    // Campo para introducir o recibir el DNI del paciente
    @FXML
    private TextField txtBuscarDni;

    // =========================
    // FICHA PACIENTE (SOLO LECTURA)
    // =========================

    // Campos informativos del paciente (no editables desde esta vista)
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtNombre;
    @FXML
    private DatePicker dpFechaNacimiento;
    @FXML
    private ComboBox<String> cmbSexo;
    @FXML
    private TextField txtDni;
    @FXML
    private TextField txtNhc;
    @FXML
    private TextField txtNuhsa;
    @FXML
    private TextField txtNuss;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtProvincia;
    @FXML
    private TextField txtCp;
    @FXML
    private TextField txtAseguradora;
    @FXML
    private TextField txtPoliza;
    @FXML
    private ImageView imgFoto;

    // =========================
    // DATOS DE LA CITA
    // =========================

    // Campos propios de la cita
    @FXML
    private DatePicker dpFechaCita;
    @FXML
    private ComboBox<Medico> cmbMedico;
    @FXML
    private Spinner<LocalTime> spHora;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private TextField txtDuracion;

    // =========================
    // DAOs
    // =========================

    // Acceso a datos de pacientes, médicos y citas
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final CitaDAO citaDAO = new CitaDAO();

    // =========================
    // ESTADO
    // =========================

    // Paciente actualmente seleccionado para la cita
    private Paciente pacienteSeleccionado;

    // Si no es null, estamos editando una cita existente
    private Cita citaEnEdicion = null;

    // =========================
    // INITIALIZE
    // =========================

    /**
     * Método que se ejecuta automáticamente al cargar la vista.
     * Aquí configuramos:
     * - El spinner de hora en intervalos de 15 minutos
     * - El bloqueo de los campos del paciente
     * - La carga del combo de médicos activos
     */
    @FXML
    private void initialize() {

        // Configuración del Spinner para seleccionar hora en tramos de 15 minutos
        SpinnerValueFactory<LocalTime> valueFactory =
                new SpinnerValueFactory<>() {

                    {
                        setValue(LocalTime.of(9, 0)); // Hora inicial por defecto
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

        // Bloqueamos los campos del paciente (solo lectura)
        bloquearCamposPaciente();

        // Cargar médicos activos en el ComboBox
        cmbMedico.setItems(
                FXCollections.observableArrayList(
                        medicoDAO.findAllActivos()
                )
        );

        // Definimos cómo se muestra el médico en el ComboBox
        cmbMedico.setConverter(new StringConverter<>() {
            @Override
            public String toString(Medico medico) {
                return medico != null ? medico.getNombreApellidos() : "";
            }

            @Override
            public Medico fromString(String string) {
                return null; // No se usa conversión inversa
            }
        });
    }

    /**
     * Método utilizado cuando se llega desde la vista de citas o pacientes.
     * Permite que el DNI se cargue automáticamente al abrir la vista.
     */
    public void setDniInicial(String dni) {
        if (dni != null && !dni.isBlank()) {
            txtBuscarDni.setText(dni);
            buscarPaciente();
        }
    }

    /**
     * Deshabilita todos los campos del paciente para que sean solo informativos.
     */
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

    /**
     * Rellena todos los campos del formulario con los datos del paciente seleccionado.
     */
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

        // Carga de la foto si existe
        if (p.getFotoPath() != null) {
            try {
                imgFoto.setImage(new javafx.scene.image.Image(
                        new java.io.File(p.getFotoPath()).toURI().toString()
                ));
            } catch (Exception ignored) {
            }
        }
    }
    // =========================
    // ACCIONES
    // =========================

    /**
     * Método asociado al botón "Buscar".
     * Permite buscar un paciente por DNI y cargar sus datos
     * en el formulario de la nueva cita.
     */
    @FXML
    private void buscarPaciente() {

        String dni = txtBuscarDni.getText();

        // Validación básica: el DNI no puede estar vacío
        if (dni == null || dni.isBlank()) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Introduzca un DNI para buscar el paciente."
            ).showAndWait();
            return;
        }

        // Se consulta la base de datos a través del DAO
        Paciente p = pacienteDAO.findByDni(dni);

        // Si no se encuentra paciente, se informa al usuario
        if (p == null) {
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "No se ha encontrado ningún paciente con ese DNI."
            ).showAndWait();
            limpiarFormularioPaciente();
            return;
        }

        // Si existe, se cargan los datos en el formulario
        cargarPacienteEnFormulario(p);
    }

    /**
     * Limpia todos los campos del formulario relacionados con el paciente.
     * Se utiliza cuando no se encuentra el paciente o cuando se desea
     * reiniciar la ficha antes de una nueva búsqueda.
     */
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


    /*
     * Método antiguo para volver a la vista de citas.
     * Se dejó comentado cuando se implementó la navegación
     * mediante pila (SceneManager.goBack()) para que el flujo
     * fuese más dinámico según desde dónde se acceda.
     */

//    @FXML
//    private void volver() {
//        SceneManager.loadScene(
//                "/es/medicarte/view/citas.fxml",
//                "MedicArte - Citas"
//        );
//    }

    /**
     * Vuelve a la vista anterior utilizando el sistema
     * de navegación basado en pila (history).
     * Permite mantener una navegación más lógica y flexible.
     */
    @FXML
    private void volver() {
        SceneManager.goBack();
    }

    /**
     * Método principal asociado al botón "Guardar".
     * Decide si se debe crear una nueva cita o actualizar una existente,
     * dependiendo de si estamos en modo edición o en modo creación.
     */
    @FXML
    private void guardarCita() {

        // Primero se validan los datos del formulario
        if (!validarFormulario()) {
            return;
        }

        // Si no hay cita en edición → crear nueva
        if (citaEnEdicion == null) {
            crearNuevaCita();
        }
        // Si ya existe → actualizar
        else {
            actualizarCitaExistente();
        }
    }

    /**
     * Este método se utiliza cuando se accede desde la vista de citas
     * en modo "Editar".
     * Carga todos los datos de la cita seleccionada en el formulario
     * para permitir su modificación.
     */
    public void setCitaParaEdicion(Cita cita) {

        this.citaEnEdicion = cita;

        // Cargar paciente asociado a la cita
        Paciente p = pacienteDAO.findById(cita.getIdPaciente());
        if (p != null) {
            txtBuscarDni.setText(p.getDni());
            cargarPacienteEnFormulario(p);

            // Se bloquea el campo DNI para evitar cambiar el paciente
            txtBuscarDni.setDisable(true);
        }

        // Cargar fecha
        dpFechaCita.setValue(
                cita.getFechaHora().toLocalDate()
        );

        // Cargar hora (normalizando segundos y nanos)
        LocalTime hora = cita.getFechaHora()
                .toLocalTime()
                .withSecond(0)
                .withNano(0);

        spHora.getValueFactory().setValue(hora);

        // Cargar duración si existe
        if (cita.getDuracionMin() != null) {
            txtDuracion.setText(String.valueOf(cita.getDuracionMin()));
        }

        // Cargar observaciones
        txtObservaciones.setText(cita.getObservaciones());

        // Cargar médico asociado
        Medico medico = medicoDAO.findById(cita.getIdMedico());
        if (medico != null) {
            cmbMedico.setValue(medico);
        }
    }

    /**
     * Método que valida todos los campos obligatorios antes de crear o actualizar una cita.
     * Se separa en un método independiente para mantener el código más limpio
     * y evitar repetir validaciones en distintos puntos.
     */
    private boolean validarFormulario() {

        // Comprobamos que exista un paciente seleccionado
        if (pacienteSeleccionado == null) {
            mostrarWarning("Debe seleccionar un paciente.");
            return false;
        }

        // Comprobamos que exista un médico seleccionado
        if (cmbMedico.getValue() == null) {
            mostrarWarning("Debe seleccionar un médico para la cita.");
            return false;
        }

        // Validamos que haya fecha
        if (dpFechaCita.getValue() == null) {
            mostrarWarning("Debe seleccionar una fecha para la cita.");
            return false;
        }

        // Validamos que haya hora
        if (spHora.getValue() == null) {
            mostrarWarning("Debe seleccionar una hora para la cita.");
            return false;
        }

        // Validamos duración (debe ser número positivo)
        try {
            int duracion = Integer.parseInt(txtDuracion.getText());
            if (duracion <= 0) {
                throw new NumberFormatException();
            }
        } catch (Exception e) {
            mostrarWarning("La duración debe ser un número de minutos válido.");
            return false;
        }

        // Si todo es correcto, devolvemos true
        return true;
    }

    /**
     * Método auxiliar para mostrar alertas de tipo WARNING.
     * Se centraliza aquí para evitar repetir código y mantener coherencia.
     */
    private void mostrarWarning(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }

    /**
     * Método encargado de crear una nueva cita en base de datos.
     * Se ejecuta únicamente cuando no estamos en modo edición.
     */
    private void crearNuevaCita() {

        // Creamos el objeto Cita y asignamos sus valores
        Cita cita = new Cita();
        cita.setIdPaciente(pacienteSeleccionado.getIdPaciente());
        cita.setIdMedico(cmbMedico.getValue().getIdMedico());

        // Construimos el LocalDateTime a partir del DatePicker y el Spinner de hora
        cita.setFechaHora(
                LocalDateTime.of(dpFechaCita.getValue(), spHora.getValue())
        );

        // Asignamos duración y observaciones
        cita.setDuracionMin(Integer.parseInt(txtDuracion.getText()));
        cita.setObservaciones(txtObservaciones.getText());

        // Por defecto, toda cita nueva queda pendiente y de origen clínica
        cita.setEstado("PENDIENTE");
        cita.setOrigen("CLINICA");

        // Insertamos en base de datos
        boolean ok = citaDAO.insert(cita);

        if (ok) {
            new Alert(Alert.AlertType.INFORMATION,
                    "La cita ha sido creada correctamente.")
                    .showAndWait();

            // Volvemos a la vista anterior usando la pila de navegación
            SceneManager.goBack();
        }
    }

    /**
     * Método encargado de actualizar una cita existente.
     * Solo modifica los campos editables: médico, fecha/hora, duración y observaciones.
     */
    private void actualizarCitaExistente() {

        // Actualizamos los datos modificables
        citaEnEdicion.setIdMedico(cmbMedico.getValue().getIdMedico());
        citaEnEdicion.setFechaHora(
                LocalDateTime.of(dpFechaCita.getValue(), spHora.getValue())
        );
        citaEnEdicion.setDuracionMin(Integer.parseInt(txtDuracion.getText()));
        citaEnEdicion.setObservaciones(txtObservaciones.getText());

        // Ejecutamos el update en base de datos
        boolean ok = citaDAO.update(citaEnEdicion);

        if (ok) {
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "La cita ha sido modificada correctamente."
            ).showAndWait();

            // Volvemos a la vista anterior
            SceneManager.goBack();
        }
    }
}