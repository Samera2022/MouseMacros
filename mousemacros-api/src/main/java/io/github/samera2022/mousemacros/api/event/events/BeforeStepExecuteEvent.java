package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.action.IMouseAction;
import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;

/**
 * Event fired just before a macro step is executed during playback.
 * This event can be cancelled to skip the execution of this step.
 */
public class BeforeStepExecuteEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    private final int stepIndex;
    private final IMouseAction action;
    private final boolean isLastStep;

    /**
     * Creates a new BeforeStepExecuteEvent.
     *
     * @param stepIndex The zero-based index of the step being executed
     * @param action The action that will be executed
     * @param isLastStep Whether this is the last step in the macro
     */
    public BeforeStepExecuteEvent(int stepIndex, IMouseAction action, boolean isLastStep) {
        this.stepIndex = stepIndex;
        this.action = action;
        this.isLastStep = isLastStep;
    }

    /**
     * Gets the index of the step being executed.
     *
     * @return The zero-based step index
     */
    public int getStepIndex() {
        return stepIndex;
    }

    /**
     * Gets the action that will be executed.
     *
     * @return The mouse action
     */
    public IMouseAction getAction() {
        return action;
    }

    /**
     * Checks if this is the last step in the macro.
     *
     * @return true if this is the last step, false otherwise
     */
    public boolean isLastStep() {
        return isLastStep;
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
