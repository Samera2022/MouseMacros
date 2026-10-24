package io.github.samera2022.mousemacros.app.ui.frame;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.config.CacheManager;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;
import io.github.samera2022.mousemacros.app.macro.GlobalMouseListener;
import io.github.samera2022.mousemacros.app.macro.MacroManager;
import io.github.samera2022.mousemacros.app.macro.MouseAction;
import io.github.samera2022.mousemacros.app.ui.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.app.ui.frame.settings.HotkeyDialog;
import io.github.samera2022.mousemacros.app.util.OtherUtil;
import io.github.samera2022.mousemacros.app.util.SystemUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.github.samera2022.mousemacros.app.manager.LogManager.log;

/**
 * Main recorder window. The center is a structured event timeline rather than
 * a raw text console, so recorded actions remain scannable as the macro grows.
 */
public class MainFrame extends JFrame {
    /** Compatibility alias for integrations that used the old logArea field. */
    public static JTable logArea;
    private static DefaultTableModel timelineModel;
    private static JLabel stateLabel, actionCountLabel, durationLabel, stateDot;
    private static int actionSequence;
    private static long recordingStartedAt;
    private static long lastRecordingDuration;

    private final JButton startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn, abortBtn;
    private final Timer stateTimer;

    public static int keyRecord = NativeKeyEvent.VC_F2;
    public static int keyStop = NativeKeyEvent.VC_F3;
    public static int keyPlay = NativeKeyEvent.VC_F4;
    public static int keyAbort = NativeKeyEvent.VC_F5;
    public static final GlobalMouseListener GML = new GlobalMouseListener();

    private TrayIcon trayIcon;
    private SystemTray tray;

    private static Color WINDOW = new Color(0x15181D);
    private static Color SURFACE = new Color(0x1D2128);
    private static Color SURFACE_RAISED = new Color(0x252A32);
    private static Color LINE = new Color(0x343A45);
    private static Color TEXT = new Color(0xE8ECF2);
    private static Color MUTED = new Color(0x8D96A5);
    private static final Color BLUE = new Color(0x5AA9FF);
    private static final Color GREEN = new Color(0x4ED6A1);
    private static final Color ORANGE = new Color(0xF3B562);
    private static final Color RED = new Color(0xF07178);
    private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("HH:mm:ss");

    /*
     * Keep the singleton after the visual constants above.  Java initializes
     * static fields in source order; constructing the frame earlier meant
     * actionButton() saw TEXT/accent colors as null and failed during startup.
     */
    public static MainFrame MAIN_FRAME = new MainFrame();

    public MainFrame() {
        if (ConfigManager.getBoolean("follow_system_settings")) {
            String[] availableLangs = ConfigManager.getAvailableLangs();
            ConfigManager.set("switch_lang", SystemUtil.getSystemLang(availableLangs));
            ConfigManager.set("enable_dark_mode", SystemUtil.isSystemDarkMode());
        }
        Localizer.load(ConfigManager.getString("switch_lang"));
        setTitle(Localizer.get("main_frame"));
        setName("main_frame");
        setIconImage(new ImageIcon(Objects.requireNonNull(
                getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage());
        Localizer.setRuntimeSwitch(true);
        loadHotkeys();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(760, 480));
        setLocationRelativeTo(null);
        getContentPane().setBackground(WINDOW);
        setLayout(new BorderLayout());

        timelineModel = new DefaultTableModel(timelineColumns(), 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            @Override public Class<?> getColumnClass(int column) {
                return column == 0 ? Integer.class : column == 5 ? Long.class : String.class;
            }
        };
        logArea = new JTable(timelineModel) {
            @Override public String getToolTipText(java.awt.event.MouseEvent event) {
                int row = rowAtPoint(event.getPoint());
                int column = columnAtPoint(event.getPoint());
                if (row < 0 || column < 0) return null;
                Object value = getValueAt(row, column);
                return value == null ? null : value.toString();
            }
        };
        styleTimeline();

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(WINDOW);
        root.setBorder(new EmptyBorder(18, 20, 14, 20));
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildTimeline(), BorderLayout.CENTER);

        startBtn = actionButton(getStartBtnText(), BLUE);
        stopBtn = actionButton(getStopBtnText(), RED);
        playBtn = actionButton(getPlayBtnText(), GREEN);
        abortBtn = actionButton(getAbortBtnText(), ORANGE);
        saveBtn = quietButton(Localizer.get("main_frame.save_macro"));
        loadBtn = quietButton(Localizer.get("main_frame.load_macro"));
        settingsBtn = quietButton(Localizer.get("settings"));
        root.add(buildToolbar(), BorderLayout.SOUTH);
        add(root, BorderLayout.CENTER);

        wireActions();
        refreshRuntimeState();

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

        stateTimer = new Timer(350, e -> refreshRuntimeState());
        stateTimer.start();
        readjust();
        setLocationRelativeTo(null);
        applyTheme();
        addWindowListener(new WindowClosingAdapter());
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
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

    private void loadHotkeys() {
        Map<String, String> keyMap = ConfigManager.getKeyMap();
        if (keyMap == null) return;
        try { if (keyMap.containsKey("start_record")) keyRecord = Integer.parseInt(keyMap.get("start_record")); } catch (Exception ignored) {}
        try { if (keyMap.containsKey("stop_record")) keyStop = Integer.parseInt(keyMap.get("stop_record")); } catch (Exception ignored) {}
        try { if (keyMap.containsKey("play_macro")) keyPlay = Integer.parseInt(keyMap.get("play_macro")); } catch (Exception ignored) {}
        try { if (keyMap.containsKey("abort_macro_operation")) keyAbort = Integer.parseInt(keyMap.get("abort_macro_operation")); } catch (Exception ignored) {}
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setOpaque(false);
        JPanel identity = new JPanel();
        identity.setOpaque(false);
        identity.setLayout(new BoxLayout(identity, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("MouseMacros");
        title.setForeground(TEXT);
        title.setFont(new Font(Font.DIALOG, Font.BOLD, 22));
        JLabel subtitle = new JLabel(Localizer.get("timeline.subtitle"));
        subtitle.setName("timeline.subtitle");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
        subtitle.setBorder(new EmptyBorder(5, 1, 0, 0));
        identity.add(title);
        identity.add(subtitle);

        JPanel status = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        status.setOpaque(false);
        stateDot = new JLabel("●");
        stateDot.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        stateLabel = metricLabel("READY");
        actionCountLabel = metricLabel("0 actions");
        durationLabel = metricLabel("00:00");
        status.add(stateDot);
        status.add(stateLabel);
        status.add(dividerLabel());
        status.add(actionCountLabel);
        status.add(dividerLabel());
        status.add(durationLabel);
        header.add(identity, BorderLayout.WEST);
        status.setBorder(new EmptyBorder(12, 0, 0, 0));
        header.add(status, BorderLayout.SOUTH);
        return header;
    }

    private JPanel buildTimeline() {
        JPanel card = new JPanel(new BorderLayout());
        card.setName("timeline.card");
        card.setBackground(SURFACE);
        card.setBorder(BorderFactory.createLineBorder(LINE));
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setName("timeline.titleBar");
        titleBar.setBackground(SURFACE_RAISED);
        titleBar.setBorder(new EmptyBorder(10, 14, 10, 14));
        JLabel title = new JLabel(Localizer.get("timeline.title"));
        title.setForeground(TEXT);
        title.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
        JLabel hint = new JLabel(Localizer.get("timeline.hint"));
        hint.setForeground(MUTED);
        hint.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        title.setName("timeline.title");
        hint.setName("timeline.hint");
        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(hint, BorderLayout.EAST);

        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(SURFACE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        card.add(titleBar, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new GridLayout(2, 1, 0, 8));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(2, 0, 0, 0));
        JPanel transport = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        transport.setOpaque(false);
        transport.add(startBtn); transport.add(stopBtn); transport.add(playBtn); transport.add(abortBtn);
        JPanel files = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        files.setOpaque(false);
        files.add(saveBtn); files.add(loadBtn); files.add(settingsBtn);
        bar.add(transport);
        bar.add(files);
        return bar;
    }

    private void styleTimeline() {
        logArea.setRowHeight(30);
        logArea.setShowGrid(true);
        logArea.setGridColor(LINE);
        logArea.setIntercellSpacing(new Dimension(1, 1));
        logArea.setBackground(SURFACE);
        logArea.setForeground(TEXT);
        logArea.setSelectionBackground(new Color(0x304A66));
        logArea.setSelectionForeground(Color.WHITE);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        logArea.setFillsViewportHeight(true);
        if (logArea.getRowSorter() == null) logArea.setAutoCreateRowSorter(true);
        logArea.getTableHeader().setReorderingAllowed(false);
        logArea.getTableHeader().setResizingAllowed(true);
        logArea.getTableHeader().setBackground(SURFACE_RAISED);
        logArea.getTableHeader().setForeground(MUTED);
        logArea.getTableHeader().setFont(new Font(Font.DIALOG, Font.BOLD, 11));
        logArea.getTableHeader().setPreferredSize(new Dimension(0, 34));
        int[] widths = {45, 82, 125, 160, 180, 75};
        for (int i = 0; i < widths.length; i++) logArea.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        DefaultTableCellRenderer renderer = new TimelineCellRenderer();
        for (int i = 0; i < logArea.getColumnCount(); i++) logArea.getColumnModel().getColumn(i).setCellRenderer(renderer);
    }

    private JButton actionButton(String text, Color accent) {
        JButton button = new JButton(text);
        styleButton(button, accent);
        return button;
    }

    private void styleButton(JButton button, Color accent) {
        button.setForeground(TEXT);
        button.setBackground(new Color((SURFACE.getRed() * 4 + accent.getRed()) / 5,
                (SURFACE.getGreen() * 4 + accent.getGreen()) / 5,
                (SURFACE.getBlue() * 4 + accent.getBlue()) / 5));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 150)),
                new EmptyBorder(8, 13, 8, 13)));
        button.setFocusPainted(false);
        button.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        button.setOpaque(true);
    }

    private JButton quietButton(String text) {
        JButton button = new JButton(text);
        styleQuietButton(button);
        return button;
    }

    private void styleQuietButton(JButton button) {
        button.setForeground(TEXT);
        button.setBackground(SURFACE_RAISED);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE), new EmptyBorder(8, 12, 8, 12)));
        button.setFocusPainted(false);
        button.setFont(new Font(Font.DIALOG, Font.PLAIN, 13));
        button.setOpaque(true);
    }

    private JLabel metricLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        return label;
    }

    private JLabel dividerLabel() {
        JLabel divider = new JLabel("·");
        divider.setForeground(LINE);
        return divider;
    }

    private void wireActions() {
        startBtn.addActionListener(e -> {
            if (!MacroManager.isRecording() && !HotkeyDialog.inHotKeyDialog) MacroManager.startRecording();
            refreshRuntimeState();
        });
        stopBtn.addActionListener(e -> {
            if (MacroManager.isRecording() && !HotkeyDialog.inHotKeyDialog) MacroManager.stopRecording();
            else log(Localizer.get("log.macro_not_recording"));
            refreshRuntimeState();
        });
        playBtn.addActionListener(e -> {
            if (!MacroManager.isRecording() && !HotkeyDialog.inHotKeyDialog) {
                if (MacroManager.isPaused()) MacroManager.resume(); else MacroManager.play();
            }
            refreshRuntimeState();
        });
        abortBtn.addActionListener(e -> {
            if (MacroManager.isPlaying() && !HotkeyDialog.inHotKeyDialog) {
                if (MacroManager.isPaused()) MacroManager.abort(); else MacroManager.pause();
            } else log(Localizer.get("log.macro_not_running"));
            refreshRuntimeState();
        });
        saveBtn.addActionListener(e -> { if (!MacroManager.isRecording()) MacroManager.saveToFile(this); });
        loadBtn.addActionListener(e -> { if (!MacroManager.isRecording()) MacroManager.loadFromFile(this); });
        settingsBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new SettingsDialog().setVisible(true)));
    }

    public void refreshRuntimeState() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::refreshRuntimeState);
            return;
        }
        boolean recording = MacroManager.isRecording();
        boolean playing = MacroManager.isPlaying();
        boolean paused = MacroManager.isPaused();
        if (!recording && recordingStartedAt > 0) {
            lastRecordingDuration = System.currentTimeMillis() - recordingStartedAt;
            recordingStartedAt = 0;
        }
        if (recording) {
            stateLabel.setText(Localizer.get("timeline.state.recording"));
            stateDot.setForeground(RED);
            if (recordingStartedAt == 0) {
                recordingStartedAt = System.currentTimeMillis();
                lastRecordingDuration = 0;
            }
        } else if (playing && paused) {
            stateLabel.setText(Localizer.get("timeline.state.paused")); stateDot.setForeground(ORANGE);
        } else if (playing) {
            stateLabel.setText(Localizer.get("timeline.state.playing")); stateDot.setForeground(GREEN);
        } else {
            stateLabel.setText(Localizer.get("timeline.state.ready"));
            stateDot.setForeground(BLUE);
        }
        int count = MacroManager.getActions().size();
        actionCountLabel.setText(String.format(Localizer.get("timeline.action_count"), count));
        long elapsed = recording && recordingStartedAt > 0
                ? System.currentTimeMillis() - recordingStartedAt : lastRecordingDuration;
        durationLabel.setText(String.format("%02d:%02d", elapsed / 60000, (elapsed / 1000) % 60));
        startBtn.setEnabled(!recording && !playing);
        stopBtn.setEnabled(recording);
        playBtn.setEnabled((!recording && !playing) || paused);
        abortBtn.setEnabled(playing);
        saveBtn.setEnabled(!recording);
        loadBtn.setEnabled(!recording && !playing);
        refreshSpecialTexts();
    }

    public static void clearTimeline() {
        if (timelineModel != null) SwingUtilities.invokeLater(() -> { timelineModel.setRowCount(0); actionSequence = 0; });
    }

    public static void replaceTimelineWithActions(List<MouseAction> actions) {
        if (timelineModel == null) return;
        List<MouseAction> snapshot = new java.util.ArrayList<>(actions);
        SwingUtilities.invokeLater(() -> {
            timelineModel.setRowCount(0);
            actionSequence = 0;
            for (MouseAction action : snapshot) appendActionOnEdt(action);
            scrollToLatestRow();
        });
    }

    public static void appendAction(MouseAction action) {
        if (timelineModel != null) SwingUtilities.invokeLater(() -> appendActionOnEdt(action));
    }

    private static void appendActionOnEdt(MouseAction action) {
        String input = actionInput(action);
        String position = action.type == 10 || action.type == 11 ? "—" : action.x + ", " + action.y;
        timelineModel.addRow(new Object[]{++actionSequence, LocalTime.now().format(CLOCK),
                actionType(action), input, position, action.delay});
        scrollToLatestRow();
    }

    public static void appendSystemLog(String message) {
        if (timelineModel == null) return;
        SwingUtilities.invokeLater(() -> {
            timelineModel.addRow(new Object[]{null, LocalTime.now().format(CLOCK), Localizer.get("timeline.system"), message, "—", null});
            scrollToLatestRow();
        });
    }

    private static String actionType(MouseAction action) {
        switch (action.type) {
            case 0:
            case 7:
                return Localizer.get("timeline.move");
            case 1:
                return Localizer.get("timeline.mouse_down");
            case 2:
                return Localizer.get("timeline.mouse_up");
            case 3:
                return Localizer.get("timeline.wheel");
            case 10:
                return Localizer.get("timeline.key_down");
            case 11:
                return Localizer.get("timeline.key_up");
            default:
                return action.getType();
        }
    }

    private static String actionInput(MouseAction action) {
        if (action.type == 10 || action.type == 11) return OtherUtil.getNativeKeyDisplayText(action.keyCode);
        if (action.type == 3) return "× " + action.wheelAmount;
        if (action.button == 1) return Localizer.get("timeline.left");
        if (action.button == 2) return Localizer.get("timeline.middle");
        if (action.button == 3) return Localizer.get("timeline.right");
        return "—";
    }

    public void refreshMainFrameTexts() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::refreshMainFrameTexts);
            return;
        }
        for (int i = 0; i < timelineColumns().length; i++)
            logArea.getColumnModel().getColumn(i).setHeaderValue(timelineColumns()[i]);
        refreshLabels(getContentPane());
        applyTheme();
        refreshRuntimeState();
        setTitle(Localizer.get("main_frame"));
        refreshSpecialTexts();
        saveBtn.setText(Localizer.get("main_frame.save_macro"));
        loadBtn.setText(Localizer.get("main_frame.load_macro"));
        settingsBtn.setText(Localizer.get("settings"));
        if (trayIcon != null) {
            trayIcon.setToolTip(Localizer.get("main_frame"));
            if (trayIcon.getPopupMenu() != null && trayIcon.getPopupMenu().getItemCount() >= 3) {
                trayIcon.getPopupMenu().getItem(0).setLabel(Localizer.get("tray.show_main_menu"));
                trayIcon.getPopupMenu().getItem(2).setLabel(Localizer.get("tray.exit"));
            }
        }
        readjust();
    }

    public void refreshSpecialTexts() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::refreshSpecialTexts);
            return;
        }
        if (startBtn == null) return;
        startBtn.setText(getStartBtnText());
        stopBtn.setText(getStopBtnText());
        playBtn.setText(getPlayBtnText());
        abortBtn.setText(getAbortBtnText());
    }

    private String getStartBtnText() { return "● " + Localizer.get("main_frame.start_record") + "  " + OtherUtil.getNativeKeyDisplayText(keyRecord); }
    private String getStopBtnText() { return "■ " + Localizer.get("main_frame.stop_record") + "  " + OtherUtil.getNativeKeyDisplayText(keyStop); }
    private String getPlayBtnText() { return "▶ " + (MacroManager.isPaused() ? Localizer.get("main_frame.resume_macro") : Localizer.get("main_frame.play_macro")) + "  " + OtherUtil.getNativeKeyDisplayText(keyPlay); }
    private String getAbortBtnText() { return "Ⅱ " + (MacroManager.isPaused() ? Localizer.get("main_frame.abort_macro_operation") : Localizer.get("main_frame.pause_macro")) + "  " + OtherUtil.getNativeKeyDisplayText(keyAbort); }

    public void readjust() {
        Dimension minimum = new Dimension(760, 560);
        setMinimumSize(minimum);
        String cached = CacheManager.cache.windowSizeMap.get(getName());
        Dimension desired = new Dimension(960, 640);
        if (cached != null && ConfigManager.getInt("readjust_frame_mode") != ConfigManager.RFM_STANDARDIZED) {
            try {
                String[] parts = cached.split("[, *]");
                if (parts.length == 2) desired = new Dimension(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            } catch (NumberFormatException ignored) { }
        }
        Dimension layout = getContentPane().getPreferredSize();
        Insets frameInsets = getInsets();
        minimum.width = Math.max(minimum.width, layout.width + frameInsets.left + frameInsets.right);
        Rectangle screen = getGraphicsConfiguration().getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        int maxWidth = screen.width - screenInsets.left - screenInsets.right;
        int maxHeight = screen.height - screenInsets.top - screenInsets.bottom;
        minimum.width = Math.min(minimum.width, maxWidth);
        minimum.height = Math.min(minimum.height, maxHeight);
        setMinimumSize(minimum);
        setSize(Math.min(maxWidth, Math.max(minimum.width, desired.width)),
                Math.min(maxHeight, Math.max(minimum.height, desired.height)));
        validate();
    }

    private void initTrayIcon() {
        if (!SystemTray.isSupported()) return;
        if (tray == null) tray = SystemTray.getSystemTray();
        if (trayIcon == null) {
            Image trayImage = new ImageIcon(Objects.requireNonNull(
                    getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage();
            PopupMenu popupMenu = new PopupMenu();
            MenuItem showItem = new MenuItem(Localizer.get("tray.show_main_menu"));
            MenuItem exitItem = new MenuItem(Localizer.get("tray.exit"));
            popupMenu.add(showItem); popupMenu.addSeparator(); popupMenu.add(exitItem);
            trayIcon = new TrayIcon(trayImage, Localizer.get("main_frame"), popupMenu);
            trayIcon.setImageAutoSize(true);
            showItem.addActionListener(e -> restoreFromTray());
            exitItem.addActionListener(e -> { tray.remove(trayIcon); System.exit(0); });
            trayIcon.addActionListener(e -> restoreFromTray());
        }
    }

    public void minimizeToTray() {
        initTrayIcon();
        if (tray != null && trayIcon != null) {
            try { tray.add(trayIcon); setVisible(false); }
            catch (AWTException ex) { log(Localizer.get("log.adding_tray_failed") + ":" + ex.getMessage()); setExtendedState(getExtendedState() | ICONIFIED); }
        } else setExtendedState(getExtendedState() | ICONIFIED);
    }

    private void restoreFromTray() {
        setVisible(true); setExtendedState(JFrame.NORMAL);
        if (tray != null && trayIcon != null) tray.remove(trayIcon);
        toFront();
    }

    private static void scrollToLatestRow() {
        if (timelineModel.getRowCount() == 0) return;
        int viewRow = logArea.convertRowIndexToView(timelineModel.getRowCount() - 1);
        if (viewRow >= 0) logArea.scrollRectToVisible(logArea.getCellRect(viewRow, 0, true));
    }

    private static Object[] timelineColumns() {
        return new Object[]{"#", Localizer.get("timeline.time"), Localizer.get("timeline.event"),
                Localizer.get("timeline.input"), Localizer.get("timeline.position"), Localizer.get("timeline.delay")};
    }

    private void refreshLabels(Container parent) {
        for (Component component : parent.getComponents()) {
            if (component instanceof JLabel && component.getName() != null && component.getName().startsWith("timeline."))
                ((JLabel) component).setText(Localizer.get(component.getName()));
            if (component instanceof Container) refreshLabels((Container) component);
        }
    }

    public void applyTheme() {
        boolean dark = ConfigManager.getBoolean("enable_dark_mode");
        WINDOW = new Color(dark ? 0x15181D : 0xF3F5F8);
        SURFACE = new Color(dark ? 0x1D2128 : 0xFFFFFF);
        SURFACE_RAISED = new Color(dark ? 0x252A32 : 0xEAF0F6);
        LINE = new Color(dark ? 0x343A45 : 0xCCD5E0);
        TEXT = new Color(dark ? 0xE8ECF2 : 0x182538);
        MUTED = new Color(dark ? 0xA6B0BF : 0x52647A);
        themeComponents(getContentPane());
        styleTimeline();
        styleButton(startBtn, BLUE); styleButton(stopBtn, RED);
        styleButton(playBtn, GREEN); styleButton(abortBtn, ORANGE);
        styleQuietButton(saveBtn); styleQuietButton(loadBtn); styleQuietButton(settingsBtn);
        refreshRuntimeState();
        repaint();
    }

    private void themeComponents(Component component) {
        if (component instanceof JPanel) {
            component.setBackground("timeline.card".equals(component.getName()) ? SURFACE
                    : "timeline.titleBar".equals(component.getName()) ? SURFACE_RAISED : WINDOW);
        } else if (component instanceof JViewport) component.setBackground(SURFACE);
        else if (component instanceof JLabel) {
            component.setForeground("timeline.subtitle".equals(component.getName())
                    || "timeline.hint".equals(component.getName()) ? MUTED : TEXT);
        }
        if (component instanceof Container)
            for (Component child : ((Container) component).getComponents()) themeComponents(child);
    }

    @Override public void dispose() {
        if (stateTimer != null) stateTimer.stop();
        super.dispose();
    }

    private static class TimelineCellRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                                  boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            if (value == null) setText("—");
            else if (column == 5) setText(value + " ms");
            setBorder(new EmptyBorder(0, 10, 0, 10));
            setHorizontalAlignment(column == 0 || column == 5 ? RIGHT : LEFT);
            if (!selected) {
                setBackground(row % 2 == 0 ? SURFACE : SURFACE_RAISED);
                setForeground(column == 2 && Localizer.get("timeline.system").equals(String.valueOf(table.getValueAt(row, 2))) ? (ConfigManager.getBoolean("enable_dark_mode") ? ORANGE : new Color(0x8A4B00)) : TEXT);
            }
            return this;
        }
    }
}
