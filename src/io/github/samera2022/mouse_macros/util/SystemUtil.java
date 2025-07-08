package io.github.samera2022.mouse_macros.util;

import java.awt.*;

public class SystemUtil {
    //0代表x，1代表y
    public static double[] getScale(){
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        double scaleX = gc.getDefaultTransform().getScaleX();
        double scaleY = gc.getDefaultTransform().getScaleY();
        return new double[]{scaleX, scaleY};
    }
    // 获取系统语言（如lang文件夹无该语言则返回en_us）
    public static String getSystemLang(String[] availableLangs) {
        String sysLang = System.getProperty("user.language", "en").toLowerCase();
        String sysCountry = System.getProperty("user.country", "US").toLowerCase();
        String full = sysLang + "_" + sysCountry;
        for (String l : availableLangs) {
            if (l.equalsIgnoreCase(full)) return l;
        }
        for (String l : availableLangs) {
            if (l.startsWith(sysLang + "_")) return l;
        }
        for (String l : availableLangs) {
            if (l.equalsIgnoreCase("en_us")) return l;
        }
        return availableLangs.length > 0 ? availableLangs[0] : "en_us";
    }

    // 判断系统是否为深色模式（仅支持Windows 10+，其他平台默认false）
    public static boolean isSystemDarkMode() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            try {
                Process p = Runtime.getRuntime().exec(
                        "reg query HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize /v AppsUseLightTheme");
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("AppsUseLightTheme")) {
                        String[] arr = line.trim().split(" ");
                        String val = arr[arr.length - 1];
                        return "0x0".equals(val); // 0=dark, 1=light
                    }
                }
            } catch (Exception ignored) {}
        }
        // 其他平台可扩展
        return false;
    }
}
