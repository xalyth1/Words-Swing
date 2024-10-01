package pl.com.words.gui.components;

import pl.com.words.gui.GUI;
import pl.com.words.gui.components.behavior.FunctionalitiesPanelController;
import pl.com.words.model.ApplicationSettings;
import pl.com.words.model.Model;

import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.util.Vector;

import static java.awt.GridBagConstraints.LINE_START;
import static pl.com.words.model.ApplicationSettings.PREFERRED_BUTTON_DIMENSION;

public class FunctionalitiesPanel extends JPanel {
    private JButton settingsButton;
    private JButton resetSelectedWordsButton;
    private JTextField newWordTextField;
    private JButton addWordButton;
    private JTextField searchTextField;
    private JComboBox<String> listJComboBox;
    private JButton addToListButton;
    private JButton deleteWordButton;

    private Model model;
    private Panels panels;
    private FunctionalitiesPanelController controller;

    public FunctionalitiesPanel(Panels panels, Model model) {
        super(new GridBagLayout());
        this.settingsButton = settingsButton();
        this.resetSelectedWordsButton = resetSelectedWordsButton();
        this.newWordTextField = newWordTextField();
        this.addWordButton = addWordButton();
        this.searchTextField = searchTextField();
        this.listJComboBox = this.listsJComboBox(model.getListsNames());
        this.addToListButton = this.addSelectedToListJButton();
        this.deleteWordButton = this.deleteWordButton();

        this.model = model;
        this.panels = panels;
        this.controller = new FunctionalitiesPanelController(this.panels, this, this.model);

        //GridBagLayout layout = new GridBagLayout();
        this.addSubPanelStructureToFunctionalitiesPanel();

        //this.setLayout(layout);
    }

    private JButton deleteWordButton() {
        JButton deleteWordButton = new JButton("Delete selected");
        deleteWordButton.setToolTipText("Delete selected words from current list");
        deleteWordButton.setPreferredSize(new Dimension(240,40));

        return deleteWordButton;
    }

    private JButton addSelectedToListJButton() {
        JButton addSelectedToList = new JButton("Add selected words to list");
        addSelectedToList.setPreferredSize(new Dimension(350, 40));
        addSelectedToList.setVisible(false);
        return addSelectedToList;
    }

    private JTextField searchTextField() {
        JTextField searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(300,40));
        return searchTextField;
    }

    private JButton addWordButton() {
        JButton addWordButton = new JButton("Add");
        addWordButton.setPreferredSize(PREFERRED_BUTTON_DIMENSION);
        return addWordButton;
    }

    private JButton settingsButton() {
        JButton settingsButton = new JButton("Settings");
        settingsButton.setIcon(new ImageIcon("settingsIcon.png"));
        settingsButton.setPreferredSize(PREFERRED_BUTTON_DIMENSION);
        //settingsButton.addActionListener(e -> settingsPanel.setVisible(!settingsPanel.isVisible()));
        return settingsButton;
    }
    private JButton resetSelectedWordsButton() {
        JButton resetSelectedWordsButton = new JButton("Reset selection");
        resetSelectedWordsButton.setToolTipText("Unselect all");
        return resetSelectedWordsButton;
    }

    private JTextField newWordTextField() {
        JTextField newWordTextField = new JTextField();
        newWordTextField.setPreferredSize(new Dimension(300,40));
        return newWordTextField;
    }

    private JComboBox<String> listsJComboBox(List<String> listNames) {
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

    private void addSubPanelStructureToFunctionalitiesPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = LINE_START;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JPanel row0 = new JPanel();
        row0.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        row0.add(settingsButton);
        row0.add(resetSelectedWordsButton);
        row0.add(deleteWordButton);
        this.add(row0, gbc);


        gbc.gridy = 1;
        gbc.gridx = 0;
        JPanel row1 = new JPanel();
        var addWordLabel = new JLabel("Add word:");
        addWordLabel.setPreferredSize(new Dimension(120,40));
        row1.add(addWordLabel);
        row1.add(newWordTextField);
        row1.add(addWordButton);
        this.add(row1, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        JPanel row2 = new JPanel();
        var searchLabel = new JLabel("Search:");
        searchLabel.setPreferredSize(new Dimension(120,40));
        row2.add(searchLabel);
        row2.add(searchTextField);
        this.add(row2, gbc);



        gbc.gridy = 3;
        gbc.gridx = 0;
        JPanel row3 = new JPanel();
        JLabel listLabel = new JLabel("List:");
        listLabel.setPreferredSize(new Dimension(120,40));
        row3.add(listLabel);

        row3.add(listJComboBox);
        row3.add(addToListButton);
        this.add(row3, gbc);



    }


    /**
     * Getters
     */

    public JButton getSettingsButton() {
        return settingsButton;
    }

    public JButton getResetSelectedWordsButton() {
        return resetSelectedWordsButton;
    }

    public JTextField getNewWordTextField() {
        return newWordTextField;
    }

    public JButton getAddWordButton() {
        return addWordButton;
    }

    public JTextField getSearchTextField() {
        return searchTextField;
    }

    public JComboBox<String> getListJComboBox() {
        return listJComboBox;
    }

    public JButton getAddToListButton() {
        return addToListButton;
    }

    public JButton getDeleteWordButton() {
        return deleteWordButton;
    }

    public FunctionalitiesPanelController getController() {
        return controller;
    }
}
