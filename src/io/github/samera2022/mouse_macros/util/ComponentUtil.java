package io.github.samera2022.mouse_macros.util;

import io.github.samera2022.mouse_macros.constant.ColorConsts;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.ui.component.CustomFileChooser;
import io.github.samera2022.mouse_macros.ui.component.CustomScrollBarUI;

import javax.swing.*;
import java.awt.*;

import static io.github.samera2022.mouse_macros.constant.ColorConsts.*;

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

    public static void setMode(Component root, int mode){
        setComponent(root, mode);
    }

    private static void setComponent(Component comp, int mode) {
        Color[] colorScheme = new Color[]{};
        switch (mode) {
            case OtherConsts.DARK_MODE:
                colorScheme = DARK_MODE_SCHEME;
                break;
            case OtherConsts.LIGHT_MODE:
                colorScheme = LIGHT_MODE_SCHEME;
                break;
        }
        if (colorScheme.length == 9)
            setComponent(comp, mode, colorScheme[0], colorScheme[1], colorScheme[2], colorScheme[3], colorScheme[4], colorScheme[5], colorScheme[6], colorScheme[7], colorScheme[8]);
        else {
            System.out.println("Wrong Color Scheme! Apply Default Color Scheme.");
            setComponent(comp, OtherConsts.DARK_MODE);
        }
    }

    // 递归设置风格
    private static void setComponent(Component comp, int mode, Color bg, Color fg, Color pbg, Color pfg, Color bbg, Color bfg, Color lbg, Color lfg, Color caret) {
        if (comp != null)
//            if (comp instanceof Container && (!(comp instanceof JScrollPane)) && (!(comp instanceof CustomFileChooser))) {
//                comp.setBackground(bg);
//                comp.setForeground(fg);
//            } else if (comp instanceof CustomFileChooser) {
//                ((CustomFileChooser) comp).setMode(mode);
//            } else
                if (comp instanceof JScrollPane) {
                comp.setBackground(pbg);
                setComponent(((JScrollPane) comp).getVerticalScrollBar(),mode);
                setComponent(((JScrollPane) comp).getHorizontalScrollBar(),mode);
                for (Component c : ((JScrollPane) comp).getViewport().getComponents()) {
                    setComponent(c, mode, bg, fg, pbg, pfg, bbg, bfg, lbg, lfg, caret);
                }
            } else if (comp instanceof JPanel) {
                comp.setBackground(pbg);
                for (Component c : ((JPanel) comp).getComponents()) {
                    if (c!=null) setComponent(c, mode, bg, fg, pbg, pfg, bbg, bfg, lbg, lfg, caret);
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
            } else if (comp instanceof JComboBox) {
                //已经不便于用上面的Color...来表示了
                switch (mode) {
                    case OtherConsts.DARK_MODE:
                        UIManager.put("ComboBox.disabledBackground", DARK_MODE_DISABLED_BACKGROUND);
                        UIManager.put("ComboBox.disabledForeground", DARK_MODE_DISABLED_FOREGROUND);
                        UIManager.put("ComboBox.background", DARK_MODE_PANEL_BACKGROUND);
                        UIManager.put("ComboBox.foreground", DARK_MODE_PANEL_FOREGROUND);
                        UIManager.put("ComboBox.selectionBackground", DARK_MODE_BACKGROUND);
                        UIManager.put("ComboBox.selectionForeground", DARK_MODE_FOREGROUND);
                        UIManager.put("ComboBox.buttonBackground", DARK_MODE_BUTTON_BACKGROUND);
                        UIManager.put("ComboBox.buttonShadow", DARK_MODE_BUTTON_FOREGROUND);
                        break;
                    case OtherConsts.LIGHT_MODE:
                        UIManager.put("ComboBox.disabledBackground", LIGHT_MODE_DISABLED_BACKGROUND);
                        UIManager.put("ComboBox.disabledForeground", LIGHT_MODE_DISABLED_FOREGROUND);
                        UIManager.put("ComboBox.background", LIGHT_MODE_PANEL_BACKGROUND);
                        UIManager.put("ComboBox.foreground", LIGHT_MODE_PANEL_FOREGROUND);
                        UIManager.put("ComboBox.selectionBackground", LIGHT_MODE_BACKGROUND);
                        UIManager.put("ComboBox.selectionForeground", LIGHT_MODE_FOREGROUND);
                        UIManager.put("ComboBox.buttonBackground", LIGHT_MODE_BUTTON_BACKGROUND);
                        UIManager.put("ComboBox.buttonShadow", LIGHT_MODE_BUTTON_FOREGROUND);
                        break;
                }
                SwingUtilities.updateComponentTreeUI(comp);
            } else if (comp instanceof JSeparator) {
                comp.setBackground(pbg);
            } else if (comp instanceof JScrollBar) {
                ((JScrollBar) comp).setUI(new CustomScrollBarUI(mode));
                SwingUtilities.invokeLater(comp::repaint);
            } else if (comp instanceof JTable) {
                // 文件选择器内部的表格
                comp.setBackground(bg);
                comp.setForeground(fg);
            }

    }

    public static void setCorrectSize(Component c, int x, int y){
        c.setSize((int)(x/SystemUtil.getScale()[0]), (int)(y/SystemUtil.getScale()[1]));
    }

    public static void setContainerMode(Container c, int mode){
        switch (mode){
            case OtherConsts.DARK_MODE:
                c.setBackground(DARK_MODE_BACKGROUND);
                c.setForeground(DARK_MODE_FOREGROUND);
                break;
            case OtherConsts.LIGHT_MODE:
                c.setBackground(LIGHT_MODE_BACKGROUND);
                c.setForeground(LIGHT_MODE_FOREGROUND);
                break;
        }
    }
}
