package io.github.samera2022.mousemacros.api.event;

/**
 * Defines the priority levels for event handlers.
 * Handlers are executed in order from LOWEST to MONITOR.
 */
public enum EventPriority {
    /** Lowest priority - executed first */
    LOWEST,
    /** Low priority */
    LOW,
    /** Normal priority - the default */
    NORMAL,
    /** High priority */
    HIGH,
    /** Highest priority */
    HIGHEST,
    /** Monitor priority - executed last, should only observe and not modify events */
    MONITOR
}
