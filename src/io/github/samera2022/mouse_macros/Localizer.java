package io.github.samera2022.mouse_macros;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

public class Localizer {
    private static Map<String, String> translations = new HashMap<>();
    private static String currentLang;
    private static boolean runtimeSwitch = false;
    private static boolean isDevMode;

    static {
        // 判断是否为开发环境
        File devLangFile = new File("lang/zh_cn.json");
        isDevMode = devLangFile.exists();

        // 自动检测系统语言
        String sysLang = java.util.Locale.getDefault().toString().toLowerCase();
        sysLang = sysLang.replace('-', '_');
        if (sysLang.startsWith("zh")) sysLang = "zh_cn";
        else if (sysLang.startsWith("en")) sysLang = "en_us";
        // 检查本地化文件是否存在，否则用en_us
        if (isDevMode) {
            File langFile = new File("lang/" + sysLang + ".json");
            if (!langFile.exists()) langFile = new File("./lang/" + sysLang + ".json");
            if (!langFile.exists()) sysLang = "en_us";
        }
        currentLang = sysLang;
        load(currentLang);
    }

    public static void setRuntimeSwitch(boolean enable) {
        runtimeSwitch = enable;
    }

    public static boolean isRuntimeSwitch() {
        return runtimeSwitch;
    }

    public static boolean isDevMode() {
        return isDevMode;
    }

    public static void load(String lang) {
        try {
            if (isDevMode) {
                File file = new File("lang/" + lang + ".json");
                if (!file.exists()) file = new File("./lang/" + lang + ".json");
                translations = new Gson().fromJson(new FileReader(file), Map.class);
            } else {
                String path = "lang/" + lang + ".json";
                try (InputStream in = Localizer.class.getClassLoader().getResourceAsStream(path)) {
                    if (in == null) throw new FileNotFoundException(path);
                    InputStreamReader reader = new InputStreamReader(in, "UTF-8");
                    translations = new Gson().fromJson(reader, Map.class);
                }
            }
            currentLang = lang;
        } catch (Exception e) {
            translations = new HashMap<>();
        }
    }

    public static String get(String key) {
        String value = translations.get(key);
        if (value != null) return value;
        // 若当前语言缺失，尝试读取英文
        if (!"en_us".equals(currentLang)) {
            try {
                if (isDevMode) {
                    File file = new File("lang/en_us.json");
                    if (!file.exists()) file = new File("./lang/en_us.json");
                    Map<String, String> enMap = new Gson().fromJson(new FileReader(file), Map.class);
                    String enValue = enMap.get(key);
                    if (enValue != null) return enValue;
                } else {
                    String path = "lang/en_us.json";
                    try (InputStream in = Localizer.class.getClassLoader().getResourceAsStream(path)) {
                        if (in != null) {
                            InputStreamReader reader = new InputStreamReader(in, "UTF-8");
                            Map<String, String> enMap = new Gson().fromJson(reader, Map.class);
                            String enValue = enMap.get(key);
                            if (enValue != null) return enValue;
                        }
                    }
                }
            } catch (Exception ignored) {}
        }
        return key;
    }

    public static String getCurrentLang() {
        return currentLang;
    }
}