
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.com.words.model.Model;
import pl.com.words.model.WordsList;

import java.util.ArrayList;
import java.util.List;
public class ModelTest {

    @Test
    public void testGetListsNames() {
        Model model = new Model();
        model.addWordsList(new WordsList("List name 1", new ArrayList<>()));
        model.addWordsList(new WordsList("List name 2", new ArrayList<>()));
        model.addWordsList(new WordsList("List name 3", new ArrayList<>()));

        List<String> actualListsNames = model.getListsNames();
        List<String> expectedListsNames = List.of("List name 1", "List name 2", "List name 3");
        Assertions.assertEquals(expectedListsNames, actualListsNames);

    }


}
