package pl.com.words;

import pl.com.words.gui.GUI;

import javax.swing.*;

public class WordsVocabularyLearningSwingApplication {
    public static void main(String[] args) {
        //Data data =..
        //Model model = new Model(data);
        //Swing start gui(model);
        SwingUtilities.invokeLater(new GUI());
    }
}
