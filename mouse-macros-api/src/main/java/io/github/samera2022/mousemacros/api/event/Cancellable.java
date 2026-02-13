package io.github.samera2022.mousemacros.api.event;

/**
 * Interface for events that can be cancelled.
 * When an event is cancelled, subsequent handlers may choose to ignore it
 * (if they have {@code ignoreCancelled = true}), and the default action may be prevented.
 */
public interface Cancellable {
    /**
     * Checks whether this event has been cancelled.
     *
     * @return true if the event is cancelled, false otherwise
     */
    boolean isCancelled();

    /**
     * Sets the cancellation state of this event.
     *
     * @param cancel true to cancel the event, false to un-cancel it
     */
    void setCancelled(boolean cancel);
}
