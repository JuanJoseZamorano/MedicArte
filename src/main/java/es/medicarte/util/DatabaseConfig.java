package es.medicarte.util;

public class DatabaseConfig {

    public static final String DB_HOST =
            System.getenv().getOrDefault("DB_HOST", "localhost");

    public static final String DB_PORT =
            System.getenv().getOrDefault("DB_PORT", "5432");

    public static final String DB_NAME =
            System.getenv().getOrDefault("DB_NAME", "MedicArte");

    public static final String DB_USER =
            System.getenv().getOrDefault("DB_USER", "postgres");

    public static final String DB_PASSWORD =
            System.getenv().getOrDefault("DB_PASS", "Erkenenpostgres23");

    public static String getJdbcUrl() {
        return "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    }
}
