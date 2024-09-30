package pl.com.words.gui.components.behavior;

import pl.com.words.gui.components.Panels;
import pl.com.words.model.Model;
import pl.com.words.model.Word;

import javax.swing.*;
import java.util.Comparator;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

public class PanelsController {

    private Panels panels;
    private Model model;

    public PanelsController(Panels panels, Model model) {
        this.panels = panels;
        this.model = model;
    }

    public void addBehavior() {
        this.panels.getSettingsPanel().getController().addBehavior();

        //todo add here behavior for next panels
    }







}
