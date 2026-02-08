package io.github.samera2022.mousemacros.app.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("缓存管理系统测试")
public class CacheManagerTest {

    @BeforeEach
    public void setUp() {
        CacheManager.reloadCache();
    }

    @Test
    @DisplayName("获取缓存对象")
    public void testGetCache() {
        assertNotNull(CacheManager.cache);
    }

    @Test
    @DisplayName("缓存对象有效字段")
    public void testCacheFields() {
        CacheManager.Cache cache = CacheManager.cache;

        assertNotNull(cache.lastLoadDirectory);
        assertNotNull(cache.lastSaveDirectory);
        assertNotNull(cache.windowSizeMap);
        assertNotNull(cache.defaultCloseOperation);
    }

    @Test
    @DisplayName("窗口尺寸映射为空或包含有效数据")
    public void testWindowSizeMap() {
        // 初始状态可能为空

        // 添加窗口尺寸
        CacheManager.cache.windowSizeMap.put("test_frame", "800x600");
        assertEquals("800x600", CacheManager.cache.windowSizeMap.get("test_frame"));
    }

    @Test
    @DisplayName("设置最后加载目录")
    public void testLastLoadDirectory() {
        String testPath = "C:\\Users\\TestUser\\Documents";
        CacheManager.cache.lastLoadDirectory = testPath;

        assertEquals(testPath, CacheManager.cache.lastLoadDirectory);
    }

    @Test
    @DisplayName("设置最后保存目录")
    public void testLastSaveDirectory() {
        String testPath = "C:\\Users\\TestUser\\Documents\\Macros";
        CacheManager.cache.lastSaveDirectory = testPath;

        assertEquals(testPath, CacheManager.cache.lastSaveDirectory);
    }

    @Test
    @DisplayName("默认关闭操作常数")
    public void testCloseOperationConstants() {
        assertEquals("exit_on_close", CacheManager.EXIT_ON_CLOSE);
        assertEquals("minimize_to_tray", CacheManager.MINIMIZE_TO_TRAY);
        assertEquals("", CacheManager.UNKNOWN);
    }

    @Test
    @DisplayName("设置默认关闭操作")
    public void testDefaultCloseOperation() {
        CacheManager.cache.defaultCloseOperation = CacheManager.EXIT_ON_CLOSE;
        assertEquals(CacheManager.EXIT_ON_CLOSE, CacheManager.cache.defaultCloseOperation);

        CacheManager.cache.defaultCloseOperation = CacheManager.MINIMIZE_TO_TRAY;
        assertEquals(CacheManager.MINIMIZE_TO_TRAY, CacheManager.cache.defaultCloseOperation);
    }

    @Test
    @DisplayName("保存并重新加载缓存")
    public void testSaveAndReloadCache() {
        String testFrameName = "test_frame_unit";
        String testSize = "1024x768";

        CacheManager.cache.windowSizeMap.put(testFrameName, testSize);
        CacheManager.cache.lastLoadDirectory = "C:\\test\\path";

        CacheManager.saveCache();
        CacheManager.reloadCache();

        assertEquals(testSize, CacheManager.cache.windowSizeMap.get(testFrameName));
        assertEquals("C:\\test\\path", CacheManager.cache.lastLoadDirectory);
    }

    @Test
    @DisplayName("多次保存不会丢失数据")
    public void testMultipleSaves() {
        CacheManager.cache.windowSizeMap.put("window1", "640x480");
        CacheManager.saveCache();

        CacheManager.cache.windowSizeMap.put("window2", "800x600");
        CacheManager.saveCache();

        CacheManager.reloadCache();

        assertEquals("640x480", CacheManager.cache.windowSizeMap.get("window1"));
        assertEquals("800x600", CacheManager.cache.windowSizeMap.get("window2"));
    }

    @Test
    @DisplayName("缓存重载时初始化空缓存")
    public void testReloadCacheCreatesNewIfMissing() {
        CacheManager.cache = new CacheManager.Cache();
        CacheManager.cache.lastLoadDirectory = "test";
        CacheManager.reloadCache();

        // 重载后应该有有效的缓存对象
        assertNotNull(CacheManager.cache);
        assertNotNull(CacheManager.cache.windowSizeMap);
    }

    @Test
    @DisplayName("清空窗口尺寸映射")
    public void testClearWindowSizeMap() {
        CacheManager.cache.windowSizeMap.put("window1", "640x480");
        CacheManager.cache.windowSizeMap.put("window2", "800x600");

        CacheManager.cache.windowSizeMap.clear();

        assertTrue(CacheManager.cache.windowSizeMap.isEmpty());
    }

    @Test
    @DisplayName("缓存对象可序列化")
    public void testCacheSerializable() {
        CacheManager.Cache cache = new CacheManager.Cache();
        cache.lastLoadDirectory = "C:\\path";
        cache.lastSaveDirectory = "C:\\path\\macros";
        cache.defaultCloseOperation = CacheManager.EXIT_ON_CLOSE;
        cache.windowSizeMap.put("main_frame", "800x600");

        // 保存测试缓存
        CacheManager.cache = cache;
        CacheManager.saveCache();
        CacheManager.reloadCache();

        // 验证数据完整性
        assertEquals("C:\\path", CacheManager.cache.lastLoadDirectory);
        assertEquals("C:\\path\\macros", CacheManager.cache.lastSaveDirectory);
        assertEquals("800x600", CacheManager.cache.windowSizeMap.get("main_frame"));
    }
}

