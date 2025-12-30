package es.medicarte.controller;

import javafx.fxml.FXML;
import es.medicarte.model.Paciente;
import es.medicarte.model.PacienteDAO;
import javafx.scene.control.TextField;

public class CitasController {
    private Integer idPacienteFiltro = null;

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    @FXML
    private TextField txtBuscarDni;
    @FXML
    private TextField txtBuscarApellidos;
    @FXML
    private TextField txtBuscarNombre;
    @FXML
    private void initialize() {
            prepararVistaGeneral();
    }

    private void cargarTodasLasCitas() {
        // Usará CitaDAO más adelante
    }

    private void cargarCitasPorPaciente(int idPaciente) {
        // Usará CitaDAO.findByPaciente(idPaciente)
    }

    public void setIdPacienteFiltro(Integer idPaciente) {
        this.idPacienteFiltro = idPaciente;
        System.out.println("Filtro recibido: " + idPaciente);
        if (idPacienteFiltro != null) {
            // cargarCitasPorPaciente(idPacienteFiltro);
            prepararVistaFiltradaPorPaciente();
            // txtBuscarDni.setDisable(true); // opcional
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
        // Abrirá la vista de nueva cita
    }
}
