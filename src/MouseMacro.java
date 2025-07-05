import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MouseMacro extends JFrame implements NativeKeyListener, NativeMouseInputListener {
    private JTextArea logArea;
    private JButton startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn;
    //hotkey等按钮在showSettingsDialog()内
    private JButton hotkeyBtn, langBtn;
    private volatile boolean recording = false;
    private List<MouseAction> actions = new ArrayList<>();
    private long lastTime = 0;

    // 热键自定义
    private int keyRecord = NativeKeyEvent.VC_F2;
    private int keyStop = NativeKeyEvent.VC_F3;
    private int keyPlay = NativeKeyEvent.VC_F4;
    public MouseMacro() {
        // 本地化初始化
        Localizer.load("zh_cn"); // 默认中文
        boolean enableLangSwitch = true; // 控制是否支持运行时切换
        Localizer.setRuntimeSwitch(enableLangSwitch);
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
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 上下各5像素边距
        // 第一行：开始录制、停止录制、执行宏
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        startBtn = new JButton(getStartBtnText());
        stopBtn = new JButton(getStopBtnText());
        playBtn = new JButton(getPlayBtnText());
        row1.add(startBtn);
        row1.add(stopBtn);
        row1.add(playBtn);
        // 第二行：保存宏、加载宏、设置
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
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
        adjustFrameWidth(startBtn, stopBtn, playBtn, saveBtn, loadBtn);

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
        dialog.add(new JLabel()); dialog.add(confirm);
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
            // 更新主界面按钮文本
            startBtn.setText(getStartBtnText());
            stopBtn.setText(getStopBtnText());
            playBtn.setText(getPlayBtnText());
            dialog.dispose();
        });
        dialog.setSize(300, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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
                java.io.PrintWriter out = new java.io.PrintWriter(chooser.getSelectedFile(), "UTF-8");
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

    // 语言切换对话框
    private void showLangDialog(JButton langBtn) {
        String[] langs = {"zh_cn", "en_us"}; // 可扩展
        String[] langNames = {"简体中文", "English"};
        String current = Localizer.getCurrentLang();
        int idx = Arrays.asList(langs).indexOf(current);
        String sel = (String) JOptionPane.showInputDialog(this, Localizer.get("choose_lang"), Localizer.get("switch_lang"), JOptionPane.PLAIN_MESSAGE, null, langNames, langNames[idx>=0?idx:0]);
        if (sel != null) {
            int newIdx = Arrays.asList(langNames).indexOf(sel);
            if (newIdx >= 0 && !langs[newIdx].equals(current)) {
                Localizer.load(langs[newIdx]);
                // 刷新界面文本
                setTitle(Localizer.get("title"));
                startBtn.setText(getStartBtnText());
                stopBtn.setText(getStopBtnText());
                playBtn.setText(getPlayBtnText());
                saveBtn.setText(Localizer.get("save_macro"));
                loadBtn.setText(Localizer.get("load_macro"));
                hotkeyBtn.setText(Localizer.get("custom_hotkey"));
                //langBtn受(Boolean) runningSwitch的影响
                if (langBtn != null) langBtn.setText(Localizer.get("switch_lang"));
                // 重新调整窗体宽度
                adjustFrameWidth(startBtn, stopBtn, playBtn, saveBtn, loadBtn);
            }
        }
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
        JDialog dialog = new JDialog(this, Localizer.get("settings"), true);
        dialog.setLayout(new GridLayout(2, 1, 10, 10));
        hotkeyBtn = new JButton(Localizer.get("custom_hotkey"));
        hotkeyBtn.addActionListener(e -> showHotkeyDialog());
        dialog.add(hotkeyBtn);
        if (Localizer.isRuntimeSwitch()) {
            langBtn = new JButton(Localizer.get("switch_lang"));
            langBtn.addActionListener(e2 -> showLangDialog(langBtn));
            dialog.add(langBtn);
        }
        dialog.setSize(300, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
