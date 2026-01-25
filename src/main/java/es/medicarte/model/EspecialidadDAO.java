package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadDAO {

    private static final String SELECT_ALL =
            "SELECT * FROM medicarte.especialidad ORDER BY nombre";

    public List<Especialidad> findAll() {

        List<Especialidad> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Especialidad e = new Especialidad();
                e.setIdEspecialidad(rs.getInt("id_especialidad"));
                e.setNombre(rs.getString("nombre"));
                lista.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    public Especialidad findById(int idEspecialidad) {

        String sql = "SELECT * FROM medicarte.especialidad WHERE id_especialidad = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEspecialidad);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Especialidad e = new Especialidad();
                    e.setIdEspecialidad(rs.getInt("id_especialidad"));
                    e.setNombre(rs.getString("nombre"));
                    return e;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
