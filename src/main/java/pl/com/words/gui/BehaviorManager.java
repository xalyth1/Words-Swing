package pl.com.words.gui;

import pl.com.words.model.Model;
import pl.com.words.model.Word;
import pl.com.words.model.WordsList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

public class BehaviorManager {
    private static final BehaviorManager behaviorManager = new BehaviorManager();
    private BehaviorManager() { }

    public static BehaviorManager getInstance() {
        return behaviorManager;
    }


    void setBehaviorTo_Add_Button(JButton addWordButton) {
        addWordButton.addActionListener( e -> {
            WordsList currentList = MockData.words;
            // add words to current list
        });

    }


    void setBehaviorTo_listsJComboBox(JComboBox<String> jComboBox, JFrame frame, JLabel currentListJLabel, Model model) {
        DefaultComboBoxModel<String> m = (DefaultComboBoxModel<String>) jComboBox.getModel();

        jComboBox.addActionListener(e -> {
            JComboBox<String> source = (JComboBox<String>) e.getSource();
            String selectedOption = (String) source.getSelectedItem();
            System.out.println("Selected option: " + selectedOption);
            if (selectedOption.equals("...Add new list...")) {
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
            } else {
                currentListJLabel.setText("Current list: " + selectedOption);
                //TODO
                // change buttonsPanel to show newly selected list's words
                //buttons
            }
        });
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

    void addBehaviorTo_Buttons(JPanel buttonsPanel, JTextField focusTextField, JButton addToListButton) {
        for (Component c : buttonsPanel.getComponents()) {
            if (c instanceof JButton b) {
                b.addActionListener(e -> {
                    focusTextField.requestFocus();
                    String wordText = b.getText();
                    Word w = MockData.get(wordText);
                    manageWordSelection(w);

                    List<Word> selected = MockData.words.getList().stream().unordered().filter(Word::isSelected).toList();
                    long selectedCount = selected.size();
                    System.out.println("SELECTED: " + selected);
                    System.out.println("SELECTED Count: " + selectedCount);

                    MockData.isAnythingSelected = selectedCount > 0;

                    System.out.println("is Anything Selected? : " + MockData.isAnythingSelected);

                    if (MockData.isAnythingSelected) {
                        addToListButton.setVisible(true);
                        //addWordsToListButton.setVisible = true
                    } else {
                        addToListButton.setVisible(false);
                    }

                });
            }
        }
    }

    private void manageWordSelection(Word w) {
        if (!MockData.SELECTION_MODE)
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
        resetSelectedWords.addActionListener(e -> MockData.words.getList().stream().forEach(x -> {
            x.setSelected(false);
            x.getjButton().setBackground(bgDefaultColor);
            x.getjButton().setForeground(fgDefaultColor);
        }));

        resetSelectedWords.addActionListener(e -> addToListButton.setVisible(false));
    }

    void setBehaviorTo_DisplayMode(JPanel buttonsPanel, JRadioButton displayModeDefault, JRadioButton displayModeAlphabetical) {
        displayModeDefault.addActionListener(e -> {
            MockData.words.getList().sort(Comparator.comparingInt(Word::getId));
            rearrangeWordButtons(buttonsPanel);
        });
        displayModeAlphabetical.addActionListener(e -> {
            MockData.words.getList().sort(Comparator.comparing(Word::getHeadword, CASE_INSENSITIVE_ORDER));
            System.out.println(MockData.words.getList().get(0).getHeadword());
            System.out.println(MockData.words.getList().get(1).getHeadword());
            System.out.println(MockData.words.getList().get(2));
            MockData.words.getList().get(3);

            rearrangeWordButtons(buttonsPanel);
        });
    }

    void rearrangeWordButtons(JPanel buttonsPanel) {
        buttonsPanel.removeAll();
        for (Word w : MockData.words.getList()) {
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

    void setBehaviorToDeleteWordsButton(JButton deleteJButton, JPanel buttonsPanel) {
        deleteJButton.addActionListener(e -> {
            Component[] components = buttonsPanel.getComponents();

            if (MockData.isAnythingSelected) {

                for (Component c : components) {
                    if (c instanceof JButton) {
                        String wordStr = ((JButton) c).getText();
                        Word word = MockData.get(wordStr);
                        c.setVisible(!word.isSelected());

                    }
                }
            }

        });
    }
}
