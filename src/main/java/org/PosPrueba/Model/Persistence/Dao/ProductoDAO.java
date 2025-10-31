package org.PosPrueba.Model.Persistence.Dao;

import org.PosPrueba.Model.Producto;

import java.util.List;

public interface ProductoDAO {

    Producto buscarPorId(Long id) throws Exception;

    List<Producto> listarTodos() throws Exception;

    void actualizar(Producto producto) throws Exception;
}
