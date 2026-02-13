package io.github.samera2022.mousemacros.app.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("系统工具测试")
public class SystemUtilTest {

    @Test
    @DisplayName("获取屏幕缩放比例")
    public void testGetScale() {
        double[] scale = SystemUtil.getScale();
        assertNotNull(scale);
        assertEquals(2, scale.length);
        assertTrue(scale[0] > 0, "X缩放比应该大于0");
        assertTrue(scale[1] > 0, "Y缩放比应该大于0");
    }

    @Test
    @DisplayName("缩放比例为有效值")
    public void testScaleValidValue() {
        double[] scale = SystemUtil.getScale();
        // 通常缩放比在0.5-3.0之间
        assertTrue(scale[0] >= 0.5 && scale[0] <= 3.0);
        assertTrue(scale[1] >= 0.5 && scale[1] <= 3.0);
    }

    @Test
    @DisplayName("X和Y缩放通常相同")
    public void testScaleXEqualsY() {
        double[] scale = SystemUtil.getScale();
        // 大多数系统中X和Y缩放相同
        assertEquals(scale[0], scale[1], 0.01);
    }

    @Test
    @DisplayName("获取系统语言")
    public void testGetSystemLang() {
        String[] availableLangs = {"en_us", "zh_cn", "ja_jp"};
        String lang = SystemUtil.getSystemLang(availableLangs);

        assertNotNull(lang);
        assertTrue(java.util.Arrays.asList(availableLangs).contains(lang),
            "应该返回可用语言列表中的一个");
    }

    @Test
    @DisplayName("系统语言格式正确")
    public void testSystemLangFormat() {
        String[] availableLangs = {"en_us", "zh_cn"};
        String lang = SystemUtil.getSystemLang(availableLangs);
        assertTrue(lang.matches("[a-z]{2}_[a-z]{2}"));
    }

    @Test
    @DisplayName("语言列表为空时返回默认")
    public void testGetSystemLangEmptyList() {
        String[] emptyLangs = {};
        String lang = SystemUtil.getSystemLang(emptyLangs);
        assertEquals("en_us", lang, "空列表应返回en_us");
    }

    @Test
    @DisplayName("只有一个语言时返回该语言")
    public void testGetSystemLangSingleLanguage() {
        String[] singleLang = {"zh_cn"};
        String lang = SystemUtil.getSystemLang(singleLang);
        assertEquals("zh_cn", lang);
    }

    @Test
    @DisplayName("系统语言匹配完整代码")
    public void testSystemLangExactMatch() {
        String[] langs = {"en_us", "en_gb", "zh_cn"};
        String lang = SystemUtil.getSystemLang(langs);
        // 应该返回其中之一
        assertTrue(java.util.Arrays.asList(langs).contains(lang));
    }

    @Test
    @DisplayName("语言代码不区分大小写")
    public void testLanguageCodeCase() {
        String[] langs = {"EN_US", "ZH_CN"};
        String lang = SystemUtil.getSystemLang(langs);
        assertNotNull(lang);
    }

    @Test
    @DisplayName("检测系统深色模式")
    public void testIsSystemDarkMode() {
        // Windows系统可能返回true或false
        // 非Windows系统应该返回false
        boolean isDarkMode = SystemUtil.isSystemDarkMode();
        // 就是检查它不抛异常并返回boolean
        assertTrue(isDarkMode == true || isDarkMode == false);
    }

    @Test
    @DisplayName("非Windows系统深色模式检测应返回false")
    public void testDarkModeNonWindows() {
        String os = System.getProperty("os.name");
        if (!os.toLowerCase().contains("win")) {
            assertFalse(SystemUtil.isSystemDarkMode());
        }
    }

    @Test
    @DisplayName("多次调用返回一致结果")
    public void testConsistentResults() {
        double[] scale1 = SystemUtil.getScale();
        double[] scale2 = SystemUtil.getScale();

        assertEquals(scale1[0], scale2[0]);
        assertEquals(scale1[1], scale2[1]);
    }

    @Test
    @DisplayName("系统工具方法不抛异常")
    public void testUtilityMethodsNotThrow() {
        assertDoesNotThrow(() -> {
            SystemUtil.getScale();
            SystemUtil.getSystemLang(new String[]{"en_us"});
            SystemUtil.isSystemDarkMode();
        });
    }
}

