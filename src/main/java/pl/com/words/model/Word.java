package pl.com.words.model;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Word implements Comparable<Word>{
    private static int NEXT_ID = 0;
    private final int id;
    private final String headword;

    private final String definition;

    private final JButton jButton;

    /**
     * Is this Word is Selected via jButton in GUI
     */
    private boolean isSelected = false;

    public Word(String headword, String definition) {
        this.id = Word.NEXT_ID;
        Word.NEXT_ID++;

        this.headword = headword;
        this.definition = definition;
        this.jButton = new JButton(this.headword);

        LineBorder lineBorder = new LineBorder(Color.BLACK, 1);
        EmptyBorder emptyBorder = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        this.jButton.setBorder(compoundBorder);
    }

    public String getHeadword() {
        return headword;
    }

    public String getDefinition() {
        return definition;
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
        return this.getHeadword();
    }
}
