package es.medicarte.controller;

import es.medicarte.model.*;
import es.medicarte.util.SceneManager;
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
    @FXML
    private void initialize() {

        // Listado de consultas previas (solo lectura)
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

        // Listener de cambio de episodio
        cmbEpisodio.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) ->
                        onEpisodioSeleccionado(newVal));
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
            lblEspecialidad.setText("Seleccione especialidad");
            listConsultasPrevias.getItems().clear();
            limpiarConsultaActual();
            return;
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
        // Se implementará en el siguiente paso
        // Aquí se creará la consulta y, si procede, el episodio
    }
}
