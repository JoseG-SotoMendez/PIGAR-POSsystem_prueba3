package org.PosPrueba.Model.Persistence.Dao;

import org.PosPrueba.Model.Venta;
import org.PosPrueba.Model.DetalleVenta;
import java.sql.SQLException;
import java.util.List;

public interface VentaDAO {
    Long guardarVentaConDetalles(Venta venta, List<DetalleVenta> detalles) throws SQLException;
    Venta buscarPorId(Long id) throws SQLException;
    List<Venta> listarTodos() throws SQLException;
}
