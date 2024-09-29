package pl.com.words.gui;

import pl.com.words.gui.components.DefinitionPanel;
import pl.com.words.gui.components.Panels;
import pl.com.words.model.ApplicationSettings;
import pl.com.words.model.Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.GridBagConstraints.LINE_START;

/**
 * This is Words-Swing Application GUI
 * (c) xalyth
 * Creation date: 01-04 - 2024
 * Updates and development: 07-10  -  2024
 */
public class GUI extends JFrame implements Runnable {

    //private JScrollPane buttonsPanelScrollPane;

    /**Menu
     */
    JMenuBar menuBar;
    JMenu menu;
    JMenu submenu;
    JMenuItem addListMenuItem;

    /**
     * Model
     */
    Model model;
    Panels panels;

    SwingElementsCreator swingElementsCreator;

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

        this.swingElementsCreator = new SwingElementsCreator(model);

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
        manager.setBehaviorToFileMenuItem(addListMenuItem);
        var deleteWordButton = panels.getNorthPanel().getDeleteWordButton();
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
        SwingElementsCreator creator = new SwingElementsCreator(model);




        this.menuBar = creator.menuBar();
        this.menu = creator.menu();
        this.submenu = creator.fileSubMenu();
        this.addListMenuItem = creator.addListMenuItem();

        JFrame.setDefaultLookAndFeelDecorated(true);
    }

    private void associateElements(Model model) {
        //Menus
        submenu.add(addListMenuItem);
        menu.add(submenu);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);


        //
//        southPanel.add(settingsButton);
//        southPanel.add(newWordTextField);
//        southPanel.add(addWordButton);
//        southPanel.add(searchTextField);


//        buttonsPanelScrollPane.revalidate();
//        buttonsPanelScrollPane.repaint();

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

        associateElements(model);
        createLayout();
    }
}