package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.action.IMouseAction;
import io.github.samera2022.mousemacros.api.event.Event;

import java.util.Collections;
import java.util.List;

/**
 * Event fired after macro recording has stopped.
 * Provides access to the final recorded macro data.
 */
public class AfterRecordStopEvent extends Event {
    private final List<IMouseAction> finalMacroData;
    private final int totalActions;

    /**
     * Creates a new AfterRecordStopEvent.
     *
     * @param finalMacroData The final list of recorded actions
     */
    public AfterRecordStopEvent(List<IMouseAction> finalMacroData) {
        this.finalMacroData = finalMacroData;
        this.totalActions = finalMacroData.size();
    }

    /**
     * Gets an unmodifiable view of the final recorded macro data.
     *
     * @return An unmodifiable list of all recorded actions
     */
    public List<IMouseAction> getFinalMacroData() {
        return Collections.unmodifiableList(finalMacroData);
    }

    /**
     * Gets the total number of recorded actions.
     *
     * @return The count of actions in the recorded macro
     */
    public int getTotalActions() {
        return totalActions;
    }
}
