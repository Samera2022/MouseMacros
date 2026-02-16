package io.github.samera2022.mousemacros;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.samera2022.mousemacros.constant.OtherConsts;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Localizer {
    private static Map<String, String> translations = new HashMap<>();
    private static final Map<String, String> fallbackTranslations; // en_us
    private static String currentLang;
    private static boolean runtimeSwitch = false;

    static {
        // 1. Always load en_us as a permanent fallback map.
        fallbackTranslations = loadLanguageMap("en_us");

        // 2. Detect system language and load it.
        String sysLang = java.util.Locale.getDefault().toString().toLowerCase();
        sysLang = sysLang.replace('-', '_');
        if (sysLang.startsWith("zh")) {
            sysLang = "zh_cn";
        } else {
            sysLang = "en_us"; // Default to en_us if not Chinese
        }
        load(sysLang);
    }

    /**
     * Helper method to load a specific language map from resources.
     * @param lang The language code (e.g., "en_us").
     * @return A map of translations, or an empty map if loading fails.
     */
    private static Map<String, String> loadLanguageMap(String lang) {
        String path = OtherConsts.RELATIVE_PATH + "langs/" + lang + ".json";
        try (InputStream in = Localizer.class.getResourceAsStream(path)) {
            if (in == null) {
                System.err.println("Language file not found: " + path);
                return Collections.emptyMap();
            }
            InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
            Map<String, String> loadedMap = new Gson().fromJson(reader, new TypeToken<Map<String, String>>() {}.getType());
            return loadedMap != null ? loadedMap : Collections.emptyMap();
        } catch (Exception e) {
            System.err.println("Failed to load or parse language file: " + path);
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    /**
     * Loads translations for the specified language.
     * @param lang The language code to load (e.g., "en_us").
     */
    public static void load(String lang) {
        if (lang == null) return;

        // If loading the fallback language, just copy the already loaded map.
        if (lang.equals("en_us")) {
            translations = new HashMap<>(fallbackTranslations);
        } else {
            translations = loadLanguageMap(lang);
        }
        currentLang = lang;
    }

    /**
     * Gets the translated string for a given key.
     * It falls back to English if the key is not found in the current language.
     * If still not found, it returns the key itself.
     * @param key The key for the translation.
     * @return The translated string.
     */
    public static String get(String key) {
        if (key == null) return "";

        // 1. Try to get from the current language map.
        String value = translations.get(key);
        if (value != null) {
            return value;
        }

        // 2. If not found, fall back to the English map.
        value = fallbackTranslations.get(key);

        // 3. If still not found, return the key itself.
        return value != null ? value : key;
    }

    public static String getS(String key) { return get("settings."+key); }

    public static boolean hasKey(String key) {
        return translations.containsKey(key) || fallbackTranslations.containsKey(key);
    }

    public static void setRuntimeSwitch(boolean enable) {
        runtimeSwitch = enable;
    }

    public static boolean isRuntimeSwitch() {
        return runtimeSwitch;
    }

    public static String getCurrentLang() {
        return currentLang;
    }
}