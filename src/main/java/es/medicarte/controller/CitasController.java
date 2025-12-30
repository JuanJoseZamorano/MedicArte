package es.medicarte.controller;

import es.medicarte.util.SceneManager;
import javafx.fxml.FXML;
import es.medicarte.model.Paciente;
import es.medicarte.model.PacienteDAO;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import es.medicarte.model.Cita;
import es.medicarte.model.CitaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
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
    private void cancelarCita() {
        // Se implementará más adelante
    }

    @FXML
    private void pasarConsulta() {
        // Se implementará más adelante
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
    private void volver() {
        SceneManager.loadScene(
                "/es/medicarte/view/medico_dashboard.fxml",
                "MedicArte - Nueva cita"
        );
    }

}
