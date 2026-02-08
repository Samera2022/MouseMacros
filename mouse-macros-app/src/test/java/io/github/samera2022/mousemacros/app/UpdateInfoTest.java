package io.github.samera2022.mousemacros.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("版本管理系统测试")
public class UpdateInfoTest {

    @Test
    @DisplayName("获取所有版本")
    public void testGetAllVersions() {
        String[] versions = UpdateInfo.getAllVersions();
        assertNotNull(versions);
        assertTrue(versions.length > 0, "应该至少有一个版本");

        // 检查版本格式
        for (String version : versions) {
            assertTrue(version.matches("\\d+\\.\\d+\\.\\d+.*"),
                "版本应该符合语义化版本格式: " + version);
        }
    }

    @Test
    @DisplayName("获取最新版本")
    public void testGetLatestVersion() {
        String latest = UpdateInfo.getLatestVersion();
        assertNotNull(latest);
        assertNotEquals("0.0.0", latest);
    }

    @Test
    @DisplayName("获取所有版本显示名称")
    public void testGetAllDisplayNames() {
        String[] displayNames = UpdateInfo.getAllDisplayNames();
        assertNotNull(displayNames);
        assertTrue(displayNames.length > 0);

        // 显示名称应该包含日期
        for (String name : displayNames) {
            assertTrue(name.contains("["), "显示名称应该包含 [");
            assertTrue(name.contains("]"), "显示名称应该包含 ]");
        }
    }

    @Test
    @DisplayName("按版本查找")
    public void testFindByVersion() {
        String latest = UpdateInfo.getLatestVersion();
        UpdateInfo info = UpdateInfo.findByVersion(latest);
        assertNotNull(info);
        assertEquals(latest, info.getVersion());
    }

    @Test
    @DisplayName("按版本查找 - 不存在版本应抛异常")
    public void testFindByVersionNotFound() {
        assertThrows(IllegalArgumentException.class,
            () -> UpdateInfo.findByVersion("999.999.999"));
    }

    @Test
    @DisplayName("版本比较 - 简单版本")
    public void testCompareVersionsSimple() {
        // 1.0.0 < 2.0.0
        assertTrue(UpdateInfo.compareVersions("1.0.0", "2.0.0") < 0);
        // 2.0.0 > 1.0.0
        assertTrue(UpdateInfo.compareVersions("2.0.0", "1.0.0") > 0);
        // 1.0.0 == 1.0.0
        assertEquals(0, UpdateInfo.compareVersions("1.0.0", "1.0.0"));
    }

    @Test
    @DisplayName("版本比较 - 语义化版本")
    public void testCompareVersionsSemantic() {
        // 1.0.0 < 1.1.0 < 1.1.1
        assertTrue(UpdateInfo.compareVersions("1.0.0", "1.1.0") < 0);
        assertTrue(UpdateInfo.compareVersions("1.1.0", "1.1.1") < 0);
    }

    @Test
    @DisplayName("版本比较 - 预发行版本")
    public void testCompareVersionsPrerelease() {
        // 1.0.0 > 1.0.0-rc1
        assertTrue(UpdateInfo.compareVersions("1.0.0", "1.0.0-rc1") > 0);
        // 1.0.0-rc1 < 1.0.0-rc2
        assertTrue(UpdateInfo.compareVersions("1.0.0-rc1", "1.0.0-rc2") < 0);
    }

    @Test
    @DisplayName("版本比较 - isNewer方法")
    public void testIsNewer() {
        assertTrue(UpdateInfo.isNewer("2.0.0", "1.0.0"));
        assertFalse(UpdateInfo.isNewer("1.0.0", "2.0.0"));
        assertFalse(UpdateInfo.isNewer("1.0.0", "1.0.0"));
    }

    @Test
    @DisplayName("获取格式化版本日志")
    public void testGetFormattedLog() {
        String latest = UpdateInfo.getLatestVersion();
        UpdateInfo info = UpdateInfo.findByVersion(latest);
        String log = info.getFormattedLog();

        assertNotNull(log);
        assertTrue(log.contains(latest));
        assertTrue(log.contains(info.getReleaseDate()));
    }

    @Test
    @DisplayName("获取版本信息")
    public void testGetVersionInfo() {
        String latest = UpdateInfo.getLatestVersion();
        UpdateInfo info = UpdateInfo.findByVersion(latest);

        assertNotNull(info.getVersion());
        assertNotNull(info.getReleaseDate());
        assertNotNull(info.getDescription());
        assertNotNull(info.getDisplayName());
    }

    @Test
    @DisplayName("版本数组是否递增")
    public void testVersionsSorted() {
        String[] versions = UpdateInfo.getAllVersions();
        for (int i = 0; i < versions.length - 1; i++) {
            assertTrue(UpdateInfo.compareVersions(versions[i], versions[i + 1]) <= 0,
                "版本应该升序排列");
        }
    }
}

