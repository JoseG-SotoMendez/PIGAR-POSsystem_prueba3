package org.PosPrueba.Model.Persistence.impl;

import org.PosPrueba.Config.DataSourceConfig;
import org.PosPrueba.Model.Cliente;
import org.PosPrueba.Model.Persistence.Dao.ClienteDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLClienteDAO implements ClienteDAO {

    @Override
    public Long guardar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, apellido, documento, telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getDocumento());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());
            ps.setString(6, cliente.getDireccion());
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
    public Cliente buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
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
    public Cliente buscarPorDocumento(String documento) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE documento = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<Cliente> listarTodos() throws SQLException {
        String sql = "SELECT * FROM clientes ORDER BY nombre, apellido";
        List<Cliente> lista = new ArrayList<>();
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
    public List<Cliente> buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE LOWER(nombre) LIKE ? OR LOWER(apellido) LIKE ? ORDER BY nombre, apellido";
        List<Cliente> lista = new ArrayList<>();
        String patron = "%" + nombre.toLowerCase() + "%";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ?, apellido = ?, documento = ?, telefono = ?, email = ?, direccion = ? WHERE id = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getDocumento());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());
            ps.setString(6, cliente.getDireccion());
            ps.setLong(7, cliente.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Cliente mapRow(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setNombre(rs.getString("nombre"));
        c.setApellido(rs.getString("apellido"));
        c.setDocumento(rs.getString("documento"));
        c.setTelefono(rs.getString("telefono"));
        c.setEmail(rs.getString("email"));
        c.setDireccion(rs.getString("direccion"));
        return c;
    }
}
