package persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.com.words.persistence.Database;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static persistence.TestUtil.doesTableExist;
import static persistence.TestUtil.tableNames;

public class DeleteDataTest {
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
    public void test_delete_ShouldReturnFalseIfWordDoesNotExistInDB() {
        assertFalse(database.delete("nonexistingword"));
    }

    @Test
    public void test_delete_ShouldReturnTrueIfWordExistsInDB() {
        assertTrue(database.delete("extraordinary"));
    }

    @Test
    public void test_delete_ShouldNotContainWordAfterDeletion() {
        assertTrue(database.delete("market"));
        assertFalse(database.exists("market"));
    }

    @Test
    public void test_delete_CheckIntegrityEnsureAllRelationsDeleted() {
        assertTrue(database.delete("extraordinary"));

    }


}
