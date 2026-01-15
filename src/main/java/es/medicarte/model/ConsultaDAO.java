package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    private static final String SELECT_BY_EPISODIO =
            "SELECT * FROM medicarte.consulta " +
                    "WHERE id_episodio = ? " +
                    "ORDER BY fecha_hora ASC";

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
}
