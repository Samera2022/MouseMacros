package io.github.samera2022.mousemacros.api.event.events;

import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;

/**
 * Event fired when user input is captured during macro recording.
 * This event can be cancelled to prevent the input from being recorded.
 *
 * <p>Input types:
 * <ul>
 * <li>1 = Mouse Press</li>
 * <li>2 = Mouse Release</li>
 * <li>3 = Mouse Wheel</li>
 * <li>10 = Key Press</li>
 * <li>11 = Key Release</li>
 * </ul>
 */
public class OnInputCapturedEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    private final int inputType; // 1=MousePress, 2=MouseRelease, 3=Wheel, 10=KeyPress, 11=KeyRelease
    private final int keyCode;
    private final int x;
    private final int y;
    private final long delay;

    /**
     * Creates a new OnInputCapturedEvent.
     *
     * @param inputType The type of input (1-3 for mouse, 10-11 for keyboard)
     * @param keyCode The key or button code
     * @param x The X coordinate of the mouse
     * @param y The Y coordinate of the mouse
     * @param delay The delay since the last recorded action (in milliseconds)
     */
    public OnInputCapturedEvent(int inputType, int keyCode, int x, int y, long delay) {
        this.inputType = inputType;
        this.keyCode = keyCode;
        this.x = x;
        this.y = y;
        this.delay = delay;
    }

    /**
     * Gets the input type.
     *
     * @return The input type code
     */
    public int getInputType() {
        return inputType;
    }

    /**
     * Gets the key or button code.
     *
     * @return The key/button code
     */
    public int getKeyCode() {
        return keyCode;
    }

    /**
     * Gets the X coordinate of the mouse.
     *
     * @return The X coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Y coordinate of the mouse.
     *
     * @return The Y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the delay since the last recorded action.
     *
     * @return The delay in milliseconds
     */
    public long getDelay() {
        return delay;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
