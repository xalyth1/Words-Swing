package pl.com.words.persistence;

import pl.com.words.configuration.ConfigLoader;
import pl.com.words.model.Word;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final String DROP_ALL_TABLES = """
            DROP TABLE Words_List_Items;
            DROP TABLE Words_Lists;
            DROP TABLE Words_Definitions;
            DROP TABLE Words;
            DROP TABLE Definitions;
            """;

    private final String INSERT_DATA = """
            INSERT INTO Words VALUES (1, "extraordinary");
            INSERT INTO Words VALUES (2, "fluent");
            INSERT INTO Words VALUES (3, "art");
            INSERT INTO Words VALUES (4, "market");
            INSERT INTO Definitions VALUES (1, "wyjątkowy");
            INSERT INTO Definitions VALUES (2, "niezwykły");
            INSERT INTO Definitions VALUES (3, "niesłychany");
            INSERT INTO Definitions VALUES (4, "biegły, płynny");
            INSERT INTO Definitions VALUES (5, "sztuka");
            INSERT INTO Definitions VALUES (6, "rynek");
            INSERT INTO Words_Definitions VALUES(1,1,1);
            INSERT INTO Words_Definitions VALUES(2,1,2);
            INSERT INTO Words_Definitions VALUES(3,1,3);
            INSERT INTO Words_Definitions VALUES(4,2,4);
            INSERT INTO Words_Definitions VALUES(5,3,5);
            INSERT INTO Words_Definitions VALUES(6,4,6);
                        
                        
            INSERT INTO Words_Lists VALUES (1, "initial list");
            INSERT INTO Words_List_Items VALUES (1, 1);
            INSERT INTO Words_List_Items VALUES (1, 3);
                        
            INSERT INTO Words_Lists VALUES (2, "second list");
            INSERT INTO Words_List_Items VALUES (2, 2);
            INSERT INTO Words_List_Items VALUES (2, 4);
            """;

    public Database() {
        this.connection = this.createConnection();
    }

    public Database(boolean createSchema) {
        this();
        if (createSchema) {
            this.createSchemaIfNotExists();
            this.insertData();
        }



    }

    private void insertData() {
        try (PreparedStatement pstmt = this.connection.prepareStatement(INSERT_DATA)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void queryDatabase(String query) {
        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            try(ResultSet rs = pstmt.executeQuery()){

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /*
    addWord(Word word)
    if (word.headrod exists in database) {
        do nothing
    } else {
        // headword does not exist
        database_word_id = insertIntoWords(headword)
        insert definitions(word.definitions, database_word_id)

    }

     */

    //check if given headword exists in database
    public boolean exists(String headword) {
        String query = "SELECT headword FROM Words WHERE headword = (?)";
        boolean result = false;
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, headword);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean addWord(Word word) {
        String INSERT_WORD_SQL = """
                INSERT OR IGNORE INTO Words(headword)
                VALUES (?);
                """;

        String INSERT_DEFINITION_SQL = """
                INSERT OR IGNORE INTO Definitions(definition)
                VALUES(?);
                """;
        String INSERT_WORDS_DEFINITIONS_SQL;

        String headword = word.getHeadword();

        if (this.exists(headword)) {
            return false;
        }


        try (PreparedStatement insertIntoWords = connection.prepareStatement(INSERT_WORD_SQL);
             PreparedStatement insertDefinition = connection.prepareStatement(INSERT_DEFINITION_SQL)){
            //Insert into words
            insertIntoWords.setString(1, headword);
            int updatedRows = insertIntoWords.executeUpdate();
            if (updatedRows != 0) {
                //insert all definitions
                insertDefinitions(word.getDefinitions());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     *
     * @param definitions - list of dictionary definitions to insert into Definitions Table
     * @return List of inserted rows IDs database Definitions Table
     */
    private List<Integer> insertDefinitions(List<String> definitions) {
        List<Integer> ids = new ArrayList<>();

        String INSERT_DEFINITION_SQL = """
                INSERT OR IGNORE INTO Definitions(definition)
                VALUES(?);
                """;

        String SELECT_DEFINITION = """
                SELECT id FROM Definitions WHERE definition = (?);
                """;
        try (PreparedStatement insertAllDefinitions = connection.prepareStatement(INSERT_DEFINITION_SQL);
             PreparedStatement selectDefinition = connection.prepareStatement(SELECT_DEFINITION)) {
            for (String definition : definitions) {
                insertAllDefinitions.setString(1, definition);
                int updatedRows = insertAllDefinitions.executeUpdate();
                if (updatedRows == 0) {
                    selectDefinition.setString(1, definition);
                    ResultSet rs = selectDefinition.executeQuery();
                    if (rs.next()) {
                        //TODO

                    }
                } else {

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Optional<Long> insertDefinition(String definition) throws DatabaseException {
        Optional<Long> id = Optional.empty();
        String INSERT_DEFINITION_SQL = """
                INSERT OR IGNORE INTO Definitions(definition)
                VALUES(?);
                """;
        String SELECT_DEFINITION = """
                SELECT id FROM Definitions WHERE definition = (?);
                """;
        try (PreparedStatement insertDefinitionPStmt = connection.prepareStatement(INSERT_DEFINITION_SQL);
             PreparedStatement selectDefinitionPStmt = connection.prepareStatement(SELECT_DEFINITION)) {
            insertDefinitionPStmt.setString(1, definition);
            int updatedRows = insertDefinitionPStmt.executeUpdate();
            if (updatedRows == 0) { //
                selectDefinitionPStmt.setString(1, definition);
                ResultSet rs = selectDefinitionPStmt.executeQuery();
                if (rs.next()) {
                    long defId = rs.getInt("id");
                    id = Optional.of(defId);
                    if (rs.next()) {
                        // Result set has more than 1 value - Unexpected database consistency problem
                        throw new DatabaseException("Unexpected database behavior. ResultSet has more than one value");
                    }
                }
            } else {
                ResultSet generatedKeys = insertDefinitionPStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long generatedId = generatedKeys.getLong(1);
                    id = Optional.of(generatedId);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
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

     private Connection createConnection() {
        String url = ConfigLoader.getInstance().getProperty("jdbc.url");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Creating connection to database not succeded!");
        }
        return connection;
    }

    private Connection createConnection(boolean testMode) {
        if (!testMode) {
            return this.createConnection();
        } else {

        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}
