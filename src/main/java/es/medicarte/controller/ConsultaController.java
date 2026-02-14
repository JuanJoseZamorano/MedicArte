package es.medicarte.controller;

import es.medicarte.model.*;
import es.medicarte.util.SceneManager;
import es.medicarte.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.util.List;

/**
 * Controlador de la vista de Consulta.
 * Esta clase gestiona tanto:
 * - La creación de una nueva consulta (desde Citas)
 * - La visualización en modo lectura (desde Historial)
 * Además controla la lógica de episodios clínicos asociados al paciente.
 */
public class ConsultaController {

    // =========================
    // CABECERA
    // =========================
    // Información básica del paciente y contexto de la consulta

    @FXML private Label lblPaciente;       // Nombre completo del paciente
    @FXML private Label lblEdadSexo;       // Edad y sexo
    @FXML private Label lblFechaHora;      // Fecha y hora de la consulta
    @FXML private Label lblEspecialidad;   // Especialidad asociada al episodio
    @FXML private ComboBox<Episodio> cmbEpisodio; // Selector de episodio clínico

    // =========================
    // HISTÓRICO / DATOS CLÍNICOS
    // =========================
    // Panel izquierdo con historial del episodio y antecedentes del paciente

    @FXML private ListView<Consulta> listConsultasPrevias; // Consultas anteriores del episodio
    @FXML private TextArea txtAntPersonales;               // Antecedentes personales
    @FXML private TextArea txtAntFamiliares;               // Antecedentes familiares
    @FXML private TextArea txtAlergias;                    // Alergias registradas
    @FXML private TextArea txtTratamientoActual;           // Tratamiento actual del paciente

    // =========================
    // CONSULTA ACTUAL
    // =========================
    // Campos editables de la consulta que se está realizando

    @FXML private TextArea txtMotivoConsulta;
    @FXML private TextArea txtAnamnesis;
    @FXML private TextArea txtExploracion;
    @FXML private TextArea txtDiagnostico;
    @FXML private TextArea txtTratamiento;
    @FXML private TextArea txtObservaciones;
    @FXML private ComboBox<Especialidad> cmbEspecialidad; // Solo editable si es episodio nuevo
    @FXML private Button btnGrabarConsulta;

    // =========================
    // ESTADO INTERNO
    // =========================

    private Cita citaActual;               // Cita desde la que se accede (si viene desde agenda)
    private Paciente pacienteActual;       // Paciente al que pertenece la consulta
    private Episodio episodioSeleccionado; // Episodio actualmente seleccionado

    /**
     * Objeto especial que usamos como marcador visual para crear
     * un nuevo episodio clínico desde el ComboBox.
     * Lo utilizamos en vez de null para evitar problemas con los listeners.
     */
    private final Episodio EPISODIO_NUEVO = new Episodio() {{
        setMotivo("Nuevo episodio…");
        setEstado("");
    }};

    // =========================
    // DAOs
    // =========================
    // Acceso a base de datos

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final EpisodioDAO episodioDAO = new EpisodioDAO();
    private final ConsultaDAO consultaDAO = new ConsultaDAO();
    private final EspecialidadDAO especialidadDAO = new EspecialidadDAO();

    // Variable para controlar si estamos en modo solo lectura
    private boolean soloLectura = false;

    // =========================
    // INITIALIZE
    // =========================

    /**
     * Método que se ejecuta automáticamente al cargar el FXML.
     * Aquí configuramos los listeners y el estado inicial de los componentes.
     */
    @FXML
    private void initialize() {

        // Por defecto el botón de grabar está deshabilitado
        btnGrabarConsulta.setDisable(true);

        // =========================
        // Configuración del ListView de consultas previas
        // =========================
        listConsultasPrevias.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Consulta c, boolean empty) {
                super.updateItem(c, empty);

                if (empty || c == null) {
                    setText(null);
                } else {
                    setText(
                            c.getFechaHora().toLocalDate() +
                                    " - " +
                                    (c.getMotivoConsulta() != null
                                            ? c.getMotivoConsulta()
                                            : "Consulta")
                    );
                }
            }
        });

        // =========================
        // Listener de cambio de episodio
        // =========================
        // Cada vez que el usuario cambia el episodio seleccionado,
        // se ejecuta la lógica correspondiente.
        cmbEpisodio.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) ->
                        onEpisodioSeleccionado(newVal));

        // =========================
        // Listener de cambio de especialidad
        // =========================
        cmbEspecialidad.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldEsp, newEsp) -> {
                    if (newEsp != null) {
                        lblEspecialidad.setText("Especialidad: " + newEsp.getNombre());
                    }
                    actualizarEstadoBotonGrabar();
                });

        // =========================
        // Cargar especialidades desde la base de datos
        // =========================
        cmbEspecialidad.setItems(
                FXCollections.observableArrayList(
                        especialidadDAO.findAll()
                )
        );

        // Por defecto la especialidad está deshabilitada
        cmbEspecialidad.setDisable(true);
        cmbEspecialidad.setValue(null);

        // Listener para habilitar el botón grabar solo si hay datos mínimos
        txtMotivoConsulta.textProperty()
                .addListener((obs, oldVal, newVal) ->
                        actualizarEstadoBotonGrabar());
    }

    // =========================
    // ENTRADA DESDE CITAS
    // =========================

    /**
     * Este método es llamado desde SceneManager cuando
     * se accede a la consulta desde una cita pendiente.
     */
    public void setCita(Cita cita) {

        this.citaActual = cita;

        // Recuperamos el paciente asociado a la cita
        this.pacienteActual = pacienteDAO.findById(
                cita.getIdPaciente()
        );

        cargarCabecera();
        cargarDatosPaciente();
        cargarEpisodios();
    }

    // =========================
    // CABECERA
    // =========================

    /**
     * Carga los datos básicos del paciente en la parte superior.
     */
    private void cargarCabecera() {

        lblPaciente.setText(
                pacienteActual.getApellidos() + ", " +
                        pacienteActual.getNombre()
        );

        lblEdadSexo.setText(
                pacienteActual.getFechaNacimiento() + " / " +
                        pacienteActual.getSexo()
        );

        if (citaActual != null) {
            // Si viene desde agenda mostramos fecha real
            lblFechaHora.setText(
                    citaActual.getFechaHora().toString()
            );
        } else {
            // Si viene desde historial
            lblFechaHora.setText("Consulta histórica");
        }
    }

    // =========================
    // DATOS CLÍNICOS DEL PACIENTE
    // =========================

    /**
     * Carga los antecedentes y datos clínicos del paciente.
     * Estos campos son solo informativos.
     */
    private void cargarDatosPaciente() {

        txtAntPersonales.setText(
                pacienteActual.getAntecedentesPersonales()
        );

        txtAntFamiliares.setText(
                pacienteActual.getAntecedentesFamiliares()
        );

        txtAlergias.setText(
                pacienteActual.getAlergias()
        );

        txtTratamientoActual.setText(
                pacienteActual.getTratamientoActual()
        );
    }

    // =========================
    // EPISODIOS
    // =========================

    /**
     * Carga los episodios clínicos existentes del paciente
     * y añade la opción "Nuevo episodio".
     */
    private void cargarEpisodios() {

        List<Episodio> episodios =
                episodioDAO.findByPaciente(
                        pacienteActual.getIdPaciente()
                );

        ObservableList<Episodio> items =
                FXCollections.observableArrayList();

        items.addAll(episodios);

        // Añadimos opción para crear nuevo episodio
        items.add(EPISODIO_NUEVO);

        cmbEpisodio.setItems(items);

        // Convertimos el objeto Episodio en texto visible
        cmbEpisodio.setConverter(new StringConverter<>() {
            @Override
            public String toString(Episodio e) {
                if (e == EPISODIO_NUEVO) {
                    return "+ Nuevo episodio…";
                }
                return e.getMotivo() + " (" + e.getEstado() + ")";
            }

            @Override
            public Episodio fromString(String s) {
                return null;
            }
        });

        // Seleccionamos por defecto nuevo episodio
        cmbEpisodio.getSelectionModel().select(EPISODIO_NUEVO);
    }

    /**
     * Lógica que se ejecuta cuando el usuario cambia de episodio.
     */
    private void onEpisodioSeleccionado(Episodio episodio) {

        episodioSeleccionado = episodio;

        if (episodio == EPISODIO_NUEVO) {
            // Nuevo episodio: permitir seleccionar especialidad
            lblEspecialidad.setText("Especialidad:");
            cmbEspecialidad.setDisable(false);
            cmbEspecialidad.setValue(null);
            cmbEspecialidad.setVisible(true);
            listConsultasPrevias.getItems().clear();
            limpiarConsultaActual();
        } else {
            // Episodio existente
            cmbEspecialidad.setDisable(true);

            Especialidad esp = especialidadDAO.findById(
                    episodio.getIdEspecialidad()
            );

            cmbEspecialidad.setValue(esp);

            if (esp != null) {
                lblEspecialidad.setText("Especialidad: " + esp.getNombre());
            } else {
                lblEspecialidad.setText("Especialidad:");
            }

            cargarConsultasPrevias(episodio.getIdEpisodio());
        }

        actualizarEstadoBotonGrabar();
    }


    // =========================
    // CONSULTAS PREVIAS
    // =========================
    /**
     * Carga en el ListView las consultas pertenecientes
     * al episodio seleccionado.
     * Se utiliza cuando el usuario selecciona un episodio
     * en el ComboBox superior.
     */
    private void cargarConsultasPrevias(int idEpisodio) {

        List<Consulta> consultas =
                consultaDAO.findByEpisodio(idEpisodio);

        listConsultasPrevias.setItems(
                FXCollections.observableArrayList(consultas)
        );
    }

    // =========================
    // CONSULTA ACTUAL
    // =========================
    /**
     * Limpia todos los campos de la consulta actual.
     * Se utiliza cuando se crea un nuevo episodio o cuando
     * se pulsa el botón "Limpiar consulta".
     */
    private void limpiarConsultaActual() {

        txtMotivoConsulta.clear();
        txtAnamnesis.clear();
        txtExploracion.clear();
        txtDiagnostico.clear();
        txtTratamiento.clear();
        txtObservaciones.clear();
    }

    // =========================
    // ACCIONES
    // =========================

    /**
     * Vuelve a la pantalla anterior utilizando la pila
     * de navegación gestionada por SceneManager.
     */
    @FXML
    private void cancelar() {
        SceneManager.goBack();
    }

    /**
     * Limpia únicamente los campos de la consulta actual
     * sin afectar al episodio seleccionado.
     */
    @FXML
    private void limpiarConsulta() {
        limpiarConsultaActual();
    }

    /**
     * Método principal para registrar una consulta.
     * Se encarga de:
     * 1. Validar datos básicos.
     * 2. Crear un nuevo episodio si es necesario.
     * 3. Insertar la consulta en base de datos.
     * 4. Marcar la cita como COMPLETADA.
     */
    @FXML
    private void grabarConsulta() {

        // =========================
        // VALIDACIONES BÁSICAS
        // =========================
        if (citaActual == null || pacienteActual == null) {
            new Alert(Alert.AlertType.ERROR,
                    "No hay contexto de cita o paciente.")
                    .showAndWait();
            return;
        }

        if (txtMotivoConsulta.getText() == null || txtMotivoConsulta.getText().isBlank()) {
            new Alert(Alert.AlertType.WARNING,
                    "Debe indicar el motivo de la consulta.")
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

        int idMedico = usuario.getIdMedico();
        int idEpisodio;

        // =========================
        // OBTENER O CREAR EPISODIO
        // =========================
        if (episodioSeleccionado != null && episodioSeleccionado != EPISODIO_NUEVO) {

            // Si el episodio ya existe, usamos su ID
            idEpisodio = episodioSeleccionado.getIdEpisodio();

        } else {

            // Si es un nuevo episodio, primero comprobamos/creamos historia clínica
            HistoriaDAO historiaDAO = new HistoriaDAO();
            HistoriaClinica historia = historiaDAO.findOrCreateByPaciente(
                    pacienteActual.getIdPaciente()
            );

            // Creamos el nuevo episodio asociado a la historia
            Episodio nuevoEpisodio = new Episodio();
            nuevoEpisodio.setIdHistoria(historia.getIdHistoria());
            nuevoEpisodio.setIdEspecialidad(
                    obtenerEspecialidadSeleccionada()
            );
            nuevoEpisodio.setMotivo(txtMotivoConsulta.getText());
            nuevoEpisodio.setEstado("ABIERTO");

            int nuevoId = episodioDAO.insertar(nuevoEpisodio);

            if (nuevoId <= 0) {
                new Alert(Alert.AlertType.ERROR,
                        "No se pudo crear el episodio clínico.")
                        .showAndWait();
                return;
            }

            idEpisodio = nuevoId;
        }

        // =========================
        // CREAR CONSULTA
        // =========================
        Consulta consulta = new Consulta();
        consulta.setIdEpisodio(idEpisodio);
        consulta.setIdMedico(idMedico);
        consulta.setIdCita(citaActual.getIdCita());
        consulta.setFechaHora(citaActual.getFechaHora());
        consulta.setMotivoConsulta(txtMotivoConsulta.getText());
        consulta.setAnamnesis(txtAnamnesis.getText());
        consulta.setExploracion(txtExploracion.getText());
        consulta.setDiagnostico(txtDiagnostico.getText());
        consulta.setTratamiento(txtTratamiento.getText());
        consulta.setObservaciones(txtObservaciones.getText());
        consulta.setEstado("FINALIZADA");

        boolean insertada = consultaDAO.insert(consulta);

        if (!insertada) {
            new Alert(Alert.AlertType.ERROR,
                    "No se pudo guardar la consulta.")
                    .showAndWait();
            return;
        }

        // =========================
        // MARCAR CITA COMO COMPLETADA
        // =========================
        CitaDAO citaDAO = new CitaDAO();
        boolean completada = citaDAO.completarCita(
                citaActual.getIdCita()
        );

        if (!completada) {
            new Alert(Alert.AlertType.WARNING,
                    "La consulta se guardó, pero no se pudo marcar la cita como completada.")
                    .showAndWait();
        }

        // Mensaje final de confirmación
        Alert ok = new Alert(
                Alert.AlertType.INFORMATION,
                "La consulta se ha registrado correctamente."
        );
        ok.setHeaderText(null);
        ok.showAndWait();

        // Volvemos a la agenda
        SceneManager.loadScene(
                "/es/medicarte/view/citas.fxml",
                "MedicArte - Citas"
        );
    }

    /**
     * Devuelve el ID de la especialidad seleccionada
     * cuando se crea un nuevo episodio.
     */
    private int obtenerEspecialidadSeleccionada() {

        Especialidad esp = cmbEspecialidad.getValue();

        if (esp == null) {
            throw new IllegalStateException(
                    "Debe seleccionar una especialidad para el nuevo episodio."
            );
        }

        return esp.getIdEspecialidad();
    }

    /**
     * Controla cuándo debe estar habilitado el botón
     * "Grabar consulta".
     * Solo se activa si:
     * - Hay motivo válido.
     * - Hay episodio seleccionado (o especialidad en caso de nuevo episodio).
     */
    private void actualizarEstadoBotonGrabar() {

        boolean motivoValido =
                txtMotivoConsulta.getText() != null &&
                        !txtMotivoConsulta.getText().isBlank();

        boolean episodioValido;

        if (episodioSeleccionado == EPISODIO_NUEVO) {
            episodioValido = cmbEspecialidad.getValue() != null;
        } else {
            episodioValido = episodioSeleccionado != null;
        }

        btnGrabarConsulta.setDisable(
                !(motivoValido && episodioValido)
        );
    }

    /**
     * Carga una consulta en modo solo lectura.
     * Se utiliza cuando accedemos desde el historial
     * clínico en lugar de desde la agenda.
     */
    public void setConsultaSoloLectura(Consulta consulta) {
        this.soloLectura = true;
        cargarConsulta(consulta);
        deshabilitarEdicionConsulta();
    }

    /**
     * Rellena todos los campos con los datos
     * de una consulta ya existente.
     */
    private void cargarConsulta(Consulta consulta) {

        this.citaActual = null; // No se edita desde historial

        this.episodioSeleccionado = episodioDAO.findById(
                consulta.getIdEpisodio()
        );

        HistoriaDAO historiaDAO = new HistoriaDAO();
        HistoriaClinica historia =
                historiaDAO.findById(episodioSeleccionado.getIdHistoria());

        pacienteActual = pacienteDAO.findById(
                historia.getIdPaciente()
        );

        cargarCabecera();
        cargarDatosPaciente();

        txtMotivoConsulta.setText(consulta.getMotivoConsulta());
        txtAnamnesis.setText(consulta.getAnamnesis());
        txtExploracion.setText(consulta.getExploracion());
        txtDiagnostico.setText(consulta.getDiagnostico());
        txtTratamiento.setText(consulta.getTratamiento());
        txtObservaciones.setText(consulta.getObservaciones());

        lblEspecialidad.setText(
                "Especialidad: " +
                        especialidadDAO.findById(
                                episodioSeleccionado.getIdEspecialidad()
                        ).getNombre()
        );
    }

    /**
     * Desactiva todos los campos de edición cuando
     * se visualiza una consulta histórica.
     */
    private void deshabilitarEdicionConsulta() {

        txtMotivoConsulta.setEditable(false);
        txtAnamnesis.setEditable(false);
        txtExploracion.setEditable(false);
        txtDiagnostico.setEditable(false);
        txtTratamiento.setEditable(false);
        txtObservaciones.setEditable(false);

        cmbEpisodio.setDisable(true);
        cmbEspecialidad.setDisable(true);

        btnGrabarConsulta.setDisable(true);
    }
}
