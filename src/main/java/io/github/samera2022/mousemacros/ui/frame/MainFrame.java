package io.github.samera2022.mousemacros.ui.frame;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import io.github.samera2022.mousemacros.Localizer;
import io.github.samera2022.mousemacros.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.config.ConfigManager;
import io.github.samera2022.mousemacros.constant.OtherConsts;
import io.github.samera2022.mousemacros.listener.GlobalMouseListener;
import io.github.samera2022.mousemacros.manager.CacheManager;
import io.github.samera2022.mousemacros.manager.MacroManager;
import io.github.samera2022.mousemacros.ui.frame.settings.HotkeyDialog;
import io.github.samera2022.mousemacros.util.ComponentUtil;
import io.github.samera2022.mousemacros.util.OtherUtil;
import io.github.samera2022.mousemacros.util.SystemUtil;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.github.samera2022.mousemacros.manager.LogManager.log;

public class MainFrame extends JFrame {
    public static JTextArea logArea;
    private final JButton startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn, abortBtn;
    public static int keyRecord = NativeKeyEvent.VC_F2;
    public static int keyStop = NativeKeyEvent.VC_F3;
    public static int keyPlay = NativeKeyEvent.VC_F4;
    public static int keyAbort = NativeKeyEvent.VC_F5;

    public static final GlobalMouseListener GML = new GlobalMouseListener();

    public static MainFrame MAIN_FRAME = new MainFrame();

    private TrayIcon trayIcon;
    private SystemTray tray;

    public MainFrame() {
        if (ConfigManager.getBoolean("follow_system_settings")) {
            String[] availableLangs = ConfigManager.getAvailableLangs();
            ConfigManager.set("switch_lang", SystemUtil.getSystemLang(availableLangs));
            ConfigManager.set("enable_dark_mode", SystemUtil.isSystemDarkMode());
        }
        Localizer.load(ConfigManager.getString("switch_lang"));
        setTitle(Localizer.get("main_frame"));
        setName("main_frame");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage());
        boolean enableLangSwitch = true;
        Localizer.setRuntimeSwitch(enableLangSwitch);

        Map<String, String> keyMap = ConfigManager.getKeyMap();
        if (keyMap != null) {
            if (keyMap.containsKey("start_record")) {
                try { keyRecord = Integer.parseInt(keyMap.get("start_record")); } catch (Exception ignored) {}
            }
            if (keyMap.containsKey("stop_record")) {
                try { keyStop = Integer.parseInt(keyMap.get("stop_record")); } catch (Exception ignored) {}
            }
            if (keyMap.containsKey("play_macro")) {
                try { keyPlay = Integer.parseInt(keyMap.get("play_macro")); } catch (Exception ignored) {}
            }
            if (keyMap.containsKey("abort_macro_operation")) {
                try { keyAbort = Integer.parseInt(keyMap.get("abort_macro_operation")); } catch (Exception ignored) {}
            }
        }

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        startBtn = new JButton(getStartBtnText());
        stopBtn = new JButton(getStopBtnText());
        playBtn = new JButton(getPlayBtnText());
        row1.add(startBtn);
        row1.add(stopBtn);
        row1.add(playBtn);
        abortBtn = new JButton(getAbortBtnText());
        saveBtn = new JButton(Localizer.get("main_frame.save_macro"));
        loadBtn = new JButton(Localizer.get("main_frame.load_macro"));
        settingsBtn = new JButton(Localizer.get("settings"));
        row2.add(abortBtn);
        row2.add(saveBtn);
        row2.add(loadBtn);
        row2.add(settingsBtn);

        panel.add(row1);
        panel.add(row2);

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.add(separator, BorderLayout.NORTH);
        southContainer.add(panel, BorderLayout.CENTER);

        add(southContainer, BorderLayout.SOUTH);

        startBtn.addActionListener(e -> {
            if ((!MacroManager.isRecording()) && (!HotkeyDialog.inHotKeyDialog)) MacroManager.startRecording();
        });
        stopBtn.addActionListener(e -> {
            if ((MacroManager.isRecording()) && (!HotkeyDialog.inHotKeyDialog)) MacroManager.stopRecording();
            else log(Localizer.get("log.macro_not_recording"));
        });
        playBtn.addActionListener(e -> {
            if ((!MacroManager.isRecording()) && (!HotkeyDialog.inHotKeyDialog)) {
                if (MacroManager.isPaused()) {
                    MacroManager.resume();
                } else {
                    MacroManager.play();
                }
            }
        });
        abortBtn.addActionListener(e -> {
            if ((MacroManager.isPlaying()) && (!HotkeyDialog.inHotKeyDialog)) {
                if (MacroManager.isPaused()) {
                    MacroManager.abort();
                } else {
                    MacroManager.pause();
                }
            } else {
                log(Localizer.get("log.macro_not_running"));
            }
        });
        saveBtn.addActionListener(e -> {
            if (!MacroManager.isRecording()) MacroManager.saveToFile(this);
        });
        loadBtn.addActionListener(e -> {
            if (!MacroManager.isRecording()) MacroManager.loadFromFile(this);
        });
        settingsBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new SettingsDialog().setVisible(true)));

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            log(Localizer.get("log.hook_registration_failed") + e.getMessage());
        }
        GlobalScreen.addNativeKeyListener(GML);
        GlobalScreen.addNativeMouseListener(GML);
        GlobalScreen.addNativeMouseWheelListener(GML);
        GlobalScreen.addNativeMouseMotionListener(GML);
        readjust();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        ComponentUtil.setMode(getContentPane(), ConfigManager.getBoolean("enable_dark_mode") ?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
        addWindowListener(new WindowClosingAdapter());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                switch (CacheManager.cache.defaultCloseOperation) {
                    case CacheManager.UNKNOWN:
                        new ExitDialog(MainFrame.this).setVisible(true);
                        break;
                    case CacheManager.EXIT_ON_CLOSE:
                        System.exit(0);
                        break;
                    case CacheManager.MINIMIZE_TO_TRAY:
                        minimizeToTray();
                        break;
                }
            }
        });
    }

    public void refreshMainFrameTexts() {
        setTitle(Localizer.get("main_frame"));
        refreshSpecialTexts();
        saveBtn.setText(Localizer.get("main_frame.save_macro"));
        loadBtn.setText(Localizer.get("main_frame.load_macro"));
        settingsBtn.setText(Localizer.get("settings"));
        if (trayIcon != null) {
            trayIcon.setToolTip(Localizer.get("main_frame"));
            if (trayIcon.getPopupMenu() != null && trayIcon.getPopupMenu().getItemCount() >= 2) {
                trayIcon.getPopupMenu().getItem(0).setLabel(Localizer.get("tray.show_main_menu"));
                trayIcon.getPopupMenu().getItem(2).setLabel(Localizer.get("tray.exit"));
            }
        }
    }

    public void refreshSpecialTexts() {
        startBtn.setText(getStartBtnText());
        stopBtn.setText(getStopBtnText());
        playBtn.setText(getPlayBtnText());
        abortBtn.setText(getAbortBtnText());
    }

    private String getStartBtnText() {
        return Localizer.get("main_frame.start_record") + " (" + OtherUtil.getNativeKeyDisplayText(keyRecord) + ")";
    }

    private String getStopBtnText() {
        return Localizer.get("main_frame.stop_record") + " (" + OtherUtil.getNativeKeyDisplayText(keyStop) + ")";
    }

    private String getPlayBtnText() {
        if (MacroManager.isPaused()) {
            return Localizer.get("main_frame.resume_macro") + " (" + OtherUtil.getNativeKeyDisplayText(keyPlay) + ")";
        }
        return Localizer.get("main_frame.play_macro") + " (" + OtherUtil.getNativeKeyDisplayText(keyPlay) + ")";
    }

    private String getAbortBtnText() {
        if (MacroManager.isPaused()) {
            return Localizer.get("main_frame.abort_macro_operation") + " (" + OtherUtil.getNativeKeyDisplayText(keyAbort) + ")";
        }
        return Localizer.get("main_frame.pause_macro") + " (" + OtherUtil.getNativeKeyDisplayText(keyAbort) + ")";
    }

    private void initTrayIcon() {
        if (!SystemTray.isSupported()) return;
        if (tray == null) {
            tray = SystemTray.getSystemTray();
        }
        if (trayIcon == null) {
            Image trayImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage();
            PopupMenu popupMenu = new PopupMenu();
            MenuItem showItem = new MenuItem(Localizer.get("tray.show_main_menu"));
            MenuItem exitItem = new MenuItem(Localizer.get("tray.exit"));
            popupMenu.add(showItem);
            popupMenu.addSeparator();
            popupMenu.add(exitItem);
            trayIcon = new TrayIcon(trayImage, Localizer.get("main_frame"), popupMenu);
            trayIcon.setImageAutoSize(true);
            showItem.addActionListener(e -> restoreFromTray());
            exitItem.addActionListener(e -> {
                tray.remove(trayIcon);
                System.exit(0);
            });
            trayIcon.addActionListener(e -> restoreFromTray());
        }
    }

    public void readjust() {
        ComponentUtil.adjustFrameWithCache(this, 0, new JComponent[]{logArea}, new JComponent[]{startBtn, stopBtn, playBtn}, new JComponent[]{abortBtn, saveBtn, loadBtn, settingsBtn});
    }
    public void minimizeToTray() {
        initTrayIcon();
        if (tray != null && trayIcon != null) {
            try {
                tray.add(trayIcon);
                setVisible(false);
            } catch (AWTException ex) {
                log(Localizer.get("log.adding_tray_failed") + ":" + ex.getMessage());
                dispose();
            }
        } else {
            dispose();
        }
    }

    private void restoreFromTray() {
        setVisible(true);
        setExtendedState(JFrame.NORMAL);
        if (tray != null && trayIcon != null) {
            tray.remove(trayIcon);
        }
        toFront();
    }
}