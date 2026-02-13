package io.github.samera2022.mousemacros.app;

import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.event.EventManager;
import io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent;
import io.github.samera2022.mousemacros.app.script.ScriptManager;
import io.github.samera2022.mousemacros.app.util.ConsoleOutputCapturer;

import javax.swing.*;

import static io.github.samera2022.mousemacros.app.ui.frame.MainFrame.MAIN_FRAME;

public class Main {
    public static void main(String[] args) {
        ConsoleOutputCapturer.start();
        String dllName = "JNativeHook.x86_64.dll";
        String configDir = ConfigManager.CONFIG_DIR;
        java.io.File libDir = new java.io.File(configDir+"/libs/", dllName);
        if (!libDir.exists()) libDir.mkdirs();
        System.setProperty("jnativehook.lib.path", libDir.getAbsolutePath());
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        try {
            if (ConfigManager.getBoolean("enable_dark_mode")) UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            else UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> MAIN_FRAME.setVisible(true));
        
        // 初始化脚本系统 - 使用统一的加载和处理方法
        ScriptManager.loadAndProcessScripts();
        EventManager.callEvent(new OnAppLaunchedEvent("2.0.0", System.getProperty("java.version")));
    }
}
