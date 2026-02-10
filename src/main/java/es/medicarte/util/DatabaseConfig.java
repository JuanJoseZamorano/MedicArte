package es.medicarte.util;

/**
 * Clase de configuración de la base de datos.
 * Centraliza los datos de conexión y permite
 * sobreescribirlos mediante variables de entorno,
 * facilitando el despliegue en distintos entornos
 * (local, Docker, etc.).
 */
public class DatabaseConfig {

    // Host de la base de datos (por defecto localhost)
    public static final String DB_HOST =
            System.getenv().getOrDefault("DB_HOST", "localhost");

    // Puerto de la base de datos (por defecto 5432)
    public static final String DB_PORT =
            System.getenv().getOrDefault("DB_PORT", "5432");

    // Nombre de la base de datos
    public static final String DB_NAME =
            System.getenv().getOrDefault("DB_NAME", "MedicArte");

    // Usuario de la base de datos
    public static final String DB_USER =
            System.getenv().getOrDefault("DB_USER", "postgres");

    // Contraseña de la base de datos
    public static final String DB_PASSWORD =
            System.getenv().getOrDefault("DB_PASS", "Erkenenpostgres23");

    /**
     * Construye la URL JDBC de conexión a PostgreSQL
     * a partir de los parámetros configurados.
     *
     * @return URL JDBC completa
     */
    public static String getJdbcUrl() {
        return "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    }
}
