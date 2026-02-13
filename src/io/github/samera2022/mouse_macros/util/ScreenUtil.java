package io.github.samera2022.mouse_macros.util;

import java.awt.*;

public class ScreenUtil {
    // 将归一化坐标还原为主屏幕坐标（适配Robot，自动处理DPI缩放）
    public static Point denormalizeFromVirtualOrigin(int x, int y) {
        Point virtualOrigin = getVirtualOrigin();
        // 全局坐标
        int globalX = x + virtualOrigin.x;
        int globalY = y + virtualOrigin.y;
        // 获取主屏幕左上角和缩放因子
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        Rectangle primaryBounds = gc.getBounds();
        double scaleX = gc.getDefaultTransform().getScaleX();
        double scaleY = gc.getDefaultTransform().getScaleY();
        // 转为主屏幕坐标并除以缩放因子
        int robotX = (int) Math.round((globalX - primaryBounds.x) / scaleX);
        int robotY = (int) Math.round((globalY - primaryBounds.y) / scaleY);
        return new Point(robotX, robotY);
    }

    // 获取所有屏幕的最左上角坐标（多屏归一化原点）
    private static Point getVirtualOrigin() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = ge.getScreenDevices();
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        for (GraphicsDevice device : devices) {
            Rectangle bounds = device.getDefaultConfiguration().getBounds();
            if (bounds.x < minX) minX = bounds.x;
            if (bounds.y < minY) minY = bounds.y;
        }
        return new Point(minX, minY);
    }

    // 将全局屏幕坐标归一化到虚拟原点
    public static Point normalizeToVirtualOrigin(int x, int y) {
        Point origin = getVirtualOrigin();
        return new Point(x - origin.x, y - origin.y);
    }
}
