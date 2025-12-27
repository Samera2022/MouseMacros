package io.github.samera2022.mouse_macros.ui.frame.settings;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.adapter.WindowClosingAdapter;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.manager.CacheManager;
import io.github.samera2022.mouse_macros.manager.ConfigManager;
import io.github.samera2022.mouse_macros.ui.frame.MainFrame;
import io.github.samera2022.mouse_macros.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;
import static io.github.samera2022.mouse_macros.ui.frame.MainFrame.*;
import static io.github.samera2022.mouse_macros.util.OtherUtil.getNativeKeyDisplayText;

public class HotkeyDialog extends JDialog {
    public static boolean inHotKeyDialog = false;
    public HotkeyDialog(){
        inHotKeyDialog = true;
        //owner
        setTitle(Localizer.get("settings.custom_hotkey"));
        setName(Localizer.get("settings.custom_hotkey"));
        setModal(true);
        setLayout(new GridLayout(4, 2, 5, 5));
        JLabel l1 = new JLabel(Localizer.get("start_record") + ":");
        JLabel l2 = new JLabel(Localizer.get("stop_record") + ":");
        JLabel l3 = new JLabel(Localizer.get("play_macro") + ":");
        JLabel l4 = new JLabel(Localizer.get("abort_macro_operation") + ":");
        JTextField t1 = new JTextField();
        JTextField t2 = new JTextField();
        JTextField t3 = new JTextField();
        JTextField t4 = new JTextField();
        t1.setText(getNativeKeyDisplayText(keyRecord));
        t2.setText(getNativeKeyDisplayText(keyStop));
        t3.setText(getNativeKeyDisplayText(keyPlay));
        t4.setText(getNativeKeyDisplayText(keyAbort));
        t1.setEditable(false); t2.setEditable(false); t3.setEditable(false); t4.setEditable(false);
        JButton confirm = new JButton(Localizer.get("settings.custom_hotkey.confirm"));
        add(l1); add(t1);
        add(l2); add(t2);
        add(l3); add(t3);
        add(l4); add(t4);
        add(new JLabel()); add(new JLabel()); // 占位，保持布局整齐

        // 构建3行2列的GridLayout面板
        JPanel gridPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20)); // 上端15像素，左右各20像素间距
        gridPanel.add(l1); gridPanel.add(t1);
        gridPanel.add(l2); gridPanel.add(t2);
        gridPanel.add(l3); gridPanel.add(t3);
        gridPanel.add(l4); gridPanel.add(t4);
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
        ComponentUtil.setMode(getContentPane(),config.enableDarkMode? OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
        ComponentUtil.applyWindowSizeCache(this, "HotkeyDialog", 500, 360);
        setLocationRelativeTo(this);

        // 捕获JNativeHook按键
        final int[] tempRecord = {keyRecord};
        final int[] tempStop = {keyStop};
        final int[] tempPlay = {keyPlay};
        final int[] tempAbort = {keyAbort};
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
                } else if (t4.hasFocus()) {
                    tempAbort[0] = e.getKeyCode();
                    t4.setText(getNativeKeyDisplayText(tempAbort[0]));
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
        t4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { t4.requestFocus(); }
        });
        confirm.addActionListener(e -> {
            keyRecord = tempRecord[0];
            keyStop = tempStop[0];
            keyPlay = tempPlay[0];
            keyAbort = tempAbort[0];
            // 存储到keyMap，key与本地化一致
            config.keyMap.put("start_record", String.valueOf(keyRecord));
            config.keyMap.put("stop_record", String.valueOf(keyStop));
            config.keyMap.put("play_macro", String.valueOf(keyPlay));
            config.keyMap.put("abort_macro_operation", String.valueOf(keyAbort));
            ConfigManager.saveConfig(config); // 保存配置
            ConfigManager.reloadConfig();
            // 全局刷新热键绑定
            GlobalScreen.removeNativeKeyListener(GML); // JNativeHook 没有 getNativeKeyListeners 方法，只能移除本实例     // 注销所有全局热键监听器（只保留当前实例）   // 先注销旧热键
            GlobalScreen.addNativeKeyListener(GML);    // 注册当前实例为全局热键监听器    // 直接注册本实例（JNativeHook 会自动去重）    // 重新注册新热键
            // 更新主界面按钮文本
//            startBtn.setText(getStartBtnText());
//            stopBtn.setText(getStopBtnText());
//            playBtn.setText(getPlayBtnText());
            GlobalScreen.removeNativeKeyListener(keyListener);
            inHotKeyDialog = false;
            dispose();
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ComponentUtil.setCorrectSize(this, 500, 380);
        // 读取缓存尺寸
//        String sizeStr = CacheManager.getWindowSize("HotkeyDialog");
//        if (sizeStr != null && sizeStr.matches("\\d+\\*\\d+")) {
//            String[] arr = sizeStr.split("\\*");
//            setSize(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
//        } else {
//            setSize(400, 300); // 你可以根据实际默认值调整
//        }
        setLocationRelativeTo(this);
        addWindowListener(new WindowClosingAdapter());
    }
}
