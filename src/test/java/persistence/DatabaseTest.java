package persistence;

import org.junit.jupiter.api.*;
import pl.com.words.model.Word;
import pl.com.words.persistence.Database;
import pl.com.words.persistence.DatabaseException;
import pl.com.words.persistence.WordRecord;

import javax.swing.text.html.Option;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    //private static final String DB_URL = "jdbc:sqlite:src/test/resources/WordsDatabase.db"; // Path to the words.db
    private Database database;



    @BeforeEach
    void setUp() {
        database = new Database(true);
        database.createSchemaIfNotExists();
        database.insertData();
    }

    @AfterEach
    void clear() {
        database.dropAllTables();
    }


    @Test
    public void test_addWord_ShouldAddNewWordWithTableReferentialIntegrity() {
        WordRecord wr = new WordRecord("brilliant", Set.of("jasny", "genialny"));
        database.addWord(wr);
        WordRecord result = database.getWord(wr.headword()).orElseThrow();
        assertEquals(wr, result);


    }

    @Test
    public void test_insertIntoWordsDefinitions_ShouldReturnId() {
        Optional<Long> result = database.insertIntoWordsDefinitions(5L,7L);
        assertTrue(result.isPresent());
        assertEquals(7L, result.orElseThrow());

    }

    @Test
    public void test_insertIntoWords_ShouldReturnIdOfNewlyAddedHeadword() {
        Optional<Long> id = database.insertIntoWords("independent");
        assertTrue(id.isPresent());
        assertEquals(id.orElseThrow(), 5);
    }

    @Test
    public void test_insertIntoWords_ShouldReturnIdOfExistingHeadwordInTable() {
        Optional<Long> id = database.insertIntoWords("market");

        assertTrue(id.isPresent());
        assertEquals(id.orElseThrow(), 4);
    }


    public List<Long> insert3ExemplaryDefinitions() {
        List<String> definitionsToAdd = new ArrayList<>();
        definitionsToAdd.add("samochód");
        definitionsToAdd.add("rower");
        definitionsToAdd.add("pojazd");

        List<Long> ids = null;

        try {
            ids = database.insertDefinitions(definitionsToAdd);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return ids;
    }

    @Test
    public void test_insertDefinitions_ShouldReturnProperIdsOfInsertedDefinitios() {
        List<String> definitionsToAdd = new ArrayList<>();
        definitionsToAdd.add("samochód");
        definitionsToAdd.add("rower");
        definitionsToAdd.add("pojazd");

        List<Long> ids = null;

        try {
            ids = database.insertDefinitions(definitionsToAdd);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        assertNotNull(ids);
        assertEquals(ids.size(), 3);

        String SQL = "SELECT definition FROM Definitions WHERE id = (?)";

        List<String> retriedDefinitions = new ArrayList<>();
        try (PreparedStatement pstmt = database.getConnection().prepareStatement(SQL)) {
            for (Long id : ids) {
                pstmt.setLong(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    retriedDefinitions.add(rs.getString("definition"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(retriedDefinitions, definitionsToAdd);
    }

    @Test
    public void test_insertDefinitions_ShouldReturnListOfIds() {
        List<Long> ids = insert3ExemplaryDefinitions();

        assertNotNull(ids);
        assertEquals(ids.size(), 3);
    }

    @Test
    public void test_insertDefinitions_ReturnedIdShouldPointToDefinition() {
        String definitionToBeAdded = "samochód";
        Optional<Long> opt = Optional.empty();
        try {
            opt = database.insertDefinition(definitionToBeAdded);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        assertTrue(opt.isPresent());

        String GET_DEFINITION = "SELECT definition FROM Definitions WHERE id = (?)";
        String retrieveDefinition = null;
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(GET_DEFINITION)) {
            preparedStatement.setLong(1, opt.orElseThrow());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                retrieveDefinition = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertNotNull(retrieveDefinition);
        assertEquals(retrieveDefinition, definitionToBeAdded);
    }

    @Test
    public void shouldHaveProperDefinitionsForExistingWords() {
        WordRecord extraordinary = new WordRecord("extraordinary",
                new HashSet<>(Set.of("wyjątkowy", "niezwykły", "niesłychany")));
        WordRecord art = new WordRecord("art", new HashSet<>(Set.of("sztuka")));
        WordRecord market = new WordRecord("market", new HashSet<>(Set.of("rynek")));
        WordRecord fluent = new WordRecord("fluent", new HashSet<>(Set.of("biegły, płynny")));

        Optional<WordRecord> result1 = database.getWord("extraordinary");
        Optional<WordRecord> result2 = database.getWord("art");
        Optional<WordRecord> result3 = database.getWord("market");
        Optional<WordRecord> result4 = database.getWord("fluent");

        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        assertTrue(result3.isPresent());
        assertTrue(result4.isPresent());

        assertEquals(extraordinary, result1.orElseThrow());
        assertEquals(art, result2.orElseThrow());
        assertEquals(market, result3.orElseThrow());
        assertEquals(fluent, result4.orElseThrow());
    }

    @Test
    public void shouldNotContainNotAddedWordsAfterInsertData() {
        assertFalse(database.exists("is"));
        assertFalse(database.exists("be"));
        assertFalse(database.exists("car"));

        assertFalse(database.exists("marke"));
        assertFalse(database.exists("extraordinaryy"));
    }

    @Test
    public void shouldContainWordsAfterInsertData() {
        assertTrue(database.exists("extraordinary"));
        assertTrue(database.exists("fluent"));
        assertTrue(database.exists("art"));
        assertTrue(database.exists("market"));
    }

    @Test
    public void shouldReturnTrueWhenHeadwordExists() {
        // Zakładając, że w tabeli words istnieje headword "extraordinary"
        String headword = "extraordinary";

        // Przygotowanie testowej sytuacji - headword "extraordinary" ma istnieć w bazie
        boolean result = database.exists(headword);

        // Sprawdzamy, czy metoda zwróciła true
        assertTrue(result, "The headword should exist in the database.");
    }

    @Test
    public void shouldReturnFalseWhenHeadwordDoesNotExist() {
        String headword = "elephant";
        boolean result = database.exists(headword);
        assertFalse(result, "exists(non existing word) should return false");
    }

}
