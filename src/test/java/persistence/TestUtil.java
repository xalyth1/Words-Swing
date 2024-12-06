package persistence;

import org.junit.jupiter.api.Test;
import pl.com.words.persistence.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtil {
    static final String[] tableNames = {"Words","Definitions", "Words_Definitions","Words_Lists","Words_List_Items" };

    @Test
    public void test_dropAllTables_ShouldNotThrowException() {
        Database database = new Database(true);
        assertDoesNotThrow(() -> database.dropAllTables());
    }


    public static boolean doesTableExist(Database database, String tableName) {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";

        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, tableName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Returns true if the table exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**@return - row with given @param headword exists in Words table */
    public static boolean rowExistsInWordsTable(Database database, String headword) {
        return database.exists(headword);
    }

    public static boolean rowExistsInWordsTable(Database database, long word_id) {
        final String query = "SELECT id, headword FROM Words WHERE id = (?)";
        boolean result;
        try (PreparedStatement pstmt = database.getConnection().prepareStatement(query)) {
            pstmt.setLong(1, word_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = true;
                } else {
                    result = false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public static boolean rowExistsInDefinitionsTable(Database database, String definition) {
        final String query = "SELECT id, definition FROM Definitions WHERE definition = (?)";
        boolean result;
        try (PreparedStatement pstmt = database.getConnection().prepareStatement(query)) {
            pstmt.setString(1, definition);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = true;
                } else {
                    result = false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    //idea: Table as parameter !
    public static boolean rowExistsInTable(Database database, String tableName, String value) {
        String columnName = switch (tableName) {
            case "Words" -> "headword";
            case "Definitions" -> "definition";
            case "Words_Definitions", "Words_Lists" -> "id";
            default -> "not completed";
            //TODO rethink this approach
            //type of value Object ?
        };
        return false;
    }


    @Test
    public void test_rowExistsInWordsTable_ShouldReturnTrueGivenExistingWordAndFalseGivenNonExisting() {
        var db = new Database(true);
        db.createSchemaIfNotExists();
        db.insertData();
        assertTrue(rowExistsInWordsTable(db, 1));
        assertTrue(rowExistsInWordsTable(db, 2));
        assertTrue(rowExistsInWordsTable(db, 3));
        assertTrue(rowExistsInWordsTable(db, 4));

        assertFalse(rowExistsInWordsTable(db, 5));
    }

    @Test
    public void test_rowExistsInDefinitionsTable_ShouldReturnTrueGivenExistingDefintionAndFalseGivenNonExisting() {
        var db = new Database(true);
        db.createSchemaIfNotExists();
        db.insertData();
        assertTrue(rowExistsInDefinitionsTable(db,"wyjątkowy"));
        assertTrue(rowExistsInDefinitionsTable(db,"niezwykły"));
        assertTrue(rowExistsInDefinitionsTable(db,"niesłychany"));
        assertTrue(rowExistsInDefinitionsTable(db,"biegły, płynny"));
        assertTrue(rowExistsInDefinitionsTable(db,"sztuka"));
        assertTrue(rowExistsInDefinitionsTable(db,"rynek"));

        assertFalse(rowExistsInDefinitionsTable(db,"samochód"));
        assertFalse(rowExistsInDefinitionsTable(db,"rower"));
        assertFalse(rowExistsInDefinitionsTable(db,"biegły"));

    }

}
