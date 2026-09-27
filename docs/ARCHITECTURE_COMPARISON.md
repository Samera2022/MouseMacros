# MouseMacros 架构演进对比

## 快速对比

| 特性 | MouseMacros 1.4.0 (当前) | saMacros 2.0.0 (参考) | MouseMacros 2.0.0 (目标) |
|------|-------------------------|---------------------|------------------------|
| **架构模式** | 单体模块 | 多模块分离 | 多模块分离 |
| **模块数量** | 1 | 2 (api + app) | 2 (api + app) |
| **API 独立性** | ❌ 无独立API | ✅ 纯接口模块 | ✅ 纯接口模块 |
| **事件系统** | ❌ 无 | ✅ 完整事件系统 | ✅ 完整事件系统 |
| **脚本支持** | ❌ 无 | ✅ GraalVM JS | ⚠️ 待定 |
| **第三方扩展** | ❌ 不支持 | ✅ 支持 | ✅ 支持 |
| **Maven 发布** | ❌ 单一 JAR | ✅ API + APP 独立 | ✅ API + APP 独立 |

## 包结构对比

### MouseMacros 1.4.0 (单模块)
\\\
io.github.samera2022.mousemacros
├── Main.java
├── action/
│   └── MouseAction.java
├── config/
│   ├── ConfigManager.java
│   └── WhitelistManager.java
├── constant/
├── listener/
├── manager/
└── ui/
\\\

### saMacros 2.0.0 (多模块)
\\\
samacros-api (模块1)
└── io.github.samera2022.samacros.api
    ├── action/
    │   └── IMouseAction.java (接口)
    ├── config/
    │   └── IConfig.java (接口)
    ├── event/
    │   ├── Event.java
    │   ├── Cancellable.java
    │   ├── EventHandler.java
    │   ├── EventPriority.java
    │   ├── Listener.java
    │   └── events/
    │       ├── BeforePlaybackStartEvent.java
    │       ├── OnInputCapturedEvent.java
    │       └── ... (14个事件类)
    └── script/
        ├── IScriptAPI.java
        └── IScriptContext.java

samacros-app (模块2)
└── io.github.samera2022.samacros.app
    ├── Main.java
    ├── action/
    │   └── MouseAction.java (实现)
    ├── config/
    │   └── ConfigManager.java (实现)
    ├── event/
    │   ├── EventManager.java
    │   └── RegisteredListener.java
    ├── script/
    │   ├── ScriptAPI.java
    │   ├── ScriptContext.java
    │   └── ScriptManager.java
    ├── manager/
    ├── listener/
    └── ui/
\\\

### MouseMacros 2.0.0 (目标架构)
\\\
mousemacros-api (模块1 - 新建)
└── io.github.samera2022.mousemacros.api
    ├── action/
    │   └── IMouseAction.java (接口)
    ├── config/
    │   └── IConfig.java (接口)
    ├── event/
    │   ├── Event.java
    │   ├── Cancellable.java
    │   ├── EventHandler.java
    │   ├── EventPriority.java
    │   ├── Listener.java
    │   └── events/
    │       ├── BeforePlaybackStartEvent.java
    │       ├── OnInputCapturedEvent.java
    │       └── ... (核心事件)
    └── package-info.java

mousemacros-app (模块2 - 重组)
└── io.github.samera2022.mousemacros.app
    ├── Main.java
    ├── action/
    │   └── MouseAction.java (实现)
    ├── config/
    │   ├── ConfigManager.java (实现)
    │   └── WhitelistManager.java
    ├── event/
    │   ├── EventManager.java
    │   └── RegisteredListener.java
    ├── constant/
    ├── listener/
    ├── manager/
    └── ui/
\\\

## 依赖关系图

### 当前架构 (1.4.0)
\\\
MouseMacros.jar
├── gson
└── jnativehook
\\\

### 目标架构 (2.0.0)
\\\
mousemacros-api.jar
└── (零依赖)

mousemacros-app.jar
├── mousemacros-api
├── gson
└── jnativehook
\\\

## 事件系统架构

### 核心组件
\\\
Event (抽象基类)
  ├── timestamp: long
  └── getTimestamp(): long

Cancellable (接口)
  ├── isCancelled(): boolean
  └── setCancelled(boolean): void

EventPriority (枚举)
  ├── LOWEST (0)
  ├── LOW (1)
  ├── NORMAL (2)
  ├── HIGH (3)
  ├── HIGHEST (4)
  └── MONITOR (5)

EventHandler (注解)
  ├── priority: EventPriority
  └── ignoreCancelled: boolean

Listener (标记接口)

EventManager (管理器 - App模块)
  ├── registerListener(Listener)
  ├── unregisterListener(Listener)
  └── fireEvent(Event): Event
\\\

### 事件类型分类
\\\
生命周期事件
├── OnAppLaunchedEvent
└── OnMenuInitEvent

录制事件
├── BeforeRecordStartEvent (可取消)
├── AfterRecordStopEvent
├── OnInputCapturedEvent (可取消)
└── OnActionAddedEvent

回放事件
├── BeforePlaybackStartEvent (可取消)
├── BeforeStepExecuteEvent (可取消)
├── AfterStepExecuteEvent
├── OnPlaybackAbortedEvent
└── OnLoopCompleteEvent

宏管理事件
├── OnMacroLoadEvent
└── OnMacroSaveEvent

配置事件
├── OnConfigChangedEvent
└── OnTooltipDisplayEvent (可取消)
\\\

## 构建流程对比

### 当前构建 (1.4.0)
\\\
mvn clean package
  └── target/MouseMacros.jar (含依赖)
\\\

### 目标构建 (2.0.0)
\\\
mvn clean package
├── mousemacros-api/target/
│   ├── mousemacros-api-2.0.0.jar
│   ├── mousemacros-api-2.0.0-sources.jar
│   └── mousemacros-api-2.0.0-javadoc.jar
├── mousemacros-app/target/
│   └── mousemacros-app-2.0.0.jar
└── target/
    └── saMacros.jar (最终产物, 含所有依赖)
\\\

## 发布策略对比

### 当前 (1.4.0)
- 单一 JAR 发布
- 无法独立使用 API

### 目标 (2.0.0)
- API 模块发布到 Maven 仓库
  - 第三方开发者可单独引用
  - 提供 sources 和 javadoc
- App 模块打包为可执行 JAR
  - 包含 API 和所有依赖

## 迁移路径

\\\
第1步: 备份当前代码
├── git tag v1.4.0-last-single-module
└── git checkout -b feature/multi-module-refactor

第2步: 创建父POM
├── 将 pom.xml → pom.xml.backup
├── 创建新的父 pom.xml
└── 设置 packaging=pom

第3步: 创建 API 模块
├── mkdir mousemacros-api
├── 创建 mousemacros-api/pom.xml
├── 创建包结构
├── 创建事件系统基础类
└── 定义核心接口

第4步: 创建 App 模块
├── mkdir mousemacros-app
├── 创建 mousemacros-app/pom.xml
├── 移动现有代码
├── 调整包路径
└── 实现事件管理器

第5步: 验证构建
├── mvn clean compile
├── mvn package
└── 测试生成的 JAR

第6步: 集成事件系统
├── 在关键点触发事件
└── 编写示例监听器

第7步: 文档和测试
├── 更新 README
├── 编写 API 文档
└── 添加单元测试
\\\

## 关键决策点

| 决策项 | 选项A | 选项B | 推荐 |
|--------|-------|-------|------|
| **脚本引擎** | 暂不实现 | 立即添加 GraalVM | 选项A (先重构) |
| **版本号** | 保持 1.5.0 | 跳到 2.0.0 | 选项B (重大变更) |
| **向后兼容** | 保留旧包路径 | 直接迁移 | 选项B (清晰分离) |
| **事件数量** | 精简版(5-8个) | 完整版(14个) | 选项A (逐步增加) |

## 时间估算

| 阶段 | 预计时间 | 关键产出 |
|------|---------|---------|
| 创建父POM和模块结构 | 1-2小时 | 可编译的骨架 |
| API模块开发 | 3-4小时 | 完整的接口定义 |
| App模块重组 | 2-3小时 | 代码迁移完成 |
| 事件系统实现 | 4-6小时 | EventManager + 核心事件 |
| 集成和测试 | 3-4小时 | 功能验证通过 |
| 文档编写 | 2-3小时 | API文档 + 迁移指南 |
| **总计** | **15-22小时** | **可发布的 2.0.0 版本** |

## 风险和缓解

| 风险 | 影响 | 缓解措施 |
|------|------|---------|
| 包路径变更导致现有宏失效 | 高 | 宏文件不包含包路径 |
| 构建复杂度增加 | 中 | 提供清晰的构建文档 |
| API 设计不合理 | 高 | 参考 saMacros 成熟设计 |
| 事件系统性能影响 | 低 | 只在关键点触发事件 |

## 参考资源

- **saMacros 源码**: D:\Users\_S_A_M\Project\IdeaProjects\saMacros
- **参考提交**: 67a4545 (2026-02-16)
- **文档**:
  - docs/MULTI_MODULE_REFACTORING_PLAN.md (本项目)
  - docs/EVENT_SYSTEM_DESIGN.md (本项目)

---

*创建日期: 2026-09-28*
*作者: Based on saMacros 2.0.0 architecture*
