package persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.com.words.persistence.Database;
import pl.com.words.persistence.DatabaseException;
import pl.com.words.persistence.WordRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class InsertDataTest {

    private Database database;

    @BeforeEach
    public void setUp() {
        database = new Database(true);
        database.dropAllTables();
        database.createSchemaIfNotExists();
        database.insertData();
    }

    private boolean doesTableExist(String tableName) {
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


    @Test
    public void test_insertDefinitions_ShouldReturnProperIdsOfInsertedDefinitions() {
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

        List<String> retrievedDefinitions = new ArrayList<>();
        try (PreparedStatement pstmt = database.getConnection().prepareStatement(SQL)) {
            for (Long id : ids) {
                pstmt.setLong(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    retrievedDefinitions.add(rs.getString("definition"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(retrievedDefinitions, definitionsToAdd);
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


    //------


    @Test
    public void test_addWord_ShouldAddNewWordWithTableReferentialIntegrity() {
        WordRecord wr = new WordRecord("brilliant", Set.of("jasny", "genialny"));
        database.addWord(wr);
        WordRecord result = database.getWord(wr.headword()).orElseThrow();
        assertEquals(wr, result);
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

    @Test
    public void test_insertIntoWordsDefinitions_ShouldReturnId() {
        Optional<Long> result = database.insertIntoWordsDefinitions(5L,7L);
        assertTrue(result.isPresent());
        assertEquals(7L, result.orElseThrow());
    }

    @Test
    public void test_insertData_ShouldContainDefinedWords() {
        assertTrue(database.exists("art"));
        assertTrue(database.exists("market"));
        assertTrue(database.exists("extraordinary"));
        assertTrue(database.exists("fluent"));

        database.dropAllTables();
    }
    //------------------------------------------------------------------

    @Test
    public void test_dropAllTables_ShouldNotThrowExceptionIfNoSchema() {
        database = new Database(true);
        assertDoesNotThrow(() -> database.dropAllTables());
    }

}
