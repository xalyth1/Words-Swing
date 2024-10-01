package pl.com.words.gui.components.behavior;


import pl.com.words.gui.components.Menu;
import pl.com.words.gui.components.Panels;
import pl.com.words.model.Model;

import javax.swing.*;

public class MenuController {
    private Model model;
    private Panels panels;
    private Menu menu;
    public MenuController(Menu menu, Panels panels, Model model) {
        this.model = model;
        this.menu = menu;
        this.panels = panels;
    }

    public void addBehavior() {
        setBehaviorToFileMenuItem(menu.getAddListMenuItem());
    }


    /**
     * Behavior
     */
    private void setBehaviorToFileMenuItem(JMenuItem addListMenuItem) {
        addListMenuItem.addActionListener(e -> {
            // open dialog, input text with list name, check if name does not exist if so, display communicate, else add new listName to Lists list
            System.out.println("set behavior");

            JComboBox<String> listJComboBox = this.panels.getFunctionalitiesPanel().getListJComboBox();
            DefaultComboBoxModel<String> m = (DefaultComboBoxModel<String>) listJComboBox.getModel();

            this.panels.getFunctionalitiesPanel()
                    .getController()
                    .getUserInputController()
                    .handleAddingList(this.panels.getGui(), m, this.model);
        });
    }
}
