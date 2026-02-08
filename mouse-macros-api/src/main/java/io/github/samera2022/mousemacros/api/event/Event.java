package io.github.samera2022.mousemacros.api.event;

/**
 * Base class for all events in the MouseMacros event system.
 * Each event is automatically timestamped upon creation.
 */
public abstract class Event {
    private final long timestamp;
    
    /**
     * Creates a new event with the current system timestamp.
     */
    public Event() {
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Gets the timestamp when this event was created.
     *
     * @return The timestamp in milliseconds since epoch
     */
    public long getTimestamp() {
        return timestamp;
    }
}
