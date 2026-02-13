package io.github.samera2022.mousemacros.app.util;

import io.github.samera2022.mousemacros.app.manager.CacheManager;
import io.github.samera2022.mousemacros.app.config.ConfigManager;


import javax.swing.*;
import java.awt.*;

import static io.github.samera2022.mousemacros.app.constant.ColorConsts.*;

public class ComponentUtil {

    private static int[] getProperSize(int hAdjust, JComponent[]... comps2) {
        int width_max = 0;
        int height_max = 0;
        for (int i = 1; i <= comps2.length; i++) {
            JComponent[] comps = comps2[i - 1];
            int width_len = 0;
            int height_len = 0;
            for (JComponent comp : comps) {
                width_len += comp.getPreferredSize().width;
                height_len += comp.getPreferredSize().height;
            }
            width_max = Math.max(width_max, width_len);
            height_max = Math.max(height_max, height_len);
        }
        int finalWidth = width_max + 80 + 20;
        int finalHeight = height_max + hAdjust + 20;
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

    public static void adjustFrameWithCache(Window window, int hAdjust, JComponent[]... comps) {
        String rawSizeString = CacheManager.cache.windowSizeMap.get(window.getName());
        int[] properSize = getProperSize(hAdjust, comps);
        if (rawSizeString == null) {
            int[] fitSize = fitSize(properSize[0], properSize[1]);
            window.setSize(fitSize[0], fitSize[1]);
        } else {
            int[] cacheSize = parseWindowSize(rawSizeString);
            switch (ConfigManager.getInt("readjust_frame_mode")) {
                case ConfigManager.RFM_MIXED:
                    int[] fitSize = fitSize(Math.max(properSize[0], cacheSize[0]), Math.max(properSize[1], cacheSize[1]));
                    window.setSize(fitSize[0], fitSize[1]);
                    break;
                case ConfigManager.RFM_STANDARDIZED:
                    window.setSize(properSize[0], properSize[1]);
                    break;
                case ConfigManager.RFM_MEMORIZED:
                    window.setSize(cacheSize[0], cacheSize[1]);
                    break;
            }
        }
    }
}
