package pl.com.words;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

public class BehaviorManager {
    private static final BehaviorManager behaviorManager = new BehaviorManager();
    private BehaviorManager() { }

    public static BehaviorManager getInstance() {
        return behaviorManager;
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

                    List<Word> selected = MockData.words.stream().unordered().filter(Word::isSelected).toList();
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
        resetSelectedWords.addActionListener(e -> MockData.words.stream().forEach(x -> {
            x.setSelected(false);
            x.getjButton().setBackground(bgDefaultColor);
            x.getjButton().setForeground(fgDefaultColor);
        }));

        resetSelectedWords.addActionListener(e -> addToListButton.setVisible(false));
    }

    void setBehaviorTo_DisplayMode(JPanel buttonsPanel, JRadioButton displayModeDefault, JRadioButton displayModeAlphabetical) {
        displayModeDefault.addActionListener(e -> {
            MockData.words.sort(Comparator.comparingInt(Word::getId));
            rearrangeWordButtons(buttonsPanel);
        });
        displayModeAlphabetical.addActionListener(e -> {
            MockData.words.sort(Comparator.comparing(Word::getWord, CASE_INSENSITIVE_ORDER));
            System.out.println(MockData.words.get(0).getWord());
            System.out.println(MockData.words.get(1).getWord());
            System.out.println(MockData.words.get(2));
            MockData.words.get(3);

            rearrangeWordButtons(buttonsPanel);
        });
    }

    void rearrangeWordButtons(JPanel buttonsPanel) {
        buttonsPanel.removeAll();
        for (Word w : MockData.words) {
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
