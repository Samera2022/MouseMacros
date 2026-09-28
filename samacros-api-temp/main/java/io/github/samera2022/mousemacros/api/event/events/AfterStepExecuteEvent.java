package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.event.Event;

/**
 * Event fired after a macro step has been executed during playback.
 * Provides information about the execution result.
 *
 * <p>Execution statuses:
 * <ul>
 * <li>"SUCCESS" - The step was executed successfully</li>
 * <li>"FAILED" - The step execution failed</li>
 * <li>"SKIPPED" - The step was skipped (e.g., cancelled by a handler)</li>
 * </ul>
 */
public class AfterStepExecuteEvent extends Event {
    private final int stepIndex;
    private final String executionStatus; // "SUCCESS", "FAILED", "SKIPPED"

    /**
     * Creates a new AfterStepExecuteEvent.
     *
     * @param stepIndex The zero-based index of the step that was executed
     * @param executionStatus The execution result ("SUCCESS", "FAILED", or "SKIPPED")
     */
    public AfterStepExecuteEvent(int stepIndex, String executionStatus) {
        this.stepIndex = stepIndex;
        this.executionStatus = executionStatus;
    }

    /**
     * Gets the index of the step that was executed.
     *
     * @return The zero-based step index
     */
    public int getStepIndex() {
        return stepIndex;
    }

    /**
     * Gets the execution status of the step.
     *
     * @return The execution status ("SUCCESS", "FAILED", or "SKIPPED")
     */
    public String getExecutionStatus() {
        return executionStatus;
    }
}
