package io.github.samera2022.mouse_macros;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import static io.github.samera2022.mouse_macros.ui.frame.MainFrame.MAIN_FRAME;

public class MouseMacro {
    public static void main(String[] args) {
        String dllName = "JNativeHook.x86_64.dll";
        String configDir = io.github.samera2022.mouse_macros.manager.ConfigManager.CONFIG_DIR;
        java.io.File libDir = new java.io.File(configDir+"/libs/", dllName);
        if (!libDir.exists()) libDir.mkdirs();
        System.setProperty("jnativehook.lib.path", libDir.getAbsolutePath());
        // ====== 启动主界面 ======
        SwingUtilities.invokeLater(() -> MAIN_FRAME.setVisible(true));
        System.out.println("当前 UI 外观: " + UIManager.getLookAndFeel().getName());
        System.out.println("当前 UI 类名: " + UIManager.getLookAndFeel().getClass().getName());
    }
}