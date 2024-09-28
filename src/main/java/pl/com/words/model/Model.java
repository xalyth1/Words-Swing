package pl.com.words.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.com.words.api.WordsServiceApiClient;

public class Model {
    private List<WordsList> listOfWordsList;
    private WordsList currentList;

    public static boolean USE_SELECTION_MODE = true;

    public final WordsServiceApiClient api = new WordsServiceApiClient();

    public Model() {
        this.listOfWordsList = new ArrayList<>();
        //this.listOfWordsList.add(this.loadWordsFromCSV());

        this.listOfWordsList.add(createInitialListWithExemplaryWords());
        this.currentList = listOfWordsList.get(0);
    }

    private WordsList createInitialListWithExemplaryWords() {
        List<String> headwords = new ArrayList<>();

        try {
            headwords = Files.readAllLines(Paths.get("src/main/resources/initialList.txt"));
        } catch (IOException e) {
            System.out.println("Program error: reading exemplary words from initialList.txt file failed.");
            e.printStackTrace();
        }

        List<Word> wordObjects = new ArrayList<>();
        headwords.stream()
                .forEach(headword -> {
                    Word word = this.createWordObject(headword, api.getDefinitions(headword));
                    wordObjects.add(word);
                });

        WordsList wl = new WordsList("Initial List", wordObjects);
        return  wl;
    }

    private Word createWordObject(String headword, String rawDefinitions) {
        ObjectMapper objectMapper = new ObjectMapper();
        Word w = null;
        try {
            List<String> list =
                    objectMapper.readValue(rawDefinitions, new TypeReference<ArrayList<String>>() {
            });
            w = new Word(headword, list);
        } catch (JsonProcessingException e) {
            System.out.println("Error. Cannot parse definitions and create Word object");
        }
        return w;
    }

    public Word get(String wordStr) {
        Optional<Word> opt = currentList.getList().stream().filter(w -> w.getHeadword().equals(wordStr)).findFirst();
        return opt.get();
    }

    /**
     *
     * @param headword - word to be searched
     * @param listId - id of list on which the word will be searched
     * @return
     */
    public Word search(String headword, int listId) {
        WordsList list = this.listOfWordsList
                .stream()
                .filter(wl -> wl.getId() == listId)
                .distinct()
                .findFirst()
                .get();


        Optional<Word> opt = list.getList().stream()
                .filter(w -> w.getHeadword().equals(headword))
                .findFirst();
        return opt.get();
    }

    public void addWordsList(WordsList wordsList) {
        listOfWordsList.add(wordsList);
    }

    public void deleteList(WordsList wordList) {
        listOfWordsList.remove(wordList);
    }

    public WordsList getList(String listName) throws Exception {
        Optional<WordsList> list =
            listOfWordsList.stream()
                .filter(wl -> wl.getListName().equals(listName))
                .findFirst();
        if (list.isPresent()) {
            return list.get();
        } else {
            throw new Exception("There is no such list '" + listName + "' in the model!");
        }
    }

    public List<String> getListsNames() {
        List<String> names = listOfWordsList
                .stream()
                .map(e -> e.getListName())
                .toList();
        return names;
    }

//    private WordsList loadWordsFromCSV() {
//        List<Word> words = new ArrayList<>();
//        try {
//            ClassLoader classLoader = getClass().getClassLoader();
//            File myObj = new File(classLoader.getResource("mock.txt").getFile());
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//                String data = myReader.nextLine();
//                var splitted = data.split(",");
//                words.add(new Word(splitted[0], splitted[1]));
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//        WordsList initial = new WordsList("Initial List", words);
//        return initial;
//    }

    public WordsList getCurrentList() {
        return currentList;
    }

    public void setCurrentList(WordsList currentList) {
        this.currentList = currentList;
    }
}
