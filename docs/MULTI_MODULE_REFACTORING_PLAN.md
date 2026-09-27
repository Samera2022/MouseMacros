# MouseMacros 多模块架构重构方案

## 参考项目
本方案参考 saMacros 项目的早期提交 (67a4545, 2026-02-16)，该项目从 MouseMacros 分叉后实现了现代化的多模块架构。

## 当前架构（单模块）

**项目结构：**
```
MouseMacros/
├── pom.xml (单一 JAR 打包)
├── src/
│   └── main/java/io/github/samera2022/mousemacros/
│       ├── Main.java
│       ├── action/
│       ├── config/
│       ├── ui/
│       └── util/
```

**问题：**
- 所有代码耦合在一个模块中
- 无法为第三方开发者提供独立的API
- 难以进行模块级别的版本管理
- 构建和测试全部或无

## 目标架构（多模块）

**新项目结构：**
```
MouseMacros/
├── pom.xml (父 POM, packaging=pom)
├── mousemacros-api/
│   ├── pom.xml
│   └── src/main/java/io/github/samera2022/mousemacros/api/
│       ├── action/         # 接口定义
│       ├── config/         # 配置接口
│       ├── event/          # 事件系统
│       │   ├── Event.java
│       │   ├── EventHandler.java
│       │   ├── Cancellable.java
│       │   └── events/     # 具体事件类
│       └── script/         # 脚本API接口
├── mousemacros-app/
│   ├── pom.xml
│   └── src/main/java/io/github/samera2022/mousemacros/app/
│       ├── Main.java
│       ├── action/         # 实现类
│       ├── config/         # 配置实现
│       ├── event/          # 事件管理器
│       ├── ui/             # UI组件
│       ├── util/           # 工具类
│       └── script/         # 脚本引擎实现
```

## 模块职责划分

### 1. mousemacros-api 模块

**职责：** 提供纯接口定义，无实现逻辑

**包含内容：**
- \pi.action\ - 鼠标动作接口 (IMouseAction)
- \pi.config\ - 配置接口 (IConfig)
- \pi.event\ - 事件系统框架
  - Event, Cancellable, EventHandler, EventPriority, Listener
  - events/ - 具体事件类（参考 saMacros 的14个事件）
- \pi.script\ - 脚本API接口 (IScriptAPI, IScriptContext)

**依赖：** 零外部依赖（纯Java接口）

**优势：**
- 轻量级，可独立发布到 Maven 仓库
- 第三方开发者只需依赖此模块即可开发插件/脚本
- API稳定性保证

### 2. mousemacros-app 模块

**职责：** 实现所有业务逻辑和UI

**包含内容：**
- 所有接口的实现类
- UI框架和组件
- 事件管理器
- 脚本引擎实现
- 工具类

**依赖：**
- mousemacros-api (内部依赖)
- gson
- jnativehook
- 可选：graalvm, flatlaf

## 重构步骤

### Phase 1: 创建父POM结构
1. 将当前 \pom.xml\ 重命名为 \pom.xml.backup\
2. 创建新的父 \pom.xml\:
   - \packaging\ 改为 \pom\
   - 添加 \<modules>\ 声明
   - 将依赖版本移到 \<dependencyManagement>\
   - 将插件配置移到 \<pluginManagement>\

### Phase 2: 创建 mousemacros-api 模块
1. 创建 \mousemacros-api/\ 目录
2. 创建 \mousemacros-api/pom.xml\
3. 提取接口到 \mousemacros-api/src/main/java/\:
   - 创建事件系统框架（参考 saMacros）
   - 定义核心接口
   - 添加 package-info.java 文档

### Phase 3: 创建 mousemacros-app 模块
1. 创建 \mousemacros-app/\ 目录
2. 创建 \mousemacros-app/pom.xml\
3. 移动实现代码到 \mousemacros-app/src/main/java/\
4. 调整包路径: \mousemacros\ → \mousemacros.app\
5. 移动资源文件到 \mousemacros-app/src/main/resources/\

### Phase 4: 依赖和构建配置
1. 配置 app 模块依赖 api 模块
2. 配置 assembly 插件输出到父目录
3. 更新主类路径

### Phase 5: 验证和测试
1. 执行 \mvn clean compile\ 验证编译
2. 执行 \mvn package\ 验证打包
3. 测试生成的 JAR 文件
4. 验证所有功能正常

## POM 配置参考

### 父 pom.xml 核心配置
\\\xml
<packaging>pom</packaging>
<modules>
    <module>mousemacros-api</module>
    <module>mousemacros-app</module>
</modules>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.samera2022</groupId>
            <artifactId>mousemacros-api</artifactId>
            <version>\</version>
        </dependency>
        <!-- 其他依赖版本管理 -->
    </dependencies>
</dependencyManagement>
\\\

### mousemacros-api/pom.xml
\\\xml
<parent>
    <groupId>io.github.samera2022</groupId>
    <artifactId>mousemacros-parent</artifactId>
    <version>2.0.0</version>
    <relativePath>../pom.xml</relativePath>
</parent>

<artifactId>mousemacros-api</artifactId>
<packaging>jar</packaging>

<dependencies>
    <!-- 零外部依赖 -->
</dependencies>
\\\

### mousemacros-app/pom.xml
\\\xml
<parent>
    <groupId>io.github.samera2022</groupId>
    <artifactId>mousemacros-parent</artifactId>
    <version>2.0.0</version>
    <relativePath>../pom.xml</relativePath>
</parent>

<artifactId>mousemacros-app</artifactId>
<packaging>jar</packaging>

<dependencies>
    <dependency>
        <groupId>io.github.samera2022</groupId>
        <artifactId>mousemacros-api</artifactId>
    </dependency>
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
    </dependency>
    <dependency>
        <groupId>com.github.kwhat</groupId>
        <artifactId>jnativehook</artifactId>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-assembly-plugin</artifactId>
            <configuration>
                <outputDirectory>\</outputDirectory>
            </configuration>
        </plugin>
    </plugins>
</build>
\\\

## 关键事件系统设计（参考 saMacros）

### 事件类型列表
1. **生命周期事件**
   - OnAppLaunchedEvent
   - OnMenuInitEvent

2. **录制相关事件**
   - BeforeRecordStartEvent
   - AfterRecordStopEvent
   - OnInputCapturedEvent
   - OnActionAddedEvent

3. **回放相关事件**
   - BeforePlaybackStartEvent
   - BeforeStepExecuteEvent
   - AfterStepExecuteEvent
   - OnPlaybackAbortedEvent
   - OnLoopCompleteEvent

4. **宏管理事件**
   - OnMacroLoadEvent
   - OnMacroSaveEvent

5. **配置和UI事件**
   - OnConfigChangedEvent
   - OnTooltipDisplayEvent

### 事件系统核心类
- \Event.java\ - 基础事件类
- \Cancellable.java\ - 可取消事件接口
- \EventHandler.java\ - 事件处理器注解
- \EventPriority.java\ - 事件优先级枚举
- \Listener.java\ - 监听器标记接口

## 版本升级建议

建议将重构后的版本号从 1.4.0 升级到 2.0.0，因为：
- 架构级别的重大变更
- API 模块的引入
- 包路径的变化

## 向后兼容性

**不兼容变更：**
- 包路径从 \mousemacros\ 变更为 \mousemacros.app\
- 需要更新所有导入语句

**建议：**
- 保留 1.4.0 分支作为稳定版本
- 在 2.0.0 中完成多模块重构
- 提供迁移指南

## 优势总结

1. **清晰的关注点分离** - API与实现分离
2. **更好的可扩展性** - 第三方开发者友好
3. **独立版本管理** - API可以独立发布
4. **构建优化** - 可以只构建需要的模块
5. **测试改进** - 可以针对API层编写单元测试
6. **文档生成** - 可以单独为API生成JavaDoc

## 参考资料

- saMacros 早期提交: 67a4545 (2026-02-16)
- saMacros 仓库: D:\Users\_S_A_M\Project\IdeaProjects\saMacros
- Maven 多模块项目最佳实践

---

*创建日期: 2026-09-28*
*基于: MouseMacros 1.4.0*
*参考: saMacros 2.0.0 初始架构*
