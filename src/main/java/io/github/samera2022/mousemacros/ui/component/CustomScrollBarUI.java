package io.github.samera2022.mousemacros.ui.component;

import io.github.samera2022.mousemacros.constant.OtherConsts;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

import static io.github.samera2022.mousemacros.constant.ColorConsts.*;

//需要重写ScrollBar上箭头的颜色，因而不能用UIManager代替
public class CustomScrollBarUI extends BasicScrollBarUI {
    private int mode;

    private Color track;
    private Color thumb;
    private Color arrowBg;
    private Color arrow;

    public CustomScrollBarUI(int mode){
        this.mode = mode;
        switch (mode){
            case OtherConsts.DARK_MODE:
                track = DARK_MODE_SCROLLBAR_BACKGROUND;
                thumb = DARK_MODE_SCROLLBAR_FOREGROUND;
                arrowBg = DARK_MODE_SCROLLBAR_BACKGROUND;
                arrow = DARK_MODE_SCROLLBAR_FOREGROUND;
                break;
            case OtherConsts.LIGHT_MODE:
                track = LIGHT_MODE_SCROLLBAR_BACKGROUND;
                thumb = LIGHT_MODE_SCROLLBAR_FOREGROUND;
                arrowBg = LIGHT_MODE_SCROLLBAR_BACKGROUND;
                arrow = LIGHT_MODE_SCROLLBAR_FOREGROUND;
                break;
        }
    }
    public int getMode() {return mode;}
    public CustomScrollBarUI(Color bg, Color fg){
        this.track = bg;
        this.thumb = fg;
        this.arrowBg = bg;
        this.arrow = fg;
    }
    public CustomScrollBarUI(Color track, Color thumb, Color arrowBg, Color arrow) {
        this.track = track;
        this.thumb = thumb;
        this.arrowBg = arrowBg;
        this.arrow = arrow;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    // 设置轨道颜色为灰色
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(track);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    // 设置滑块颜色为黑色
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        g.setColor(thumb);
        g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
    }

    // 自定义箭头按钮（灰色背景）
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createArrowButton(orientation);
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createArrowButton(orientation);
    }

    private JButton createArrowButton(int orientation) {
        JButton button = new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(16, 16);
            }
        };

        button.setBackground(arrowBg);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true); // 确保背景色可见

        // 自定义箭头绘制
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(arrow);

                int w = c.getWidth();
                int h = c.getHeight();
                int size = Math.min(w, h) / 2;
                int x = (w - size) / 2;
                int y = (h - size) / 2;

                // 根据方向绘制箭头
                if (orientation == SwingConstants.NORTH || orientation == SwingConstants.WEST) {
                    int[] xPoints = {x, x + size, x + size / 2};
                    int[] yPoints = {y + size, y + size, y};
                    g2.fillPolygon(xPoints, yPoints, 3);
                } else {
                    int[] xPoints = {x, x + size, x + size / 2};
                    int[] yPoints = {y, y, y + size};
                    g2.fillPolygon(xPoints, yPoints, 3);
                }
                g2.dispose();
            }
        });
        return button;
    }
    public void setTrack(Color track) {this.track = track;}
    public void setThumb(Color thumb) {this.thumb = thumb;}
    public void setArrowBg(Color arrowBg) {this.arrowBg = arrowBg;}
    public void setArrow(Color arrow) {this.arrow = arrow;}
}