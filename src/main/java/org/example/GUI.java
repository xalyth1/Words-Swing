package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
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

    private JComboBox listJComboBox;

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

    public static void main(String[] args)  {
        SwingUtilities.invokeLater(new GUI());
    }

    public void run() {
    }

    public GUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", new Font("Arial", Font.PLAIN, 28));

        UIManager.put("ToolTip.font", new FontUIResource(new Font("SansSerif", Font.PLAIN, 18)));
        //setSize(100, 100);
        //setResizable(false);
        //setLocationRelativeTo(null);
        setLocation(900,20);
        setPreferredSize(new Dimension(1350,1100));
        setTitle("Words - Now come easy!");
        SwingUtils.setGlobalFont();

        this.initializeGUIStructure();
        this.organizeLayout();

        MockData.addWords(buttonsPanel, definitionTextArea);

        BehaviorManager.getInstance().setBehaviorTo_SearchTextField(searchTextField, buttonsPanel);
        BehaviorManager.getInstance().addBehaviorTo_Buttons(buttonsPanel, searchTextField, addToListButton);
        BehaviorManager.getInstance().addBehaviorTo_SettingsButton(settingsButton, settingsPanel);
        BehaviorManager.getInstance().setBehaviorTo_DisplayMode(buttonsPanel, displayModeDefault, displayModeAlphabetical);
        BehaviorManager.getInstance().setBehaviorTo_resetSelectedWordsButton(resetSelectedWordsButton, addToListButton);
        BehaviorManager.getInstance().setBehaviorToFileMenuItem(addListMenuItem);
        BehaviorManager.getInstance().setBehaviorToDeleteWordsButton(deleteWordButton, buttonsPanel);


        //repaint();
        pack();
        setVisible(true);
    }

    private void initializeGUIStructure() {
        SwingElementsCreator creator = SwingElementsCreator.getInstance();
        this.settingsButton = creator.settings_Button();
        this.addWordButton = creator.add_Word_Button();
        this.deleteWordButton = creator.delete_Word_Button();
        this.resetSelectedWordsButton = creator.resetSelectedWords_Button();

        this.newWordTextField = creator.newWord_TextField();
        this.searchTextField = creator.search_TextField();

        this.definitionTextArea = creator.definition_TextArea();
        this.definitionScrollPane = creator.definition_ScrollPane(this.definitionTextArea);

        this.currentListJLabel = creator.currentListJLabel();
        this.listJComboBox = creator.lists_JComboBox(this, currentListJLabel);
        //https://stackoverflow.com/questions/7427511/java-swing-dynamic-jcombobox
        //TODO make dynamic jcombo box as on this picture
        // https://vaadin.com/docs/v8/framework/components/components-combobox

        this.addToListButton = creator.addSelectedToList_JButton();

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
        this.buttonsPanelScrollPane = SwingElementsCreator.getInstance().buttonsPanel_ScrollPane(buttonsPanel);
        centralPanel.add(buttonsPanelScrollPane); // TODO Why not showin up?


        northPanel.add(currentListJLabel);
        currentListJLabel.setBorder(new EmptyBorder(0, 50, 0, 50));
        northPanel.add(deleteWordButton);


        southPanel.add(meaningPanel);
        //

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = LINE_START;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JPanel row0 = new JPanel();
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


    private void addBehaviorToElements() {
        // behavior creator class or something like that
        BehaviorManager manager = BehaviorManager.getInstance();

    }

    private void organizeLayout() {
        createPanels();
        associateElements();
        createLayout();
        addBehaviorToElements();


        //TODO start here
    }



}
