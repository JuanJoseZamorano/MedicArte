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

public class ConsultaController {

    // =========================
    // CABECERA
    // =========================
    @FXML private Label lblPaciente;
    @FXML private Label lblEdadSexo;
    @FXML private Label lblFechaHora;
    @FXML private Label lblEspecialidad;
    @FXML private ComboBox<Episodio> cmbEpisodio;

    // =========================
    // HISTÓRICO / DATOS CLÍNICOS
    // =========================
    @FXML private ListView<Consulta> listConsultasPrevias;
    @FXML private TextArea txtAntPersonales;
    @FXML private TextArea txtAntFamiliares;
    @FXML private TextArea txtAlergias;
    @FXML private TextArea txtTratamientoActual;

    // =========================
    // CONSULTA ACTUAL
    // =========================
    @FXML private TextArea txtMotivoConsulta;
    @FXML private TextArea txtAnamnesis;
    @FXML private TextArea txtExploracion;
    @FXML private TextArea txtDiagnostico;
    @FXML private TextArea txtTratamiento;
    @FXML private TextArea txtObservaciones;
    @FXML private ComboBox<Especialidad> cmbEspecialidad;
    // =========================
    // ESTADO
    // =========================
    private Cita citaActual;
    private Paciente pacienteActual;
    private Episodio episodioSeleccionado;



    // =========================
    // DAOs
    // =========================
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final EpisodioDAO episodioDAO = new EpisodioDAO();
    private final ConsultaDAO consultaDAO = new ConsultaDAO();

    // =========================
    // INITIALIZE
    // =========================
    private final EspecialidadDAO especialidadDAO = new EspecialidadDAO();
    @FXML
    private void initialize() {

        // =========================
        // Listado de consultas previas (solo lectura)
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
        cmbEpisodio.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) ->
                        onEpisodioSeleccionado(newVal));

        // =========================
        // ComboBox de especialidades
        // =========================
        cmbEspecialidad.setItems(
                FXCollections.observableArrayList(
                        especialidadDAO.findAll()
                )
        );

        // Por defecto, deshabilitado
        cmbEspecialidad.setDisable(true);
        cmbEspecialidad.setValue(null);
    }


    // =========================
    // ENTRADA DESDE CITAS
    // =========================
    public void setCita(Cita cita) {
        this.citaActual = cita;

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
    private void cargarCabecera() {

        lblPaciente.setText(
                pacienteActual.getApellidos() + ", " +
                        pacienteActual.getNombre()
        );

        lblEdadSexo.setText(
                pacienteActual.getFechaNacimiento() + " / " +
                        pacienteActual.getSexo()
        );

        lblFechaHora.setText(
                citaActual.getFechaHora().toString()
        );
    }

    // =========================
    // DATOS CLÍNICOS DEL PACIENTE
    // =========================
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
    private void cargarEpisodios() {

        List<Episodio> episodios =
                episodioDAO.findByPaciente(
                        pacienteActual.getIdPaciente()
                );

        ObservableList<Episodio> items =
                FXCollections.observableArrayList();

        items.addAll(episodios);
        items.add(null); // + Nuevo episodio…

        cmbEpisodio.setItems(items);

        cmbEpisodio.setConverter(new StringConverter<>() {
            @Override
            public String toString(Episodio e) {
                if (e == null) {
                    return "+ Nuevo episodio…";
                }
                return e.getMotivo() + " (" + e.getEstado() + ")";
            }

            @Override
            public Episodio fromString(String s) {
                return null;
            }
        });

        cmbEpisodio.getSelectionModel().select(null);
    }

    private void onEpisodioSeleccionado(Episodio episodio) {

        episodioSeleccionado = episodio;

        if (episodio == null) {
            // Nuevo episodio
            lblEspecialidad.setText("Nueva especialidad");
            cmbEspecialidad.setDisable(false);
            cmbEspecialidad.setValue(null);
            listConsultasPrevias.getItems().clear();
            limpiarConsultaActual();
        } else {
            // Episodio existente
            lblEspecialidad.setText("Especialidad");
            cmbEspecialidad.setDisable(true);

            Especialidad esp = especialidadDAO.findById(
                    episodio.getIdEspecialidad()
            );
            cmbEspecialidad.setValue(esp);

            cargarConsultasPrevias(episodio.getIdEpisodio());
        }

        // Episodio existente
        lblEspecialidad.setText(
                "Especialidad ID: " +
                        episodio.getIdEspecialidad()
        );

        cargarConsultasPrevias(
                episodio.getIdEpisodio()
        );
    }

    // =========================
    // CONSULTAS PREVIAS
    // =========================
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
    @FXML
    private void cancelar() {
        SceneManager.loadScene(
                "/es/medicarte/view/citas.fxml",
                "MedicArte - Citas"
        );
    }

    @FXML
    private void limpiarConsulta() {
        limpiarConsultaActual();
    }

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
        if (episodioSeleccionado != null) {

            // Episodio existente
            idEpisodio = episodioSeleccionado.getIdEpisodio();

        } else {

            // Crear nuevo episodio
            HistoriaDAO historiaDAO = new HistoriaDAO();
            HistoriaClinica historia = historiaDAO.findOrCreateByPaciente(
                    pacienteActual.getIdPaciente()
            );

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

        // =========================
        // VOLVER A AGENDA
        // =========================
        SceneManager.loadScene(
                "/es/medicarte/view/citas.fxml",
                "MedicArte - Citas"
        );
    }
    private int obtenerEspecialidadSeleccionada() {

        Especialidad esp = cmbEspecialidad.getValue();

        if (esp == null) {
            throw new IllegalStateException(
                    "Debe seleccionar una especialidad para el nuevo episodio."
            );
        }

        return esp.getIdEspecialidad();
    }
}
