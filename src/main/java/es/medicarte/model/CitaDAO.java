package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO encargada de gestionar el acceso a datos
 * de las citas médicas.
 */
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

    private static final String CANCELAR_CITA_SQL =
            "UPDATE medicarte.cita SET estado = 'CANCELADA' WHERE id_cita = ?";

    private static final String COMPLETAR_CITA =
            "UPDATE medicarte.cita SET estado = 'COMPLETADA' WHERE id_cita = ?";

    /**
     * Inserta una nueva cita en la base de datos.
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
     * Obtiene las citas de un paciente concreto.
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

    /**
     * Mapea un ResultSet a un objeto Cita.
     */
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

    /**
     * Obtiene todas las citas del sistema.
     */
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

    /**
     * Marca una cita como cancelada.
     */
    public boolean cancelarCita(int idCita) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CANCELAR_CITA_SQL)) {

            ps.setInt(1, idCita);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Marca una cita como completada tras pasar a consulta.
     */
    public boolean completarCita(int idCita) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(COMPLETAR_CITA)) {

            ps.setInt(1, idCita);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cuenta el número de citas pendientes para el día actual.
     */
    public int countCitasPendientesHoy() {

        String sql = """
        SELECT COUNT(*)
        FROM medicarte.cita
        WHERE estado = 'PENDIENTE'
          AND DATE(fecha_hora) = CURRENT_DATE
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Obtiene la próxima cita pendiente posterior al momento actual.
     */
    public Cita findProximaCitaPendiente() {

        String sql = """
        SELECT *
        FROM medicarte.cita
        WHERE estado = 'PENDIENTE'
          AND fecha_hora > NOW()
        ORDER BY fecha_hora ASC
        LIMIT 1
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Obtiene las citas asociadas a una fecha concreta.
     */
    public List<Cita> findByFecha(LocalDate fecha) {

        String sql = """
        SELECT *
        FROM medicarte.cita
        WHERE DATE(fecha_hora) = ?
        ORDER BY fecha_hora
        """;

        List<Cita> resultado = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapResultSet(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    /**
     * Actualiza los datos básicos de una cita existente.
     * Se utiliza al editar una cita.
     */
    public boolean update(Cita cita) {

        String sql = """
        UPDATE medicarte.cita
        SET id_medico = ?,
            fecha_hora = ?,
            duracion_min = ?,
            observaciones = ?
        WHERE id_cita = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cita.getIdMedico());
            ps.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
            ps.setInt(3, cita.getDuracionMin());
            ps.setString(4, cita.getObservaciones());
            ps.setInt(5, cita.getIdCita());

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
