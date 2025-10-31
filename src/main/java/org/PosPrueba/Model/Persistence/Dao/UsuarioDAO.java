package org.PosPrueba.Model.Persistence.Dao;

import org.PosPrueba.Model.Usuario;
import java.sql.SQLException;
import java.util.List;

public interface UsuarioDAO {
    Long guardar(Usuario usuario) throws SQLException;
    Usuario buscarPorId(Long id) throws SQLException;
    Usuario buscarPorUsername(String username) throws SQLException;
    Usuario autenticar(String username, String password) throws SQLException;
    List<Usuario> listarTodos() throws SQLException;
    void actualizar(Usuario usuario) throws SQLException;
    void eliminar(Long id) throws SQLException;
}
