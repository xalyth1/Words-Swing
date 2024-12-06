package persistence;

import org.junit.jupiter.api.Test;
import pl.com.words.persistence.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static persistence.TestUtil.tableNames;
import static persistence.TestUtil.doesTableExist;


/**
 * Test class which is testing schema-related database methods
 */
public class SchemaTest {
    private Database database;

    //private boolean doesTableExist(String tableName) {
    //    String query = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
    //
    //    try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
    //        statement.setString(1, tableName);
    //        try (ResultSet resultSet = statement.executeQuery()) {
    //            return resultSet.next(); // Returns true if the table exists
    //        }
    //    } catch (SQLException e) {
    //        e.printStackTrace();
    //    }
    //    return false;
    //}

    @Test
    public void test_SchemaCreationAndDeletion() {
        database = new Database(true);
        for (String tableName : tableNames) {
            assertFalse(doesTableExist(database, tableName));
        }
        database.createSchemaIfNotExists();
        for (String tableName : tableNames) {
            assertTrue(doesTableExist(database, tableName));
        }
        database.dropAllTables();
        for (String tableName : tableNames) {
            assertFalse(doesTableExist(database, tableName));
        }
    }

    @Test
    public void test_createSchemaIfNotExists_ShouldCreateDBSchema() {
        database = new Database(true);
        database.dropAllTables();
        database.createSchemaIfNotExists();
        for (String tableName : tableNames) {
            assertTrue(doesTableExist(database, tableName));
        }
        database.dropAllTables();
    }

    @Test
    public void test_Database_NewlyCreatedDatabaseObjectShouldNotContainSchema() {
        database = new Database(true);
        for (String tableName : tableNames) {
            assertFalse(doesTableExist(database, tableName));
        }
    }
}
