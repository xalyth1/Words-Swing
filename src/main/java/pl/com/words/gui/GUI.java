package pl.com.words.gui;

import pl.com.words.gui.components.Panels;
import pl.com.words.model.ApplicationSettings;
import pl.com.words.model.Model;

import javax.swing.*;
import java.awt.*;

import pl.com.words.gui.components.Menu;

/**
 * This is Words-Swing Application GUI
 * (c) xalyth
 * Creation date: 01-04 - 2024
 * Updates and development: 07-12  -  2024
 */
public class GUI extends JFrame implements Runnable {
    private Model model;
    private Panels panels;
    private Menu menu;

    public void run() {
    }

    public GUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(900,20);
        setPreferredSize(new Dimension(1350,1100));
        setTitle("Words - Now come easy!");
        ApplicationSettings.setupUIDesign();
        //ApplicationSettings.setAlternativeUserInterfaceColours();

        this.model = new Model();
        this.panels = new Panels(this, model);
        this.menu = new Menu(this.model, this.panels);

        this.initializeGUIStructure(model);
        this.createLayout();

        this.setJMenuBar(this.menu.getMenuBar());

        //repaint();
        pack();
        setVisible(true);
    }

    private void initializeGUIStructure(Model model) {

        JFrame.setDefaultLookAndFeelDecorated(true);
    }

    private void refreshButtonsPanel() {
        JPanel buttonsPanel = panels.getButtonsPanel();
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    private void createLayout() {
        BorderLayout mainLayout = new BorderLayout();
        this.getContentPane().setLayout(mainLayout);
        this.getContentPane().add(this.panels.getNorthPanel(), BorderLayout.PAGE_START);
        this.getContentPane().add(this.panels.getSouthPanel(), BorderLayout.PAGE_END);
        this.getContentPane().add(this.panels.getCentralPanel(), BorderLayout.CENTER);
        this.getContentPane().add(this.panels.getSettingsPanel(), BorderLayout.LINE_START);
    }
}