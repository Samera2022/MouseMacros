package io.github.samera2022.mouse_macros;

import javax.swing.*;

import static io.github.samera2022.mouse_macros.ui.frame.MainFrame.MAIN_FRAME;

public class MouseMacro {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> MAIN_FRAME.setVisible(true));
    }
}