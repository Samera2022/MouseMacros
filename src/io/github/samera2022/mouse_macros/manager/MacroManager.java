package io.github.samera2022.mouse_macros.manager;

import io.github.samera2022.mouse_macros.action.MouseAction;
import io.github.samera2022.mouse_macros.Localizer;
import java.util.ArrayList;
import java.util.List;
import java.awt.Component;
import javax.swing.JFileChooser;

import static io.github.samera2022.mouse_macros.manager.LogManager.log;

public class MacroManager {
    private static boolean recording = false;
    private static final List<MouseAction> actions = new ArrayList<>();
    private static long lastTime = 0;

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

    public static boolean isRecording() {
        return recording;
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

    public static void clear() {
        actions.clear();
    }

    public static void saveToFile(Component parent) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            try (java.io.PrintWriter out = new java.io.PrintWriter(chooser.getSelectedFile(), java.nio.charset.StandardCharsets.UTF_8)) {
                for (MouseAction a : actions) {
                    out.println(a.x + "," + a.y + "," + a.type + "," + a.button + "," + a.delay);
                }
                io.github.samera2022.mouse_macros.manager.LogManager.log("宏已保存: " + chooser.getSelectedFile().getAbsolutePath());
            } catch (Exception ex) {
                io.github.samera2022.mouse_macros.manager.LogManager.log("保存失败: " + ex.getMessage());
            }
        }
    }

    public static void loadFromFile(Component parent) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            try (java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(chooser.getSelectedFile()))) {
                actions.clear();
                String line;
                while ((line = in.readLine()) != null) {
                    String[] arr = line.split(",");
                    if (arr.length == 5) {
                        int x = Integer.parseInt(arr[0]);
                        int y = Integer.parseInt(arr[1]);
                        int type = Integer.parseInt(arr[2]);
                        int button = Integer.parseInt(arr[3]);
                        long delay = Long.parseLong(arr[4]);
                        actions.add(new MouseAction(x, y, type, button, delay));
                    }
                }
                io.github.samera2022.mouse_macros.manager.LogManager.log("宏已加载: " + chooser.getSelectedFile().getAbsolutePath() + " (" + actions.size() + " 步)");
            } catch (Exception ex) {
                io.github.samera2022.mouse_macros.manager.LogManager.log("加载失败: " + ex.getMessage());
            }
        }
    }
}
