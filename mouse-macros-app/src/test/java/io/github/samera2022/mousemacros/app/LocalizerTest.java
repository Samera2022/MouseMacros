package io.github.samera2022.mousemacros.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("国际化系统测试")
public class LocalizerTest {

    @BeforeEach
    public void setUp() {
        // Localizer 是单例，已在类加载时初始化
    }

    @Test
    @DisplayName("获取翻译 - 存在的键")
    public void testGetExistingKey() {
        String result = Localizer.get("settings");
        assertNotNull(result, "翻译不应该为null");
        assertNotEquals("", result, "翻译不应该为空字符串");
    }

    @Test
    @DisplayName("获取翻译 - 不存在的键应返回键本身")
    public void testGetNonExistingKey() {
        String key = "non_existing_key_12345";
        String result = Localizer.get(key);
        assertEquals(key, result, "不存在的键应返回键本身");
    }

    @Test
    @DisplayName("获取翻译 - null键返回空字符串")
    public void testGetNullKey() {
        String result = Localizer.get(null);
        assertEquals("", result, "null键应返回空字符串");
    }

    @Test
    @DisplayName("获取设置翻译 - 自动前缀")
    public void testGetSettingsKey() {
        String result = Localizer.getS("enable_dark_mode");
        assertNotNull(result);
        // 应该查找 "settings.enable_dark_mode"
    }

    @Test
    @DisplayName("检查键是否存在")
    public void testHasKey() {
        assertTrue(Localizer.hasKey("settings"), "常见键应该存在");
        assertFalse(Localizer.hasKey("non_existing_key_xyz"), "不存在的键应返回false");
    }

    @Test
    @DisplayName("获取当前语言")
    public void testGetCurrentLang() {
        String lang = Localizer.getCurrentLang();
        assertNotNull(lang);
        assertTrue(lang.matches("[a-z]{2}_[a-z]{2}"), "语言代码格式应为 xx_xx");
    }

    @Test
    @DisplayName("设置运行时语言切换标志")
    public void testRuntimeSwitchFlag() {
        Localizer.setRuntimeSwitch(true);
        assertTrue(Localizer.isRuntimeSwitch());

        Localizer.setRuntimeSwitch(false);
        assertFalse(Localizer.isRuntimeSwitch());
    }

    @Test
    @DisplayName("加载指定语言")
    public void testLoadLanguage() {
        String currentLang = Localizer.getCurrentLang();
        Localizer.load("en_us");
        assertEquals("en_us", Localizer.getCurrentLang());

        // 恢复原语言
        Localizer.load(currentLang);
    }

    @Test
    @DisplayName("多语言Fallback机制")
    public void testFallbackMechanism() {
        // 加载任意语言，不存在的键应Fallback到英文或返回键本身
        String key = "non_existent_test_key";
        String result = Localizer.get(key);
        // 结果应该是键本身（因为English中也不存在）
        assertEquals(key, result);
    }
}

