package com._2itesting.tests.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
// Utility class to load test configuration from a properties file Need to implement but potentially after the presentation
public class TestConfig {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = TestConfig.class.getClassLoader()
                .getResourceAsStream("test-config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static String getBaseUrl() {
        return getProperty("base.url", "https://www.edgewordstraining.co.uk/demo-site");
    }

    public static String getDefaultUsername() {
        return getProperty("default.username", "luis.hueso@2.com");
    }

    public static String getDefaultPassword() {
        return getProperty("default.password", "luis.hueso");
    }
}