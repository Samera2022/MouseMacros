package io.github.samera2022.mouse_macros.util;

import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.manager.CacheManager;
import io.github.samera2022.mouse_macros.manager.ConfigManager;
import io.github.samera2022.mouse_macros.ui.component.CustomScrollBarUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import static io.github.samera2022.mouse_macros.constant.ColorConsts.*;

public class ComponentUtil {
    // 这里加入hAdjust的主要原因是很多窗体有额外加进去的Box占位符，导致行与行之间的距离被直接忽略了。因此需要手动加调整h的修正量
    // 得到刚好撑满的3:2的比例
    private static int[] getProperSize(int hAdjust, JComponent[]... comps2) {
        int width_max = 0;
        int height_max = 0;
        for (int i = 1; i <= comps2.length; i++) {
            JComponent[] comps = comps2[i-1];
            int width_len = 0;
            int height_len = 0;
            for (JComponent comp : comps) {
                width_len += comp.getPreferredSize().width;
                height_len += comp.getPreferredSize().height;
            }
            width_max = Math.max(width_max, width_len);
            height_max = Math.max(height_max, height_len);
        }
        int finalWidth = width_max+80+20;
        int finalHeight = height_max+hAdjust+20;
        return fitSize(finalWidth, finalHeight);
    }


    private static int[] fitSize(int width, int height) {
        int targetH = height;
        int targetW = (int) Math.ceil(height * 3.0 / 2.0);
        if (targetW < width) {
            targetW = width;
            targetH = (int) Math.ceil(width * 2.0 / 3.0);
        }
        return new int[]{targetW, targetH};
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
            } else if (comp instanceof JButton || comp instanceof JRadioButton) {
                ((AbstractButton) comp).setFocusPainted(false);
                comp.setBackground(bbg);
                comp.setForeground(bfg);
            } else if (comp instanceof JTextField) {
                    //MetalLookAndFeel没有关于disabledBackground或者类似的属性……
                switch (mode) {
                    case OtherConsts.DARK_MODE:
                        UIManager.put("TextField.background", DARK_MODE_PANEL_BACKGROUND);
                        UIManager.put("TextField.foreground", DARK_MODE_PANEL_FOREGROUND);
                        UIManager.put("TextField.inactiveForeground", DARK_MODE_DISABLED_FOREGROUND);
                        UIManager.put("TextField.inactiveBackground", DARK_MODE_DISABLED_BACKGROUND);
                        break;
                    case OtherConsts.LIGHT_MODE:
                        UIManager.put("TextField.background", LIGHT_MODE_PANEL_BACKGROUND);
                        UIManager.put("TextField.foreground", LIGHT_MODE_PANEL_FOREGROUND);
                        UIManager.put("TextField.inactiveForeground", LIGHT_MODE_DISABLED_FOREGROUND);
                        UIManager.put("TextField.inactiveBackground", LIGHT_MODE_DISABLED_BACKGROUND);
                        break;
                }
                SwingUtilities.updateComponentTreeUI(comp);
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

    private static int[] parseWindowSize(String sizeStr) {
        if (sizeStr == null) return null;
        String[] arr = null;
        if (sizeStr.matches("\\d+,\\d+")) {
            arr = sizeStr.split(",");
        } else if (sizeStr.matches("\\d+\\*\\d+")) {
            arr = sizeStr.split("\\*");
        }
        if (arr != null && arr.length == 2) {
            try {
                int w = Integer.parseInt(arr[0]);
                int h = Integer.parseInt(arr[1]);
                return new int[]{w, h};
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static void adjustFrameWithCache(Window window, int hAdjust, JComponent[]... comps ){
        String rawSizeString = CacheManager.cache.windowSizeMap.get(window.getName());
        int[] properSize = getProperSize(hAdjust, comps);
        if (rawSizeString==null) {
            int[] fitSize = fitSize(properSize[0], properSize[1]);
            window.setSize(fitSize[0], fitSize[1]);
        }
        else {
            int[] rawSize = parseWindowSize(rawSizeString);
            switch (ConfigManager.config.readjustFrameMode) {
                case ConfigManager.RFN_MIXED:
                    int[] fitSize = fitSize(Math.max(properSize[0], rawSize[0]), Math.max(properSize[1], rawSize[1]));
                    window.setSize(fitSize[0], fitSize[1]);
                    break;
                case ConfigManager.RFM_STANDARDIZED:
                    window.setSize(properSize[0],properSize[1]);
                    break;
                case ConfigManager.RFM_MEMORIZED:
                    window.setSize(rawSize[0], rawSize[1]);
                    break;
            }
        }
    }
}
