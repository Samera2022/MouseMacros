package io.github.samera2022.mousemacros.api.script;

/**
 * Provides macro state information to scripts.
 */
public interface IMacroInfo {
    /**
     * Checks if a macro is currently being recorded.
     *
     * @return true if recording, false otherwise
     */
    boolean isRecording();

    /**
     * Checks if a macro is currently playing.
     *
     * @return true if playing, false otherwise
     */
    boolean isPlaying();

    /**
     * Checks if macro playback is paused.
     *
     * @return true if paused, false otherwise
     */
    boolean isPaused();

    /**
     * Gets the number of actions in the current macro.
     *
     * @return Number of actions
     */
    int getActionsCount();
}

