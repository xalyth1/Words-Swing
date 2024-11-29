package persistence;

import org.junit.jupiter.api.*;
import pl.com.words.persistence.Database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
