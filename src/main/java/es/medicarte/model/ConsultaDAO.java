package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO encargada de gestionar el acceso a datos
 * de las consultas médicas.
 */
public class ConsultaDAO {

    // Consulta para obtener las consultas de un episodio,
    // ordenadas cronológicamente
    private static final String SELECT_BY_EPISODIO =
            "SELECT * FROM medicarte.consulta " +
                    "WHERE id_episodio = ? " +
                    "ORDER BY fecha_hora ASC";

    // Inserción de una nueva consulta
    private static final String INSERT_CONSULTA =
            "INSERT INTO medicarte.consulta " +
                    "(id_episodio, id_medico, id_cita, fecha_hora, motivo_consulta, anamnesis, exploracion, diagnostico, tratamiento, observaciones, estado) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Obtiene todas las consultas asociadas a un episodio clínico.
     *
     * @param idEpisodio Identificador del episodio
     * @return Lista de consultas
     */
    public List<Consulta> findByEpisodio(int idEpisodio) {

        List<Consulta> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_EPISODIO)) {

            ps.setInt(1, idEpisodio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapConsulta(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Mapea un ResultSet a un objeto Consulta.
     * Centraliza la conversión desde JDBC.
     */
    private Consulta mapConsulta(ResultSet rs) throws SQLException {

        Consulta c = new Consulta();

        c.setIdConsulta(rs.getInt("id_consulta"));
        c.setIdEpisodio(rs.getInt("id_episodio"));
        c.setIdMedico(rs.getInt("id_medico"));
        c.setIdCita(rs.getObject("id_cita", Integer.class));

        Timestamp ts = rs.getTimestamp("fecha_hora");
        if (ts != null) {
            c.setFechaHora(ts.toLocalDateTime());
        }

        c.setMotivoConsulta(rs.getString("motivo_consulta"));
        c.setAnamnesis(rs.getString("anamnesis"));
        c.setExploracion(rs.getString("exploracion"));
        c.setDiagnostico(rs.getString("diagnostico"));
        c.setDiagnosticoCod(rs.getString("diagnostico_cod"));
        c.setTratamiento(rs.getString("tratamiento"));
        c.setObservaciones(rs.getString("observaciones"));
        c.setEstado(rs.getString("estado"));

        return c;
    }

    /**
     * Inserta una nueva consulta en la base de datos.
     *
     * @param c Consulta a insertar
     * @return true si la inserción se realiza correctamente
     */
    public boolean insert(Consulta c) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_CONSULTA)) {

            ps.setInt(1, c.getIdEpisodio());
            ps.setInt(2, c.getIdMedico());
            ps.setInt(3, c.getIdCita());
            ps.setTimestamp(4, Timestamp.valueOf(c.getFechaHora()));
            ps.setString(5, c.getMotivoConsulta());
            ps.setString(6, c.getAnamnesis());
            ps.setString(7, c.getExploracion());
            ps.setString(8, c.getDiagnostico());
            ps.setString(9, c.getTratamiento());
            ps.setString(10, c.getObservaciones());
            ps.setString(11, c.getEstado());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

