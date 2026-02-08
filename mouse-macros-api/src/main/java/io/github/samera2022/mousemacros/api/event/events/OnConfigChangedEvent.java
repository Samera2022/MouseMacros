package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.event.Event;

/**
 * Event fired when a configuration value has been changed.
 * This allows scripts to react to configuration changes at runtime.
 */
public class OnConfigChangedEvent extends Event {
    private final String configKey;
    private final Object oldValue;
    private final Object newValue;

    /**
     * Creates a new OnConfigChangedEvent.
     *
     * @param configKey The key of the configuration that changed
     * @param oldValue The previous value (may be null)
     * @param newValue The new value
     */
    public OnConfigChangedEvent(String configKey, Object oldValue, Object newValue) {
        this.configKey = configKey;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Gets the key of the configuration that changed.
     *
     * @return The configuration key
     */
    public String getConfigKey() {
        return configKey;
    }

    /**
     * Gets the previous value of the configuration.
     *
     * @return The old value, or null if there was no previous value
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * Gets the new value of the configuration.
     *
     * @return The new value
     */
    public Object getNewValue() {
        return newValue;
    }
}
