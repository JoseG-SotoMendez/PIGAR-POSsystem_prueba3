package org.PosPrueba.Model.Service;

import org.PosPrueba.Model.Producto;
import org.PosPrueba.Model.Persistence.Dao.ProductoDAO;

import java.util.List;

public class ServicioProducto {

    private final ProductoDAO productoDAO;

    public ServicioProducto(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    public List<Producto> listarProductos() throws Exception {
        return productoDAO.listarTodos();
    }

    public Producto obtenerProducto(Long id) throws Exception {
        return productoDAO.buscarPorId(id);
    }

    public void actualizarStock(Long id, int delta) throws Exception {
        Producto p = productoDAO.buscarPorId(id);
        if (p == null) {
            throw new IllegalArgumentException("Producto no existe: " + id);
        }
        p.setStock(p.getStock() + delta);
        productoDAO.actualizar(p);
    }
}
