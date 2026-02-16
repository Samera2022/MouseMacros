package io.github.samera2022.mousemacros.ui.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.ItemEvent;

//一个用于自定义颜色的ComboBox类，暂用UIManager代替
public class CustomComboBox<E> extends JComboBox<E> {

    // 颜色配置
    private Color enabledBg = Color.WHITE;
    private Color enabledFg = Color.BLACK;
    private Color disabledBg = Color.LIGHT_GRAY;
    private Color disabledFg = Color.DARK_GRAY;
    private Color enabledSelectionBg = new Color(0, 120, 215);
    private Color enabledSelectionFg = Color.WHITE;
    private Color disabledSelectionBg = new Color(180, 180, 180);
    private Color disabledSelectionFg = Color.DARK_GRAY;
    private Color buttonColor = Color.GRAY;
    private Color borderColor = Color.GRAY;

    public CustomComboBox(E[] items) {
        super(items);
        init();
    }

    private void init() {
        // 1. 设置自定义渲染器
        setRenderer(new CustomComboBoxRenderer());

        // 2. 设置自定义UI（包含按钮和边框）
        setUI(new ColorComboBoxUI());

        // 3. 添加状态监听器
        addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                repaint(); // 选择变化时重绘
            }
        });

        // 初始状态同步
        updateColors();
    }

    // 更新所有组件颜色
    private void updateColors() {
        setBackground(isEnabled() ? enabledBg : disabledBg);
        setForeground(isEnabled() ? enabledFg : disabledFg);
        repaint();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateColors();
    }

    // ========== 颜色设置方法 ==========
    public void setEnabledBackground(Color color) { enabledBg = color; updateColors(); }
    public void setEnabledForeground(Color color) { enabledFg = color; updateColors(); }
    public void setDisabledBackground(Color color) { disabledBg = color; updateColors(); }
    public void setDisabledForeground(Color color) { disabledFg = color; updateColors(); }
    public void setSelectionBackground(Color color) { enabledSelectionBg = color; }
    public void setSelectionForeground(Color color) { enabledSelectionFg = color; }
    public void setDisabledSelectionBackground(Color color) { disabledSelectionBg = color; }
    public void setDisabledSelectionForeground(Color color) { disabledSelectionFg = color; }
    public void setButtonColor(Color color) { buttonColor = color; repaint(); }
    public void setBorderColor(Color color) { borderColor = color; repaint(); }

    // ========== 自定义渲染器 ==========
    private class CustomComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (!isEnabled()) {
                // 禁用状态
                setBackground(index == getSelectedIndex() ?
                        disabledSelectionBg : disabledBg);
                setForeground(disabledSelectionFg);
            } else {
                // 启用状态
                if (isSelected) {
                    setBackground(enabledSelectionBg);
                    setForeground(enabledSelectionFg);
                } else {
                    setBackground(enabledBg);
                    setForeground(enabledFg);
                }
            }
            return this;
        }
    }

    // ========== 自定义UI (控制按钮/边框) ==========
    private class ColorComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            return new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // 绘制三角形箭头
                    int x = getWidth() / 2 - 4;
                    int y = getHeight() / 2 - 2;

                    int[] xPoints = {x, x + 8, x + 4};
                    int[] yPoints = {y, y, y + 6};

                    g2.setColor(isEnabled() ? buttonColor : disabledFg);
                    g2.fillPolygon(xPoints, yPoints, 3);
                    g2.dispose();
                }
            };
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            // 覆盖绘制逻辑
        }

        @Override
        protected ComboPopup createPopup() {
            return new BasicComboPopup(comboBox) {
                @Override
                protected void paintBorder(Graphics g) {
                    g.setColor(borderColor);
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                }
            };
        }
    }
}