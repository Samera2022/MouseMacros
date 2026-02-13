# MouseMacros API Quick Reference Card / 快速参考卡

## Current Available APIs / 现有可用 API

### 1. Script API (mm object)

```javascript
// Event Listening / 事件监听
mm.on(eventClassName, callback)
// mm.on('io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent', 
//       (event) => { /* handler */ })

// Context Access / 获取上下文
const context = mm.getContext()
// context.simulate(action)           - 模拟操作
// context.getPixelColor(x, y)        - 获取像素颜色
// context.showToast(title, msg)      - 显示通知
// context.getAppConfig()             - 获取配置

// Logging / 日志输出
mm.log(message)

// Cleanup / 资源清理
mm.cleanup()  // Auto-called when script is disabled
```

### 2. Configuration API (IConfig)

```javascript
const config = mm.getContext().getAppConfig()

config.getBoolean(key)    // 读取布尔值
config.getInt(key)        // 读取整数
config.getDouble(key)     // 读取浮点数
config.getString(key)     // 读取字符串
config.getKeyMap()        // 获取所有配置
```

---

## Available Events / 可用事件

### Application Lifecycle / 应用生命周期

```
OnAppLaunchedEvent
├─ getApplicationVersion()
└─ getJavaVersion()
```

### Recording Events / 录制事件

```
BeforeRecordStartEvent (Cancellable)
├─ getStartTime()
└─ getInitialMousePos()

AfterRecordStopEvent
└─ getActionsCount()

OnInputCapturedEvent (Cancellable)
├─ getInputType()      // 1=Press, 2=Release, 3=Wheel, 10=KeyPress, 11=KeyRelease
├─ getKeyCode()
├─ getX(), getY()
└─ getDelay()
```

### Playback Events / 回放事件

```
BeforePlaybackStartEvent (Cancellable)
├─ getMacroData()
└─ getRepeatCount()

BeforeStepExecuteEvent (Cancellable)
├─ getAction()
└─ getActionIndex()

AfterStepExecuteEvent
├─ getAction()
└─ getActionIndex()

OnLoopCompleteEvent
├─ getLoopNumber()
└─ getTotalLoops()

OnPlaybackAbortedEvent
```

### File Operations / 文件操作

```
OnMacroSaveEvent
├─ getMacroName()
└─ getFilePath()

OnMacroLoadEvent
├─ getMacroName()
└─ getFilePath()
```

### Configuration / 配置变化

```
OnConfigChangedEvent
├─ getChangedKey()
└─ getNewValue()

OnMenuInitEvent

OnActionAddedEvent
└─ getAction()

OnTooltipDisplayEvent (Cancellable)
├─ getTooltipText()
└─ setTooltipText()
```

---

## Recommended New APIs / 推荐的新 API

### Priority 1: Macro Management (v2.1.0)

```javascript
// mm.macro object (Recommended to add)
mm.macro.isRecording()           // 是否录制中
mm.macro.isPlaying()             // 是否播放中
mm.macro.isPaused()              // 是否暂停中
mm.macro.getActions()            // 获取操作列表
mm.macro.getActionsCount()       // 获取操作数量
mm.macro.getCurrentLoop()        // 当前循环号
mm.macro.getCurrentActionIndex() // 当前操作索引
mm.macro.play()                  // 播放
mm.macro.pause()                 // 暂停
mm.macro.resume()                // 恢复
mm.macro.abort()                 // 中止
mm.macro.clear()                 // 清空
mm.macro.startRecording()        // 开始录制
mm.macro.stopRecording()         // 停止录制
```

### Priority 1: System Information (v2.1.0)

```javascript
// mm.system object (Recommended to add)
mm.system.getScale()             // 获取DPI缩放[scaleX, scaleY]
mm.system.getVirtualOrigin()     // 获取虚拟原点 {x, y}
mm.system.normalizeToVirtualOrigin(x, y)      // 坐标转换
mm.system.denormalizeFromVirtualOrigin(x, y)  // 坐标反转
mm.system.getSystemLanguage()    // 获取系统语言
mm.system.isSystemDarkMode()     // 是否深色模式
mm.system.getOSName()            // 获取操作系统
mm.system.getJavaVersion()       // 获取Java版本
```

### Priority 2: Internationalization (v2.2.0)

```javascript
// mm.i18n object (Recommended to add)
mm.i18n.get(key)                 // 获取翻译字符串
mm.i18n.getSettings(key)         // 获取设置相关翻译
mm.i18n.hasKey(key)              // 检查键是否存在
mm.i18n.getCurrentLanguage()     // 获取当前语言
mm.i18n.getAvailableLanguages()  // 获取可用语言列表
mm.i18n.switchLanguage(lang)     // 切换语言
mm.i18n.reloadLanguage()         // 重新加载语言
```

### Priority 2: Clipboard (v2.2.0)

```javascript
// mm.clipboard object (Recommended to add)
mm.clipboard.getText()           // 获取文本
mm.clipboard.setText(text)       // 设置文本
mm.clipboard.getFiles()          // 获取文件列表
mm.clipboard.setFiles(files)     // 设置文件列表
```

### Priority 2: File Operations (v2.2.0)

```javascript
// mm.file object (Recommended to add)
mm.file.readText(path)           // 读取文本文件
mm.file.writeText(path, content) // 写入文本文件
mm.file.exists(path)             // 检查文件是否存在
mm.file.listDirectory(path)      // 列出目录内容
mm.file.createDirectory(path)    // 创建目录
mm.file.delete(path)             // 删除文件/目录
```

### Priority 3: Window Management (v2.3.0+)

```javascript
// mm.window object (Recommended for future)
mm.window.getActiveWindow()      // 获取活跃窗口
mm.window.getAllWindows()        // 获取所有窗口
mm.window.focusWindow(title)     // 焦点某个窗口
mm.window.getWindowByTitle(title) // 通过标题获取窗口
mm.window.getWindowByClass(className) // 通过类名获取窗口
```

### Priority 3: Network (v2.3.0+)

```javascript
// mm.http object (Recommended for future)
mm.http.get(url)                 // GET请求
mm.http.post(url, data)          // POST请求
mm.http.put(url, data)           // PUT请求
mm.http.delete(url)              // DELETE请求
mm.http.download(url, path)      // 下载文件
```

### Priority 3: Timers (v2.3.0+)

```javascript
// mm.timer object (Recommended for future)
mm.timer.setTimeout(fn, delay)   // 延迟执行
mm.timer.setInterval(fn, delay)  // 周期执行
mm.timer.clearInterval(id)       // 清除间隔
mm.timer.delay(ms)               // 延迟Promise
```

---

## Script Metadata Template / 脚本元数据模板

```javascript
// Required fields / 必需字段
const registry_name = 'my_script';
const display_name = 'My Script Name';
const version = '1.0.0';
const author = 'Your Name';
const description = 'What this script does';
const available_version = '1.0.0';

// Optional fields / 可选字段
const soft_dependencies = ['optional_script'];
const hard_dependencies = ['required_script'];
const requires_native_access = false;
const native_access_description = 'Why it needs access';
```

---

## Common Patterns / 常用模式

### Pattern 1: Event Monitoring / 事件监控

```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', 
    (event) => {
        mm.log('Playback starting with ' + event.getRepeatCount() + ' repeats');
    });
```

### Pattern 2: State Checking / 状态检查

```javascript
if (mm.macro.isPlaying()) {
    mm.log('Macro is playing');
} else if (mm.macro.isRecording()) {
    mm.log('Macro is recording');
} else {
    mm.log('Macro is idle');
}
```

### Pattern 3: Conditional Execution / 条件执行

```javascript
mm.on('BeforePlaybackStartEvent', (event) => {
    if (event.getRepeatCount() > 100) {
        mm.log('Warning: High repeat count');
        event.setCancelled(true);  // 取消事件
    }
});
```

### Pattern 4: Error Handling / 错误处理

```javascript
try {
    const scale = mm.system.getScale();
    if (scale[0] < 1.0) {
        mm.log('Warning: Scale less than 1.0');
    }
} catch (error) {
    mm.log('Error: ' + error.message);
}
```

### Pattern 5: Feature Detection / 功能检测

```javascript
if (typeof mm.macro !== 'undefined') {
    // Use mm.macro API
    const count = mm.macro.getActionsCount();
} else {
    // Fallback to event-based approach
    mm.log('mm.macro not available');
}
```

---

## Event Input Types / 输入类型常数

```javascript
const MOUSE_MOVE = 0;      // 鼠标移动
const MOUSE_PRESS = 1;     // 鼠标按下
const MOUSE_RELEASE = 2;   // 鼠标释放
const MOUSE_WHEEL = 3;     // 鼠标滚轮
const KEY_PRESS = 10;      // 键盘按下
const KEY_RELEASE = 11;    // 键盘释放
```

---

## Troubleshooting Quick Guide / 故障排除快速指南

| 问题 | 解决方案 |
|------|---------|
| 脚本不加载 | 检查元数据格式，检查文件名是否为.js |
| 事件不触发 | 验证事件类名，检查脚本是否启用 |
| 获取null | 使用try-catch包装，检查条件 |
| 权限被拒绝 | 检查requires_native_access设置，确认白名单 |
| 性能下降 | 减少日志输出，优化事件处理器 |

---

## Performance Tips / 性能建议

1. **Minimize Logging** / 减少日志输出
   ```javascript
   // ❌ 不好 - 每次都执行
   mm.on('BeforeStepExecuteEvent', (e) => {
       mm.log('Step ' + e.getActionIndex());
   });
   
   // ✅ 好 - 每10步记录一次
   mm.on('BeforeStepExecuteEvent', (e) => {
       if (e.getActionIndex() % 10 === 0) {
           mm.log('Step ' + e.getActionIndex());
       }
   });
   ```

2. **Avoid Heavy Processing in Event Handlers**
   ```javascript
   // ❌ 不好 - 复杂处理
   mm.on('AfterStepExecuteEvent', (e) => {
       // 复杂的数据处理...
   });
   
   // ✅ 好 - 简单的标记或计数
   var processed = 0;
   mm.on('AfterStepExecuteEvent', (e) => {
       processed++;
   });
   ```

3. **Cache Configuration Values**
   ```javascript
   // ❌ 不好 - 每次查询
   mm.on('BeforeStepExecuteEvent', (e) => {
       var quick = mm.getContext().getAppConfig().getBoolean('enable_quick_mode');
   });
   
   // ✅ 好 - 缓存一次
   var config = mm.getContext().getAppConfig();
   var quick = config.getBoolean('enable_quick_mode');
   mm.on('BeforeStepExecuteEvent', (e) => {
       // 使用cached的quick变量
   });
   ```

---

## Resources / 资源

- 完整开发指南：`docs/en/SCRIPT_DEVELOPMENT_GUIDE.md` or `docs/zh/SCRIPT_DEVELOPMENT_GUIDE.md`
- 扩展API参考：`docs/en/EXTENDED_API_REFERENCE.md` or `docs/zh/EXTENDED_API_REFERENCE.md`
- API分析报告：`docs/en/API_ANALYSIS_REPORT.md` or `docs/zh/API_ANALYSIS_REPORT.md`

---

**Version:** 1.0  
**Last Updated:** February 12, 2026  
**Compatible with:** MouseMacros v2.0.0+

