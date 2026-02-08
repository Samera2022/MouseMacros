package io.github.samera2022.mousemacros.app.script;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("脚本描述元数据测试")
public class ScriptDescriptionTest {

    private ScriptDescription scriptDescription;

    @BeforeEach
    public void setUp() {
        scriptDescription = new ScriptDescription(
            "test_script.js",
            "1.2.3",
            "Test Author",
            "A test script description",
            "Test Script Display Name",
            "test_script_register",
            "1.2.5",
            new String[]{"soft_dep1", "soft_dep2"},
            new String[]{"hard_dep1"},
            true,
            "This script requires native access for system operations"
        );
    }

    @Test
    @DisplayName("获取基本名称")
    public void testGetBaseName() {
        assertEquals("Test Script Display Name", scriptDescription.getDisplayName());
        assertEquals("test_script", scriptDescription.getRegisterName());
    }

    @Test
    @DisplayName("获取版本")
    public void testGetVersion() {
        assertEquals("1.2.3", scriptDescription.getVersion());
    }

    @Test
    @DisplayName("获取作者")
    public void testGetAuthor() {
        assertEquals("Test Author", scriptDescription.getAuthor());
    }

    @Test
    @DisplayName("获取描述")
    public void testGetDescription() {
        assertEquals("A test script description", scriptDescription.getDescription());
    }

    @Test
    @DisplayName("获取显示名称")
    public void testGetDisplayName() {
        assertEquals("Test Script Display Name", scriptDescription.getDisplayName());
    }

    @Test
    @DisplayName("获取注册名")
    public void testGetRegisterName() {
        assertEquals("test_script_register", scriptDescription.getRegisterName());
    }

    @Test
    @DisplayName("获取可用版本")
    public void testGetAvailableVersion() {
        assertEquals("1.2.5", scriptDescription.getAvailableVersion());
    }

    @Test
    @DisplayName("获取软依赖")
    public void testGetSoftDependencies() {
        String[] softDeps = scriptDescription.getSoftDependencies();
        assertNotNull(softDeps);
        assertEquals(2, softDeps.length);
        assertEquals("soft_dep1", softDeps[0]);
        assertEquals("soft_dep2", softDeps[1]);
    }

    @Test
    @DisplayName("获取硬依赖")
    public void testGetHardDependencies() {
        String[] hardDeps = scriptDescription.getHardDependencies();
        assertNotNull(hardDeps);
        assertEquals(1, hardDeps.length);
        assertEquals("hard_dep1", hardDeps[0]);
    }

    @Test
    @DisplayName("检查是否需要native访问")
    public void testIsRequiresNativeAccess() {
        assertTrue(scriptDescription.isRequiresNativeAccess());
    }

    @Test
    @DisplayName("获取native访问描述")
    public void testGetNativeAccessDescription() {
        assertEquals("This script requires native access for system operations",
            scriptDescription.getNativeAccessDescription());
    }

    @Test
    @DisplayName("创建无native访问的脚本描述")
    public void testCreateWithoutNativeAccess() {
        ScriptDescription desc = new ScriptDescription(
            "safe_script.js",
            "1.0.0",
            "Author",
            "Safe script",
            "Safe Script",
            "safe_script",
            "1.0.0",
            new String[0],
            new String[0],
            false,
            ""
        );

        assertFalse(desc.isRequiresNativeAccess());
        assertEquals("", desc.getNativeAccessDescription());
    }

    @Test
    @DisplayName("空依赖数组")
    public void testEmptyDependencies() {
        ScriptDescription desc = new ScriptDescription(
            "standalone.js",
            "1.0.0",
            "Author",
            "Standalone script",
            "Standalone",
            "standalone",
            "1.0.0",
            new String[0],
            new String[0],
            false,
            ""
        );

        assertEquals(0, desc.getSoftDependencies().length);
        assertEquals(0, desc.getHardDependencies().length);
    }

    @Test
    @DisplayName("软依赖不应为null")
    public void testSoftDependenciesNotNull() {
        assertNotNull(scriptDescription.getSoftDependencies());
    }

    @Test
    @DisplayName("硬依赖不应为null")
    public void testHardDependenciesNotNull() {
        assertNotNull(scriptDescription.getHardDependencies());
    }

    @Test
    @DisplayName("版本字符串格式")
    public void testVersionFormat() {
        assertTrue(scriptDescription.getVersion().matches("\\d+\\.\\d+\\.\\d+.*"));
    }

    @Test
    @DisplayName("注册名应该唯一和有效")
    public void testRegisterNameValidity() {
        String registerName = scriptDescription.getRegisterName();
        assertNotNull(registerName);
        assertFalse(registerName.isEmpty());
        assertTrue(registerName.matches("[a-zA-Z0-9_]+"));
    }

    @Test
    @DisplayName("脚本名称不应为空")
    public void testNameNotEmpty() {
        assertFalse(scriptDescription.getDisplayName().isEmpty());
        assertFalse(scriptDescription.getAuthor().isEmpty());
    }

    @Test
    @DisplayName("多个软依赖")
    public void testMultipleSoftDependencies() {
        String[] deps = scriptDescription.getSoftDependencies();
        assertTrue(deps.length >= 2);
        assertTrue(java.util.Arrays.asList(deps).contains("soft_dep1"));
        assertTrue(java.util.Arrays.asList(deps).contains("soft_dep2"));
    }

    @Test
    @DisplayName("脚本描述完整性")
    public void testDescriptionCompleteness() {
        assertNotNull(scriptDescription.getDisplayName());
        assertNotNull(scriptDescription.getVersion());
        assertNotNull(scriptDescription.getAuthor());
        assertNotNull(scriptDescription.getDescription());
        assertNotNull(scriptDescription.getRegisterName());
        assertNotNull(scriptDescription.getAvailableVersion());
        assertNotNull(scriptDescription.getSoftDependencies());
        assertNotNull(scriptDescription.getHardDependencies());
        assertNotNull(scriptDescription.getNativeAccessDescription());
    }

    @Test
    @DisplayName("可用版本与当前版本可能不同")
    public void testAvailableVersionDifference() {
        // 可用版本可能新于当前版本（用于检查更新）
        ScriptDescription desc = new ScriptDescription(
            "updatable.js",
            "1.0.0",
            "Author",
            "Can be updated",
            "Updatable Script",
            "updatable",
            "2.0.0",  // 有新版本可用
            new String[0],
            new String[0],
            false,
            ""
        );

        assertNotEquals(desc.getVersion(), desc.getAvailableVersion());
    }
}

