package io.github.samera2022.mousemacros.app.event;

import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;
import io.github.samera2022.mousemacros.api.event.Listener;
import io.github.samera2022.mousemacros.api.event.EventHandler;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private static final Map<Class<? extends Event>, List<RegisteredListener>> listeners = new ConcurrentHashMap<>();
    private static final Map<Class<? extends Event>, List<RegisteredListener>> listenerCache = new ConcurrentHashMap<>();

    public static void registerEvents(Listener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class) && method.getParameterCount() == 1) {
                Class<?> paramType = method.getParameterTypes()[0];
                if (Event.class.isAssignableFrom(paramType)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Event> eventClass = (Class<? extends Event>) paramType;
                    
                    listeners.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>()).add(new RegisteredListener(listener, method, method.getAnnotation(EventHandler.class).priority(), method.getAnnotation(EventHandler.class).ignoreCancelled()));
                }
            }
        }
        // Invalidate the cache whenever a new listener is registered.
        listenerCache.clear();
    }

    public static void unregisterEvents(Listener listener) {
        for (List<RegisteredListener> list : listeners.values()) {
            list.removeIf(rl -> rl.getListener() == listener);
        }
        // Invalidate the cache whenever a listener is unregistered.
        listenerCache.clear();
    }

    private static List<RegisteredListener> getHandlersFor(Class<? extends Event> eventClass) {
        // Compute and cache the sorted list of handlers for this event type.
        return listenerCache.computeIfAbsent(eventClass, clazz -> {
            Set<RegisteredListener> collected = new HashSet<>();
            Class<?> current = clazz;
            while (current != null && Event.class.isAssignableFrom(current)) {
                List<RegisteredListener> registered = listeners.get(current);
                if (registered != null) {
                    collected.addAll(registered);
                }
                current = current.getSuperclass();
            }
            List<RegisteredListener> sorted = new ArrayList<>(collected);
            sorted.sort(Comparator.comparing(RegisteredListener::getPriority));
            return sorted;
        });
    }

    public static void callEvent(Event event) {
        List<RegisteredListener> handlers = getHandlersFor(event.getClass());
        if (handlers == null || handlers.isEmpty()) {
            return;
        }

        for (RegisteredListener rl : handlers) {
            try {
                // The script listener (ScriptEventListener) is responsible for checking the event type instance.
                // We only need to check for cancellation here.
                if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && !rl.isIgnoreCancelled()) {
                    continue;
                }
                rl.getMethod().setAccessible(true);
                rl.getMethod().invoke(rl.getListener(), event);
            } catch (Exception e) {
                System.err.println("Error dispatching event " + event.getClass().getSimpleName() + " to " + rl.getListener().getClass().getName());
                e.printStackTrace();
            }
        }
    }
}
