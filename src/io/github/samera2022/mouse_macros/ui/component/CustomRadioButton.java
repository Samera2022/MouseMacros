package io.github.samera2022.mouse_macros.ui.component;

import javax.swing.*;
import java.awt.*;

public class CustomRadioButton extends JRadioButton {
    private static final int outerR = 14;
    private static final int deltaR = 6;
    // 颜色配置，仅用于圆形部分
    private Color enabledBg = Color.WHITE;
    private Color disabledBg = Color.LIGHT_GRAY;
    private Color selectedBg = new Color(0, 120, 215);
    private Color borderColor = Color.GRAY;
    private Color disabledBorderColor = Color.DARK_GRAY;

    public CustomRadioButton(String text) {
        super(text);
        setOpaque(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 保留原生文本渲染
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int size = Math.min(getHeight(), outerR); // 更小的圆形
        int x = 2;
        int y = (getHeight() - size) / 2;
        // 背景
        g2.setColor(isEnabled() ? enabledBg : disabledBg);
        g2.fillOval(x, y, size, size);
        // 边框
        g2.setColor(isEnabled() ? borderColor : disabledBorderColor);
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(x, y, size, size);
        // 选中效果
        if (isSelected()) {
            g2.setColor(isEnabled() ? selectedBg : disabledBorderColor);
            int innerSize = size - deltaR; // 更小的内圆
            g2.fillOval(x + 3, y + 3, innerSize, innerSize);
        }
        g2.dispose();
    }

    // 可选：颜色设置方法，仅影响圆形部分
    public void setEnabledBackground(Color color) { enabledBg = color; repaint(); }
    public void setDisabledBackground(Color color) { disabledBg = color; repaint(); }
    public void setSelectedBackground(Color color) { selectedBg = color; repaint(); }
    public void setBorderColor(Color color) { borderColor = color; repaint(); }
    public void setDisabledBorderColor(Color color) { disabledBorderColor = color; repaint(); }
}
