package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    private static final String INSERT_SQL =
            "INSERT INTO medicarte.cita " +
                    "(id_paciente, id_medico, fecha_hora, estado, origen, observaciones, duracion_min) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_PACIENTE_SQL =
            "SELECT * FROM medicarte.cita " +
                    "WHERE id_paciente = ? " +
                    "ORDER BY fecha_hora";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM medicarte.cita ORDER BY fecha_hora";

    /**
     * Inserta una nueva cita
     */
    public boolean insert(Cita c) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setInt(1, c.getIdPaciente());
            ps.setInt(2, c.getIdMedico());
            ps.setTimestamp(3, Timestamp.valueOf(c.getFechaHora()));
            ps.setString(4, c.getEstado());
            ps.setString(5, c.getOrigen());
            ps.setString(6, c.getObservaciones());
            ps.setObject(7, c.getDuracionMin(), Types.SMALLINT);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene las citas de un paciente
     */
    public List<Cita> findByPaciente(int idPaciente) {

        List<Cita> citas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_PACIENTE_SQL)) {

            ps.setInt(1, idPaciente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return citas;
    }

    private Cita mapResultSet(ResultSet rs) throws SQLException {

        Cita c = new Cita();

        c.setIdCita(rs.getInt("id_cita"));
        c.setIdPaciente(rs.getInt("id_paciente"));
        c.setIdMedico(rs.getInt("id_medico"));

        Timestamp ts = rs.getTimestamp("fecha_hora");
        if (ts != null) {
            c.setFechaHora(ts.toLocalDateTime());
        }

        c.setEstado(rs.getString("estado"));
        c.setOrigen(rs.getString("origen"));
        c.setObservaciones(rs.getString("observaciones"));
        c.setDuracionMin(rs.getObject("duracion_min", Integer.class));

        return c;
    }
    public List<Cita> findAll() {

        List<Cita> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
