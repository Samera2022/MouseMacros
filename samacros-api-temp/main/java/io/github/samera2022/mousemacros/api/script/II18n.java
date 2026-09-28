package io.github.samera2022.mousemacros.api.script;

/**
 * Provides internationalization (i18n) support to scripts.
 */
public interface II18n {
    /**
     * Gets the translated string for a given key.
     *
     * @param key The translation key
     * @return The translated string, or the key itself if not found
     */
    String get(String key);

    /**
     * Checks if a translation key exists.
     *
     * @param key The translation key to check
     * @return true if the key exists, false otherwise
     */
    boolean hasKey(String key);

    /**
     * Gets the current language code.
     *
     * @return Current language code (e.g., "en_us", "zh_cn")
     */
    String getCurrentLanguage();
}

