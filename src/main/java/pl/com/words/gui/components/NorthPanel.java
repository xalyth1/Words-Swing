package pl.com.words.gui.components;

import pl.com.words.model.Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NorthPanel extends JPanel {
    private JButton deleteWordButton;
    private JLabel currentListJLabel;

    public NorthPanel(Model model) {
        this.deleteWordButton = this.deleteWordButton();
        this.currentListJLabel = this.currentListJLabel(model);
        this.currentListJLabel.setBorder(new EmptyBorder(0, 50, 0, 50));
        this.add(deleteWordButton);
        this.add(currentListJLabel);
    }

    private JButton deleteWordButton() {
        JButton deleteWordButton = new JButton("Delete selected");
        deleteWordButton.setToolTipText("Delete selected words from current list");
        deleteWordButton.setPreferredSize(new Dimension(240,40));

        return deleteWordButton;
    }

    private JLabel currentListJLabel(Model model) {

        return new JLabel("Current list: " + model.getCurrentList().getListName());
    }


    /**
     * Getters
     */

    public JButton getDeleteWordButton() {
        return deleteWordButton;
    }

    public JLabel getCurrentListJLabel() {
        return currentListJLabel;
    }
}
