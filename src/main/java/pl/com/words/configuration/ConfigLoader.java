package pl.com.words.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    public static void main(String[] args) {
        ConfigLoader configLoader = new ConfigLoader();
        String x = configLoader.getProperty("jdbc.url");
        System.out.println(x);
    }
    private Properties properties = new Properties();

    public ConfigLoader() {
        // development here is default value
        String profile = System.getProperty("profile.env", "development");
        String propertiesFile =
                String.format("/application-%s.properties", profile);
        try (InputStream input = getClass().getResourceAsStream(propertiesFile)) {
            if (input == null) {
                throw new RuntimeException("Nie znaleziono pliku właściwości: " + propertiesFile);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas ładowania pliku właściwości: " + propertiesFile, e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
