package config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    // Loads configuration properties and provides a method to access them
    private static Properties props = new Properties();
    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            props.load(fis);
        } catch (Exception e) {
            System.out.println("Could not load config.properties: " + e.getMessage());
        }
    }
    // Retrieves the value of a configuration property by key
    public static String get(String key) {
        return props.getProperty(key);
    }
}