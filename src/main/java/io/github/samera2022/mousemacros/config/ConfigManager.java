package io.github.samera2022.mousemacros.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.samera2022.mousemacros.Localizer;
import io.github.samera2022.mousemacros.constant.OtherConsts;
import io.github.samera2022.mousemacros.ui.frame.settings.SettingsRegistry;
import io.github.samera2022.mousemacros.util.FileUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigManager {
    public static String CONFIG_DIR;
    private static final String CONFIG_PATH;
    private static final String WHITELIST_PATH;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, Object> settings = new HashMap<>();

    public static final int RFM_MIXED = 1;
    public static final int RFM_STANDARDIZED = 2;
    public static final int RFM_MEMORIZED = 3;

    static {
        CONFIG_DIR = FileUtil.getLocalStoragePath().toString();
        CONFIG_PATH = Paths.get(CONFIG_DIR, "config.cfg").toString();
        WHITELIST_PATH = Paths.get(CONFIG_DIR, "white_list.json").toString();
        reload();
    }

    public static void reload() {
        loadConfig();
    }

    public static void loadConfig() {
        try {
            String json = FileUtil.readFile(CONFIG_PATH);
            if (json == null || json.trim().isEmpty()) {
                resetToDefault();
                saveConfig();
                return;
            }
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> loadedSettings = gson.fromJson(json, type);
            settings.clear();
            settings.putAll(loadedSettings);
        } catch (IOException e) {
            resetToDefault();
            saveConfig();
        }
    }

    public static void saveConfig() {
        try {
            java.io.File dir = new java.io.File(CONFIG_DIR);
            if (!dir.exists()) dir.mkdirs();
            String json = gson.toJson(settings);
            FileUtil.writeFile(CONFIG_PATH, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void resetToDefault() {
        settings.clear();
        for (SettingsRegistry setting : SettingsRegistry.values()) {
            settings.put(setting.i18nKey, setting.defaultValue);
        }

        // Set default keyMap values
        Map<String, String> defaultKeyMap = new HashMap<>();
        defaultKeyMap.put("start_record", "60"); // F1
        defaultKeyMap.put("stop_record", "61");  // F2
        defaultKeyMap.put("play_macro", "62");   // F3
        defaultKeyMap.put("abort_macro_operation", "63"); // F4
        settings.put("keyMap", defaultKeyMap);
    }

    public static void set(String key, Object value) {
        settings.put(key, value);
    }

    public static boolean getBoolean(String key) {
        Object value = settings.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }

    public static int getInt(String key) {
        Object value = settings.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }

    public static double getDouble(String key) {
        Object value = settings.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }

    public static String getString(String key) {
        Object value = settings.get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getKeyMap() {
        Object value = settings.get("keyMap");
        if (value instanceof Map) {
            return (Map<String, String>) value;
        }
        return new HashMap<>();
    }
    public static String[] getAvailableLangs() {
        List<String> langs = new ArrayList<>();
        try {
            String resPath = OtherConsts.RELATIVE_PATH + "langs/";
            URL resource = Localizer.class.getResource(resPath);
            if (resource != null) {
                URI uri = resource.toURI();
                Path myPath;
                if (uri.getScheme().equals("jar")) {
                    FileSystem fileSystem;
                    try {
                        fileSystem = FileSystems.getFileSystem(uri);
                    } catch (FileSystemNotFoundException e) {
                        fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    }
                    myPath = fileSystem.getPath(resPath);
                } else {
                    myPath = Paths.get(uri);
                }
                try (Stream<Path> paths = Files.walk(myPath, 1)) {
                    langs = paths
                            .filter(Files::isRegularFile)
                            .map(p -> p.getFileName().toString())
                            .filter(name -> name.endsWith(".json"))
                            .map(name -> name.substring(0, name.length() - 5))
                            .collect(Collectors.toList());
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return langs.toArray(new String[0]);
    }
}
