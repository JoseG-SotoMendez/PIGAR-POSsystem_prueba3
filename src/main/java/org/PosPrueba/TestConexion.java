package org.PosPrueba;

import org.PosPrueba.Config.DataSourceConfig;
import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        try (Connection c = DataSourceConfig.getConnection()) {
            System.out.println("Conexión OK: " + c.getMetaData().getURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
