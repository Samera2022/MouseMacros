package io.github.samera2022.mousemacros.macro;

import java.awt.*;

public class MouseAction {
    public int x;
    public int y;
    public int type;
    public int button;
    public long delay;
    public int wheelAmount = 0; // 滚轮事件专用
    public int keyCode = 0; // 键盘事件专用
    public int awtKeyCode = 0; // AWT键码，回放用
    public MouseAction(int x, int y, int type, int button, long delay) {
        this(x, y, type, button, delay, 0, 0, 0);
    }
    public MouseAction(int x, int y, int type, int button, long delay, int wheelAmount) {
        this(x, y, type, button, delay, wheelAmount, 0, 0);
    }
    public MouseAction(int x, int y, int type, int button, long delay, int wheelAmount, int keyCode) {
        this(x, y, type, button, delay, wheelAmount, keyCode, 0);
    }
    public MouseAction(int x, int y, int type, int button, long delay, int wheelAmount, int keyCode, int awtKeyCode) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.button = button;
        this.delay = delay;
        this.wheelAmount = wheelAmount;
        this.keyCode = keyCode;
        this.awtKeyCode = awtKeyCode;
    }
    public void perform() {
        try {
            if (robotInstance == null) {
                robotInstance = new Robot();
            }
            Robot robot = robotInstance;
            Point global = ScreenUtil.denormalizeFromVirtualOrigin(x, y);
            if (type == 3) { // 滚轮事件
                robot.mouseMove(global.x, global.y);
                robot.mouseWheel(wheelAmount);
                return;
            }
            if (type == 10) { // 键盘按下
                robot.keyPress(awtKeyCode > 0 ? awtKeyCode : keyCode);
                return;
            }
            if (type == 11) { // 键盘释放
                robot.keyRelease(awtKeyCode > 0 ? awtKeyCode : keyCode);
                return;
            }
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
        // btn: 1=左键，2=中键，3=右键
        switch (btn) {
            case 1: return java.awt.event.InputEvent.BUTTON1_DOWN_MASK; // 左键
            case 2: return java.awt.event.InputEvent.BUTTON2_DOWN_MASK; // 中键
            case 3: return java.awt.event.InputEvent.BUTTON3_DOWN_MASK; // 右键

            default: return java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
        }
    }

}
