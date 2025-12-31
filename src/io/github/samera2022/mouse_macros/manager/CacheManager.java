package io.github.samera2022.mouse_macros.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.samera2022.mouse_macros.util.FileUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CacheManager {
    // 保证cache.json与config.cfg同目录
    private static final String CACHE_PATH = FileUtil.getLocalStoragePath() + (FileUtil.getLocalStoragePath().toString().endsWith("/") || FileUtil.getLocalStoragePath().toString().endsWith("\\") ? "" : java.io.File.separator) + "cache.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static FileChooserCache cache = loadCache();

    public static final String EXIT_ON_CLOSE = "exit_on_close";
    public static final String MINIMIZE_TO_TRAY = "minimize_to_tray";
    public static final String UNKNOWN = "";

    public static class FileChooserCache {
        public String lastLoadDirectory = "";
        public String lastSaveDirectory = "";
        public java.util.Map<String, String> windowSizeMap = new java.util.HashMap<>(); // 窗体尺寸记忆
        public String defaultCloseOperation = "";
    }

    public static FileChooserCache getCache() {
        return cache;
    }

    public static void reloadCache() {
        cache = loadCache();
    }

    public static void saveCache() {
        saveCache(cache);
    }

    public static void saveCache(FileChooserCache c) {
        try {
            Path cachePath = Paths.get(CACHE_PATH);
            // 确保父目录存在
            if (cachePath.getParent() != null) Files.createDirectories(cachePath.getParent());
            String json = gson.toJson(c);
            Files.write(cachePath, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Failed to save cache: " + e.getMessage());
        }
    }

    public static FileChooserCache loadCache() {
        try {
            Path cachePath = Paths.get(CACHE_PATH);
            if (Files.exists(cachePath)) {
                String json = new String(Files.readAllBytes(cachePath), StandardCharsets.UTF_8);
                return gson.fromJson(json, FileChooserCache.class);
            }
        } catch (Exception e) {
            System.err.println("Failed to load cache: " + e.getMessage());
        }
        return new FileChooserCache();
    }

    public static String getLastLoadDirectory() {
        return cache.lastLoadDirectory;
    }
    public static void setLastLoadDirectory(String dir) {
        cache.lastLoadDirectory = dir;
        saveCache();
    }
    public static String getLastSaveDirectory() {
        return cache.lastSaveDirectory;
    }
    public static void setLastSaveDirectory(String dir) {
        cache.lastSaveDirectory = dir;
        saveCache();
    }
    // 窗体尺寸记忆
    public static String getWindowSize(String windowName) {
        if (cache.windowSizeMap == null) cache.windowSizeMap = new java.util.HashMap<>();
        return cache.windowSizeMap.get(windowName);
    }
    public static void setWindowSize(String windowName, String size) {
        if (cache.windowSizeMap == null) cache.windowSizeMap = new java.util.HashMap<>();
        cache.windowSizeMap.put(windowName, size);
        saveCache();
    }
    public static String getDefaultCloseOperation() { return cache.defaultCloseOperation; }
    public static void setDefaultCloseOperation(String defaultCloseOperation) {
        cache.defaultCloseOperation = defaultCloseOperation;
        saveCache();
    }
}
