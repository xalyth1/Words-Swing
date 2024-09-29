package pl.com.words.gui.components;

import javax.swing.*;

public class Menu {
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenu subMenu;
    private JMenuItem addListMenuItem;

    public Menu() {
        this.menuBar = this.menuBar();
        this.menu = this.menu();
        this.subMenu = this.fileSubMenu();
        this.addListMenuItem = this.addListMenuItem();

        this.createMenuStructure();
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
