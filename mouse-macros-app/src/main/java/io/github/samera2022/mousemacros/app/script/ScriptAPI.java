package io.github.samera2022.mousemacros.app.script;

import io.github.samera2022.mousemacros.api.event.Event;
import io.github.samera2022.mousemacros.api.event.EventHandler;
import io.github.samera2022.mousemacros.api.event.Listener;
import io.github.samera2022.mousemacros.api.script.IScriptAPI;
import io.github.samera2022.mousemacros.app.event.EventManager;
import io.github.samera2022.mousemacros.app.manager.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScriptAPI implements IScriptAPI {
    private final List<Listener> registeredListeners = new ArrayList<>();
    private final ScriptContext scriptContext;

    public ScriptAPI(ScriptContext scriptContext) {
        this.scriptContext = scriptContext;
    }

    @Override
    public void on(String eventClassName, Consumer<Event> callback) {
        try {
            Class<?> eventClass = Class.forName(eventClassName);
            if (Event.class.isAssignableFrom(eventClass)) {
                ScriptEventListener listener = new ScriptEventListener(eventClass, callback);
                EventManager.registerEvents(listener);
                registeredListeners.add(listener);
            } else {
                System.err.println("ScriptAPI Error: Class " + eventClassName + " is not a valid Event.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("ScriptAPI Error: Event class not found - " + eventClassName);
        }
    }

    @Override
    public void cleanup() {
        for (Listener listener : registeredListeners) {
            EventManager.unregisterEvents(listener);
        }
        registeredListeners.clear();
    }

    @Override
    public ScriptContext getContext() {
        return this.scriptContext;
    }

    @Override
    public void log(String message) { LogManager.log(message); }

    private static class ScriptEventListener implements Listener {
        private final Class<?> targetEventClass;
        private final Consumer<Event> callback;

        public ScriptEventListener(Class<?> targetEventClass, Consumer<Event> callback) {
            this.targetEventClass = targetEventClass;
            this.callback = callback;
        }

        @EventHandler
        public void onGenericEvent(Event event) {
            if (targetEventClass.isInstance(event)) {
                try {
                    callback.accept(event);
                } catch (Exception e) {
                    System.err.println("Error executing script callback for event: " + event.getClass().getSimpleName());
                    e.printStackTrace();
                }
            }
        }
    }
}
