package pl.com.words;

import pl.com.words.gui.GUI;
import pl.com.words.persistence.Database;

import javax.swing.*;

public class WordsVocabularyLearningSwingApplication {
    public static void main(String[] args) {


        SwingUtilities.invokeLater(new GUI());
    }
}
