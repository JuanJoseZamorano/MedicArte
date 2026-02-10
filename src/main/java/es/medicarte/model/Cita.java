package es.medicarte.model;

import java.time.LocalDateTime;

/**
 * Clase modelo que representa una cita médica.
 * Una cita puede derivar posteriormente en una consulta.
 */
public class Cita {

    // Identificador único de la cita
    private int idCita;

    // Paciente al que pertenece la cita
    private int idPaciente;

    // Médico asignado a la cita
    private int idMedico;

    // Fecha y hora programada para la cita
    private LocalDateTime fechaHora;

    // Estado de la cita (PENDIENTE / CANCELADA / COMPLETADA)
    private String estado;

    // Origen de la cita (CLINICA / APP)
    private String origen;

    // Observaciones asociadas a la cita
    private String observaciones;

    // Duración estimada de la cita en minutos
    private Integer duracionMin;

    public Cita() {
    }

    /**
     * Constructor utilizado al crear una nueva cita desde la aplicación.
     * Inicializa los valores por defecto.
     */
    public Cita(int idPaciente, int idMedico, LocalDateTime fechaHora,
                String observaciones, Integer duracionMin) {

        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaHora = fechaHora;
        this.observaciones = observaciones;
        this.duracionMin = duracionMin;
        this.estado = "PENDIENTE";
        this.origen = "CLINICA";
    }

    // ===== GETTERS & SETTERS =====

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(Integer duracionMin) {
        this.duracionMin = duracionMin;
    }
}
