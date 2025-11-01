package org.PosPrueba.Model.Persistence.Factory;

import org.PosPrueba.Model.Persistence.Dao.ProductoDAO;
import org.PosPrueba.Model.Persistence.impl.MySQLProductoDAO;
import org.PosPrueba.Model.Persistence.Dao.UsuarioDAO;
import org.PosPrueba.Model.Persistence.impl.MySQLUsuarioDAO;
import org.PosPrueba.Model.Persistence.Dao.ClienteDAO;
import org.PosPrueba.Model.Persistence.impl.MySQLClienteDAO;
import org.PosPrueba.Model.Persistence.Dao.VentaDAO;
import org.PosPrueba.Model.Persistence.impl.MySQLVentaDAO;
import org.PosPrueba.Model.Persistence.Dao.ProveedorDAO;
import org.PosPrueba.Model.Persistence.impl.MySQLProveedorDAO;

/**
 * Fábrica simple: por ahora solo crea DAOs MySQL.
 * Si más adelante quieres varias implementaciones, amplía esta clase.
 */
public final class FabricaDAO {

    private FabricaDAO() {
        // utilitaria
    }

    public static ProductoDAO crearProductoDAO() {
        // MySQLProductoDAO usa DataSourceConfig internamente
        return new MySQLProductoDAO();
    }

    public static UsuarioDAO crearUsuarioDAO() {
        return new MySQLUsuarioDAO();
    }

    public static ClienteDAO crearClienteDAO() {
        return new MySQLClienteDAO();
    }

    public static VentaDAO crearVentaDAO() {
        return new MySQLVentaDAO();
    }

    public static ProveedorDAO crearProveedorDAO() {
        return new MySQLProveedorDAO();
    }

}
