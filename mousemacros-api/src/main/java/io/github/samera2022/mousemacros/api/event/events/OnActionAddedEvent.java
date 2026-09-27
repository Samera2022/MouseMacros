package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.action.IMouseAction;
import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;

/**
 * Event fired when an action is added to the macro during recording.
 * This event can be cancelled to prevent the action from being added.
 */
public class OnActionAddedEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    private final IMouseAction action;
    private final int currentIndex;

    /**
     * Creates a new OnActionAddedEvent.
     *
     * @param action The action being added
     * @param currentIndex The index where the action will be inserted
     */
    public OnActionAddedEvent(IMouseAction action, int currentIndex) {
        this.action = action;
        this.currentIndex = currentIndex;
    }

    /**
     * Gets the action being added.
     *
     * @return The mouse action
     */
    public IMouseAction getAction() {
        return action;
    }

    /**
     * Gets the index where the action will be inserted.
     *
     * @return The zero-based index
     */
    public int getCurrentIndex() {
        return currentIndex;
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
