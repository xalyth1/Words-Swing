package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public final class SwingElementsCreator {
    private static final SwingElementsCreator creator = new SwingElementsCreator();

    private static final Dimension PREFFERED_BUTTON_DIMESNION = new Dimension(140, 40);

    private SwingElementsCreator() { }

    public static SwingElementsCreator getInstance() {
        return creator;
    }

    JButton settings_Button() {
        JButton settingsButton = new JButton("Settings");
        settingsButton.setIcon(new ImageIcon("settingsIcon.png"));
        settingsButton.setPreferredSize(PREFFERED_BUTTON_DIMESNION);
        //settingsButton.addActionListener(e -> settingsPanel.setVisible(!settingsPanel.isVisible()));
        return settingsButton;
    }

    JTextField newWord_TextField() {
        JTextField newWordTextField = new JTextField();
        newWordTextField.setPreferredSize(new Dimension(300,40));
        Font font = newWordTextField.getFont().deriveFont((float)28); //deriving a new font
        newWordTextField.setFont(font);
        return newWordTextField;
    }

    JTextField search_TextField() {
        JTextField searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(300,40));
        Font font = searchTextField.getFont().deriveFont((float)28); //deriving a new font
        searchTextField.setFont(font);
        return searchTextField;
    }

    JButton add_Word_Button() {
        JButton addWordButton = new JButton("Add");
        addWordButton.setPreferredSize(PREFFERED_BUTTON_DIMESNION);
        return addWordButton;
    }

    JButton delete_Word_Button() {
        JButton deleteWordButton = new JButton("Delete selected");
        deleteWordButton.setToolTipText("Delete selected words from current list");
        deleteWordButton.setPreferredSize(new Dimension(240,40));

        return deleteWordButton;
    }



    JTextArea definition_TextArea() {
        JTextArea definitionTextArea = new JTextArea();
        definitionTextArea.setEditable(false);
        definitionTextArea.setLineWrap(true);
        definitionTextArea.setWrapStyleWord(true);
        return definitionTextArea;
    }

    JScrollPane definition_ScrollPane(JTextArea jTextArea) {
        JScrollPane definitionScrollPane = new JScrollPane(jTextArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);
        definitionScrollPane.setVisible(true);
        definitionScrollPane.setPreferredSize(new Dimension(300,300));
        return definitionScrollPane;
    }


    JScrollPane buttonsPanel_ScrollPane(JPanel buttonsPanel) {
        JScrollPane buttonsPanelScrollPane = new JScrollPane(buttonsPanel,
                VERTICAL_SCROLLBAR_ALWAYS,
                HORIZONTAL_SCROLLBAR_ALWAYS);
        buttonsPanelScrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        buttonsPanelScrollPane.setPreferredSize(new Dimension(1000,600));
        buttonsPanelScrollPane.getVerticalScrollBar().setUnitIncrement(5);
        return buttonsPanelScrollPane;
    }

    JButton resetSelectedWords_Button() {
        JButton resetSelectedWordsButton = new JButton("Reset selection");
        return resetSelectedWordsButton;
    }

    JComboBox lists_JComboBox(JFrame frame, JLabel currentListJLabel) {
        String[] listArray = new String[] {};
        Vector<String> v = new Vector<>();
        v.add("Default");
        v.add("Exam");
        v.add("...Add new list...");

        JComboBox<String> jComboBox = new JComboBox(v);
        jComboBox.addActionListener(e -> {
            JComboBox<String> source = (JComboBox<String>) e.getSource();
            String selectedOption = (String) source.getSelectedItem();
            System.out.println("Selected option: " + selectedOption);
            if (selectedOption.equals("...Add new list...")) {
                String resultListName = showTextInputDialog(frame, "Enter new list name:");
                if (resultListName != null) {
                    //check if result list name does not exist
                    if (listNameExists(resultListName, v)) {
                        JOptionPane.showMessageDialog(frame,
                                "\"" + resultListName + "\" list name already exists!");
                    } else {
                        v.add(v.size() - 1, resultListName);
                        JOptionPane.showMessageDialog(frame, "Added new \"" + resultListName + "\" list!");
                    }
                }
            } else {
                currentListJLabel.setText("Current list: " + selectedOption);
                //TODO
                // change buttonsPanel to show newly selected list's words
            }
        });

        jComboBox.setPreferredSize(new Dimension(300, 40));
        jComboBox.setFont(new Font("Serif", Font.BOLD, 30));
        return jComboBox;
    }

    private String showTextInputDialog(JFrame parent, String message) {
        JTextField textField = new JTextField();
        Object[] messageComponents = {message, textField};
        int option = JOptionPane.showConfirmDialog(parent, messageComponents, "Input", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            return textField.getText();
        }
        return null;
    }

    private boolean listNameExists(String list, Vector<String> v) {
        return v.contains(list);

    }


    JButton clearList_JButton() {
        JButton clearListJButton = new JButton("Clear List");
        return clearListJButton;
    }

    JButton addSelectedToList_JButton() {
        JButton addSelectedToList = new JButton("Add selected words to list");
        addSelectedToList.setPreferredSize(new Dimension(350, 40));
        addSelectedToList.setVisible(false);
        return addSelectedToList;
    }


    JMenuBar menuBar() {
        return new JMenuBar();
    }

    JMenu menu() {

        JMenu menu = new JMenu("File");
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
