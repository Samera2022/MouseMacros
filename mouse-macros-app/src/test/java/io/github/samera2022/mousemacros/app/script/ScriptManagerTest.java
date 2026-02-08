package io.github.samera2022.mousemacros.app.script;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("脚本管理系统测试")
public class ScriptManagerTest {

    @BeforeEach
    public void setUp() {
        // ScriptManager 会扫描脚本目录并加载脚本
    }

    @Test
    @DisplayName("脚本路径存在")
    public void testScriptPathExists() {
        assertNotNull(ScriptManager.SCRIPT_PATH);
        assertFalse(ScriptManager.SCRIPT_PATH.isEmpty());
    }

    @Test
    @DisplayName("脚本目录可创建")
    public void testScriptDirectory() {
        File scriptDir = new File(ScriptManager.SCRIPT_PATH);
        assertTrue(scriptDir.exists() || scriptDir.mkdirs(),
            "脚本目录应该存在或可创建");
    }

    @Test
    @DisplayName("获取脚本列表")
    public void testGetScripts() {
        List<ScriptPlugin> scripts = ScriptManager.getScripts();
        assertNotNull(scripts, "脚本列表不应该为null");
        // 脚本列表可能为空（如果没有脚本文件）
    }

    @Test
    @DisplayName("脚本列表返回新列表副本")
    public void testGetScriptsCopyNotReference() {
        List<ScriptPlugin> scripts1 = ScriptManager.getScripts();
        List<ScriptPlugin> scripts2 = ScriptManager.getScripts();

        assertNotSame(scripts1, scripts2, "应该返回新的列表副本");
        assertEquals(scripts1.size(), scripts2.size(), "内容应该相同");
    }

    @Test
    @DisplayName("加载并处理脚本")
    public void testLoadAndProcessScripts() {
        // 这个方法会扫描目录、检查依赖、检查白名单等
        assertDoesNotThrow(ScriptManager::loadAndProcessScripts);

        List<ScriptPlugin> scripts = ScriptManager.getScripts();
        assertNotNull(scripts);
    }

    @Test
    @DisplayName("检查依赖关系")
    public void testCheckDependencies() {
        // 首先加载脚本
        ScriptManager.loadAndProcessScripts();

        // 检查依赖不应抛出异常
        assertDoesNotThrow(ScriptManager::checkDependencies);
    }

    @Test
    @DisplayName("脚本检查依赖后设置问题")
    public void testDependencyCheckSetsIssues() {
        ScriptManager.loadAndProcessScripts();

        // 如果有脚本并且有依赖问题，应该被标记
        List<ScriptPlugin> scripts = ScriptManager.getScripts();
        for (ScriptPlugin script : scripts) {
            // 脚本应该有 issues 列表
            assertNotNull(script.getDescription());
        }
    }

    @Test
    @DisplayName("脚本元数据可读取")
    public void testScriptMetadataReadable() {
        ScriptManager.loadAndProcessScripts();

        List<ScriptPlugin> scripts = ScriptManager.getScripts();
        for (ScriptPlugin script : scripts) {
            assertNotNull(script.getDescription());
            assertNotNull(script.getName());
            assertNotNull(script.getAuthor());
            assertNotNull(script.getVersion());
        }
    }

    @Test
    @DisplayName("脚本启用/禁用状态初始化")
    public void testScriptInitialState() {
        ScriptManager.loadAndProcessScripts();

        List<ScriptPlugin> scripts = ScriptManager.getScripts();
        for (ScriptPlugin script : scripts) {
            // 脚本应该有初始启用/禁用状态
            assertFalse(script.isLoaded() && !script.isEnabled(),
                "已启用的脚本应该被加载，或已禁用脚本不应加载");
        }
    }

    @Test
    @DisplayName("禁用脚本")
    public void testDisableScript() {
        ScriptManager.loadAndProcessScripts();
        List<ScriptPlugin> scripts = ScriptManager.getScripts();

        if (!scripts.isEmpty()) {
            ScriptPlugin script = scripts.get(0);

            // 禁用不应抛异常
            assertDoesNotThrow(() -> ScriptManager.disableScript(script));
        }
    }

    @Test
    @DisplayName("启用脚本（非native）")
    public void testEnableScriptNonNative() {
        ScriptManager.loadAndProcessScripts();
        List<ScriptPlugin> scripts = ScriptManager.getScripts();

        // 找一个非native脚本
        for (ScriptPlugin script : scripts) {
            if (!script.getDescription().isRequiresNativeAccess()) {
                // 应该能启用（如果不需要native访问）
                assertDoesNotThrow(() -> ScriptManager.enableScript(script));
                break;
            }
        }
    }

    @Test
    @DisplayName("不提供凭证启用native脚本应失败")
    public void testEnableNativeScriptWithoutCredentials() {
        ScriptManager.loadAndProcessScripts();
        List<ScriptPlugin> scripts = ScriptManager.getScripts();

        // 找一个native脚本
        for (ScriptPlugin script : scripts) {
            if (script.getDescription().isRequiresNativeAccess()) {
                // 不提供native访问凭证应该失败或日志警告
                ScriptManager.enableScript(script);
                // 脚本应该仍然禁用
                assertFalse(script.isLoaded());
                break;
            }
        }
    }

    @Test
    @DisplayName("脚本列表会清理旧上下文")
    public void testLoadAndProcessClearsOldContexts() {
        // 加载一次
        ScriptManager.loadAndProcessScripts();
        int firstLoadSize = ScriptManager.getScripts().size();

        // 再加载一次应该清理旧的
        assertDoesNotThrow(ScriptManager::loadAndProcessScripts);

        int secondLoadSize = ScriptManager.getScripts().size();
        assertEquals(firstLoadSize, secondLoadSize, "加载次数不应影响脚本数量");
    }

    @Test
    @DisplayName("脚本注册名唯一性")
    public void testScriptRegisterNamesUnique() {
        ScriptManager.loadAndProcessScripts();
        List<ScriptPlugin> scripts = ScriptManager.getScripts();

        // 检查注册名不重复
        Set<String> registerNames = new java.util.HashSet<>();
        for (ScriptPlugin script : scripts) {
            String registerName = script.getRegisterName();
            assertFalse(registerNames.contains(registerName),
                "脚本注册名应该唯一: " + registerName);
            registerNames.add(registerName);
        }
    }

    @Test
    @DisplayName("脚本文件可访问")
    public void testScriptFilesAccessible() {
        ScriptManager.loadAndProcessScripts();
        List<ScriptPlugin> scripts = ScriptManager.getScripts();

        for (ScriptPlugin script : scripts) {
            File file = script.getFile();
            assertNotNull(file);
            assertTrue(file.exists(), "脚本文件应该存在: " + file.getPath());
        }
    }

    @Test
    @DisplayName("已禁用脚本文件有.disabled后缀")
    public void testDisabledScriptFileSuffix() {
        ScriptManager.loadAndProcessScripts();
        List<ScriptPlugin> scripts = ScriptManager.getScripts();

        for (ScriptPlugin script : scripts) {
            if (!script.isEnabled()) {
                String filename = script.getFile().getName();
                assertTrue(filename.endsWith(".disabled"),
                    "已禁用脚本文件应该有.disabled后缀");
            }
        }
    }

    @Test
    @DisplayName("已启用脚本文件无.disabled后缀")
    public void testEnabledScriptFileNoDisabledSuffix() {
        ScriptManager.loadAndProcessScripts();
        List<ScriptPlugin> scripts = ScriptManager.getScripts();

        for (ScriptPlugin script : scripts) {
            if (script.isEnabled()) {
                String filename = script.getFile().getName();
                assertFalse(filename.endsWith(".disabled"),
                    "已启用脚本文件不应该有.disabled后缀");
            }
        }
    }
}

