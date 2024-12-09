package pl.com.words;

import pl.com.words.configuration.ConfigLoader;
import pl.com.words.gui.GUI;
import pl.com.words.persistence.Database;

import javax.swing.*;

public class WordsVocabularyLearningSwingApplication {
    public static void main(String[] args) {
        if (ConfigLoader.getInstance().getProperty("words.service.url").equals("http://localhost:8080/")) {
            startLocalWordsServiceLocallyForTesting();
        }

        SwingUtilities.invokeLater(new GUI());
    }


    private static void startLocalWordsServiceLocallyForTesting() {
        // start Words Service on localhost
    }
}
