package io.github.samera2022.mousemacros.api.action;

/**
 * Interface representing a mouse action that can be recorded and played back.
 * 
 * @since 2.0.0
 */
public interface IMouseAction {
    /**
     * Gets the type of this mouse action.
     * 
     * @return The action type (e.g., "PRESS", "RELEASE", "MOVE", "WHEEL")
     */
    String getType();
    
    /**
     * Gets the X coordinate of this action.
     * 
     * @return The X coordinate
     */
    int getX();
    
    /**
     * Gets the Y coordinate of this action.
     * 
     * @return The Y coordinate
     */
    int getY();
    
    /**
     * Gets the delay before this action in milliseconds.
     * 
     * @return The delay in milliseconds
     */
    long getDelay();
    
    /**
     * Sets the delay before this action.
     * 
     * @param delay The delay in milliseconds
     */
    void setDelay(long delay);
    
    /**
     * Performs the action.
     */
    void perform();
}