package io.github.samera2022.mouse_macros.ui.frame;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.adapter.WindowClosingAdapter;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.listener.GlobalMouseListener;
import io.github.samera2022.mouse_macros.manager.CacheManager;
import io.github.samera2022.mouse_macros.manager.MacroManager;
import io.github.samera2022.mouse_macros.manager.ConfigManager;
import io.github.samera2022.mouse_macros.ui.component.CustomScrollBarUI;
import io.github.samera2022.mouse_macros.ui.frame.settings.HotkeyDialog;
import io.github.samera2022.mouse_macros.util.ComponentUtil;
import io.github.samera2022.mouse_macros.util.OtherUtil;
import io.github.samera2022.mouse_macros.util.SystemUtil;
import static io.github.samera2022.mouse_macros.manager.LogManager.log;
import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFrame extends JFrame{
    public static JTextArea logArea;
    public final JButton startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn, abortBtn, macroSettingsBtn;

    // 热键自定义
    public static int keyRecord = NativeKeyEvent.VC_F2;
    public static int keyStop = NativeKeyEvent.VC_F3;
    public static int keyPlay = NativeKeyEvent.VC_F4;
    public static int keyAbort = NativeKeyEvent.VC_F5;

    public static final GlobalMouseListener GML = new GlobalMouseListener();

    public static final MainFrame MAIN_FRAME = new MainFrame();

    private TrayIcon trayIcon;
    private SystemTray tray;

    public MainFrame() {
        // 1.1 若开启跟随系统设置，自动同步语言和深色模式
        if (config.followSystemSettings) {
            String[] availableLangs = ConfigManager.getAvailableLangs();
            config.lang = SystemUtil.getSystemLang(availableLangs);
            config.enableDarkMode = SystemUtil.isSystemDarkMode();
        }
        setTitle(Localizer.get("main_frame"));
        setName("main_frame");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/MouseMacros.png"))).getImage());
        // 2. 用配置初始化本地化、热键、主题等
        Localizer.load(config.lang); // 动态加载语言
        boolean enableLangSwitch = true;
        Localizer.setRuntimeSwitch(enableLangSwitch);
        // 3. 初始化热键（如keyMap有值则覆盖设定的默认值）
        if (config.keyMap != null) {
            if (config.keyMap.containsKey("start_record")) {
                try { keyRecord = Integer.parseInt(config.keyMap.get("start_record")); } catch (Exception ignored) {} }
            if (config.keyMap.containsKey("stop_record")) {
                try { keyStop = Integer.parseInt(config.keyMap.get("stop_record")); } catch (Exception ignored) {} }
            if (config.keyMap.containsKey("play_macro")) {
                try { keyPlay = Integer.parseInt(config.keyMap.get("play_macro")); } catch (Exception ignored) {} }
            if (config.keyMap.containsKey("abort_macro_operation")) {
                try { keyAbort = Integer.parseInt(config.keyMap.get("abort_macro_operation")); } catch (Exception ignored) {} }
        }
//        ComponentUtil.setCorrectSize(this,1200,660);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        // 应用自定义滚动条UI  此处本来可以用boolean代替的，但是想了想后面可能会拓展其他的主题样式
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(config.enableDarkMode? OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE));
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI(config.enableDarkMode? OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE));
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

        // 按钮分组布局，增加上下边距
        // 声明panel为成员变量
        JPanel panel = new JPanel(); // 使用成员变量
        panel.setLayout(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 上下各5像素边距
        // 第一行：开始录制、停止录制、执行宏
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        startBtn = new JButton(getStartBtnText());
        stopBtn = new JButton(getStopBtnText());
        playBtn = new JButton(getPlayBtnText());
        row1.add(startBtn);
        row1.add(stopBtn);
        row1.add(playBtn);
        // 第二行：保存宏、加载宏、设置
        abortBtn = new JButton(getAbortBtnText());
        saveBtn = new JButton(Localizer.get("main_frame.save_macro"));
        loadBtn = new JButton(Localizer.get("main_frame.load_macro"));
        settingsBtn = new JButton(Localizer.get("settings"));
        macroSettingsBtn = new JButton(Localizer.get("macro_settings"));
        row2.add(abortBtn); // 添加到最前面
        row2.add(saveBtn);
        row2.add(loadBtn);
        row2.add(settingsBtn);
        row2.add(macroSettingsBtn); // 添加到最后面

        panel.add(row1);
        panel.add(row2);

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.add(separator, BorderLayout.NORTH); // 分隔线在顶部
        southContainer.add(panel, BorderLayout.CENTER);    // 按钮面板在下方

        add(southContainer, BorderLayout.SOUTH);

        // 自动调整窗体宽度
        ComponentUtil.adjustFrameWidth(this, startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn);

        //限制1/2
        startBtn.addActionListener(e -> {if ((!MacroManager.isRecording())&&(!HotkeyDialog.inHotKeyDialog)) MacroManager.startRecording();});
        stopBtn.addActionListener(e -> {if ((MacroManager.isRecording())&&(!HotkeyDialog.inHotKeyDialog)) MacroManager.stopRecording(); else log(Localizer.get("log.macro_not_recording"));});
        playBtn.addActionListener(e -> {if ((!MacroManager.isRecording())&&(!HotkeyDialog.inHotKeyDialog)) MacroManager.play();});
        abortBtn.addActionListener(e -> {if ((MacroManager.isPlaying())&&(!HotkeyDialog.inHotKeyDialog)) MacroManager.abort(); else log(Localizer.get("log.macro_not_running"));});
        saveBtn.addActionListener(e -> {if (!MacroManager.isRecording()) MacroManager.saveToFile(this);});
        loadBtn.addActionListener(e -> {if (!MacroManager.isRecording()) MacroManager.loadFromFile(this);});
        settingsBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new SettingsDialog().setVisible(true)));
        macroSettingsBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new MacroSettingsDialog().setVisible(true)));

        // 禁用JNativeHook日志
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
        // 4. 启动时根据配置应用暗色模式
        ComponentUtil.setMode(getContentPane(),config.enableDarkMode?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
        // 统一应用窗体大小缓存（优先cache.json，无则默认）
        ComponentUtil.applyWindowSizeCache(this, "main_frame", 430, 330);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowClosingAdapter());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                switch (CacheManager.getDefaultCloseOperation()) {
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

    // 在MouseMacro类中添加
    public void refreshMainFrameTexts() {
        setTitle(Localizer.get("main_frame"));
        //本来想用循环的，但是因为热键显示不好操作，所以还得最后一个个手动加
        refreshSpecialTexts();
        saveBtn.setText(Localizer.get("main_frame.save_macro"));
        loadBtn.setText(Localizer.get("main_frame.load_macro"));
        settingsBtn.setText(Localizer.get("settings"));
        macroSettingsBtn.setText(Localizer.get("macro_settings"));
        // 托盘相关刷新
        if (trayIcon != null) {
            trayIcon.setToolTip(Localizer.get("main_frame"));
            if (trayIcon.getPopupMenu() != null && trayIcon.getPopupMenu().getItemCount() >= 2) {
                // 第一个是showItem，第二个是exitItem
                trayIcon.getPopupMenu().getItem(0).setLabel(Localizer.get("tray.show_main_menu"));
                trayIcon.getPopupMenu().getItem(2).setLabel(Localizer.get("tray.exit"));
            }
        }
        // 如有其它需要本地化的组件，也可在此统一刷新
        ComponentUtil.adjustFrameWidth(this, startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn, abortBtn, macroSettingsBtn);
    }

    public void refreshSpecialTexts(){
        startBtn.setText(getStartBtnText());
        stopBtn.setText(getStopBtnText());
        playBtn.setText(getPlayBtnText());
        abortBtn.setText(getAbortBtnText());
    }
    // 工具方法：根据当前热键动态生成按钮文本
    private String getStartBtnText() {return Localizer.get("main_frame.start_record") + " (" + OtherUtil.getNativeKeyDisplayText(keyRecord) + ")";}
    private String getStopBtnText() {return Localizer.get("main_frame.stop_record") + " (" + OtherUtil.getNativeKeyDisplayText(keyStop) + ")";}
    private String getPlayBtnText() {return Localizer.get("main_frame.play_macro") + " (" + OtherUtil.getNativeKeyDisplayText(keyPlay) + ")";}
    private String getAbortBtnText() {return Localizer.get("main_frame.abort_macro_operation") + " (" + OtherUtil.getNativeKeyDisplayText(keyAbort) + ")";}

    public static void adjustFrameWidth(){
        MAIN_FRAME.pack();
        MAIN_FRAME.setSize(MAIN_FRAME.getWidth(),(int) (660/SystemUtil.getScale()[1]));
    }

    private void initTrayIcon() {
        if (!SystemTray.isSupported()) return;
        if (tray == null) {
            tray = SystemTray.getSystemTray();
        }
        if (trayIcon == null) {
            Image trayImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/MouseMacros.png"))).getImage();
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
