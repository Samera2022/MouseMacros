package io.github.samera2022.mousemacros.ui.frame.settings;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import io.github.samera2022.mousemacros.Localizer;
import io.github.samera2022.mousemacros.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.constant.OtherConsts;
import io.github.samera2022.mousemacros.config.ConfigManager;
import io.github.samera2022.mousemacros.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Objects;

import static io.github.samera2022.mousemacros.ui.frame.MainFrame.*;
import static io.github.samera2022.mousemacros.util.OtherUtil.getNativeKeyDisplayText;

public class HotkeyDialog extends JDialog {
    public static boolean inHotKeyDialog = false;

    public HotkeyDialog() {
        inHotKeyDialog = true;
        setTitle(Localizer.get("settings.custom_hotkey"));
        setName("settings.custom_hotkey");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH+"icons/MouseMacros.png"))).getImage());
        setModal(true);

        // 界面组件初始化
        JLabel l1 = new JLabel(Localizer.get("main_frame.start_record") + ":");
        JLabel l2 = new JLabel(Localizer.get("main_frame.stop_record") + ":");
        JLabel l3 = new JLabel(Localizer.get("main_frame.play_macro") + ":");
        JLabel l4 = new JLabel(Localizer.get("main_frame.abort_macro_operation") + ":");

        JTextField t1 = new JTextField();
        JTextField t2 = new JTextField();
        JTextField t3 = new JTextField();
        JTextField t4 = new JTextField();

        t1.setText(getNativeKeyDisplayText(keyRecord));
        t2.setText(getNativeKeyDisplayText(keyStop));
        t3.setText(getNativeKeyDisplayText(keyPlay));
        t4.setText(getNativeKeyDisplayText(keyAbort));

        // 禁止直接输入文本，仅通过键盘Hook捕获
        t1.setEditable(false); t2.setEditable(false); t3.setEditable(false); t4.setEditable(false);
        // 设置光标样式为默认（因为不可编辑），增强视觉一致性
        t1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton confirm = new JButton(Localizer.get("settings.custom_hotkey.confirm"));

        // 构建布局
        JPanel gridPanel = new JPanel(new GridLayout(4, 2, 5, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        gridPanel.add(l1); gridPanel.add(t1);
        gridPanel.add(l2); gridPanel.add(t2);
        gridPanel.add(l3); gridPanel.add(t3);
        gridPanel.add(l4); gridPanel.add(t4);

        JPanel confirmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        confirmPanel.add(confirm);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(gridPanel);
        mainPanel.add(confirmPanel);

        // 点击空白处失去焦点
        mainPanel.setFocusable(true); // 让面板可以接收焦点
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainPanel.requestFocusInWindow(); // 点击主面板，从文本框夺走焦点
            }
        });
        setContentPane(mainPanel);

        // 临时变量存储按键码
        final int[] tempRecord = {keyRecord};
        final int[] tempStop = {keyStop};
        final int[] tempPlay = {keyPlay};
        final int[] tempAbort = {keyAbort};

        // 定义按键监听器
        NativeKeyListener keyListener = new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                // 只有当文本框确实拥有焦点时，才修改对应的值
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
        };

        // 统一处理文本框的焦点行为
        FocusAdapter textFocusAdapter = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 获得焦点时，变色提醒正在录制
                ((JTextField)e.getSource()).setBackground(UIManager.getColor("TextField.inactiveForeground"));
                GlobalScreen.addNativeKeyListener(keyListener);
            }

            @Override
            public void focusLost(FocusEvent e) {
                // 失去焦点时，恢复颜色，并注销全局监听
                ((JTextField)e.getSource()).setBackground(UIManager.getColor("TextField.background"));
                GlobalScreen.removeNativeKeyListener(keyListener);
            }
        };

        JTextField[] fields = {t1, t2, t3, t4};
        for (JTextField f : fields) {
            f.addFocusListener(textFocusAdapter);
            // 确保鼠标点击能精准触发焦点请求
            f.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    f.requestFocusInWindow();
                }
            });
        }

        confirm.addActionListener(e -> {
            keyRecord = tempRecord[0];
            keyStop = tempStop[0];
            keyPlay = tempPlay[0];
            keyAbort = tempAbort[0];

            Map<String, String> keyMap = ConfigManager.getKeyMap();
            keyMap.put("start_record", String.valueOf(keyRecord));
            keyMap.put("stop_record", String.valueOf(keyStop));
            keyMap.put("play_macro", String.valueOf(keyPlay));
            keyMap.put("abort_macro_operation", String.valueOf(keyAbort));
            ConfigManager.set("keyMap", keyMap);

            ConfigManager.saveConfig();

            GlobalScreen.removeNativeKeyListener(GML);
            GlobalScreen.addNativeKeyListener(GML);

            GlobalScreen.removeNativeKeyListener(keyListener);
            inHotKeyDialog = false;
            dispose();
        });

        // 窗体收尾工作
        ComponentUtil.setMode(getContentPane(), ConfigManager.getBoolean("enable_dark_mode") ? OtherConsts.DARK_MODE : OtherConsts.LIGHT_MODE);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中
        addWindowListener(new WindowClosingAdapter());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {inHotKeyDialog = false;}
            @Override
            public void windowClosing(WindowEvent e) {inHotKeyDialog = false;}
        });
    }
}