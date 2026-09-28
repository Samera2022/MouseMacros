package io.github.samera2022.mousemacros.app.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;
import io.github.samera2022.mousemacros.app.ui.frame.settings.SettingsRegistry;
import io.github.samera2022.mousemacros.app.util.FileUtil;

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

            // Migrate old config keys before merging
            if (loadedSettings != null) {
                migrateLoadedSettings(loadedSettings);
            }
            settings.clear();
            settings.putAll(loadedSettings);
        } catch (IOException e) {
            resetToDefault();
            saveConfig();
        }
    }
    /**
     * Migrate old config keys to new format in loaded settings map.
     */
    private static void migrateLoadedSettings(Map<String, Object> loadedSettings) {
        Map<String, String> migrations = new HashMap<>();
        migrations.put("followSystemSettings", "follow_system_settings");
        migrations.put("switchLang", "switch_lang");
        migrations.put("enableDarkMode", "enable_dark_mode");
        migrations.put("allowLongStr", "allow_long_str");
        migrations.put("readjustFrameMode", "readjust_frame_mode");
        migrations.put("enableDefaultStorage", "enable_default_storage");
        migrations.put("defaultMmcStoragePath", "default_mmc_storage_path");
        migrations.put("enableQuickMode", "enable_quick_mode");
        migrations.put("enableCustomMacroSettings", "enable_custom_macro_settings");
        migrations.put("repeatTime", "repeat_times");
        migrations.put("repeatDelay", "repeat_delay");

        // Migrate old keys
        for (Map.Entry<String, String> entry : migrations.entrySet()) {
            if (loadedSettings.containsKey(entry.getKey())) {
                loadedSettings.put(entry.getValue(), loadedSettings.get(entry.getKey()));
                loadedSettings.remove(entry.getKey());
            }
        }

        // Convert readjust_frame_mode from string to integer
        Object readjustValue = loadedSettings.get("readjust_frame_mode");
        if (readjustValue instanceof String) {
            String strValue = (String) readjustValue;
            if ("MIXED".equals(strValue)) {
                loadedSettings.put("readjust_frame_mode", 1);
            } else if ("STANDARDIZED".equals(strValue)) {
                loadedSettings.put("readjust_frame_mode", 2);
            } else if ("MEMORIZED".equals(strValue)) {
                loadedSettings.put("readjust_frame_mode", 3);
            }
        }
        // Also handle incorrect numeric types (e.g. 0.0 should become 0 or use default 2)
        else if (readjustValue instanceof Number) {
            int intValue = ((Number) readjustValue).intValue();
            if (intValue < 1 || intValue > 3) {
                // Invalid value, use default
                loadedSettings.put("readjust_frame_mode", 2);
            } else {
                loadedSettings.put("readjust_frame_mode", intValue);
            }
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
        // If not found in config, try to get default value from SettingsRegistry
        for (SettingsRegistry setting : SettingsRegistry.values()) {
            if (setting.i18nKey.equals(key) && setting.defaultValue instanceof Boolean) {
                return (Boolean) setting.defaultValue;
            }
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
        // If not found in config, try to get default value from SettingsRegistry
        for (SettingsRegistry setting : SettingsRegistry.values()) {
            if (setting.i18nKey.equals(key) && setting.defaultValue instanceof Number) {
                return ((Number) setting.defaultValue).doubleValue();
            }
        }
        return 0.0;
    }

    public static String getString(String key) {
        Object value = settings.get(key);
        if (value == null) {
            // Try to get default value from SettingsRegistry
            for (SettingsRegistry setting : SettingsRegistry.values()) {
                if (setting.i18nKey.equals(key) && setting.defaultValue != null) {
                    return setting.defaultValue.toString();
                }
            }
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
