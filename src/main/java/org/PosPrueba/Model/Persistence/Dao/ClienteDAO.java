package org.PosPrueba.Model.Persistence.Dao;

import org.PosPrueba.Model.Cliente;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    Long guardar(Cliente cliente) throws SQLException;
    Cliente buscarPorId(Long id) throws SQLException;
    Cliente buscarPorDocumento(String documento) throws SQLException;
    List<Cliente> listarTodos() throws SQLException;
    List<Cliente> buscarPorNombre(String nombre) throws SQLException;
    void actualizar(Cliente cliente) throws SQLException;
    void eliminar(Long id) throws SQLException;
}
