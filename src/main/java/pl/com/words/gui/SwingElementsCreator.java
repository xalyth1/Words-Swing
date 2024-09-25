package pl.com.words.gui;

import pl.com.words.model.ApplicationSettings;
import pl.com.words.model.WordsList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public final class SwingElementsCreator {
    private static final SwingElementsCreator creator = new SwingElementsCreator();

    private static final Dimension PREFERRED_BUTTON_DIMENSION = new Dimension(140, 40);

    private SwingElementsCreator() { }

    public static SwingElementsCreator getInstance() {
        return creator;
    }

    JButton settingsButton() {
        JButton settingsButton = new JButton("Settings");
        settingsButton.setIcon(new ImageIcon("settingsIcon.png"));
        settingsButton.setPreferredSize(PREFERRED_BUTTON_DIMENSION);
        //settingsButton.addActionListener(e -> settingsPanel.setVisible(!settingsPanel.isVisible()));
        return settingsButton;
    }

    JTextField newWordTextField() {
        JTextField newWordTextField = new JTextField();
        newWordTextField.setPreferredSize(new Dimension(300,40));
        return newWordTextField;
    }

    JTextField searchTextField() {
        JTextField searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(300,40));
        return searchTextField;
    }

    JButton addWordButton() {
        JButton addWordButton = new JButton("Add");
        addWordButton.setPreferredSize(PREFERRED_BUTTON_DIMENSION);
        return addWordButton;
    }

    JButton deleteWordButton() {
        JButton deleteWordButton = new JButton("Delete selected");
        deleteWordButton.setToolTipText("Delete selected words from current list");
        deleteWordButton.setPreferredSize(new Dimension(240,40));

        return deleteWordButton;
    }

    JTextArea definitionTextArea() {
        JTextArea definitionTextArea = new JTextArea();
        definitionTextArea.setEditable(false);
        definitionTextArea.setLineWrap(true);
        definitionTextArea.setWrapStyleWord(true);
        return definitionTextArea;
    }

    JScrollPane definitionScrollPane(JTextArea jTextArea) {
        JScrollPane definitionScrollPane = new JScrollPane(jTextArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);
        definitionScrollPane.setVisible(true);
        definitionScrollPane.setPreferredSize(new Dimension(300,300));
        return definitionScrollPane;
    }


    JScrollPane buttonsPanelScrollPane(JPanel buttonsPanel) {
        JScrollPane buttonsPanelScrollPane = new JScrollPane(buttonsPanel,
                VERTICAL_SCROLLBAR_ALWAYS,
                HORIZONTAL_SCROLLBAR_ALWAYS);
        buttonsPanelScrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        buttonsPanelScrollPane.setPreferredSize(new Dimension(1000,600));
        buttonsPanelScrollPane.getVerticalScrollBar().setUnitIncrement(5);
        return buttonsPanelScrollPane;
    }

    JButton resetSelectedWordsButton() {
        JButton resetSelectedWordsButton = new JButton("Reset selection");
        resetSelectedWordsButton.setToolTipText("Unselect all");
        return resetSelectedWordsButton;
    }

    void addNewList(JComboBox jComboBox) {

    }

    JComboBox<String> listsJComboBox(JFrame frame, JLabel currentListJLabel, List<String> listNames) {
        Vector<String> v = new Vector<>();
//        v.add("Default");
//        v.add("Exam");
        for (String listName : listNames) {
            v.add(listName);
        }

        v.add("...Add new list...");
        JComboBox<String> jComboBox = new JComboBox<>(v);

        jComboBox.setPreferredSize(new Dimension(300, 40));
        jComboBox.setFont(ApplicationSettings.applicationFont);
        return jComboBox;
    }





    JButton clearListJButton() {
        JButton clearListJButton = new JButton("Clear List");
        return clearListJButton;
    }

    JButton addSelectedToListJButton() {
        JButton addSelectedToList = new JButton("Add selected words to list");
        addSelectedToList.setPreferredSize(new Dimension(350, 40));
        addSelectedToList.setVisible(false);
        return addSelectedToList;
    }

    JMenuBar menuBar() {
        return new JMenuBar();
    }

    JMenu menu() {
        JMenu menu = new JMenu("Menu");
        return menu;
    }

    JMenu fileSubMenu() {
        JMenu submenu = new JMenu("Lists");
        return submenu;
    }

    JMenuItem addListMenuItem() {
        JMenuItem addNewListJMenuItem = new JMenuItem("Add New List");
        return addNewListJMenuItem;
    }

    JLabel currentListJLabel() {
        return new JLabel("Current list: Default");
    }
}
