package org.PosPrueba.Model.Persistence.Dao;

import org.PosPrueba.Model.Venta;
import org.PosPrueba.Model.DetalleVenta;

import java.sql.SQLException;
import java.util.List;

/**
 * DAO para persistencia de ventas y sus detalles.
 */
public interface VentaDAO {

    /**
     * Guarda la venta y sus detalles en la base de datos de forma atómica.
     * @return id generado de la venta
     */
    Long guardarVentaConDetalles(Venta venta, List<DetalleVenta> detalles) throws SQLException;

    Venta buscarPorId(Long id) throws SQLException;

    List<Venta> listarTodos() throws SQLException;
}
