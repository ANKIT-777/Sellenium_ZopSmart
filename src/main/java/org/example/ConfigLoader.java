package org.example;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private Properties properties;

    public ConfigLoader() {
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties = new Properties();
            if (input != null) {
                properties.load(input);
            } else {
                throw new RuntimeException("config.properties not found in the classpath");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in this code is " + e.getMessage());
        }
    }

    public String getBrowser() {
        return properties.getProperty("browser");
    }

    public String getExcel() {
        return properties.getProperty("Excel");
    }
}
