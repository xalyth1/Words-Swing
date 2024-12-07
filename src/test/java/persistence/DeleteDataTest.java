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
        //1. Words Table
        assertFalse(database.exists("extraordinary"));

        //2. Definitions Table
        //Definitions related to given headword should be IN database due to business rules
        //(further insertion of new words with similar meanings)
        assertTrue(TestUtil.rowExistsInDefinitionsTable(database, "wyjątkowy"));
        assertTrue(TestUtil.rowExistsInDefinitionsTable(database, "niezwykły"));
        assertTrue(TestUtil.rowExistsInDefinitionsTable(database, "niesłychany"));

        //Rest of the definitions should be in DB
        assertTrue(TestUtil.rowExistsInDefinitionsTable(database, "biegły, płynny"));
        assertTrue(TestUtil.rowExistsInDefinitionsTable(database, "sztuka"));
        assertTrue(TestUtil.rowExistsInDefinitionsTable(database, "rynek"));

        //3. Words_Definitions Table
        //Analogously, in Words_Definitions table, rows related to headword should not exist
        assertFalse(TestUtil.rowExistsInWordsDefinitionsTable(database,1));
        assertFalse(TestUtil.rowExistsInWordsDefinitionsTable(database,2));
        assertFalse(TestUtil.rowExistsInWordsDefinitionsTable(database,3));

        //Rest of the Words_Definitions table should be there
        assertTrue(TestUtil.rowExistsInWordsDefinitionsTable(database,4));
        assertTrue(TestUtil.rowExistsInWordsDefinitionsTable(database,5));
        assertTrue(TestUtil.rowExistsInWordsDefinitionsTable(database,6));

        //4. Words_List_Items Table
        //Word to be deleted should be also deleted from List
        assertFalse(TestUtil.rowExistsInWordListItemsTable(database, 1,1));

        //Rest of words in all Lists should be unchanged
        assertTrue(TestUtil.rowExistsInWordListItemsTable(database, 1,3));
        assertTrue(TestUtil.rowExistsInWordListItemsTable(database, 2,2));
        assertTrue(TestUtil.rowExistsInWordListItemsTable(database, 2,4));

    }


}
