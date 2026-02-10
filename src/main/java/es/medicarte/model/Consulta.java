package es.medicarte.model;

import java.time.LocalDateTime;

/**
 * Clase modelo que representa una consulta médica.
 * Una consulta pertenece a un episodio clínico y puede
 * estar asociada opcionalmente a una cita previa.
 */
public class Consulta {

    // Identificador único de la consulta
    private int idConsulta;

    // Episodio clínico al que pertenece la consulta
    private int idEpisodio;

    // Médico que realiza la consulta
    private int idMedico;

    // Cita asociada (puede ser null si no procede de cita)
    private Integer idCita;

    // Fecha y hora en la que se realiza la consulta
    private LocalDateTime fechaHora;

    // Datos clínicos de la consulta
    private String motivoConsulta;
    private String anamnesis;
    private String exploracion;
    private String diagnostico;
    private String diagnosticoCod;
    private String tratamiento;
    private String observaciones;

    // Estado de la consulta (BORRADOR / FINALIZADA)
    private String estado;

    // ===== GETTERS & SETTERS =====

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public int getIdEpisodio() {
        return idEpisodio;
    }

    public void setIdEpisodio(int idEpisodio) {
        this.idEpisodio = idEpisodio;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getAnamnesis() {
        return anamnesis;
    }

    public void setAnamnesis(String anamnesis) {
        this.anamnesis = anamnesis;
    }

    public String getExploracion() {
        return exploracion;
    }

    public void setExploracion(String exploracion) {
        this.exploracion = exploracion;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getDiagnosticoCod() {
        return diagnosticoCod;
    }

    public void setDiagnosticoCod(String diagnosticoCod) {
        this.diagnosticoCod = diagnosticoCod;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

