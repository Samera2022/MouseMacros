package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.event.Event;

/**
 * Event fired when a macro loop iteration completes.
 * Provides information about the current progress of macro playback.
 */
public class OnLoopCompleteEvent extends Event {
    private final int completedIterations;
    private final int remainingIterations;

    /**
     * Creates a new OnLoopCompleteEvent.
     *
     * @param completedIterations The number of iterations completed so far
     * @param remainingIterations The number of iterations remaining
     */
    public OnLoopCompleteEvent(int completedIterations, int remainingIterations) {
        this.completedIterations = completedIterations;
        this.remainingIterations = remainingIterations;
    }

    /**
     * Gets the number of completed iterations.
     *
     * @return The count of completed iterations
     */
    public int getCompletedIterations() {
        return completedIterations;
    }

    /**
     * Gets the number of remaining iterations.
     *
     * @return The count of remaining iterations, or -1 for infinite loops
     */
    public int getRemainingIterations() {
        return remainingIterations;
    }
}
