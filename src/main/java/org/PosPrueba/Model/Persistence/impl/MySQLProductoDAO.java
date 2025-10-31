package org.PosPrueba.Model.Persistence.impl;

import org.PosPrueba.Model.Producto;
import org.PosPrueba.Model.Persistence.Dao.ProductoDAO;
import org.PosPrueba.Config.DataSourceConfig;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLProductoDAO implements ProductoDAO {

    public MySQLProductoDAO() {
        // constructor vacío
    }

    @Override
    public Producto buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nombre, descripcion, precio_unitario, stock FROM productos WHERE id = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public List<Producto> listarTodos() throws SQLException {
        String sql = "SELECT id, nombre, descripcion, precio_unitario, stock FROM productos";
        List<Producto> lista = new ArrayList<>();
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
    public void actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio_unitario = ?, stock = ? WHERE id = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecioUnitario() == null ? BigDecimal.ZERO : producto.getPrecioUnitario());
            ps.setInt(4, producto.getStock());
            ps.setLong(5, producto.getId());
            ps.executeUpdate();
        }
    }

    private Producto mapRow(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        p.setStock(rs.getInt("stock"));
        return p;
    }
}
