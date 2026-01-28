package es.medicarte.model;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Episodio {

    private int idEpisodio;
    private int idHistoria;
    private int idEspecialidad;
    private String motivo;
    private String estado;
    private LocalDate fechaInicio;

    // ===== GETTERS & SETTERS =====

    public int getIdEpisodio() {
        return idEpisodio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
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

    @Override
    public String toString() {
        return motivo + " (" + estado + ")";
    }
}
