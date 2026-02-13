import java.util.stream.Stream;

public enum UpdateInfo {
    // 定义条目：版本号 + 发布日期 + 描述
    VERSION_0_0_1("0.0.1", "2025-07-05 20:20",
            "##[Added]\n" +
                     " - 添加开始/停止录制鼠标宏功能\n" +
                     " - 添加保存/读取鼠标宏功能\n" +
                     " - 添加自定义热键功能\n" +
                     " - 添加本地化支持\n " +
                     " - 添加自适应窗体（随按钮长度与系统缩放设置而改变）");

    private final String version;
    private final String releaseDate;
    private final String description;

    // 构造方法
    UpdateInfo(String version, String releaseDate, String description) {
        this.version = version;
        this.releaseDate = releaseDate;
        this.description = description;
    }

    // 自定义方法：获取格式化日志
    public String getFormattedLog() {
        return String.format("[%s] %s\n%s", releaseDate, version, description);
    }

    // 按版本搜索 (静态工具方法)
    public static UpdateInfo findByVersion(String version) {
        return Stream.of(values())
                .filter(log -> log.version.equals(version))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid version"));
    }
}