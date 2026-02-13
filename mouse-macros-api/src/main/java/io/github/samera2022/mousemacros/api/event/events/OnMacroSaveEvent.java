package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.event.Event;

import java.io.File;

/**
 * Event fired when a macro is saved to disk.
 * Provides access to both the file and its formatted content.
 */
public class OnMacroSaveEvent extends Event {
    private final File file;
    private final String formattedContent;

    /**
     * Creates a new OnMacroSaveEvent.
     *
     * @param file The file where the macro is being saved
     * @param formattedContent The formatted content that will be written to the file
     */
    public OnMacroSaveEvent(File file, String formattedContent) {
        this.file = file;
        this.formattedContent = formattedContent;
    }

    /**
     * Gets the file where the macro is being saved.
     *
     * @return The target file object
     */
    public File getFile() {
        return file;
    }

    /**
     * Gets the formatted content that will be saved.
     *
     * @return The macro content as a formatted string
     */
    public String getFormattedContent() {
        return formattedContent;
    }
}
