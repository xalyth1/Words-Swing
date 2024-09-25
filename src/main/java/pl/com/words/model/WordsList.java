package pl.com.words.model;

import java.util.List;

public class WordsList {
    private static Integer NEXT_ID = 0;
    private Integer id;
    private String listName;
    private List<Word> list;

    private boolean isAnythingSelected = false;


    public WordsList(String listName, List<Word> list) {
        this.id = NEXT_ID;
        NEXT_ID++;
        this.listName = listName;
        this.list = list;
    }

    public Integer getId() {
        return id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public List<Word> getList() {
        return list;
    }

    public void setList(List<Word> list) {
        this.list = list;
    }

    public boolean isAnythingSelected() {
        return isAnythingSelected;
    }

    public void setIsAnythingSelected(boolean anythingSelected) {
        isAnythingSelected = anythingSelected;
    }
}
