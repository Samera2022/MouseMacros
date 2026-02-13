# MouseMacros 扩展 API 参考指南

本文档补充了脚本开发指南，列出了可以向脚本开放的额外实用API接口。

## 目录

1. [宏管理 API](#宏管理-api)
2. [系统和屏幕 API](#系统和屏幕-api)
3. [国际化和本地化 API](#国际化和本地化-api)
4. [推荐的未来开放接口](#推荐的未来开放接口)

---

## 宏管理 API

### `mm.macro` 对象（推荐添加）

提供对当前宏状态和操作的访问。

#### 属性和方法

**状态查询：**

```javascript
// 获取当前宏的操作列表
const actions = mm.macro.getActions();
console.log('Total actions: ' + actions.length);

// 获取当前宏的循环信息（在播放时有效）
const currentLoop = mm.macro.getCurrentLoop();
const currentActionIndex = mm.macro.getCurrentActionIndex();
mm.log('Playing loop ' + (currentLoop + 1) + ', action ' + currentActionIndex);

// 查询宏状态
if (mm.macro.isRecording()) {
    mm.log('Recording in progress');
}

if (mm.macro.isPlaying()) {
    mm.log('Playback in progress');
}

if (mm.macro.isPaused()) {
    mm.log('Playback is paused');
}
```

**宏控制（当前已有）：**

```javascript
// 开始录制
mm.macro.startRecording();

// 停止录制
mm.macro.stopRecording();

// 播放宏
mm.macro.play();

// 暂停播放
mm.macro.pause();

// 恢复播放
mm.macro.resume();

// 中止播放
mm.macro.abort();

// 清空操作列表
mm.macro.clear();
```

**宏数据访问：**

```javascript
// 获取宏中的所有操作
const actions = mm.macro.getActions();
for (var i = 0; i < actions.length; i++) {
    const action = actions[i];
    mm.log('Action ' + i + ': type=' + action.getType() + 
            ', x=' + action.getX() + ', y=' + action.getY());
}

// 获取操作统计
mm.log('Total action count: ' + mm.macro.getActionsCount());

// 获取上次操作的时间戳
const lastActionTime = mm.macro.getLastTime();
```

**宏文件操作（推荐添加）：**

```javascript
// 保存宏到文件（需要用户在 UI 中选择位置）
mm.macro.saveToFile();

// 从文件加载宏
mm.macro.loadFromFile();

// 获取最后一次保存的文件路径
const lastFilePath = mm.macro.getLastSaveDirectory();
```

### 示例：动态宏修改

```javascript
const registry_name = 'macro_modifier';
const display_name = 'Macro Modifier';
const version = '1.0.0';
const author = 'Developer';
const description = 'Modifies macros before playback';
const available_version = '1.0.0';

mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    var actions = event.getMacroData();
    
    // 分析宏
    mm.log('Macro has ' + actions.length + ' actions');
    mm.log('Will repeat ' + event.getRepeatCount() + ' times');
    
    // 检查宏长度
    if (actions.length > 1000) {
        mm.log('Warning: Macro is very long (' + actions.length + ' actions)');
    }
});
```

---

## 系统和屏幕 API

### `mm.system` 对象（推荐添加）

提供对系统信息和屏幕配置的访问。

#### 屏幕坐标和缩放

```javascript
// 获取 DPI 缩放因子
const scale = mm.system.getScale();
mm.log('Screen scale: X=' + scale[0] + ', Y=' + scale[1]);

// 获取虚拟屏幕原点（用于多屏幕设置）
const origin = mm.system.getVirtualOrigin();
mm.log('Virtual origin: (' + origin.x + ', ' + origin.y + ')');

// 坐标转换
const normalizedPos = mm.system.normalizeToVirtualOrigin(1920, 1080);
mm.log('Normalized position: (' + normalizedPos.x + ', ' + normalizedPos.y + ')');

const robotPos = mm.system.denormalizeFromVirtualOrigin(100, 100);
mm.log('Robot coordinates: (' + robotPos.x + ', ' + robotPos.y + ')');
```

#### 系统信息

```javascript
// 获取系统语言
const systemLang = mm.system.getSystemLanguage();
mm.log('System language: ' + systemLang);

// 检查深色模式
const isDarkMode = mm.system.isSystemDarkMode();
mm.log('Dark mode enabled: ' + isDarkMode);

// 获取操作系统
const osName = mm.system.getOSName();
mm.log('Operating system: ' + osName);

// 获取 Java 版本
const javaVersion = mm.system.getJavaVersion();
mm.log('Java version: ' + javaVersion);
```

### 示例：屏幕感知的宏执行

```javascript
const registry_name = 'screen_aware_executor';
const display_name = 'Screen Aware Executor';
const version = '1.0.0';
const author = 'Developer';
const description = 'Adjusts macro based on screen configuration';
const available_version = '1.0.0';

mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    const scale = mm.system.getScale();
    
    // 根据缩放因子调整延迟
    if (scale[0] > 1.5 || scale[1] > 1.5) {
        mm.log('High DPI detected, increasing delays');
        // 可以修改操作的延迟
    }
    
    // 检查黑暗模式以调整颜色检测
    if (mm.system.isSystemDarkMode()) {
        mm.log('Dark mode detected, using dark theme colors');
    }
});
```

---

## 国际化和本地化 API

### `mm.i18n` 对象（推荐添加）

提供对应用本地化字符串的访问。

#### 字符串本地化

```javascript
// 获取本地化字符串
const startText = mm.i18n.get('main_frame.start_recording');
mm.log('Start button text: ' + startText);

// 获取设置相关的本地化字符串
const darkModeText = mm.i18n.getSettings('dark_mode');
mm.log('Dark mode label: ' + darkModeText);

// 检查是否存在某个键
if (mm.i18n.hasKey('custom_key')) {
    mm.log(mm.i18n.get('custom_key'));
}

// 获取当前语言
const currentLang = mm.i18n.getCurrentLanguage();
mm.log('Current language: ' + currentLang);

// 获取可用语言列表
const availableLangs = mm.i18n.getAvailableLanguages();
mm.log('Available languages: ' + availableLangs.join(', '));
```

#### 动态语言切换

```javascript
// 切换语言（推荐添加）
mm.i18n.switchLanguage('en_us');
mm.log('Language switched to: en_us');

// 重新加载语言文件
mm.i18n.reloadLanguage();
```

### 示例：多语言脚本

```javascript
const registry_name = 'multilingual_logger';
const display_name = 'Multilingual Logger';
const version = '1.0.0';
const author = 'Developer';
const description = 'Logs events in system language';
const available_version = '1.0.0';

mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    var playText = mm.i18n.get('log.start_playback');
    var repeatTimes = event.getRepeatCount();
    mm.log(playText + ' (x' + repeatTimes + ')');
    
    // 根据语言调整日志格式
    var currentLang = mm.i18n.getCurrentLanguage();
    if (currentLang.startsWith('zh')) {
        mm.log('中文用户: 即将开始回放');
    } else if (currentLang.startsWith('en')) {
        mm.log('English user: Starting playback');
    }
});
```

---

## 推荐的未来开放接口

### 1. 剪贴板 API

```javascript
// 获取剪贴板内容
const clipboardText = mm.clipboard.getText();

// 设置剪贴板内容
mm.clipboard.setText('some text');

// 获取剪贴板中的文件列表
const files = mm.clipboard.getFiles();
```

### 2. 文件 API

```javascript
// 读取文件
const content = mm.file.readText('/path/to/file.txt');

// 写入文件
mm.file.writeText('/path/to/file.txt', 'content');

// 列出目录内容
const files = mm.file.listDirectory('/path/to/dir');

// 检查文件是否存在
if (mm.file.exists('/path/to/file.txt')) {
    mm.log('File exists');
}
```

### 3. 网络 API

```javascript
// 发送 HTTP 请求
const response = mm.http.get('https://api.example.com/data');
mm.log('Response: ' + response.status + ' ' + response.body);

// POST 请求
const result = mm.http.post('https://api.example.com/data', 
    { key: 'value' });
```

### 4. 时间和定时器 API

```javascript
// 设置超时
mm.timer.setTimeout(() => {
    mm.log('Timeout executed');
}, 1000);

// 设置间隔
const intervalId = mm.timer.setInterval(() => {
    mm.log('Interval executed');
}, 2000);

// 清除间隔
mm.timer.clearInterval(intervalId);

// 延迟执行（async/await 风格）
mm.timer.delay(1000).then(() => {
    mm.log('1 second passed');
});
```

### 5. 窗口和 UI API

```javascript
// 获取活跃窗口信息
const activeWindow = mm.window.getActiveWindow();
mm.log('Active window title: ' + activeWindow.getTitle());
mm.log('Active window class: ' + activeWindow.getClassName());

// 获取窗口列表
const windows = mm.window.getAllWindows();
mm.log('Total windows: ' + windows.length);

// 焦点某个窗口
mm.window.focusWindow('notepad.exe');

// 获取窗口位置和大小
const rect = activeWindow.getRect();
mm.log('Window position: (' + rect.x + ', ' + rect.y + ')');
mm.log('Window size: ' + rect.width + ' x ' + rect.height);
```

### 6. 性能监控 API

```javascript
// 开始性能测量
mm.perf.startMeasure('my_operation');

// 执行操作...

// 停止测量并获取耗时
const elapsed = mm.perf.endMeasure('my_operation');
mm.log('Operation took ' + elapsed + ' ms');

// 获取内存使用情况
const memInfo = mm.perf.getMemoryInfo();
mm.log('Memory used: ' + memInfo.usedMB + ' MB');
```

---

## 现有但可增强的 API

### 增强 `ScriptContext` 的建议

当前 `mm.getContext()` 返回的对象提供：

```javascript
const context = mm.getContext();

// 已有的方法
context.simulate(action);           // 模拟鼠标/键盘操作
context.getPixelColor(x, y);        // 获取像素颜色
context.showToast(title, msg);      // 显示通知
context.getAppConfig();              // 获取应用配置
```

**建议增强项：**

```javascript
// 获取鼠标位置
const mousePos = context.getMousePosition();
mm.log('Mouse at: (' + mousePos.x + ', ' + mousePos.y + ')');

// 获取活跃窗口
const windowInfo = context.getActiveWindowInfo();
mm.log('Window: ' + windowInfo.title);

// 获取屏幕尺寸
const screenSize = context.getScreenSize();
mm.log('Screen: ' + screenSize.width + 'x' + screenSize.height);

// 设置剪贴板
context.setClipboardText('text');

// 播放声音
context.playSound('notification.wav');
```

---

## 最佳实践

### 1. 功能检测

```javascript
// 检查 API 是否可用
if (typeof mm.macro !== 'undefined') {
    mm.log('Macro API available');
    mm.macro.play();
} else {
    mm.log('Macro API not available');
}
```

### 2. 优雅降级

```javascript
// 尝试使用新 API，回退到事件
try {
    const actions = mm.macro.getActions();
} catch (e) {
    mm.log('Using event-based approach instead');
    mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', 
        (event) => {
            const actions = event.getMacroData();
        });
}
```

### 3. 错误处理

```javascript
try {
    const scale = mm.system.getScale();
    if (scale[0] < 1.0) {
        mm.log('Warning: Scale is less than 1.0');
    }
} catch (error) {
    mm.log('Error getting system scale: ' + error.message);
}
```

---

## API 实现路线图

| API | 优先级 | 状态 | 计划版本 |
|-----|--------|------|---------|
| `mm.macro.*` | 高 | 规划中 | 2.1.0 |
| `mm.system.*` | 高 | 规划中 | 2.1.0 |
| `mm.i18n.*` | 中 | 规划中 | 2.2.0 |
| `mm.clipboard.*` | 中 | 规划中 | 2.2.0 |
| `mm.file.*` | 中 | 规划中 | 2.2.0 |
| `mm.window.*` | 低 | 研究中 | 2.3.0 |
| `mm.http.*` | 低 | 研究中 | 2.3.0 |
| `mm.timer.*` | 低 | 研究中 | 2.3.0 |

---

## 总结

MouseMacros 的脚本 API 设计灵活且可扩展。通过逐步添加上述建议的接口，可以为开发者提供强大的工具来创建复杂的自动化脚本，同时保持安全性和稳定性。

每个新 API 的引入都应该：
1. 有明确的用途和用例
2. 提供清晰的错误处理机制
3. 包含完整的文档和示例
4. 在生产环境中充分测试

---

**创建日期：** 2026 年 2 月  
**版本：** 1.0  
**最后更新：** 2026 年 2 月 12 日

