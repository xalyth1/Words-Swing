package pl.com.words.persistence;

import pl.com.words.configuration.ConfigLoader;
import pl.com.words.model.Word;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class Database {

    public static void main(String[] args) {
        Database database = new Database();
        database.createSchemaIfNotExists();

        Word word = new Word("heat", List.of("upał"));

        //database.addWord(word);
        database.delete(1);

    }


    private Connection connection = null;

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

    Database() {
        this.connection = this.getConnection();
        this.createSchemaIfNotExists();
    }

    private boolean addWord(Word word) {
        String INSERT_WORD_SQL = """
                INSERT INTO Words(headword)
                VALUES (?);
                """;

        String INSERT_DEFINITION_SQL;
        String INSERT_WORDS_DEFINITIONS_SQL;

        String headword = word.getHeadword();


        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_WORD_SQL)){
            preparedStatement.setString(1, headword);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean delete(int wordId) {
        //1. delete from Word_Definitions table
        String DELETE_FROM_WORD_DEFINITIONS = """
                DELETE FROM Words_Definitions
                WHERE Words_Definitions.word_id = ?;
                """;

        //2. delete from Word_List_Items table
        String DELETE_FROM_WORD_LIST_ITEMS = """
                DELETE FROM Words_List_Items
                WHERE Words_List_Items.word_id = ?;
                """;

        //3. delete from Words table
        String DELETE_WORD_BY_ID = """
                DELETE FROM Words
                WHERE Words.id = ?;
                """;

        try (PreparedStatement deleteFromWordDefinitions = connection.prepareStatement(DELETE_FROM_WORD_DEFINITIONS);
             PreparedStatement deleteFromWordListItems = connection.prepareStatement(DELETE_FROM_WORD_LIST_ITEMS);
             PreparedStatement deleteWord = connection.prepareStatement(DELETE_WORD_BY_ID)) {
            connection.setAutoCommit(false);

            deleteFromWordDefinitions.setInt(1, wordId);
            deleteFromWordDefinitions.executeUpdate();

            deleteFromWordListItems.setInt(1, wordId);
            deleteFromWordListItems.executeUpdate();

            deleteWord.setInt(1, wordId);
            deleteWord.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }



    //private boolean deleteWord(Word word) {
    //    String DELETE_WORD_SQL = """
    //            DELETE FROM Words
    //            WHERE Words.headword = ?;
    //            """;
    //
    //    String headword = word.getHeadword();
    //
    //
    //    try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_WORD_SQL)){
    //        preparedStatement.setString(1, headword);
    //        preparedStatement.executeUpdate();
    //
    //    } catch (SQLException e) {
    //        e.printStackTrace();
    //    }
    //    return true;
    //}

    private void createSchemaIfNotExists() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_DB_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        String url = ConfigLoader.getInstance().getProperty("jdbc.url");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Creating connection to database not succeded!");
        }
        return connection;
    }
}
