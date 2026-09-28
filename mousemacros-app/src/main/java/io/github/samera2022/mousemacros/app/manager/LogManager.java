package io.github.samera2022.mousemacros.app.manager;

import io.github.samera2022.mousemacros.app.ui.frame.MainFrame;

import javax.swing.*;

public class LogManager {
    public static void log(String msg) {
        SwingUtilities.invokeLater(() -> MainFrame.logArea.append(msg + "\n"));
        System.out.println(msg);
    }
}
