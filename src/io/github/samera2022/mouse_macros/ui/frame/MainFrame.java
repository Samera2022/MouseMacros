package io.github.samera2022.mouse_macros.ui.frame;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.listener.GlobalMouseListener;
import io.github.samera2022.mouse_macros.manager.MacroManager;
import io.github.samera2022.mouse_macros.manager.ConfigManager;
import io.github.samera2022.mouse_macros.util.ComponentUtil;
import io.github.samera2022.mouse_macros.util.SystemUtil;
import static io.github.samera2022.mouse_macros.manager.LogManager.log;
import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFrame extends JFrame implements NativeKeyListener, NativeMouseInputListener {
    public static JTextArea logArea;
    public final JButton startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn;

    // 热键自定义
    public static int keyRecord = NativeKeyEvent.VC_F2;
    public static int keyStop = NativeKeyEvent.VC_F3;
    public static int keyPlay = NativeKeyEvent.VC_F4;

    public static final GlobalMouseListener GML = new GlobalMouseListener();

    public static final MainFrame MAIN_FRAME = new MainFrame();
    // 主入口
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> MAIN_FRAME.setVisible(true));
    }

    public MainFrame() {
        // 1.1 若开启跟随系统设置，自动同步语言和深色模式
        if (config.followSystemSettings) {
            String[] availableLangs = ConfigManager.getAvailableLangs();
            config.lang = SystemUtil.getSystemLang(availableLangs);
            config.enableDarkMode = SystemUtil.isSystemDarkMode();
        }
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
        }
        setTitle(Localizer.get("title"));
        ComponentUtil.setCorrectSize(this,1200,660);
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
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
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
        ComponentUtil.adjustFrameWidth(this, startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn);
        
        startBtn.addActionListener(e -> MacroManager.startRecording());
        stopBtn.addActionListener(e -> MacroManager.stopRecording());
        playBtn.addActionListener(e -> MacroManager.play());
        saveBtn.addActionListener(e -> MacroManager.saveToFile(this));
        loadBtn.addActionListener(e -> MacroManager.loadFromFile(this));
        settingsBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new SettingsDialog().setVisible(true)));

        // 禁用JNativeHook日志
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            log(Localizer.get("hook_registration_failed") + e.getMessage());
        }
        GlobalScreen.addNativeKeyListener(GML);
        GlobalScreen.addNativeMouseListener(GML);
        GlobalScreen.addNativeMouseMotionListener(GML);

        // 4. 启动时根据配置应用暗色模式
        if (config.enableDarkMode) {
            ComponentUtil.applyDarkMode(this.getContentPane(),this);
        } else {
            ComponentUtil.applyLightMode(this.getContentPane(),this);
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // 工具方法：显示JNativeHook的keyText
    public static String getNativeKeyDisplayText(int keyCode) {
        String keyText = NativeKeyEvent.getKeyText(keyCode);
        if (keyText == null || keyText.trim().isEmpty() || keyText.startsWith("Unknown")) {
            keyText = "0x" + Integer.toHexString(keyCode).toUpperCase();
        }
        return keyText;
    }

    // 在MouseMacro类中添加
    public void refreshMainFrameTexts() {
        setTitle(Localizer.get("title"));
        //本来想用循环的，但是因为热键显示不好操作，所以还得最后一个个手动加
        refreshSpecialTexts();
        saveBtn.setText(Localizer.get("save_macro"));
        loadBtn.setText(Localizer.get("load_macro"));
        settingsBtn.setText(Localizer.get("settings"));
        // 如有其它需要本地化的组件，也可在此统一刷新
        ComponentUtil.adjustFrameWidth(this, startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn);
    }

    public void refreshSpecialTexts(){
        startBtn.setText(getStartBtnText());
        stopBtn.setText(getStopBtnText());
        playBtn.setText(getPlayBtnText());
    }

    // 工具方法：根据当前热键动态生成按钮文本
    private String getStartBtnText() {return Localizer.get("start_record") + " (" + getNativeKeyDisplayText(keyRecord) + ")";}
    private String getStopBtnText() {return Localizer.get("stop_record") + " (" + getNativeKeyDisplayText(keyStop) + ")";}
    private String getPlayBtnText() {return Localizer.get("play_macro") + " (" + getNativeKeyDisplayText(keyPlay) + ")";}

}
