package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EpisodioDAO {

    private static final String SELECT_BY_PACIENTE =
            "SELECT e.* " +
                    "FROM medicarte.episodio e " +
                    "JOIN medicarte.historia_clinica h ON e.id_historia = h.id_historia " +
                    "WHERE h.id_paciente = ? " +
                    "ORDER BY e.fecha_inicio DESC";

    private static final String INSERT_EPISODIO =
            "INSERT INTO medicarte.episodio " +
                    "(id_historia, id_especialidad, motivo, estado) " +
                    "VALUES (?, ?, ?, 'ABIERTO') " +
                    "RETURNING id_episodio";

    public int insertar(Episodio e) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_EPISODIO)) {

            ps.setInt(1, e.getIdHistoria());
            ps.setInt(2, e.getIdEspecialidad());
            ps.setString(3, e.getMotivo());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_episodio");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    public List<Episodio> findByPaciente(int idPaciente) {

        List<Episodio> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_PACIENTE)) {

            ps.setInt(1, idPaciente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Episodio e = new Episodio();
                    e.setIdEpisodio(rs.getInt("id_episodio"));
                    e.setIdHistoria(rs.getInt("id_historia"));
                    e.setIdEspecialidad(rs.getInt("id_especialidad"));
                    e.setMotivo(rs.getString("motivo"));
                    e.setEstado(rs.getString("estado"));
                    e.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                    lista.add(e);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    public Episodio findById(int idEpisodio) {

        String sql = "SELECT * FROM medicarte.episodio WHERE id_episodio = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEpisodio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Episodio e = new Episodio();
                    e.setIdEpisodio(rs.getInt("id_episodio"));
                    e.setIdHistoria(rs.getInt("id_historia"));
                    e.setIdEspecialidad(rs.getInt("id_especialidad"));
                    e.setMotivo(rs.getString("motivo"));
                    e.setEstado(rs.getString("estado"));

                    if (rs.getDate("fecha_inicio") != null) {
                        e.setFechaInicio(
                                rs.getDate("fecha_inicio").toLocalDate()
                        );
                    }

                    return e;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
