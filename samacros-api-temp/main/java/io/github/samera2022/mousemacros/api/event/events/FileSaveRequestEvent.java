package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;

import java.io.File;
import java.util.function.Consumer;

/**
 * Event fired when a request is made to save a file.
 * GUI modules should listen for this event and show a save file dialog.
 * The result is communicated back via the provided callback.
 */
public class FileSaveRequestEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    private final String title;
    private final String extension;
    private final String description;
    private final Consumer<File> onFileSelected;
    private final Runnable onCancel;

    /**
     * Creates a new FileSaveRequestEvent.
     *
     * @param title The title for the save dialog
     * @param extension The file extension (e.g., "mmc")
     * @param description The description for the file type (e.g., "Mouse Macro Files")
     * @param onFileSelected Callback invoked when a file is selected (null if cancelled)
     * @param onCancel Callback invoked when the dialog is cancelled
     */
    public FileSaveRequestEvent(String title, String extension, String description,
                               Consumer<File> onFileSelected, Runnable onCancel) {
        this.title = title;
        this.extension = extension;
        this.description = description;
        this.onFileSelected = onFileSelected;
        this.onCancel = onCancel;
    }

    /**
     * Gets the title for the save dialog.
     *
     * @return The dialog title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the file extension.
     *
     * @return The file extension (without dot)
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Gets the description for the file type.
     *
     * @return The file type description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Invokes the file selected callback.
     * Should be called by the GUI module when a file is selected.
     *
     * @param file The selected file (null if cancelled)
     */
    public void onFileSelected(File file) {
        if (onFileSelected != null) {
            onFileSelected.accept(file);
        }
    }

    /**
     * Invokes the cancel callback.
     * Should be called by the GUI module when the dialog is cancelled.
     */
    public void onCancel() {
        if (onCancel != null) {
            onCancel.run();
        }
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