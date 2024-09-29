package pl.com.words.gui.components;

import javax.swing.*;
import java.awt.*;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;

public class DefinitionPanel extends JPanel {
    private JTextArea definitionTextArea;
    private JScrollPane definitionScrollPane;

    public DefinitionPanel() {
        this.definitionTextArea = this.createDefinitionTextArea();
        this.definitionScrollPane = this.createDefinitionScrollPane(this.definitionTextArea);
        this.add(definitionScrollPane);
    }

    private JScrollPane createDefinitionScrollPane(JTextArea jTextArea) {
        JScrollPane definitionScrollPane = new JScrollPane(jTextArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);
        definitionScrollPane.setVisible(true);
        definitionScrollPane.setPreferredSize(new Dimension(300,300));
        return definitionScrollPane;
    }

    private JTextArea createDefinitionTextArea() {
        JTextArea definitionTextArea = new JTextArea();
        definitionTextArea.setEditable(false);
        definitionTextArea.setLineWrap(true);
        definitionTextArea.setWrapStyleWord(true);
        return definitionTextArea;
    }

    public JTextArea getDefinitionTextArea() {
        return definitionTextArea;
    }
}
