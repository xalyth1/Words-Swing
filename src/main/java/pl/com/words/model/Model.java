package pl.com.words.model;

import java.util.*;
import java.util.stream.Collectors;

public class Model {
    private List<WordsList> listOfWordsList;

    public Model() {
        this.listOfWordsList = new ArrayList<>();
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




}
