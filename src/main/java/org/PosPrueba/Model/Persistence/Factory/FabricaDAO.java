package org.PosPrueba.Model.Persistence.Factory;

import org.PosPrueba.Model.Persistence.Dao.ProductoDAO;
import org.PosPrueba.Model.Persistence.impl.MySQLProductoDAO;
import org.PosPrueba.Model.Persistence.Dao.UsuarioDAO;
import org.PosPrueba.Model.Persistence.impl.MySQLUsuarioDAO;

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

}
