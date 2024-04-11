package pl.com.words.gui;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class SwingUtils {
    public static void setGlobalFont() {
        Font font = new Font("Helvetica", Font.BOLD, 24);
        UIManager.put("Button.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("ToolTip.font", new FontUIResource(new Font("SansSerif", Font.PLAIN, 18)));
        // Add more component types and properties as needed
        UIManager.getDefaults().put("TextArea.font", UIManager.getFont("Label.font"));


//        UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Arial", Font.PLAIN, 28));
//        UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Britannic Bold", Font.PLAIN, 28));
    }


}
