package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}
