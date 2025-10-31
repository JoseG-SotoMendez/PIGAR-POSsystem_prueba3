package org.PosPrueba.Config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DataSourceConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = DataSourceConfig.class.getResourceAsStream("/application.properties")) {
            if (is != null) {
                PROPS.load(is);
                // cargar driver si fuerza (opcional)
                String driver = PROPS.getProperty("db.driver");
                if (driver != null && !driver.isBlank()) {
                    try {
                        Class.forName(driver);
                    } catch (ClassNotFoundException e) {
                        // ignorar si no está (DriverManager puede auto-cargar en versiones modernas)
                    }
                }
            } else {
                System.err.println("No se encontró application.properties en classpath.");
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError("No se pudo leer application.properties: " + e.getMessage());
        }
    }

    private DataSourceConfig() {
        // utilidad estática
    }

    public static Connection getConnection() throws SQLException {
        String url = PROPS.getProperty("db.url");
        String user = PROPS.getProperty("db.user");
        String pass = PROPS.getProperty("db.password");
        if (url == null) {
            throw new SQLException("db.url no definido en application.properties");
        }
        return DriverManager.getConnection(url, user, pass);
    }
}
