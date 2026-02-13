/**
 * Provides interfaces for script execution and interaction with the MouseMacros runtime.
 *
 * <p>This package contains the core interfaces that scripts (e.g., JavaScript) use to
 * interact with the MouseMacros application. Scripts can register event listeners,
 * access application context, and control macro execution.
 *
 * <h2>Main Interfaces</h2>
 * <ul>
 * <li>{@link io.github.samera2022.mousemacros.api.script.IScriptAPI} -
 *     The main API accessible to scripts, providing methods for event registration,
 *     logging, and context access</li>
 * <li>{@link io.github.samera2022.mousemacros.api.script.IScriptContext} -
 *     Provides runtime context methods for simulating actions, screen capture,
 *     notifications, and configuration access</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <p>In JavaScript, the API is exposed as a global object:
 * <pre>
 * // Register an event listener
 * api.on("io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent",
 *        function(event) {
 *            api.log("App launched: " + event.getAppVersion());
 *        });
 *
 * // Access the context
 * var ctx = api.getContext();
 *
 * // Get screen pixel color
 * var color = ctx.getPixelColor(100, 100);
 * api.log("Color at (100,100): " + color);
 *
 * // Show a notification
 * ctx.showToast("Hello", "Script is running!");
 *
 * // Access configuration
 * var config = ctx.getAppConfig();
 * var recordHotkey = config.getString("hotkey.record");
 * </pre>
 *
 * <h2>Script Lifecycle</h2>
 * <ol>
 * <li>Script is loaded and executed</li>
 * <li>Script registers event listeners using {@link io.github.samera2022.mousemacros.api.script.IScriptAPI#on}</li>
 * <li>Listeners are called when events occur</li>
 * <li>When script is unloaded, {@link io.github.samera2022.mousemacros.api.script.IScriptAPI#cleanup}
 *     is called to remove all listeners</li>
 * </ol>
 *
 * @see io.github.samera2022.mousemacros.api.event Event system documentation
 * @since 2.0.0
 */
package io.github.samera2022.mousemacros.api.script;

