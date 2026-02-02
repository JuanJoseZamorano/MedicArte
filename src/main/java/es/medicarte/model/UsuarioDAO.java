package es.medicarte.model;

import es.medicarte.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario findByUsername(String username) {

        String sql = """
            SELECT id_usuario, username, password_hash, rol, activo, id_medico
            FROM medicarte.usuario
            WHERE username = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setUsername(rs.getString("username"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setRol(rs.getString("rol"));
                    u.setActivo(rs.getBoolean("activo"));
                    u.setIdMedico(rs.getObject("id_medico", Integer.class));
                    return u;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<Usuario> findAll() {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT * FROM medicarte.usuario ORDER BY username";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setRol(rs.getString("rol"));
                u.setActivo(rs.getBoolean("activo"));
                u.setIdMedico((Integer) rs.getObject("id_medico"));
                usuarios.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }
    public boolean existsByUsername(String username) {

        String sql = "SELECT 1 FROM medicarte.usuario WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void delete(int idUsuario) {

        String sql = "DELETE FROM medicarte.usuario WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insert(Usuario usuario) {

        String sql = """
        INSERT INTO medicarte.usuario
        (username, password_hash, rol, id_medico, activo)
        VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPasswordHash());
            ps.setString(3, usuario.getRol());

            if (usuario.getIdMedico() != null) {
                ps.setInt(4, usuario.getIdMedico());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            ps.setBoolean(5, usuario.isActivo());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
