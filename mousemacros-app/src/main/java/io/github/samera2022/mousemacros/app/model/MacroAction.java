package io.github.samera2022.mousemacros.app.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single macro action in the table
 */
public class MacroAction {
    private final LocalTime timestamp;
    private final String type;
    private final String button;
    private final int x;
    private final int y;
    private final long delay;
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public MacroAction(LocalTime timestamp, String type, String button, int x, int y, long delay) {
        this.timestamp = timestamp;
        this.type = type;
        this.button = button;
        this.x = x;
        this.y = y;
        this.delay = delay;
    }
    
    public String getFormattedTime() {
        return timestamp.format(TIME_FORMATTER);
    }
    
    public String getType() {
        return type;
    }
    
    public String getButton() {
        return button;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public long getDelay() {
        return delay;
    }
}