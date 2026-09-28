package io.github.samera2022.mousemacros.api.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event handler within a {@link Listener} class.
 * The method must accept exactly one parameter of a type that extends {@link Event}.
 *
 * <p>Example usage:
 * <pre>
 * public class MyListener implements Listener {
 *     &#64;EventHandler(priority = EventPriority.HIGH)
 *     public void onAppLaunched(OnAppLaunchedEvent event) {
 *         // Handle event
 *     }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    /**
     * The priority of this event handler. Higher priorities are executed first.
     *
     * @return The event priority (default: NORMAL)
     */
    EventPriority priority() default EventPriority.NORMAL;

    /**
     * Whether this handler should ignore cancelled events.
     * If true, this handler will not be called for events that have been cancelled.
     *
     * @return true to ignore cancelled events, false otherwise (default: false)
     */
    boolean ignoreCancelled() default false;
}
