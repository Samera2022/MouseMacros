package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.event.Event;

/**
 * Event fired when macro playback is aborted before completion.
 * Provides information about why and where the playback was interrupted.
 */
public class OnPlaybackAbortedEvent extends Event {
    private final String abortReason;
    private final int atStepIndex;

    /**
     * Creates a new OnPlaybackAbortedEvent.
     *
     * @param abortReason A description of why the playback was aborted
     * @param atStepIndex The step index where the abort occurred
     */
    public OnPlaybackAbortedEvent(String abortReason, int atStepIndex) {
        this.abortReason = abortReason;
        this.atStepIndex = atStepIndex;
    }

    /**
     * Gets the reason for the abort.
     *
     * @return A description of why playback was aborted
     */
    public String getAbortReason() {
        return abortReason;
    }

    /**
     * Gets the step index where the abort occurred.
     *
     * @return The zero-based step index
     */
    public int getAtStepIndex() {
        return atStepIndex;
    }
}
