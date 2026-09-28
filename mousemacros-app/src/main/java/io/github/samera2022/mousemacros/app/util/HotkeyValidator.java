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
    
    // Keys that are known to have issues with JNativeHook
    private static final Set<Integer> PROBLEMATIC_KEYS = new HashSet<>();
    
    static {
        // Function keys on some laptops (often require Fn modifier)
        // These may not be captured correctly on all systems
        
        // Media keys (often hardware-level, may not generate events)
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_MEDIA_PLAY);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_MEDIA_STOP);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_MEDIA_PREVIOUS);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_MEDIA_NEXT);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_VOLUME_UP);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_VOLUME_DOWN);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_VOLUME_MUTE);
        
        // Power keys (usually system-level)
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_POWER);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_SLEEP);
        
        // Browser/App control keys (hardware-dependent)
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_BACK);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_FORWARD);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_REFRESH);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_STOP);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_SEARCH);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_FAVORITES);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_BROWSER_HOME);
        
        // App launch keys
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_APP_MAIL);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_APP_CALCULATOR);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_APP_MUSIC);
        PROBLEMATIC_KEYS.add(NativeKeyEvent.VC_APP_PICTURES);
    }
    
    // Keys that require special modifier keys (Fn) on laptops
    private static final Set<Integer> FN_DEPENDENT_KEYS = new HashSet<>();
    
    static {
        // F13-F24 (often Fn + F1-F12 on laptops)
        FN_DEPENDENT_KEYS.add(NativeKeyEvent.VC_F13);
        FN_DEPENDENT_KEYS.add(NativeKeyEvent.VC_F14);
        FN_DEPENDENT_KEYS.add(NativeKeyEvent.VC_F15);
        FN_DEPENDENT_KEYS.add(NativeKeyEvent.VC_F16);
        FN_DEPENDENT_KEYS.add(NativeKeyEvent.VC_F17);
        FN_DEPENDENT_KEYS.add(NativeKeyEvent.VC_F18);
        FN_DEPENDENT_KEYS.add(NativeKeyEvent.VC_F19);
        FN_DEPENDENT_KEYS.add(NativeKeyEvent.VC_F20);
        FN_DEPENDENT_KEYS.add(NativeKeyEvent.VC_F21);
        FN_DEPENDENT_KEYS.add(NativeKeyEvent.VC_F22);
        FN_DEPENDENT_KEYS.add(NativeKeyEvent.VC_F23);
        FN_DEPENDENT_KEYS.add(NativeKeyEvent.VC_F24);
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
     * Check if a key requires Fn modifier on laptops.
     * @param keyCode JNativeHook key code
     * @return true if key typically requires Fn key
     */
    public static boolean requiresFnKey(int keyCode) {
        return FN_DEPENDENT_KEYS.contains(keyCode);
    }
    
    /**
     * Get warning message for a problematic key.
     * @param keyCode JNativeHook key code
     * @return localized warning message, or null if key is OK
     */
    public static String getKeyWarning(int keyCode) {
        if (isProblematicKey(keyCode)) {
            return Localizer.get("hotkey.warning.media_key");
        } else if (requiresFnKey(keyCode)) {
            return Localizer.get("hotkey.warning.fn_key");
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