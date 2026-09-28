package io.github.samera2022.mousemacros.api.script;

/**
 * Provides system information to scripts.
 */
public interface ISystemInfo {
    /**
     * Gets the DPI scale factors for the primary screen.
     *
     * @return Array with [scaleX, scaleY]
     */
    double[] getScale();

    /**
     * Checks if the system is in dark mode.
     * Currently only supports Windows 10+, returns false on other platforms.
     *
     * @return true if dark mode is enabled, false otherwise
     */
    boolean isSystemDarkMode();

    /**
     * Gets the system language code.
     *
     * @return Language code (e.g., "en_us", "zh_cn")
     */
    String getSystemLanguage();

    /**
     * Gets the operating system name.
     *
     * @return OS name (e.g., "Windows 10", "Linux")
     */
    String getOSName();

    /**
     * Gets the Java version.
     *
     * @return Java version string (e.g., "11.0.12")
     */
    String getJavaVersion();
}

