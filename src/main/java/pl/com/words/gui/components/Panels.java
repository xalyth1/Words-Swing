package pl.com.words.gui.components;

import pl.com.words.gui.GUI;
import pl.com.words.model.Model;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.LINE_START;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class Panels {

    GUI gui;
    /**
     * Panels
     */
    private JPanel southPanel, centralPanel;
    private NorthPanel northPanel;
    private DefinitionPanel definitionPanel;
    private SettingsPanel settingsPanel;
    private FunctionalitiesPanel functionalitiesPanel;
    private JPanel buttonsPanel;

    public Panels(Model model) {
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


        this.southPanel.revalidate();
        this.southPanel.repaint();
    }

    /**
     *
     * Create the respective Panels
     */
    private NorthPanel createNorthPanel(Model model) {
        NorthPanel northPanel = new NorthPanel(model);
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
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
