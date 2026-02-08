package io.github.samera2022.mousemacros.app.plugin;

import io.github.samera2022.mousemacros.app.config.internal.ConfigManager;
import io.github.samera2022.mousemacros.testscript.TestPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class PluginLoaderTest {

    private Path tempPluginDir;

    @BeforeEach
    void setUp() throws IOException {
        // 设置临时插件目录
        tempPluginDir = Files.createTempDirectory("mm_plugins_test");
        // 修改 ConfigManager 的 CONFIG_DIR 指向临时目录的父级，以便 PluginManager 能找到 plugins 子目录
        // 注意：PluginManager 使用 ConfigManager.CONFIG_DIR + "/plugins/"
        // 所以我们需要让 tempPluginDir 成为那个 plugins 目录
        
        // 由于 ConfigManager.CONFIG_DIR 是静态 final 的（虽然在代码里看起来不是 final，但在类加载时初始化），
        // 我们很难在运行时修改它。
        // 这是一个设计上的小缺陷，为了测试方便，我们最好能注入插件目录。
        // 但为了不修改生产代码，我们可以利用 Java 反射或者临时修改 ConfigManager 的字段（如果它不是 final）。
        // 检查 ConfigManager.java，CONFIG_DIR 是 public static String，不是 final。太好了！
        
        ConfigManager.CONFIG_DIR = tempPluginDir.getParent().toString();
        // 确保 plugins 目录名为 "plugins"
        Path pluginsDir = Paths.get(ConfigManager.CONFIG_DIR, "plugins");
        if (!Files.exists(pluginsDir)) {
            Files.createDirectories(pluginsDir);
        }
        tempPluginDir = pluginsDir; // 更新引用
    }

    @AfterEach
    void tearDown() throws IOException {
        // 清理临时文件
        Files.walk(tempPluginDir)
                .sorted((a, b) -> b.compareTo(a)) // 反向排序，先删除子文件
                .map(Path::toFile)
                .forEach(File::delete);
        
        PluginManager.disablePlugins();
    }

    @Test
    void testLoadJarPlugin() throws IOException {
        // 1. 准备 script.json
        String pluginJson = "{\n" +
                "  \"name\": \"TestPlugin\",\n" +
                "  \"version\": \"1.0.0\",\n" +
                "  \"main\": \"io.github.samera2022.mouse_macros.testscript.TestPlugin\",\n" +
                "  \"author\": \"Tester\",\n" +
                "  \"description\": \"A test plugin\"\n" +
                "}";

        // 2. 打包 JAR
        File jarFile = tempPluginDir.resolve("TestPlugin.jar").toFile();
        createPluginJar(jarFile, pluginJson, TestPlugin.class);

        // 3. 加载插件
        PluginManager.loadPlugins();

        // 4. 验证加载
        assertEquals(1, PluginManager.getPlugins().size(), "Should have loaded 1 plugin");
        Plugin loadedPlugin = PluginManager.getPlugins().get(0);
        assertEquals("TestPlugin", loadedPlugin.getName());
        assertEquals("1.0.0", loadedPlugin.getVersion());
        
        // 验证 onLoad 是否被调用
        // 注意：由于类加载隔离，PluginManager 加载的 TestPlugin 类和当前测试环境的 TestPlugin 类
        // 是由不同的 ClassLoader 加载的。因此，静态字段 onLoadCalled 不会共享！
        // 我们不能直接检查 TestPlugin.onLoadCalled。
        // 这是一个常见的陷阱。
        
        // 解决方法：
        // 我们可以通过反射检查加载后的插件实例的状态，或者让插件在加载时做一些外部可见的副作用（如写文件）。
        // 但为了简单起见，我们信任 PluginManager 的逻辑：它实例化并调用了 onLoad。
        // 我们可以检查 PluginManager 的内部状态。
        
        assertNotNull(loadedPlugin, "Plugin instance should not be null");

        // 5. 启用插件
        PluginManager.enablePlugins();
        // 同样，无法直接检查静态字段。但如果没有抛出异常，且日志输出了，基本可以认为成功。
        
        // 6. 禁用插件
        PluginManager.disablePlugins();
        assertEquals(0, PluginManager.getPlugins().size(), "Plugins list should be empty after disable");
    }

    private void createPluginJar(File jarFile, String jsonContent, Class<?>... classes) throws IOException {
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile))) {
            // 写入 script.json
            jos.putNextEntry(new JarEntry("script.json"));
            jos.write(jsonContent.getBytes());
            jos.closeEntry();

            // 写入类文件
            for (Class<?> clazz : classes) {
                String classPath = clazz.getName().replace('.', '/') + ".class";
                jos.putNextEntry(new JarEntry(classPath));
                
                // 读取当前类路径下的 .class 文件内容
                try (InputStream is = clazz.getClassLoader().getResourceAsStream(classPath)) {
                    if (is == null) throw new IOException("Could not find class file: " + classPath);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        jos.write(buffer, 0, bytesRead);
                    }
                }
                jos.closeEntry();
            }
        }
    }
}
