package org.example;

import javax.swing.*;
import java.awt.*;

public class SwingUtils {
    public static void setGlobalFont() {
        Font font = new Font("Helvetica", Font.BOLD, 24);
        UIManager.put("Button.font", font);
        UIManager.put("Label.font", font);
        // Add more component types and properties as needed
        UIManager.getDefaults().put("TextArea.font", UIManager.getFont("Label.font"));
    }


}
