/**
 * Contains all event types that can be fired during MouseMacros application lifecycle.
 *
 * <p>Events are organized by their triggering phase:
 *
 * <h2>Application Lifecycle Events</h2>
 * <ul>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent} - Application startup</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.OnConfigChangedEvent} - Configuration changes</li>
 * </ul>
 *
 * <h2>Recording Events</h2>
 * <ul>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.BeforeRecordStartEvent} - Before recording starts (cancellable)</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.OnInputCapturedEvent} - Input captured during recording (cancellable)</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.OnActionAddedEvent} - Action added to macro (cancellable)</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.AfterRecordStopEvent} - After recording stops</li>
 * </ul>
 *
 * <h2>Playback Events</h2>
 * <ul>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent} - Before playback starts (cancellable)</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.BeforeStepExecuteEvent} - Before each step executes (cancellable)</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.AfterStepExecuteEvent} - After each step executes</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.OnLoopCompleteEvent} - Loop iteration completed</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.OnPlaybackAbortedEvent} - Playback aborted</li>
 * </ul>
 *
 * <h2>File Events</h2>
 * <ul>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.OnMacroLoadEvent} - Macro file loaded</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.OnMacroSaveEvent} - Macro file saved</li>
 * </ul>
 *
 * <h2>UI Events</h2>
 * <ul>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.OnMenuInitEvent} - Popup menu initialized</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.OnTooltipDisplayEvent} - Tooltip about to display</li>
 * </ul>
 *
 * <h2>Cancellable Events</h2>
 * <p>Events that implement {@link io.github.samera2022.mousemacros.api.event.Cancellable} can be cancelled
 * to prevent their default action:
 * <ul>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.BeforeRecordStartEvent}</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent}</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.BeforeStepExecuteEvent}</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.OnInputCapturedEvent}</li>
 * <li>{@link io.github.samera2022.mousemacros.api.event.events.OnActionAddedEvent}</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <pre>
 * public class MyListener implements Listener {
 *     &#64;EventHandler(priority = EventPriority.HIGH)
 *     public void onAppLaunched(OnAppLaunchedEvent event) {
 *         System.out.println("App version: " + event.getAppVersion());
 *     }
 *
 *     &#64;EventHandler
 *     public void beforePlayback(BeforePlaybackStartEvent event) {
 *         if (event.getMacroData().size() &gt; 100) {
 *             event.setCancelled(true); // Cancel playback for large macros
 *         }
 *     }
 * }
 * </pre>
 *
 * @since 2.0.0
 */
package io.github.samera2022.mousemacros.api.event.events;

