package io.github.samera2022.mousemacros.app.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("其他工具类测试")
public class OtherUtilTest {

    @Test
    @DisplayName("获取按键显示文本 - 功能键")
    public void testGetNativeKeyDisplayTextFunctionKey() {
        // F1 的键码是 60
        String display = OtherUtil.getNativeKeyDisplayText(60);
        assertNotNull(display);
        assertTrue(display.contains("F1"));
    }

    @Test
    @DisplayName("获取按键显示文本 - F2")
    public void testGetNativeKeyDisplayTextF2() {
        // F2 的键码是 61
        String display = OtherUtil.getNativeKeyDisplayText(61);
        assertNotNull(display);
        assertTrue(display.contains("F2"));
    }

    @Test
    @DisplayName("获取按键显示文本 - F3")
    public void testGetNativeKeyDisplayTextF3() {
        // F3 的键码是 62
        String display = OtherUtil.getNativeKeyDisplayText(62);
        assertNotNull(display);
        assertTrue(display.contains("F3"));
    }

    @Test
    @DisplayName("获取按键显示文本 - F4")
    public void testGetNativeKeyDisplayTextF4() {
        // F4 的键码是 63
        String display = OtherUtil.getNativeKeyDisplayText(63);
        assertNotNull(display);
        assertTrue(display.contains("F4"));
    }

    @Test
    @DisplayName("获取按键显示文本 - 无效键码")
    public void testGetNativeKeyDisplayTextInvalidCode() {
        String display = OtherUtil.getNativeKeyDisplayText(-1);
        assertNotNull(display);
        // 无效键码应该返回某种标识
    }

    @Test
    @DisplayName("获取按键显示文本 - 零键码")
    public void testGetNativeKeyDisplayTextZeroCode() {
        String display = OtherUtil.getNativeKeyDisplayText(0);
        assertNotNull(display);
    }

    @Test
    @DisplayName("不同键码返回不同文本")
    public void testDifferentKeyCodesDifferentDisplay() {
        String display60 = OtherUtil.getNativeKeyDisplayText(60);
        String display61 = OtherUtil.getNativeKeyDisplayText(61);

        // F1和F2应该有不同的显示
        assertNotEquals(display60, display61);
    }

    @Test
    @DisplayName("获取按键显示文本不为空")
    public void testNativeKeyDisplayNotEmpty() {
        for (int i = 60; i <= 73; i++) {  // F1-F12
            String display = OtherUtil.getNativeKeyDisplayText(i);
            assertFalse(display.isEmpty(), "按键显示文本不应为空");
        }
    }

    @Test
    @DisplayName("按键文本应该可读")
    public void testNativeKeyDisplayReadable() {
        String display = OtherUtil.getNativeKeyDisplayText(60);
        // 应该包含数字或字母
        assertTrue(display.matches(".*[a-zA-Z0-9]+.*"));
    }

    @Test
    @DisplayName("相同键码返回相同文本")
    public void testConsistentKeyDisplay() {
        String display1 = OtherUtil.getNativeKeyDisplayText(60);
        String display2 = OtherUtil.getNativeKeyDisplayText(60);

        assertEquals(display1, display2);
    }

    @Test
    @DisplayName("所有功能键都有文本表示")
    public void testAllFunctionKeysHaveDisplay() {
        // F1-F12 的键码是 60-71
        for (int i = 60; i <= 71; i++) {
            String display = OtherUtil.getNativeKeyDisplayText(i);
            assertNotNull(display);
            assertFalse(display.isEmpty());
        }
    }

    @Test
    @DisplayName("获取按键显示文本不抛异常")
    public void testGetKeyDisplayDoesNotThrow() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 256; i++) {
                OtherUtil.getNativeKeyDisplayText(i);
            }
        });
    }

    @Test
    @DisplayName("常用快捷键文本")
    public void testCommonHotkeyDisplays() {
        // 获取默认的4个快捷键的显示文本
        String start = OtherUtil.getNativeKeyDisplayText(60);   // F1
        String stop = OtherUtil.getNativeKeyDisplayText(61);    // F2
        String play = OtherUtil.getNativeKeyDisplayText(62);    // F3
        String abort = OtherUtil.getNativeKeyDisplayText(63);   // F4

        assertNotNull(start);
        assertNotNull(stop);
        assertNotNull(play);
        assertNotNull(abort);

        assertFalse(start.isEmpty());
        assertFalse(stop.isEmpty());
        assertFalse(play.isEmpty());
        assertFalse(abort.isEmpty());
    }

    @Test
    @DisplayName("按键显示文本应该简洁")
    public void testKeyDisplayBrief() {
        String display = OtherUtil.getNativeKeyDisplayText(60);
        // 一般应该在3-10个字符
        assertTrue(display.length() >= 2 && display.length() <= 20);
    }
}

