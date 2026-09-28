package io.github.samera2022.mousemacros.app.util;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import io.github.samera2022.mousemacros.app.Localizer;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class to detect and warn about unsupported or problematic hotkeys.
 * Helps users understand why certain key combinations may not record properly.
 */
public class HotkeyValidator {
    
    // Keys that CANNOT be captured by Java applications (hardware-level processing)
    private static final Set<Integer> IMPOSSIBLE_KEYS = new HashSet<>();
    
    // Keys that are problematic but may work on some systems
    private static final Set<Integer> PROBLEMATIC_KEYS = new HashSet<>();
    
    static {
        // NOTE: Fn key itself CANNOT be detected by any Java library
        // Fn is processed at keyboard firmware level and never reaches the OS
        
        // Media keys (hardware-level, often impossible to capture)
        IMPOSSIBLE_KEYS.add(NativeKeyEvent.VC_MEDIA_PLAY);
        IMPOSSIBLE_KEYS.add(NativeKeyEvent.VC_MEDIA_STOP);
        IMPOSSIBLE_KEYS.add(NativeKeyEvent.VC_MEDIA_PREVIOUS);
        IMPOSSIBLE_KEYS.add(NativeKeyEvent.VC_MEDIA_NEXT);
        IMPOSSIBLE_KEYS.add(NativeKeyEvent.VC_VOLUME_UP);
        IMPOSSIBLE_KEYS.add(NativeKeyEvent.VC_VOLUME_DOWN);
        IMPOSSIBLE_KEYS.add(NativeKeyEvent.VC_VOLUME_MUTE);
        
        // Power keys (system-level, cannot be captured)
        IMPOSSIBLE_KEYS.add(NativeKeyEvent.VC_POWER);
        IMPOSSIBLE_KEYS.add(NativeKeyEvent.VC_SLEEP);
        
        // Browser/App control keys (hardware-dependent, may work on desktop keyboards)
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_BACK);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_FORWARD);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_REFRESH);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_STOP);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_SEARCH);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_FAVORITES);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_HOME);
        
        // App launch keys (may work on some keyboards)
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_APP_MAIL);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_APP_CALCULATOR);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_APP_MUSIC);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_APP_PICTURES);
        
        // F13-F24 often require Fn on laptops, but can work on extended keyboards
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_F13);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_F14);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_F15);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_F16);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_F17);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_F18);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_F19);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_F20);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_F21);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_F22);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_F23);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_F24);
    }
    
    /**
     * Check if a key is impossible to capture (hardware-level).
     * @param keyCode JNativeHook key code
     * @return true if key CANNOT be captured
     */
    public static boolean isImpossibleKey(int keyCode) {
        return IMPOSSIBLE_KEYS.contains(keyCode);
    }
    
    /**
     * Check if a key is known to have recording issues.
     * @param keyCode JNativeHook key code
     * @return true if key may not record properly
     */
    public static boolean isProblematicKey(int keyCode) {
        return PROBLEMATIC_KEYS.contains(keyCode);
    }
    
    /**
     * Get warning message for a problematic key.
     * @param keyCode JNativeHook key code
     * @return localized warning message, or null if key is OK
     */
    public static String getKeyWarning(int keyCode) {
        if (isImpossibleKey(keyCode)) {
            return Localizer.get("hotkey.warning.impossible_key");
        } else if (isProblematicKey(keyCode)) {
            return Localizer.get("hotkey.warning.problematic_key");
        }
        return null;
    }
    
    /**
     * Check if a key code is valid (non-zero and recognized).
     * @param keyCode JNativeHook key code
     * @return true if valid
     */
    public static boolean isValidKey(int keyCode) {
        return keyCode != 0 && keyCode != NativeKeyEvent.VC_UNDEFINED;
    }
    
    /**
     * Get a user-friendly explanation for recording failures.
     * @return localized explanation message
     */
    public static String getRecordingFailureHelp() {
        return Localizer.get("hotkey.help.recording_failure");
    }
    
    /**
     * Get list of recommended hotkey types.
     * @return localized recommendations
     */
    public static String getRecommendedKeys() {
        return Localizer.get("hotkey.help.recommended_keys");
    }
}