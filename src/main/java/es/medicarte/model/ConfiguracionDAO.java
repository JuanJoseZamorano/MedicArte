package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Clase DAO encargada de gestionar la configuración
 * general de la aplicación mediante pares clave-valor.
 */
public class ConfiguracionDAO {

    private static final String SELECT_SQL =
            "SELECT valor FROM medicarte.configuracion WHERE clave = ?";

    private static final String INSERT_SQL =
            "INSERT INTO medicarte.configuracion (clave, valor) VALUES (?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE medicarte.configuracion SET valor = ? WHERE clave = ?";

    /**
     * Obtiene el valor asociado a una clave de configuración.
     *
     * @param clave Clave de configuración
     * @return Valor asociado o null si no existe
     */
    public String getValor(String clave) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SQL)) {

            ps.setString(1, clave);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("valor");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Inserta o actualiza una clave de configuración.
     * Si la clave existe se actualiza, si no, se crea.
     *
     * @param clave Clave de configuración
     * @param valor Valor a almacenar
     */
    public void setValor(String clave, String valor) {

        try (Connection conn = DatabaseConnection.getConnection()) {

            boolean existe;

            // Comprobamos si la clave ya existe
            try (PreparedStatement ps = conn.prepareStatement(SELECT_SQL)) {
                ps.setString(1, clave);
                try (ResultSet rs = ps.executeQuery()) {
                    existe = rs.next();
                }
            }

            if (existe) {
                try (PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
                    ps.setString(1, valor);
                    ps.setString(2, clave);
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
                    ps.setString(1, clave);
                    ps.setString(2, valor);
                    ps.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
