package io.github.samera2022.mousemacros.api.event;

/**
 * Defines priority levels for event handlers.
 * Handlers are executed in order from HIGHEST to LOWEST.
 * MONITOR priority is special - it should only observe events, never modify them.
 * 
 * @since 2.0.0
 */
public enum EventPriority {
    /**
     * Executed last, priority 0
     */
    LOWEST(0),
    
    /**
     * Low priority, priority 1
     */
    LOW(1),
    
    /**
     * Normal priority (default), priority 2
     */
    NORMAL(2),
    
    /**
     * High priority, priority 3
     */
    HIGH(3),
    
    /**
     * Executed first, priority 4
     */
    HIGHEST(4),
    
    /**
     * Monitor only - should not modify the event.
     * Executed after all other priorities, priority 5
     */
    MONITOR(5);
    
    private final int slot;
    
    EventPriority(int slot) {
        this.slot = slot;
    }
    
    /**
     * Gets the numeric priority slot.
     * Higher numbers execute later (except MONITOR which is special).
     *
     * @return The priority slot number
     */
    public int getSlot() {
        return slot;
    }
}
