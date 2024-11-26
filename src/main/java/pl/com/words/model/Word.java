package pl.com.words.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Word implements Comparable<Word>{
    private static int NEXT_ID = 0;
    private final int id;
    private final String headword;

    /**
     * List of dictionary definitions
     */
    private final List<String> definitions;

    /**
     * Simplified definition means - only first definition from many available for this particular word
     */
    private final String simplifiedDefinition;

    /**
     * All definitions in one string value
     */
    private final String fullDefinition;

    private final JButton jButton;

    /**
     * Is this Word is Selected via jButton in GUI
     */
    private boolean isSelected = false;

    public Word(String headword, List<String> definitions) {
        this.id = Word.NEXT_ID;
        Word.NEXT_ID++;

        this.headword = headword;
        this.definitions = definitions;
        this.simplifiedDefinition = definitions.get(0);
        this.fullDefinition = this.prepareExtendedFullWordDefinitions(definitions);

        this.jButton = new JButton(this.headword);
        LineBorder lineBorder = new LineBorder(Color.BLACK, 1);
        EmptyBorder emptyBorder = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        this.jButton.setBorder(compoundBorder);
    }


    private String prepareExtendedFullWordDefinitions(List<String> definitions) {
        StringBuilder formattedMultipleDefinitions = new StringBuilder();
        definitions.stream()
                .forEach(definition ->
                        formattedMultipleDefinitions
                                .append("•")
                                .append(definition)
                                .append("\n")
                );
        return formattedMultipleDefinitions.toString();
    }

    /**
     * definition of word containing full definitions (all)
     * @return  formattedMultipleDefinitions
     */
    private String prepareExtendedFullWordDefinitions(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder formattedMultipleDefinitions = new StringBuilder();
        try {
            // Parse JSON string to User object
            List<String> list = objectMapper.readValue(json, new TypeReference<ArrayList<String>>() {});
            list.stream().forEach(definition -> formattedMultipleDefinitions.append(definition).append("\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedMultipleDefinitions.toString();
    }

    private Word(String headword, String definition) {
        this.id = Word.NEXT_ID;
        Word.NEXT_ID++;

        this.headword = headword;
        this.definitions = new ArrayList<>();
        this.definitions.add(definition);
        this.simplifiedDefinition = definition;
        this.fullDefinition = null;
        this.jButton = new JButton(this.headword);


        LineBorder lineBorder = new LineBorder(Color.BLACK, 1);
        EmptyBorder emptyBorder = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        this.jButton.setBorder(compoundBorder);
    }

    public String getHeadword() {
        return headword;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public String getSimplifiedDefinition() {
        return simplifiedDefinition;
    }

    public String getFullDefinition() {
        return fullDefinition;
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
