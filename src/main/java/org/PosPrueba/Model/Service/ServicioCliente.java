package org.PosPrueba.Model.Service;

import org.PosPrueba.Model.Cliente;
import org.PosPrueba.Model.Persistence.Dao.ClienteDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ServicioCliente {
    private final ClienteDAO clienteDAO;

    public ServicioCliente(ClienteDAO clienteDAO) {
        this.clienteDAO = Objects.requireNonNull(clienteDAO);
    }

    public Long guardarCliente(Cliente cliente) throws SQLException {
        return clienteDAO.guardar(cliente);
    }

    public Cliente obtenerCliente(Long id) throws SQLException {
        return clienteDAO.buscarPorId(id);
    }

    public Cliente buscarPorDocumento(String documento) throws SQLException {
        return clienteDAO.buscarPorDocumento(documento);
    }

    public List<Cliente> listarClientes() throws SQLException {
        return clienteDAO.listarTodos();
    }

    public List<Cliente> buscarPorNombre(String nombre) throws SQLException {
        return clienteDAO.buscarPorNombre(nombre);
    }

    public void actualizarCliente(Cliente cliente) throws SQLException {
        clienteDAO.actualizar(cliente);
    }

    public void eliminarCliente(Long id) throws SQLException {
        clienteDAO.eliminar(id);
    }
}
