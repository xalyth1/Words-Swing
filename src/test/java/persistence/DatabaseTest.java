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
    void setUp() throws SQLException {
        database = new Database();


    }

    @Test
    public void testExists_whenHeadwordExists_returnsTrue() {
        // Zakładając, że w tabeli words istnieje headword "extraordinary"
        String headword = "extraordinary";

        // Przygotowanie testowej sytuacji - headword "extraordinary" ma istnieć w bazie
        boolean result = database.exists(headword);

        // Sprawdzamy, czy metoda zwróciła true
        assertTrue(result, "The headword should exist in the database.");
    }

    @Test
    public void testExists_whenHeadwordDoesNotExist_returnsFalse() {
        String headword = "elephant";
        boolean result = database.exists(headword);
        assertFalse(result, "exists(non existing word) should return false");
    }

}
