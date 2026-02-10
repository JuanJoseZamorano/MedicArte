package es.medicarte.model;

/**
 * Clase modelo que representa a un médico del sistema.
 * Contiene la información profesional básica necesaria
 * para la gestión de citas y consultas.
 */
public class Medico {

    // Identificador único del médico en la base de datos
    private int idMedico;

    // Nombre y apellidos completos del médico
    private String nombreApellidos;

    // Número de colegiado profesional
    private String numColegiado;

    // Identificador de la especialidad asociada al médico
    private int idEspecialidad;

    // Indica si el médico está activo en el sistema
    private boolean activo;

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getNombreApellidos() {
        return nombreApellidos;
    }

    public void setNombreApellidos(String nombreApellidos) {
        this.nombreApellidos = nombreApellidos;
    }

    public String getNumColegiado() {
        return numColegiado;
    }

    public void setNumColegiado(String numColegiado) {
        this.numColegiado = numColegiado;
    }

    public int getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
