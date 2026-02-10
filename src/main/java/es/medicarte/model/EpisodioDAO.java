package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO encargada de gestionar el acceso a datos
 * de los episodios clínicos.
 */
public class EpisodioDAO {

    // Consulta que obtiene los episodios de un paciente
    // a partir de su historia clínica
    private static final String SELECT_BY_PACIENTE =
            "SELECT e.* " +
                    "FROM medicarte.episodio e " +
                    "JOIN medicarte.historia_clinica h ON e.id_historia = h.id_historia " +
                    "WHERE h.id_paciente = ? " +
                    "ORDER BY e.fecha_inicio DESC";

    // Inserción de un nuevo episodio (estado inicial ABIERTO)
    private static final String INSERT_EPISODIO =
            "INSERT INTO medicarte.episodio " +
                    "(id_historia, id_especialidad, motivo, estado) " +
                    "VALUES (?, ?, ?, 'ABIERTO') " +
                    "RETURNING id_episodio";

    /**
     * Inserta un nuevo episodio clínico en la base de datos.
     *
     * @param e Episodio a insertar
     * @return Identificador del episodio creado o -1 si falla
     */
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

    /**
     * Obtiene todos los episodios asociados a un paciente.
     * Se utiliza, por ejemplo, al mostrar el historial clínico.
     *
     * @param idPaciente Identificador del paciente
     * @return Lista de episodios
     */
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
                    e.setFechaInicio(
                            rs.getDate("fecha_inicio").toLocalDate()
                    );
                    lista.add(e);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Busca un episodio a partir de su identificador.
     *
     * @param idEpisodio Identificador del episodio
     * @return Episodio encontrado o null si no existe
     */
    public Episodio findById(int idEpisodio) {

        String sql =
                "SELECT * FROM medicarte.episodio WHERE id_episodio = ?";

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

