package es.medicarte.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexión con la base de datos.
 * Implementa un acceso centralizado para reutilizar la conexión
 * durante la ejecución de la aplicación.
 */
public class DatabaseConnection {

    // Conexión única reutilizable a la base de datos
    private static Connection connection;

    // Constructor privado para evitar la creación de instancias
    // Esta clase actúa como proveedor de conexión estático
    private DatabaseConnection() {
        // No instanciable
    }

    /**
     * Devuelve una conexión activa a la base de datos.
     * Si la conexión no existe o está cerrada, se crea una nueva.
     *
     * @return Conexión JDBC activa
     * @throws SQLException Si ocurre un error al conectar con la base de datos
     */
    public static Connection getConnection() throws SQLException {

        // Se comprueba si la conexión es nula o está cerrada
        if (connection == null || connection.isClosed()) {

            // Se establece la conexión utilizando los datos de configuración
            connection = DriverManager.getConnection(
                    DatabaseConfig.getJdbcUrl(),
                    DatabaseConfig.DB_USER,
                    DatabaseConfig.DB_PASSWORD
            );
        }

        return connection;
    }
}
