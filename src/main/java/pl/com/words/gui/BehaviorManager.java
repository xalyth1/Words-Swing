package pl.com.words.gui;

import pl.com.words.api.WordsServiceApiClient;
import pl.com.words.media.MP3Player;
import pl.com.words.model.Model;
import pl.com.words.model.Word;
import pl.com.words.model.WordsList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

public class BehaviorManager {

    private final Model model;
    private final WordsServiceApiClient service;

    BehaviorManager(Model model) {
        this.model = model;
        this.service = new WordsServiceApiClient();
    }

    public void addWords(JPanel panel, JTextArea definitionTextArea, WordsList list) {
        for (Word w : list.getList()) {
            JButton b = w.getjButton();
            b.addActionListener(e -> definitionTextArea.setText(w.getFullDefinition()));
            panel.add(b);
        }
    }

    void setBehaviorTo_Add_Button(JButton addWordButton, JTextField newWordTextField,
                                  JPanel buttonsPanel, JTextArea definitionTextArea, JTextField focusTextField, JButton addToListButton, Model model, JCheckBox pronunciationCheckBox) {
        addWordButton.addActionListener( e -> {
            String headword = newWordTextField.getText().toLowerCase();
            newWordTextField.setText("");



            String jsonDefinition = service.getDefinitions(headword);
            Word word = model.createWordObject(headword, jsonDefinition);
            model.addWordToCurrentList(word);
            JButton button = word.getjButton();
            buttonsPanel.add(button);
            button.addActionListener(event -> definitionTextArea.setText(word.getFullDefinition()));
            addBehaviorToWordButton(button, buttonsPanel, focusTextField, addToListButton, model, pronunciationCheckBox);

            newWordTextField.requestFocus();
            buttonsPanel.revalidate();
            buttonsPanel.repaint();
        });

    }


    void setBehaviorTo_listsJComboBox(JComboBox<String> jComboBox, JFrame frame, JLabel currentListJLabel, Model model,
                                      JPanel buttonsPanel, JTextArea definitionTextArea) {
        DefaultComboBoxModel<String> m = (DefaultComboBoxModel<String>) jComboBox.getModel();

        jComboBox.addActionListener(e -> {
            JComboBox<String> source = (JComboBox<String>) e.getSource();
            String selectedOption = (String) source.getSelectedItem();
            System.out.println("Selected option: " + selectedOption);
            if (selectedOption.equals("...Add new list...")) {
                handleAddingList(frame, m, model);
            } else {
                currentListJLabel.setText("Current list: " + selectedOption);
                //TODO
                // change buttonsPanel to show newly selected list's words
                //buttons

                //search for selectedOption in model . wordslist . list names
                try {
                    WordsList wl = model.getList(selectedOption);

                    model.setCurrentList(wl);

                    //remove buttons from previous list
                    buttonsPanel.removeAll();

                    //add buttons from newly selected list to buttonsPanel JPanel
                    addWords(buttonsPanel, definitionTextArea, model.getCurrentList());
                    buttonsPanel.revalidate();
                    buttonsPanel.repaint();


                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame,"Program error. There is no selected list in the model");
                }

            }
        });
    }

    private void handleAddingList(JFrame frame, DefaultComboBoxModel<String> m, Model model) {
        String resultListName = showTextInputDialog(frame, "Enter new list name:");
        if (resultListName != null) {
            if (listNameExists(resultListName, m)) {
                JOptionPane.showMessageDialog(frame,
                        "\"" + resultListName + "\" list name already exists!");
            } else {
                m.insertElementAt(resultListName, m.getSize() - 1);
                model.addWordsList(new WordsList(resultListName, new ArrayList<>()));
                JOptionPane.showMessageDialog(frame, "Added new \"" + resultListName + "\" list!");
            }
        }
    }

    private boolean listNameExists(String list, ComboBoxModel m) {
        Vector<String> v = new Vector<>();
        for (int i = 0; i < m.getSize(); i++) {
            v.add((String)m.getElementAt(i));
        }
        return v.contains(list);

    }

    private String showTextInputDialog(JFrame parent, String message) {
        JTextField textField = new JTextField();
        Object[] messageComponents = {message, textField};
        int option = JOptionPane.showConfirmDialog(parent, messageComponents, "Create new list", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            return textField.getText();
        }
        return null;
    }

    void setBehaviorTo_SearchTextField(JTextField searchTextField, JPanel buttonsPanel) {
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                JTextField textField = (JTextField) e.getSource();
                String text = textField.getText();
                System.out.println("TEXT: " + text );
                filterButtons(text, buttonsPanel);
            }
        });
    }

    private void filterButtons(String text, JPanel buttonsPanel) {
        var components = buttonsPanel.getComponents();
        if (text == null || text.equals("")) {
            for (Component c : components) {
                c.setVisible(true);
            }
        } else {
            for (Component c : components) {
                if (c instanceof JButton b) {
                    int len = text.length();
                    b.setVisible(b.getText().length() >= len && b.getText().substring(0, len).equals(text));
                }
            }
        }
    }

    void addBehaviorTo_Buttons(JPanel buttonsPanel, JTextField focusTextField, JButton addToListButton, Model model,
                               JCheckBox pronunciationButton) {
        for (Component c : buttonsPanel.getComponents()) {
            if (c instanceof JButton b) {
                addBehaviorToWordButton(b, buttonsPanel, focusTextField, addToListButton,
                        model, pronunciationButton);
            }
        }
    }

    private void addBehaviorToWordButton(JButton b, JPanel buttonsPanel, JTextField focusTextField, JButton addToListButton, Model model,
                                         JCheckBox pronunciationButton) {
        b.addActionListener(e -> {
            focusTextField.requestFocus();
            String wordText = b.getText();
            Word w = model.get(wordText);
            manageWordSelection(w);

            List<Word> selected = model.getCurrentList().getList().stream()
                    .unordered().filter(Word::isSelected).toList();
            long selectedCount = selected.size();
            System.out.println("SELECTED: " + selected);
            System.out.println("SELECTED Count: " + selectedCount);

            model.getCurrentList().setIsAnythingSelected(selectedCount > 0);

            System.out.println("is Anything Selected? : " + model.getCurrentList().isAnythingSelected());

            if (model.getCurrentList().isAnythingSelected()) {
                addToListButton.setVisible(true);
            } else {
                addToListButton.setVisible(false);
            }

            //pronunciation
            managePronunciation(w.getHeadword(), pronunciationButton);

        });

    }

    private void managePronunciation(String headword, JCheckBox pronunciationButton) {
        if (pronunciationButton.isSelected()) {
            //request Words-Service API for mp3
            InputStream mp3Stream = service.getPronunciation(headword);

            MP3Player myPlayer = new MP3Player(mp3Stream);
            myPlayer.play();
        }
    }

    private void manageWordSelection(Word w) {
        if (!Model.USE_SELECTION_MODE)
            return;

        JButton b = w.getjButton();
        if (w.isSelected()) {
            w.setSelected(false);
            //set default colors:
            b.setBackground(new JButton().getBackground());
            b.setForeground(new JButton().getForeground());
        } else {
            w.setSelected(true);
            b.setBackground(Color.green);
            b.setForeground(Color.blue);
        }
    }

    void addBehaviorTo_SettingsButton(JButton settingsButton, JPanel settingsPanel) {
        settingsButton.addActionListener(e -> settingsPanel.setVisible(settingsPanel.isVisible() ? false : true));
    }

    void setBehaviorTo_resetSelectedWordsButton(JButton resetSelectedWords, JButton addToListButton) {
        Color bgDefaultColor = new JButton().getBackground();
        Color fgDefaultColor = new JButton().getForeground();
        resetSelectedWords.addActionListener(e -> model.getCurrentList().getList().stream().forEach(x -> {
            x.setSelected(false);
            x.getjButton().setBackground(bgDefaultColor);
            x.getjButton().setForeground(fgDefaultColor);
        }));

        resetSelectedWords.addActionListener(e -> addToListButton.setVisible(false));
    }

    void setBehaviorTo_DisplayMode(JPanel buttonsPanel, JRadioButton displayModeDefault, JRadioButton displayModeAlphabetical) {
        displayModeDefault.addActionListener(e -> {
            model.getCurrentList().getList().sort(Comparator.comparingInt(Word::getId));
            rearrangeWordButtons(buttonsPanel);
        });
        displayModeAlphabetical.addActionListener(e -> {
            model.getCurrentList().getList().sort(Comparator.comparing(Word::getHeadword, CASE_INSENSITIVE_ORDER));
            System.out.println(model.getCurrentList().getList().get(0).getHeadword());
            System.out.println(model.getCurrentList().getList().get(1).getHeadword());
            System.out.println(model.getCurrentList().getList().get(2));
            model.getCurrentList().getList().get(3);

            rearrangeWordButtons(buttonsPanel);
        });
    }

    void rearrangeWordButtons(JPanel buttonsPanel) {
        buttonsPanel.removeAll();
        for (Word w : model.getCurrentList().getList()) {
            buttonsPanel.add(w.getjButton());
        }
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        //System.out.println(MockData.words.stream().map(x -> x.getWord()).findFirst());
    }

    void setBehaviorToFileMenuItem(JMenuItem addListMenuItem) {
        addListMenuItem.addActionListener(e -> {
            // open dialog, input text with list name, check if name does not exist if so, display communicate, else add new listName to Lists list
            System.out.println("set behavior");
        });
    }

    void setBehaviorToDeleteWordsButton(JButton deleteJButton, JPanel buttonsPanel, Model model) {
        deleteJButton.addActionListener(e -> {
            Component[] components = buttonsPanel.getComponents();

            if (model.getCurrentList().isAnythingSelected()) {

                for (Component c : components) {
                    if (c instanceof JButton) {
                        String wordStr = ((JButton) c).getText();
                        Word word = model.get(wordStr);
                        c.setVisible(!word.isSelected());

                    }
                }
            }

        });
    }
}
