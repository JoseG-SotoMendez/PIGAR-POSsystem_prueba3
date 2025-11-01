package org.PosPrueba.Model.Persistence.impl;

import org.PosPrueba.Config.DataSourceConfig;
import org.PosPrueba.Model.DetalleVenta;
import org.PosPrueba.Model.Venta;
import org.PosPrueba.Model.Persistence.Dao.VentaDAO;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySQLVentaDAO implements VentaDAO {

    @Override
    public Long guardarVentaConDetalles(Venta venta, List<DetalleVenta> detalles) throws SQLException {
        String sqlInsertVenta = "INSERT INTO ventas (cliente_id, fecha, total) VALUES (?, ?, ?)";
        String sqlInsertDetalle = "INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";

        try (Connection conn = DataSourceConfig.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psVenta = conn.prepareStatement(sqlInsertVenta, Statement.RETURN_GENERATED_KEYS)) {
                if (venta.getClienteId() == null) {
                    psVenta.setNull(1, Types.BIGINT);
                } else {
                    psVenta.setLong(1, venta.getClienteId());
                }
                LocalDateTime fecha = venta.getFecha();
                Timestamp ts = (fecha == null) ? new Timestamp(System.currentTimeMillis()) : Timestamp.valueOf(fecha);
                psVenta.setTimestamp(2, ts);
                psVenta.setBigDecimal(3, venta.getTotal() == null ? BigDecimal.ZERO : venta.getTotal());
                psVenta.executeUpdate();

                Long ventaId;
                try (ResultSet keys = psVenta.getGeneratedKeys()) {
                    if (keys.next()) ventaId = keys.getLong(1);
                    else throw new SQLException("No se pudo obtener id generado para la venta.");
                }

                try (PreparedStatement psDet = conn.prepareStatement(sqlInsertDetalle);
                     PreparedStatement psUpd = conn.prepareStatement(sqlUpdateStock)) {

                    // Insertar detalles
                    for (DetalleVenta d : detalles) {
                        psDet.setLong(1, ventaId);
                        psDet.setLong(2, d.getProductoId());
                        psDet.setInt(3, d.getCantidad());
                        psDet.setBigDecimal(4, d.getPrecioUnitario());
                        psDet.addBatch();
                    }
                    psDet.executeBatch();

                    // Actualizar stocks (en la misma transacción)
                    for (DetalleVenta d : detalles) {
                        psUpd.setInt(1, d.getCantidad());
                        psUpd.setLong(2, d.getProductoId());
                        psUpd.setInt(3, d.getCantidad());
                        int affected = psUpd.executeUpdate();
                        if (affected != 1) {
                            throw new SQLException("Stock insuficiente para producto id " + d.getProductoId());
                        }
                    }
                }

                conn.commit();
                return ventaId;

            } catch (SQLException e) {
                try { conn.rollback(); } catch (SQLException ignore) {}
                throw e;
            } finally {
                try { conn.setAutoCommit(true); } catch (SQLException ignore) {}
            }
        }
    }


    @Override
    public Venta buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, cliente_id, fecha, total FROM ventas WHERE id = ?";
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Venta v = new Venta();
                    v.setId(rs.getLong("id"));
                    Object cid = rs.getObject("cliente_id");
                    if (cid != null) v.setClienteId(((Number)cid).longValue());
                    Timestamp ts = rs.getTimestamp("fecha");
                    if (ts != null) v.setFecha(ts.toLocalDateTime());
                    v.setTotal(rs.getBigDecimal("total"));
                    return v;
                }
                return null;
            }
        }
    }

    @Override
    public List<Venta> listarTodos() throws SQLException {
        String sql = "SELECT id, cliente_id, fecha, total FROM ventas ORDER BY fecha DESC";
        List<Venta> lista = new ArrayList<>();
        try (Connection conn = DataSourceConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Venta v = new Venta();
                v.setId(rs.getLong("id"));
                Object cid = rs.getObject("cliente_id");
                if (cid != null) v.setClienteId(((Number)cid).longValue());
                Timestamp ts = rs.getTimestamp("fecha");
                if (ts != null) v.setFecha(ts.toLocalDateTime());
                v.setTotal(rs.getBigDecimal("total"));
                lista.add(v);
            }
        }
        return lista;
    }
}
