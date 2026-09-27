# MouseMacros 事件系统设计参考

## 概述
本文档基于 saMacros 2.0.0 的事件系统实现，为 MouseMacros 的多模块重构提供事件系统设计参考。

## 核心组件

### 1. Event (基础事件类)
\\\java
package io.github.samera2022.mousemacros.api.event;

public abstract class Event {
    private final long timestamp;
    
    public Event() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}
\\\

**职责：**
- 所有事件的基类
- 自动记录事件创建时间戳
- 提供时间戳访问方法

### 2. Cancellable (可取消接口)
\\\java
package io.github.samera2022.mousemacros.api.event;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancel);
}
\\\

**职责：**
- 标记事件可以被取消
- 提供取消状态的获取和设置
- 事件处理器可以根据取消状态决定是否继续处理

### 3. EventPriority (事件优先级)
\\\java
package io.github.samera2022.mousemacros.api.event;

public enum EventPriority {
    LOWEST(0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4),
    MONITOR(5);  // 只监控，不修改事件
    
    private final int slot;
    
    EventPriority(int slot) {
        this.slot = slot;
    }
    
    public int getSlot() {
        return slot;
    }
}
\\\

**职责：**
- 定义事件处理器的执行顺序
- MONITOR 优先级特殊：只读事件，不应修改

### 4. EventHandler (事件处理器注解)
\\\java
package io.github.samera2022.mousemacros.api.event;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    EventPriority priority() default EventPriority.NORMAL;
    boolean ignoreCancelled() default false;
}
\\\

**职责：**
- 标记方法为事件处理器
- 指定处理器优先级
- 指定是否忽略已取消的事件

**使用示例：**
\\\java
@EventHandler(priority = EventPriority.HIGH)
public void onBeforeStep(BeforeStepExecuteEvent event) {
    if (shouldSkip(event.getAction())) {
        event.setCancelled(true);
    }
}
\\\

### 5. Listener (监听器接口)
\\\java
package io.github.samera2022.mousemacros.api.event;

public interface Listener {
    // 标记接口，用于类型检查
}
\\\

**职责：**
- 标记类为事件监听器
- 包含 @EventHandler 注解的方法的类必须实现此接口

## 完整事件列表

### 生命周期事件

#### OnAppLaunchedEvent
\\\java
public class OnAppLaunchedEvent extends Event {
    // 应用启动完成后触发
    // 不可取消
}
\\\

#### OnMenuInitEvent
\\\java
public class OnMenuInitEvent extends Event {
    private final JPopupMenu menu;
    
    // 系统托盘菜单初始化时触发
    // 可以用来添加自定义菜单项
}
\\\

### 录制相关事件

#### BeforeRecordStartEvent
\\\java
public class BeforeRecordStartEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    private String recordMode;  // "normal" or "append"
    
    // 开始录制前触发
    // 可取消以阻止录制
}
\\\

#### AfterRecordStopEvent
\\\java
public class AfterRecordStopEvent extends Event {
    private final List<IMouseAction> recordedActions;
    
    // 录制停止后触发
    // 可以访问录制的动作列表
}
\\\

#### OnInputCapturedEvent
\\\java
public class OnInputCapturedEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    private final String inputType;  // "mouse" or "keyboard"
    private final Object rawEvent;   // NativeMouseEvent or NativeKeyEvent
    
    // 捕获到输入事件时触发
    // 可取消以忽略此输入
}
\\\

#### OnActionAddedEvent
\\\java
public class OnActionAddedEvent extends Event {
    private final IMouseAction action;
    private final int index;
    
    // 动作被添加到宏时触发
    // 不可取消（已添加）
}
\\\

### 回放相关事件

#### BeforePlaybackStartEvent
\\\java
public class BeforePlaybackStartEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    private final List<IMouseAction> actions;
    private int loopCount;  // 可修改
    
    // 开始回放前触发
    // 可取消以阻止回放
    // 可修改循环次数
}
\\\

#### BeforeStepExecuteEvent
\\\java
public class BeforeStepExecuteEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    private final int stepIndex;
    private final IMouseAction action;
    private final boolean isLastStep;
    
    // 执行步骤前触发
    // 可取消以跳过此步骤
}
\\\

#### AfterStepExecuteEvent
\\\java
public class AfterStepExecuteEvent extends Event {
    private final int stepIndex;
    private final IMouseAction action;
    private final boolean isLastStep;
    private final boolean successful;
    
    // 步骤执行后触发
    // 不可取消（已执行）
}
\\\

#### OnPlaybackAbortedEvent
\\\java
public class OnPlaybackAbortedEvent extends Event {
    private final String reason;  // "user_interrupt", "error", etc.
    private final int lastCompletedStep;
    
    // 回放中止时触发
}
\\\

#### OnLoopCompleteEvent
\\\java
public class OnLoopCompleteEvent extends Event {
    private final int completedLoopNumber;
    private final int totalLoops;
    
    // 完成一次循环后触发
}
\\\

### 宏管理事件

#### OnMacroLoadEvent
\\\java
public class OnMacroLoadEvent extends Event {
    private final String filePath;
    private final List<IMouseAction> actions;
    
    // 宏文件加载后触发
}
\\\

#### OnMacroSaveEvent
\\\java
public class OnMacroSaveEvent extends Event {
    private final String filePath;
    private final List<IMouseAction> actions;
    
    // 宏文件保存后触发
}
\\\

### 配置和UI事件

#### OnConfigChangedEvent
\\\java
public class OnConfigChangedEvent extends Event {
    private final String configKey;
    private final Object oldValue;
    private final Object newValue;
    
    // 配置项改变时触发
}
\\\

#### OnTooltipDisplayEvent
\\\java
public class OnTooltipDisplayEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    private String message;  // 可修改
    private int displayDuration;  // 可修改
    
    // 显示提示信息前触发
    // 可取消或修改内容
}
\\\

## 事件管理器实现（App模块）

\\\java
package io.github.samera2022.mousemacros.app.event;

import io.github.samera2022.mousemacros.api.event.*;

public class EventManager {
    private static final EventManager instance = new EventManager();
    private final Map<Class<? extends Event>, List<RegisteredListener>> handlers;
    
    private EventManager() {
        handlers = new ConcurrentHashMap<>();
    }
    
    public static EventManager getInstance() {
        return instance;
    }
    
    /**
     * 注册监听器
     */
    public void registerListener(Listener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation == null) continue;
            
            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1 || !Event.class.isAssignableFrom(params[0])) {
                throw new IllegalArgumentException("Invalid event handler: " + method);
            }
            
            @SuppressWarnings("unchecked")
            Class<? extends Event> eventType = (Class<? extends Event>) params[0];
            
            RegisteredListener registered = new RegisteredListener(
                listener, method, annotation.priority(), annotation.ignoreCancelled()
            );
            
            handlers.computeIfAbsent(eventType, k -> new ArrayList<>())
                   .add(registered);
            
            // 按优先级排序
            handlers.get(eventType).sort(Comparator.comparing(
                RegisteredListener::getPriority, 
                Comparator.reverseOrder()
            ));
        }
    }
    
    /**
     * 取消注册监听器
     */
    public void unregisterListener(Listener listener) {
        handlers.values().forEach(list -> 
            list.removeIf(rl -> rl.getListener() == listener)
        );
    }
    
    /**
     * 触发事件
     */
    public <T extends Event> T fireEvent(T event) {
        List<RegisteredListener> listeners = handlers.get(event.getClass());
        if (listeners == null) return event;
        
        boolean cancelled = event instanceof Cancellable 
            && ((Cancellable) event).isCancelled();
        
        for (RegisteredListener listener : listeners) {
            if (cancelled && listener.isIgnoreCancelled()) {
                continue;
            }
            
            try {
                listener.invoke(event);
            } catch (Exception e) {
                System.err.println("Error invoking event handler: " + e);
                e.printStackTrace();
            }
            
            cancelled = event instanceof Cancellable 
                && ((Cancellable) event).isCancelled();
        }
        
        return event;
    }
}
\\\

## 使用示例

### 创建自定义监听器
\\\java
public class MyMacroListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onBeforePlayback(BeforePlaybackStartEvent event) {
        System.out.println("即将回放 " + event.getActions().size() + " 个动作");
        
        // 如果宏太长，禁止执行
        if (event.getActions().size() > 1000) {
            event.setCancelled(true);
            System.out.println("宏过长，已取消");
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onStepExecute(BeforeStepExecuteEvent event) {
        System.out.println("执行步骤 " + event.getStepIndex());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void logConfigChange(OnConfigChangedEvent event) {
        System.out.println("配置变更: " + event.getConfigKey() 
            + " = " + event.getNewValue());
    }
}
\\\

### 注册和使用
\\\java
// 在应用启动时
EventManager manager = EventManager.getInstance();
manager.registerListener(new MyMacroListener());

// 触发事件
BeforePlaybackStartEvent event = new BeforePlaybackStartEvent(actions, loopCount);
manager.fireEvent(event);

if (!event.isCancelled()) {
    // 执行回放
}
\\\

## 优势分析

### 1. 解耦合
- 事件发布者无需知道订阅者
- 动态添加/移除监听器
- 核心逻辑与扩展逻辑分离

### 2. 可扩展性
- 第三方可以通过事件系统扩展功能
- 无需修改核心代码
- 支持插件架构

### 3. 可控制性
- 通过优先级控制执行顺序
- 通过取消机制阻止默认行为
- 通过 ignoreCancelled 实现只读监听

### 4. 可测试性
- 事件可以独立测试
- 监听器可以独立测试
- 易于模拟和验证

## 与现有代码的集成

### 现有代码改造点
1. **MacroManager.startPlayback()**
   - 在开始前触发 BeforePlaybackStartEvent
   - 在每步执行前后触发对应事件
   - 在完成/中止时触发对应事件

2. **GlobalMouseListener**
   - 在捕获输入时触发 OnInputCapturedEvent
   - 在添加动作时触发 OnActionAddedEvent

3. **ConfigManager**
   - 在配置改变时触发 OnConfigChangedEvent

4. **Main.java**
   - 在应用启动后触发 OnAppLaunchedEvent
   - 在托盘菜单初始化时触发 OnMenuInitEvent

## 迁移步骤

1. 在 mousemacros-api 中创建事件系统基础类
2. 定义所有事件类
3. 在 mousemacros-app 中实现 EventManager
4. 在现有代码中集成事件触发
5. 编写测试用例验证事件系统
6. 创建示例监听器演示用法

---

*参考来源: saMacros 2.0.0 (commit 67a4545)*
*创建日期: 2026-09-28*
