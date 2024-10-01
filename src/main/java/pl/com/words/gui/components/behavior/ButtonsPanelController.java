package pl.com.words.gui.components.behavior;

import pl.com.words.api.WordsServiceApiClient;
import pl.com.words.gui.components.ButtonsPanel;
import pl.com.words.gui.components.Panels;
import pl.com.words.media.MP3Player;
import pl.com.words.model.Model;
import pl.com.words.model.Word;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.List;

public class ButtonsPanelController {
    private Panels panels;
    private ButtonsPanel buttonsPanel;
    private Model model;
    private WordsServiceApiClient service = new WordsServiceApiClient();

    public ButtonsPanelController(Panels panels, ButtonsPanel buttonsPanel, Model model) {
        this.panels = panels;
        this.buttonsPanel = buttonsPanel;
        this.model = model;
    }

    public void addBehavior() {
        this.setBehaviorToButtons(this.model);
    }

    private void setBehaviorToButtons(Model model) {
        JTextField searchTextField = panels.getFunctionalitiesPanel().getSearchTextField();
        JButton addToListButton = panels.getFunctionalitiesPanel().getAddToListButton();
        JCheckBox pronunciationButton = panels.getSettingsPanel().getPronunciationCheckBox();

        for (Component c : this.buttonsPanel.getComponents()) {
            if (c instanceof JButton b) {
                this.addBehaviorToWordButton(b, searchTextField, addToListButton,
                        model, pronunciationButton);
            }
        }
    }

    public void addBehaviorToWordButton(JButton b, JTextField focusTextField, JButton addToListButton, Model model,
                                         JCheckBox pronunciationButton) {
        b.addActionListener(e -> {
            focusTextField.requestFocus();
            String wordText = b.getText();
            Word w = model.get(wordText);
            manageWordSelection(w);

            List<Word> selected = model.getCurrentList().getList().stream()
                    .unordered().filter(Word::isSelected).toList();
            long selectedCount = selected.size();

            model.getCurrentList().setIsAnythingSelected(selectedCount > 0);

            if (model.getCurrentList().isAnythingSelected()) {
                addToListButton.setVisible(true);
            } else {
                addToListButton.setVisible(false);
            }

            managePronunciation(w.getHeadword(), pronunciationButton);
        });

    }

    private void managePronunciation(String headword, JCheckBox pronunciationButton) {
        if (pronunciationButton.isSelected()) {
            //request Words-Service API for mp3
            InputStream mp3Stream = service.getPronunciation(headword);

            MP3Player myPlayer = new MP3Player(mp3Stream);
            myPlayer.play();
        }
    }

    private void manageWordSelection(Word w) {
        if (!Model.USE_SELECTION_MODE)
            return;

        JButton b = w.getjButton();
        if (w.isSelected()) {
            w.setSelected(false);
            //set default colors:
            b.setBackground(new JButton().getBackground());
            b.setForeground(new JButton().getForeground());
        } else {
            w.setSelected(true);
            b.setBackground(Color.green);
            b.setForeground(Color.blue);
        }
    }

}
