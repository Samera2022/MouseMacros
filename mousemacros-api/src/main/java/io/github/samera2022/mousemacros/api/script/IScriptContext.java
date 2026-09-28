package io.github.samera2022.mousemacros.api.script;

import io.github.samera2022.mousemacros.api.action.IMouseAction;
import io.github.samera2022.mousemacros.api.config.IConfig;

import java.awt.Color;

/**
 * Provides core context methods for script execution, allowing scripts to interact
 * with the application's runtime environment.
 */
public interface IScriptContext {
    /**
     * Simulates a mouse action programmatically.
     *
     * @param action The mouse action to simulate
     */
    void simulate(IMouseAction action);

    /**
     * Retrieves the color of a pixel at the specified screen coordinates.
     *
     * @param x The X coordinate on the screen
     * @param y The Y coordinate on the screen
     * @return The color of the pixel at the given coordinates
     */
    Color getPixelColor(int x, int y);

    /**
     * Displays a toast notification to the user.
     *
     * @param title The title of the notification
     * @param msg The message body of the notification
     */
    void showToast(String title, String msg);

    /**
     * Retrieves the application configuration.
     *
     * @return The current application configuration
     */
    IConfig getAppConfig();

    /**
     * Gets system information (OS, Java version, DPI scale, dark mode, etc.).
     *
     * @return System information interface
     */
    ISystemInfo getSystemInfo();

    /**
     * Gets screen information (size, count, coordinate conversion, etc.).
     *
     * @return Screen information interface
     */
    IScreenInfo getScreenInfo();

    /**
     * Gets macro state information (recording, playing, paused, action count, etc.).
     *
     * @return Macro information interface
     */
    IMacroInfo getMacroInfo();

    /**
     * Gets internationalization (i18n) support for translations.
     *
     * @return Internationalization interface
     */
    II18n getI18n();

    /**
     * Gets mouse information (current position, etc.).
     *
     * @return Mouse information interface
     */
    IMouseInfo getMouseInfo();
}
