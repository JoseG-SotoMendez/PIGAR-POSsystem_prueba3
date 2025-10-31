package org.PosPrueba.Config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

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
                    } catch (ClassNotFoundException ignored) {}
                }
            } else {
                System.err.println("application.properties no encontrado en classpath");
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DataSourceConfig() {}

    /**
     * Devuelve una conexión JDBC usando los parámetros de application.properties
     * db.url, db.user, db.password
     */
    public static Connection getConnection() throws Exception {
        String url = PROPS.getProperty("db.url");
        String user = PROPS.getProperty("db.user");
        String pass = PROPS.getProperty("db.password");
        return DriverManager.getConnection(url, user, pass);
    }
}
