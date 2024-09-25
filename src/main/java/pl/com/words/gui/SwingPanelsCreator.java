package pl.com.words.gui;

import pl.com.words.model.Model;

import javax.swing.*;
import java.awt.*;

public class SwingPanelsCreator {
    private static final SwingPanelsCreator creator = new SwingPanelsCreator();

    private SwingPanelsCreator() { }

    public static SwingPanelsCreator getInstance() {
        return creator;
    }

    JPanel north_Panel() {
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        northPanel.setBackground(Color.LIGHT_GRAY);
        return northPanel;
    }

    JPanel settings_Panel(JRadioButton displayModeDefault, JRadioButton displayModeAlphabetical, Model model) {
        JLabel displayMode = new JLabel("Display mode:     ");
        Font font = displayMode.getFont();
        //JRadioButton displayModeList = new JRadioButton();

        ButtonGroup displayModeGroup = new ButtonGroup();
        displayModeGroup.add(displayModeDefault);
        displayModeGroup.add(displayModeAlphabetical);

        displayModeDefault.setFont(displayMode.getFont());
        displayModeAlphabetical.setFont(displayMode.getFont());
        displayModeAlphabetical.setMargin(new Insets(0,0,20,0));


        /**
         * 1. pronunciation sound off - nothing on click
         * 2. pronunciation only on click
         * 3. pronunciation off AND selection
         * 4. pronunciatino on AND selection
         *
         * 1. pronunciation off - selection off
         * 2. pronunciation on  - selection off
         * 3. pronunciation off - selection on
         * 4. pronunciation on  - selection on
         *
         */
        JLabel wordSelectionLabel = new JLabel("Interaction mode:");
        JCheckBox pronunciationCheckBox = new JCheckBox("Pronunciation");
        JCheckBox selectionCheckBox = new JCheckBox("Selection");
//        selectionCheckBox.addActionListener( e-> MockData.SELECTION_MODE = !MockData.SELECTION_MODE );

        pronunciationCheckBox.setSelected(true);
        selectionCheckBox.setSelected(true);
        Model.SELECTION_MODE = selectionCheckBox.isSelected();
        selectionCheckBox.addActionListener( e-> Model.SELECTION_MODE = !selectionCheckBox.isSelected());

        pronunciationCheckBox.setBackground(Color.LIGHT_GRAY);
        selectionCheckBox.setBackground(Color.LIGHT_GRAY);

        pronunciationCheckBox.setFont(displayMode.getFont());
        selectionCheckBox.setFont(displayMode.getFont());


        JPanel settingsPanel = new JPanel();
        //settingsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setPreferredSize(new Dimension(300,640));
        settingsPanel.setVisible(false);
        settingsPanel.setBackground(Color.LIGHT_GRAY);

        settingsPanel.add(displayMode);
        settingsPanel.add(displayModeDefault);
        settingsPanel.add(displayModeAlphabetical);

        settingsPanel.add(wordSelectionLabel);
        settingsPanel.add(pronunciationCheckBox);
        settingsPanel.add(selectionCheckBox);

        return settingsPanel;
    }

    JPanel central_Panel() {
        return new JPanel();
    }

    JPanel south_Panel() {
        JPanel southPanel = new JPanel();
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return southPanel;
    }

    JPanel buttons_Panel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setPreferredSize(new Dimension(950,3000));
        return buttonsPanel;
    }


    JPanel meaning_Panel() {
        return new JPanel();
    }

    JPanel functionalities_Panel() {
        JPanel functionalitiesPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();

        functionalitiesPanel.setLayout(layout);
        return functionalitiesPanel;
    }

}
