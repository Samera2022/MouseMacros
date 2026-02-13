package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.action.IMouseAction;
import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;

import java.util.Collections;
import java.util.List;

/**
 * Event fired just before macro playback starts.
 * This event can be cancelled to prevent the playback from starting.
 * Provides access to the macro data and repeat count.
 */
public class BeforePlaybackStartEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    private final List<IMouseAction> macroData;
    private final int repeatCount;

    /**
     * Creates a new BeforePlaybackStartEvent.
     *
     * @param macroData The list of actions in the macro to be played back
     * @param repeatCount The number of times the macro will repeat (-1 for infinite)
     */
    public BeforePlaybackStartEvent(List<IMouseAction> macroData, int repeatCount) {
        this.macroData = macroData;
        this.repeatCount = repeatCount;
    }

    /**
     * Gets an unmodifiable view of the macro data.
     *
     * @return An unmodifiable list of actions that will be executed
     */
    public List<IMouseAction> getMacroData() {
        return Collections.unmodifiableList(macroData);
    }

    /**
     * Gets the number of times the macro will repeat.
     *
     * @return The repeat count, or -1 for infinite looping
     */
    public int getRepeatCount() {
        return repeatCount;
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
