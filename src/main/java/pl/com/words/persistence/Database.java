package pl.com.words.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class Database {

    public static void main(String[] args) {
        Database.createSchemaIfNotExists();
    }
    private static final String CREATE_DB_SQL = """
            CREATE TABLE IF NOT EXISTS Words (
                id INTEGER PRIMARY KEY,
                headword TEXT NOT NULL UNIQUE
            );
                        
            CREATE TABLE IF NOT EXISTS Definitions (
                id INTEGER PRIMARY KEY,
                definition TEXT NOT NULL UNIQUE
            );
                        
            CREATE TABLE IF NOT EXISTS Words_Definitions (
                id INTEGER PRIMARY KEY,
                word_id INTEGER,
                definition_id INTEGER,
                FOREIGN KEY (word_id) REFERENCES Words(id),
                FOREIGN KEY (definition_id) REFERENCES Definitions(id)
            );
                        
            CREATE TABLE IF NOT EXISTS Words_Lists (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL
            );
                        
                        
            CREATE TABLE IF NOT EXISTS Words_List_Items (
                words_list_id INTEGER,
                word_id INTEGER,
                PRIMARY KEY (words_list_id, word_id),
                FOREIGN KEY (words_list_id) REFERENCES Words_Lists(id),
                FOREIGN KEY (word_id) REFERENCES Words(id)
            );
            """;

    private static void createSchemaIfNotExists() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_DB_SQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(Paths.get("src/main/resources/database.properties"))) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = properties.getProperty("jdbc.url");
        return DriverManager.getConnection(url);
    }
}
