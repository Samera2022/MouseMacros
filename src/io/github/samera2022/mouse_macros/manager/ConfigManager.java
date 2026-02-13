package io.github.samera2022.mouse_macros.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.manager.config.FileChooserConfig;
import io.github.samera2022.mouse_macros.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final String CONFIG_DIR = "D" + System.getProperty("user.home").substring(1).replace('\\','/') + "/AppData/MouseMacros/";
    private static final String CONFIG_PATH = CONFIG_DIR + "config.cfg";
    private static final String FILE_CHOOSER_CONFIG_PATH = CONFIG_DIR + "cache.json";
    private static final Gson gson = new Gson();
    public static Config config ;
    public static FileChooserConfig fc_config;
    static {
        config = loadConfig();
        fc_config = loadFileChooserConfig();
    }

    public static class Config {
        public boolean followSystemSettings = true;
        public String lang = "zh_cn";
        public boolean enableDarkMode = false;
        public String defaultMmcStoragePath = "";
        public Map<String, String> keyMap = new HashMap<>();
        public boolean enableCustomMacroSettings = false;
        public int repeatTime = 1;
    }

    public static void reloadConfig(){config = loadConfig();}

    // 读取配置文件，若不存在则返回默认配置
    public static Config loadConfig() {
        try {
            String json = FileUtil.readFile(CONFIG_PATH);
            if (json == null || json.trim().isEmpty()) return new Config();
            return gson.fromJson(json, Config.class);
        } catch (IOException e) {
            Config _config = new Config();
            saveConfig(_config);
            return _config;
        }
    }

    // 保存配置到文件
    public static void saveConfig(Config config) {
        try {
            // 确保父目录存在
            java.io.File dir = new java.io.File(CONFIG_DIR);
            if (!dir.exists()) dir.mkdirs();
            String json = gson.toJson(config);
            FileUtil.writeFile(CONFIG_PATH, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取lang目录下所有可用本地化文件名（不含后缀名），兼容开发与包执行
    public static String[] getAvailableLangs() {
        java.util.List<String> langs = new java.util.ArrayList<>();
        if (Localizer.isDevMode()) {
            String langDir = "lang";
            String[] files = FileUtil.listFileNames(langDir);
            if (files != null) {
                for (String name : files) {
                    int idx = name.lastIndexOf('.');
                    if (idx > 0) {
                        langs.add(name.substring(0, idx));
                    }
                }
            }
        } else {
            try {
                java.net.URL dirURL = ConfigManager.class.getClassLoader().getResource("lang/");
                if (dirURL != null && dirURL.getProtocol().equals("jar")) {
                    String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
                    java.util.jar.JarFile jar = new java.util.jar.JarFile(jarPath);
                    java.util.Enumeration<java.util.jar.JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        String entry = entries.nextElement().getName();
                        if (entry.startsWith("lang/") && entry.endsWith(".json")) {
                            String name = entry.substring("lang/".length(), entry.length() - ".json".length());
                            langs.add(name);
                        }
                    }
                    jar.close();
                } else if (dirURL != null && dirURL.getProtocol().equals("file")) {
                    // 兼容未打包但以file协议运行的情况
                    File dir = new File(dirURL.toURI());
                    File[] files = dir.listFiles((d, n) -> n.endsWith(".json"));
                    if (files != null) {
                        for (File f : files) {
                            String name = f.getName();
                            int idx = name.lastIndexOf('.');
                            if (idx > 0) {
                                langs.add(name.substring(0, idx));
                            }
                        }
                    }
                }
            } catch (Exception ignored) {}
        }
        return langs.toArray(new String[0]);
    }

    // 从文件加载配置
    public static FileChooserConfig loadFileChooserConfig() {
        try {
            Path configPath = Paths.get(FILE_CHOOSER_CONFIG_PATH);
            if (Files.exists(configPath)) {
                String json = new String(Files.readAllBytes(configPath), StandardCharsets.UTF_8);
                return gson.fromJson(json, FileChooserConfig.class);
            }
        } catch (Exception e) {
            System.err.println("Failed to load config: " + e.getMessage());
        }
        return new FileChooserConfig(); // 返回空配置
    }

    public static void reloadFileChooserConfig(){
        fc_config = loadFileChooserConfig();
    }

    // 保存配置到文件
    public static void saveFileChooserConfig(FileChooserConfig config) {
        try {
            Path configPath = Paths.get(FILE_CHOOSER_CONFIG_PATH);
            Files.createDirectories(configPath.getParent()); // 确保目录存在

            String json = gson.toJson(config);
            Files.write(configPath, json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }
}
