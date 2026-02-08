package io.github.samera2022.mousemacros.app.adapter;

import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.ui.component.CustomToolTipWindow;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class CustomMouseAdapter extends MouseAdapter {
    private CustomToolTipWindow window;
    private final String tipText;
    private final boolean allowLongStr;

    private Timer showTimer;
    private Timer hideTimer;
    private static final int INITIAL_DELAY = 200; // 鼠标进入后延迟200毫秒显示提示
    private static final int DISMISS_DELAY = 100; // 鼠标离开后延迟100毫秒隐藏提示

    // 存储mouseEntered事件，以便在计时器触发时获取鼠标位置
    private MouseEvent lastMouseEvent;

    public CustomMouseAdapter(String tipText) {
        this.tipText = tipText;
        this.allowLongStr = ConfigManager.getBoolean("allow_long_str");
        initTimers();
    }
    public CustomMouseAdapter(String tipText, boolean allowLongStr) {
        this.tipText = tipText;
        this.allowLongStr = allowLongStr;
        initTimers();
    }

    private void initTimers() {
        showTimer = new Timer(INITIAL_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 只有当鼠标仍然在组件上且满足显示条件时才显示提示
                if (lastMouseEvent != null && shouldShowTip(lastMouseEvent)) {
                    if (window == null) {
                        window = new CustomToolTipWindow(tipText, allowLongStr);
                    }
                    Point p = lastMouseEvent.getLocationOnScreen();
                    window.setLocation(p.x + 10, p.y + 10);
                    window.setVisible(true);
                }
                lastMouseEvent = null; // 使用后清除事件
            }
        });
        showTimer.setRepeats(false); // 只触发一次

        hideTimer = new Timer(DISMISS_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (window != null) {
                    window.setVisible(false);
                }
            }
        });
        hideTimer.setRepeats(false); // 只触发一次
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        lastMouseEvent = e; // 存储当前鼠标事件
        hideTimer.stop(); // 停止任何待处理的隐藏计时器
        
        // 只有当满足显示条件时，才启动显示计时器
        // 注意：这里不要立即显示窗口，否则延迟就失效了
        if (shouldShowTip(e)) {
            showTimer.restart(); // 启动或重新启动显示计时器
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        showTimer.stop(); // 停止任何待处理的显示计时器
        lastMouseEvent = null; // 清除存储的事件
        
        // 启动隐藏计时器，而不是立即隐藏
        hideTimer.restart(); 
    }

    protected abstract boolean shouldShowTip(MouseEvent e);
}
