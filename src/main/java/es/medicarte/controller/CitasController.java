package es.medicarte.controller;

import es.medicarte.util.SceneManager;
import javafx.fxml.FXML;
import es.medicarte.model.Paciente;
import es.medicarte.model.PacienteDAO;
import javafx.scene.control.*;
import es.medicarte.model.Cita;
import es.medicarte.model.CitaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import es.medicarte.model.Cita;
import es.medicarte.model.CitaDAO;
import es.medicarte.model.Paciente;
import es.medicarte.model.PacienteDAO;


public class CitasController {
    private Integer idPacienteFiltro = null;
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    @FXML
    private TextField txtBuscarDni;
    @FXML
    private TextField txtBuscarApellidos;
    @FXML
    private TextField txtBuscarNombre;
    @FXML private ListView<Cita> listCitas;

    private final CitaDAO citaDAO = new CitaDAO();
    private final ObservableList<Cita> citas = FXCollections.observableArrayList();


    @FXML private Label lblPaciente;
    @FXML private Label lblFechaHora;
    @FXML private Label lblObservaciones;
    @FXML private Label lblEstado;

    private Cita citaSeleccionada;

    @FXML
    private void initialize() {
        prepararVistaGeneral();
        configurarListViewCitas();
        configurarSeleccionCita();
        if (idPacienteFiltro == null) {
            cargarTodasLasCitas();
        }

    }


    private void cargarTodasLasCitas() {
        citas.setAll(citaDAO.findAll());
        listCitas.setItems(citas);
    }

    private void cargarCitasPorPaciente(int idPaciente) {
        citas.setAll(citaDAO.findByPaciente(idPaciente));
        listCitas.setItems(citas);
    }

    public void setIdPacienteFiltro(Integer idPaciente) {
        this.idPacienteFiltro = idPaciente;
        System.out.println("Filtro recibido: " + idPaciente);
        if (idPacienteFiltro != null) {
            // cargarCitasPorPaciente(idPacienteFiltro);
            prepararVistaFiltradaPorPaciente();
            // txtBuscarDni.setDisable(true); // opcional
            cargarCitasPorPaciente(idPacienteFiltro);
        } else {
            cargarTodasLasCitas();
        }
    }
    private void prepararVistaFiltradaPorPaciente() {

        // Buscamos el paciente
        Paciente p = pacienteDAO.findById(idPacienteFiltro);

        if (p != null) {
            // Rellenamos el DNI automáticamente
            txtBuscarDni.setText(p.getDni());

            // Opcional: bloquear el campo para que se vea claro el filtro
            txtBuscarDni.setDisable(true);
        }
    }
    private void prepararVistaGeneral() {
        txtBuscarDni.setDisable(false);
        txtBuscarDni.clear();
    }

    private void configurarListViewCitas() {

        listCitas.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cita c, boolean empty) {
                super.updateItem(c, empty);

                if (empty || c == null) {
                    setText(null);
                } else {
                    setText(
                            c.getFechaHora().toLocalDate() + " " +
                                    c.getFechaHora().toLocalTime().withSecond(0) +
                                    " - " +
                                    (c.getObservaciones() != null ? c.getObservaciones() : "Sin motivo") +
                                    " (" + c.getEstado() + ")"
                    );
                    if ("CANCELADA".equals(c.getEstado())) {
                        setStyle("-fx-text-fill: red;");
                    } else if ("COMPLETADA".equals(c.getEstado())) {
                        setStyle("-fx-text-fill: green;");
                    } else    {
                        setStyle("");
                    }
                }
            }
        });
    }

    private void configurarSeleccionCita() {

        listCitas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        citaSeleccionada = newVal;
                        mostrarDetalleCita(newVal);
                    }
                }
        );
    }

    private void mostrarDetalleCita(Cita c) {

        // Paciente (por ahora mostramos el ID)
        Paciente p = pacienteDAO.findById(c.getIdPaciente());

        if (p != null) {
            lblPaciente.setText(p.getApellidos() + ", " + p.getNombre());
        } else {
            lblPaciente.setText("Paciente desconocido");
        }

        // Fecha y hora
        lblFechaHora.setText(
                c.getFechaHora().toLocalDate() + " " +
                        c.getFechaHora().toLocalTime().withSecond(0)
        );

        // Observaciones
        lblObservaciones.setText(
                c.getObservaciones() != null ? c.getObservaciones() : "—"
        );

        // Estado
        lblEstado.setText(c.getEstado());
    }

    @FXML
    private void buscarCitas() {
        // Se implementará más adelante
    }

    @FXML
    private void filtrarPorFechas() {
        // Se implementará más adelante
    }

    @FXML
    private void cancelar() {
        // Volver al dashboard médico
    }

    @FXML
    private void editarCita() {
        // Se implementará más adelante
    }


    @FXML
    private void pasarConsulta() {
        if (citaSeleccionada == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Debe seleccionar una cita para pasar a consulta."
            ).showAndWait();
            return;
        }

        if (!"PENDIENTE".equals(citaSeleccionada.getEstado())) {
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Solo se pueden pasar a consulta citas en estado PENDIENTE."
            ).showAndWait();
            return;
        }

        SceneManager.loadScene(
                "/es/medicarte/view/consulta.fxml",
                "MedicArte - Consulta",
                citaSeleccionada
        );
    }

    @FXML
    private void abrirHistorial() {
        // Se implementará más adelante
    }

    @FXML
    private void verPaciente() {
        // Se implementará más adelante
    }

    @FXML
    private void nuevaCita() {
        SceneManager.loadScene(
                "/es/medicarte/view/nueva_cita.fxml",
                "MedicArte - Nueva cita"
        );
    }

    @FXML
    private void cancelarCita() {

        if (citaSeleccionada == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Debe seleccionar una cita para cancelarla."
            ).showAndWait();
            return;
        }

        if ("CANCELADA".equals(citaSeleccionada.getEstado())) {
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "La cita ya está cancelada."
            ).showAndWait();
            return;
        }

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Está seguro de que desea cancelar la cita?",
                ButtonType.YES,
                ButtonType.NO
        );

        confirmacion.showAndWait();

        if (confirmacion.getResult() != ButtonType.YES) {
            return;
        }

        CitaDAO dao = new CitaDAO();
        boolean ok = dao.cancelarCita(citaSeleccionada.getIdCita());

        if (!ok) {
            new Alert(
                    Alert.AlertType.ERROR,
                    "No se pudo cancelar la cita."
            ).showAndWait();
            return;
        }

        // Actualizamos estado local
        citaSeleccionada.setEstado("CANCELADA");

        // Refrescamos la agenda
        if (idPacienteFiltro != null) {
            cargarCitasPorPaciente(idPacienteFiltro);
        } else {
            cargarTodasLasCitas();
        }

        new Alert(
                Alert.AlertType.INFORMATION,
                "La cita ha sido cancelada correctamente."
        ).showAndWait();
    }


    @FXML
    private void volver() {
        SceneManager.loadScene(
                "/es/medicarte/view/medico_dashboard.fxml",
                "MedicArte - Nueva cita"
        );
    }

}
