package pl.com.words.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigLoader {
    //public static void main(String[] args) {
    //    ConfigLoader configLoader = new ConfigLoader();
    //    String x = configLoader.getProperty("jdbc.url");
    //    System.out.println(x);
    //}

    private static ConfigLoader configLoader = new ConfigLoader();

    public static ConfigLoader getInstance(){
        return configLoader;
    }

    private Properties properties = new Properties();

    private ConfigLoader() {
        String profile = System.getProperty("profile.env", "development"); // development as default value
        String propertiesFile = String.format("/application-%s.properties", profile);
        try (InputStream input = getClass().getResourceAsStream(propertiesFile)) {
            if (input == null) {
                throw new RuntimeException("Nie znaleziono pliku właściwości: " + propertiesFile);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas ładowania pliku właściwości: " + propertiesFile, e);
        }
        this.createProductionDatabasePath();
    }
    private void createProductionDatabasePath() {
        String os = System.getProperty("os.name").toLowerCase();
        String dbPath;
        if (os.contains("win")) {
            dbPath = "C:\\ProgramData\\WordsVocabularyLearning\\WordsDatabase.db";
        } else {
            dbPath = "/var/lib/WordsVocabularyLearning/WordsDatabase.db";
        }
        String dbPathPlaceholder = properties.getProperty("jdbc.url");
        if (dbPathPlaceholder != null && dbPathPlaceholder.equals("{os-specific-path}")) {
            properties.setProperty("database.path", dbPath);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
