# MouseMacros 脚本开发文档索引

## 📚 文档总览

本目录包含 MouseMacros 脚本开发的完整文档。根据您的需求选择相应的文档。

---

## 🌍 English Documentation

### For Script Developers / 给脚本开发者

1. **[SCRIPT_DEVELOPMENT_GUIDE.md](./en/SCRIPT_DEVELOPMENT_GUIDE.md)** ⭐ 推荐首先阅读
   - Complete JavaScript script development guide
   - 734 lines of comprehensive documentation
   - 20+ events fully documented
   - 4 complete working examples
   - Best practices and troubleshooting
   
   **When to read:** Before writing your first script

2. **[API_QUICK_REFERENCE.md](./API_QUICK_REFERENCE.md)** 快速参考
   - Quick reference card for all APIs
   - Common patterns and snippets
   - Performance tips
   - Event types reference
   
   **When to read:** During script development

3. **[EXTENDED_API_REFERENCE.md](./en/EXTENDED_API_REFERENCE.md)** 扩展 API
   - Recommended future APIs
   - Proposed improvements to ScriptContext
   - API implementation patterns
   - Best practices
   
   **When to read:** When exploring advanced features

### For Project Managers / 给项目经理

4. **[API_ANALYSIS_REPORT.md](./en/API_ANALYSIS_REPORT.md)** ⭐ 完整分析
   - Complete API analysis of app module
   - Identified unexposed APIs
   - Implementation cost assessment
   - Security considerations
   - Implementation roadmap with priorities
   
   **When to read:** When planning v2.1.0+ features

---

## 🇨🇳 中文文档

### 给脚本开发者

1. **[SCRIPT_DEVELOPMENT_GUIDE.md](./zh/SCRIPT_DEVELOPMENT_GUIDE.md)** ⭐ 推荐首先阅读
   - 完整的 JavaScript 脚本开发指南
   - 734 行全面的文档
   - 20+ 个事件完整记录
   - 4 个完整工作示例
   - 最佳实践和故障排除
   
   **何时阅读：** 编写第一个脚本之前

2. **[API_QUICK_REFERENCE.md](./API_QUICK_REFERENCE.md)** 快速参考
   - 所有 API 的快速参考卡
   - 常用模式和代码片段
   - 性能建议
   - 事件类型参考
   
   **何时阅读：** 脚本开发期间

3. **[EXTENDED_API_REFERENCE.md](./zh/EXTENDED_API_REFERENCE.md)** 扩展 API
   - 推荐的未来 API
   - ScriptContext 的改进建议
   - API 实现模式
   - 最佳实践
   
   **何时阅读：** 探索高级功能时

### 给项目经理

4. **[API_ANALYSIS_REPORT.md](./zh/API_ANALYSIS_REPORT.md)** ⭐ 完整分析
   - app 模块的完整 API 分析
   - 识别的未开放 API
   - 实现成本评估
   - 安全考虑
   - 有优先级的实现路线图
   
   **何时阅读：** 规划 v2.1.0+ 功能时

---

## 📖 按用途选择文档

### "我想开始写脚本"

推荐阅读顺序：
1. Read: [SCRIPT_DEVELOPMENT_GUIDE](./en/SCRIPT_DEVELOPMENT_GUIDE.md#minimal-script-example)
2. Try: Examples in [SCRIPT_DEVELOPMENT_GUIDE](./en/SCRIPT_DEVELOPMENT_GUIDE.md#examples)
3. Reference: [API_QUICK_REFERENCE.md](./API_QUICK_REFERENCE.md)

### "我想了解所有可用事件"

1. 查看: [SCRIPT_DEVELOPMENT_GUIDE](./en/SCRIPT_DEVELOPMENT_GUIDE.md#available-events)
2. 参考: [API_QUICK_REFERENCE.md](./API_QUICK_REFERENCE.md#available-events) 的事件树

### "我想要最佳实践指导"

1. 读: [SCRIPT_DEVELOPMENT_GUIDE](./en/SCRIPT_DEVELOPMENT_GUIDE.md#best-practices)
2. 看: [API_QUICK_REFERENCE.md](./API_QUICK_REFERENCE.md#common-patterns)

### "我需要快速查阅 API"

使用: [API_QUICK_REFERENCE.md](./API_QUICK_REFERENCE.md) - 中英文对照

### "我要评估 API 实现的可行性"

阅读: [API_ANALYSIS_REPORT.md](./en/API_ANALYSIS_REPORT.md)
- Section: "Implementation Cost Assessment"
- Section: "Security and Privacy Considerations"
- Section: "API Implementation Roadmap"

### "我要规划脚本 API 的未来"

1. 首先: [API_ANALYSIS_REPORT.md](./en/API_ANALYSIS_REPORT.md)
2. 参考: [EXTENDED_API_REFERENCE.md](./en/EXTENDED_API_REFERENCE.md)

### "我需要复制粘贴代码示例"

查找: [API_QUICK_REFERENCE.md](./API_QUICK_REFERENCE.md#common-patterns)

---

## 📊 文档统计

| 文档 | 行数 | 主要内容 | 目标用户 |
|------|------|--------|--------|
| SCRIPT_DEVELOPMENT_GUIDE | 734 | 完整教程 + 事件文档 | 开发者 |
| EXTENDED_API_REFERENCE | ~500 | 新 API 建议 | 开发者 + 管理者 |
| API_ANALYSIS_REPORT | ~600 | 详细分析 + 规划 | 管理者 + 架构师 |
| API_QUICK_REFERENCE | ~300 | 快速查阅 | 所有人 |

**总计：** ~2134 行文档，50+ 个代码示例

---

## 🎯 核心内容速览

### 现有 API (✅ 已实现)

```javascript
mm.on(eventClassName, callback)        // 事件监听
mm.getContext()                        // 获取上下文
mm.log(message)                        // 日志输出
mm.cleanup()                           // 资源清理

// 通过 context 获取
context.simulate(action)               // 模拟操作
context.getPixelColor(x, y)            // 获取像素颜色
context.showToast(title, msg)          // 显示通知
context.getAppConfig()                 // 获取配置
```

### 推荐新增 API (⚠️ 计划中)

**高优先级 (v2.1.0):**
- `mm.macro.*` - 宏管理接口
- `mm.system.*` - 系统信息接口

**中优先级 (v2.2.0):**
- `mm.i18n.*` - 国际化支持
- `mm.clipboard.*` - 剪贴板操作
- `mm.file.*` - 文件操作

**低优先级 (v2.3.0+):**
- `mm.window.*` - 窗口管理
- `mm.http.*` - 网络请求
- `mm.timer.*` - 定时器

---

## 🔗 快速链接

### 学习资源

- [脚本开发指南 - 概述](./en/SCRIPT_DEVELOPMENT_GUIDE.md#overview)
- [脚本开发指南 - 最小示例](./en/SCRIPT_DEVELOPMENT_GUIDE.md#minimal-script-example)
- [脚本开发指南 - 4 个完整示例](./en/SCRIPT_DEVELOPMENT_GUIDE.md#examples)
- [快速参考 - 常用模式](./API_QUICK_REFERENCE.md#common-patterns)

### 参考资料

- [所有可用事件列表](./en/SCRIPT_DEVELOPMENT_GUIDE.md#available-events)
- [API 快速参考卡](./API_QUICK_REFERENCE.md)
- [事件类型常数](./API_QUICK_REFERENCE.md#event-input-types)

### 规划文档

- [推荐的新 API](./en/EXTENDED_API_REFERENCE.md#recommended-future-apis)
- [API 实现路线图](./en/API_ANALYSIS_REPORT.md#api-implementation-roadmap)
- [成本评估](./en/API_ANALYSIS_REPORT.md#vi-implementation-cost-assessment)

---

## 💡 核心概念

### Script Metadata / 脚本元数据

每个脚本必须声明：
- `registry_name` - 唯一标识符
- `display_name` - 显示名称
- `version` - 当前版本
- `author` - 作者名称
- `description` - 功能描述
- `available_version` - 最新版本

### Event System / 事件系统

脚本通过事件监听与应用交互：
- **应用生命周期事件** - OnAppLaunchedEvent
- **录制事件** - BeforeRecordStartEvent, OnInputCapturedEvent
- **播放事件** - BeforePlaybackStartEvent, OnLoopCompleteEvent
- **文件事件** - OnMacroSaveEvent, OnMacroLoadEvent
- **配置事件** - OnConfigChangedEvent

### Dependencies / 依赖管理

脚本支持：
- **软依赖** - 可选增强功能
- **硬依赖** - 必需的功能

### Permissions / 权限模型

- 无权限 - 事件监听、日志输出
- 需审批 - 文件操作、网络操作
- 需原生访问 - 系统级操作

---

## ✨ 文档特色

✅ **完整性**
- 20+ 个事件完整文档
- 50+ 个代码示例
- 4 个实现完整的脚本示例

✅ **实用性**
- 快速参考卡片
- 常用代码模式
- 最佳实践指导

✅ **专业性**
- 详细的安全分析
- 成本效益评估
- 清晰的实现路线图

✅ **多语言**
- 英文完整文档
- 中文完整翻译
- 代码示例国际化

---

## 🚀 快速开始

### 第 1 步：理解基础 (5 分钟)

读这个：[最小脚本示例](./en/SCRIPT_DEVELOPMENT_GUIDE.md#minimal-script-example)

### 第 2 步：学习事件 (15 分钟)

学习这些事件：
- OnAppLaunchedEvent
- BeforePlaybackStartEvent
- OnLoopCompleteEvent

参考：[可用事件](./en/SCRIPT_DEVELOPMENT_GUIDE.md#available-events)

### 第 3 步：编写第一个脚本 (30 分钟)

参考：[示例 1: 操作计数器](./en/SCRIPT_DEVELOPMENT_GUIDE.md#example-1-action-counter-script)

### 第 4 步：查阅 API (需要时)

使用：[快速参考卡](./API_QUICK_REFERENCE.md)

---

## 📞 获取帮助

如果您：

- 📖 **不知道从何开始**
  → 阅读 [SCRIPT_DEVELOPMENT_GUIDE](./en/SCRIPT_DEVELOPMENT_GUIDE.md)

- 🔍 **需要快速查阅某个 API**
  → 使用 [API_QUICK_REFERENCE](./API_QUICK_REFERENCE.md)

- 🎯 **想要编写特定功能的脚本**
  → 查看 [SCRIPT_DEVELOPMENT_GUIDE 中的示例](./en/SCRIPT_DEVELOPMENT_GUIDE.md#examples)

- 📋 **需要了解未来 API 规划**
  → 阅读 [API_ANALYSIS_REPORT](./en/API_ANALYSIS_REPORT.md)

- 🔧 **要评估实现成本**
  → 查看 [实现成本评估](./en/API_ANALYSIS_REPORT.md#vi-implementation-cost-assessment)

---

## 📅 文档版本

- **版本:** 1.0
- **发布日期:** 2026 年 2 月 12 日
- **适用范围:** MouseMacros v2.0.0+
- **维护状态:** 活跃
- **最后更新:** 2026 年 2 月 12 日

---

## 🎓 建议的学习路径

```
初级开发者 (Beginner)
└─> SCRIPT_DEVELOPMENT_GUIDE (全读)
    └─> 运行示例代码
        └─> API_QUICK_REFERENCE (随时查阅)

中级开发者 (Intermediate)
├─> SCRIPT_DEVELOPMENT_GUIDE (重点章节)
├─> EXTENDED_API_REFERENCE (了解未来功能)
└─> API_QUICK_REFERENCE (日常工作)

高级开发者 (Advanced)
├─> API_ANALYSIS_REPORT (深入理解)
├─> EXTENDED_API_REFERENCE (设计新脚本)
└─> API_QUICK_REFERENCE (参考)

项目经理 (Project Manager)
└─> API_ANALYSIS_REPORT
    ├─> 实现成本评估
    ├─> 优先级规划
    └─> 安全考虑
```

---

**Happy scripting! / 祝您脚本开发愉快！**

如有问题，请参考相应的文档章节或使用快速参考卡片。

