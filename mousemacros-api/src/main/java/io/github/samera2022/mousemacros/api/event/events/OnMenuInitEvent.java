package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.event.Event;

import javax.swing.*;

/**
 * Event fired when the application popup menu is being initialized.
 * Allows scripts to add custom menu items to the tray icon popup menu.
 */
public class OnMenuInitEvent extends Event {
    private final JPopupMenu popupMenu;

    /**
     * Creates a new OnMenuInitEvent.
     *
     * @param popupMenu The popup menu being initialized
     */
    public OnMenuInitEvent(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    /**
     * Gets the popup menu being initialized.
     * Scripts can add custom menu items to this menu.
     *
     * @return The JPopupMenu instance
     */
    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }
}
