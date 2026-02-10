package es.medicarte.model;

import java.time.LocalDate;

/**
 * Clase que representa un episodio clínico.
 * Un episodio agrupa una o varias consultas dentro
 * de una misma historia clínica y especialidad.
 */
public class Episodio {

    // Identificador único del episodio
    private int idEpisodio;

    // Identificador de la historia clínica a la que pertenece
    private int idHistoria;

    // Identificador de la especialidad asociada al episodio
    private int idEspecialidad;

    // Motivo o descripción del episodio clínico
    private String motivo;

    // Estado del episodio (ABIERTO / CERRADO)
    private String estado;

    // Fecha de inicio del episodio
    private LocalDate fechaInicio;

    // ===== GETTERS & SETTERS =====

    public int getIdEpisodio() {
        return idEpisodio;
    }

    public void setIdEpisodio(int idEpisodio) {
        this.idEpisodio = idEpisodio;
    }

    public int getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(int idHistoria) {
        this.idHistoria = idHistoria;
    }

    public int getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Se sobrescribe toString para mostrar correctamente
     * el episodio en ComboBox.
     */
    @Override
    public String toString() {
        return motivo + " (" + estado + ")";
    }
}
