package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigFileReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = ConfigFileReader.class.getClassLoader().getResourceAsStream("configs/config.properties")) {
            if (inputStream == null) {
                throw new FileNotFoundException(("Property file not found in the class path."));
            }
            properties.load(inputStream);
            System.out.println("property file loaded successfully");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config properties", e);
        }
    }
    public static String get(String key){
        return properties.getProperty(key);
    }

}
