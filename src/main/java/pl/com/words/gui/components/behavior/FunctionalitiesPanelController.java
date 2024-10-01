package pl.com.words.gui.components.behavior;

import pl.com.words.api.WordsServiceApiClient;
import pl.com.words.gui.GUI;
import pl.com.words.gui.components.ButtonsPanel;
import pl.com.words.gui.components.FunctionalitiesPanel;
import pl.com.words.gui.components.Panels;
import pl.com.words.model.Model;
import pl.com.words.model.Word;
import pl.com.words.model.WordsList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class FunctionalitiesPanelController {
    private Panels panels;
    private FunctionalitiesPanel functionalitiesPanel;
    private Model model;
    private WordsServiceApiClient service = new WordsServiceApiClient();
    public FunctionalitiesPanelController(Panels panels, FunctionalitiesPanel functionalitiesPanel, Model model) {
        this.panels = panels;
        this.functionalitiesPanel = functionalitiesPanel;
        this.model = model;
    }

    public void addBehavior() {
        this.addWords();
        this.setBehaviorToDeleteWordsButton();
        this.setBehaviorToSearchTextField();
        this.setBehaviorTolistsJComboBox(panels.getGui(), this.model);
        this.setBehaviorToSettingsButton();
        this.setBehaviorToAddButton(this.model);
        this.setBehaviorToResetSelectedWordsButton();
    }

    /**
     * Functionalities Panel's behavior methods
     */

    private void setBehaviorToResetSelectedWordsButton() {
        JButton resetSelectedWords = this.functionalitiesPanel.getResetSelectedWordsButton();
        JButton addToListButton = this.functionalitiesPanel.getAddToListButton();

        Color bgDefaultColor = new JButton().getBackground();
        Color fgDefaultColor = new JButton().getForeground();
        resetSelectedWords.addActionListener(e -> model.getCurrentList().getList().stream().forEach(x -> {
            x.setSelected(false);
            x.getjButton().setBackground(bgDefaultColor);
            x.getjButton().setForeground(fgDefaultColor);
        }));

        resetSelectedWords.addActionListener(e -> addToListButton.setVisible(false));
    }

    private void setBehaviorToAddButton(Model model) {

        var addWordButton = panels.getFunctionalitiesPanel().getAddWordButton();
        var newWordTextField = panels.getFunctionalitiesPanel().getNewWordTextField();
        var searchTextField = panels.getFunctionalitiesPanel().getSearchTextField();
        var pronunciationCheckBox = panels.getSettingsPanel().getPronunciationCheckBox();
        var buttonsPanel = panels.getButtonsPanel();
        var definitionTextArea = panels.getDefinitionPanel().getDefinitionTextArea();
        var focusTextField = panels.getFunctionalitiesPanel().getNewWordTextField();
        JButton addToListButton = panels.getFunctionalitiesPanel().getAddToListButton();

        addWordButton.addActionListener( e -> {
            String headword = newWordTextField.getText().toLowerCase();
            newWordTextField.setText("");

            String jsonDefinition = service.getDefinitions(headword);
            Word word = model.createWordObject(headword, jsonDefinition);
            model.addWordToCurrentList(word);
            JButton button = word.getjButton();
            buttonsPanel.add(button);
            button.addActionListener(event -> definitionTextArea.setText(word.getFullDefinition()));
            buttonsPanel.getController().addBehaviorToWordButton(button, /*buttonsPanel,*/ focusTextField, addToListButton, model, pronunciationCheckBox);

            newWordTextField.requestFocus();
            buttonsPanel.revalidate();
            buttonsPanel.repaint();
        });

    }

    private void setBehaviorToSettingsButton() {
        JButton settingsButton = panels.getFunctionalitiesPanel().getSettingsButton();
        JPanel settingsPanel = panels.getSettingsPanel();
        settingsButton.addActionListener(e -> settingsPanel.setVisible(settingsPanel.isVisible() ? false : true));
    }

    private void setBehaviorToSearchTextField() {
        JTextField searchTextField = panels.getFunctionalitiesPanel().getSearchTextField();
        ButtonsPanel buttonsPanel = panels.getButtonsPanel();
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
    private void filterButtons(String text, ButtonsPanel buttonsPanel) {
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


    private void setBehaviorToDeleteWordsButton() {
        JButton deleteJButton = this.functionalitiesPanel.getDeleteWordButton();
        JPanel buttonsPanel = panels.getButtonsPanel();
        deleteJButton.addActionListener(e -> {
            Component[] components = buttonsPanel.getComponents();

            List<Component> componentsToRemove = new ArrayList<>();
            if (model.getCurrentList().isAnythingSelected()) {
                for (Component c : components) {
                    if (c instanceof JButton) {
                        String wordStr = ((JButton) c).getText();
                        Word word = model.get(wordStr);
                        if (word.isSelected()) {
                            c.setVisible(!word.isSelected());
                            model.getCurrentList().getList().remove(word);
                            componentsToRemove.add(c);
                        }
                    }
                }
                for (Component comp : componentsToRemove) {
                    buttonsPanel.remove(comp);
                }
            }
        });
    }

    private void setBehaviorTolistsJComboBox(GUI gui, Model model) {
        JComboBox<String> listJComboBox = panels.getFunctionalitiesPanel().getListJComboBox();
        JLabel currentListJLabel = panels.getNorthPanel().getCurrentListJLabel();
        JTextArea definitionTextArea = panels.getDefinitionPanel().getDefinitionTextArea();
        JPanel buttonsPanel = panels.getButtonsPanel();

        DefaultComboBoxModel<String> m = (DefaultComboBoxModel<String>) listJComboBox.getModel();

        listJComboBox.addActionListener(e -> {
            JComboBox<String> source = (JComboBox<String>) e.getSource();
            String selectedOption = (String) source.getSelectedItem();
            System.out.println("Selected option: " + selectedOption);
            if (selectedOption.equals("...Add new list...")) {
                handleAddingList(gui, m, model);
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
                    //addWords(buttonsPanel, definitionTextArea, model.getCurrentList());
                    addWords();

                    buttonsPanel.revalidate();
                    buttonsPanel.repaint();


                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(gui,"Program error. There is no selected list in the model");
                }

            }
        });
    }

    /**
     * addWords method is used to adding the word Buttons
     * to the initial word List visual representation (buttonsPanel)
     */
    private void addWords() {
        JPanel buttonsPanel = panels.getButtonsPanel();
        JTextArea definitionTextArea = panels.getDefinitionPanel().getDefinitionTextArea();
        WordsList currentWordsList = model.getCurrentList();
        List<Word> actualWords = currentWordsList.getList();
        for (Word w : actualWords) {
            JButton b = w.getjButton();
            b.addActionListener(e -> definitionTextArea.setText(w.getFullDefinition()));
            buttonsPanel.add(b);
        }
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
    private String showTextInputDialog(JFrame parent, String message) {
        JTextField textField = new JTextField();
        Object[] messageComponents = {message, textField};
        int option = JOptionPane.showConfirmDialog(parent, messageComponents, "Create new list", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            return textField.getText();
        }
        return null;
    }
    private boolean listNameExists(String list, ComboBoxModel m) {
        Vector<String> v = new Vector<>();
        for (int i = 0; i < m.getSize(); i++) {
            v.add((String)m.getElementAt(i));
        }
        return v.contains(list);

    }



}
