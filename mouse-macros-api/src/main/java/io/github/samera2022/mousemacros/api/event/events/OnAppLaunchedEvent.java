package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.event.Event;

/**
 * Event fired when the MouseMacros application has finished launching.
 * This is typically the first event that scripts will receive.
 */
public class OnAppLaunchedEvent extends Event {
    private final String appVersion;
    private final String runtimeEnv;

    /**
     * Creates a new OnAppLaunchedEvent.
     *
     * @param appVersion The version of the application
     * @param runtimeEnv The runtime environment identifier
     */
    public OnAppLaunchedEvent(String appVersion, String runtimeEnv) {
        this.appVersion = appVersion;
        this.runtimeEnv = runtimeEnv;
    }

    /**
     * Gets the application version.
     *
     * @return The version string of the application
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * Gets the runtime environment identifier.
     *
     * @return The runtime environment (e.g., "JRE 11", "GraalVM")
     */
    public String getRuntimeEnv() {
        return runtimeEnv;
    }
}
