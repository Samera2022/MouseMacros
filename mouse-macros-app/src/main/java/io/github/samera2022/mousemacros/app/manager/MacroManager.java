package io.github.samera2022.mousemacros.app.manager;

import io.github.samera2022.mousemacros.api.event.events.*;
import io.github.samera2022.mousemacros.app.action.MouseAction;
import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.constant.FileConsts;
import io.github.samera2022.mousemacros.app.event.EventManager;
import io.github.samera2022.mousemacros.app.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.samera2022.mousemacros.app.manager.LogManager.log;

public class MacroManager {
    private static boolean recording;
    private static boolean playing = false;
    private static boolean paused = false;
    private static final Object pauseLock = new Object();
    private static final List<MouseAction> actions = new ArrayList<>();
    private static long lastTime = 0;
    private static Thread playThread = null;

    private static int currentLoop = 0;
    private static int currentActionIndex = 0;

    private static void preciseSleep(long millis) throws InterruptedException {
        if (millis <= 0) {
            return;
        }
        long sleepFor = millis - 1;
        long spinFor = 1_000_000L;

        if (sleepFor > 0) {
            Thread.sleep(sleepFor);
        }

        long end = System.nanoTime() + spinFor;
        while (System.nanoTime() < end) {
            // Busy-wait
        }
    }

    public static void startRecording() {
        long startTime = System.currentTimeMillis();
        Point initialMousePos = MouseInfo.getPointerInfo().getLocation();
        BeforeRecordStartEvent event = new BeforeRecordStartEvent(startTime, initialMousePos);
        EventManager.callEvent(event);
        if (event.isCancelled()) return;

        actions.clear();
        recording = true;
        lastTime = System.currentTimeMillis();
        log(Localizer.get("log.start_recording"));
    }

    public static void stopRecording() {
        recording = false;
        EventManager.callEvent(new AfterRecordStopEvent(new ArrayList<>(actions)));
        log(Localizer.get("log.stop_recording_msg1") + actions.size() + Localizer.get("log.stop_recording_msg2"));
    }

    public static void play() {
        if (paused) {
            resume();
            return;
        }
        if (playing) {
            return;
        }
        if (actions.isEmpty()) {
            log(Localizer.get("log.no_recorded_actions"));
            return;
        }

        int repeatTime = ConfigManager.getInt("repeat_times");
        BeforePlaybackStartEvent event = new BeforePlaybackStartEvent(new ArrayList<>(actions), repeatTime);
        EventManager.callEvent(event);
        if (event.isCancelled()) return;

        log(Localizer.get("log.start_playback"));
        playing = true;
        paused = false;
        currentLoop = 0;
        currentActionIndex = 0;

        playThread = new Thread(() -> {
            String abortReason = "COMPLETED";
            try {
                for (int i = 0; i < repeatTime; i++) {
                    currentLoop = i;
                    if (!playing || Thread.interrupted()) {
                        abortReason = "INTERRUPTED";
                        return;
                    }
                    for (int j = 0; j < actions.size(); j++) {
                        currentActionIndex = j;
                        MouseAction action = actions.get(j);

                        BeforeStepExecuteEvent stepEvent = new BeforeStepExecuteEvent(j, action, j == actions.size() - 1);
                        EventManager.callEvent(stepEvent);
                        if (stepEvent.isCancelled()) {
                            EventManager.callEvent(new AfterStepExecuteEvent(j, "SKIPPED"));
                            continue;
                        }

                        synchronized (pauseLock) {
                            while (paused) {
                                pauseLock.wait();
                            }
                        }
                        if (!playing || Thread.interrupted()) {
                            abortReason = "INTERRUPTED";
                            return;
                        }
                        
                        long sleepTime = ConfigManager.getBoolean("enable_quick_mode") ? 0 : action.delay;
                        preciseSleep(sleepTime);

                        action.perform();
                        EventManager.callEvent(new AfterStepExecuteEvent(j, "SUCCESS"));
                    }
                    OnLoopCompleteEvent loopEvent = new OnLoopCompleteEvent(i + 1, repeatTime - (i + 1));
                    EventManager.callEvent(loopEvent);
                }
            } catch (InterruptedException e) {
                abortReason = "INTERRUPTED";
                log(Localizer.get("log.macro_aborted"));
            } catch (Exception e) {
                abortReason = "EXCEPTION: " + e.getMessage();
                log(Localizer.get("log.playback_error") + e.getMessage());
            } finally {
                playing = false;
                paused = false;
                playThread = null;
                if (!abortReason.equals("COMPLETED")) {
                    EventManager.callEvent(new OnPlaybackAbortedEvent(abortReason, currentActionIndex));
                }
                SwingUtilities.invokeLater(MainFrame.MAIN_FRAME::refreshSpecialTexts);
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

    public static boolean isPaused() {
        return paused;
    }

    public static void pause() {
        if (playing && !paused) {
            paused = true;
            log(Localizer.get("log.macro_paused"));
            SwingUtilities.invokeLater(MainFrame.MAIN_FRAME::refreshSpecialTexts);
        }
    }

    public static void resume() {
        if (playing && paused) {
            synchronized (pauseLock) {
                paused = false;
                pauseLock.notifyAll();
            }
            log(Localizer.get("log.macro_resumed"));
            SwingUtilities.invokeLater(MainFrame.MAIN_FRAME::refreshSpecialTexts);
        }
    }

    public static void abort() {
        playing = false;
        paused = false;
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
        if (playThread != null && playThread.isAlive()) {
            playThread.interrupt();
        }
        SwingUtilities.invokeLater(MainFrame.MAIN_FRAME::refreshSpecialTexts);
    }

    public static void recordAction(MouseAction action) {
        OnActionAddedEvent event = new OnActionAddedEvent(action, actions.size());
        EventManager.callEvent(event);
        if (event.isCancelled()) return;

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

    public static void saveToFile(Component parent) {
        JFileChooser chooser = new JFileChooser();
        if (ConfigManager.getBoolean("enable_default_storage")) {
            String defaultPath = ConfigManager.getString("default_mmc_storage_path");
            if (defaultPath != null && !defaultPath.isEmpty()) {
                File dir = new File(defaultPath);
                if (!dir.exists()) dir.mkdirs();
                chooser.setCurrentDirectory(dir);
            }
        } else {
            String lastSaveDir = CacheManager.cache.lastSaveDirectory;
            if (lastSaveDir != null && !lastSaveDir.isEmpty()) {
                File dir = new File(lastSaveDir);
                if (!dir.exists()) dir.mkdirs();
                chooser.setCurrentDirectory(dir);
            }
        }
        chooser.setFileFilter(FileConsts.MMC_FILTER);

        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".mmc"))
                selectedFile = new File(selectedFile.getAbsolutePath() + ".mmc");
            
            CacheManager.cache.lastSaveDirectory = selectedFile.getParent();
            CacheManager.saveCache();
            if (CacheManager.cache.lastLoadDirectory == null || CacheManager.cache.lastLoadDirectory.isEmpty()) {
                CacheManager.cache.lastLoadDirectory = selectedFile.getParent();
                CacheManager.saveCache();
            }

            String content = actions.stream()
                    .map(a -> a.x + "," + a.y + "," + a.type + "," + a.button + "," + a.delay + "," + a.wheelAmount + "," + a.keyCode + "," + a.awtKeyCode)
                    .collect(Collectors.joining("\n"));
            
            OnMacroSaveEvent event = new OnMacroSaveEvent(selectedFile, content);
            EventManager.callEvent(event);

            try (PrintWriter out = new PrintWriter(selectedFile, StandardCharsets.UTF_8)) {
                out.print(event.getFormattedContent());
                log(Localizer.get("log.macro_saved") + chooser.getSelectedFile().getAbsolutePath());
            } catch (Exception ex) {
                log(Localizer.get("log.macro_saving_failed") + ex.getMessage());
            }
        }
    }

    public static void loadFromFile(Component parent) {
        JFileChooser chooser = new JFileChooser();
        if (ConfigManager.getBoolean("enable_default_storage")) {
            String defaultPath = ConfigManager.getString("default_mmc_storage_path");
            if (defaultPath != null && !defaultPath.isEmpty()) {
                File dir = new File(defaultPath);
                if (!dir.exists()) dir.mkdirs();
                chooser.setCurrentDirectory(dir);
            }
        } else {
            String lastLoadDir = CacheManager.cache.lastLoadDirectory;
            if (lastLoadDir != null && !lastLoadDir.isEmpty()) {
                File dir = new File(lastLoadDir);
                if (!dir.exists()) dir.mkdirs();
                chooser.setCurrentDirectory(dir);
            }
        }
        chooser.setFileFilter(FileConsts.MMC_FILTER);

        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            CacheManager.cache.lastLoadDirectory = selectedFile.getParent();
            CacheManager.saveCache();
            if (CacheManager.cache.lastSaveDirectory == null || CacheManager.cache.lastSaveDirectory.isEmpty()) {
                CacheManager.cache.lastSaveDirectory = selectedFile.getParent();
                CacheManager.saveCache();
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile), StandardCharsets.UTF_8))) {
                String rawContent = in.lines().collect(Collectors.joining("\n"));
                OnMacroLoadEvent event = new OnMacroLoadEvent(selectedFile, rawContent);
                EventManager.callEvent(event);
                
                String[] lines = event.getRawContent().split("\n");
                actions.clear();
                int lineNum = 0;
                for (String line : lines) {
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
                        } else if (arr.length == 5) {
                            int x = Integer.parseInt(arr[0]);
                            int y = Integer.parseInt(arr[1]);
                            int type = Integer.parseInt(arr[2]);
                            int button = Integer.parseInt(arr[3]);
                            long delay = Long.parseLong(arr[4]);
                            actions.add(new MouseAction(x, y, type, button, delay, 0, 0, 0));
                        }
                    } catch (Exception ex) {
                        log(Localizer.get("log.macro_loading_line_error") + lineNum + ": " + ex.getMessage());
                    }
                }
                log(Localizer.get("log.macro_loaded_msg1") + selectedFile.getAbsolutePath() + " (" + actions.size() + " " + Localizer.get("log.macro_loaded_msg2") + ")");
            } catch (Exception ex) {
                log(Localizer.get("log.macro_loading_failed") + ex.getMessage());
            }
        }
    }
}
