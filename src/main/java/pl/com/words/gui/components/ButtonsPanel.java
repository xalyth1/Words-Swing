package pl.com.words.gui.components;

import pl.com.words.gui.components.behavior.ButtonsPanelController;
import pl.com.words.model.Model;

import javax.swing.*;
import java.awt.*;

public class ButtonsPanel extends JPanel {
    private Panels panels;
    private Model model;

    private ButtonsPanelController controller;

    public ButtonsPanel(Panels panels, Model model) {
        super(new FlowLayout());
        this.panels = panels;
        this.model = model;
        this.controller = new ButtonsPanelController(panels, this, model);
    }


    public ButtonsPanelController getController() {
        return controller;
    }
}
