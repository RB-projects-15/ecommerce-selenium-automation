package com.qa.automation.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (FileInputStream file = new FileInputStream(
                "src/test/resources/config.properties")) {

            properties.load(file);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to load config.properties. "
                            + "Check that the file exists at "
                            + "src/test/resources/config.properties.",
                    e);
        }
    }

    private ConfigReader() {
        // Prevent object creation
    }

    public static String getProperty(String key) {

        String value = properties.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing or empty configuration property: " + key);
        }

        return value.trim();
    }
}
