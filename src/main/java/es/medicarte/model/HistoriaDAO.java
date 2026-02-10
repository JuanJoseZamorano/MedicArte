package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;

/**
 * Clase DAO encargada de gestionar el acceso a la tabla
 * historia_clinica de la base de datos.
 */
public class HistoriaDAO {

    private static final String SELECT_BY_PACIENTE =
            "SELECT * FROM medicarte.historia_clinica WHERE id_paciente = ?";

    private static final String INSERT_HISTORIA =
            "INSERT INTO medicarte.historia_clinica " +
                    "(id_paciente, fecha_apertura, estado) " +
                    "VALUES (?, ?, 'ACTIVA') " +
                    "RETURNING id_historia";

    /**
     * Obtiene la historia clínica de un paciente.
     * Si no existe, la crea automáticamente.
     *
     * Este enfoque garantiza que siempre exista una historia clínica
     * antes de crear episodios o consultas.
     */
    public HistoriaClinica findOrCreateByPaciente(int idPaciente) {

        try (Connection conn = DatabaseConnection.getConnection()) {

            // 1️⃣ Intentar obtener la historia clínica existente
            try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_PACIENTE)) {
                ps.setInt(1, idPaciente);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        HistoriaClinica h = new HistoriaClinica();
                        h.setIdHistoria(rs.getInt("id_historia"));
                        h.setIdPaciente(rs.getInt("id_paciente"));
                        h.setFechaApertura(
                                rs.getDate("fecha_apertura").toLocalDate()
                        );
                        h.setEstado(rs.getString("estado"));
                        h.setNotas(rs.getString("notas"));
                        return h;
                    }
                }
            }

            // 2️⃣ Si no existe, se crea automáticamente
            try (PreparedStatement ps = conn.prepareStatement(INSERT_HISTORIA)) {
                ps.setInt(1, idPaciente);
                ps.setDate(2, Date.valueOf(LocalDate.now()));

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        HistoriaClinica h = new HistoriaClinica();
                        h.setIdHistoria(rs.getInt(1));
                        h.setIdPaciente(idPaciente);
                        h.setFechaApertura(LocalDate.now());
                        h.setEstado("ACTIVA");
                        return h;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Obtiene una historia clínica a partir de su identificador.
     *
     * @param idHistoria Identificador de la historia clínica
     * @return Historia clínica encontrada o null si no existe
     */
    public HistoriaClinica findById(int idHistoria) {

        String sql =
                "SELECT * FROM medicarte.historia_clinica WHERE id_historia = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idHistoria);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HistoriaClinica h = new HistoriaClinica();
                    h.setIdHistoria(rs.getInt("id_historia"));
                    h.setIdPaciente(rs.getInt("id_paciente"));
                    h.setEstado(rs.getString("estado"));
                    h.setFechaApertura(
                            rs.getDate("fecha_apertura").toLocalDate()
                    );
                    h.setNotas(rs.getString("notas"));
                    return h;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

