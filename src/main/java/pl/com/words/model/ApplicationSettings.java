package pl.com.words.model;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class ApplicationSettings {
    public static final Font applicationFont = new Font("Helvetica",Font.BOLD, 24);
    public static final Font menuFont = new Font("Helvetica",Font.BOLD, 18);

    public static void setUpFonts() {
        setMenuFont();

        UIManager.put("Button.font", applicationFont);
        UIManager.put("Label.font", applicationFont);
        UIManager.put("ComboBox.font", applicationFont);
        UIManager.put("ToolTip.font", new FontUIResource(new Font("SansSerif", Font.PLAIN, 18)));
//        UIManager.getDefaults().put("TextArea.font", UIManager.getFont("Label.font"));
        UIManager.put("TextField.font", new Font("Helvetica", Font.PLAIN, 28));
        UIManager.put("TextArea.font", applicationFont);
    }

    private static void setMenuFont() {
        UIManager.put("Menu.font", menuFont);
        UIManager.put("MenuItem.font", menuFont);
    }
}