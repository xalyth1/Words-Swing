package pl.com.words.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import pl.com.words.model.Word;

public class Model {
    private List<WordsList> listOfWordsList;
    private WordsList currentList;

    public static boolean SELECTION_MODE = true;

    public Model() {
        this.listOfWordsList = new ArrayList<>();
        this.listOfWordsList.add(this.loadWordsFromCSV());
        this.currentList = listOfWordsList.get(0);
    }

    public Word get(String wordStr) {
        Optional<Word> opt = currentList.getList().stream().filter(w -> w.getHeadword().equals(wordStr)).findFirst();
        return opt.get();
    }

    public Word get(String wordStr, int listId) {
        WordsList list = this.listOfWordsList
                .stream()
                .filter(wl -> wl.getId() == listId)
                .distinct()
                .findFirst()
                .get();


        Optional<Word> opt = list.getList().stream().filter(w -> w.getHeadword().equals(wordStr)).findFirst();
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

    private WordsList loadWordsFromCSV() {
        List<Word> words = new ArrayList<>();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File myObj = new File(classLoader.getResource("mock.txt").getFile());
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                var splitted = data.split(",");
                words.add(new Word(splitted[0], splitted[1]));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        WordsList initial = new WordsList("Initial List", words);
        return initial;
    }

    public WordsList getCurrentList() {
        return currentList;
    }

    public void setCurrentList(WordsList currentList) {
        this.currentList = currentList;
    }
}
