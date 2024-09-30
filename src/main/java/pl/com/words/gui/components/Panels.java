package pl.com.words.gui.components;

import pl.com.words.api.WordsServiceApiClient;
import pl.com.words.gui.GUI;
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

import static java.awt.GridBagConstraints.LINE_START;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class Panels {

    GUI gui;
    Model model;
    WordsServiceApiClient service = new WordsServiceApiClient();
    /**
     * Panels
     */
    private JPanel southPanel, centralPanel;
    private NorthPanel northPanel;
    private DefinitionPanel definitionPanel;
    private SettingsPanel settingsPanel;
    private FunctionalitiesPanel functionalitiesPanel;
    private JPanel buttonsPanel;

    public Panels(GUI gui, Model model) {
        this.gui = gui;
        this.model = model;

        this.northPanel = this.createNorthPanel(model);
        this.southPanel = this.createSouthPanel();
        this.centralPanel = this.createCentralPanel();
        this.buttonsPanel = this.createButtonsPanel();
        this.definitionPanel = this.createDefinitionPanel();
        this.settingsPanel = this.createSettingsPanel();
        this.functionalitiesPanel = this.createFunctionalitiesPanel(model);

        this.southPanel.add(definitionPanel);
        this.southPanel.add(functionalitiesPanel);
        this.southPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JScrollPane buttonsPanelScrollPane = buttonsPanelScrollPane(buttonsPanel);
        centralPanel.add(buttonsPanelScrollPane);


        this.addBehavior();

        this.southPanel.revalidate();
        this.southPanel.repaint();

    }

    private void addBehavior() {
        this.addWords();
        this.addBehaviorTo_Buttons(this.model);
        this.setBehaviorTo_SearchTextField();
        this.setBehaviorTo_listsJComboBox(this.gui, this.model);
        this.addBehaviorTo_SettingsButton();
        this.setBehaviorTo_Add_Button(this.model);
        this.setBehaviorTo_DisplayMode();
        this.setBehaviorTo_resetSelectedWordsButton();
        this.setBehaviorToDeleteWordsButton(this.model);
    }


    /**
     * Behavior methods
     */

    private void setBehaviorToDeleteWordsButton(Model model) {
        JButton deleteJButton = this.functionalitiesPanel.getDeleteWordButton();
        JPanel buttonsPanel = this.buttonsPanel;
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


    private void setBehaviorTo_resetSelectedWordsButton() {
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

    private void setBehaviorTo_DisplayMode() {
        JPanel buttonsPanel = this.getButtonsPanel();
        JRadioButton displayModeDefault = this.settingsPanel.getDisplayModeDefault();
        JRadioButton displayModeAlphabetical = this.settingsPanel.getDisplayModeAlphabetical();


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
    private void rearrangeWordButtons(JPanel buttonsPanel) {
        buttonsPanel.removeAll();
        for (Word w : model.getCurrentList().getList()) {
            buttonsPanel.add(w.getjButton());
        }
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        //System.out.println(MockData.words.stream().map(x -> x.getWord()).findFirst());
    }

    private void setBehaviorTo_Add_Button(Model model) {

        var addWordButton = this.getFunctionalitiesPanel().getAddWordButton();
        var newWordTextField = this.getFunctionalitiesPanel().getNewWordTextField();
        var searchTextField = this.getFunctionalitiesPanel().getSearchTextField();
        var pronunciationCheckBox = this.getSettingsPanel().getPronunciationCheckBox();
        var buttonsPanel = this.getButtonsPanel();
        var definitionTextArea = this.getDefinitionPanel().getDefinitionTextArea();
        var focusTextField = this.getFunctionalitiesPanel().getNewWordTextField();
        JButton addToListButton = this.getFunctionalitiesPanel().getAddToListButton();



        addWordButton.addActionListener( e -> {
            String headword = newWordTextField.getText().toLowerCase();
            newWordTextField.setText("");



            String jsonDefinition = service.getDefinitions(headword);
            Word word = model.createWordObject(headword, jsonDefinition);
            model.addWordToCurrentList(word);
            JButton button = word.getjButton();
            buttonsPanel.add(button);
            button.addActionListener(event -> definitionTextArea.setText(word.getFullDefinition()));
            addBehaviorToWordButton(button, /*buttonsPanel,*/ focusTextField, addToListButton, model, pronunciationCheckBox);

            newWordTextField.requestFocus();
            buttonsPanel.revalidate();
            buttonsPanel.repaint();
        });

    }

    private void addBehaviorTo_SettingsButton() {
        JButton settingsButton = this.getFunctionalitiesPanel().getSettingsButton();
        JPanel settingsPanel = this.getSettingsPanel();
        settingsButton.addActionListener(e -> settingsPanel.setVisible(settingsPanel.isVisible() ? false : true));
    }


    /**
     * addWords method is used to adding the word Buttons
     * to the initial word List visual representation (buttonsPanel)
     */
    private void addWords() {
        JPanel buttonsPanel = this.getButtonsPanel();
        JTextArea definitionTextArea = this.definitionPanel.getDefinitionTextArea();
        WordsList currentWordsList = model.getCurrentList();
        List<Word> actualWords = currentWordsList.getList();
        for (Word w : actualWords) {
            JButton b = w.getjButton();
            b.addActionListener(e -> definitionTextArea.setText(w.getFullDefinition()));
            buttonsPanel.add(b);
        }
    }

    private void addBehaviorTo_Buttons(Model model) {
        JTextField searchTextField = this.getFunctionalitiesPanel().getSearchTextField();
        JButton addToListButton = this.getFunctionalitiesPanel().getAddToListButton();
        JCheckBox pronunciationButton = this.getSettingsPanel().getPronunciationCheckBox();

        for (Component c : this.buttonsPanel.getComponents()) {
            if (c instanceof JButton b) {
                addBehaviorToWordButton(b, searchTextField, addToListButton,
                        model, pronunciationButton);
            }
        }
    }

    private void setBehaviorTo_listsJComboBox(GUI gui,  Model model) {

        JComboBox<String> listJComboBox = this.getFunctionalitiesPanel().getListJComboBox();
        JLabel currentListJLabel = this.getNorthPanel().getCurrentListJLabel();
        JTextArea definitionTextArea = this.getDefinitionPanel().getDefinitionTextArea();
        JPanel buttonsPanel = this.getButtonsPanel();

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

    private void addBehaviorToWordButton(JButton b, JTextField focusTextField, JButton addToListButton, Model model,
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
    private void managePronunciation(String headword, JCheckBox pronunciationButton) {
        if (pronunciationButton.isSelected()) {
            //request Words-Service API for mp3
            InputStream mp3Stream = service.getPronunciation(headword);

            MP3Player myPlayer = new MP3Player(mp3Stream);
            myPlayer.play();
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

    void setBehaviorTo_SearchTextField() {
        JTextField searchTextField = this.getFunctionalitiesPanel().getSearchTextField();
        JPanel buttonsPanel = this.getButtonsPanel();
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

    /**
     *
     * Create the respective Panels
     */
    private NorthPanel createNorthPanel(Model model) {
        NorthPanel northPanel = new NorthPanel(model);
        //northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        northPanel.setBackground(Color.LIGHT_GRAY);
        return northPanel;
    }
    private JPanel createCentralPanel() {
        JPanel centralPanel = new JPanel();

        return centralPanel;
    }

    private JScrollPane buttonsPanelScrollPane(JPanel buttonsPanel) {
        JScrollPane buttonsPanelScrollPane = new JScrollPane(buttonsPanel,
                VERTICAL_SCROLLBAR_ALWAYS,
                HORIZONTAL_SCROLLBAR_ALWAYS);
        buttonsPanelScrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        buttonsPanelScrollPane.setPreferredSize(new Dimension(1000,600));
        buttonsPanelScrollPane.getVerticalScrollBar().setUnitIncrement(5);
        return buttonsPanelScrollPane;
    }

    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel();
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return southPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setPreferredSize(new Dimension(950,3000));
        return buttonsPanel;
    }
    private DefinitionPanel createDefinitionPanel() {

        return new DefinitionPanel();
    }

    private FunctionalitiesPanel createFunctionalitiesPanel(Model model) {
        FunctionalitiesPanel functionalitiesPanel = new FunctionalitiesPanel(model);

        return functionalitiesPanel;
    }



    private SettingsPanel createSettingsPanel() {
        return new SettingsPanel();
    }

    /**
     *
     * Getters
     */

    public JPanel getSouthPanel() {
        return southPanel;
    }

    public NorthPanel getNorthPanel() {
        return northPanel;
    }

    public JPanel getCentralPanel() {
        return centralPanel;
    }

    public DefinitionPanel getDefinitionPanel() {
        return definitionPanel;
    }

    public SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }

    public FunctionalitiesPanel getFunctionalitiesPanel() {
        return functionalitiesPanel;
    }

    public JPanel getButtonsPanel() {
        return buttonsPanel;
    }
}
