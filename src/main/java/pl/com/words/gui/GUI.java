package pl.com.words.gui;

import pl.com.words.gui.components.DefinitionPanel;
import pl.com.words.gui.components.Panels;
import pl.com.words.model.ApplicationSettings;
import pl.com.words.model.Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import pl.com.words.gui.components.Menu;

/**
 * This is Words-Swing Application GUI
 * (c) xalyth
 * Creation date: 01-04 - 2024
 * Updates and development: 07-10  -  2024
 */
public class GUI extends JFrame implements Runnable {
    Model model;
    Panels panels;
    Menu menu;


    public void run() {
    }

    public GUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(900,20);
        setPreferredSize(new Dimension(1350,1100));
        setTitle("Words - Now come easy!");
        ApplicationSettings.setUpFonts();


        this.model = new Model();
        this.panels = new Panels(model);
        this.menu = new Menu();

        this.initializeGUIStructure(model);
        this.organizeLayout(model);

        this.addBehaviorToElements(model);

        //repaint();
        pack();
        setVisible(true);
    }

    private void addBehaviorToElements(Model model) {
        BehaviorManager manager = new BehaviorManager(model);

        manager.addWords(panels.getButtonsPanel(), panels.getDefinitionPanel().getDefinitionTextArea(), model.getCurrentList());

        manager.setBehaviorTo_SearchTextField(panels.getFunctionalitiesPanel().getSearchTextField(), panels.getButtonsPanel());

        JPanel buttonsPanel = panels.getButtonsPanel();
        manager.addBehaviorTo_Buttons(buttonsPanel, panels.getFunctionalitiesPanel().getSearchTextField(), panels.getFunctionalitiesPanel().getAddToListButton(), model,
                panels.getSettingsPanel().getPronunciationCheckBox());

        JButton settingsButton = panels.getFunctionalitiesPanel().getSettingsButton();
        manager.addBehaviorTo_SettingsButton(settingsButton, panels.getSettingsPanel());



        manager.setBehaviorTo_DisplayMode(buttonsPanel, panels.getSettingsPanel().getDisplayModeDefault(), panels.getSettingsPanel().getDisplayModeAlphabetical());
        var resetSelectedWordsButton = panels.getFunctionalitiesPanel().getResetSelectedWordsButton();
        var addToListButton = panels.getFunctionalitiesPanel().getAddToListButton();
        manager.setBehaviorTo_resetSelectedWordsButton(resetSelectedWordsButton, addToListButton);
        manager.setBehaviorToFileMenuItem(this.menu.getAddListMenuItem());
        var deleteWordButton = panels.getFunctionalitiesPanel().getDeleteWordButton();
        manager.setBehaviorToDeleteWordsButton(deleteWordButton, buttonsPanel, model);

        var listJComboBox = panels.getFunctionalitiesPanel().getListJComboBox();
        var currentListJLabel = panels.getNorthPanel().getCurrentListJLabel();
        var definitionTextArea = panels.getDefinitionPanel().getDefinitionTextArea();
        manager.setBehaviorTo_listsJComboBox(listJComboBox, this, currentListJLabel, model, buttonsPanel,
                definitionTextArea);

        var addWordButton = panels.getFunctionalitiesPanel().getAddWordButton();
        var newWordTextField = panels.getFunctionalitiesPanel().getNewWordTextField();
        var searchTextField = panels.getFunctionalitiesPanel().getSearchTextField();
        var pronunciationCheckBox = panels.getSettingsPanel().getPronunciationCheckBox();
        manager.setBehaviorTo_Add_Button(addWordButton, newWordTextField, buttonsPanel,
                definitionTextArea, searchTextField, addToListButton, model, pronunciationCheckBox);
    }

    private void initializeGUIStructure(Model model) {

        JFrame.setDefaultLookAndFeelDecorated(true);
    }

    private void associateElements() {
        this.setJMenuBar(this.menu.getMenuBar());

        JPanel buttonsPanel = panels.getButtonsPanel();
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    private void createLayout() {
        BorderLayout mainLayout = new BorderLayout();
        getContentPane().setLayout(mainLayout);
        getContentPane().add(panels.getNorthPanel(), BorderLayout.PAGE_START);
        getContentPane().add(panels.getSouthPanel(), BorderLayout.PAGE_END);
        getContentPane().add(panels.getCentralPanel(), BorderLayout.CENTER);
        getContentPane().add(panels.getSettingsPanel(), BorderLayout.LINE_START);
    }

    private void organizeLayout(Model model) {

        associateElements();
        createLayout();
    }
}