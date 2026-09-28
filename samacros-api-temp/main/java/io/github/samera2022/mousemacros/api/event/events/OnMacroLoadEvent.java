package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.event.Event;

import java.io.File;

/**
 * Event fired when a macro file is loaded from disk.
 * Provides access to both the file and its raw content.
 */
public class OnMacroLoadEvent extends Event {
    private final File file;
    private final String rawContent;

    /**
     * Creates a new OnMacroLoadEvent.
     *
     * @param file The macro file that was loaded
     * @param rawContent The raw content of the macro file
     */
    public OnMacroLoadEvent(File file, String rawContent) {
        this.file = file;
        this.rawContent = rawContent;
    }

    /**
     * Gets the macro file that was loaded.
     *
     * @return The file object
     */
    public File getFile() {
        return file;
    }

    /**
     * Gets the raw content of the loaded macro.
     *
     * @return The file content as a string
     */
    public String getRawContent() {
        return rawContent;
    }
}
