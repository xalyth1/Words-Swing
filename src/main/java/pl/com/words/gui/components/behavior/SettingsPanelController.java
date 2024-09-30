package pl.com.words.gui.components.behavior;

import pl.com.words.api.WordsServiceApiClient;
import pl.com.words.gui.components.Panels;
import pl.com.words.gui.components.SettingsPanel;
import pl.com.words.media.MP3Player;
import pl.com.words.model.Model;
import pl.com.words.model.Word;

import javax.swing.*;
import java.io.InputStream;
import java.util.Comparator;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

public class SettingsPanelController {

    Panels panels;
    SettingsPanel settingsPanel;
    Model model;
    WordsServiceApiClient service = new WordsServiceApiClient();

    public SettingsPanelController(Panels panels, SettingsPanel settingsPanel, Model model) {
        this.panels = panels;
        this.settingsPanel = settingsPanel;
        this.model = model;
    }

    public void addBehavior() {
        this.setBehaviorTo_DisplayMode();
    }

    /**
     * Settings Panel's behavior methods
     */
    private void setBehaviorTo_DisplayMode() {
        JPanel buttonsPanel = panels.getButtonsPanel();
        JRadioButton displayModeDefault = panels.getSettingsPanel().getDisplayModeDefault();
        JRadioButton displayModeAlphabetical = panels.getSettingsPanel().getDisplayModeAlphabetical();


        displayModeDefault.addActionListener(e -> {
            model.getCurrentList().getList().sort(Comparator.comparingInt(Word::getId));
            rearrangeWordButtons(buttonsPanel);
        });
        displayModeAlphabetical.addActionListener(e -> {
            model.getCurrentList().getList().sort(Comparator.comparing(Word::getHeadword, CASE_INSENSITIVE_ORDER));
            System.out.println(model.getCurrentList().getList().get(0).getHeadword());
            System.out.println(model.getCurrentList().getList().get(1).getHeadword());
            System.out.println(model.getCurrentList().getList().get(2));
            model.getCurrentList().getList().get(3);

            rearrangeWordButtons(buttonsPanel);
        });
    }

    private void rearrangeWordButtons(JPanel buttonsPanel) {
        buttonsPanel.removeAll();
        for (Word w : model.getCurrentList().getList()) {
            buttonsPanel.add(w.getjButton());
        }
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        //System.out.println(MockData.words.stream().map(x -> x.getWord()).findFirst());
    }




}
