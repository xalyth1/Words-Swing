package pl.com.words.gui.components;

import pl.com.words.model.Model;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private JRadioButton displayModeDefault = new JRadioButton("Ordinal");
    private JRadioButton displayModeAlphabetical = new JRadioButton("Alphabetical");

    private JCheckBox pronunciationCheckBox = new JCheckBox("Pronunciation");
    private JCheckBox selectionCheckBox = new JCheckBox("Selection");

    public SettingsPanel() {
        this.displayModeDefault.setBackground(Color.LIGHT_GRAY);
        this.displayModeAlphabetical.setBackground(Color.LIGHT_GRAY);

        this.createSettingsPanelElements();

    }


    private void createSettingsPanelElements() {
        JLabel displayMode = new JLabel("Display mode:     ");

        ButtonGroup displayModeGroup = new ButtonGroup();
        displayModeGroup.add(displayModeDefault);
        displayModeGroup.add(displayModeAlphabetical);

        displayModeDefault.setFont(displayMode.getFont());
        displayModeAlphabetical.setFont(displayMode.getFont());
        displayModeAlphabetical.setMargin(new Insets(0,0,20,0));

        JLabel wordSelectionLabel = new JLabel("Interaction mode:");

        pronunciationCheckBox.setSelected(true);
        selectionCheckBox.setSelected(true);
        selectionCheckBox.addActionListener( e-> {
            boolean newValue = selectionCheckBox.isSelected();
            Model.USE_SELECTION_MODE = newValue;
        });

        pronunciationCheckBox.setBackground(Color.LIGHT_GRAY);
        selectionCheckBox.setBackground(Color.LIGHT_GRAY);

        pronunciationCheckBox.setFont(displayMode.getFont());
        selectionCheckBox.setFont(displayMode.getFont());

        //JPanel settingsPanel = new JPanel();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(300,640));
        this.setVisible(false);
        this.setBackground(Color.LIGHT_GRAY);

        this.add(displayMode);
        this.add(displayModeDefault);
        this.add(displayModeAlphabetical);

        this.add(wordSelectionLabel);
        this.add(pronunciationCheckBox);
        this.add(selectionCheckBox);
    }


    /**
     * Getters
     */

    public JRadioButton getDisplayModeDefault() {
        return displayModeDefault;
    }

    public JRadioButton getDisplayModeAlphabetical() {
        return displayModeAlphabetical;
    }

    public JCheckBox getPronunciationCheckBox() {
        return pronunciationCheckBox;
    }

    public JCheckBox getSelectionCheckBox() {
        return selectionCheckBox;
    }
}
