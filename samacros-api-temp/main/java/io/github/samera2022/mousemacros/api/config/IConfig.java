package io.github.samera2022.mousemacros.api.config;

import java.util.Map;

/**
 * Interface for application configuration, providing read-only access to settings.
 */
public interface IConfig {
    /**
     * Retrieves a boolean configuration value.
     *
     * @param key The configuration key
     * @return The boolean value associated with the key
     */
    public boolean getBoolean(String key);

    /**
     * Retrieves an integer configuration value.
     *
     * @param key The configuration key
     * @return The integer value associated with the key
     */
    public int getInt(String key);

    /**
     * Retrieves a double configuration value.
     *
     * @param key The configuration key
     * @return The double value associated with the key
     */
    double getDouble(String key);

    /**
     * Retrieves a string configuration value.
     *
     * @param key The configuration key
     * @return The string value associated with the key
     */
    String getString(String key);

    /**
     * Retrieves all key mappings in the configuration.
     *
     * @return A map of all configuration keys to their string values
     */
    Map<String, String> getKeyMap();
}
