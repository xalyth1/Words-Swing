package pl.com.words;

import javax.swing.*;

public class Word implements Comparable<Word>{
    private static int NEXT_ID = 0;
    private final int id;
    private final String word;

    private final String explanation;

    private final JButton jButton;

    /**
     * If this Word is Selected via jButton in GUI
     */
    private boolean isSelected = false;

    public Word(String word, String explanation) {
        this.id = Word.NEXT_ID;
        Word.NEXT_ID++;

        this.word = word;
        this.explanation = explanation;
        this.jButton = new JButton(this.word);
    }

    public String getWord() {
        return word;
    }

    public String getExplanation() {
        return explanation;
    }

    public JButton getjButton() {
        return jButton;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int compareTo(Word o) {
        return this.id - o.id;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return this.getWord();
    }
}
