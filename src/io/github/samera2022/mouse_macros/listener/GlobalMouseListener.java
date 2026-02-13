package io.github.samera2022.mouse_macros.listener;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.action.MouseAction;
import io.github.samera2022.mouse_macros.manager.MacroManager;
import io.github.samera2022.mouse_macros.util.ScreenUtil;

import java.awt.*;

import static io.github.samera2022.mouse_macros.manager.LogManager.log;

public class GlobalMouseListener implements NativeKeyListener, NativeMouseInputListener {
    // 热键自定义
    public static int keyRecord = NativeKeyEvent.VC_F2;
    public static int keyStop = NativeKeyEvent.VC_F3;
    public static int keyPlay = NativeKeyEvent.VC_F4;

    public GlobalMouseListener() {}

    // 全局快捷键监听
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == keyRecord) {
            MacroManager.startRecording();
        } else if (e.getKeyCode() == keyStop) {
            MacroManager.stopRecording();
        } else if (e.getKeyCode() == keyPlay) {
            MacroManager.play();
        }
    }

    @Override public void nativeKeyReleased(NativeKeyEvent e) {}
    @Override public void nativeKeyTyped(NativeKeyEvent e) {}

    // 鼠标事件监听（录制功能实现）
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        if (MacroManager.isRecording()) {
            Point p = ScreenUtil.normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            MacroManager.recordAction(new MouseAction(p.x, p.y, 1, e.getButton(), now - MacroManager.getLastTime()));
            MacroManager.setLastTime(now);
            log(Localizer.get("recording_msg1")+" (" + p.x + "," + p.y + ")");
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        if (MacroManager.isRecording()) {
            Point p = ScreenUtil.normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            long delay = Math.min(now - MacroManager.getLastTime(), 100); // 限制最大delay为100ms
            MacroManager.recordAction(new MouseAction(p.x, p.y, 2, e.getButton(), delay));
            MacroManager.setLastTime(now);
            log(Localizer.get("recording_msg2")+" (" + p.x + "," + p.y + ")");
        }
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        if (MacroManager.isRecording()) {
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
}
