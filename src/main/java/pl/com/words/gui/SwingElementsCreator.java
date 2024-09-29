package pl.com.words.gui;

import pl.com.words.model.ApplicationSettings;
import pl.com.words.model.Model;
import pl.com.words.model.WordsList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public final class SwingElementsCreator {
    //private static final SwingElementsCreator creator = new SwingElementsCreator();



    private Model model;
    SwingElementsCreator(Model model) {
        this.model = model;
    }




















    void addNewList(JComboBox jComboBox) {

    }







    JButton clearListJButton() {
        JButton clearListJButton = new JButton("Clear List");
        return clearListJButton;
    }



    JMenuBar menuBar() {
        return new JMenuBar();
    }

    JMenu menu() {
        JMenu menu = new JMenu("Menu");
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


}
