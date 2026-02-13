# MouseMacros

<p align="center">
  <img src="https://raw.githubusercontent.com/Samera2022/MouseMacros/main/docs/images/MouseMacrosIcon.png" alt="MouseMacros Logo" width="120">
  <br>
  <b>一款轻量级、跨平台的 Java 工具，用于录制和回放鼠标及键盘宏。</b>
  <br>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-1.8%2B-orange.svg" alt="Java 版本">
  <img src="https://img.shields.io/badge/License-GPL--3.0-blue.svg" alt="许可证">
  <img src="https://img.shields.io/github/v/release/Samera2022/MouseMacros" alt="最新发布">
  <img src="https://img.shields.io/github/v/release/Samera2022/MouseMacros?include_prereleases&label=pre-release&color=orange" alt="预发布版本">
  <br>
  <img src="https://img.shields.io/github/actions/workflow/status/Samera2022/MouseMacros/release.yml?label=CI/CD" alt="构建状态">
  <a href="https://deepwiki.com/Samera2022/MouseMacros"><img src="https://deepwiki.com/badge.svg" alt="询问 DeepWiki"></a>
</p>

<div align="center">

| <sub>EN</sub> [English](../../README.md) | <sub>ZH</sub> [中文](README_ZH_CN.md) |
|---------------------------------------|---------------------------------------|

</div>

## 预览
<p align="center">
  <img src="../images/MouseMacrosMainFrame.png" width="400" alt="主界面">
<br>
  <sub style="font-size: 14px;"><i>MouseMacros 主界面展示</i></sub>
</p>

## 功能特性

* **全方位录制**：无缝捕获鼠标左/中/右键点击、滚轮滚动以及键盘输入。
* **全局热键**：即使应用最小化也能进行控制。支持完全自定义以下功能键：
    * 开始/停止录制
    * 播放宏
    * 终止操作（针对失控宏的紧急停止）
* **多语言支持**：内置 **英语 (美国)** 和 **简体中文** 本地化，同时支持日语、俄语、韩语、西班牙语和法语。
* **主题引擎**：支持 **浅色** 和 **深色** 模式，并可选择自动跟随系统设置。
* **持久化**：宏以 `.mmc` (CSV 格式) 文件保存，方便分享和手动编辑。
* **智能记忆**：自动记住窗口大小、上次使用的目录以及跨会话的自定义配置。
* **悬浮提示**：在光标附近显示实用的操作说明和提示，方便操作。
* **强大的脚本引擎**：通过 JavaScript 扩展功能，实现自定义逻辑、事件处理等。

## 安全性与二进制完整性
为了确保 Windows 二进制文件的安全性和真实性，MouseMacros 目前正在接入 SignPath Foundation 以获取免费的代码签名。
- **状态**：申请中 / 集成挂起。
- **未来发布**：一旦获得批准，所有的 Windows 安装程序 (.msi) 和可执行文件 (.exe) 都将由 SignPath Foundation 进行数字签名。
  ![alt text](https://img.shields.io/badge/代码签名-SignPath.io-blue)

## 快速入门

### 快速启动
**I. Jar 用户**
1. 请确保已安装 JRE 1.8 或更高版本。如果没有，可以从[此处](https://www.oracle.com/technetwork/cn/java/javase/downloads/jre8-downloads-2133155-zhs.html)下载。
2. 从 [Releases](https://github.com/Samera2022/MouseMacros/releases) 页面下载最新的 `.jar` 文件。
3. 双击 jar 文件，或使用命令行运行应用：
    ```bash
    java -jar MouseMacros.jar
    ```
**II. Exe 用户**
1. 从 [Releases](https://github.com/Samera2022/MouseMacros/releases) 页面下载最新的 `.exe` 文件。
2. 点击即可启动！所有环境已集成到单个 `exe` 文件中！

### 使用方法
<p align="center">
  <img src="../images/MouseMacrosSettingsDialog.png" width="400" alt="设置对话框">
</p>

1. **调整**：语言的选择将决定界面中的文字，从而导致某些按钮可能无法完整显示。在这种情况下，您需要将窗口调整至合适的尺寸。
2. **配置**：打开“设置”对话框和“宏设置”对话框来设置您偏好的热键。详细的配置文档请参考 [配置](#配置) 章节。
3. **录制**：按下您的“开始录制”热键或点击界面中的对应按钮，然后执行操作。
4. **保存**：使用“保存宏”将录制内容导出为 `.mmc` 文件。
5. **回放**：使用“加载宏”读取 `.mmc` 文件并按下“播放宏”。

## 配置

应用程序将设置存储在用户的 AppData 目录中：
`%USERPROFILE%/AppData/MouseMacros/`

| 文件 | 描述 |
|:-------------|:------------------------------------------------------------------------|
| `config.cfg` | 存储 UI 语言、主题模式、按键映射和默认存储路径。 |
| `cache.json` | 存储最近的文件路径和窗口尺寸。 |
| `white_list.json` | 存储用户批准的、需要原生访问权限的脚本和作者。 |

### 设置对话框选项
| 名称 | 键名 | 描述 |
|:---------------------------------|:--------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 跟随系统设置 | `followSystemSettings`(boolean) | 控制是否跟随系统默认设置。 |
| 切换语言 | `lang`(String) | 若 `followSystemSettings` 为 false，可通过此组合框选择其他显示语言。 |
| 启用深色模式 | `enableDarkMode`(boolean) | 若 `followSystemSettings` 为 false，可通过此复选框选择是否开启深色模式。 |
| 启用默认存储 | `enableDefaultStorage`(boolean) | 控制是否启用 `defaultMmcStoragePath`。若为真，`cache.json` 中的最近保存/加载目录将被忽略。每次打开文件选择对话框时，都会自动定位到该默认路径。 |
| 默认 MouseMacros 存储路径 | `defaultMmcStoragePath`(String) | 当启用默认存储时，此项决定默认文件夹。若文件夹不存在，应用将尝试创建，否则将打开默认的“用户文档”文件夹。 |
| 启用快速模式 | `enableQuickMode`(boolean) | 控制是否启用“无延迟模式”。在此模式下，应用将忽略动作间的等待时间。此模式具有危险性，强烈建议在启用前设置好 **终止操作** 热键和 **重复延迟**。 |
| 允许长提示文本 | `allowLongStr`(boolean) | 控制是否开启长提示显示。若为 false，提示将限制在给定宽度内；否则将尝试单行显示，除非超过窗口宽度。 |
| 窗口重调模式 | `readjustFrameMode`(String) | 控制在无缓存时以 3:2 比例显示窗口的模式。若存在缓存，在更改语言后，应用将在处理历史尺寸与推荐尺寸时从三种模式中选择。 |

### 宏设置对话框选项
| 名称 | 键名 | 描述 |
|:---------------------------------|:-------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 启用自定义宏设置 | `enableCustomMacroSettings`(boolean) | 控制是否开启自定义宏设置。 |
| 执行重复次数 | `repeatTime`(int) | 若启用，应用将按给定次数自动重复执行宏。 |
| 重复延迟 (秒) | `repeatDelay`(double) | 若启用，应用将在下次执行前推迟给定时间。最高支持三位小数（精确到毫秒）。 |

## 🔌 通过脚本扩展

MouseMacros 拥有一个由 GraalVM 驱动的强大脚本系统，允许您使用 JavaScript 来扩展其功能。您可以监听应用程序事件、与核心功能交互以及创建自定义逻辑。

### 工作原理

1.  **创建脚本**：编写一个 `.js` 文件，并将其放入您的 MouseMacros 配置目录下的 `scripts` 文件夹中 (`%USERPROFILE%/AppData/MouseMacros/scripts`)。
2.  **定义元数据**：在脚本顶部，定义全局变量以提供元数据。这对于应用程序正确管理您的脚本至关重要。

    ```javascript
    // ==UserScript==
    var display_name = "我的超棒脚本";
    var register_name = "my_awesome_script"; // 一个唯一的、小写蛇形命名的标识符
    var author = "你的名字"; // 仅支持单一作者
    var version = "1.0.0";
    var description = "这个脚本能做一些很棒的事。";
    var available_version = "2.0.0~2.1.*"; // 兼容的 MouseMacros 版本，支持通配符写法和区间写法
    var hard_dependencies = ["another_script_name"]; // 必须启用的脚本
    var soft_dependencies = []; // 可选脚本
    var requireNativeAccess = false; // 如果需要高级（更高权限）的功能，则需将其设置为true
    var requireNativeAccessDescription = "..."; // 向用户解释为什么该脚本需要高级权限
    // ==/UserScript==
    ```

3.  **编写代码**：使用全局 `mm` 对象与应用程序进行交互。

### 安全性与原生访问

为安全起见，脚本在权限有限的沙箱环境中运行。但是，某些脚本可能需要“原生访问”权限来执行高级任务（例如，文件 I/O、运行外部进程）。

-   **请求访问**：要请求原生访问权限，请将以下元数据添加到您的脚本中：
    ```javascript
    var requireNativeAccess = true;
    var requireNativeAccessDescription = "此脚本需要读/写文件才能正常工作。";
    ```
-   **用户批准**：当首次加载需要原生访问权限的脚本时，它**默认是禁用的**。用户必须通过 `设置 > 脚本管理器` 手动启用它，届时会显示一个安全警告。
-   **白名单**：批准后，用户可以选择将特定脚本或脚本作者加入白名单，该信息记录在 `white_list.json` 中。已加入白名单的脚本/作者将来会自动获得原生访问权限。

### 脚本 API 快速参考

API 通过全局 `mm` 对象公开。

#### `mm` 对象

| 方法 | 描述 |
| :----------------------------------- | :------------------------------------------------------------------------------------------------------ |
| `on(eventClassName, callback)` | 为特定应用程序事件注册一个监听器。第一个参数是事件的完整 Java 类名。 |
| `log(message)` | 将消息打印到应用程序的日志控制台。 |
| `getContext()` | 返回 `ScriptContext` 对象以进行更高级的交互。 |
| `cleanup()` | 注销脚本创建的所有事件监听器。在脚本被禁用时自动调用。 |

#### `mm.getContext()` 对象

| 方法 | 描述 |
| :------------------ | :----------------------------------------------------------------------- |
| `simulate(action)` | 模拟一个鼠标动作。（尚未完全实现） |
| `getPixelColor(x,y)`| 获取指定屏幕坐标处像素的颜色。（尚未完全实现） |
| `showToast(t, m)` | 显示一个浮动通知。（尚未完全实现） |
| `getAppConfig()` | 返回一个 `IConfig` 对象以读取应用程序设置（`getBoolean`、`getInt`、`getString` 等）。 |

### 示例脚本

此脚本在应用程序启动和宏开始录制时向控制台记录一条消息。

```javascript
// ==UserScript==
var display_name = "你好世界脚本";
var register_name = "hello_world";
var author = "脚本开发者";
var version = "1.0.0";
var description = "一个简单的示例脚本。";
var available_version = "*"; // 兼容所有版本
// ==/UserScript==

// 监听应用程序启动事件
mm.on('io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent', function(event) {
    mm.log("来自'你好世界脚本'的问候！");
    mm.log("应用版本: " + event.getAppVersion());
});

// 监听录制开始前的事件
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforeRecordStartEvent', function(event) {
    mm.log("录制即将在 " + event.getStartTime() + " 开始");
});
```

## 开发文档

### 本地文档

有关深入信息，请参阅以下本地文档：

*   [脚本开发指南](./SCRIPT_DEVELOPMENT_GUIDE.md) - 编写和管理 JavaScript 脚本的综合指南。
*   [扩展 API 参考](./EXTENDED_API_REFERENCE.md) - MouseMacros API 的详细参考。
*   [API 分析报告](./API_ANALYSIS_REPORT.md) - 关于 API 设计和实现的见解。
*   [开发 FAQ](./FAQ_ZH_CN.md) - 关于开发、版本控制和 CI/CD 的常见问题解答。

### 外部资源

*   由 DeepWiki 生成的详细文档可在 [GitHub Wiki](https://github.com/Samera2022/MouseMacros/wiki) 查看。由于该文档是由作者从 DeepWiki 手动汇编的，可能存在滞后性。
*   如需查看最新的文档，请参考 [Samera2022/MouseMacros | DeepWiki](https://deepwiki.com/Samera2022/MouseMacros) 或点击页面顶部的徽章。该网站每周更新本项目文档，并提供“Refresh this wiki”按钮及邮件输入框，以便在未索引时强制更新。

## 其他

### 贡献
欢迎贡献代码！如果您发现 Bug 或有功能建议，请提交 Issue。
### 作者
**开发者: Samera2022**
* **GitHub**: [@Samera2022](https://github.com/Samera2022)
### 许可证
本项目采用 GNU General Public License v3.0 许可证 - 详见 `LICENSE` 文件。