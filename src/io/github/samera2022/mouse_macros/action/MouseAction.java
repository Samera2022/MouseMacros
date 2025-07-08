package io.github.samera2022.mouse_macros.action;

import io.github.samera2022.mouse_macros.util.ScreenUtil;

import java.awt.*;

public class MouseAction {
    public int x;
    public int y;
    public int type;
    public int button;
    public long delay;
    public MouseAction(int x, int y, int type, int button, long delay) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.button = button;
        this.delay = delay;
    }
    public void perform() {
        try {
            if (robotInstance == null) {
                robotInstance = new Robot();
            }
            Robot robot = robotInstance;
            // 直接用虚拟原点全局坐标
            Point global = ScreenUtil.denormalizeFromVirtualOrigin(x, y);
            robot.mouseMove(global.x, global.y);
            switch (type) {
                case 1: // press
                    robot.mousePress(getAWTButtonMask(button));
                    break;
                case 2: // release
                    robot.mouseRelease(getAWTButtonMask(button));
                    break;
            }
        } catch (Exception e) {
            // 忽略异常
        }
    }
    // 共享Robot实例，避免频繁创建
    private static Robot robotInstance = null;
    private int getAWTButtonMask(int btn) {
        switch (btn) {
            case 1: return java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
            case 2: return java.awt.event.InputEvent.BUTTON2_DOWN_MASK;
            case 3: return java.awt.event.InputEvent.BUTTON3_DOWN_MASK;

            default: return java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
        }
    }

}
