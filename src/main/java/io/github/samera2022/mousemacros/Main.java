package io.github.samera2022.mousemacros;

import io.github.samera2022.mousemacros.config.ConfigManager;

import javax.swing.*;

import static io.github.samera2022.mousemacros.ui.frame.MainFrame.MAIN_FRAME;

public class Main {
    public static void main(String[] args) {
        String dllName = "JNativeHook.x86_64.dll";
        String configDir = ConfigManager.CONFIG_DIR;
        java.io.File libDir = new java.io.File(configDir+"/libs/", dllName);
        if (!libDir.exists()) libDir.mkdirs();
        System.setProperty("jnativehook.lib.path", libDir.getAbsolutePath());
        SwingUtilities.invokeLater(() -> MAIN_FRAME.setVisible(true));
    }
}