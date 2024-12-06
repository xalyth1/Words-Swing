package pl.com.words.persistence;

import pl.com.words.configuration.ConfigLoader;

import java.sql.*;
import java.util.*;

public class Database {

    public static void main(String[] args) {
        Database database = new Database(true);
        database.createSchemaIfNotExists();
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
            DROP TABLE IF EXISTS Words_List_Items;
            DROP TABLE IF EXISTS Words_Lists;
            DROP TABLE IF EXISTS Words_Definitions;
            DROP TABLE IF EXISTS Words;
            DROP TABLE IF EXISTS Definitions;""";

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
            INSERT INTO Words_List_Items VALUES (2, 4);""";

    public Database() {
        this(false);
    }

    public Database(boolean testMode) {
        this.connection = this.createConnection(testMode);
        if (!testMode) {
            this.createSchemaIfNotExists();
            this.insertData();
        }
    }

    public void insertData() {
        String[] queries = INSERT_DATA.split(";");
        String currentQuery="";
        try (Statement statement = this.connection.createStatement()) {
            for (String query : queries) {
                currentQuery = query;
                statement.execute(query);

            }
        } catch (SQLException e) {
            System.out.println("Exception while executing currentQuery: " + currentQuery);
            e.printStackTrace();
        }
    }

    public void dropAllTables() {
        boolean result = true;
        String[] queries = DROP_ALL_TABLES.split(";");
        try (Statement statement = this.connection.createStatement()) {
            for (String query : queries) {
                statement.execute(query.trim()+";");
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method checks if given headword exists in database
     */
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

    private static final String GET_WORD_WITH_DEFINITIONS_QUERY = """
            SELECT Definitions.definition FROM Words
            INNER JOIN Words_Definitions
            ON Words.id = Words_Definitions.word_id
            INNER JOIN Definitions
            ON Words_Definitions.definition_id = Definitions.id
            WHERE Words.headword = (?);""";

    public Optional<WordRecord> getWord(String headword) {
        Optional<WordRecord> result = Optional.empty();
        if (this.exists(headword)) {
            WordRecord retrievedWord;
            Set<String> definitions = retrieveDefinitions(headword);
            retrievedWord = new WordRecord(headword, definitions);
            result = Optional.of(retrievedWord);
        }
        return result;
    }

    private Set<String> retrieveDefinitions(String headword) {
        Set<String> definitionsSet = new HashSet<>();
        try (PreparedStatement getDefinitionsStatement = connection.prepareStatement(GET_WORD_WITH_DEFINITIONS_QUERY)) {
            getDefinitionsStatement.setString(1, headword);
            ResultSet rs = getDefinitionsStatement.executeQuery();
            while (rs.next()) {
                definitionsSet.add(rs.getString("definition"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return definitionsSet;
    }

    private static final String INSERT_DEFINITIONS_SQL = """
                INSERT OR IGNORE INTO Definitions(definition)
                VALUES(?);
                """;

    public boolean addWord(WordRecord word) {
        boolean operationResult;
        if (this.exists(word.headword())) {
            return true;
        }
        String headword = word.headword();
        try {
            connection.setAutoCommit(false);
            //1. Insert Definitions
            List<Long> definitions_ids = this.insertDefinitions(word.definitions().stream().toList());
            //2. Insert into Words
            Long word_id = this.insertIntoWords(headword).orElseThrow();
            //3. Insert into WordsDefinitions
            for(Long definition_id : definitions_ids) {
                this.insertIntoWordsDefinitions(word_id, definition_id);
            }
            connection.commit();
            operationResult = true;
        } catch (SQLException | DatabaseException e) {
            try {
                connection.rollback();
                System.out.println("Transaction rolled back due to an error.");
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            e.printStackTrace();
            operationResult = false;
        }
        return operationResult;
    }

    public Optional<Long> insertIntoWordsDefinitions(Long word_id, Long definition_id) {
        Optional<Long> id = Optional.empty();
        final String INSERT_WORDS_DEFINITIONS_SQL = """
                INSERT OR IGNORE INTO Words_Definitions(word_id, definition_id)
                VALUES(?, ?) RETURNING id;
                """;
        try (PreparedStatement insertIntoWords = connection.prepareStatement(INSERT_WORDS_DEFINITIONS_SQL)) {
            insertIntoWords.setLong(1, word_id);
            insertIntoWords.setLong(2, definition_id);
            ResultSet rs = insertIntoWords.executeQuery();
            if (rs.next()) {
                id = Optional.of(rs.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public Optional<Long> insertIntoWords(String headword) {
        Optional<Long> id = Optional.empty();
        final String INSERT_WORD_SQL = """
                INSERT OR IGNORE INTO Words(headword)
                VALUES (?);
                """;
        final String SELECT_FROM_WORDS = """
                SELECT id FROM Words WHERE headword = (?)""";
        try (PreparedStatement insertIntoWords = connection.prepareStatement(INSERT_WORD_SQL);
             PreparedStatement selectId = connection.prepareStatement(SELECT_FROM_WORDS)) {
            insertIntoWords.setString(1, headword);
            insertIntoWords.executeUpdate();

            selectId.setString(1, headword);
            ResultSet rs = selectId.executeQuery();
            if (rs.next()) {
                id = Optional.of(rs.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     *
     * @param definitions - list of dictionary definitions to insert into Definitions Table
     * @return List of inserted rows IDs database Definitions Table
     */
    public List<Long> insertDefinitions(List<String> definitions) throws DatabaseException {
        List<Long> ids = new ArrayList<>();

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
                Optional<Long> id = getIdOfInsertedDefinition(updatedRows, insertAllDefinitions, selectDefinition, definition);
                if (id.isPresent()) {
                    ids.add(id.orElseThrow());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public Optional<Long> insertDefinition(String definition) throws DatabaseException {
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

    private Optional<Long> getIdOfInsertedDefinition(int updatedRows,
                                             PreparedStatement insertDefinitionPStmt,
                                             PreparedStatement selectDefinitionPStmt,
                                             String definition) throws SQLException, DatabaseException {
        Optional<Long> id = Optional.empty();
        if (updatedRows == 0) { // definition already exist
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
        return id;
    }

    /**
     *
     * Delete word and all related data(definitions, association table data)
     *
     */
    public boolean delete(String headword) {
        if (!this.exists(headword)) {
            return false;
        }
        boolean operationResult = false;

        final String GET_ID = """
                SELECT id From Words WHERE headword = (?)""";

        try (PreparedStatement getIdStmt = connection.prepareStatement(GET_ID)) {
            getIdStmt.setString(1, headword);
            ResultSet rs = getIdStmt.executeQuery();
            if (rs.next()) {
                long id = rs.getLong("id");
                operationResult = this.delete(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operationResult;
    }

    private boolean delete(long wordId) {
        boolean operationResult;
        //1. Delete from Word_Definitions table
        final String DELETE_FROM_WORD_DEFINITIONS = """
                DELETE FROM Words_Definitions
                WHERE Words_Definitions.word_id = ?;
                """;

        //2. Delete from Word_List_Items table
        final String DELETE_FROM_WORD_LIST_ITEMS = """
                DELETE FROM Words_List_Items
                WHERE Words_List_Items.word_id = ?;
                """;

        //3. Delete from Words table
        final String DELETE_WORD_BY_ID = """
                DELETE FROM Words
                WHERE Words.id = ?;
                """;

        try (PreparedStatement deleteFromWordDefinitions = connection.prepareStatement(DELETE_FROM_WORD_DEFINITIONS);
             PreparedStatement deleteFromWordListItems = connection.prepareStatement(DELETE_FROM_WORD_LIST_ITEMS);
             PreparedStatement deleteWord = connection.prepareStatement(DELETE_WORD_BY_ID)) {
            connection.setAutoCommit(false);

            //1. Delete from Word_Definitions table
            deleteFromWordDefinitions.setLong(1, wordId);
            deleteFromWordDefinitions.executeUpdate();

            //2. Delete from Word_List_Items table
            deleteFromWordListItems.setLong(1, wordId);
            deleteFromWordListItems.executeUpdate();

            //3. Delete from Words table
            deleteWord.setLong(1, wordId);
            deleteWord.executeUpdate();

            connection.commit();
            operationResult = true;
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Transaction rolled back due to an error.");
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            e.printStackTrace();
            operationResult = false;
        }
        return operationResult;
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

    public void createSchemaIfNotExists() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_DB_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     private Connection createConnection() {
        String url = ConfigLoader.getInstance().getProperty("jdbc.url");
        return this.createConnection(url);
    }

    private Connection createConnection(String url) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Creating connection to database not succeded!");
        }
        return connection;
    }

    public Connection createConnection(boolean testMode) {
        if (!testMode) {
            return this.createConnection();
        } else {
            //String url = ConfigLoader.getInstance().getProperty("test.jdbc.url");
            String url = "jdbc:sqlite::memory:";
            return this.createConnection(url);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}
