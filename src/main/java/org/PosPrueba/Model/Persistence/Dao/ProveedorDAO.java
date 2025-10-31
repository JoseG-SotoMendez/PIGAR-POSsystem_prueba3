package org.PosPrueba.Model.Persistence.Dao;

import org.PosPrueba.Model.Proveedor;
import java.sql.SQLException;
import java.util.List;

public interface ProveedorDAO {
    Long guardar(Proveedor proveedor) throws SQLException;
    Proveedor buscarPorId(Long id) throws SQLException;
    Proveedor buscarPorRuc(String ruc) throws SQLException;
    List<Proveedor> listarTodos() throws SQLException;
    List<Proveedor> buscarPorNombre(String nombre) throws SQLException;
    void actualizar(Proveedor proveedor) throws SQLException;
    void eliminar(Long id) throws SQLException;
}
