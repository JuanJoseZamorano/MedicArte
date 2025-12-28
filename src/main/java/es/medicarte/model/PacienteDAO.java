package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    private static final String INSERT_SQL =
            "INSERT INTO medicarte.paciente (" +
                    "dni, nombre, apellidos, fecha_nacimiento, sexo, telefono, email, " +
                    "direccion, provincia, cp, aseguradora, num_poliza, nuhsa, nuss, nhc, " +
                    "grupo_sanguineo, alergias, antecedentes_personales, antecedentes_familiares, tratamiento_actual" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE medicarte.paciente SET " +
                    "dni=?, nombre=?, apellidos=?, fecha_nacimiento=?, sexo=?, telefono=?, email=?, " +
                    "direccion=?, provincia=?, cp=?, aseguradora=?, num_poliza=?, nuhsa=?, nuss=?, nhc=?, " +
                    "grupo_sanguineo=?, alergias=?, antecedentes_personales=?, antecedentes_familiares=?, tratamiento_actual=? " +
                    "WHERE id_paciente=?";


    private static final String SELECT_ALL_SQL =
            "SELECT * FROM medicarte.paciente ORDER BY apellidos, nombre";

    private static final String EXISTS_DNI_SQL =
            "SELECT COUNT(*) FROM medicarte.paciente WHERE dni = ?";

    private static final String DELETE_SQL =
            "DELETE FROM medicarte.paciente WHERE id_paciente=?";
    /**
     * Inserta un nuevo paciente en la base de datos
     */
    public boolean insert(Paciente p) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setString(1, p.getDni());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getApellidos());

            if (p.getFechaNacimiento() != null) {
                ps.setDate(4, java.sql.Date.valueOf(p.getFechaNacimiento()));
            } else {
                ps.setDate(4, null);
            }

            ps.setString(5, p.getSexo());
            ps.setString(6, p.getTelefono());
            ps.setString(7, p.getEmail());
            ps.setString(8, p.getDireccion());
            ps.setString(9, p.getProvincia());
            ps.setString(10, p.getCp());
            ps.setString(11, p.getAseguradora());
            ps.setString(12, p.getNumPoliza());
            ps.setString(13, p.getNuhsa());
            ps.setString(14, p.getNuss());
            ps.setString(15, p.getNhc());
            ps.setString(16, p.getGrupoSanguineo());
            ps.setString(17, p.getAlergias());
            ps.setString(18, p.getAntecedentesPersonales());
            ps.setString(19, p.getAntecedentesFamiliares());
            ps.setString(20, p.getTratamientoActual());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene la lista completa de pacientes
     */
    public List<Paciente> findAll() {

        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pacientes.add(mapResultSetToPaciente(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pacientes;
    }

    /**
     * Mapea un ResultSet a un objeto Paciente
     */
    private Paciente mapResultSetToPaciente(ResultSet rs) throws SQLException {

        Paciente p = new Paciente();

        p.setIdPaciente(rs.getInt("id_paciente"));
        p.setDni(rs.getString("dni"));
        p.setNombre(rs.getString("nombre"));
        p.setApellidos(rs.getString("apellidos"));

        Date fecha = rs.getDate("fecha_nacimiento");
        if (fecha != null) {
            p.setFechaNacimiento(fecha.toLocalDate());
        }

        p.setSexo(rs.getString("sexo"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setDireccion(rs.getString("direccion"));
        p.setProvincia(rs.getString("provincia"));
        p.setCp(rs.getString("cp"));
        p.setAseguradora(rs.getString("aseguradora"));
        p.setNumPoliza(rs.getString("num_poliza"));
        p.setNuhsa(rs.getString("nuhsa"));
        p.setNuss(rs.getString("nuss"));
        p.setNhc(rs.getString("nhc"));
        p.setGrupoSanguineo(rs.getString("grupo_sanguineo"));
        p.setAlergias(rs.getString("alergias"));
        p.setAntecedentesPersonales(rs.getString("antecedentes_personales"));
        p.setAntecedentesFamiliares(rs.getString("antecedentes_familiares"));
        p.setTratamientoActual(rs.getString("tratamiento_actual"));

        Timestamp ts = rs.getTimestamp("creado_en");
        if (ts != null) {
            p.setCreadoEn(ts.toLocalDateTime());
        }

        return p;
    }

    public boolean update(Paciente p) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, p.getDni());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getApellidos());

            if (p.getFechaNacimiento() != null) {
                ps.setDate(4, java.sql.Date.valueOf(p.getFechaNacimiento()));
            } else {
                ps.setDate(4, null);
            }

            ps.setString(5, p.getSexo());
            ps.setString(6, p.getTelefono());
            ps.setString(7, p.getEmail());
            ps.setString(8, p.getDireccion());
            ps.setString(9, p.getProvincia());
            ps.setString(10, p.getCp());
            ps.setString(11, p.getAseguradora());
            ps.setString(12, p.getNumPoliza());
            ps.setString(13, p.getNuhsa());
            ps.setString(14, p.getNuss());
            ps.setString(15, p.getNhc());
            ps.setString(16, p.getGrupoSanguineo());
            ps.setString(17, p.getAlergias());
            ps.setString(18, p.getAntecedentesPersonales());
            ps.setString(19, p.getAntecedentesFamiliares());
            ps.setString(20, p.getTratamientoActual());

            ps.setInt(21, p.getIdPaciente());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsByDni(String dni) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(EXISTS_DNI_SQL)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int idPaciente) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, idPaciente);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Paciente> buscar(String apellidos, String nombre, String dni) {

        List<Paciente> resultado = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM medicarte.paciente WHERE 1=1"
        );

        if (apellidos != null && !apellidos.isBlank()) {
            sql.append(" AND apellidos ILIKE ?");
        }
        if (nombre != null && !nombre.isBlank()) {
            sql.append(" AND nombre ILIKE ?");
        }
        if (dni != null && !dni.isBlank()) {
            sql.append(" AND dni ILIKE ?");
        }

        sql.append(" ORDER BY apellidos, nombre");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (apellidos != null && !apellidos.isBlank()) {
                ps.setString(index++, "%" + apellidos + "%");
            }
            if (nombre != null && !nombre.isBlank()) {
                ps.setString(index++, "%" + nombre + "%");
            }
            if (dni != null && !dni.isBlank()) {
                ps.setString(index++, "%" + dni + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapResultSetToPaciente(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }

}

