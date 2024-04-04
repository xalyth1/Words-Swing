package org.example;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MockData {
    static List<Word> words;

    static boolean SELECTION_MODE = false;

    static boolean isAnythingSelected = false;

    public static void main(String[] args) {
       //loadWordsAndExplanationsFromCSV();
    }

    static Word get(String wordStr) {
        Optional<Word> opt = words.stream().filter(w -> w.getWord().equals(wordStr)).findFirst();
        return opt.get();
    }


    public static void addWords(JPanel panel, JTextArea definitionTextArea) {
        var words = loadWordsAndExplanationsFromCSV();
        for (Word w : words) {
            JButton b = w.getjButton();
            b.addActionListener(e -> definitionTextArea.setText(w.getExplanation()));
            panel.add(b);
        }

        MockData.words = words;
    }

    public static List<Word> loadWordsAndExplanationsFromCSV() {
        List<Word> words = new ArrayList<>();
        try {
            File myObj = new File("C:\\Users\\Pawel\\IdeaProjects\\Learning\\src\\swing\\WordsMockGUI\\wordsMock.txt");
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

//    public static List<JButton> getWordButtons(JPanel buttonsPanel) {
//        List<JButton> list = new ArrayList<>();
//        for (Component c : buttonsPanel.getComponents()) {
//            if (c instanceof JButton) {
//                JButton b = (JButton) c;
//                list.add(b);
//
//            }
//        }
//    }
}
