package org.PosPrueba.Model.Persistence.impl;

import org.PosPrueba.Config.DataSourceConfig;
import org.PosPrueba.Model.Proveedor;
import org.PosPrueba.Model.Persistence.Dao.ProveedorDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLProveedorDAO implements ProveedorDAO {

    @Override
    public Long guardar(Proveedor proveedor) throws SQLException {
        String sql = "INSERT INTO proveedores (nombre, ruc, telefono, email, direccion, contacto) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getRuc());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getEmail());
            ps.setString(5, proveedor.getDireccion());
            ps.setString(6, proveedor.getContacto());
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
    public Proveedor buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM proveedores WHERE id = ?";
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
    public Proveedor buscarPorRuc(String ruc) throws SQLException {
        String sql = "SELECT * FROM proveedores WHERE ruc = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ruc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<Proveedor> listarTodos() throws SQLException {
        String sql = "SELECT * FROM proveedores ORDER BY nombre";
        List<Proveedor> lista = new ArrayList<>();
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
    public List<Proveedor> buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM proveedores WHERE LOWER(nombre) LIKE ? ORDER BY nombre";
        List<Proveedor> lista = new ArrayList<>();
        String patron = "%" + nombre.toLowerCase() + "%";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Proveedor proveedor) throws SQLException {
        String sql = "UPDATE proveedores SET nombre = ?, ruc = ?, telefono = ?, email = ?, direccion = ?, contacto = ? WHERE id = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getRuc());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getEmail());
            ps.setString(5, proveedor.getDireccion());
            ps.setString(6, proveedor.getContacto());
            ps.setLong(7, proveedor.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM proveedores WHERE id = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Proveedor mapRow(ResultSet rs) throws SQLException {
        Proveedor p = new Proveedor();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombre"));
        p.setRuc(rs.getString("ruc"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setDireccion(rs.getString("direccion"));
        p.setContacto(rs.getString("contacto"));
        return p;
    }
}
