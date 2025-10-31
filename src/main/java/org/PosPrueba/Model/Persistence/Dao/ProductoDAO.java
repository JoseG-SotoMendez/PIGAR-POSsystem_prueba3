package org.PosPrueba.Model.Persistence.Dao;

import org.PosPrueba.Model.Producto;

import java.sql.SQLException;
import java.util.List;

public interface ProductoDAO {

    Producto buscarPorId(Long id) throws SQLException;

    List<Producto> listarTodos() throws SQLException;

    void actualizar(Producto producto) throws SQLException;
}
