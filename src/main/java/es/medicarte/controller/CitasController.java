package es.medicarte.controller;

// Clase auxiliar que utilizamos para poder mezclar cabeceras de mes
// con citas dentro del mismo ListView
import es.medicarte.util.ItemListaCitas;

// Clase encargada de gestionar los cambios de escena en la aplicación
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

/**
 * Controlador de la vista de Citas.
 *
 * Esta clase se encarga de:
 * - Mostrar todas las citas o las de un paciente concreto.
 * - Agrupar las citas por meses en el listado.
 * - Permitir buscar por DNI.
 * - Mostrar el detalle de la cita seleccionada.
 * - Gestionar las acciones sobre las citas (editar, cancelar, etc.).
 */
public class CitasController {

    // Si se entra desde Pacientes, se guarda aquí el id del paciente
    private Integer idPacienteFiltro = null;

    // DAO para acceder a los datos de pacientes
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    // Campo de búsqueda por DNI
    @FXML
    private TextField txtBuscarDni;

    // DatePicker para filtrar por fecha concreta
    @FXML
    private DatePicker dpFecha;

    // ListView que muestra citas y cabeceras de mes
    @FXML
    private ListView<ItemListaCitas> listCitas;

    // DAO para acceder a los datos de citas
    private final CitaDAO citaDAO = new CitaDAO();

    // Lista observable que alimenta el ListView
    private final ObservableList<ItemListaCitas> citas = FXCollections.observableArrayList();

    // Labels de detalle de la cita seleccionada
    @FXML private Label lblPaciente;
    @FXML private Label lblFechaHora;
    @FXML private Label lblObservaciones;
    @FXML private Label lblEstado;

    // Cita actualmente seleccionada en la lista
    private Cita citaSeleccionada;

    /**
     * Método initialize() que se ejecuta automáticamente
     * cuando se carga el FXML.
     */
    @FXML
    private void initialize() {

        // Preparar vista en modo general (sin filtro)
        prepararVistaGeneral();

        // Configurar cómo se muestran los elementos del ListView
        configurarListViewCitas();

        // Configurar comportamiento al seleccionar una cita
        configurarSeleccionCita();

        // Si no hay filtro por paciente, cargar todas las citas
        if (idPacienteFiltro == null) {
            cargarTodasLasCitas();
        }
    }

    /**
     * Carga todas las citas de la base de datos.
     * Después las agrupa por meses.
     */
    private void cargarTodasLasCitas() {
        List<Cita> lista = citaDAO.findAll();
        cargarCitasConCabeceras(lista);
    }

    /**
     * Carga solo las citas de un paciente concreto.
     */
    private void cargarCitasPorPaciente(int idPaciente) {
        List<Cita> lista = citaDAO.findByPaciente(idPaciente);
        cargarCitasConCabeceras(lista);
    }

    /**
     * Método que se llama desde SceneManager cuando
     * se entra en esta vista con filtro de paciente.
     */
    public void setIdPacienteFiltro(Integer idPaciente) {
        this.idPacienteFiltro = idPaciente;

        if (idPacienteFiltro != null) {
            prepararVistaFiltradaPorPaciente();
            cargarCitasPorPaciente(idPacienteFiltro);
        } else {
            cargarTodasLasCitas();
        }
    }

    /**
     * Ajusta la vista cuando se ha entrado desde Pacientes.
     * Rellena el DNI automáticamente y bloquea el campo.
     */
    private void prepararVistaFiltradaPorPaciente() {

        // Buscamos el paciente en la base de datos
        Paciente p = pacienteDAO.findById(idPacienteFiltro);

        if (p != null) {
            // Rellenamos el DNI automáticamente
            txtBuscarDni.setText(p.getDni());

            // Bloqueamos el campo para evitar modificar el filtro
            txtBuscarDni.setDisable(true);
        }
    }

    @FXML
    private void buscarPaciente() {
        // Método reservado (actualmente no implementado)
    }

    /**
     * Prepara la vista en modo general (sin filtro por paciente).
     */
    private void prepararVistaGeneral() {
        txtBuscarDni.setDisable(false);
        txtBuscarDni.clear();
    }

    /**
     * Configura el ListView para que:
     * - Muestre cabeceras de mes en negrita.
     * - Muestre citas con colores según estado.
     */
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

                // Si es cabecera (mes)
                if (item.isCabecera()) {
                    setText(item.getTexto());
                    setStyle("""
                    -fx-font-weight: bold;
                    -fx-text-fill: #555;
                    -fx-padding: 10 0 5 10;
                """);
                }
                // Si es una cita normal
                else {
                    Cita c = item.getCita();

                    setText("   " +
                            c.getFechaHora().toLocalDate() + " " +
                            c.getFechaHora().toLocalTime().withSecond(0) +
                            " - " +
                            (c.getObservaciones() != null
                                    ? c.getObservaciones()
                                    : "Sin motivo") +
                            " (" + c.getEstado() + ")"
                    );

                    // Color según estado
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

    /**
     * Configura el comportamiento cuando el usuario
     * selecciona una cita en la lista.
     */
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

    /**
     * Muestra en la parte derecha el detalle de la cita seleccionada.
     */
    private void mostrarDetalleCita(Cita c) {

        // Obtenemos el paciente asociado a la cita
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
                c.getObservaciones() != null
                        ? c.getObservaciones()
                        : "—"
        );

        // Estado
        lblEstado.setText(c.getEstado());
    }

    /**
     * Búsqueda de citas por DNI del paciente.
     */
    @FXML
    private void buscarCitas() {

        String dni = txtBuscarDni.getText();

        // Si el campo está vacío, mostramos todas
        if (dni == null || dni.isBlank()) {
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

        // Si el paciente existe, cargamos solo sus citas
        cargarCitasPorPaciente(p.getIdPaciente());
    }

    /**
     * Filtra las citas por la fecha seleccionada en el DatePicker.
     * Si no se selecciona fecha, se muestra un aviso.
     * Si no hay citas ese día, se informa al usuario.
     */
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

        // Obtenemos las citas para la fecha seleccionada
        List<Cita> lista = citaDAO.findByFecha(fecha);

        if (lista.isEmpty()) {
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "No hay citas para la fecha seleccionada."
            ).showAndWait();
        }

        // Reutilizamos el método que agrupa por meses
        cargarCitasConCabeceras(lista);
    }

    /**
     * Método reservado para navegación (actualmente no implementado).
     */
    @FXML
    private void cancelar() {
        // Volver al dashboard médico
    }

    /**
     * Permite editar una cita siempre que esté en estado PENDIENTE.
     * Se reutiliza la vista de NuevaCitaController para modificar los datos.
     */
    @FXML
    private void editarCita() {

        if (citaSeleccionada == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Debe seleccionar una cita para editarla."
            ).showAndWait();
            return;
        }

        // Solo permitimos editar citas pendientes
        if (!"PENDIENTE".equals(citaSeleccionada.getEstado())) {
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Solo se pueden editar citas pendientes."
            ).showAndWait();
            return;
        }

        // Cargamos la vista de nueva cita en modo edición
        SceneManager.loadScene(
                "/es/medicarte/view/nueva_cita.fxml",
                "MedicArte - Editar cita",
                controller -> {
                    if (controller instanceof NuevaCitaController nc) {
                        nc.setCitaParaEdicion(citaSeleccionada);
                    }
                }
        );
    }

    /**
     * Permite pasar una cita pendiente a consulta.
     * Solo se puede hacer si el estado es PENDIENTE.
     */
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

        // Se abre la vista de consulta pasando la cita seleccionada
        SceneManager.loadScene(
                "/es/medicarte/view/consulta.fxml",
                "MedicArte - Consulta",
                citaSeleccionada
        );
    }

    /**
     * Abre el historial clínico del paciente asociado a la cita seleccionada.
     */
    @FXML
    private void abrirHistorial() {

        // Recuperamos el paciente de la cita
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

    /**
     * Abre directamente la ficha del paciente asociado a la cita seleccionada.
     */
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

    /**
     * Abre la vista de nueva cita.
     * Si el DNI está relleno, se pasa automáticamente para precargar el paciente.
     */
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

    /**
     * Cancela una cita siempre que no esté ya cancelada ni completada.
     */
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

        // Actualizamos el estado en memoria
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

    /**
     * Vuelve a la vista anterior utilizando la pila de navegación del SceneManager.
     */
    @FXML
    private void volver() {
        SceneManager.goBack();
    }

    /**
     * Agrupa las citas por meses y añade cabeceras visuales
     * para mejorar la lectura del listado.
     */
    private void cargarCitasConCabeceras(List<Cita> lista) {

        citas.clear();

        YearMonth mesActual = null;

        for (Cita c : lista) {

            YearMonth mesCita = YearMonth.from(c.getFechaHora().toLocalDate());

            // Si cambia el mes, añadimos una cabecera
            if (!mesCita.equals(mesActual)) {

                String tituloMes =
                        mesCita.getMonth()
                                .getDisplayName(TextStyle.FULL, new Locale("es"))
                                .toUpperCase()
                                + " " + mesCita.getYear();

                citas.add(new ItemListaCitas(tituloMes));
                mesActual = mesCita;
            }

            // Añadimos la cita debajo de su mes correspondiente
            citas.add(new ItemListaCitas(c));
        }

        listCitas.setItems(citas);
    }
}

