package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.event.Event;

/**
 * Event fired when a tooltip is about to be displayed.
 * Allows scripts to modify the tooltip text before it is shown to the user.
 */
public class OnTooltipDisplayEvent extends Event {
    private final String rawText;
    private String modifiedText;

    /**
     * Creates a new OnTooltipDisplayEvent.
     *
     * @param rawText The original tooltip text
     */
    public OnTooltipDisplayEvent(String rawText) {
        this.rawText = rawText;
        this.modifiedText = rawText;
    }

    /**
     * Gets the original tooltip text.
     *
     * @return The unmodified tooltip text
     */
    public String getRawText() {
        return rawText;
    }

    /**
     * Gets the current modified tooltip text.
     *
     * @return The tooltip text that will be displayed
     */
    public String getModifiedText() {
        return modifiedText;
    }

    /**
     * Sets the modified tooltip text.
     * This text will be displayed instead of the original.
     *
     * @param modifiedText The new tooltip text to display
     */
    public void setModifiedText(String modifiedText) {
        this.modifiedText = modifiedText;
    }
}
