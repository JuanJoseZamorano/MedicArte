package es.medicarte.controller;

import es.medicarte.model.*;
import es.medicarte.service.InformeConsultaService;
import es.medicarte.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.List;

/**
 * Controlador de la vista de Historial Clínico.
 * Esta clase se encarga de:
 * - Mostrar los datos generales del paciente.
 * - Mostrar los episodios clínicos asociados al paciente.
 * - Mostrar las consultas de cada episodio.
 * - Permitir visualizar una consulta completa en modo lectura.
 * - Generar el informe PDF de una consulta.
 * Esta vista se abre siempre desde la pantalla de Pacientes.
 */
public class HistorialController {

    // =========================
    // CABECERA PACIENTE
    // =========================
    // Labels que muestran los datos básicos del paciente
    @FXML private Label lblNombrePaciente;
    @FXML private Label lblDni;
    @FXML private Label lblFechaNacimiento;
    @FXML private Label lblDireccion;
    @FXML private Label lblEmail;
    @FXML private Label lblTelefono;
    @FXML private Label lblAseguradora;

    // Imagen de la foto del paciente
    @FXML private ImageView imgFoto;

    // =========================
    // TAB GENERAL (EPISODIOS)
    // =========================
    // Tabla que muestra los episodios clínicos del paciente
    @FXML private TableView<Episodio> tblEpisodios;

    // Columnas de la tabla de episodios
    @FXML private TableColumn<Episodio, String> colEspecialidad;
    @FXML private TableColumn<Episodio, String> colMotivo;
    @FXML private TableColumn<Episodio, Integer> colNumConsultas;
    @FXML private TableColumn<Episodio, String> colFechaInicio;
    @FXML private TableColumn<Episodio, String> colEstado;

    // =========================
    // TAB EPISODIOS (CONSULTAS)
    // =========================
    // Combo para filtrar consultas por episodio
    @FXML private ComboBox<Episodio> cmbFiltroEpisodio;

    // Tabla de consultas del episodio seleccionado
    @FXML private TableView<Consulta> tblConsultas;

    // Columnas de la tabla de consultas
    @FXML private TableColumn<Consulta, String> colEpisodioConsulta;
    @FXML private TableColumn<Consulta, Integer> colNumConsulta;
    @FXML private TableColumn<Consulta, String> colMotivoConsulta;
    @FXML private TableColumn<Consulta, String> colFechaConsulta;

    // =========================
    // ESTADO INTERNO
    // =========================
    // Paciente actualmente cargado en el historial
    private Paciente pacienteActual;

    // =========================
    // DAOs
    // =========================
    // Acceso a base de datos para episodios, consultas y especialidades
    private final EpisodioDAO episodioDAO = new EpisodioDAO();
    private final ConsultaDAO consultaDAO = new ConsultaDAO();
    private final EspecialidadDAO especialidadDAO = new EspecialidadDAO();

    // =========================
    // INITIALIZE
    // =========================
    /**
     * Método que se ejecuta automáticamente al cargar la vista.
     * Aquí se configuran las columnas de las tablas y los listeners.
     */
    @FXML
    private void initialize() {

        // ----- TAB GENERAL -----
        // Columna especialidad: se obtiene el nombre desde la tabla especialidad
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

        // Motivo de consulta
        colMotivoConsulta.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getMotivoConsulta()
                )
        );

        // Fecha de la consulta
        colFechaConsulta.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getFechaHora() != null
                                ? data.getValue().getFechaHora().toLocalDate().toString()
                                : ""
                )
        );

        // Número de consulta dentro del episodio (posición en la lista)
        colNumConsulta.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        tblConsultas.getItems().indexOf(data.getValue()) + 1
                ).asObject()
        );

        // Columna episodio en la tabla de consultas
        colEpisodioConsulta.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getMotivoConsulta()
                )
        );

        // Motivo del episodio
        colMotivo.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getMotivo()
                )
        );

        // Estado del episodio (ABIERTO / CERRADO)
        colEstado.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getEstado()
                )
        );

        // Fecha de inicio del episodio
        colFechaInicio.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getFechaInicio() != null
                                ? data.getValue().getFechaInicio().toString()
                                : ""
                )
        );

        // Número de consultas asociadas a cada episodio
        colNumConsultas.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        consultaDAO.findByEpisodio(
                                data.getValue().getIdEpisodio()
                        ).size()
                ).asObject()
        );

        // Listener del combo para filtrar consultas por episodio
        cmbFiltroEpisodio.valueProperty().addListener((obs, oldVal, newVal) ->
                cargarConsultas(newVal)
        );
    }

    // =========================
    // ENTRADA DESDE PACIENTES
    // =========================
    /**
     * Método que recibe el paciente desde la vista anterior.
     */
    public void setPaciente(Paciente paciente) {
        this.pacienteActual = paciente;
        cargarCabecera();
        cargarEpisodios();
    }

    // =========================
    // CABECERA
    // =========================
    /**
     * Carga los datos generales del paciente en la cabecera.
     */
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

        // Carga de foto si existe
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
    /**
     * Carga todos los episodios del paciente y los muestra en la tabla.
     */
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
    /**
     * Carga las consultas del episodio seleccionado.
     */
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

    /**
     * Abre la consulta seleccionada en modo solo lectura.
     */
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

    /**
     * Vuelve a la pantalla anterior usando la pila de navegación.
     */
    @FXML
    private void cancelar() {
        SceneManager.goBack();
    }

    /**
     * Genera el informe PDF de la consulta seleccionada.
     */
    @FXML
    private void imprimir() {

        Consulta seleccionada =
                tblConsultas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Debe seleccionar una consulta.")
                    .showAndWait();
            return;
        }

        InformeConsultaService.generarPdfConsulta(seleccionada);
    }
}
