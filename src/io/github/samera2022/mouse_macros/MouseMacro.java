package io.github.samera2022.mouse_macros;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import io.github.samera2022.mouse_macros.constant.ColorConsts;
import io.github.samera2022.mouse_macros.constant.IconConsts;
import io.github.samera2022.mouse_macros.constant.OtherConsts;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MouseMacro extends JFrame implements NativeKeyListener, NativeMouseInputListener {
    private final JTextArea logArea;
    private final JButton startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn;
    //hotkey等按钮在showSettingsDialog()内
    private JDialog settingDialog;
    private volatile boolean recording = false;
    private List<MouseAction> actions = new ArrayList<>();
    private long lastTime = 0;

    // 热键自定义
    private int keyRecord = NativeKeyEvent.VC_F2;
    private int keyStop = NativeKeyEvent.VC_F3;
    private int keyPlay = NativeKeyEvent.VC_F4;
    // 配置对象
    private final ConfigManager.Config config;

    public MouseMacro() {
        // 1. 读取配置
        config = ConfigManager.loadConfig();
        // 1.1 若开启跟随系统设置，自动同步语言和深色模式
        if (config.followSystemSettings) {
            String[] availableLangs = ConfigManager.getAvailableLangs();
            config.lang = getSystemLang(availableLangs);
            config.enableDarkMode = isSystemDarkMode();
        }
        // 2. 用配置初始化本地化、热键、主题等
        Localizer.load(config.lang); // 动态加载语言
        boolean enableLangSwitch = true;
        Localizer.setRuntimeSwitch(enableLangSwitch);
        // 3. 初始化热键（如keyMap有值则覆盖设定的默认值）
        if (config.keyMap != null) {
            if (config.keyMap.containsKey("record")) {
                try { keyRecord = Integer.parseInt(config.keyMap.get("record")); } catch (Exception ignored) {} }
            if (config.keyMap.containsKey("stop")) {
                try { keyStop = Integer.parseInt(config.keyMap.get("stop")); } catch (Exception ignored) {} }
            if (config.keyMap.containsKey("play")) {
                try { keyPlay = Integer.parseInt(config.keyMap.get("play")); } catch (Exception ignored) {} }
        }
        setTitle(Localizer.get("title"));
        setSize((int) (1200/getScale()[0]), (int) (660/getScale()[1]));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);

        // 按钮分组布局，增加上下边距
        // 声明panel为成员变量
        JPanel panel = new JPanel(); // 使用成员变量
        panel.setLayout(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 上下各5像素边距
        // 第一行：开始录制、停止录制、执行宏
        // 声明为成员变量
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        // 声明为成员变量
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        startBtn = new JButton(getStartBtnText());
        stopBtn = new JButton(getStopBtnText());
        playBtn = new JButton(getPlayBtnText());
        row1.add(startBtn);
        row1.add(stopBtn);
        row1.add(playBtn);
        // 第二行：保存宏、加载宏、设置
        saveBtn = new JButton(Localizer.get("save_macro"));
        loadBtn = new JButton(Localizer.get("load_macro"));
        settingsBtn = new JButton(Localizer.get("settings"));
        row2.add(saveBtn);
        row2.add(loadBtn);
        row2.add(settingsBtn);

        panel.add(row1);
        panel.add(row2);
        add(panel, BorderLayout.SOUTH);

        // 自动调整窗体宽度
        adjustFrameWidth(startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn);

        startBtn.addActionListener(e -> startRecording());
        stopBtn.addActionListener(e -> stopRecording());
        playBtn.addActionListener(e -> playMacro());
        saveBtn.addActionListener(e -> saveMacroToFile());
        loadBtn.addActionListener(e -> loadMacroFromFile());
        settingsBtn.addActionListener(e -> showSettingsDialog());

        // 禁用JNativeHook日志
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            log("全局钩子注册失败: " + e.getMessage());
        }
        GlobalScreen.addNativeKeyListener(this);
        GlobalScreen.addNativeMouseListener(this);
        GlobalScreen.addNativeMouseMotionListener(this);

        // 4. 启动时根据配置应用暗色模式
        if (config.enableDarkMode) {
            applyDarkMode(this.getContentPane());
        } else {
            applyLightMode(this.getContentPane());
        }
    }

    private void log(String msg) {
        SwingUtilities.invokeLater(() -> logArea.append(msg + "\n"));
    }

    // 录制相关方法
    private void startRecording() {
        if (!recording) {
            actions.clear();
            recording = true;
            lastTime = System.currentTimeMillis();
            log(Localizer.get("start_recording"));
        }
    }

    private void stopRecording() {
        if (recording) {
            recording = false;
            log(Localizer.get("stop_recording_msg1") + actions.size() + Localizer.get("stop_recording_msg2"));
        }
    }

    // 回放宏
    private void playMacro() {
        if (actions.isEmpty()) {
            log(Localizer.get("no_recorded_actions"));
            return;
        }
        log(Localizer.get("start_playback"));
        new Thread(() -> {
            try {
                for (MouseAction action : actions) {
                    Thread.sleep(action.delay);
                    action.perform();
                }
                log(Localizer.get("playback_complete"));
            } catch (Exception e) {
                log(Localizer.get("playback_error") + e.getMessage());
            }
        }).start();
    }

    // 工具方法：显示JNativeHook的keyText
    private String getNativeKeyDisplayText(int keyCode) {
        String keyText = NativeKeyEvent.getKeyText(keyCode);
        if (keyText == null || keyText.trim().isEmpty() || keyText.startsWith("Unknown")) {
            keyText = "0x" + Integer.toHexString(keyCode).toUpperCase();
        }
        return keyText;
    }

    // 工具方法：根据当前热键动态生成按钮文本
    private String getStartBtnText() {
        return Localizer.get("start_record") + " (" + getNativeKeyDisplayText(keyRecord) + ")";
    }
    private String getStopBtnText() {
        return Localizer.get("stop_record") + " (" + getNativeKeyDisplayText(keyStop) + ")";
    }
    private String getPlayBtnText() {
        return Localizer.get("execute_macro") + " (" + getNativeKeyDisplayText(keyPlay) + ")";
    }

    // 全局快捷键监听
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == keyRecord) {
            startRecording();
        } else if (e.getKeyCode() == keyStop) {
            stopRecording();
        } else if (e.getKeyCode() == keyPlay) {
            playMacro();
        }
    }

    @Override public void nativeKeyReleased(NativeKeyEvent e) {}
    @Override public void nativeKeyTyped(NativeKeyEvent e) {}

    // 鼠标事件监听（录制功能实现）
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        if (recording) {
            Point p = normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            actions.add(new MouseAction(p.x, p.y, 1, e.getButton(), now - lastTime));
            lastTime = now;
//            log("记录: 按下 " + e.getButton() + " (" + p.x + "," + p.y + ")");
            log(Localizer.get("recording_msg1")+" (" + p.x + "," + p.y + ")");
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        if (recording) {
            Point p = normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            actions.add(new MouseAction(p.x, p.y, 2, e.getButton(), now - lastTime));
            lastTime = now;
//            log("记录: 松开 " + e.getButton() + " (" + p.x + "," + p.y + ")");
            log(Localizer.get("recording_msg2")+" (" + p.x + "," + p.y + ")");
        }
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        if (recording) {
            Point p = normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            actions.add(new MouseAction(p.x, p.y, 0, 0, now - lastTime));
            lastTime = now;
        }
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        nativeMouseMoved(e);
    }

    // 主入口
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MouseMacro().setVisible(true));
    }

    // 鼠标操作数据结构（完善构造函数）
    static class MouseAction {
        int x, y, type, button;
        long delay;
        MouseAction(int x, int y, int type, int button, long delay) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.button = button;
            this.delay = delay;
        }
        void perform() {
            try {
                if (robotInstance == null) {
                    robotInstance = new Robot();
                }
                Robot robot = robotInstance;
                // 直接用虚拟原点全局坐标
                Point global = denormalizeFromVirtualOrigin(x, y);
                robot.mouseMove(global.x, global.y);
                switch (type) {
                    case 1: // press
                        robot.mousePress(getAWTButtonMask(button));
                        break;
                    case 2: // release
                        robot.mouseRelease(getAWTButtonMask(button));
                        break;
                }
            } catch (Exception e) {
                // 忽略异常
            }
        }
        // 共享Robot实例，避免频繁创建
        private static Robot robotInstance = null;
        private int getAWTButtonMask(int btn) {
            switch (btn) {
                case 1: return java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
                case 2: return java.awt.event.InputEvent.BUTTON2_DOWN_MASK;
                case 3: return java.awt.event.InputEvent.BUTTON3_DOWN_MASK;

                default: return java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
            }
        }
    }

    // 获取所有屏幕的最左上角坐标（多屏归一化原点）
    private static Point getVirtualOrigin() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = ge.getScreenDevices();
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        for (GraphicsDevice device : devices) {
            Rectangle bounds = device.getDefaultConfiguration().getBounds();
            if (bounds.x < minX) minX = bounds.x;
            if (bounds.y < minY) minY = bounds.y;
        }
        return new Point(minX, minY);
    }

    // 将全局屏幕坐标归一化到虚拟原点
    private static Point normalizeToVirtualOrigin(int x, int y) {
        Point origin = getVirtualOrigin();
        return new Point(x - origin.x, y - origin.y);
    }

    // 将归一化坐标还原为主屏幕坐标（适配Robot，自动处理DPI缩放）
    private static Point denormalizeFromVirtualOrigin(int x, int y) {
        Point virtualOrigin = getVirtualOrigin();
        // 全局坐标
        int globalX = x + virtualOrigin.x;
        int globalY = y + virtualOrigin.y;
        // 获取主屏幕左上角和缩放因子
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        Rectangle primaryBounds = gc.getBounds();
        double scaleX = gc.getDefaultTransform().getScaleX();
        double scaleY = gc.getDefaultTransform().getScaleY();
        // 转为主屏幕坐标并除以缩放因子
        int robotX = (int) Math.round((globalX - primaryBounds.x) / scaleX);
        int robotY = (int) Math.round((globalY - primaryBounds.y) / scaleY);
        return new Point(robotX, robotY);
    }

    private static double[] getScale(){
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        double scaleX = gc.getDefaultTransform().getScaleX();
        double scaleY = gc.getDefaultTransform().getScaleY();
        return new double[]{scaleX, scaleY};
    }

    // 保存宏到文件
    private void saveMacroToFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.PrintWriter out = new java.io.PrintWriter(chooser.getSelectedFile(), StandardCharsets.UTF_8);
                for (MouseAction a : actions) {
                    out.println(a.x + "," + a.y + "," + a.type + "," + a.button + "," + a.delay);
                }
                out.close();
                log("宏已保存: " + chooser.getSelectedFile().getAbsolutePath());
            } catch (Exception ex) {
                log("保存失败: " + ex.getMessage());
            }
        }
    }

    // 加载宏从文件
    private void loadMacroFromFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.util.List<MouseAction> loaded = new ArrayList<>();
                java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(chooser.getSelectedFile()));
                String line;
                while ((line = in.readLine()) != null) {
                    String[] arr = line.split(",");
                    if (arr.length == 5) {
                        int x = Integer.parseInt(arr[0]);
                        int y = Integer.parseInt(arr[1]);
                        int type = Integer.parseInt(arr[2]);
                        int button = Integer.parseInt(arr[3]);
                        long delay = Long.parseLong(arr[4]);
                        loaded.add(new MouseAction(x, y, type, button, delay));
                    }
                }
                in.close();
                actions = loaded;
                log("宏已加载: " + chooser.getSelectedFile().getAbsolutePath() + " (" + actions.size() + " 步)");
            } catch (Exception ex) {
                log("加载失败: " + ex.getMessage());
            }
        }
    }

//    // 语言切换对话框
//    private void showLangDialog(JButton langBtn) {
//        String[] langs = {"zh_cn", "en_us"}; // 可扩展
//        String[] langNames = {"简体中文", "English"};
//        String current = io.github.samera2022.mouse_macros.Localizer.getCurrentLang();
//        int idx = Arrays.asList(langs).indexOf(current);
//        String sel = (String) JOptionPane.showInputDialog(this, io.github.samera2022.mouse_macros.Localizer.get("choose_lang"), io.github.samera2022.mouse_macros.Localizer.get("switch_lang"), JOptionPane.PLAIN_MESSAGE, null, langNames, langNames[idx>=0?idx:0]);
//        if (sel != null) {
//            int newIdx = Arrays.asList(langNames).indexOf(sel);
//            if (newIdx >= 0 && !langs[newIdx].equals(current)) {
//                io.github.samera2022.mouse_macros.Localizer.load(langs[newIdx]);
//                // 刷新界面文本
//                refreshTexts();
//            }
//        }
//    }
    // 在MouseMacro类中添加
    private void refreshMainFrameTexts() {
        setTitle(Localizer.get("title"));
        //本来想用循环的，但是因为热键显示不好操作，所以还得最后一个个手动加
        startBtn.setText(getStartBtnText());
        stopBtn.setText(getStopBtnText());
        playBtn.setText(getPlayBtnText());
        saveBtn.setText(Localizer.get("save_macro"));
        loadBtn.setText(Localizer.get("load_macro"));
        settingsBtn.setText(Localizer.get("settings"));
        // 如有其它需要本地化的组件，也可在此统一刷新
        adjustFrameWidth(startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn);
    }

    // 自动调整窗体宽度
    private void adjustFrameWidth(JButton... btns) {
        int padding = 80; // 额外边距
        // 两行分组
        int row1 = btns[0].getPreferredSize().width + btns[1].getPreferredSize().width + btns[2].getPreferredSize().width + 40;
        int row2 = btns[3].getPreferredSize().width + btns[4].getPreferredSize().width + 20;
        int maxWidth = Math.max(row1, row2) + padding;
        setSize(maxWidth, getHeight());
    }

    // 新增设置对话框方法
    private void showSettingsDialog() {
        settingDialog = new JDialog(this, Localizer.get("settings"), true);
        settingDialog.setLayout(new BorderLayout(10, 10));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel settingTitle = new JLabel(Localizer.get("settings"));
        settingTitle.setFont(settingTitle.getFont().deriveFont(Font.BOLD, 18f));
        settingTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(settingTitle);
        content.add(Box.createVerticalStrut(10));
        content.add(new JSeparator());

        // 跟随系统设置（文字在左，勾选框在右）
        JPanel followSysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel followSysLabel = new JLabel(Localizer.get("settings.follow_system_settings"));
        JCheckBox followSysBox = new JCheckBox(IconConsts.CHECK_BOX);
        followSysBox.setSelected(config.followSystemSettings);
//        followSysBox.setIcon(IconConsts.UNSELECTED_ICON);
//        followSysBox.setSelectedIcon(IconConsts.SELECTED_ICON);
        followSysPanel.add(followSysLabel);
        followSysPanel.add(Box.createHorizontalStrut(10));
        followSysPanel.add(followSysBox);
        followSysPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(followSysPanel);

        // 二级设置面板（缩进）
        JPanel subSettingsPanel = new JPanel();
        subSettingsPanel.setLayout(new BoxLayout(subSettingsPanel, BoxLayout.Y_AXIS));
        subSettingsPanel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0)); // 四个空格缩进
        subSettingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 语言选择
        JLabel langLabel = new JLabel(Localizer.get("settings.switch_lang"));
        String[] langs = ConfigManager.getAvailableLangs();
        JComboBox<String> langCombo = new JComboBox<>(langs);
        langCombo.setSelectedItem(config.lang);
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        langPanel.add(langLabel);
        langPanel.add(Box.createHorizontalStrut(10));
        langPanel.add(langCombo);
        langPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subSettingsPanel.add(langPanel);
        subSettingsPanel.add(Box.createVerticalStrut(10));

        // 暗色模式（文字在左，勾选框在右）
        JPanel darkModePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel darkModeLabel = new JLabel(Localizer.get("settings.enable_dark_mode"));
        JCheckBox darkModeBox = new JCheckBox(IconConsts.CHECK_BOX);
        darkModeBox.setSelected(config.enableDarkMode);
//        darkModeBox.setIcon(IconConsts.UNSELECTED_ICON);
//        darkModeBox.setSelectedIcon(IconConsts.SELECTED_ICON);
        darkModePanel.add(darkModeLabel);
        darkModePanel.add(Box.createHorizontalStrut(10));
        darkModePanel.add(darkModeBox);
        darkModePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subSettingsPanel.add(darkModePanel);
        subSettingsPanel.add(Box.createVerticalStrut(10));

        content.add(subSettingsPanel);

        // 默认存储路径
        JLabel pathLabel = new JLabel(Localizer.get("settings.default_mmc_storage_path"));
        JTextField pathField = new JTextField(config.defaultMmcStoragePath, 20);
        JButton browseBtn = new JButton(Localizer.get("settings.browse"));
        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (!pathField.getText().isEmpty())
                chooser.setCurrentDirectory(new java.io.File(pathField.getText()));
            int ret = chooser.showOpenDialog(settingDialog);
            if (ret == JFileChooser.APPROVE_OPTION) {
                pathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        JPanel pathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pathPanel.add(pathLabel);
        pathPanel.add(Box.createHorizontalStrut(10));
        pathPanel.add(pathField);
        pathPanel.add(Box.createHorizontalStrut(10));
        pathPanel.add(browseBtn);
        pathPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(pathPanel);

        // 热键自定义 + 关于作者 + 更新日志 三列按钮
        JButton hotkeyBtn = new JButton(Localizer.get("custom_hotkey"));
        hotkeyBtn.addActionListener(e -> showHotkeyDialog());
        JButton aboutBtn = new JButton(Localizer.get("about_author"));
        aboutBtn.addActionListener(e -> showAboutDialog());//
        JButton updateInfoBtn = new JButton(Localizer.get("update_info"));
        updateInfoBtn.addActionListener(e -> showUpdateInfoDialog());
        JPanel hotkeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hotkeyPanel.add(hotkeyBtn);
        hotkeyPanel.add(Box.createHorizontalStrut(10));
        hotkeyPanel.add(aboutBtn);
        hotkeyPanel.add(Box.createHorizontalStrut(10));
        hotkeyPanel.add(updateInfoBtn);
        hotkeyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(hotkeyPanel);

        // 保存按钮单独底部居中
        JButton saveSettingsBtn = new JButton(Localizer.get("settings.save_settings"));
        saveSettingsBtn.addActionListener(e -> {
            config.followSystemSettings = followSysBox.isSelected();
            config.lang = (String) langCombo.getSelectedItem();
            config.defaultMmcStoragePath = pathField.getText();
            config.enableDarkMode = darkModeBox.isSelected();
            // 热键配置保存到config.keyMap（假设已有相关逻辑）
            ConfigManager.saveConfig(config);
            Localizer.load(config.lang);
            refreshMainFrameTexts();
            // 此处是保存时使用暗色
            // Question: RootPane比ContentPane范围更广，那么此处用getRootPane是否更好？
            // 但是似乎用到ContentPane就已经把能看到的组件都设置好了
            if (config.enableDarkMode) {
                applyDarkMode(settingDialog.getContentPane());
                applyDarkMode(this.getContentPane());
            } else {
                applyLightMode(settingDialog.getContentPane());
                applyLightMode(this.getContentPane());
            }
            settingDialog.dispose();
        });
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        savePanel.add(saveSettingsBtn);
        savePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // 联动逻辑：根据followSysBox状态控制langCombo和darkModeBox可编辑性，并同步系统设置
        java.awt.event.ItemListener followSysListener = e -> {
            boolean enabled = !followSysBox.isSelected();
            langCombo.setEnabled(enabled);
            darkModeBox.setEnabled(enabled);
            if (!enabled) {
                // 跟随系统，自动设置语言和暗色模式
                String sysLang = getSystemLang(ConfigManager.getAvailableLangs());
                boolean sysDark = isSystemDarkMode();
                langCombo.setSelectedItem(sysLang);
                darkModeBox.setSelected(sysDark);
            }
        };
        followSysBox.addItemListener(followSysListener);
        // 初始化时同步一次
        followSysListener.itemStateChanged(null);

        settingDialog.add(content, BorderLayout.CENTER);
        settingDialog.add(savePanel, BorderLayout.SOUTH);
        // 此处是初始化时设置暗色
        if (config.enableDarkMode) {
            applyDarkMode(settingDialog.getContentPane());
            applyDarkMode(this.getContentPane());
        } else {
            applyLightMode(settingDialog.getContentPane());
            applyLightMode(this.getContentPane());
        }
        settingDialog.pack();
        settingDialog.setLocationRelativeTo(this);
        settingDialog.setVisible(true);
    }

    // 热键设置对话框（使用JNativeHook的keyCode）
    private void showHotkeyDialog() {
        JDialog dialog = new JDialog(this, Localizer.get("custom_hotkey"), true);
        dialog.setLayout(new GridLayout(4, 2, 5, 5));
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
        JButton confirm = new JButton(Localizer.get("confirm"));
        dialog.add(l1); dialog.add(t1);
        dialog.add(l2); dialog.add(t2);
        dialog.add(l3); dialog.add(t3);
        dialog.add(new JLabel()); dialog.add(new JLabel()); // 占位，保持布局整齐

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
        dialog.setContentPane(mainPanel);
        if (config.enableDarkMode) {
            applyDarkMode(dialog.getContentPane());
        } else {
            applyLightMode(dialog.getContentPane());
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
            GlobalScreen.removeNativeKeyListener(keyListener);
            // 更新主界面 含按键名称的按钮 文本
            startBtn.setText(getStartBtnText());
            stopBtn.setText(getStopBtnText());
            playBtn.setText(getPlayBtnText());
            dialog.dispose();
        });
        dialog.setSize(300, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog(this, Localizer.get("about_author"), true);
        aboutDialog.setLayout(new BorderLayout(10, 10));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel aboutTitle = new JLabel(Localizer.get("about_author"));
        aboutTitle.setFont(aboutTitle.getFont().deriveFont(Font.BOLD, 18f));
        aboutTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(aboutTitle);
        content.add(Box.createVerticalStrut(10));
        content.add(new JSeparator());
        // 新增无边框JTextArea
        JTextArea aboutArea = new JTextArea(OtherConsts.ABOUT_AUTHOR);
        aboutArea.setEditable(false);
        aboutArea.setLineWrap(true);
        aboutArea.setWrapStyleWord(true);
        aboutArea.setOpaque(false);
        aboutArea.setBorder(null);
        aboutArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(aboutArea);

        aboutDialog.add(content, BorderLayout.CENTER);
        // 保持与主设置窗体一致的暗色/亮色模式
        if (config.enableDarkMode) {
            applyDarkMode(aboutDialog.getContentPane());
        } else {
            applyLightMode(aboutDialog.getContentPane());
        }
        aboutDialog.setSize(settingDialog.getSize());
//        aboutDialog.pack();
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.setVisible(true);
    }
    // 更新日志弹窗
    private void showUpdateInfoDialog() {
        JDialog updateInfoDialog = new JDialog(this, Localizer.get("update_info"), true);
        updateInfoDialog.setLayout(new BorderLayout(10, 10));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel updateInfoTitle = new JLabel(Localizer.get("update_info"));
        updateInfoTitle.setFont(updateInfoTitle.getFont().deriveFont(Font.BOLD, 18f));
        updateInfoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(updateInfoTitle);
        content.add(Box.createVerticalStrut(10));
        content.add(new JSeparator());

        // 正确类型的JComboBox，显示版本号或标题
        JComboBox<String> infoCombo = new JComboBox<>(UpdateInfo.getAllVersions()); // 只显示标题
        infoCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(infoCombo);

        // JTextArea显示内容，初始为第一个内容
        String firstContent = UpdateInfo.values().length > 0 ? UpdateInfo.values()[0].getFormattedLog() : "";
        JTextArea updateInfoArea = new JTextArea(firstContent);
        updateInfoArea.setEditable(false);
        updateInfoArea.setLineWrap(true);
        updateInfoArea.setWrapStyleWord(true);
        updateInfoArea.setOpaque(false);
        updateInfoArea.setBorder(null);
        updateInfoArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(updateInfoArea);

        // ComboBox切换事件
        infoCombo.addActionListener(e -> {
            int idx = infoCombo.getSelectedIndex();
            if (idx >= 0 && idx < UpdateInfo.values().length) {
                updateInfoArea.setText(UpdateInfo.values()[idx].getFormattedLog());
            }
        });

        updateInfoDialog.add(content, BorderLayout.CENTER);
        if (config.enableDarkMode) {
            applyDarkMode(updateInfoDialog.getContentPane());
        } else {
            applyLightMode(updateInfoDialog.getContentPane());
        }
        updateInfoDialog.setSize(settingDialog.getSize());
//        aboutDialog.pack();
        updateInfoDialog.setLocationRelativeTo(this);
        updateInfoDialog.setVisible(true);
    }
    // 获取设置对话框的大小
    private Dimension getSettingsDialogSize() {
        // 参考showSettingsDialog的pack后大小
        JDialog temp = new JDialog();
        temp.setLayout(new BorderLayout());
        JPanel dummy = new JPanel();
        dummy.setPreferredSize(new Dimension(500, 300));
        temp.add(dummy, BorderLayout.CENTER);
        temp.pack();
        Dimension size = temp.getSize();
        temp.dispose();
        return size;
    }

    // 集中设置主界面和任意面板的暗色风格
    private void applyDarkMode(Component root) {
        setComponent(root, ColorConsts.DARK_MODE_SCHEME);
        if (root instanceof JDialog || root instanceof JFrame) root.setBackground(ColorConsts.DARK_MODE_BACKGROUND);
        UIManager.put("ComboBox.disabledBackground", ColorConsts.DARK_MODE_DISABLED_BACKGROUND);
        UIManager.put("ComboBox.disabledForeground", ColorConsts.DARK_MODE_DISABLED_FOREGROUND);
//        UIManager.put("CheckBox.select", ColorConsts.DARK_MODE_PANEL_BACKGROUND);
//        UIManager.put("CheckBox.focus", ColorConsts.DARK_MODE_PANEL_FOREGROUND);
//        UIManager.put("CheckBox.foreground", checkbox.getForeground());
        SwingUtilities.updateComponentTreeUI(this);
    }

    // 集中设置主界面和任意面板的亮色风格
    private void applyLightMode(Component root) {
        setComponent(root, ColorConsts.LIGHT_MODE_SCHEME);
        if (root instanceof JDialog || root instanceof JFrame) root.setBackground(ColorConsts.LIGHT_MODE_BACKGROUND);
        UIManager.put("ComboBox.disabledBackground", ColorConsts.LIGHT_MODE_DISABLED_BACKGROUND);
        UIManager.put("ComboBox.disabledForeground", ColorConsts.LIGHT_MODE_DISABLED_FOREGROUND);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void setComponent(Component comp, Color[] colorScheme) {
        if (colorScheme.length == 7)
            setComponent(comp, colorScheme[0], colorScheme[1], colorScheme[2], colorScheme[3], colorScheme[4], colorScheme[5], colorScheme[6]);
        else {
            System.out.println("Wrong Color Scheme! Apply Default Color Scheme.");
            setComponent(comp, ColorConsts.DARK_MODE_SCHEME);
        }
    }

    // 递归设置风格
    private void setComponent(Component comp, Color bg, Color fg, Color pbg, Color pfg, Color bbg, Color bfg, Color caret) {
        if (comp != null)
            if (comp instanceof JScrollPane) {
            comp.setBackground(pbg);
            for (Component c : ((JScrollPane) comp).getViewport().getComponents()) {
                if (c != null) setComponent(c, bg, fg, pbg, pfg, bbg, bfg, caret);
            }
        } else if (comp instanceof JPanel) {
            comp.setBackground(pbg);
            for (Component c : ((JPanel) comp).getComponents()) {
                if (c!=null) setComponent(c, bg, fg, pbg, pfg, bbg, bfg, caret);
            }
        } else if (comp instanceof JLabel) {
//            comp.setBackground(bg);
            comp.setForeground(fg);
        } else if (comp instanceof JButton) {
            ((JButton) comp).setFocusPainted(false);
            comp.setBackground(bbg);
            comp.setForeground(bfg);
        } else if (comp instanceof JTextField) {
            comp.setBackground(pbg);
            comp.setForeground(pfg);
            ((JTextField) comp).setCaretColor(caret);
        } else if (comp instanceof JTextArea) {
            comp.setBackground(pbg);
            comp.setForeground(pfg);
            ((JTextArea) comp).setCaretColor(caret);
        } else if (comp instanceof JCheckBox) {
            comp.setBackground(pbg);
            comp.setForeground(pfg);
        } else if (comp instanceof JComboBox||comp instanceof JScrollBar) {
            comp.setBackground(pbg);
            comp.setForeground(pfg);
        } else if (comp instanceof JSeparator) {
            comp.setBackground(pbg);
        } else {
            System.out.println("INCOMPATIBLE COMPONENT: "+comp.getName());
            comp.setBackground(bg);
            comp.setForeground(fg);
        }
    }

    // 获取系统语言（如lang文件夹无该语言则返回en_us）
    private String getSystemLang(String[] availableLangs) {
        String sysLang = System.getProperty("user.language", "en").toLowerCase();
        String sysCountry = System.getProperty("user.country", "US").toLowerCase();
        String full = sysLang + "_" + sysCountry;
        for (String l : availableLangs) {
            if (l.equalsIgnoreCase(full)) return l;
        }
        for (String l : availableLangs) {
            if (l.startsWith(sysLang + "_")) return l;
        }
        for (String l : availableLangs) {
            if (l.equalsIgnoreCase("en_us")) return l;
        }
        return availableLangs.length > 0 ? availableLangs[0] : "en_us";
    }

    // 判断系统是否为深色模式（仅支持Windows 10+，其他平台默认false）
    private boolean isSystemDarkMode() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            try {
                Process p = Runtime.getRuntime().exec(
                    "reg query HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize /v AppsUseLightTheme");
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("AppsUseLightTheme")) {
                        String[] arr = line.trim().split(" ");
                        String val = arr[arr.length - 1];
                        return "0x0".equals(val); // 0=dark, 1=light
                    }
                }
            } catch (Exception ignored) {}
        }
        // 其他平台可扩展
        return false;
    }

}
