package pl.com.words.gui;

import pl.com.words.model.ApplicationSettings;
import pl.com.words.model.Model;
import pl.com.words.model.WordsList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.GridBagConstraints.LINE_START;

/**
 * This is Words-Swing Application GUI
 * (c) xalyth
 * Creation date: 01-04 - 2024
 */
public class GUI extends JFrame implements Runnable {

    /**
     * Functions
     */
    private JButton addWordButton, deleteWordButton, settingsButton, resetSelectedWordsButton, addToListButton;
    private JTextField newWordTextField, searchTextField;

    private JComboBox<String> listJComboBox;

    /**
     * Display
     */
    private JTextArea definitionTextArea;
    private JScrollPane definitionScrollPane;
    private JScrollPane buttonsPanelScrollPane;

    /**
     * Panels
     */
    private JPanel buttonsPanel, southPanel, northPanel, centralPanel, meaningPanel, settingsPanel, functionalitiesPanel;

    /** Settings panel
     */
    JRadioButton displayModeDefault = new JRadioButton("Ordinal");
    JRadioButton displayModeAlphabetical = new JRadioButton("Alphabetical");

    /**Menu
     */
    JMenuBar menuBar;
    JMenu menu;
    JMenu submenu;
    JMenuItem addListMenuItem;
    JLabel currentListJLabel;

    Model model;

    public void run() {
    }

    public GUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(900,20);
        setPreferredSize(new Dimension(1350,1100));
        setTitle("Words - Now come easy!");
        ApplicationSettings.setUpFonts();

        this.initializeGUIStructure();
        this.organizeLayout();

        this.model = new Model();
        (new MockData()).addWords(buttonsPanel, definitionTextArea, model);



        this.addBehaviorToElements();

        //repaint();
        pack();
        setVisible(true);
    }

    private void addBehaviorToElements() {
        BehaviorManager manager = BehaviorManager.getInstance();
        manager.setBehaviorTo_SearchTextField(searchTextField, buttonsPanel);
        manager.addBehaviorTo_Buttons(buttonsPanel, searchTextField, addToListButton);
        manager.addBehaviorTo_SettingsButton(settingsButton, settingsPanel);
        manager.setBehaviorTo_DisplayMode(buttonsPanel, displayModeDefault, displayModeAlphabetical);
        manager.setBehaviorTo_resetSelectedWordsButton(resetSelectedWordsButton, addToListButton);
        manager.setBehaviorToFileMenuItem(addListMenuItem);
        manager.setBehaviorToDeleteWordsButton(deleteWordButton, buttonsPanel);

        manager.setBehaviorTo_listsJComboBox(listJComboBox, this, currentListJLabel, model);
    }

    private void initializeGUIStructure() {
        SwingElementsCreator creator = SwingElementsCreator.getInstance();
        this.settingsButton = creator.settingsButton();
        this.addWordButton = creator.addWordButton();
        this.deleteWordButton = creator.deleteWordButton();
        this.resetSelectedWordsButton = creator.resetSelectedWordsButton();

        this.newWordTextField = creator.newWordTextField();
        this.searchTextField = creator.searchTextField();

        this.definitionTextArea = creator.definitionTextArea();
        this.definitionScrollPane = creator.definitionScrollPane(this.definitionTextArea);

        this.currentListJLabel = creator.currentListJLabel();
        this.listJComboBox = creator.listsJComboBox(this, currentListJLabel);

        this.addToListButton = creator.addSelectedToListJButton();

        this.menuBar = creator.menuBar();
        this.menu = creator.menu();
        this.submenu = creator.fileSubMenu();
        this.addListMenuItem = creator.addListMenuItem();


        this.displayModeDefault.setBackground(Color.LIGHT_GRAY);
        this.displayModeAlphabetical.setBackground(Color.LIGHT_GRAY);

        JFrame.setDefaultLookAndFeelDecorated(true);
    }

    private void createPanels() {
        SwingPanelsCreator creator = SwingPanelsCreator.getInstance();
        this.northPanel = creator.north_Panel();
        this.southPanel = creator.south_Panel();
        this.centralPanel = creator.central_Panel();
        this.buttonsPanel = creator.buttons_Panel();
        this.meaningPanel = creator.meaning_Panel();
        this.settingsPanel = creator.settings_Panel(this.displayModeDefault, this.displayModeAlphabetical);
        this.functionalitiesPanel = creator.functionalities_Panel();
    }

    private void associateElements() {
        meaningPanel.add(definitionScrollPane);

        //centralPanel.add(buttonsPanel);
        this.buttonsPanelScrollPane = SwingElementsCreator.getInstance().buttonsPanelScrollPane(buttonsPanel);
        centralPanel.add(buttonsPanelScrollPane);

        northPanel.add(currentListJLabel);
        currentListJLabel.setBorder(new EmptyBorder(0, 50, 0, 50));
        northPanel.add(deleteWordButton);

        southPanel.add(meaningPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = LINE_START;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JPanel row0 = new JPanel();
        row0.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        row0.add(settingsButton);
        row0.add(resetSelectedWordsButton);
        functionalitiesPanel.add(row0, gbc);


        gbc.gridy = 1;
        gbc.gridx = 0;
        JPanel row1 = new JPanel();
        var addWordLabel = new JLabel("Add word:");
        addWordLabel.setPreferredSize(new Dimension(120,40));
        row1.add(addWordLabel);
        row1.add(newWordTextField);
        row1.add(addWordButton);
        functionalitiesPanel.add(row1, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        JPanel row2 = new JPanel();
        var searchLabel = new JLabel("Search:");
        searchLabel.setPreferredSize(new Dimension(120,40));
        row2.add(searchLabel);
        row2.add(searchTextField);
        functionalitiesPanel.add(row2, gbc);



        gbc.gridy = 3;
        gbc.gridx = 0;
        JPanel row3 = new JPanel();
        JLabel listLabel = new JLabel("List:");
        listLabel.setPreferredSize(new Dimension(120,40));
        row3.add(listLabel);

        row3.add(listJComboBox);
        row3.add(addToListButton);
        functionalitiesPanel.add(row3, gbc);

        //Menus
        submenu.add(addListMenuItem);
        menu.add(submenu);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        southPanel.add(functionalitiesPanel);
        southPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        //
//        southPanel.add(settingsButton);
//        southPanel.add(newWordTextField);
//        southPanel.add(addWordButton);
//        southPanel.add(searchTextField);

        buttonsPanelScrollPane.revalidate();
        buttonsPanelScrollPane.repaint();

        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    private void createLayout() {
        BorderLayout mainLayout = new BorderLayout();
        getContentPane().setLayout(mainLayout);
        getContentPane().add(northPanel, BorderLayout.PAGE_START);
        getContentPane().add(southPanel, BorderLayout.PAGE_END);
        getContentPane().add(centralPanel, BorderLayout.CENTER);
        getContentPane().add(settingsPanel, BorderLayout.LINE_START);
    }

    private void organizeLayout() {
        createPanels();
        associateElements();
        createLayout();
        //addBehaviorToElements();
    }
}
