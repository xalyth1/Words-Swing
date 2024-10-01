package pl.com.words.gui.components.behavior;

import pl.com.words.model.Model;
import pl.com.words.model.WordsList;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Vector;

public class UserInputController {


    public void handleAddingList(JFrame frame, DefaultComboBoxModel<String> m, Model model) {
        String resultListName = showTextInputDialog(frame, "Enter new list name:");
        if (resultListName != null) {
            if (listNameExists(resultListName, m)) {
                JOptionPane.showMessageDialog(frame,
                        "\"" + resultListName + "\" list name already exists!");
            } else {
                m.insertElementAt(resultListName, m.getSize() - 1);
                model.addWordsList(new WordsList(resultListName, new ArrayList<>()));
                JOptionPane.showMessageDialog(frame, "Added new \"" + resultListName + "\" list!");
            }
        }
    }
    private String showTextInputDialog(JFrame parent, String message) {
        JTextField textField = new JTextField();
        Object[] messageComponents = {message, textField};
        int option = JOptionPane.showConfirmDialog(parent, messageComponents, "Create new list", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            return textField.getText();
        }
        return null;
    }
    private boolean listNameExists(String list, ComboBoxModel m) {
        Vector<String> v = new Vector<>();
        for (int i = 0; i < m.getSize(); i++) {
            v.add((String)m.getElementAt(i));
        }
        return v.contains(list);

    }
}
