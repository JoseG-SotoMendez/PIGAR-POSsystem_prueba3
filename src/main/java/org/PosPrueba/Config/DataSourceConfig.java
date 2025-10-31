package org.PosPrueba.Config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utilidad para obtener conexiones JDBC leyendo application.properties.
 * Lanza SQLException para que los DAOs puedan declarar solo SQLException.
 */
public final class DataSourceConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = DataSourceConfig.class.getResourceAsStream("/application.properties")) {
            if (is != null) {
                PROPS.load(is);
                String driver = PROPS.getProperty("db.driver");
                if (driver != null && !driver.isBlank()) {
                    try {
                        Class.forName(driver);
                    } catch (ClassNotFoundException ignored) {
                        // driver no encontrado: el driver JDBC moderno suele auto-registrarse
                    }
                }
            } else {
                throw new ExceptionInInitializerError("No se encontró application.properties en classpath");
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Error leyendo application.properties: " + e.getMessage());
        }
    }

    private DataSourceConfig() { /* utilitaria */ }

    /**
     * Devuelve una conexión JDBC. Lanza SQLException si hay error al conectarse.
     */
    public static Connection getConnection() throws SQLException {
        String url = PROPS.getProperty("db.url");
        String user = PROPS.getProperty("db.user");
        String pass = PROPS.getProperty("db.password");
        return DriverManager.getConnection(url, user, pass);
    }
}
