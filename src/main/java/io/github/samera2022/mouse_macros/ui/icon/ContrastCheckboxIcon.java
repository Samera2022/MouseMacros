package io.github.samera2022.mouse_macros.ui.icon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

// 增强对比度的勾选框图标
public class ContrastCheckboxIcon implements Icon {
    private final int size;
    private final Color checkColor;

    // 启用状态颜色
    private final Color enabledBorderColor = new Color(100, 100, 120);
    private final Color enabledBgColor = new Color(250, 250, 252);
    private final Color enabledHighlightColor = new Color(230, 230, 240);

    // 禁用状态颜色 - 使用更浅的色调
    private final Color disabledBorderColor = new Color(180, 180, 190);
    private final Color disabledBgColor = new Color(245, 245, 247);

    // 阴影颜色
    private final Color shadowColor = new Color(0, 0, 0, 40);

    public ContrastCheckboxIcon(int size, Color checkColor) {
        this.size = size;
        this.checkColor = checkColor;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean enabled = c.isEnabled();
        boolean selected = c instanceof AbstractButton && ((AbstractButton) c).isSelected();

        // 绘制阴影（仅启用状态）
        if (enabled) {
            g2d.setColor(shadowColor);
            g2d.fillRoundRect(x + 1, y + 2, size, size, 6, 6);
        }

        // 绘制背景
        Color bgColor = enabled ? enabledBgColor : disabledBgColor;
        RoundRectangle2D box = new RoundRectangle2D.Float(x, y, size, size, 5, 5);
        g2d.setColor(bgColor);
        g2d.fill(box);

        // 绘制边框（禁用状态使用虚线边框）
        Color borderColor = enabled ? enabledBorderColor : disabledBorderColor;
        g2d.setColor(borderColor);

        if (enabled) {
            // 实线边框
            g2d.draw(box);

            // 添加内高光
            g2d.setColor(enabledHighlightColor);
            g2d.draw(new RoundRectangle2D.Float(x + 0.5f, y + 0.5f, size - 1, size - 1, 4, 4));
        } else {
            // 虚线边框 - 增强禁用状态视觉效果
            Stroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 10.0f,
                    new float[]{3.0f, 3.0f}, 0.0f);
            g2d.setStroke(dashed);
            g2d.draw(box);
            g2d.setStroke(new BasicStroke()); // 恢复默认
        }

        // 绘制选中状态
        if (selected) {
            drawCheckMark(g2d, x, y, enabled);
        }

        g2d.dispose();
    }

    private void drawCheckMark(Graphics2D g2d, int x, int y, boolean enabled) {
        // 禁用状态使用更浅的颜色
        Color markColor = enabled ? checkColor : new Color(170, 170, 180);

        // 创建对勾路径
        Path2D check = new Path2D.Float();
        check.moveTo(x + size * 0.22, y + size * 0.48);
        check.lineTo(x + size * 0.42, y + size * 0.68);
        check.lineTo(x + size * 0.78, y + size * 0.32);

        // 设置线条样式
        g2d.setColor(markColor);
        g2d.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(check);
    }

    @Override
    public int getIconWidth() { return size; }

    @Override
    public int getIconHeight() { return size; }
}