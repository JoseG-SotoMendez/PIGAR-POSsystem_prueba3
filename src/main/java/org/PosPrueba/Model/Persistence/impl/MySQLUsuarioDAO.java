package org.PosPrueba.Model.Persistence.impl;

import org.PosPrueba.Config.DataSourceConfig;
import org.PosPrueba.Model.Usuario;
import org.PosPrueba.Model.Persistence.Dao.UsuarioDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLUsuarioDAO implements UsuarioDAO {

    @Override
    public Long guardar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (username, password, nombre, apellido, email, rol, activo, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword()); // En producción, usar hash (BCrypt)
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getApellido());
            ps.setString(5, usuario.getEmail());
            ps.setString(6, usuario.getRol().name());
            ps.setBoolean(7, usuario.isActivo());
            ps.setTimestamp(8, Timestamp.valueOf(usuario.getFechaCreacion()));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
                throw new SQLException("No se pudo obtener el ID generado");
            }
        }
    }

    @Override
    public Usuario buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    @Override
    public Usuario buscarPorUsername(String username) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    @Override
    public Usuario autenticar(String username, String password) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ? AND activo = true";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password); // En producción, comparar con hash
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT * FROM usuarios ORDER BY username";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET username = ?, password = ?, nombre = ?, apellido = ?, email = ?, rol = ?, activo = ? WHERE id = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getApellido());
            ps.setString(5, usuario.getEmail());
            ps.setString(6, usuario.getRol().name());
            ps.setBoolean(7, usuario.isActivo());
            ps.setLong(8, usuario.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellido"));
        u.setEmail(rs.getString("email"));
        u.setRol(Usuario.Rol.valueOf(rs.getString("rol")));
        u.setActivo(rs.getBoolean("activo"));
        Timestamp ts = rs.getTimestamp("fecha_creacion");
        if (ts != null) {
            u.setFechaCreacion(ts.toLocalDateTime());
        }
        return u;
    }
}
