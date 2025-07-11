package io.github.samera2022.mouse_macros;

import java.util.stream.Stream;

public enum UpdateInfo {
    // 定义条目：版本号 + 发布日期 + 描述
    VERSION_0_0_1("0.0.1", "2025-07-05 20:20",
            "##[Added]\n" +
                    " - 添加开始/停止录制鼠标宏功能\n" +
                    " - 添加保存/读取鼠标宏功能\n" +
                    " - 添加自定义热键功能\n" +
                    " - 添加本地化支持\n" +
                    " - 添加自适应窗体（随按钮长度与系统缩放设置而改变）"),
    VERSION_0_0_2("0.0.2", "2025-07-06 22:51",
            "##[Added]\n" +
                    " - 添加配置文件功能\n" +
                    " - 添加设置界面\n" +
                    " - 添加\"关于作者\"功能按钮\n" +
                    " - 添加\"更新日志\"功能按钮\n\n" +
                    "##[Changed]\n" +
                    " - 将自定义热键功能移入\"设置\"界面\n\n" +
                    "##[Detailed]\n" +
                    "#关于配置文件功能\n" +
                    "目前已添加以下可配置项：\n" +
                    "(boolean)跟随系统设置，(boolean)启用深色模式，(String)切换语言，(String)默认鼠标宏存储地址，[未实装，不可用](Map<String,String>)按键映射表，\n" +
                    "注意：\"跟随系统设置\"与(\"启用深色模式\", \"切换语言\")存在上位关系。如果选择了\"跟随系统设置\"，那么后二者就不再接受用户更改，直接读取系统的相应设置。"),
    VERSION_0_1_0("0.1.0","2025-07-08 15:08",
            "##[Added]\n" +
                    " - 实装配置文件的keyMap\n\n" +
                    "##[Changed]\n" +
                    " - 重构项目整体结构，降低代码耦合度"),
    VERSION_1_0_0("1.0.0","2025-07-09 21:31",
            "##[Added]\n" +
                    " - 添加对鼠标中键，鼠标滚轮和键盘的支持\n" +
                    " - 区分了鼠标左键右键和中键\n" +
                    " - 添加自定义宏设置\n" +
                    " - 添加中止宏按键功能\n" +
                    " - 添加保留上次存储/读取鼠标宏路径的功能\n\n" +
                    "##[Warn]\n" +
                    " - 在创建settings.custom_hotkey界面后，不点击任何对话框直接按键盘，将会直接改动start_record的按键设置。\n" +
                    " - JFileChooser的界面无法应用暗色模式……要重写这个类实在太费劲了。");

    private final String version;
    private final String releaseDate;
    private final String description;

    // 构造方法
    UpdateInfo(String version, String releaseDate, String description) {
        this.version = version;
        this.releaseDate = releaseDate;
        this.description = description;
    }

    public String getVersion() {return version;}
    public String getReleaseDate() {return releaseDate;}
    public String getDisplayName() {return String.format("[%s] %s",releaseDate,version);}
    public String getDescription() {return description;}

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

    // 新增：获取所有版本号的String数组
    public static String[] getAllVersions() {
        return Stream.of(values()).map(log -> log.version).toArray(String[]::new);
    }

    public static String[] getAllDisplayNames() {
        return Stream.of(values()).map(UpdateInfo::getDisplayName).toArray(String[]::new);
    }
}