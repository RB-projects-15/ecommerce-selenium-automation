package com.qa.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Central access point for framework configuration.
 *
 * <p>Lookup order: JVM system property (e.g. {@code -Dbrowser=firefox}) first,
 * then {@code config.properties} from the classpath.
 */
public final class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private ConfigReader() {
        // utility class
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException(
                        CONFIG_FILE + " was not found on the classpath (expected in src/test/resources).");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read " + CONFIG_FILE, e);
        }
        return properties;
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = PROPERTIES.getProperty(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing or empty configuration property: " + key);
        }
        return value.trim();
    }

    public static int getInt(String key) {
        String value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Configuration property '" + key + "' must be an integer but was: " + value, e);
        }
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    /** Base URL guaranteed to end with a slash, so paths can be appended safely. */
    public static String getBaseUrl() {
        String url = get("baseUrl");
        return url.endsWith("/") ? url : url + "/";
    }
}
