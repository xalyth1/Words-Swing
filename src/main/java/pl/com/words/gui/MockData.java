package pl.com.words.gui;

import pl.com.words.model.Word;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MockData {
    static List<Word> words;

    static boolean SELECTION_MODE;

    static boolean isAnythingSelected = false;

    public static void main(String[] args) {
       //loadWordsAndExplanationsFromCSV();
    }

    static Word get(String wordStr) {
        Optional<Word> opt = words.stream().filter(w -> w.getHeadword().equals(wordStr)).findFirst();
        return opt.get();
    }

    public void addWords(JPanel panel, JTextArea definitionTextArea) {
        var words = loadWordsAndExplanationsFromCSV();
        for (Word w : words) {
            JButton b = w.getjButton();
            b.addActionListener(e -> definitionTextArea.setText(w.getDefinition()));
            panel.add(b);
        }

        MockData.words = words;
    }

    public List<Word> loadWordsAndExplanationsFromCSV() {
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
        return words;
    }
}
