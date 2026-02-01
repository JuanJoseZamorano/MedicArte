package es.medicarte.controller;

import es.medicarte.model.*;
import es.medicarte.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorialController {

    // =========================
    // CABECERA PACIENTE
    // =========================
    @FXML private Label lblNombrePaciente;
    @FXML private Label lblDni;
    @FXML private Label lblFechaNacimiento;
    @FXML private Label lblDireccion;
    @FXML private Label lblEmail;
    @FXML private Label lblTelefono;
    @FXML private Label lblAseguradora;
    @FXML private ImageView imgFoto;

    // =========================
    // TAB GENERAL (EPISODIOS)
    // =========================
    @FXML private TableView<Episodio> tblEpisodios;
    @FXML private TableColumn<Episodio, String> colEspecialidad;
    @FXML private TableColumn<Episodio, String> colMotivo;
    @FXML private TableColumn<Episodio, Integer> colNumConsultas;
    @FXML private TableColumn<Episodio, String> colFechaInicio;
    @FXML private TableColumn<Episodio, String> colEstado;

    // =========================
    // TAB EPISODIOS (CONSULTAS)
    // =========================
    @FXML private ComboBox<Episodio> cmbFiltroEpisodio;
    @FXML private TableView<Consulta> tblConsultas;
    @FXML private TableColumn<Consulta, String> colEpisodioConsulta;
    @FXML private TableColumn<Consulta, Integer> colNumConsulta;
    @FXML private TableColumn<Consulta, String> colMotivoConsulta;
    @FXML private TableColumn<Consulta, String> colFechaConsulta;

    // =========================
    // ESTADO
    // =========================
    private Paciente pacienteActual;

    // =========================
    // DAOs
    // =========================
    private final EpisodioDAO episodioDAO = new EpisodioDAO();
    private final ConsultaDAO consultaDAO = new ConsultaDAO();
    private final EspecialidadDAO especialidadDAO = new EspecialidadDAO();

    // =========================
    // INITIALIZE
    // =========================
    @FXML
    private void initialize() {

        // ----- TAB GENERAL -----
        colEspecialidad.setCellValueFactory(data ->
                javafx.beans.property.SimpleStringProperty.stringExpression(
                        javafx.beans.binding.Bindings.createStringBinding(() -> {
                            Especialidad esp =
                                    especialidadDAO.findById(data.getValue().getIdEspecialidad());
                            return esp != null ? esp.getNombre() : "";
                        })
                )
        );

        // =========================
// TAB EPISODIOS - columnas consultas
// =========================
        colMotivoConsulta.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getMotivoConsulta()
                )
        );

        colFechaConsulta.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getFechaHora() != null
                                ? data.getValue().getFechaHora().toLocalDate().toString()
                                : ""
                )
        );

// Número de consulta dentro del episodio (posición)
        colNumConsulta.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        tblConsultas.getItems().indexOf(data.getValue()) + 1
                ).asObject()
        );

// Episodio (texto)
        colEpisodioConsulta.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getMotivoConsulta() // o texto que prefieras
                )
        );

        colMotivo.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getMotivo()
                )
        );

        colEstado.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getEstado()
                )
        );

        colFechaInicio.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getFechaInicio() != null
                                ? data.getValue().getFechaInicio().toString()
                                : ""
                )
        );

        // Nº de consultas por episodio
        colNumConsultas.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        consultaDAO.findByEpisodio(
                                data.getValue().getIdEpisodio()
                        ).size()
                ).asObject()
        );

        // ----- TAB EPISODIOS -----
        cmbFiltroEpisodio.valueProperty().addListener((obs, oldVal, newVal) ->
                cargarConsultas(newVal)
        );
    }

    // =========================
    // ENTRADA DESDE PACIENTES
    // =========================
    public void setPaciente(Paciente paciente) {
        this.pacienteActual = paciente;
        cargarCabecera();
        cargarEpisodios();
    }

    // =========================
    // CABECERA
    // =========================
    private void cargarCabecera() {

        lblNombrePaciente.setText(
                pacienteActual.getApellidos() + ", " + pacienteActual.getNombre()
        );
        lblDni.setText(pacienteActual.getDni());
        lblFechaNacimiento.setText(
                pacienteActual.getFechaNacimiento() != null
                        ? pacienteActual.getFechaNacimiento().toString()
                        : ""
        );
        lblDireccion.setText(pacienteActual.getDireccion());
        lblEmail.setText(pacienteActual.getEmail());
        lblTelefono.setText(pacienteActual.getTelefono());
        lblAseguradora.setText(
                pacienteActual.getAseguradora() + " / " +
                        pacienteActual.getNumPoliza()
        );

        if (pacienteActual.getFotoPath() != null) {
            File f = new File(pacienteActual.getFotoPath());
            if (f.exists()) {
                imgFoto.setImage(
                        new javafx.scene.image.Image(f.toURI().toString())
                );
            }
        }
    }

    // =========================
    // CARGA EPISODIOS
    // =========================
    private void cargarEpisodios() {

        List<Episodio> episodios =
                episodioDAO.findByPaciente(pacienteActual.getIdPaciente());

        tblEpisodios.setItems(
                FXCollections.observableArrayList(episodios)
        );

        cmbFiltroEpisodio.setItems(
                FXCollections.observableArrayList(episodios)
        );

        if (!episodios.isEmpty()) {
            cmbFiltroEpisodio.getSelectionModel().selectFirst();
            cargarConsultas(cmbFiltroEpisodio.getSelectionModel().getSelectedItem());
        } else {
            tblConsultas.getItems().clear();
        }
    }

    // =========================
    // CARGA CONSULTAS
    // =========================
    private void cargarConsultas(Episodio episodio) {

        if (episodio == null) {
            tblConsultas.getItems().clear();
            return;
        }

        List<Consulta> consultas =
                consultaDAO.findByEpisodio(episodio.getIdEpisodio());

        tblConsultas.setItems(
                FXCollections.observableArrayList(consultas)
        );
    }

    // =========================
    // ACCIONES
    // =========================
    @FXML
    private void verConsultaCompleta() {

        Consulta seleccionada =
                tblConsultas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Debe seleccionar una consulta.")
                    .showAndWait();
            return;
        }

        SceneManager.loadScene(
                "/es/medicarte/view/consulta.fxml",
                "Consulta (solo lectura)",
                seleccionada
        );
    }


    @FXML
    private void imprimir() {
        new Alert(Alert.AlertType.INFORMATION,
                "La impresión se implementará en una fase posterior.")
                .showAndWait();
    }

    @FXML
    private void cancelar() {
        SceneManager.loadScene(
                "/es/medicarte/view/pacientes.fxml",
                "MedicArte - Pacientes"
        );
    }
}

