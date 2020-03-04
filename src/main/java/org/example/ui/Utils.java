package org.example.ui;

import javax.swing.*;

public class Utils {
    public static void disableButtons(JButton... buttons) {
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
    }
}
