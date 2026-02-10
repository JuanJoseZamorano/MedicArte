package es.medicarte.model;

import java.time.LocalDate;

/**
 * Clase modelo que representa la historia clínica de un paciente.
 * Cada paciente dispone de una única historia clínica, sobre la cual
 * se agrupan los distintos episodios y consultas.
 */
public class HistoriaClinica {

    // Identificador único de la historia clínica
    private int idHistoria;

    // Identificador del paciente al que pertenece la historia
    private int idPaciente;

    // Fecha de apertura de la historia clínica
    private LocalDate fechaApertura;

    // Estado de la historia (ACTIVA / CERRADA)
    private String estado;

    // Notas generales asociadas a la historia clínica
    private String notas;

    public int getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(int idHistoria) {
        this.idHistoria = idHistoria;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}


