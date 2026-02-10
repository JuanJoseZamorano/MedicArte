package es.medicarte.model;

/**
 * Clase modelo que representa una especialidad médica.
 * Se utiliza para clasificar médicos, episodios y consultas.
 */
public class Especialidad {

    // Identificador único de la especialidad
    private int idEspecialidad;

    // Nombre descriptivo de la especialidad
    private String nombre;

    public int getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Se sobrescribe toString para mostrar correctamente
     * el nombre de la especialidad en controles gráficos
     * como ComboBox o ListView.
     */
    @Override
    public String toString() {
        return nombre;
    }
}
