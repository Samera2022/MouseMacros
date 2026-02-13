package io.github.samera2022.mouse_macros.listener;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener;
import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.action.MouseAction;
import io.github.samera2022.mouse_macros.manager.MacroManager;
import io.github.samera2022.mouse_macros.ui.frame.settings.HotkeyDialog;
import io.github.samera2022.mouse_macros.util.OtherUtil;
import io.github.samera2022.mouse_macros.util.ScreenUtil;

import java.awt.*;

import static io.github.samera2022.mouse_macros.manager.LogManager.log;
import static io.github.samera2022.mouse_macros.ui.frame.MainFrame.*;

public class GlobalMouseListener implements NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener {
    // 热键自定义
//    public static int keyRecord = NativeKeyEvent.VC_F2;
//    public static int keyStop = NativeKeyEvent.VC_F3;
//    public static int keyPlay = NativeKeyEvent.VC_F4;

    public GlobalMouseListener() {}

    // 全局快捷键监听
    //限制2/2
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == keyRecord && (!MacroManager.isRecording()) && (!HotkeyDialog.inHotKeyDialog)) {
            MacroManager.startRecording();
        } else if (e.getKeyCode() == keyStop && MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            MacroManager.stopRecording();
        } else if (e.getKeyCode() == keyPlay && (!MacroManager.isRecording()) && (!HotkeyDialog.inHotKeyDialog)) {
            MacroManager.play();
        } else if (e.getKeyCode() == keyAbort && MacroManager.isPlaying() && (!HotkeyDialog.inHotKeyDialog)) {
            MacroManager.abort();
        } else if (MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            // 录制普通键盘按下事件，排除热键
            long now = System.currentTimeMillis();
            int awtKeyCode = toAwtKeyCode(e.getKeyCode());
            MacroManager.recordAction(new MouseAction(0, 0, 10, 0, now - MacroManager.getLastTime(), 0, e.getKeyCode(), awtKeyCode));
            MacroManager.setLastTime(now);
            log(Localizer.get("recording_key_pressed") + OtherUtil.getNativeKeyDisplayText(e.getKeyCode()));
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            long now = System.currentTimeMillis();
            int awtKeyCode = toAwtKeyCode(e.getKeyCode());
            MacroManager.recordAction(new MouseAction(0, 0, 11, 0, now - MacroManager.getLastTime(), 0, e.getKeyCode(), awtKeyCode));
            MacroManager.setLastTime(now);
            log(Localizer.get("recording_key_released") + OtherUtil.getNativeKeyDisplayText(e.getKeyCode()));
        }
    }

    @Override public void nativeKeyTyped(NativeKeyEvent e) {}

    // 鼠标事件监听（录制功能实现）
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        if (MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            Point p = ScreenUtil.normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            int btn = e.getButton();
            // JNativeHook: 1=左键, 2=右键, 3=中键
            int macroBtn = btn == 2 ? 3 : btn == 3 ? 2 : btn; // 转换为AWT标准: 1=左,2=中,3=右
            String btnName = macroBtn == 1 ? Localizer.get("mouse_left") : macroBtn == 2 ? Localizer.get("mouse_middle") : macroBtn == 3 ? Localizer.get("mouse_right") : ("btn"+macroBtn);
            MacroManager.recordAction(new MouseAction(p.x, p.y, 1, macroBtn, now - MacroManager.getLastTime()));
            MacroManager.setLastTime(now);
            log(Localizer.get("recording_mouse_pressed")+" ["+btnName+"] (" + p.x + "," + p.y + ")");
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        if (MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            Point p = ScreenUtil.normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            int btn = e.getButton();
            int macroBtn = btn == 2 ? 3 : btn == 3 ? 2 : btn; // 转换为AWT标准: 1=左,2=中,3=右
            String btnName = macroBtn == 1 ? Localizer.get("mouse_left") : macroBtn == 2 ? Localizer.get("mouse_middle") : macroBtn == 3 ? Localizer.get("mouse_right") : ("btn"+macroBtn);
            long delay = now - MacroManager.getLastTime();
            MacroManager.recordAction(new MouseAction(p.x, p.y, 2, macroBtn, delay));
            MacroManager.setLastTime(now);
            log(Localizer.get("recording_mouse_released")+" ["+btnName+"] (" + p.x + "," + p.y + ")");
        }
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        if (MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            Point p = ScreenUtil.normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            var actions = MacroManager.getActions();
            // 只在距离上次记录超过一定阈值时才记录，防止过多无效点
            if (actions.isEmpty() || p.distance(actions.get(actions.size()-1).x, actions.get(actions.size()-1).y) > 5) {
                long delay = Math.min(now - MacroManager.getLastTime(), 100); // 限制最大delay为100ms
                MacroManager.recordAction(new MouseAction(p.x, p.y, 0, 0, delay));
                MacroManager.setLastTime(now);
            }
        }
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        nativeMouseMoved(e);
    }

    // 鼠标滚轮事件监听（录制功能实现）
    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
        if (MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            Point p = ScreenUtil.normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            int wheelAmount = e.getWheelRotation();
            long delay = now - MacroManager.getLastTime();
            MacroManager.recordAction(new MouseAction(p.x, p.y, 3, 0, delay, wheelAmount));
            MacroManager.setLastTime(now);
            log(Localizer.get("recording_scroll_msg1") + " ("+ Localizer.get("recording_scroll_msg2") + wheelAmount + ")");
        }
    }

    private int toAwtKeyCode(int nativeKeyCode) {
        // JNativeHook -> AWT KeyEvent 映射（常用键，需补充完整）
        switch (nativeKeyCode) {
            case NativeKeyEvent.VC_A: return java.awt.event.KeyEvent.VK_A;
            case NativeKeyEvent.VC_B: return java.awt.event.KeyEvent.VK_B;
            case NativeKeyEvent.VC_C: return java.awt.event.KeyEvent.VK_C;
            case NativeKeyEvent.VC_D: return java.awt.event.KeyEvent.VK_D;
            case NativeKeyEvent.VC_E: return java.awt.event.KeyEvent.VK_E;
            case NativeKeyEvent.VC_F: return java.awt.event.KeyEvent.VK_F;
            case NativeKeyEvent.VC_G: return java.awt.event.KeyEvent.VK_G;
            case NativeKeyEvent.VC_H: return java.awt.event.KeyEvent.VK_H;
            case NativeKeyEvent.VC_I: return java.awt.event.KeyEvent.VK_I;
            case NativeKeyEvent.VC_J: return java.awt.event.KeyEvent.VK_J;
            case NativeKeyEvent.VC_K: return java.awt.event.KeyEvent.VK_K;
            case NativeKeyEvent.VC_L: return java.awt.event.KeyEvent.VK_L;
            case NativeKeyEvent.VC_M: return java.awt.event.KeyEvent.VK_M;
            case NativeKeyEvent.VC_N: return java.awt.event.KeyEvent.VK_N;
            case NativeKeyEvent.VC_O: return java.awt.event.KeyEvent.VK_O;
            case NativeKeyEvent.VC_P: return java.awt.event.KeyEvent.VK_P;
            case NativeKeyEvent.VC_Q: return java.awt.event.KeyEvent.VK_Q;
            case NativeKeyEvent.VC_R: return java.awt.event.KeyEvent.VK_R;
            case NativeKeyEvent.VC_S: return java.awt.event.KeyEvent.VK_S;
            case NativeKeyEvent.VC_T: return java.awt.event.KeyEvent.VK_T;
            case NativeKeyEvent.VC_U: return java.awt.event.KeyEvent.VK_U;
            case NativeKeyEvent.VC_V: return java.awt.event.KeyEvent.VK_V;
            case NativeKeyEvent.VC_W: return java.awt.event.KeyEvent.VK_W;
            case NativeKeyEvent.VC_X: return java.awt.event.KeyEvent.VK_X;
            case NativeKeyEvent.VC_Y: return java.awt.event.KeyEvent.VK_Y;
            case NativeKeyEvent.VC_Z: return java.awt.event.KeyEvent.VK_Z;
            case NativeKeyEvent.VC_0: return java.awt.event.KeyEvent.VK_0;
            case NativeKeyEvent.VC_1: return java.awt.event.KeyEvent.VK_1;
            case NativeKeyEvent.VC_2: return java.awt.event.KeyEvent.VK_2;
            case NativeKeyEvent.VC_3: return java.awt.event.KeyEvent.VK_3;
            case NativeKeyEvent.VC_4: return java.awt.event.KeyEvent.VK_4;
            case NativeKeyEvent.VC_5: return java.awt.event.KeyEvent.VK_5;
            case NativeKeyEvent.VC_6: return java.awt.event.KeyEvent.VK_6;
            case NativeKeyEvent.VC_7: return java.awt.event.KeyEvent.VK_7;
            case NativeKeyEvent.VC_8: return java.awt.event.KeyEvent.VK_8;
            case NativeKeyEvent.VC_9: return java.awt.event.KeyEvent.VK_9;
            case NativeKeyEvent.VC_ENTER: return java.awt.event.KeyEvent.VK_ENTER;
            case NativeKeyEvent.VC_SPACE: return java.awt.event.KeyEvent.VK_SPACE;
            case NativeKeyEvent.VC_TAB: return java.awt.event.KeyEvent.VK_TAB;
            case NativeKeyEvent.VC_ESCAPE: return java.awt.event.KeyEvent.VK_ESCAPE;
            case NativeKeyEvent.VC_BACKSPACE: return java.awt.event.KeyEvent.VK_BACK_SPACE;
            case NativeKeyEvent.VC_SHIFT: return java.awt.event.KeyEvent.VK_SHIFT;
            case NativeKeyEvent.VC_CONTROL: return java.awt.event.KeyEvent.VK_CONTROL;
            case NativeKeyEvent.VC_ALT: return java.awt.event.KeyEvent.VK_ALT;
            case NativeKeyEvent.VC_META: return java.awt.event.KeyEvent.VK_META;
            case NativeKeyEvent.VC_CAPS_LOCK: return java.awt.event.KeyEvent.VK_CAPS_LOCK;
            case NativeKeyEvent.VC_F1: return java.awt.event.KeyEvent.VK_F1;
            case NativeKeyEvent.VC_F2: return java.awt.event.KeyEvent.VK_F2;
            case NativeKeyEvent.VC_F3: return java.awt.event.KeyEvent.VK_F3;
            case NativeKeyEvent.VC_F4: return java.awt.event.KeyEvent.VK_F4;
            case NativeKeyEvent.VC_F5: return java.awt.event.KeyEvent.VK_F5;
            case NativeKeyEvent.VC_F6: return java.awt.event.KeyEvent.VK_F6;
            case NativeKeyEvent.VC_F7: return java.awt.event.KeyEvent.VK_F7;
            case NativeKeyEvent.VC_F8: return java.awt.event.KeyEvent.VK_F8;
            case NativeKeyEvent.VC_F9: return java.awt.event.KeyEvent.VK_F9;
            case NativeKeyEvent.VC_F10: return java.awt.event.KeyEvent.VK_F10;
            case NativeKeyEvent.VC_F11: return java.awt.event.KeyEvent.VK_F11;
            case NativeKeyEvent.VC_F12: return java.awt.event.KeyEvent.VK_F12;
            // ...可继续补充...
            default: return nativeKeyCode;
        }
    }
}
