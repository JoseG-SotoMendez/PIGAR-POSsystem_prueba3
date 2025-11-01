package org.PosPrueba.Model.Service;

import com.sun.jdi.connect.spi.Connection;
import org.PosPrueba.Config.DataSourceConfig;
import org.PosPrueba.Model.DetalleVenta;
import org.PosPrueba.Model.Venta;
import org.PosPrueba.Model.Producto;
import org.PosPrueba.Model.ItemCarrito;
import org.PosPrueba.Model.Persistence.Dao.VentaDAO;
import org.PosPrueba.Model.Persistence.Dao.ProductoDAO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServicioVenta {

    private final VentaDAO ventaDAO;
    private final ProductoDAO productoDAO;

    public ServicioVenta(VentaDAO ventaDAO, ProductoDAO productoDAO) {
        this.ventaDAO = ventaDAO;
        this.productoDAO = productoDAO;
    }

    /**
     * Crea una venta a partir de una lista de ItemCarrito.
     * Lanza excepción si algún producto no existe o no hay stock suficiente.
     * Devuelve el id de la venta creada.
     */
    public Long crearVentaDesdeCarrito(List<ItemCarrito> items, Long clienteId) throws Exception {
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("Carrito vacío");

        Venta venta = new Venta();
        venta.setClienteId(clienteId);

        BigDecimal total = BigDecimal.ZERO;
        List<DetalleVenta> detalles = new ArrayList<>();

        // Validar y construir detalles
        for (ItemCarrito it : items) {
            Long pid = it.getProductoId();
            Producto p = productoDAO.buscarPorId(pid);
            if (p == null) throw new Exception("Producto no encontrado: id=" + pid);
            if (p.getStock() < it.getCantidad()) {
                throw new Exception("Stock insuficiente para el producto: " + p.getNombre());
            }
            DetalleVenta d = new DetalleVenta();
            d.setProductoId(pid);
            d.setCantidad(it.getCantidad());
            d.setPrecioUnitario(it.getPrecioUnitario());
            detalles.add(d);

            total = total.add(it.getPrecioUnitario().multiply(BigDecimal.valueOf(it.getCantidad())));
        }

        venta.setTotal(total);

        // Delegamos la inserción + actualización de stock al DAO (que hace la transacción)
        Long ventaId = ventaDAO.guardarVentaConDetalles(venta, detalles);

        return ventaId;
    }

    public BigDecimal obtenerTotalVentas() throws SQLException {
        String sql = "SELECT SUM(total) FROM ventas";
        try (Connection conn = DataSourceConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO

    }
