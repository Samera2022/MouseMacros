package io.github.samera2022.mouse_macros.manager;

import io.github.samera2022.mouse_macros.ui.frame.MainFrame;

import javax.swing.*;

public class LogManager {
    public static void log(String msg) {
        SwingUtilities.invokeLater(() -> MainFrame.logArea.append(msg + "\n"));
    }
}
