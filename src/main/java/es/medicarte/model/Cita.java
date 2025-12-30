package es.medicarte.model;

import java.time.LocalDateTime;

public class Cita {

    private int idCita;
    private int idPaciente;
    private int idMedico;
    private LocalDateTime fechaHora;
    private String estado;
    private String origen;
    private String observaciones;
    private Integer duracionMin;

    public Cita() {
    }

    // Constructor para crear cita
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

