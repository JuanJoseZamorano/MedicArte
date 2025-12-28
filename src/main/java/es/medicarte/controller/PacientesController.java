package es.medicarte.controller;

import es.medicarte.model.Paciente;
import es.medicarte.model.PacienteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class PacientesController {

    @FXML
    private ListView<Paciente> listPacientes;

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final ObservableList<Paciente> pacientes = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configurarListView();
        cargarPacientes();
    }

    /**
     * Carga los pacientes desde la base de datos
     */
    private void cargarPacientes() {
        pacientes.clear();
        pacientes.addAll(pacienteDAO.findAll());
        listPacientes.setItems(pacientes);
    }

    /**
     * Configura cómo se muestra cada paciente en la lista
     */
    private void configurarListView() {
        listPacientes.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Paciente p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setText(null);
                } else {
                    setText(p.getApellidos() + ", " + p.getNombre()
                            + " (" + p.getDni() + ")");
                }
            }
        });
    }
}
