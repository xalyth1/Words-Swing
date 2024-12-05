package persistence;

import org.junit.jupiter.api.Test;
import pl.com.words.persistence.Database;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestUtil {
    static final String[] tableNames = {"Words","Definitions", "Words_Definitions","Words_Lists","Words_List_Items" };

    @Test
    public void test_dropAllTables_ShouldNotThrowException() {
        Database database = new Database(true);
        assertDoesNotThrow(() -> database.dropAllTables());
    }
}
