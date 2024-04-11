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




}
