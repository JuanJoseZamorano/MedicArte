package es.medicarte.controller;

import es.medicarte.model.Paciente;
import es.medicarte.model.PacienteDAO;
import es.medicarte.util.FileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import es.medicarte.util.SceneManager;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Controlador de la vista de gestión de pacientes.
 * Esta clase se encarga de:
 * - Mostrar el listado de pacientes.
 * - Permitir la búsqueda.
 * - Gestionar el alta, edición y eliminación.
 * - Gestionar la fotografía del paciente.
 * - Navegar a citas e historial clínico.
 * Sigue el patrón MVC, delegando el acceso a datos en PacienteDAO.
 */
public class PacientesController {

    // ===================== COMPONENTES DE LA VISTA =====================

    // Lista visual donde se muestran los pacientes
    @FXML
    private ListView<Paciente> listPacientes;

    // --------------------- CAMPOS DE BÚSQUEDA ---------------------

    @FXML private TextField txtBuscarApellidos;
    @FXML private TextField txtBuscarNombre;
    @FXML private TextField txtBuscarDni;

    // --------------------- CAMPOS DEL FORMULARIO PRINCIPAL ---------------------

    @FXML private TextField txtApellidos;
    @FXML private TextField txtNombre;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private ComboBox<String> cmbSexo;
    @FXML private TextField txtDni;
    @FXML private TextField txtNhc;
    @FXML private TextField txtNuhsa;
    @FXML private TextField txtNuss;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtProvincia;
    @FXML private TextField txtCp;
    @FXML private TextField txtAseguradora;
    @FXML private TextField txtPoliza;

    // --------------------- DATOS CLÍNICOS ---------------------

    @FXML private TextArea txtAntPersonales;
    @FXML private TextArea txtAntFamiliares;
    @FXML private TextArea txtTratamiento;
    @FXML private TextArea txtAlergias;

    // --------------------- FOTO ---------------------

    @FXML private ImageView imgFoto;

    // --------------------- BOTONES ---------------------

    @FXML private Button btnLimpiar;
    @FXML private Button btnFoto;

    // ===================== VARIABLES DE CONTROL =====================

    // DAO para acceso a datos
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    // Lista observable que alimenta el ListView
    private final ObservableList<Paciente> pacientes = FXCollections.observableArrayList();

    // Paciente actualmente seleccionado en la lista
    private Paciente pacienteSeleccionado;

    // Ruta temporal de la foto seleccionada
    private String fotoSeleccionadaPath;

    /**
     * Estilo visual que se aplica cuando el formulario
     * entra en modo edición o alta.
     */
    private static final String ESTILO_EDITABLE =
            "-fx-background-color: rgba(206, 230, 244, 0.8);" +
                    "-fx-border-color: #c0c0c0;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 4;" +
                    "-fx-background-radius: 4;";

    /**
     * Método que se ejecuta automáticamente cuando se carga la vista.
     * Inicializa todos los componentes y deja el formulario en modo solo lectura.
     */
    @FXML
    private void initialize() {

        deshabilitarEdicion();      // Por defecto el formulario está bloqueado
        configurarListView();      // Configuramos cómo se muestran los pacientes
        configurarComboSexo();     // Cargamos valores del combo sexo
        cargarPacientes();         // Cargamos todos los pacientes desde BD
        configurarSeleccion();     // Configuramos listener de selección
        btnLimpiar.setVisible(false); // El botón limpiar solo se usa en modo alta
    }

    // =================== CONFIGURACIONES ===================

    /**
     * Define cómo se renderiza cada paciente dentro del ListView.
     * Se muestra Apellidos, Nombre y DNI.
     */
    private void configurarListView() {
        listPacientes.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Paciente p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null :
                        p.getApellidos() + ", " + p.getNombre() + " (" + p.getDni() + ")");
            }
        });
    }

    /**
     * Carga los valores posibles del campo Sexo.
     * Se hace manualmente ya que son valores cerrados.
     */
    private void configurarComboSexo() {
        cmbSexo.getItems().addAll("Hombre", "Mujer", "Otro");
    }

    /**
     * Listener que detecta cuándo se selecciona un paciente en la lista.
     * Al seleccionar uno, se cargan automáticamente sus datos en el formulario.
     */
    private void configurarSeleccion() {
        listPacientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        pacienteSeleccionado = newVal;
                        cargarPacienteEnFormulario(newVal);
                    }
                }
        );

        deshabilitarEdicion(); // Se mantiene bloqueado hasta entrar en modo edición
    }

    // =================== CARGA DE DATOS ===================

    /**
     * Carga todos los pacientes desde la base de datos
     * y los muestra en la lista.
     */
    private void cargarPacientes() {
        pacientes.setAll(pacienteDAO.findAll());
        listPacientes.setItems(pacientes);
        deshabilitarEdicion();
    }

    /**
     * Rellena todos los campos del formulario
     * con los datos del paciente seleccionado.
     */
    private void cargarPacienteEnFormulario(Paciente p) {

        // Guardamos la ruta de la foto
        fotoSeleccionadaPath = p.getFotoPath();

        // Cargamos imagen si existe
        if (fotoSeleccionadaPath != null && !fotoSeleccionadaPath.isBlank()) {
            File file = new File(fotoSeleccionadaPath);
            if (file.exists()) {
                imgFoto.setImage(new Image(file.toURI().toString()));
            } else {
                imgFoto.setImage(null);
            }
        } else {
            imgFoto.setImage(null);
        }

        // Carga de datos personales
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

        // Carga de datos clínicos
        txtAntPersonales.setText(p.getAntecedentesPersonales());
        txtAntFamiliares.setText(p.getAntecedentesFamiliares());
        txtTratamiento.setText(p.getTratamientoActual());
        txtAlergias.setText(p.getAlergias());

        // Ajustes visuales
        btnLimpiar.setVisible(false);
        quitarColorAlta();
        deshabilitarEdicion();
    }
    /**
     * Método principal de guardado.
     * Este método gestiona tanto el alta de un nuevo paciente
     * como la edición de uno existente.
     * Se diferencia el comportamiento según si existe o no
     * un paciente seleccionado en memoria.
     */
    @FXML
    private void guardarPaciente() {

        // Obtenemos los valores introducidos en el formulario
        String dniIntroducido = txtDni.getText();
        String nhcIntroducido = txtNhc.getText();

    /* =========================================================
       CASO INSERT (MODO ALTA)
       ========================================================= */
        if (pacienteSeleccionado == null) {

            // Validación de DNI duplicado
            if (pacienteDAO.existsByDni(dniIntroducido)) {
                new Alert(
                        Alert.AlertType.WARNING,
                        "Ya existe un paciente con ese DNI.\nNo se puede crear el paciente."
                ).showAndWait();
                return;
            }

            // Validación de NHC obligatorio
            if (nhcIntroducido == null || nhcIntroducido.isBlank()) {
                new Alert(
                        Alert.AlertType.WARNING,
                        "El NHC es obligatorio."
                ).showAndWait();
                return;
            }

            // Validación de NHC duplicado
            if (pacienteDAO.existsByNhc(nhcIntroducido)) {
                new Alert(
                        Alert.AlertType.WARNING,
                        "Ya existe un paciente con ese NHC."
                ).showAndWait();
                return;
            }

            // Creamos nuevo objeto paciente
            Paciente p = new Paciente();

            // Rellenamos el objeto con los datos del formulario
            rellenarPacienteDesdeFormulario(p);

            // Inicialmente la foto se pone a null (se gestionará después)
            p.setFotoPath(null);

            // Insertamos en base de datos
            boolean ok = pacienteDAO.insert(p);

            if (!ok) {
                new Alert(Alert.AlertType.ERROR,
                        "No se pudo guardar el paciente").showAndWait();
                return;
            }

            /*
             * Recargamos la lista para obtener el paciente ya insertado
             * con su ID generado por la base de datos.
             */
            cargarPacientes();

            Paciente nuevo = pacientes.stream()
                    .filter(pa -> pa.getDni().equals(p.getDni()))
                    .findFirst()
                    .orElse(null);

            /*
             * Si el paciente tiene foto seleccionada,
             * la copiamos al directorio interno y actualizamos la ruta.
             */
            if (nuevo != null && fotoSeleccionadaPath != null) {
                try {
                    File origen = new File(fotoSeleccionadaPath);
                    if (origen.exists()) {
                        String rutaInterna = FileUtils.copiarFotoPaciente(
                                origen,
                                nuevo.getIdPaciente()
                        );
                        nuevo.setFotoPath(rutaInterna);
                        pacienteDAO.update(nuevo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Seleccionamos automáticamente el nuevo paciente en la lista
            listPacientes.getSelectionModel().select(nuevo);
            pacienteSeleccionado = nuevo;

            // Volvemos al modo normal
            btnLimpiar.setVisible(false);
            quitarColorAlta();

            return;
        }

    /* =========================================================
       CASO UPDATE (MODO EDICIÓN)
       ========================================================= */

        String dniOriginal = pacienteSeleccionado.getDni();
        String nhcOriginal = pacienteSeleccionado.getNhc();
        String nhcNuevo = txtNhc.getText();

        // Validación: no permitir cambiar DNI por uno ya existente
        if (!dniIntroducido.equals(dniOriginal)
                && pacienteDAO.existsByDni(dniIntroducido)) {

            new Alert(
                    Alert.AlertType.WARNING,
                    "No se puede modificar el DNI.\nYa existe otro paciente con ese DNI."
            ).showAndWait();
            return;
        }

        // Validación: no permitir cambiar NHC por uno ya existente
        if (!nhcNuevo.equals(nhcOriginal)
                && pacienteDAO.existsByNhc(nhcNuevo)) {

            new Alert(
                    Alert.AlertType.WARNING,
                    "No se puede modificar el NHC.\nYa existe otro paciente con ese NHC."
            ).showAndWait();
            return;
        }

        // Actualizamos los datos básicos del paciente
        rellenarPacienteDesdeFormulario(pacienteSeleccionado);

        /*
         * Si se ha seleccionado una nueva foto,
         * se copia al directorio interno y se actualiza la ruta.
         */
        if (fotoSeleccionadaPath != null) {
            try {
                File origen = new File(fotoSeleccionadaPath);
                if (origen.exists()) {
                    String rutaInterna = FileUtils.copiarFotoPaciente(
                            origen,
                            pacienteSeleccionado.getIdPaciente()
                    );
                    pacienteSeleccionado.setFotoPath(rutaInterna);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Guardamos cambios en base de datos
        boolean ok = pacienteDAO.update(pacienteSeleccionado);

        if (ok) {
            cargarPacientes();
            listPacientes.getSelectionModel().select(pacienteSeleccionado);
            btnLimpiar.setVisible(false);
            quitarColorAlta();
        } else {
            new Alert(Alert.AlertType.ERROR,
                    "No se pudo actualizar el paciente").showAndWait();
        }

        // Volvemos a modo solo lectura
        deshabilitarEdicion();
    }
    /**
     * Copia los datos introducidos en el formulario
     * al objeto Paciente recibido por parámetro.
     * Este método centraliza la asignación de valores
     * para evitar duplicación de código en insert y update.
     */
    private void rellenarPacienteDesdeFormulario(Paciente p) {

        p.setFotoPath(fotoSeleccionadaPath);
        p.setApellidos(txtApellidos.getText());
        p.setNombre(txtNombre.getText());
        p.setFechaNacimiento(dpFechaNacimiento.getValue());
        p.setSexo(cmbSexo.getValue());
        p.setDni(txtDni.getText());
        p.setNhc(txtNhc.getText());
        p.setNuhsa(txtNuhsa.getText());
        p.setNuss(txtNuss.getText());
        p.setTelefono(txtTelefono.getText());
        p.setEmail(txtEmail.getText());
        p.setDireccion(txtDireccion.getText());
        p.setProvincia(txtProvincia.getText());
        p.setCp(txtCp.getText());
        p.setAseguradora(txtAseguradora.getText());
        p.setNumPoliza(txtPoliza.getText());

        p.setAntecedentesPersonales(txtAntPersonales.getText());
        p.setAntecedentesFamiliares(txtAntFamiliares.getText());
        p.setTratamientoActual(txtTratamiento.getText());
        p.setAlergias(txtAlergias.getText());
    }
    /**
     * Activa el modo alta.
     * Se limpia el formulario, se habilita la edición
     * y se aplica estilo visual diferenciador.
     */
    @FXML
    private void modoAlta() {

        pacienteSeleccionado = null;

        limpiarFormulario();

        btnLimpiar.setVisible(true);

        listPacientes.getSelectionModel().clearSelection();

        habilitarEdicion();
        cambiarColorAlta();
    }
    /**
     * Limpia todos los campos del formulario.
     * Se utiliza principalmente en modo alta o tras eliminar un paciente.
     * No elimina datos de la base de datos, solo limpia la interfaz.
     */
    @FXML
    private void limpiarFormulario() {

        // Campos personales
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

        // Campos clínicos
        txtAntPersonales.clear();
        txtAntFamiliares.clear();
        txtTratamiento.clear();
        txtAlergias.clear();

        // Imagen del paciente
        imgFoto.setImage(null);
        fotoSeleccionadaPath = null;
        imgFoto.setImage(null); // Se limpia por seguridad
    }

    /**
     * Realiza la búsqueda de pacientes en base a los criterios introducidos.
     * Se delega la consulta al DAO y se actualiza la lista visual.
     */
    @FXML
    private void buscarPacientes() {

        String apellidos = txtBuscarApellidos.getText();
        String nombre = txtBuscarNombre.getText();
        String dni = txtBuscarDni.getText();

        // Se actualiza la lista observable con el resultado de la búsqueda
        pacientes.setAll(
                pacienteDAO.buscar(apellidos, nombre, dni)
        );

        // Se limpia la selección actual
        listPacientes.getSelectionModel().clearSelection();
        pacienteSeleccionado = null;

        // Se deshabilita edición hasta nueva selección
        deshabilitarEdicion();
    }

    /**
     * Elimina el paciente seleccionado tras confirmación del usuario.
     * Se comprueba previamente que haya un paciente seleccionado.
     */
    @FXML
    private void eliminarPaciente() {

        if (pacienteSeleccionado == null) {
            new Alert(Alert.AlertType.WARNING,
                    "No hay paciente seleccionado").showAndWait();
            return;
        }

        // Confirmación antes de eliminar (buena práctica de seguridad)
        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Seguro que desea eliminar el paciente?",
                ButtonType.YES, ButtonType.NO
        );

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {

                boolean ok = pacienteDAO.delete(
                        pacienteSeleccionado.getIdPaciente()
                );

                if (ok) {
                    limpiarFormulario();
                    cargarPacientes();
                } else {
                    new Alert(Alert.AlertType.ERROR,
                            "No se pudo eliminar el paciente")
                            .showAndWait();
                }
            }
        });
    }

    /**
     * Vuelve a la vista anterior usando el sistema de navegación con pila.
     * Esto permite mantener coherencia en la navegación.
     */
    @FXML
    private void cancelar() {
        SceneManager.goBack();
    }

    /**
     * Permite seleccionar una imagen desde el sistema de archivos
     * para asignarla como foto del paciente.
     * Solo acepta imágenes PNG y JPG.
     */
    @FXML
    private void cambiarFoto() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto del paciente");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(
                listPacientes.getScene().getWindow()
        );

        if (file != null) {
            fotoSeleccionadaPath = file.getAbsolutePath();
            Image image = new Image(file.toURI().toString());
            imgFoto.setImage(image);
        }
    }

    /**
     * Abre la vista de citas filtrando por el paciente seleccionado.
     */
    @FXML
    private void verCitas() {

        if (pacienteSeleccionado == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Debe seleccionar un paciente para ver sus citas"
            ).showAndWait();
            return;
        }

        SceneManager.loadScene(
                "/es/medicarte/view/citas.fxml",
                "MedicArte - Citas",
                pacienteSeleccionado.getIdPaciente()
        );
    }

    /**
     * Abre el historial clínico del paciente seleccionado.
     */
    @FXML
    private void verHistorial() {

        if (pacienteSeleccionado == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Debe seleccionar un paciente."
            ).showAndWait();
            return;
        }

        SceneManager.loadScene(
                "/es/medicarte/view/historial.fxml",
                "MedicArte - Historial Clínico",
                pacienteSeleccionado
        );
    }

    /**
     * Abre la vista de nueva cita cargando automáticamente
     * el paciente actualmente seleccionado.
     */
    public void crearCita() {

        if (pacienteSeleccionado == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Debe seleccionar un paciente para crear una cita."
            ).showAndWait();
            return;
        }

        String dni = pacienteSeleccionado.getDni();

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
     * Permite cargar directamente un paciente en la vista
     * cuando se accede desde otra pantalla.
     */
    public void setPacienteInicial(int idPaciente) {

        Paciente p = pacienteDAO.findById(idPaciente);

        if (p != null) {
            cargarPacienteEnFormulario(p);
            listPacientes.getSelectionModel().select(p);
        }
    }

    /**
     * Aplica un estilo visual especial cuando estamos en modo alta,
     * para indicar al usuario que los campos están activos para edición.
     */
    private void cambiarColorAlta(){

        txtApellidos.setStyle(ESTILO_EDITABLE);
        txtNombre.setStyle(ESTILO_EDITABLE);
        dpFechaNacimiento.setStyle(ESTILO_EDITABLE);
        cmbSexo.setStyle(ESTILO_EDITABLE);
        txtDni.setStyle(ESTILO_EDITABLE);
        txtNhc.setStyle(ESTILO_EDITABLE);
        txtNuhsa.setStyle(ESTILO_EDITABLE);
        txtNuss.setStyle(ESTILO_EDITABLE);
        txtTelefono.setStyle(ESTILO_EDITABLE);
        txtEmail.setStyle(ESTILO_EDITABLE);
        txtDireccion.setStyle(ESTILO_EDITABLE);
        txtProvincia.setStyle(ESTILO_EDITABLE);
        txtCp.setStyle(ESTILO_EDITABLE);
        txtAseguradora.setStyle(ESTILO_EDITABLE);
        txtPoliza.setStyle(ESTILO_EDITABLE);
        btnFoto.setDisable(false);
        txtAntPersonales.setStyle(ESTILO_EDITABLE);
        txtAntFamiliares.setStyle(ESTILO_EDITABLE);
        txtTratamiento.setStyle(ESTILO_EDITABLE);
        txtAlergias.setStyle(ESTILO_EDITABLE);
    }

    /**
     * Elimina el estilo visual especial aplicado en modo alta.
     */
    private void quitarColorAlta(){

        txtApellidos.setStyle(null);
        txtNombre.setStyle(null);
        dpFechaNacimiento.setStyle(null);
        cmbSexo.setStyle(null);
        txtDni.setStyle(null);
        txtNhc.setStyle(null);
        txtNuhsa.setStyle(null);
        txtNuss.setStyle(null);
        txtTelefono.setStyle(null);
        txtEmail.setStyle(null);
        txtDireccion.setStyle(null);
        txtProvincia.setStyle(null);
        txtCp.setStyle(null);
        txtAseguradora.setStyle(null);
        txtPoliza.setStyle(null);
        txtAntPersonales.setStyle(null);
        txtAntFamiliares.setStyle(null);
        txtTratamiento.setStyle(null);
        txtAlergias.setStyle(null);
        btnFoto.setDisable(true);
    }

    /**
     * Bloquea todos los campos del formulario.
     * Se utiliza cuando no hay selección o tras guardar.
     */
    private void deshabilitarEdicion(){
        txtApellidos.setEditable(false);
        txtNombre.setEditable(false);
        dpFechaNacimiento.setEditable(false);
        cmbSexo.setEditable(false);
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
        btnFoto.setDisable(true);
        txtAntPersonales.setEditable(false);
        txtAntFamiliares.setEditable(false);
        txtAlergias.setEditable(false);
        txtTratamiento.setEditable(false);
    }

    /**
     * Activa todos los campos del formulario.
     * Se usa en modo alta o edición.
     */
    private void habilitarEdicion(){
        txtApellidos.setEditable(true);
        txtNombre.setEditable(true);
        dpFechaNacimiento.setEditable(true);
        cmbSexo.setEditable(true);
        txtDni.setEditable(true);
        txtNhc.setEditable(true);
        txtNuhsa.setEditable(true);
        txtNuss.setEditable(true);
        txtTelefono.setEditable(true);
        txtEmail.setEditable(true);
        txtDireccion.setEditable(true);
        txtProvincia.setEditable(true);
        txtCp.setEditable(true);
        txtAseguradora.setEditable(true);
        txtPoliza.setEditable(true);
        btnFoto.setDisable(false);
        txtAntPersonales.setEditable(true);
        txtAntFamiliares.setEditable(true);
        txtAlergias.setEditable(true);
        txtTratamiento.setEditable(true);
    }

    /**
     * Método auxiliar para forzar modo edición
     * desde otras partes del sistema.
     */
    public void setEditarPacientes() {
        habilitarEdicion();
        cambiarColorAlta();
    }
}
