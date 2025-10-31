package org.PosPrueba.Model.Service;

import org.PosPrueba.Model.Usuario;
import org.PosPrueba.Model.Persistence.Dao.UsuarioDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ServicioUsuario {
    private final UsuarioDAO usuarioDAO;
    private Usuario usuarioActual;

    public ServicioUsuario(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = Objects.requireNonNull(usuarioDAO);
    }

    public Usuario autenticar(String username, String password) throws SQLException {
        Usuario usuario = usuarioDAO.autenticar(username, password);
        if (usuario != null) {
            this.usuarioActual = usuario;
        }
        return usuario;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public boolean tienePermiso(Usuario.Rol... rolesPermitidos) {
        if (usuarioActual == null) return false;
        for (Usuario.Rol rol : rolesPermitidos) {
            if (usuarioActual.getRol() == rol) {
                return true;
            }
        }
        return false;
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        return usuarioDAO.listarTodos();
    }

    public Long guardarUsuario(Usuario usuario) throws SQLException {
        return usuarioDAO.guardar(usuario);
    }

    public void actualizarUsuario(Usuario usuario) throws SQLException {
        usuarioDAO.actualizar(usuario);
    }

    public void eliminarUsuario(Long id) throws SQLException {
        usuarioDAO.eliminar(id);
    }
}
