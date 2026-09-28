package io.github.samera2022.mousemacros.api.script;

import java.awt.Point;

/**
 * Provides screen information and coordinate conversion utilities.
 */
public interface IScreenInfo {
    /**
     * Gets the width of the primary screen.
     *
     * @return Screen width in pixels
     */
    int getWidth();

    /**
     * Gets the height of the primary screen.
     *
     * @return Screen height in pixels
     */
    int getHeight();

    /**
     * Gets the number of screens (monitors) attached to the system.
     *
     * @return Number of screens
     */
    int getScreenCount();

    /**
     * Gets the virtual screen origin point (the top-left corner of all screens combined).
     *
     * @return Point representing the virtual origin
     */
    Point getVirtualOrigin();

    /**
     * Normalizes screen coordinates to the virtual origin.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return Normalized point
     */
    Point normalizeToVirtualOrigin(int x, int y);

    /**
     * Denormalizes coordinates from virtual origin to primary screen coordinates.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return Denormalized point
     */
    Point denormalizeFromVirtualOrigin(int x, int y);
}

