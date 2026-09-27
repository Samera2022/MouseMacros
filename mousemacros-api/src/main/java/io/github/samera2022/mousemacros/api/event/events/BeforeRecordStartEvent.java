package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;

import java.awt.*;

/**
 * Event fired just before macro recording starts.
 * This event can be cancelled to prevent the recording from starting.
 */
public class BeforeRecordStartEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    private final long startTime;
    private final Point initialMousePos;

    /**
     * Creates a new BeforeRecordStartEvent.
     *
     * @param startTime The timestamp when recording is scheduled to start
     * @param initialMousePos The mouse position at the start of recording
     */
    public BeforeRecordStartEvent(long startTime, Point initialMousePos) {
        this.startTime = startTime;
        this.initialMousePos = initialMousePos;
    }

    /**
     * Gets the scheduled start time for recording.
     *
     * @return The start timestamp in milliseconds
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Gets the initial mouse position when recording starts.
     *
     * @return The mouse coordinates
     */
    public Point getInitialMousePos() {
        return initialMousePos;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
