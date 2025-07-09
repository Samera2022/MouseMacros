package io.github.samera2022.mouse_macros.manager;

import io.github.samera2022.mouse_macros.action.MouseAction;
import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.constant.FileConsts;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.manager.config.FileChooserConfig;
import io.github.samera2022.mouse_macros.ui.component.CustomFileChooser;
import io.github.samera2022.mouse_macros.util.ComponentUtil;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.awt.Component;

import static io.github.samera2022.mouse_macros.manager.LogManager.log;
import static io.github.samera2022.mouse_macros.manager.ConfigManager.*;

public class MacroManager {
    private static boolean recording;
    private static boolean playing = false;
    private static final List<MouseAction> actions = new ArrayList<>();
    private static long lastTime = 0;
    private static Thread playThread = null;

    public static void startRecording() {
        actions.clear();
        recording = true;
        lastTime = System.currentTimeMillis();
        log(Localizer.get("start_recording"));
    }

    public static void stopRecording() {
        recording = false;
        log(Localizer.get("stop_recording_msg1") + actions.size() + Localizer.get("stop_recording_msg2"));
    }

    public static void play() {
        if (actions.isEmpty()) {
            log(Localizer.get("no_recorded_actions"));
            return;
        }
        log(Localizer.get("start_playback"));
        playing = true;
        playThread = new Thread(() -> {
            try {
                for (int i = 0; i < config.repeatTime; i++) {
                    for (MouseAction action : actions) {
                        if (Thread.interrupted()) {
                            return;
                        }
                        Thread.sleep(action.delay);
                        action.perform();
                    }
                }
                log(Localizer.get("playback_complete"));
            } catch (InterruptedException e) {
                log(Localizer.get("macro_aborted"));
            } catch (Exception e) {
                log(Localizer.get("playback_error") + e.getMessage());
            } finally {
                playing = false;
                playThread = null;
            }
        });
        playThread.start();
    }

    public static boolean isRecording() {
        return recording;
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static void abort() {
        playing = false;
        if (playThread != null && playThread.isAlive()) {
            playThread.interrupt();
            log(Localizer.get("macro_aborted"));
        }
        log(Localizer.get("macro_not_running"));
    }

    public static void recordAction(MouseAction action) {
        actions.add(action);
        lastTime = System.currentTimeMillis();
    }

    public static long getLastTime() {
        return lastTime;
    }

    public static void setLastTime(long t) {
        lastTime = t;
    }

    public static List<MouseAction> getActions() {
        return actions;
    }

    public static void clear() {actions.clear();}

    public static void saveToFile(Component parent) {
//        CustomFileChooser chooser = new CustomFileChooser(config.enableDarkMode? OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
        JFileChooser chooser = new JFileChooser();
        // 设置初始目录
        if (fc_config.getLastSaveDirectory() != null)
            chooser.setCurrentDirectory(fc_config.getLastSaveDirectory());
        chooser.setFileFilter(FileConsts.MMC_FILTER);
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            // 自动添加缺失的 .mmc 后缀
            if (!selectedFile.getName().toLowerCase().endsWith(".mmc")) 
                selectedFile = new File(selectedFile.getAbsolutePath() + ".mmc");
            fc_config.setLastSaveDirectory(selectedFile.getParentFile());
            saveFileChooserConfig(fc_config);
            reloadFileChooserConfig();
            try (PrintWriter out = new PrintWriter(selectedFile, StandardCharsets.UTF_8)) {
                for (MouseAction a : actions) {
                    out.println(a.x + "," + a.y + "," + a.type + "," + a.button + "," + a.delay + "," + a.wheelAmount + "," + a.keyCode + "," + a.awtKeyCode);
                }
                log(Localizer.get("macro_saved") + chooser.getSelectedFile().getAbsolutePath());
            } catch (Exception ex) {
                log(Localizer.get("macro_saving_failed") + ex.getMessage());
            }
        }
    }

    public static void loadFromFile(Component parent) {
//        CustomFileChooser chooser = new CustomFileChooser(config.enableDarkMode? OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
        JFileChooser chooser = new JFileChooser();
        if (fc_config.getLastLoadDirectory() != null)
            chooser.setCurrentDirectory(fc_config.getLastLoadDirectory());
        chooser.setFileFilter(FileConsts.MMC_FILTER);
        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            // 确保读取的文件是 UTF-8 编码（避免乱码问题）
            fc_config.setLastLoadDirectory(selectedFile.getParentFile());
            saveFileChooserConfig(fc_config);
            reloadFileChooserConfig();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(selectedFile),
                            StandardCharsets.UTF_8))) {
                actions.clear();
                String line;
                int lineNum = 0;
                while ((line = in.readLine()) != null) {
                    lineNum++;
                    try {
                        String[] arr = line.split(",");
                        if (arr.length == 8) {
                            int x = Integer.parseInt(arr[0]);
                            int y = Integer.parseInt(arr[1]);
                            int type = Integer.parseInt(arr[2]);
                            int button = Integer.parseInt(arr[3]);
                            long delay = Long.parseLong(arr[4]);
                            int wheelAmount = Integer.parseInt(arr[5]);
                            int keyCode = Integer.parseInt(arr[6]);
                            int awtKeyCode = Integer.parseInt(arr[7]);
                            actions.add(new MouseAction(x, y, type, button, delay, wheelAmount, keyCode, awtKeyCode));
                        } else if (arr.length == 7) {
                            int x = Integer.parseInt(arr[0]);
                            int y = Integer.parseInt(arr[1]);
                            int type = Integer.parseInt(arr[2]);
                            int button = Integer.parseInt(arr[3]);
                            long delay = Long.parseLong(arr[4]);
                            int wheelAmount = Integer.parseInt(arr[5]);
                            int keyCode = Integer.parseInt(arr[6]);
                            actions.add(new MouseAction(x, y, type, button, delay, wheelAmount, keyCode, 0));
                        } else if (arr.length == 6) {
                            int x = Integer.parseInt(arr[0]);
                            int y = Integer.parseInt(arr[1]);
                            int type = Integer.parseInt(arr[2]);
                            int button = Integer.parseInt(arr[3]);
                            long delay = Long.parseLong(arr[4]);
                            int wheelAmount = Integer.parseInt(arr[5]);
                            actions.add(new MouseAction(x, y, type, button, delay, wheelAmount, 0, 0));
                        } else if (arr.length == 5) { // 兼容旧格式
                            int x = Integer.parseInt(arr[0]);
                            int y = Integer.parseInt(arr[1]);
                            int type = Integer.parseInt(arr[2]);
                            int button = Integer.parseInt(arr[3]);
                            long delay = Long.parseLong(arr[4]);
                            actions.add(new MouseAction(x, y, type, button, delay, 0, 0, 0));
                        }
                    } catch (Exception ex) {
                        log(Localizer.get("macro_loading_line_error") + lineNum + ": " + ex.getMessage());
                    }
                }
                log(Localizer.get("macro_loaded_msg1") + chooser.getSelectedFile().getAbsolutePath() + " (" + actions.size() + " " + Localizer.get("macro_loaded_msg2") + ")");
            } catch (Exception ex) {
                log(Localizer.get("macro_loading_failed") + ex.getMessage());
            }
        }
    }
}
