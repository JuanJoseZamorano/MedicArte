package es.medicarte.model;

/**
 * Clase modelo que representa un usuario del sistema.
 * Contiene la información necesaria para la autenticación
 * y la gestión de roles dentro de la aplicación.
 */
public class Usuario {

    // Identificador único del usuario en la base de datos
    private int idUsuario;

    // Nombre de usuario utilizado para iniciar sesión
    private String username;

    // Hash de la contraseña (nunca se almacena en texto plano)
    private String passwordHash;

    // Rol del usuario (ADMIN, MEDICO, etc.)
    private String rol;

    // Indica si el usuario está activo o no en el sistema
    private boolean activo;

    // Identificador del médico asociado al usuario (solo para rol MEDICO)
    // Puede ser null si el usuario no es médico
    private Integer idMedico;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Integer getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    /**
     * Se sobrescribe el método toString para mostrar correctamente
     * el usuario en controles gráficos como ComboBox.
     * Se utiliza, por ejemplo, en la administración de usuarios.
     */
    @Override
    public String toString() {
        return username + " (" + rol + ")";
    }
}
