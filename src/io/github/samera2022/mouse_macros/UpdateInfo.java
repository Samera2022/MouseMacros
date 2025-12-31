package io.github.samera2022.mouse_macros;

import java.util.stream.Stream;

public enum UpdateInfo {
    // 定义条目：版本号 + 发布日期 + 描述
    VERSION_0_0_1("0.0.1", "2025-07-05 20:20",
            "## [Added]\n" +
                    " - 添加开始/停止录制鼠标宏功能\n" +
                    " - 添加保存/读取鼠标宏功能\n" +
                    " - 添加自定义热键功能\n" +
                    " - 添加本地化支持\n" +
                    " - 添加自适应窗体（随按钮长度与系统缩放设置而改变）"),
    VERSION_0_0_2("0.0.2", "2025-07-06 22:51",
            "## [Added]\n" +
                    " - 添加配置文件功能\n" +
                    " - 添加设置界面\n" +
                    " - 添加\"关于作者\"功能按钮\n" +
                    " - 添加\"更新日志\"功能按钮\n\n" +
                    "##[Changed]\n" +
                    " - 将自定义热键功能移入\"设置\"界面\n\n" +
                    "## [Detailed]\n" +
                    "# 关于配置文件功能\n" +
                    "目前已添加以下可配置项：\n" +
                    "(boolean)跟随系统设置，(boolean)启用深色模式，(String)切换语言，(String)默认鼠标宏存储地址，[未实装，不可用](Map<String,String>)按键映射表，\n" +
                    "注意：\"跟随系统设置\"与(\"启用深色模式\", \"切换语言\")存在上位关系。如果选择了\"跟随系统设置\"，那么后二者就不再接受用户更改，直接读取系统的相应设置。"),
    VERSION_0_1_0("0.1.0","2025-07-08 15:08",
            "## [Added]\n" +
                    " - 实装配置文件的keyMap\n\n" +
                    "## [Changed]\n" +
                    " - 重构项目整体结构，降低代码耦合度"),
    VERSION_1_0_0("1.0.0","2025-07-09 21:31",
            "## Added]\n" +
                    " - 添加对鼠标中键，鼠标滚轮和键盘的支持\n" +
                    " - 区分了鼠标左键右键和中键\n" +
                    " - 添加自定义宏设置\n" +
                    " - 添加中止宏按键功能\n" +
                    " - 添加保留上次存储/读取鼠标宏路径的功能\n\n" +
                    "## [Warn]\n" +
                    " - 在创建settings.custom_hotkey界面后，不点击任何对话框直接按键盘，将会直接改动start_record的按键设置。\n" +
                    " - JFileChooser的界面无法应用暗色模式……要重写这个类实在太费劲了。"),
    VERSION_1_0_1("1.0.1","2025-12-27 23:16",
            "## [Added]\n" +
                    " - 将窗体大小添加进cache.json，现在MouseMacros将能记忆你的每个窗体的大小。\n" +
                    " - 为Settings添加enable_default_storage的按钮，具体逻辑详见Description。\n" +
                    " - 配置了一键打包的脚本，现在的Release将会提供exe版本的程序下载。\n" +
                    " - 为settings.custom_hotkey界面的TextField添加了鼠标焦点提示。\n" +
                    " - 引入DeepWiki的docs来辅助代码理解。\n\n" +
                    "## [Changed]\n" +
                    " - 修改为更加细致的README.md。\n" +
                    " - 文件结构略有修改，主要是lang文件移动到src下。\n\n" +
                    "## [Description]\n" +
                    " - 当enable_default_storage为true时，在loadMacros和saveMacros时均只会采用config中的default_mmc_storage_path。" +
                    "且在FileChooser窗体中找到其他目录保存并不会修改cache.json中的lastSaveDirectory和lastLoadDirectory。\n" +
                    " - 当enable_default_storage为false时，在loadMacros和saveMacros时均只会采用cache.json中的lastSaveDirectory和lastLoadDirectory。" +
                    "当二者均为空时为默认目录，当二者其中一者为空时，在选择好文件夹（关闭FileChooser）后会默认将有值的数据复制到空的那一者。\n\n" +
                    "## [Fixed]\n" +
                    " - 修复了1.0.0中settings.custom_hotkey的异常。\n\n" +
                    "## [To-do]\n" +
                    " - 可能考虑切换UIManager的样式，当前的Metal样式较为简陋且缺乏较多的属性，可能会考虑切换至Nimbus、System或者第三方的FlatLightLaf。\n\n" +
                    "## [Note]\n" +
                    " - 源代码大概其实只有1MB左右的大小，但是带上精简的运行环境就需要34.2MB了……\n" +
                    " - 有人可能会说，为什么1.0.0的软件包要2.84MB，而这一次的只要1.05MB了呢？是不是压缩水平提升了？" +
                    "啊其实不是啊，是因为我之前不小心把项目的根目录放进源代码目录里面了……在我编写EVB和Jpackage打包的时候才发现这一点，然后才改过来。"),
    VERSION_1_0_2("1.0.2","2025-12-28 16:52",
            "## [Added]\n" +
                    " - 在SettingsDialog添加了QuickMode，现在MouseMacros可以无视鼠标键盘的等待时间，快速执行了。\n" +
                    " - 在MacroSettingsDialog的Enable Custom Macro Settings条目下添加了Execution Repeat Delay (s)，可以在每次重复的执行之中等待指定的秒数。秒数最大支持三位小数（到毫秒）。\n\n" +
                    "## [Fixed]\n" +
                    " - 1.0.0中不小心删除了Macro Settings的保存设置按钮，这次重新添加了进去。"),
    VERSION_1_0_3("1.0.3","2025-12-31 15:45",
            "这是2025年的最后一个release了呢\n\n" +
                    "## [Added]\n" +
                    " - 给每个窗体都添加上了压缩后的图标，使得程序窗体和任务栏图标不再使用Java的默认图标了。\n" +
                    " - 添加“最小化到系统托盘”的功能，在主窗体(MainFrame)关闭后会自动最小化到系统托盘。\n" +
                    " - 支持了更多的语言，包括西班牙语、法语、日语、韩语、俄语。\n\n" +
                    "## [Changed]\n" +
                    " - 修改了语言文件的部分键格式，使之条理更加清晰。\n\n" +
                    "## [Fixed]\n" +
                    " - 修正了Abort Operation的效果，使之可以在循环执行中正确地生效。"),
    VERSION_1_0_4("1.0.4","2026-01-01 0:00",
            "这是2026年新年的第一个release呢\n\n" +
                    "## [Added]\n" +
                    " - 添加了鼠标悬浮窗显示信息。\n\n" +
                    "## [Changed]\n" +
                    " - 修改了CacheManager的逻辑，使之和ConfigManager保持一致。");

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
        return String.format("[%s] %s\n\n%s", releaseDate, version, description);
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

    public static void main(String[] args) {
        System.out.println(VERSION_1_0_4.getFormattedLog());
    }
}