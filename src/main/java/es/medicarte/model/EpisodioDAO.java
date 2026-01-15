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
                    lista.add(e);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
