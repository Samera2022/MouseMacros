package io.github.samera2022.mousemacros.ui.frame.settings;

import io.github.samera2022.mousemacros.constant.OtherConsts;

public enum SettingsRegistry {
    APPEARANCE_SECTION("appearance_section", OtherConsts.SECTION),
    FOLLOW_SYSTEM_SETTINGS("follow_system_settings", OtherConsts.CHECK_BOX, null, true),
    SWITCH_LANG("switch_lang", OtherConsts.COMBO_BOX, "follow_system_settings", 1, -1),
    ENABLE_DARK_MODE("enable_dark_mode", OtherConsts.CHECK_BOX, "follow_system_settings", false),
    ALLOW_LONG_STR("allow_long_str", OtherConsts.CHECK_BOX, null, false),
    READJUST_FRAME_MODE("readjust_frame_mode", OtherConsts.COMBO_BOX, null, 2, 3),

    SYSTEM_SECTION("system_section", OtherConsts.SECTION),
    ENABLE_DEFAULT_STORAGE("enable_default_storage", OtherConsts.CHECK_BOX, null, false),
    DEFAULT_MMC_STORAGE_PATH("default_mmc_storage_path", OtherConsts.FILE_CHOOSER, "enable_default_storage", ""),

    MACRO_SECTION("macro_section", OtherConsts.SECTION),
    ENABLE_QUICK_MODE("enable_quick_mode", OtherConsts.CHECK_BOX, null, false),
    ENABLE_CUSTOM_MACRO_SETTINGS("enable_custom_macro_settings", OtherConsts.CHECK_BOX, null, false),
    REPEAT_TIME("repeat_times", OtherConsts.SPECIFIC_TEXT_FIELD, "enable_custom_macro_settings", 1, 0, 7),
    REPEAT_DELAY("repeat_delay", OtherConsts.SPECIFIC_TEXT_FIELD, "enable_custom_macro_settings", 0.0, 0, 7);


    public final String i18nKey;
    public final int type;
    public final String relateTo;
    public final Object defaultValue;
    public final int size;
    public final int columns;

    /**
     * Main constructor. Used by SPECIFIC_TEXT_FIELD.
     */
    SettingsRegistry(String i18nKey, int type, String relateTo, Object defaultValue, int size, int columns) {
        this.i18nKey = i18nKey;
        this.type = type;
        this.relateTo = relateTo;
        this.defaultValue = defaultValue;
        this.size = size;
        this.columns = columns;
    }

    /**
     * Constructor for SECTION.
     */
    SettingsRegistry(String i18nKey, int type) {
        this(i18nKey, type, null, null, 0, 0);
    }

    /**
     * Constructor for CHECK_BOX, TEXT_FIELD, and FILE_CHOOSER.
     */
    SettingsRegistry(String i18nKey, int type, String relateTo, Object defaultValue) {
        this(i18nKey, type, relateTo, defaultValue, 0, 20);
    }
    
    /**
     * Constructor for COMBO_BOX.
     */
    SettingsRegistry(String i18nKey, int type, String relateTo, Object defaultValue, int size) {
        this(i18nKey, type, relateTo, defaultValue, size, 20);
    }

}
