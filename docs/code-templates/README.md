# 代码模板文件

本目录包含 MouseMacros 多模块重构所需的代码模板，可直接使用或作为参考。

## 📁 文件列表

### Maven POM 文件

1. **parent-pom.xml** - 父项目 POM 配置
   - 定义了两个子模块
   - 统一管理依赖版本
   - 配置通用插件

2. **api-pom.xml** - API模块 POM 配置
   - 零外部依赖
   - 配置源码和文档生成

3. **app-pom.xml** - App模块 POM 配置
   - 依赖 API 模块
   - 配置可执行JAR打包
   - 输出到父目录

### Java 类文件

4. **Event.java** - 基础事件类
   - 所有事件的抽象基类
   - 自动记录时间戳

5. **Cancellable.java** - 可取消接口
   - 标记事件可被取消
   - 提供取消状态管理

6. **EventPriority.java** - 事件优先级枚举
   - 6个优先级级别
   - 控制处理器执行顺序

## 🚀 使用方法

### Step 1: 备份当前项目
\\\ash
git tag v1.4.0-last-single-module
git checkout -b feature/multi-module-refactor
\\\

### Step 2: 创建父POM
\\\ash
# 备份当前 pom.xml
mv pom.xml pom.xml.backup

# 复制父 POM 模板
cp docs/code-templates/parent-pom.xml pom.xml
\\\

### Step 3: 创建 API 模块
\\\ash
# 创建目录结构
mkdir -p mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api/event
mkdir -p mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api/action
mkdir -p mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api/config

# 复制 POM
cp docs/code-templates/api-pom.xml mousemacros-api/pom.xml

# 复制基础类
cp docs/code-templates/Event.java mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api/event/
cp docs/code-templates/Cancellable.java mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api/event/
cp docs/code-templates/EventPriority.java mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api/event/
\\\

### Step 4: 创建 App 模块
\\\ash
# 创建目录结构
mkdir -p mousemacros-app/src/main/java/io/github/samera2022/mousemacros/app
mkdir -p mousemacros-app/src/main/resources

# 复制 POM
cp docs/code-templates/app-pom.xml mousemacros-app/pom.xml

# 移动现有代码（需要手动调整包路径）
# 从 src/ 移动到 mousemacros-app/src/
\\\

### Step 5: 验证构建
\\\ash
mvn clean compile
mvn package
\\\

## 📝 需要手动完成的任务

模板提供了骨架，但以下工作需要手动完成：

### API 模块 (mousemacros-api)

1. **创建更多事件类** (参考 EVENT_SYSTEM_DESIGN.md)
   - [ ] EventHandler.java (注解)
   - [ ] Listener.java (接口)
   - [ ] events/BeforePlaybackStartEvent.java
   - [ ] events/BeforeStepExecuteEvent.java
   - [ ] events/OnInputCapturedEvent.java
   - [ ] ... 其他事件类

2. **定义核心接口**
   - [ ] action/IMouseAction.java
   - [ ] config/IConfig.java
   - [ ] script/IScriptAPI.java (如果需要脚本支持)

3. **添加 package-info.java** 文档

### App 模块 (mousemacros-app)

1. **迁移现有代码**
   - [ ] 移动所有 src/ 下的代码到 mousemacros-app/src/
   - [ ] 更新包路径: mousemacros → mousemacros.app
   - [ ] 更新所有 import 语句

2. **实现事件管理器**
   - [ ] event/EventManager.java
   - [ ] event/RegisteredListener.java

3. **集成事件触发**
   - [ ] 在 MacroManager 中触发回放事件
   - [ ] 在 GlobalMouseListener 中触发录制事件
   - [ ] 在 ConfigManager 中触发配置事件

4. **更新资源文件路径**
   - [ ] 移动 application/ 下的资源到 src/main/resources/

## ⚡ 快速命令集合

\\\powershell
# 完整的创建命令（Windows PowerShell）

# 创建 API 模块结构
New-Item -Path "mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api" -ItemType Directory -Force
New-Item -Path "mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api/event" -ItemType Directory -Force
New-Item -Path "mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api/action" -ItemType Directory -Force
New-Item -Path "mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api/config" -ItemType Directory -Force

# 创建 App 模块结构
New-Item -Path "mousemacros-app/src/main/java" -ItemType Directory -Force
New-Item -Path "mousemacros-app/src/main/resources" -ItemType Directory -Force

# 复制 POM 文件
Copy-Item "docs/code-templates/parent-pom.xml" "pom.xml" -Force
Copy-Item "docs/code-templates/api-pom.xml" "mousemacros-api/pom.xml" -Force
Copy-Item "docs/code-templates/app-pom.xml" "mousemacros-app/pom.xml" -Force

# 复制基础类文件
Copy-Item "docs/code-templates/Event.java" "mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api/event/"
Copy-Item "docs/code-templates/Cancellable.java" "mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api/event/"
Copy-Item "docs/code-templates/EventPriority.java" "mousemacros-api/src/main/java/io/github/samera2022/mousemacros/api/event/"
\\\

## 🔗 相关文档

- [多模块重构计划](../MULTI_MODULE_REFACTORING_PLAN.md) - 完整重构步骤
- [事件系统设计](../EVENT_SYSTEM_DESIGN.md) - 所有事件类的详细定义
- [架构对比](../ARCHITECTURE_COMPARISON.md) - 新旧架构对比

## ⚠️ 注意事项

1. **编码格式**: 所有文件使用 UTF-8 编码
2. **换行符**: 建议使用 LF (Unix格式)
3. **包路径**: 确保与模板中的路径一致
4. **版本号**: 模板使用 2.0.0，可根据需要调整

## 🎯 验证清单

完成后验证以下内容：

- [ ] mvn clean compile 成功
- [ ] mvn package 成功
- [ ] 生成 	arget/MouseMacros.jar
- [ ] JAR 文件可以正常运行
- [ ] 所有现有功能正常工作

---

*模板创建日期: 2026-09-28*
*基于版本: saMacros 2.0.0 architecture*
