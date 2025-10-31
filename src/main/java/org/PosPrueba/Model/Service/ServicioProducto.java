package org.PosPrueba.Model.Service;

import org.PosPrueba.Model.Producto;
import org.PosPrueba.Model.Persistence.Dao.ProductoDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ServicioProducto {

    private final ProductoDAO productoDAO;

    public ServicioProducto(ProductoDAO productoDAO) {
        this.productoDAO = Objects.requireNonNull(productoDAO);
    }

    public List<Producto> listarProductos() throws SQLException {
        return productoDAO.listarTodos();
    }

    public Producto obtenerProducto(Long id) throws SQLException {
        return productoDAO.buscarPorId(id);
    }

    /**
     * Actualiza stock sumando 'delta' (delta puede ser negativo para restar).
     */
    public void actualizarStock(Long id, int delta) throws SQLException {
        Producto p = productoDAO.buscarPorId(id);
        if (p == null) {
            throw new IllegalArgumentException("Producto no encontrado: " + id);
        }
        int nuevo = p.getStock() + delta;
        p.setStock(nuevo);
        productoDAO.actualizar(p);
    }
}
