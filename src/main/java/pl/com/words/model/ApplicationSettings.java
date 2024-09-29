package pl.com.words.model;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class ApplicationSettings {
    public static final Font applicationFont = new Font("Helvetica",Font.BOLD, 24);
    public static final Font menuFont = new Font("Helvetica",Font.BOLD, 18);

    public static final Dimension PREFERRED_BUTTON_DIMENSION = new Dimension(140, 40);

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


//    public static void setGrayUserInterfaceColours() {
//        UIManager.put("Panel.background", new Color(60, 116, 151));
//        UIManager.put("Button.background", new Color(87, 42, 42));
//        UIManager.put("Button.foreground", new Color(50, 50, 50));
//        UIManager.put("TextField.background", new Color(230, 230, 230));
//        UIManager.put("TextField.foreground", new Color(60, 60, 60));
//        UIManager.put("TextField.border", BorderFactory.createLineBorder(new Color(180, 180, 180)));
//    }
}