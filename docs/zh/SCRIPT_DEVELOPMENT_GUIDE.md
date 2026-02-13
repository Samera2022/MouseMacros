# MouseMacros JavaScript 脚本开发指南

欢迎来到 MouseMacros 脚本开发指南！本指南将帮助您创建强大的 JavaScript 脚本来扩展 MouseMacros 的功能。

## 目录

1. [概述](#概述)
2. [脚本结构](#脚本结构)
3. [元数据声明](#元数据声明)
4. [脚本 API 参考](#脚本-api-参考)
5. [事件系统](#事件系统)
6. [可用事件](#可用事件)
7. [脚本依赖](#脚本依赖)
8. [原生访问](#原生访问)
9. [错误处理](#错误处理)
10. [最佳实践](#最佳实践)
11. [示例](#示例)

## 概述

MouseMacros 通过 GraalVM 的 Polyglot 引擎支持 JavaScript 脚本。脚本可以：

- 监听并响应应用事件
- 模拟鼠标和键盘操作
- 访问应用配置
- 捕获屏幕像素颜色
- 向用户显示通知
- 依赖其他脚本并进行完整的依赖管理
- 在用户许可的情况下，获取原生访问权限以执行高级任务

## 脚本结构

每个脚本必须包含：

1. **元数据声明** - 脚本的信息
2. **代码逻辑** - 事件处理器和功能实现
3. **可选的初始化** - 脚本加载时运行的设置代码

### 最小脚本示例

```javascript
const registry_name = 'my_script';
const display_name = '我的脚本';
const version = '1.0.0';
const author = '你的名字';
const description = '我的脚本是做什么的';
const available_version = '1.0.0~2.3.*';
const requireNativeAccess = false; // 可选
const requireNativeAccessDescription = "..." // 可选，此字符串将显示在警告窗口上
const soft_dependencies = []; // 可选
const hard_dependencies = []; // 可选

// 可选：注册事件监听器
mm.on('io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent', (event) => {
    mm.log('我的脚本已初始化！');
});
```

## 元数据声明

所有脚本必须在文件顶部以全局变量的形式声明以下元数据：

### 必需字段

| 字段 | 类型 | 描述 |
|------|------|------|
| `registry_name` | string | 脚本的唯一标识符。用于依赖和内部引用。在所有脚本中必须唯一。 |
| `display_name` | string | 在 UI 中显示的人类可读名称。 |
| `version` | string | 脚本的当前版本（推荐使用语义版本控制）。 |
| `author` | string | 作者名称。 |
| `description` | string | 脚本功能的简要描述。 |
| `available_version` | string | 此脚本兼容的 MouseMacros 版本 (例如, "2.0.0", "2.x", "1.0.0 ~ 2.0.0")。使用 "*" 表示兼容所有版本。 |

### 可选字段

| 字段 | 类型 | 描述 |
|------|------|------|
| `soft_dependencies` | string[] | 脚本 `register_name` 的数组，这些脚本能增强功能但不是必需的。 |
| `hard_dependencies` | string[] | 脚本 `register_name` 的数组，这些脚本是必需的。如果缺失，此脚本将被禁用。 |
| `requires_native_access` | boolean | 如果脚本需要原生系统访问权限，设置为 `true`。**需要用户明确批准。** |
| `native_access_description` | string | 清楚地解释为什么需要原生访问权限。这会在安全提示中显示给用户。 |

### 元数据示例

```javascript
// 基本脚本
const registry_name = 'simple_script';
const display_name = '简单脚本';
const version = '1.0.0';
const author = '张三';
const description = '记录事件的简单脚本';
const available_version = '1.0.0';

// 带有依赖的脚本
const registry_name = 'advanced_script';
const display_name = '高级脚本';
const version = '1.0.0';
const author = '李四';
const description = '依赖其他脚本的脚本';
const available_version = '1.0.0';
const soft_dependencies = ['helper_script'];
const hard_dependencies = ['core_dependency'];

// 需要原生访问的脚本
const registry_name = 'system_script';
const display_name = '系统集成脚本';
const version = '1.0.0';
const author = '管理员';
const description = '与系统 API 交互';
const available_version = '1.0.0';
const requires_native_access = true;
const native_access_description = '需要访问系统 API 以获得高级功能';
```

## 脚本 API 参考

脚本 API 通过全局 `mm` 对象进行访问。该对象提供与 MouseMacros 交互的方法。

### `mm.on(eventClassName, callback)`

注册特定事件类型的监听器。

**参数：**
- `eventClassName` (string): 事件类的完全限定名
- `callback` (function): 事件触发时执行的函数

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent', (event) => {
    mm.log('应用启动于: ' + new Date(event.getTimestamp()));
});
```

### `mm.getContext()`

返回脚本上下文对象以访问应用功能。

**返回：** 具有以下方法的 ScriptContext 对象：
- `simulate(action)` - 模拟鼠标或键盘操作
- `getPixelColor(x, y)` - 获取屏幕坐标处像素的 RGB 颜色
- `showToast(title, message)` - 向用户显示通知
- `getAppConfig()` - 访问应用配置

**示例：**
```javascript
const context = mm.getContext();
context.showToast('你好', '脚本正在运行！');
```

### `mm.log(message)`

将消息打印到控制台，带有 `[Script]` 前缀。

**参数：**
- `message` (string): 要记录的消息

**示例：**
```javascript
mm.log('这是一条日志消息');
// 输出: [Script] 这是一条日志消息
```

### `mm.cleanup()`

在脚本被禁用时自动调用。覆盖此方法以清理资源。

## 事件系统

MouseMacros 事件允许脚本对各种应用状态和用户操作做出反应。事件遵循发布-订阅模式。

### 事件生命周期

1. 应用创建事件
2. 事件通过事件系统分派
3. 所有已注册的监听器都收到通知
4. 对于可取消的事件，脚本可以阻止进一步的处理

### 事件对象方法

所有事件都有：
- `getTimestamp()` - 返回事件创建的时间（毫秒）

可取消事件有：
- `isCancelled()` - 检查事件是否已被取消
- `setCancelled(boolean)` - 取消事件（防止默认操作）

## 可用事件

### 应用生命周期事件

#### `OnAppLaunchedEvent`
应用启动时触发。

**属性：**
- `getApplicationVersion()` - 获取 MouseMacros 版本
- `getJavaVersion()` - 获取 Java 版本

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent', (event) => {
    mm.log('应用版本: ' + event.getApplicationVersion());
    mm.log('Java 版本: ' + event.getJavaVersion());
});
```

### 录制事件

#### `BeforeRecordStartEvent`
录制开始前触发。可取消。

**属性：**
- `isCancelled()` - 检查录制是否被阻止
- `setCancelled(boolean)` - 阻止录制开始

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforeRecordStartEvent', (event) => {
    mm.log('即将开始录制');
});
```

#### `AfterRecordStopEvent`
录制停止后触发。

**属性：**
- `getActionsCount()` - 记录的操作数

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.AfterRecordStopEvent', (event) => {
    mm.log('录制了 ' + event.getActionsCount() + ' 个操作');
});
```

#### `OnInputCapturedEvent`
录制期间捕获输入（鼠标或键盘）时触发。可取消。

**属性：**
- `getInputType()` - 输入类型（1=鼠标按下, 2=鼠标释放, 3=滚轮, 10=键盘按下, 11=键盘释放）
- `getKeyCode()` - 键码（键盘事件）
- `getX()` - X 坐标（鼠标事件）
- `getY()` - Y 坐标（鼠标事件）
- `getDelay()` - 距离上次操作的时间（毫秒）
- `isCancelled()` / `setCancelled()` - 取消输入捕获

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnInputCapturedEvent', (event) => {
    if (event.getInputType() === 1) { // 鼠标按下
        mm.log('鼠标在 (' + event.getX() + ', ' + event.getY() + ') 处按下');
    }
});
```

### 回放事件

#### `BeforePlaybackStartEvent`
宏回放开始前触发。可取消。

**属性：**
- `getMacroData()` - 将被播放的操作列表
- `getRepeatCount()` - 宏将重复的次数
- `isCancelled()` / `setCancelled()` - 取消回放

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    mm.log('开始回放，重复 ' + event.getRepeatCount() + ' 次');
});
```

#### `BeforeStepExecuteEvent`
在执行每个单独的操作前触发。可取消。

**属性：**
- `getAction()` - 即将执行的操作
- `getActionIndex()` - 操作的索引
- `isCancelled()` / `setCancelled()` - 跳过此操作

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforeStepExecuteEvent', (event) => {
    if (event.getActionIndex() % 10 === 0) {
        mm.log('执行操作 ' + event.getActionIndex());
    }
});
```

#### `AfterStepExecuteEvent`
每个操作执行后触发。

**属性：**
- `getAction()` - 已执行的操作
- `getActionIndex()` - 操作的索引

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.AfterStepExecuteEvent', (event) => {
    // 每个操作的自定义延迟逻辑
});
```

#### `OnLoopCompleteEvent`
当循环迭代完成时触发（宏的一次完整播放）。

**属性：**
- `getLoopNumber()` - 刚刚完成的迭代
- `getTotalLoops()` - 迭代总数

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnLoopCompleteEvent', (event) => {
    mm.log('完成循环 ' + event.getLoopNumber() + '/' + event.getTotalLoops());
});
```

#### `OnPlaybackAbortedEvent`
用户或脚本中止回放时触发。

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnPlaybackAbortedEvent', (event) => {
    mm.log('回放已中止');
});
```

### 文件操作事件

#### `OnMacroSaveEvent`
将宏保存到文件时触发。

**属性：**
- `getMacroName()` - 正在保存的宏的名称
- `getFilePath()` - 保存宏的路径

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnMacroSaveEvent', (event) => {
    mm.log('宏已保存: ' + event.getMacroName());
});
```

#### `OnMacroLoadEvent`
从文件加载宏时触发。

**属性：**
- `getMacroName()` - 加载的宏的名称
- `getFilePath()` - 加载的宏的路径

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnMacroLoadEvent', (event) => {
    mm.log('宏已加载: ' + event.getMacroName());
});
```

### 配置事件

#### `OnConfigChangedEvent`
应用配置更改时触发。

**属性：**
- `getChangedKey()` - 更改的配置键
- `getNewValue()` - 配置的新值

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnConfigChangedEvent', (event) => {
    if ('dark_mode' === event.getChangedKey()) {
        mm.log('深色模式更改为: ' + event.getNewValue());
    }
});
```

#### `OnMenuInitEvent`
UI 菜单初始化时触发。

**示例：**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnMenuInitEvent', (event) => {
    mm.log('菜单已初始化');
});
```

### 其他事件

#### `OnActionAddedEvent`
操作被记录/添加到宏时触发。

**属性：**
- `getAction()` - 添加的操作

#### `OnTooltipDisplayEvent`
即将显示工具提示时触发。可取消。

**属性：**
- `getTooltipText()` - 工具提示的文本
- `setTooltipText(String)` - 修改工具提示文本

## 脚本依赖

脚本可以依赖其他脚本来共享功能并避免代码重复。

### 声明依赖

```javascript
// 软依赖：很好有，但可选
const soft_dependencies = ['helper_script', 'utils_script'];

// 硬依赖：脚本工作所需
const hard_dependencies = ['core_dependency'];
```

### 依赖规则

- **硬依赖**：如果缺少任何硬依赖或硬依赖被禁用，脚本将无法启用
- **软依赖**：缺少软依赖不会阻止脚本运行，但功能可能有限
- **循环依赖**：不允许。系统将检测并报告循环依赖问题
- **版本检查**：依赖通过 `registry_name` 进行匹配

### 依赖最佳实践

1. 仅在绝对必要时使用硬依赖
2. 文档化每个依赖需要什么功能
3. 在运行时检查可选功能是否可用
4. 使用描述其目的的有意义的依赖名称

## 原生访问

某些脚本可能需要访问超出安全脚本 API 范围的 Java 类和系统 API。

### 请求原生访问

```javascript
const requires_native_access = true;
const native_access_description = '需要访问系统剪贴板以获得高级功能';
```

### 使用原生访问

获得批准后，脚本可以访问 Java 类：

```javascript
// 访问 Java 类
const File = Java.type('java.io.File');
const System = Java.type('java.lang.System');

// 使用 Java API
const home = System.getProperty('user.home');
mm.log('主目录: ' + home);
```

### 安全考虑

- 用户必须明确批准原生访问
- 管理员可以列入脚本/作者白名单
- 请求原生访问的脚本将向用户显示警告
- 仅在真正必要时请求原生访问

## 错误处理

健壮的错误处理可确保脚本不会导致应用崩溃。

### Try-Catch 块

```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    try {
        // 有风险的操作
        const data = event.getMacroData();
        if (data.length === 0) {
            throw new Error('没有可用的宏数据');
        }
    } catch (error) {
        mm.log('错误: ' + error.message);
        event.setCancelled(true); // 阻止回放
    }
});
```

### 验证模式

```javascript
function validateInput(value, expectedType) {
    if (typeof value !== expectedType) {
        throw new TypeError('期望 ' + expectedType + '，得到 ' + typeof value);
    }
    return value;
}

mm.on('io.github.samera2022.mousemacros.api.event.events.OnInputCapturedEvent', (event) => {
    try {
        const inputType = validateInput(event.getInputType(), 'number');
        // 处理验证的输入
    } catch (error) {
        mm.log('验证错误: ' + error.message);
    }
});
```

## 最佳实践

### 1. 保持脚本专注
每个脚本应该有单一、明确定义的目的。

### 2. 使用有意义的名称
```javascript
const registry_name = 'screenshot_capture_tool';  // 好
const registry_name = 'tool1';                    // 避免
```

### 3. 记录重要事件
```javascript
mm.log('脚本已初始化');
mm.log('处理 ' + actionCount + ' 个操作');
mm.log('操作成功完成');
```

### 4. 处理边界情况
```javascript
if (event.getActionsCount && event.getActionsCount() === 0) {
    mm.log('警告：没有操作可处理');
    return;
}
```

### 5. 清理资源
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent', (event) => {
    // 初始化
    globalResources = {};
});

// 禁用时清理
function onScriptDisabled() {
    if (globalResources) {
        // 清理代码
        globalResources = null;
    }
}
```

### 6. 为脚本版本化
使用语义版本控制（MAJOR.MINOR.PATCH）：
```javascript
const version = '1.0.0';        // 稳定版本
const version = '2.1.0-beta';   // 测试版本
```

### 7. 提供清晰的文档
包含解释复杂逻辑的注释：
```javascript
// 检查宏是否至少有 10 个操作，以避免运行微小的宏
if (event.getMacroData().length < 10) {
    mm.log('宏太短，跳过');
    return;
}
```

### 8. 进行彻底的测试
- 使用不同的配置进行测试
- 使用缺少可选依赖的方式测试
- 测试错误场景
- 使用各种事件组合进行测试

## 示例

### 示例 1：操作计数器脚本

计数宏中操作的简单脚本：

```javascript
const registry_name = 'action_counter';
const display_name = '操作计数器';
const version = '1.0.0';
const author = '开发者';
const description = '计数并记录宏操作';
const available_version = '1.0.0';

var totalActions = 0;
var totalPlaybacks = 0;

mm.on('io.github.samera2022.mousemacros.api.event.events.AfterRecordStopEvent', (event) => {
    totalActions = event.getActionsCount();
    mm.log('录制了 ' + totalActions + ' 个操作');
});

mm.on('io.github.samera2022.mousemacros.api.event.events.OnLoopCompleteEvent', (event) => {
    totalPlaybacks++;
    mm.log('完成播放 ' + totalPlaybacks);
});

mm.on('io.github.samera2022.mousemacros.api.event.events.OnPlaybackAbortedEvent', (event) => {
    mm.log('在 ' + totalPlaybacks + ' 个循环后中止播放');
});
```

### 示例 2：通知脚本

为重要事件显示通知：

```javascript
const registry_name = 'notification_system';
const display_name = '通知系统';
const version = '1.0.0';
const author = '开发者';
const description = '为宏事件显示通知';
const available_version = '1.0.0';

const context = mm.getContext();

mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    context.showToast('播放已开始', '重复 ' + event.getRepeatCount() + ' 次');
});

mm.on('io.github.samera2022.mousemacros.api.event.events.OnPlaybackAbortedEvent', (event) => {
    context.showToast('播放已中止', '宏执行已停止');
});

mm.on('io.github.samera2022.mousemacros.api.event.events.OnLoopCompleteEvent', (event) => {
    var progress = Math.round((event.getLoopNumber() / event.getTotalLoops()) * 100);
    if (progress % 25 === 0) { // 每 25% 显示一次
        context.showToast('进度', progress + '% 完成');
    }
});
```

### 示例 3：输入筛选脚本

在录制期间筛选某些输入：

```javascript
const registry_name = 'input_filter';
const display_name = '输入筛选器';
const version = '1.0.0';
const author = '开发者';
const description = '筛选鼠标移动事件';
const available_version = '1.0.0';

// 输入类型常数
const MOUSE_MOVE = 0;
const MOUSE_PRESS = 1;
const MOUSE_RELEASE = 2;
const MOUSE_WHEEL = 3;
const KEY_PRESS = 10;
const KEY_RELEASE = 11;

mm.on('io.github.samera2022.mousemacros.api.event.events.OnInputCapturedEvent', (event) => {
    // 跳过录制鼠标移动以减少噪声
    if (event.getInputType() === MOUSE_MOVE) {
        event.setCancelled(true);
        mm.log('在 (' + event.getX() + ', ' + event.getY() + ') 处筛选出鼠标移动');
    }
});
```

### 示例 4：智能宏执行脚本

带有验证的高级宏执行：

```javascript
const registry_name = 'smart_executor';
const display_name = '智能宏执行器';
const version = '1.0.0';
const author = '开发者';
const description = '智能验证和执行宏';
const available_version = '1.0.0';

var executionLog = [];

mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    var actions = event.getMacroData();
    
    if (actions.length === 0) {
        mm.log('错误：没有操作可播放');
        event.setCancelled(true);
        return;
    }
    
    if (event.getRepeatCount() > 100) {
        mm.log('警告：重复次数很高（' + event.getRepeatCount() + '）');
    }
    
    executionLog = [];
    mm.log('开始播放，共 ' + actions.length + ' 个操作');
});

mm.on('io.github.samera2022.mousemacros.api.event.events.BeforeStepExecuteEvent', (event) => {
    var action = event.getAction();
    executionLog.push({
        index: event.getActionIndex(),
        timestamp: new Date().getTime()
    });
});

mm.on('io.github.samera2022.mousemacros.api.event.events.OnLoopCompleteEvent', (event) => {
    mm.log('循环 ' + event.getLoopNumber() + '/' + event.getTotalLoops() + ' 已完成');
});

mm.on('io.github.samera2022.mousemacros.api.event.events.OnPlaybackAbortedEvent', (event) => {
    mm.log('在执行 ' + executionLog.length + ' 个操作后中止播放');
});
```

## 故障排除

### 脚本不加载
- 检查元数据格式（所有必需字段都存在）
- 验证文件以 `.js` 结尾
- 检查控制台是否有解析错误

### 事件不触发
- 验证事件类名是否正确
- 检查脚本是否在 UI 中启用
- 审查回调函数语法

### 原生访问问题
- 确保 `requires_native_access` 设置为 `true`
- 检查作者或脚本是否在白名单中
- 在访问 Java API 时使用 try-catch 块

### 依赖问题
- 验证依赖 `registry_name` 完全匹配
- 检查是否存在循环依赖
- 确保硬依赖已安装并启用

## 获取帮助

- 查阅 MouseMacros 文档
- 查看脚本目录中的示例脚本
- 检查控制台输出以获取详细的错误消息
- 使用清晰的错误描述报告问题

---

**最后更新：** 2026 年 2 月  
**MouseMacros 版本：** 2.0.0+

