package pl.com.words.model;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class ApplicationSettings {
    public static final Font applicationFont = new Font("Helvetica",Font.BOLD, 24);
    public static final Font menuFont = new Font("Helvetica",Font.BOLD, 18);

    public static final Dimension PREFERRED_BUTTON_DIMENSION = new Dimension(140, 40);

    public static void setupUIDesign() {
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


    public static void setAlternativeUserInterfaceColours() {
        // Fonts
        UIManager.put("Button.font", applicationFont);
        UIManager.put("Label.font", applicationFont);
        UIManager.put("ComboBox.font", applicationFont);
        UIManager.put("ToolTip.font", new FontUIResource(new Font("SansSerif", Font.PLAIN, 18)));
        UIManager.put("TextField.font", new Font("Helvetica", Font.PLAIN, 28));
        UIManager.put("TextArea.font", applicationFont);

        //Colors
        Color panelBG = Color.decode("#F0F0F0");
        Color buttonBG = Color.decode("#2196F3");
        Color buttonFont = Color.decode("#FFFFFF");

        UIManager.put("Panel.background", panelBG);
        UIManager.put("Button.background", buttonBG);
        UIManager.put("Button.foreground", buttonFont);
        //UIManager.put("Button.foreground", new Color(50, 50, 50));
        UIManager.put("TextField.background", new Color(230, 230, 230));
        UIManager.put("TextField.foreground", new Color(60, 60, 60));
        UIManager.put("TextField.border", BorderFactory.createLineBorder(new Color(180, 180, 180)));
    }

    void colorRepo() {
        Color davysGray = new Color(93,94,96);
        Color outerSpace = new Color(54, 65, 62);
        Color taupeGray;
        Color thistie;
        Color timberWolf = new Color(215, 214, 214);
        Color granat = new Color(27,38,59);
        Color bialy = new Color(224,225,221);
    }
}