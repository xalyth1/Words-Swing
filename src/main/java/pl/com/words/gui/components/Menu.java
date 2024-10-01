package pl.com.words.gui.components;

import pl.com.words.gui.components.behavior.MenuController;
import pl.com.words.model.Model;

import javax.swing.*;

public class Menu {
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenu subMenu;
    private JMenuItem addListMenuItem;

    private Model model;
    private Panels panels;
    private MenuController controller;

    public Menu(Model model, Panels panels) {
        this.menuBar = this.menuBar();
        this.menu = this.menu();
        this.subMenu = this.fileSubMenu();
        this.addListMenuItem = this.addListMenuItem();

        this.createMenuStructure();

        this.panels = panels;
        this.model = model;
        this.controller = new MenuController(this, this.panels, this.model);
        this.controller.addBehavior();
    }

    private void createMenuStructure() {
        this.subMenu.add(this.addListMenuItem);
        this.menu.add(this.subMenu);
        this.menuBar.add(menu);
    }

    private JMenuBar menuBar() {
        return new JMenuBar();
    }

    private JMenu menu() {
        JMenu menu = new JMenu("Menu");
        return menu;
    }

    private JMenu fileSubMenu() {
        JMenu submenu = new JMenu("Lists");
        return submenu;
    }

    private JMenuItem addListMenuItem() {
        JMenuItem addNewListJMenuItem = new JMenuItem("Add New List");
        return addNewListJMenuItem;
    }



    /**
     * Getters
     */

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public JMenu getMenu() {
        return menu;
    }

    public JMenu getSubMenu() {
        return subMenu;
    }

    public JMenuItem getAddListMenuItem() {
        return addListMenuItem;
    }
}
