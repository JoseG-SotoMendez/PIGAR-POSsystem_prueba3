package org.PosPrueba.Model.Persistence.impl;

import org.PosPrueba.Config.DataSourceConfig;
import org.PosPrueba.Model.Usuario;
import org.PosPrueba.Model.Persistence.Dao.UsuarioDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySQLUsuarioDAO implements UsuarioDAO {

    @Override
    public Long guardar(Usuario usuario) throws SQLException {
        if (usuario.getId() == null) {
            String sql = "INSERT INTO usuarios (username, password, nombre, apellido, rol, activo) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DataSourceConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, usuario.getUsername());
                ps.setString(2, usuario.getPassword());
                ps.setString(3, usuario.getNombre());
                ps.setString(4, usuario.getApellido());
                ps.setString(5, usuario.getEmail());
                ps.setString(6, usuario.getRol() == null ? null : usuario.getRol().name());
                ps.setBoolean(7, usuario.isActivo());
                Timestamp ts = usuario.getFechaCreacion() == null ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(usuario.getFechaCreacion());
                ps.setTimestamp(8, ts);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getLong(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID generado");
                    }
                }
            }
        } else {
            // Si ya existe id -> actualizar
            String sql = "INSERT INTO usuarios (username, password, nombre, apellido, rol, activo) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DataSourceConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, usuario.getUsername());
                ps.setString(2, usuario.getPassword());
                ps.setString(3, usuario.getNombre());
                ps.setString(4, usuario.getApellido());
                ps.setString(5, usuario.getEmail());
                ps.setString(6, usuario.getRol() == null ? null : usuario.getRol().name());
                ps.setBoolean(7, usuario.isActivo());
                ps.setLong(8, usuario.getId());
                ps.executeUpdate();
                return usuario.getId();
            }
        }
    }

    @Override
    public Usuario buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, username, password, nombre, apellido, email, rol, activo, fecha_creacion FROM usuarios WHERE id = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Usuario buscarPorUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password, nombre, apellido, email, rol, activo, fecha_creacion FROM usuarios WHERE username = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Usuario autenticar(String username, String password) throws SQLException {
        Usuario u = buscarPorUsername(username);
        if (u != null && u.getPassword() != null && u.getPassword().equals(password)) {
            // Nota: en producción no compares passwords en texto plano; usa hash + salt
            return u;
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> res = new ArrayList<>();
        String sql = "SELECT id, username, password, nombre, apellido, email, rol, activo, fecha_creacion FROM usuarios ORDER BY username";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                res.add(mapRow(rs));
            }
        }
        return res;
    }

    @Override
    public void actualizar(Usuario usuario) throws SQLException {
        if (usuario.getId() == null) throw new SQLException("Usuario.id es null");
        String sql = "UPDATE usuarios SET username=?, password=?, nombre=?, apellido=?, email=?, rol=?, activo=? WHERE id=?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getApellido());
            ps.setString(5, usuario.getEmail());
            ps.setString(6, usuario.getRol() == null ? null : usuario.getRol().name());
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
        String rol = rs.getString("rol");
        if (rol != null) {
            try {
                u.setRol(Usuario.Rol.valueOf(rol));
            } catch (IllegalArgumentException ex) {
                u.setRol(null);
            }
        }
        u.setActivo(rs.getBoolean("activo"));
        Timestamp ts = rs.getTimestamp("fecha_creacion");
        if (ts != null) {
            u.setFechaCreacion(ts.toLocalDateTime());
        }
        return u;
    }
}
