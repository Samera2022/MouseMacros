package io.github.samera2022.mouse_macros.util;

import io.github.samera2022.mouse_macros.constant.ColorConsts;

import javax.swing.*;
import java.awt.*;

public class ComponentUtil {
    // 自动调整窗体宽度
    public static void adjustFrameWidth(JFrame jf,JButton... btns) {
        int padding = 80; // 额外边距
        // 两行分组
        int row1 = btns[0].getPreferredSize().width + btns[1].getPreferredSize().width + btns[2].getPreferredSize().width + 40;
        int row2 = btns[3].getPreferredSize().width + btns[4].getPreferredSize().width + 20;
        int maxWidth = Math.max(row1, row2) + padding;
        jf.setSize(maxWidth, jf.getHeight());
    }

    // 集中设置主界面和任意面板的暗色风格
    public static void applyDarkMode(Component root, Component parent) {
        setComponent(root, ColorConsts.DARK_MODE_SCHEME);
        if (root instanceof JDialog || root instanceof JFrame) root.setBackground(ColorConsts.DARK_MODE_BACKGROUND);
        UIManager.put("ComboBox.disabledBackground", ColorConsts.DARK_MODE_DISABLED_BACKGROUND);
        UIManager.put("ComboBox.disabledForeground", ColorConsts.DARK_MODE_DISABLED_FOREGROUND);
        SwingUtilities.updateComponentTreeUI(parent);
    }

    // 集中设置主界面和任意面板的亮色风格
    public static void applyLightMode(Component root, Component parent) {
        setComponent(root, ColorConsts.LIGHT_MODE_SCHEME);
        if (root instanceof JDialog || root instanceof JFrame) root.setBackground(ColorConsts.LIGHT_MODE_BACKGROUND);
        UIManager.put("ComboBox.disabledBackground", ColorConsts.LIGHT_MODE_DISABLED_BACKGROUND);
        UIManager.put("ComboBox.disabledForeground", ColorConsts.LIGHT_MODE_DISABLED_FOREGROUND);
        SwingUtilities.updateComponentTreeUI(parent);
    }

    private static void setComponent(Component comp, Color[] colorScheme) {
        if (colorScheme.length == 7)
            setComponent(comp, colorScheme[0], colorScheme[1], colorScheme[2], colorScheme[3], colorScheme[4], colorScheme[5], colorScheme[6]);
        else {
            System.out.println("Wrong Color Scheme! Apply Default Color Scheme.");
            setComponent(comp, ColorConsts.DARK_MODE_SCHEME);
        }
    }

    // 递归设置风格
    private static void setComponent(Component comp, Color bg, Color fg, Color pbg, Color pfg, Color bbg, Color bfg, Color caret) {
        if (comp != null)
            if (comp instanceof JScrollPane) {
                comp.setBackground(pbg);
                for (Component c : ((JScrollPane) comp).getViewport().getComponents()) {
                    if (c != null) setComponent(c, bg, fg, pbg, pfg, bbg, bfg, caret);
                }
            } else if (comp instanceof JPanel) {
                comp.setBackground(pbg);
                for (Component c : ((JPanel) comp).getComponents()) {
                    if (c!=null) setComponent(c, bg, fg, pbg, pfg, bbg, bfg, caret);
                }
            } else if (comp instanceof JLabel) {
//            comp.setBackground(bg);
                comp.setForeground(fg);
            } else if (comp instanceof JButton) {
                ((JButton) comp).setFocusPainted(false);
                comp.setBackground(bbg);
                comp.setForeground(bfg);
            } else if (comp instanceof JTextField) {
                comp.setBackground(pbg);
                comp.setForeground(pfg);
                ((JTextField) comp).setCaretColor(caret);
            } else if (comp instanceof JTextArea) {
                comp.setBackground(pbg);
                comp.setForeground(pfg);
                ((JTextArea) comp).setCaretColor(caret);
            } else if (comp instanceof JCheckBox) {
                comp.setBackground(pbg);
                comp.setForeground(pfg);
            } else if (comp instanceof JComboBox||comp instanceof JScrollBar) {
                comp.setBackground(pbg);
                comp.setForeground(pfg);
            } else if (comp instanceof JSeparator) {
                comp.setBackground(pbg);
            }
    }

    public static void setCorrectSize(Component c, int x, int y){
        c.setSize((int)(x/SystemUtil.getScale()[0]), (int)(y/SystemUtil.getScale()[1]));
    }
}
