package io.github.samera2022.mouse_macros.util;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class OtherUtil {
    // 工具方法：显示JNativeHook的keyText
    public static String getNativeKeyDisplayText(int keyCode) {
        String keyText = NativeKeyEvent.getKeyText(keyCode);
        if (keyText == null || keyText.trim().isEmpty() || keyText.startsWith("Unknown")) {
            keyText = "0x" + Integer.toHexString(keyCode).toUpperCase();
        }
        return keyText;
    }
}
