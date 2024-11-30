package persistence;

import org.junit.jupiter.api.*;
import pl.com.words.model.Word;
import pl.com.words.persistence.Database;
import pl.com.words.persistence.WordRecord;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
