# MouseMacros API 分析总结报告

## 概述

通过详细分析 MouseMacros app 模块的源代码，我们发现了大量可以向脚本开发者开放的实用 API。本报告总结了现有 API、已实现但未开放的 API，以及推荐的未来 API。

---

## 一、已分析的模块结构

### 应用模块架构

```
mouse-macros-app/src/main/java/io/github/samera2022/mousemacros/app/
├── manager/                    # 业务逻辑管理器
│   ├── MacroManager           # 宏操作和播放控制 ✓ 部分已开放
│   ├── LogManager             # 日志管理
│   └── CacheManager           # 缓存管理
├── script/                     # 脚本管理
│   ├── ScriptAPI              # ✓ 已开放给脚本
│   ├── ScriptManager          # 脚本生命周期管理
│   ├── ScriptContext          # ✓ 部分已开放
│   ├── ScriptDescription      # 脚本元数据
│   └── ScriptPlugin           # 脚本插件封装
├── util/                       # 工具类
│   ├── ScreenUtil             # 屏幕坐标和缩放 ⚠ 可以开放
│   ├── SystemUtil             # 系统信息获取 ⚠ 可以开放
│   ├── OtherUtil              # 其他工具
│   ├── FileUtil               # 文件操作
│   └── ComponentUtil          # UI 组件工具
├── config/                     # 配置管理
│   ├── ConfigManager          # 配置读取
│   └── WhitelistManager       # 权限白名单
└── constant/                   # 常量定义
```

---

## 二、现有 API 总结

### 2.1 已开放的 API（通过 `mm` 对象）

#### 脚本 API (`IScriptAPI`)

**可用方法：**

| 方法 | 签名 | 用途 |
|------|------|------|
| `on()` | `on(String eventClassName, Consumer<Event> callback)` | 注册事件监听器 |
| `getContext()` | `getContext(): IScriptContext` | 获取脚本上下文 |
| `log()` | `log(String message)` | 输出日志 |
| `cleanup()` | `cleanup()` | 清理资源（自动调用） |

#### 脚本上下文 API (`IScriptContext`)

**可用方法：**

| 方法 | 签名 | 用途 |
|------|------|------|
| `simulate()` | `simulate(IMouseAction action)` | 模拟鼠标/键盘操作 |
| `getPixelColor()` | `getPixelColor(int x, int y): Color` | 获取屏幕像素颜色 |
| `showToast()` | `showToast(String title, String msg)` | 显示通知 |
| `getAppConfig()` | `getAppConfig(): IConfig` | 获取应用配置 |

#### 应用配置 API (`IConfig`)

**可用方法：**

| 方法 | 签名 | 用途 |
|------|------|------|
| `getBoolean()` | `getBoolean(String key): boolean` | 读取布尔配置 |
| `getInt()` | `getInt(String key): int` | 读取整数配置 |
| `getDouble()` | `getDouble(String key): double` | 读取浮点数配置 |
| `getString()` | `getString(String key): String` | 读取字符串配置 |
| `getKeyMap()` | `getKeyMap(): Map<String, String>` | 获取所有配置 |

#### 事件系统 (20+ 事件)

已完全记录在主开发指南中，包括：
- 应用生命周期事件
- 录制事件
- 回放事件
- 文件操作事件
- 配置变化事件

---

## 三、已实现但未充分开放的 API

### 3.1 MacroManager - 宏管理器

**当前实现：**

```java
// 状态查询（可访问）
public static boolean isRecording()      // 是否正在录制
public static boolean isPlaying()        // 是否正在播放
public static boolean isPaused()         // 是否已暂停

// 宏控制（可访问）
public static void startRecording()      // 开始录制
public static void stopRecording()       // 停止录制
public static void play()                // 播放
public static void pause()               // 暂停
public static void resume()              // 恢复
public static void abort()               // 中止

// 数据访问（部分可访问）
public static List<MouseAction> getActions()     // 获取操作列表
public static long getLastTime()                 // 获取上次操作时间
public static void recordAction()                // 记录操作

// 文件操作（UI 绑定）
public static void saveToFile()          // 保存到文件
public static void loadFromFile()        // 从文件加载
```

**建议：** 为脚本开放 `mm.macro` 对象，提供以下增强功能：

```javascript
mm.macro.isRecording()        // ✓ 已可通过事件检测
mm.macro.isPlaying()          // ✓ 已可通过事件检测
mm.macro.isPaused()           // ✓ 已可通过事件检测
mm.macro.getActions()         // ✓ 可通过事件获取
mm.macro.getActionsCount()    // 推荐添加
mm.macro.getCurrentLoop()     // 推荐添加
mm.macro.getCurrentActionIndex() // 推荐添加
mm.macro.getLastSaveDirectory()  // 推荐添加
```

### 3.2 ScreenUtil - 屏幕工具类

**当前实现：**

```java
// 多屏幕支持和 DPI 缩放处理
public static Point denormalizeFromVirtualOrigin(int x, int y)
public static Point normalizeToVirtualOrigin(int x, int y)
private static Point getVirtualOrigin()
```

**建议：** 通过 `mm.system` 对象开放

```javascript
mm.system.getScale()          // 获取 DPI 缩放因子
mm.system.getVirtualOrigin()  // 获取虚拟屏幕原点
mm.system.normalizeToVirtualOrigin(x, y)    // 坐标转换
mm.system.denormalizeFromVirtualOrigin(x, y) // 坐标转换
```

**实际应用：** 多屏幕和高 DPI 环境下的坐标处理

### 3.3 SystemUtil - 系统工具类

**当前实现：**

```java
// 获取系统 DPI 缩放
public static double[] getScale()

// 获取系统语言
public static String getSystemLang(String[] availableLangs)

// 检查深色模式（仅 Windows 10+）
public static boolean isSystemDarkMode()
```

**建议：** 通过 `mm.system` 对象开放

```javascript
mm.system.getScale()          // 获取缩放因子
mm.system.getSystemLanguage() // 获取系统语言
mm.system.isSystemDarkMode()  // 是否深色模式
mm.system.getOSName()         // 获取操作系统
mm.system.getJavaVersion()    // 获取 Java 版本
```

### 3.4 Localizer - 国际化支持

**当前实现：**

```java
// 加载语言文件和获取翻译
public static void load(String lang)
public static String get(String key)
public static boolean hasKey(String key)
private static Map<String, String> loadLanguageMap(String lang)
```

**建议：** 通过 `mm.i18n` 对象开放

```javascript
mm.i18n.get(key)              // 获取翻译字符串
mm.i18n.hasKey(key)           // 检查键是否存在
mm.i18n.getCurrentLanguage()  // 获取当前语言
mm.i18n.getAvailableLanguages() // 获取可用语言
mm.i18n.switchLanguage(lang)  // 切换语言（推荐）
```

---

## 四、推荐的新 API（优先级排列）

### 优先级 1（高）- v2.1.0

#### 1. Macro Management API
```javascript
mm.macro.getActionsCount()        // 获取操作数量
mm.macro.getCurrentLoop()         // 当前循环号
mm.macro.getCurrentActionIndex()  // 当前操作索引
mm.macro.getLastSaveDirectory()   // 上次保存目录
```

**用途：** 
- 脚本需要知道宏的详细执行状态
- 进度监控和条件执行

#### 2. System Information API
```javascript
mm.system.getScale()              // DPI 缩放
mm.system.isSystemDarkMode()      // 深色模式
mm.system.getSystemLanguage()     // 系统语言
mm.system.getOSName()             // 操作系统
mm.system.getJavaVersion()        // Java 版本
```

**用途：**
- 根据系统配置调整宏行为
- 处理多屏幕和高 DPI 环境
- 平台特定优化

### 优先级 2（中）- v2.2.0

#### 3. Internationalization API
```javascript
mm.i18n.get(key)                  // 获取翻译
mm.i18n.getCurrentLanguage()      // 当前语言
mm.i18n.getAvailableLanguages()   // 可用语言列表
```

**用途：**
- 多语言脚本开发
- 本地化日志和通知

#### 4. Clipboard API
```javascript
mm.clipboard.getText()            // 读取文本
mm.clipboard.setText(text)        // 设置文本
mm.clipboard.getFiles()           // 读取文件列表
```

**用途：**
- 与其他应用集成
- 自动复制/粘贴工作流

#### 5. File API
```javascript
mm.file.readText(path)            // 读取文本文件
mm.file.writeText(path, content)  // 写入文本文件
mm.file.exists(path)              // 检查文件是否存在
mm.file.listDirectory(path)       // 列出目录内容
```

**用途：**
- 从配置文件读取参数
- 输出执行结果和日志
- 脚本间的数据共享

### 优先级 3（低）- v2.3.0+

#### 6. Window Management API
```javascript
mm.window.getActiveWindow()       // 获取活跃窗口
mm.window.getAllWindows()         // 获取所有窗口
mm.window.focusWindow(title)      // 焦点某个窗口
```

**用途：**
- 针对特定应用的自动化
- 窗口状态检测

#### 7. Network API
```javascript
mm.http.get(url)                  // GET 请求
mm.http.post(url, data)           // POST 请求
mm.http.download(url, path)       // 下载文件
```

**用途：**
- 与在线服务集成
- 数据上传和同步

#### 8. Timer API
```javascript
mm.timer.setTimeout(fn, delay)    // 延迟执行
mm.timer.setInterval(fn, delay)   // 周期执行
mm.timer.delay(ms)                // 延迟 Promise
```

**用途：**
- 异步操作控制
- 定时任务

---

## 五、API 设计指导原则

### 5.1 安全性考虑

| API 类型 | 安全级别 | 说明 |
|---------|---------|------|
| 只读配置 | 高 | 已实现，安全 |
| 事件监听 | 高 | 已实现，安全 |
| 文件操作 | 中 | 需要权限控制 |
| 网络操作 | 低 | 需要明确许可 |
| 系统操作 | 低 | 需要原生访问批准 |

### 5.2 实现模式

**建议的对象结构：**

```javascript
// 核心对象
mm.on()           // 事件监听（现有）
mm.log()          // 日志输出（现有）
mm.getContext()   // 获取上下文（现有）

// 扩展对象（推荐）
mm.macro          // 宏管理
mm.system         // 系统信息
mm.i18n           // 国际化
mm.clipboard      // 剪贴板（需权限）
mm.file           // 文件操作（需权限）
mm.window         // 窗口管理（需权限）
mm.http           // 网络（需权限）
mm.timer          // 定时器
mm.perf           // 性能监控
```

### 5.3 错误处理

**推荐的错误处理模式：**

```javascript
// 1. 特性检测
if (typeof mm.macro !== 'undefined') {
    // 使用新 API
}

// 2. Try-Catch
try {
    const scale = mm.system.getScale();
} catch (error) {
    mm.log('Error: ' + error.message);
}

// 3. 回退机制
try {
    return mm.macro.getActionsCount();
} catch (e) {
    // 使用事件替代
}
```

---

## 六、实现成本评估

| API 模块 | 实现难度 | 代码行数 | 预计工时 |
|---------|---------|---------|---------|
| `mm.macro` | 低 | 200-300 | 2-4 小时 |
| `mm.system` | 低 | 100-150 | 1-2 小时 |
| `mm.i18n` | 低 | 50-100 | 1 小时 |
| `mm.clipboard` | 中 | 150-250 | 3-5 小时 |
| `mm.file` | 中 | 200-300 | 4-6 小时 |
| `mm.window` | 高 | 300-500 | 6-10 小时 |
| `mm.http` | 中 | 150-250 | 3-5 小时 |
| `mm.timer` | 低 | 100-150 | 1-2 小时 |

**总计（全部）：** 约 1100-2000 行代码，20-35 小时工时

---

## 七、安全和隐私考虑

### 7.1 权限模型

```
┌─────────────────────────────────┐
│    应用级 API（无需权限）         │
│  • 事件监听                       │
│  • 宏状态查询                     │
│  • 日志输出                       │
└─────────────────────────────────┘
        ↓
┌─────────────────────────────────┐
│    高权限 API（需明确许可）       │
│  • 文件操作                       │
│  • 网络操作                       │
│  • 剪贴板访问                     │
│  • 窗口操作                       │
└─────────────────────────────────┘
        ↓
┌─────────────────────────────────┐
│    系统级 API（需原生访问批准）   │
│  • 系统命令执行                   │
│  • 注册表修改                     │
│  • 进程操作                       │
└─────────────────────────────────┘
```

### 7.2 权限检查

**建议的权限检查代码：**

```java
// 在 ScriptAPI 中添加权限检查
public class ScriptAPI implements IScriptAPI {
    private final ScriptDescription description;
    
    public void allowFileAccess(Consumer<FileAPI> callback) {
        if (description.isRequiresNativeAccess() || 
            WhitelistManager.isWhitelisted(description)) {
            callback.accept(new FileAPI());
        } else {
            throw new SecurityException("File access not permitted");
        }
    }
}
```

---

## 八、使用示例和用例

### 用例 1：自适应宏执行

```javascript
// 根据系统配置自动调整宏
mm.on('BeforePlaybackStartEvent', (event) => {
    const scale = mm.system.getScale();
    const isDark = mm.system.isSystemDarkMode();
    
    if (scale[0] > 1.5) {
        mm.log('High DPI detected');
    }
    if (isDark) {
        mm.log('Dark mode enabled');
    }
});
```

### 用例 2：多语言日志记录

```javascript
// 使用系统语言记录日志
mm.on('OnLoopCompleteEvent', (event) => {
    const lang = mm.i18n.getCurrentLanguage();
    const msg = mm.i18n.get('log.loop_complete');
    mm.log(msg + ' ' + event.getLoopNumber());
});
```

### 用例 3：进度监控

```javascript
// 监控执行进度
mm.on('BeforeStepExecuteEvent', (event) => {
    const total = mm.macro.getActionsCount();
    const current = mm.macro.getCurrentActionIndex();
    const progress = Math.round((current / total) * 100);
    
    if (progress % 25 === 0) {
        mm.log('Progress: ' + progress + '%');
    }
});
```

---

## 九、建议和结论

### 主要建议

1. **立即实现（v2.1.0）**
   - `mm.macro` - 宏状态和控制接口
   - `mm.system` - 系统信息接口
   - 优先级高，风险低，收益大

2. **计划实现（v2.2.0）**
   - `mm.i18n` - 国际化支持
   - `mm.clipboard` - 剪贴板访问
   - `mm.file` - 文件操作（受限制）

3. **未来考虑（v2.3.0+）**
   - `mm.window` - 窗口管理
   - `mm.http` - 网络操作
   - 高风险，需充分的安全设计

### 关键安全建议

1. **权限白名单**
   - 维护已批准的脚本/作者列表
   - 定期审计权限使用

2. **沙箱隔离**
   - 限制文件系统访问范围
   - 限制网络访问域名

3. **审计日志**
   - 记录所有敏感操作
   - 提供用户可见的权限使用报告

4. **文档警告**
   - 清楚标注权限要求
   - 解释潜在安全风险

---

## 附录：参考资源

- **源文件位置**
  - ScriptAPI: `mouse-macros-app/src/main/java/.../script/ScriptAPI.java`
  - MacroManager: `mouse-macros-app/src/main/java/.../manager/MacroManager.java`
  - ScreenUtil: `mouse-macros-app/src/main/java/.../util/ScreenUtil.java`
  - SystemUtil: `mouse-macros-app/src/main/java/.../util/SystemUtil.java`
  - Localizer: `mouse-macros-app/src/main/java/.../Localizer.java`

- **相关文档**
  - SCRIPT_DEVELOPMENT_GUIDE.md - 主开发指南
  - EXTENDED_API_REFERENCE.md - 扩展 API 参考

---

**报告完成日期：** 2026 年 2 月 12 日  
**分析范围：** MouseMacros v2.0.0  
**分析深度：** 源代码级别  
**文档版本：** 1.0

