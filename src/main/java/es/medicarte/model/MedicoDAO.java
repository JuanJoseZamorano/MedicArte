package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {

    private static final String SELECT_BY_ID =
            "SELECT * FROM medicarte.medico WHERE id_medico = ?";

    public Medico findById(int idMedico) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, idMedico);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Medico m = new Medico();
                    m.setIdMedico(rs.getInt("id_medico"));
                    m.setNombreApellidos(rs.getString("nombre_apellidos"));
                    m.setNumColegiado(rs.getString("num_colegiado"));
                    return m;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Integer insert(Medico medico) {

        String sql = """
        INSERT INTO medicarte.medico
        (nombre_apellidos, num_colegiado, id_especialidad, activo)
        VALUES (?, ?, ?, ?)
        RETURNING id_medico
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, medico.getNombreApellidos());
            ps.setString(2, medico.getNumColegiado());
            ps.setInt(3, medico.getIdEspecialidad());
            ps.setBoolean(4, medico.isActivo());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_medico");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Medico> findAllActivos() {

        String sql = "SELECT * FROM medicarte.medico WHERE activo = true ORDER BY nombre_apellidos";

        List<Medico> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medico m = new Medico();
                m.setIdMedico(rs.getInt("id_medico"));
                m.setNombreApellidos(rs.getString("nombre_apellidos"));
                m.setNumColegiado(rs.getString("num_colegiado"));
                m.setIdEspecialidad(rs.getInt("id_especialidad"));
                m.setActivo(rs.getBoolean("activo"));
                lista.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }


}
