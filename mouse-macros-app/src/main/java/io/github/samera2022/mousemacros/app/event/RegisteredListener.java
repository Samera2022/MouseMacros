package io.github.samera2022.mousemacros.app.event;

import io.github.samera2022.mousemacros.api.event.EventPriority;
import io.github.samera2022.mousemacros.api.event.Listener;

import java.lang.reflect.Method;

public class RegisteredListener {
    private final Listener listener;
    private final Method method;
    private final EventPriority priority;
    private final boolean ignoreCancelled;

    public RegisteredListener(Listener listener, Method method, EventPriority priority, boolean ignoreCancelled) {
        this.listener = listener;
        this.method = method;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    public Listener getListener() { return listener; }
    public Method getMethod() { return method; }
    public EventPriority getPriority() { return priority; }
    public boolean isIgnoreCancelled() { return ignoreCancelled; }
}
