package io.github.samera2022.mouse_macros.ui.frame.settings;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.manager.ConfigManager;
import io.github.samera2022.mouse_macros.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;
import static io.github.samera2022.mouse_macros.ui.frame.MainFrame.*;

public class HotkeyDialog extends JDialog {
    public HotkeyDialog(){
        //owner
        setTitle(Localizer.get("settings.custom_hotkey"));
        setModal(true);
        setLayout(new GridLayout(4, 2, 5, 5));
        JLabel l1 = new JLabel(Localizer.get("start_record") + ":");
        JLabel l2 = new JLabel(Localizer.get("stop_record") + ":");
        JLabel l3 = new JLabel(Localizer.get("execute_macro") + ":");
        JTextField t1 = new JTextField();
        JTextField t2 = new JTextField();
        JTextField t3 = new JTextField();
        t1.setText(getNativeKeyDisplayText(keyRecord));
        t2.setText(getNativeKeyDisplayText(keyStop));
        t3.setText(getNativeKeyDisplayText(keyPlay));
        t1.setEditable(false); t2.setEditable(false); t3.setEditable(false);
        JButton confirm = new JButton(Localizer.get("settings.custom_hotkey.confirm"));
        add(l1); add(t1);
        add(l2); add(t2);
        add(l3); add(t3);
        add(new JLabel()); add(new JLabel()); // 占位，保持布局整齐

        // 构建3行2列的GridLayout面板
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20)); // 上端15像素，左右各20像素间距
        gridPanel.add(l1); gridPanel.add(t1);
        gridPanel.add(l2); gridPanel.add(t2);
        gridPanel.add(l3); gridPanel.add(t3);
        // 底部按钮面板
        JPanel confirmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        confirmPanel.add(confirm);
        confirmPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0)); // 下端只留3像素间距
        // 主面板，垂直排列
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(gridPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(confirmPanel);
        setContentPane(mainPanel);
        if (config.enableDarkMode) {
            ComponentUtil.applyDarkMode(getContentPane(),this);
        } else {
            ComponentUtil.applyLightMode(getContentPane(),this);
        }
        // 捕获JNativeHook按键
        final int[] tempRecord = {keyRecord};
        final int[] tempStop = {keyStop};
        final int[] tempPlay = {keyPlay};
        NativeKeyListener keyListener = new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                if (t1.hasFocus()) {
                    tempRecord[0] = e.getKeyCode();
                    t1.setText(getNativeKeyDisplayText(tempRecord[0]));
                } else if (t2.hasFocus()) {
                    tempStop[0] = e.getKeyCode();
                    t2.setText(getNativeKeyDisplayText(tempStop[0]));
                } else if (t3.hasFocus()) {
                    tempPlay[0] = e.getKeyCode();
                    t3.setText(getNativeKeyDisplayText(tempPlay[0]));
                }
            }
            @Override public void nativeKeyReleased(NativeKeyEvent e) {}
            @Override public void nativeKeyTyped(NativeKeyEvent e) {}
        };
        GlobalScreen.addNativeKeyListener(keyListener);
        t1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { t1.requestFocus(); }
        });
        t2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { t2.requestFocus(); }
        });
        t3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { t3.requestFocus(); }
        });
        confirm.addActionListener(e -> {
            keyRecord = tempRecord[0];
            keyStop = tempStop[0];
            keyPlay = tempPlay[0];
            // 存储到keyMap，key与本地化一致
            config.keyMap.put("start_record", String.valueOf(keyRecord));
            config.keyMap.put("stop_record", String.valueOf(keyStop));
            config.keyMap.put("execute_macro", String.valueOf(keyPlay));
            ConfigManager.saveConfig(config); // 保存配置
            // 全局刷新热键绑定
            GlobalScreen.removeNativeKeyListener(MAIN_FRAME); // JNativeHook 没有 getNativeKeyListeners 方法，只能移除本实例     // 注销所有全局热键监听器（只保留当前实例）   // 先注销旧热键
            GlobalScreen.addNativeKeyListener(MAIN_FRAME);    // 注册当前实例为全局热键监听器    // 直接注册本实例（JNativeHook 会自动去重）    // 重新注册新热键
            // 更新主界面按钮文本
//            startBtn.setText(getStartBtnText());
//            stopBtn.setText(getStopBtnText());
//            playBtn.setText(getPlayBtnText());
            GlobalScreen.removeNativeKeyListener(keyListener);
            dispose();
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 180);
        setLocationRelativeTo(this);
    }
}
