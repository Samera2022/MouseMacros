package io.github.samera2022.mouse_macros;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final String CONFIG_DIR = "D" + System.getProperty("user.home").substring(1).replace('\\','/') + "/AppData/MouseMacros/";
    private static final String CONFIG_PATH = CONFIG_DIR + "config.cfg";
    private static final Gson gson = new Gson();
    static {
        System.out.println(CONFIG_DIR);
    }
    public static class Config {
        public boolean followSystemSettings = true;
        public String lang = "zh_cn";
        public boolean enableDarkMode = false;
        public String defaultMmcStoragePath = "";
        public Map<String, String> keyMap = new HashMap<>();
    }

    // 读取配置文件，若不存在则返回默认配置
    public static Config loadConfig() {
        try {
            String json = FileUtil.readFile(CONFIG_PATH);
            if (json == null || json.trim().isEmpty()) return new Config();
            return gson.fromJson(json, Config.class);
        } catch (IOException e) {
            return new Config();
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

    public static String getConfigPath() {
        return CONFIG_PATH;
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
}
