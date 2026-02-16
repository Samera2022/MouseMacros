package io.github.samera2022.mousemacros.ui.icon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

// 实现Icon接口绘制自定义勾选框
public class CheckMarkIcon implements Icon {
    private final int size;
    private final Color checkColor;
    private final Color enabledBorderColor = new Color(120, 120, 120);
    private final Color disabledBorderColor = new Color(180, 180, 180);
    private final Color enabledBgColor = Color.WHITE;
    private final Color disabledBgColor = new Color(245, 245, 245);

    public CheckMarkIcon(int size, Color checkColor) {
        this.size = size;
        this.checkColor = checkColor;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 根据组件状态选择颜色
        boolean enabled = c.isEnabled();
        Color borderColor = enabled ? enabledBorderColor : disabledBorderColor;
        Color bgColor = enabled ? enabledBgColor : disabledBgColor;

        // 绘制圆角矩形背景
        RoundRectangle2D box = new RoundRectangle2D.Float(x + 1, y + 1, size - 2, size - 2, 5, 5);

        // 背景填充
        g2d.setColor(bgColor);
        g2d.fill(box);

        // 边框（启用状态有轻微阴影效果）
        if (enabled) {
            g2d.setColor(new Color(220, 220, 220));
            g2d.drawRoundRect(x, y, size, size, 6, 6);
        }

        g2d.setColor(borderColor);
        g2d.draw(box);

        // 若选中状态，绘制对勾
        if (c instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) c;
            if (button.isSelected()) {
                drawCheckMark(g2d, x, y, enabled);
            }
        }

        g2d.dispose();
    }

    private void drawCheckMark(Graphics2D g2d, int x, int y, boolean enabled) {
        // 根据状态调整颜色（禁用状态使用灰色）
        Color markColor = enabled ? checkColor : new Color(180, 180, 180);

        // 创建渐变效果使对勾更自然
        GradientPaint gp = new GradientPaint(
                x, y, markColor.brighter(),
                x + size, y + size, markColor.darker()
        );

        g2d.setPaint(gp);
        g2d.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // 绘制更自然的对勾路径
        Path2D check = new Path2D.Float();
        check.moveTo(x + size * 0.18, y + size * 0.45);
        check.curveTo(
                x + size * 0.18, y + size * 0.45,
                x + size * 0.35, y + size * 0.65,
                x + size * 0.42, y + size * 0.65
        );
        check.curveTo(
                x + size * 0.42, y + size * 0.65,
                x + size * 0.75, y + size * 0.25,
                x + size * 0.82, y + size * 0.35
        );

        g2d.draw(check);
    }

    @Override
    public int getIconWidth() { return size + 2; }

    @Override
    public int getIconHeight() { return size + 2; }
}