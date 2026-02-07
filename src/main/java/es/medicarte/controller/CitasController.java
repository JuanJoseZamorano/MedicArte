package es.medicarte.controller;

import es.medicarte.util.ItemListaCitas;
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

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;


public class CitasController {
    private Integer idPacienteFiltro = null;
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    @FXML
    private TextField txtBuscarDni;

//    @FXML
//    private TextField txtBuscarApellidos;
//    @FXML
//    private TextField txtBuscarNombre;

    @FXML
    private DatePicker dpFecha;

    @FXML private ListView<ItemListaCitas> listCitas;

    private final CitaDAO citaDAO = new CitaDAO();
    private final ObservableList<ItemListaCitas> citas = FXCollections.observableArrayList();


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
        List<Cita> lista = citaDAO.findAll();
        cargarCitasConCabeceras(lista);
    }

    private void cargarCitasPorPaciente(int idPaciente) {
        List<Cita> lista = citaDAO.findByPaciente(idPaciente);
        cargarCitasConCabeceras(lista);
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
    @FXML
    private void buscarPaciente() {
    }
    private void prepararVistaGeneral() {
        txtBuscarDni.setDisable(false);
        txtBuscarDni.clear();
    }


    private void configurarListViewCitas() {

        listCitas.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ItemListaCitas item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                if (item.isCabecera()) {
                    setText(item.getTexto());
                    setStyle("""
                    -fx-font-weight: bold;
                    -fx-text-fill: #555;
                    -fx-padding: 10 0 5 10;
                """);
                } else {
                    Cita c = item.getCita();

                    setText("   " +
                            c.getFechaHora().toLocalDate() + " " +
                            c.getFechaHora().toLocalTime().withSecond(0) +
                            " - " +
                            (c.getObservaciones() != null ? c.getObservaciones() : "Sin motivo")+ " (" + c.getEstado() + ")"
                    );

                    switch (c.getEstado()) {
                        case "COMPLETADA" -> setStyle("-fx-text-fill: green;");
                        case "CANCELADA"  -> setStyle("-fx-text-fill: red;");
                        case "PENDIENTE"  -> setStyle("-fx-text-fill: blue;");
                        default           -> setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });
    }


    private void configurarSeleccionCita() {

        listCitas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null && !newVal.isCabecera()) {
                        citaSeleccionada = newVal.getCita();
                        mostrarDetalleCita(citaSeleccionada);
                    }
                }
        );
    }

    private void mostrarDetalleCita(Cita c) {

        // Paciente (por ahora mostramos el ID)
        Paciente p = pacienteDAO.findById(c.getIdPaciente());

        if (p != null) {
            lblPaciente.setText(p.getApellidos() + ", " + p.getNombre());
            txtBuscarDni.setText(p.getDni());
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


        String dni = txtBuscarDni.getText();

        if (dni == null || dni.isBlank()) {
//            new Alert(
//                    Alert.AlertType.WARNING,
//                    "Introduzca un DNI para buscar sus citas."
//            ).showAndWait();
            cargarTodasLasCitas();
            return;
        }

        Paciente p = pacienteDAO.findByDni(dni);

        if (p == null) {
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "No se ha encontrado ningún paciente con ese DNI."
            ).showAndWait();

            return;
        }
        cargarCitasPorPaciente(p.getIdPaciente());
    }

    @FXML
    private void filtrarPorFechas() {
        LocalDate fecha = dpFecha.getValue();

        if (fecha == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Debe seleccionar una fecha."
            ).showAndWait();
            return;
        }

        List<Cita> lista = citaDAO.findByFecha(fecha);

        if (lista.isEmpty()) {
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "No hay citas para la fecha seleccionada."
            ).showAndWait();
        }

        // Usas el mismo método que ya agrupa por meses
        cargarCitasConCabeceras(lista);
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
        listCitas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null && !newVal.isCabecera()) {
                        citaSeleccionada = newVal.getCita();

                    }
                }
        );
        Paciente p = pacienteDAO.findById(citaSeleccionada.getIdPaciente());
        if (p == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Debe seleccionar un paciente."
            ).showAndWait();
            return;
        }

        SceneManager.loadScene(
                "/es/medicarte/view/historial.fxml",
                "MedicArte - Historial Clínico",
                p
        );
    }

    @FXML
    private void verPaciente() {

        if (citaSeleccionada == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Debe seleccionar una cita para ver el paciente."
            ).showAndWait();
            return;
        }

        int idPaciente = citaSeleccionada.getIdPaciente();

        SceneManager.loadScene(
                "/es/medicarte/view/pacientes2.fxml",
                "MedicArte - Pacientes",
                controller -> {
                    if (controller instanceof PacientesController pc) {
                        pc.setPacienteInicial(idPaciente);
                    }
                }
        );
    }

    @FXML
    private void nuevaCita() {

        String dni = txtBuscarDni.getText();

        SceneManager.loadScene(
                "/es/medicarte/view/nueva_cita.fxml",
                "MedicArte - Nueva cita",
                controller -> {
                    if (controller instanceof NuevaCitaController nc) {
                        nc.setDniInicial(dni);
                    }
                }
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

        if ("COMPLETADA".equals(citaSeleccionada.getEstado())) {
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "La cita ya se ha celebrado. No puede cancelarla."
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


//    @FXML
//    private void volver() {
//        SceneManager.loadScene(
//                "/es/medicarte/view/medico_dashboard.fxml",
//                "MedicArte - Nueva cita"
//        );
//    }

    @FXML
    private void volver() {
        SceneManager.goBack();
    }

    private void cargarCitasConCabeceras(List<Cita> lista) {

        citas.clear();

        YearMonth mesActual = null;

        for (Cita c : lista) {

            YearMonth mesCita = YearMonth.from(c.getFechaHora().toLocalDate());

            if (!mesCita.equals(mesActual)) {
                String tituloMes =
                        mesCita.getMonth()
                                .getDisplayName(TextStyle.FULL, new Locale("es"))
                                .toUpperCase()
                                + " " + mesCita.getYear();

                citas.add(new ItemListaCitas(tituloMes));
                mesActual = mesCita;
            }

            citas.add(new ItemListaCitas(c));
        }

        listCitas.setItems(citas);
    }


}
