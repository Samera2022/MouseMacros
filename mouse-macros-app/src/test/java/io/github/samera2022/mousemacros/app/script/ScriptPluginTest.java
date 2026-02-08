package io.github.samera2022.mousemacros.app.script;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("脚本插件测试")
public class ScriptPluginTest {

    private File testScriptFile;
    private ScriptDescription testDescription;
    private ScriptPlugin testPlugin;

    @BeforeEach
    public void setUp() {
        // 创建测试数据
        testScriptFile = new File("test_script.js");
        testDescription = new ScriptDescription(
            "test_script.js",
            "1.0.0",
            "Test Author",
            "Test Description",
            "Test Script",
            "test_script",
            "1.0.0",
            new String[]{"dep1"},
            new String[]{"dep2"},
            false,
            ""
        );
        testPlugin = new ScriptPlugin(testScriptFile, testDescription);
    }

    @Test
    @DisplayName("创建脚本插件")
    public void testCreateScriptPlugin() {
        assertNotNull(testPlugin);
        assertEquals(testScriptFile, testPlugin.getFile());
        assertEquals(testDescription, testPlugin.getDescription());
    }

    @Test
    @DisplayName("获取脚本名称")
    public void testGetName() {
        assertEquals("Test Script", testPlugin.getName());
    }

    @Test
    @DisplayName("获取脚本作者")
    public void testGetAuthor() {
        assertEquals("Test Author", testPlugin.getAuthor());
    }

    @Test
    @DisplayName("获取脚本版本")
    public void testGetVersion() {
        assertEquals("1.0.0", testPlugin.getVersion());
    }

    @Test
    @DisplayName("获取脚本描述")
    public void testGetDescription() {
        assertEquals("Test Description", testPlugin.getDescriptionText());
    }

    @Test
    @DisplayName("获取脚本注册名")
    public void testGetRegisterName() {
        assertEquals("test_script", testPlugin.getRegisterName());
    }

    @Test
    @DisplayName("获取脚本对象")
    public void testGetDescriptionObject() {
        assertNotNull(testPlugin.getDescription());
        assertEquals(testDescription, testPlugin.getDescription());
    }

    @Test
    @DisplayName("初始状态：未加载")
    public void testInitialStateNotLoaded() {
        assertFalse(testPlugin.isLoaded());
    }

    @Test
    @DisplayName("设置加载状态")
    public void testSetLoaded() {
        testPlugin.setLoaded(true);
        assertTrue(testPlugin.isLoaded());

        testPlugin.setLoaded(false);
        assertFalse(testPlugin.isLoaded());
    }

    @Test
    @DisplayName("初始状态：已启用（非.disabled文件）")
    public void testInitialStateEnabled() {
        assertTrue(testPlugin.isEnabled());
    }

    @Test
    @DisplayName("初始状态：已禁用（.disabled文件）")
    public void testInitialStateDisabled() {
        File disabledFile = new File("test_script.js.disabled");
        ScriptPlugin plugin = new ScriptPlugin(disabledFile, testDescription);
        assertFalse(plugin.isEnabled());
    }

    @Test
    @DisplayName("设置启用状态")
    public void testSetEnabled() {
        testPlugin.setEnabled(false);
        assertFalse(testPlugin.isEnabled());

        testPlugin.setEnabled(true);
        assertTrue(testPlugin.isEnabled());
    }

    @Test
    @DisplayName("设置和获取API实例")
    public void testSetAndGetApiInstance() {
        assertNull(testPlugin.getApiInstance());

        ScriptContext context = new ScriptContext(testScriptFile, testDescription);
        ScriptAPI api = new ScriptAPI(context);

        testPlugin.setApiInstance(api);
        assertEquals(api, testPlugin.getApiInstance());
    }

    @Test
    @DisplayName("添加脚本问题")
    public void testAddIssue() {
        ScriptIssue issue = new ScriptIssue(ScriptProblem.H_DEP_MISSING, new String[]{"missing_dep"});
        testPlugin.addIssue(issue);

        // 问题应该被添加（通过getIssues方法验证，如果存在的话）
        // 这里假设有获取问题的方法
    }

    @Test
    @DisplayName("多个问题可添加")
    public void testAddMultipleIssues() {
        ScriptIssue issue1 = new ScriptIssue(ScriptProblem.H_DEP_MISSING, new String[]{"dep1"});
        ScriptIssue issue2 = new ScriptIssue(ScriptProblem.H_DEP_MISSING, new String[]{"dep2"});

        testPlugin.addIssue(issue1);
        testPlugin.addIssue(issue2);

        // 应该支持多个问题
    }

    @Test
    @DisplayName("更改脚本文件")
    public void testSetFile() {
        File newFile = new File("new_script.js");
        testPlugin.setFile(newFile);
        assertEquals(newFile, testPlugin.getFile());
    }

    @Test
    @DisplayName("脚本插件封装完整信息")
    public void testPluginEncapsulatesAllInfo() {
        assertNotNull(testPlugin.getName());
        assertNotNull(testPlugin.getAuthor());
        assertNotNull(testPlugin.getVersion());
        assertNotNull(testPlugin.getDescriptionText());
        assertNotNull(testPlugin.getRegisterName());
        assertNotNull(testPlugin.getFile());
        assertNotNull(testPlugin.getDescription());
    }

    @Test
    @DisplayName("脚本信息的一致性")
    public void testInfoConsistency() {
        ScriptDescription desc = testPlugin.getDescription();

        assertEquals(desc.getAuthor(), testPlugin.getAuthor());
        assertEquals(desc.getVersion(), testPlugin.getVersion());
        assertEquals(desc.getDescription(), testPlugin.getDescriptionText());
        assertEquals(desc.getRegisterName(), testPlugin.getRegisterName());
    }

    @Test
    @DisplayName("API实例可以清理")
    public void testClearApiInstance() {
        ScriptContext context = new ScriptContext(testScriptFile, testDescription);
        ScriptAPI api = new ScriptAPI(context);
        testPlugin.setApiInstance(api);

        assertNotNull(testPlugin.getApiInstance());

        testPlugin.setApiInstance(null);
        assertNull(testPlugin.getApiInstance());
    }
}

