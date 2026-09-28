package io.github.samera2022.mousemacros.api.script;

import io.github.samera2022.mousemacros.api.event.Event;

import java.util.function.Consumer;

/**
 * Defines the public API methods accessible to guest scripts (e.g., JavaScript).
 */
public interface IScriptAPI {
    /**
     * Registers a listener for a specific event type.
     * @param eventClassName The fully qualified name of the event class.
     * @param callback A JavaScript function that will be executed when the event is triggered.
     */
    void on(String eventClassName, Consumer<Event> callback);

    /**
     * Unregisters all listeners created by this script.
     */
    void cleanup();

    /**
     * Provides access to the core script context.
     * @return The singleton instance of the ScriptContext.
     */
    IScriptContext getContext();

    /**
     * Prints a message to the console, prefixed with [Script].
     * @param message The message to log.
     */
    void log(String message);
}
