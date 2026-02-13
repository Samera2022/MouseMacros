package io.github.samera2022.mousemacros.app.ui.frame.settings;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Objects;

import static io.github.samera2022.mousemacros.app.ui.frame.MainFrame.*;
import static io.github.samera2022.mousemacros.app.util.OtherUtil.getNativeKeyDisplayText;

public class HotkeyDialog extends JDialog {
    public static boolean inHotKeyDialog = false;

    public HotkeyDialog() {
        inHotKeyDialog = true;
        setTitle(Localizer.get("settings.custom_hotkey"));
        setName("settings.custom_hotkey");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage());
        setModal(true);

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

        t1.setEditable(false);
        t2.setEditable(false);
        t3.setEditable(false);
        t4.setEditable(false);
        t1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton confirm = new JButton(Localizer.get("settings.custom_hotkey.confirm"));

        JPanel gridPanel = new JPanel(new GridLayout(4, 2, 5, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        gridPanel.add(l1);
        gridPanel.add(t1);
        gridPanel.add(l2);
        gridPanel.add(t2);
        gridPanel.add(l3);
        gridPanel.add(t3);
        gridPanel.add(l4);
        gridPanel.add(t4);

        JPanel confirmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        confirmPanel.add(confirm);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(gridPanel);
        mainPanel.add(confirmPanel);

        mainPanel.setFocusable(true);
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainPanel.requestFocusInWindow();
            }
        });
        setContentPane(mainPanel);

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
        };

        FocusAdapter textFocusAdapter = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                ((JTextField) e.getSource()).setBackground(UIManager.getColor("TextField.inactiveForeground"));
                GlobalScreen.addNativeKeyListener(keyListener);
            }

            @Override
            public void focusLost(FocusEvent e) {
                ((JTextField) e.getSource()).setBackground(UIManager.getColor("TextField.background"));
                GlobalScreen.removeNativeKeyListener(keyListener);
            }
        };

        JTextField[] fields = {t1, t2, t3, t4};
        for (JTextField f : fields) {
            f.addFocusListener(textFocusAdapter);
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

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowClosingAdapter());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                inHotKeyDialog = false;
            }

            @Override
            public void windowClosing(WindowEvent e) {
                inHotKeyDialog = false;
            }
        });
    }
}
