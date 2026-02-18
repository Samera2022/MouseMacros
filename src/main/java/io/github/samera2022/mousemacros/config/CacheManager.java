package io.github.samera2022.mousemacros.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.samera2022.mousemacros.util.FileUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CacheManager {

    private static final String CACHE_PATH = FileUtil.getLocalStoragePath() + (FileUtil.getLocalStoragePath().toString().endsWith("/") || FileUtil.getLocalStoragePath().toString().endsWith("\\") ? "" : java.io.File.separator) + "cache.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Cache cache = loadCache();

    public static final String EXIT_ON_CLOSE = "exit_on_close";
    public static final String MINIMIZE_TO_TRAY = "minimize_to_tray";
    public static final String UNKNOWN = "";

    public static class Cache {
        public String lastLoadDirectory = "";
        public String lastSaveDirectory = "";
        public java.util.Map<String, String> windowSizeMap = new java.util.HashMap<>(); // 窗体尺寸记忆
        public String defaultCloseOperation = "";
    }

    public static void reloadCache() {
        cache = loadCache();
    }

    public static void saveCache() {
        saveCache(cache);
    }

    private static void saveCache(Cache c) {
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

    private static Cache loadCache() {
        try {
            Path cachePath = Paths.get(CACHE_PATH);
            if (Files.exists(cachePath)) {
                String json = new String(Files.readAllBytes(cachePath), StandardCharsets.UTF_8);
                return gson.fromJson(json, Cache.class);
            }
        } catch (Exception e) {
            System.err.println("Failed to load cache: " + e.getMessage());
        }
        return new Cache();
    }
}
