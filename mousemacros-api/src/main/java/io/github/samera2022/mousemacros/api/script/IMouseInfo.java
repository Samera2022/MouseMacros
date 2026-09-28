package io.github.samera2022.mousemacros.api.script;

import java.awt.Point;

/**
 * Provides mouse information to scripts.
 */
public interface IMouseInfo {
    /**
     * Gets the current mouse cursor position.
     *
     * @return Point representing the mouse position, or null if unavailable
     */
    Point getPosition();
}

