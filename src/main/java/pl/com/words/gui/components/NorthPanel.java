package pl.com.words.gui.components;

import pl.com.words.model.Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NorthPanel extends JPanel {

    private JLabel currentListJLabel;

    public NorthPanel(Model model) {
        super(new BorderLayout());
        this.currentListJLabel = this.currentListJLabel(model);

        this.add(currentListJLabel, BorderLayout.CENTER);
    }

    private JLabel currentListJLabel(Model model) {

        JLabel jLabel = new JLabel("Current list: " + model.getCurrentList().getListName(), JLabel.CENTER);
        jLabel.setBorder(new EmptyBorder(15, 50, 15, 50));
        return jLabel;
    }


    /**
     * Getters
     */

    public JLabel getCurrentListJLabel() {
        return currentListJLabel;
    }
}
