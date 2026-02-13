package io.github.samera2022.mousemacros.app.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("配置管理系统测试")
public class ConfigManagerTest {

    @BeforeEach
    public void setUp() {
        // ConfigManager 使用单例，初始化时会加载配置
        ConfigManager.reload();
    }

    @Test
    @DisplayName("配置目录存在")
    public void testConfigDirExists() {
        assertNotNull(ConfigManager.CONFIG_DIR);
        assertFalse(ConfigManager.CONFIG_DIR.isEmpty());
    }

    @Test
    @DisplayName("获取布尔值配置")
    public void testGetBoolean() {
        // 测试已知的配置项
        assertDoesNotThrow(() -> ConfigManager.getBoolean("enable_dark_mode"));
    }

    @Test
    @DisplayName("获取整数值配置")
    public void testGetInt() {
        int result = ConfigManager.getInt("readjustFrameMode");
        assertTrue(result >= 0);
    }

    @Test
    @DisplayName("获取浮点数值配置")
    public void testGetDouble() {
        double result = ConfigManager.getDouble("some_double_setting");
        // 应该返回 0.0 或正常值
        assertTrue(result >= 0);
    }

    @Test
    @DisplayName("获取字符串值配置")
    public void testGetString() {
        // 测试可能为null或有效的语言代码的配置项
        assertDoesNotThrow(() -> ConfigManager.getString("switch_lang"));
    }

    @Test
    @DisplayName("设置配置项")
    public void testSetConfig() {
        String testKey = "test_key_12345";
        Object testValue = "test_value";

        ConfigManager.set(testKey, testValue);

        String result = ConfigManager.getString(testKey);
        assertEquals(testValue.toString(), result);
    }

    @Test
    @DisplayName("获取快捷键映射")
    public void testGetKeyMap() {
        Map<String, String> keyMap = ConfigManager.getKeyMap();
        assertNotNull(keyMap);

        // 应该包含默认快捷键
        if (!keyMap.isEmpty()) {
            assertTrue(keyMap.containsKey("start_record") ||
                      keyMap.containsKey("stop_record") ||
                      keyMap.containsKey("play_macro") ||
                      keyMap.containsKey("abort_macro_operation"));
        }
    }

    @Test
    @DisplayName("快捷键值应为数字字符串")
    public void testKeyMapValuesAreNumeric() {
        Map<String, String> keyMap = ConfigManager.getKeyMap();

        for (Map.Entry<String, String> entry : keyMap.entrySet()) {
            try {
                Integer.parseInt(entry.getValue());
            } catch (NumberFormatException e) {
                fail("快捷键值应该是数字: " + entry.getValue());
            }
        }
    }

    @Test
    @DisplayName("重置为默认配置")
    public void testResetToDefault() {
        ConfigManager.resetToDefault();

        // 快捷键应该被重置
        Map<String, String> keyMap = ConfigManager.getKeyMap();
        assertNotNull(keyMap);

        if (keyMap.containsKey("start_record")) {
            assertEquals("60", keyMap.get("start_record"), "F1 的键码是 60");
        }
    }

    @Test
    @DisplayName("保存和重新加载配置")
    public void testSaveAndReload() {
        String testKey = "test_save_reload_key";
        String testValue = "test_save_reload_value";

        ConfigManager.set(testKey, testValue);
        ConfigManager.saveConfig();
        ConfigManager.reload();

        String result = ConfigManager.getString(testKey);
        assertEquals(testValue, result);
    }

    @Test
    @DisplayName("获取可用的语言列表")
    public void testGetAvailableLangs() {
        String[] langs = ConfigManager.getAvailableLangs();
        assertNotNull(langs);

        if (langs.length > 0) {
            // 应该至少有英文或其他语言
            boolean hasValidLang = false;
            for (String lang : langs) {
                if (!lang.isEmpty()) {
                    hasValidLang = true;
                    break;
                }
            }
            assertTrue(hasValidLang, "应该有可用的语言文件");
        }
    }

    @Test
    @DisplayName("常数值验证")
    public void testConstantValues() {
        assertEquals(1, ConfigManager.RFM_MIXED);
        assertEquals(2, ConfigManager.RFM_STANDARDIZED);
        assertEquals(3, ConfigManager.RFM_MEMORIZED);
    }

    @Test
    @DisplayName("获取未设置的配置项返回默认值")
    public void testGetUnsetValue() {
        boolean boolValue = ConfigManager.getBoolean("non_existent_bool");
        assertFalse(boolValue);

        int intValue = ConfigManager.getInt("non_existent_int");
        assertEquals(0, intValue);

        double doubleValue = ConfigManager.getDouble("non_existent_double");
        assertEquals(0.0, doubleValue);

        String stringValue = ConfigManager.getString("non_existent_string");
        assertNull(stringValue);
    }

    @Test
    @DisplayName("类型转换 - Number to Int")
    public void testNumberToInt() {
        // 从JSON加载时，数值会被解析为Double，getInt应该能处理
        ConfigManager.set("test_number", 42.0);
        int result = ConfigManager.getInt("test_number");
        assertEquals(42, result);
    }

    @Test
    @DisplayName("配置可以保存和读取")
    public void testConfigPersistence() {
        // 这个测试验证了文件I/O
        ConfigManager.set("persistence_test", "persistence_value");
        ConfigManager.saveConfig();
        ConfigManager.reload();

        String result = ConfigManager.getString("persistence_test");
        assertEquals("persistence_value", result);
    }
}

