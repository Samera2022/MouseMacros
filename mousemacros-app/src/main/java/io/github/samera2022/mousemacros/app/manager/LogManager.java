package io.github.samera2022.mousemacros.app.manager;

import io.github.samera2022.mousemacros.app.ui.frame.MainFrame;

public class LogManager {
    public static void log(String msg) {
        MainFrame.appendSystemLog(msg);
        System.out.println(msg);
    }
}
