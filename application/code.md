# 软著申请代码材料

```java
package io.github.samera2022.mousemacros.api.action;
}
        package io.github.samera2022.mousemacros.api.event;
private final long timestamp;
        this.timestamp =System.

currentTimeMillis();
    }
            return timestamp;
    }
            }
            package io.github.samera2022.mousemacros.api.event;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface EventHandler {
}
package io.github.samera2022.mousemacros.api.event;
}
        package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.action.IMouseAction;
import io.github.samera2022.mousemacros.api.event.Event;
import java.util.Collections;
import java.util.List;
private final List<IMouseAction> finalMacroData;
private final int totalActions;
        this.finalMacroData =finalMacroData;
        this.totalActions =finalMacroData.

size();
    }
            return Collections.

unmodifiableList(finalMacroData);
    }
            return totalActions;
    }
            }
            package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.event.Event;
private final int stepIndex;
private final String executionStatus;
        this.stepIndex =stepIndex;
        this.executionStatus =executionStatus;
    }
            return stepIndex;
    }
            return executionStatus;
    }
            }
            package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.action.IMouseAction;
import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;
import java.util.Collections;
import java.util.List;
private boolean cancelled = false;
private final List<IMouseAction> macroData;
private final int repeatCount;
        this.macroData =macroData;
        this.repeatCount =repeatCount;
    }
            return Collections.

unmodifiableList(macroData);
    }
            return repeatCount;
    }

@Override
public boolean isCancelled() {
    return cancelled;
}

@Override
public void setCancelled(boolean cancel) {
    this.cancelled = cancel;
}
}
        package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;
import java.awt .*;
private boolean cancelled = false;
private final long startTime;
private final Point initialMousePos;
        this.startTime =startTime;
        this.initialMousePos =initialMousePos;
    }
            return startTime;
    }
            return initialMousePos;
    }

@Override
public boolean isCancelled() {
    return cancelled;
}

@Override
public void setCancelled(boolean cancel) {
    this.cancelled = cancel;
}
}
        package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.action.IMouseAction;
import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;
private boolean cancelled = false;
private final int stepIndex;
private final IMouseAction action;
private final boolean isLastStep;
        this.stepIndex =stepIndex;
        this.action =action;
        this.isLastStep =isLastStep;
    }
            return stepIndex;
    }
            return action;
    }
            return isLastStep;
    }

@Override
public boolean isCancelled() {
    return cancelled;
}

@Override
public void setCancelled(boolean cancel) {
    this.cancelled = cancel;
}
}
        package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.action.IMouseAction;
import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;
private boolean cancelled = false;
private final IMouseAction action;
private final int currentIndex;
        this.action =action;
        this.currentIndex =currentIndex;
    }
            return action;
    }
            return currentIndex;
    }

@Override
public boolean isCancelled() {
    return cancelled;
}

@Override
public void setCancelled(boolean cancel) {
    this.cancelled = cancel;
}
}
        package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.event.Event;
private final String appVersion;
private final String runtimeEnv;
        this.appVersion =appVersion;
        this.runtimeEnv =runtimeEnv;
    }
            return appVersion;
    }
            return runtimeEnv;
    }
            }
            package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.event.Event;
private final String configKey;
private final Object oldValue;
private final Object newValue;
        this.configKey =configKey;
        this.oldValue =oldValue;
        this.newValue =newValue;
    }
            return configKey;
    }
            return oldValue;
    }
            return newValue;
    }
            }
            package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;
private boolean cancelled = false;
private final int inputType;
private final int keyCode;
private final int x;
private final int y;
private final long delay;
        this.inputType =inputType;
        this.keyCode =keyCode;
        this.x =x;
        this.y =y;
        this.delay =delay;
    }
            return inputType;
    }
            return keyCode;
    }
            return x;
    }
            return y;
    }
            return delay;
    }

@Override
public boolean isCancelled() {
    return cancelled;
}

@Override
public void setCancelled(boolean cancel) {
    this.cancelled = cancel;
}
}
        package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.event.Event;
private final int completedIterations;
private final int remainingIterations;
        this.completedIterations =completedIterations;
        this.remainingIterations =remainingIterations;
    }
            return completedIterations;
    }
            return remainingIterations;
    }
            }
            package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.event.Event;
import java.io.File;
private final File file;
private final String rawContent;
        this.file =file;
        this.rawContent =rawContent;
    }
            return file;
    }
            return rawContent;
    }
            }
            package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.event.Event;
import java.io.File;
private final File file;
private final String formattedContent;
        this.file =file;
        this.formattedContent =formattedContent;
    }
            return file;
    }
            return formattedContent;
    }
            }
            package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.event.Event;
import javax.swing .*;
private final JPopupMenu popupMenu;
        this.popupMenu =popupMenu;
    }
            return popupMenu;
    }
            }
            package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.event.Event;
private final String abortReason;
private final int atStepIndex;
        this.abortReason =abortReason;
        this.atStepIndex =atStepIndex;
    }
            return abortReason;
    }
            return atStepIndex;
    }
            }
            package io.github.samera2022.mousemacros.api.event.events;
import io.github.samera2022.mousemacros.api.event.Event;
private final String rawText;
private String modifiedText;
        this.rawText =rawText;
        this.modifiedText =rawText;
    }
            return rawText;
    }
            return modifiedText;
    }
            this.modifiedText =modifiedText;
    }
            }
            package io.github.samera2022.mousemacros.api.script;
import io.github.samera2022.mousemacros.api.event.Event;
import java.util.function.Consumer;
}
        package io.github.samera2022.mousemacros.app;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Localizer {
    private static Map<String, String> translations = new HashMap<>();
    private static final Map<String, String> fallbackTranslations;
    private static String currentLang;
    private static boolean runtimeSwitch = false;

    static {
        fallbackTranslations = loadLanguageMap("en_us");
        String sysLang = java.util.Locale.getDefault().toString().toLowerCase();
        sysLang = sysLang.replace('-', '_');
        if (sysLang.startsWith("zh")) {
            sysLang = "zh_cn";
        } else {
            sysLang = "en_us";
        }
        load(sysLang);
    }

    String path = OtherConsts.RELATIVE_PATH + "langs/" + lang + ".json";
        try(
    InputStream in = Localizer.class.getResourceAsStream(path))

    {
        if (in == null) {
            System.err.println("Language file not found: " + path);
            return Collections.emptyMap();
        }
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        Map<String, String> loadedMap = new Gson().fromJson(reader, new TypeToken<Map<String, String>>() {
        }.getType());
        return loadedMap != null ? loadedMap : Collections.emptyMap();
    } catch(
    Exception e)

    {
        System.err.println("Failed to load or parse language file: " + path);
        e.printStackTrace();
        return Collections.emptyMap();
    }
}
        if(lang ==null)return;
        if(lang.

equals("en_us")){
translations =new HashMap<>(fallbackTranslations);
        }else{
translations =

loadLanguageMap(lang);
        }
currentLang =lang;
    }
            if(key ==null)return"";
String value = translations.get(key);
        if(value !=null){
        return value;
        }
value =fallbackTranslations.

get(key);
        return value !=null?value :key;
    }

public static String getS(String key) {
    return get("settings." + key);
}

public static boolean hasKey(String key) {
    return translations.containsKey(key) || fallbackTranslations.containsKey(key);
}

public static void setRuntimeSwitch(boolean enable) {
    runtimeSwitch = enable;
}

public static boolean isRuntimeSwitch() {
    return runtimeSwitch;
}

public static String getCurrentLang() {
    return currentLang;
}
}
        package io.github.samera2022.mousemacros.app;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.event.EventManager;
import io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent;
import io.github.samera2022.mousemacros.app.script.ScriptManager;
import io.github.samera2022.mousemacros.app.util.ConsoleOutputCapturer;
import javax.swing .*;
        import static io.github.samera2022.mousemacros.app.ui.frame.MainFrame.MAIN_FRAME;

public class Main {
    public static void main(String[] args) {
        ConsoleOutputCapturer.start();
        String dllName = "JNativeHook.x86_64.dll";
        String configDir = ConfigManager.CONFIG_DIR;
        java.io.File libDir = new java.io.File(configDir + "/libs/", dllName);
        if (!libDir.exists()) libDir.mkdirs();
        System.setProperty("jnativehook.lib.path", libDir.getAbsolutePath());
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        try {
            if (ConfigManager.getBoolean("enable_dark_mode"))
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            else UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> MAIN_FRAME.setVisible(true));
        ScriptManager.loadAndProcessScripts();
        EventManager.callEvent(new OnAppLaunchedEvent("2.0.0", System.getProperty("java.version")));
    }
}
package io.github.samera2022.mousemacros.app.action;
import io.github.samera2022.mousemacros.api.action.IMouseAction;
import io.github.samera2022.mousemacros.app.util.ScreenUtil;
import java.awt .*;
        import java.util.HashMap;
import java.util.Map;

public class MouseAction implements IMouseAction {
    public int x;
    public int y;
    public int type;
    public int button;
    public long delay;
    public int wheelAmount = 0;
    public int keyCode = 0;
    public int awtKeyCode = 0;
    public Map<String, Object> metadata = new HashMap<>();

    public MouseAction(int x, int y, int type, int button, long delay) {
        this(x, y, type, button, delay, 0, 0, 0);
    }

    public MouseAction(int x, int y, int type, int button, long delay, int wheelAmount) {
        this(x, y, type, button, delay, wheelAmount, 0, 0);
    }

    public MouseAction(int x, int y, int type, int button, long delay, int wheelAmount, int keyCode) {
        this(x, y, type, button, delay, wheelAmount, keyCode, 0);
    }

    public MouseAction(int x, int y, int type, int button, long delay, int wheelAmount, int keyCode, int awtKeyCode) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.button = button;
        this.delay = delay;
        this.wheelAmount = wheelAmount;
        this.keyCode = keyCode;
        this.awtKeyCode = awtKeyCode;
    }

    @Override
    public void perform() {
        try {
            if (robotInstance == null) {
                robotInstance = new Robot();
            }
            Robot robot = robotInstance;
            Point global = ScreenUtil.denormalizeFromVirtualOrigin(x, y);
            if (type == 3) {
                robot.mouseMove(global.x, global.y);
                robot.mouseWheel(wheelAmount);
                return;
            }
            if (type == 10) {
                int code = awtKeyCode > 0 ? awtKeyCode : keyCode;
                if (code > 0) robot.keyPress(code);
                return;
            }
            if (type == 11) {
                int code = awtKeyCode > 0 ? awtKeyCode : keyCode;
                if (code > 0) robot.keyRelease(code);
                return;
            }
            robot.mouseMove(global.x, global.y);
            switch (type) {
                case 1:
                    robot.mousePress(getAWTButtonMask(button));
                    break;
                case 2:
                    robot.mouseRelease(getAWTButtonMask(button));
                    break;
            }
        } catch (Exception e) {
        }
    }

    private static Robot robotInstance = null;

    private int getAWTButtonMask(int btn) {
        switch (btn) {
            case 1:
                return java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
            case 2:
                return java.awt.event.InputEvent.BUTTON2_DOWN_MASK;
            case 3:
                return java.awt.event.InputEvent.BUTTON3_DOWN_MASK;
            default:
                return java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
        }
    }
}
package io.github.samera2022.mousemacros.app.config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;
import io.github.samera2022.mousemacros.app.ui.frame.settings.SettingsRegistry;
import io.github.samera2022.mousemacros.app.util.FileUtil;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file .*;
        import java.util .*;
        import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigManager {
    public static String CONFIG_DIR;
    private static final String CONFIG_PATH;
    private static final String WHITELIST_PATH;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, Object> settings = new HashMap<>();
    public static final int RFM_MIXED = 1;
    public static final int RFM_STANDARDIZED = 2;
    public static final int RFM_MEMORIZED = 3;

    static {
        CONFIG_DIR = FileUtil.getLocalStoragePath().toString();
        CONFIG_PATH = CONFIG_DIR + "\\config.cfg";
        WHITELIST_PATH = CONFIG_DIR + "\\white_list.json";
        reload();
        loadWhitelist();
    }

    public static void reload() {
        loadConfig();
    }

    public static void loadConfig() {
        try {
            String json = FileUtil.readFile(CONFIG_PATH);
            if (json == null || json.trim().isEmpty()) {
                resetToDefault();
                saveConfig();
                return;
            }
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> loadedSettings = gson.fromJson(json, type);
            settings.clear();
            settings.putAll(loadedSettings);
        } catch (IOException e) {
            resetToDefault();
            saveConfig();
        }
    }

    public static void saveConfig() {
        try {
            java.io.File dir = new java.io.File(CONFIG_DIR);
            if (!dir.exists()) dir.mkdirs();
            String json = gson.toJson(settings);
            FileUtil.writeFile(CONFIG_PATH, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void resetToDefault() {
        settings.clear();
        for (SettingsRegistry setting : SettingsRegistry.values()) {
            settings.put(setting.i18nKey, setting.defaultValue);
        }
        Map<String, String> defaultKeyMap = new HashMap<>();
        defaultKeyMap.put("start_record", "60");
        defaultKeyMap.put("stop_record", "61");
        defaultKeyMap.put("play_macro", "62");
        defaultKeyMap.put("abort_macro_operation", "63");
        settings.put("keyMap", defaultKeyMap);
    }

    public static void set(String key, Object value) {
        settings.put(key, value);
    }

    public static boolean getBoolean(String key) {
        Object value = settings.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }

    public static int getInt(String key) {
        Object value = settings.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }

    public static double getDouble(String key) {
        Object value = settings.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }

    public static String getString(String key) {
        Object value = settings.get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getKeyMap() {
        Object value = settings.get("keyMap");
        if (value instanceof Map) {
            return (Map<String, String>) value;
        }
        return new HashMap<>();
    }

    public static void loadWhitelist() {
        try {
            String json = FileUtil.readFile(WHITELIST_PATH);
            if (json != null && !json.trim().isEmpty()) {
                Type type = new TypeToken<Map<String, Object>>() {
                }.getType();
                Map<String, Object> tempMap = gson.fromJson(json, type);
                if (tempMap.containsKey("authors")) {
                    WhitelistManager.authors = gson.fromJson(gson.toJson(tempMap.get("authors")), new TypeToken<List<String>>() {
                    }.getType());
                }
                if (tempMap.containsKey("scripts")) {
                    WhitelistManager.scripts = gson.fromJson(gson.toJson(tempMap.get("scripts")), new TypeToken<Map<String, List<WhitelistManager.ScriptInfo>>>() {
                    }.getType());
                }
            } else {
                WhitelistManager.authors.clear();
                WhitelistManager.scripts.clear();
                saveWhitelist();
            }
        } catch (IOException e) {
            WhitelistManager.authors.clear();
            WhitelistManager.scripts.clear();
            saveWhitelist();
        }
    }

    public static void saveWhitelist() {
        try {
            java.io.File dir = new java.io.File(CONFIG_DIR);
            if (!dir.exists()) dir.mkdirs();
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("authors", WhitelistManager.authors);
            tempMap.put("scripts", WhitelistManager.scripts);
            String json = gson.toJson(tempMap);
            FileUtil.writeFile(WHITELIST_PATH, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] getAvailableLangs() {
        List<String> langs = new ArrayList<>();
        try {
            String resPath = OtherConsts.RELATIVE_PATH + "langs/";
            URL resource = Localizer.class.getResource(resPath);
            if (resource != null) {
                URI uri = resource.toURI();
                Path myPath;
                if (uri.getScheme().equals("jar")) {
                    FileSystem fileSystem;
                    try {
                        fileSystem = FileSystems.getFileSystem(uri);
                    } catch (FileSystemNotFoundException e) {
                        fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    }
                    myPath = fileSystem.getPath(resPath);
                } else {
                    myPath = Paths.get(uri);
                }
                try (Stream<Path> paths = Files.walk(myPath, 1)) {
                    langs = paths
                            .filter(Files::isRegularFile)
                            .map(p -> p.getFileName().toString())
                            .filter(name -> name.endsWith(".json"))
                            .map(name -> name.substring(0, name.length() - 5))
                            .collect(Collectors.toList());
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return langs.toArray(new String[0]);
    }
}
package io.github.samera2022.mousemacros.app.event;
import io.github.samera2022.mousemacros.api.event.Cancellable;
import io.github.samera2022.mousemacros.api.event.Event;
import io.github.samera2022.mousemacros.api.event.Listener;
import io.github.samera2022.mousemacros.api.event.EventHandler;
import java.lang.reflect.Method;
import java.util .*;
        import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private static final Map<Class<? extends Event>, List<RegisteredListener>> listeners = new ConcurrentHashMap<>();
    private static final Map<Class<? extends Event>, List<RegisteredListener>> listenerCache = new ConcurrentHashMap<>();

    public static void registerEvents(Listener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class) && method.getParameterCount() == 1) {
                Class<?> paramType = method.getParameterTypes()[0];
                if (Event.class.isAssignableFrom(paramType)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Event> eventClass = (Class<? extends Event>) paramType;
                    listeners.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>()).add(new RegisteredListener(listener, method, method.getAnnotation(EventHandler.class).priority(), method.getAnnotation(EventHandler.class).ignoreCancelled()));
                }
            }
        }
        listenerCache.clear();
    }

    public static void unregisterEvents(Listener listener) {
        for (List<RegisteredListener> list : listeners.values()) {
            list.removeIf(rl -> rl.getListener() == listener);
        }
        listenerCache.clear();
    }

    private static List<RegisteredListener> getHandlersFor(Class<? extends Event> eventClass) {
        return listenerCache.computeIfAbsent(eventClass, clazz -> {
            Set<RegisteredListener> collected = new HashSet<>();
            Class<?> current = clazz;
            while (current != null && Event.class.isAssignableFrom(current)) {
                List<RegisteredListener> registered = listeners.get(current);
                if (registered != null) {
                    collected.addAll(registered);
                }
                current = current.getSuperclass();
            }
            List<RegisteredListener> sorted = new ArrayList<>(collected);
            sorted.sort(Comparator.comparing(RegisteredListener::getPriority));
            return sorted;
        });
    }

    public static void callEvent(Event event) {
        List<RegisteredListener> handlers = getHandlersFor(event.getClass());
        if (handlers == null || handlers.isEmpty()) {
            return;
        }
        for (RegisteredListener rl : handlers) {
            try {
                if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && !rl.isIgnoreCancelled()) {
                    continue;
                }
                rl.getMethod().setAccessible(true);
                rl.getMethod().invoke(rl.getListener(), event);
            } catch (Exception e) {
                System.err.println("Error dispatching event " + event.getClass().getSimpleName() + " to " + rl.getListener().getClass().getName());
                e.printStackTrace();
            }
        }
    }
}
package io.github.samera2022.mousemacros.app.event;
import io.github.samera2022.mousemacros.api.event.EventPriority;
import io.github.samera2022.mousemacros.api.event.Listener;
import java.lang.reflect.Method;

public class RegisteredListener {
    private final Listener listener;
    private final Method method;
    private final EventPriority priority;
    private final boolean ignoreCancelled;

    public RegisteredListener(Listener listener, Method method, EventPriority priority, boolean ignoreCancelled) {
        this.listener = listener;
        this.method = method;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    public Listener getListener() {
        return listener;
    }

    public Method getMethod() {
        return method;
    }

    public EventPriority getPriority() {
        return priority;
    }

    public boolean isIgnoreCancelled() {
        return ignoreCancelled;
    }
}
package io.github.samera2022.mousemacros.app.listener;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener;
import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.action.MouseAction;
import io.github.samera2022.mousemacros.app.event.EventManager;
import io.github.samera2022.mousemacros.api.event.events.OnInputCapturedEvent;
import io.github.samera2022.mousemacros.app.manager.MacroManager;
import io.github.samera2022.mousemacros.app.ui.frame.settings.HotkeyDialog;
import io.github.samera2022.mousemacros.app.util.OtherUtil;
import io.github.samera2022.mousemacros.app.util.ScreenUtil;
import java.awt .*;
        import static io.github.samera2022.mousemacros.app.manager.LogManager.log;
import static io.github.samera2022.mousemacros.app.ui.frame.MainFrame .*;

public class GlobalMouseListener implements NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener {
    public GlobalMouseListener() {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == keyRecord && (!MacroManager.isRecording()) && (!HotkeyDialog.inHotKeyDialog)) {
            MacroManager.startRecording();
        } else if (e.getKeyCode() == keyStop && MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            MacroManager.stopRecording();
        } else if (e.getKeyCode() == keyPlay && (!MacroManager.isRecording()) && (!HotkeyDialog.inHotKeyDialog)) {
            if (MacroManager.isPaused()) {
                MacroManager.resume();
            } else {
                MacroManager.play();
            }
        } else if (e.getKeyCode() == keyAbort && MacroManager.isPlaying() && (!HotkeyDialog.inHotKeyDialog)) {
            if (MacroManager.isPaused()) {
                MacroManager.abort();
            } else {
                MacroManager.pause();
            }
        } else if (MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            long now = System.currentTimeMillis();
            long delay = now - MacroManager.getLastTime();
            OnInputCapturedEvent event = new OnInputCapturedEvent(10, e.getKeyCode(), 0, 0, delay);
            EventManager.callEvent(event);
            if (event.isCancelled()) return;
            int awtKeyCode = toAwtKeyCode(e.getKeyCode());
            MacroManager.recordAction(new MouseAction(0, 0, 10, 0, delay, 0, e.getKeyCode(), awtKeyCode));
            log(Localizer.get("recording_key_pressed") + OtherUtil.getNativeKeyDisplayText(e.getKeyCode()));
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            long now = System.currentTimeMillis();
            long delay = now - MacroManager.getLastTime();
            OnInputCapturedEvent event = new OnInputCapturedEvent(11, e.getKeyCode(), 0, 0, delay);
            EventManager.callEvent(event);
            if (event.isCancelled()) return;
            int awtKeyCode = toAwtKeyCode(e.getKeyCode());
            MacroManager.recordAction(new MouseAction(0, 0, 11, 0, delay, 0, e.getKeyCode(), awtKeyCode));
            log(Localizer.get("recording_key_released") + OtherUtil.getNativeKeyDisplayText(e.getKeyCode()));
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        if (MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            Point p = ScreenUtil.normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            long delay = now - MacroManager.getLastTime();
            int btn = e.getButton();
            int macroBtn = btn == 2 ? 3 : btn == 3 ? 2 : btn;
            OnInputCapturedEvent event = new OnInputCapturedEvent(1, macroBtn, p.x, p.y, delay);
            EventManager.callEvent(event);
            if (event.isCancelled()) return;
            String btnName = macroBtn == 1 ? Localizer.get("log.mouse_left") : macroBtn == 2 ? Localizer.get("log.mouse_middle") : Localizer.get("log.mouse_right");
            MacroManager.recordAction(new MouseAction(p.x, p.y, 1, macroBtn, delay));
            log(Localizer.get("log.recording_mouse_pressed") + " [" + btnName + "] (" + p.x + "," + p.y + ")");
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        if (MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            Point p = ScreenUtil.normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            long delay = now - MacroManager.getLastTime();
            int btn = e.getButton();
            int macroBtn = btn == 2 ? 3 : btn == 3 ? 2 : btn;
            OnInputCapturedEvent event = new OnInputCapturedEvent(2, macroBtn, p.x, p.y, delay);
            EventManager.callEvent(event);
            if (event.isCancelled()) return;
            String btnName = macroBtn == 1 ? Localizer.get("log.mouse_left") : macroBtn == 2 ? Localizer.get("log.mouse_middle") : Localizer.get("log.mouse_right");
            MacroManager.recordAction(new MouseAction(p.x, p.y, 2, macroBtn, delay));
            log(Localizer.get("log.recording_mouse_released") + " [" + btnName + "] (" + p.x + "," + p.y + ")");
        }
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
        if (MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            Point p = ScreenUtil.normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            long delay = now - MacroManager.getLastTime();
            int wheelAmount = e.getWheelRotation();
            OnInputCapturedEvent event = new OnInputCapturedEvent(3, wheelAmount, p.x, p.y, delay);
            EventManager.callEvent(event);
            if (event.isCancelled()) return;
            MacroManager.recordAction(new MouseAction(p.x, p.y, 3, 0, delay, wheelAmount));
            log(Localizer.get("log.recording_scroll_msg1") + " (" + Localizer.get("log.recording_scroll_msg2") + wheelAmount + ")");
        }
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        if (MacroManager.isRecording() && (!HotkeyDialog.inHotKeyDialog)) {
            Point p = ScreenUtil.normalizeToVirtualOrigin(e.getX(), e.getY());
            long now = System.currentTimeMillis();
            var actions = MacroManager.getActions();
            if (actions.isEmpty() || p.distance(actions.get(actions.size() - 1).x, actions.get(actions.size() - 1).y) > 3 || (now - MacroManager.getLastTime()) > 20) {
                long delay = now - MacroManager.getLastTime();
                OnInputCapturedEvent event = new OnInputCapturedEvent(0, 0, p.x, p.y, delay);
                EventManager.callEvent(event);
                if (event.isCancelled()) {
                    MacroManager.setLastTime(now);
                    return;
                }
                MacroManager.recordAction(new MouseAction(p.x, p.y, 0, 0, delay));
            }
        }
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        nativeMouseMoved(e);
    }

    private int toAwtKeyCode(int nativeKeyCode) {
        switch (nativeKeyCode) {
            case NativeKeyEvent.VC_A:
                return java.awt.event.KeyEvent.VK_A;
            case NativeKeyEvent.VC_B:
                return java.awt.event.KeyEvent.VK_B;
            case NativeKeyEvent.VC_C:
                return java.awt.event.KeyEvent.VK_C;
            case NativeKeyEvent.VC_D:
                return java.awt.event.KeyEvent.VK_D;
            case NativeKeyEvent.VC_E:
                return java.awt.event.KeyEvent.VK_E;
            case NativeKeyEvent.VC_F:
                return java.awt.event.KeyEvent.VK_F;
            case NativeKeyEvent.VC_G:
                return java.awt.event.KeyEvent.VK_G;
            case NativeKeyEvent.VC_H:
                return java.awt.event.KeyEvent.VK_H;
            case NativeKeyEvent.VC_I:
                return java.awt.event.KeyEvent.VK_I;
            case NativeKeyEvent.VC_J:
                return java.awt.event.KeyEvent.VK_J;
            case NativeKeyEvent.VC_K:
                return java.awt.event.KeyEvent.VK_K;
            case NativeKeyEvent.VC_L:
                return java.awt.event.KeyEvent.VK_L;
            case NativeKeyEvent.VC_M:
                return java.awt.event.KeyEvent.VK_M;
            case NativeKeyEvent.VC_N:
                return java.awt.event.KeyEvent.VK_N;
            case NativeKeyEvent.VC_O:
                return java.awt.event.KeyEvent.VK_O;
            case NativeKeyEvent.VC_P:
                return java.awt.event.KeyEvent.VK_P;
            case NativeKeyEvent.VC_Q:
                return java.awt.event.KeyEvent.VK_Q;
            case NativeKeyEvent.VC_R:
                return java.awt.event.KeyEvent.VK_R;
            case NativeKeyEvent.VC_S:
                return java.awt.event.KeyEvent.VK_S;
            case NativeKeyEvent.VC_T:
                return java.awt.event.KeyEvent.VK_T;
            case NativeKeyEvent.VC_U:
                return java.awt.event.KeyEvent.VK_U;
            case NativeKeyEvent.VC_V:
                return java.awt.event.KeyEvent.VK_V;
            case NativeKeyEvent.VC_W:
                return java.awt.event.KeyEvent.VK_W;
            case NativeKeyEvent.VC_X:
                return java.awt.event.KeyEvent.VK_X;
            case NativeKeyEvent.VC_Y:
                return java.awt.event.KeyEvent.VK_Y;
            case NativeKeyEvent.VC_Z:
                return java.awt.event.KeyEvent.VK_Z;
            case NativeKeyEvent.VC_0:
                return java.awt.event.KeyEvent.VK_0;
            case NativeKeyEvent.VC_1:
                return java.awt.event.KeyEvent.VK_1;
            case NativeKeyEvent.VC_2:
                return java.awt.event.KeyEvent.VK_2;
            case NativeKeyEvent.VC_3:
                return java.awt.event.KeyEvent.VK_3;
            case NativeKeyEvent.VC_4:
                return java.awt.event.KeyEvent.VK_4;
            case NativeKeyEvent.VC_5:
                return java.awt.event.KeyEvent.VK_5;
            case NativeKeyEvent.VC_6:
                return java.awt.event.KeyEvent.VK_6;
            case NativeKeyEvent.VC_7:
                return java.awt.event.KeyEvent.VK_7;
            case NativeKeyEvent.VC_8:
                return java.awt.event.KeyEvent.VK_8;
            case NativeKeyEvent.VC_9:
                return java.awt.event.KeyEvent.VK_9;
            case NativeKeyEvent.VC_ENTER:
                return java.awt.event.KeyEvent.VK_ENTER;
            case NativeKeyEvent.VC_SPACE:
                return java.awt.event.KeyEvent.VK_SPACE;
            case NativeKeyEvent.VC_TAB:
                return java.awt.event.KeyEvent.VK_TAB;
            case NativeKeyEvent.VC_ESCAPE:
                return java.awt.event.KeyEvent.VK_ESCAPE;
            case NativeKeyEvent.VC_BACKSPACE:
                return java.awt.event.KeyEvent.VK_BACK_SPACE;
            case NativeKeyEvent.VC_SHIFT:
                return java.awt.event.KeyEvent.VK_SHIFT;
            case NativeKeyEvent.VC_CONTROL:
                return java.awt.event.KeyEvent.VK_CONTROL;
            case NativeKeyEvent.VC_ALT:
                return java.awt.event.KeyEvent.VK_ALT;
            case NativeKeyEvent.VC_META:
                return java.awt.event.KeyEvent.VK_META;
            case NativeKeyEvent.VC_CAPS_LOCK:
                return java.awt.event.KeyEvent.VK_CAPS_LOCK;
            case NativeKeyEvent.VC_F1:
                return java.awt.event.KeyEvent.VK_F1;
            case NativeKeyEvent.VC_F2:
                return java.awt.event.KeyEvent.VK_F2;
            case NativeKeyEvent.VC_F3:
                return java.awt.event.KeyEvent.VK_F3;
            case NativeKeyEvent.VC_F4:
                return java.awt.event.KeyEvent.VK_F4;
            case NativeKeyEvent.VC_F5:
                return java.awt.event.KeyEvent.VK_F5;
            case NativeKeyEvent.VC_F6:
                return java.awt.event.KeyEvent.VK_F6;
            case NativeKeyEvent.VC_F7:
                return java.awt.event.KeyEvent.VK_F7;
            case NativeKeyEvent.VC_F8:
                return java.awt.event.KeyEvent.VK_F8;
            case NativeKeyEvent.VC_F9:
                return java.awt.event.KeyEvent.VK_F9;
            case NativeKeyEvent.VC_F10:
                return java.awt.event.KeyEvent.VK_F10;
            case NativeKeyEvent.VC_F11:
                return java.awt.event.KeyEvent.VK_F11;
            case NativeKeyEvent.VC_F12:
                return java.awt.event.KeyEvent.VK_F12;
            default:
                return nativeKeyCode;
        }
    }
}
package io.github.samera2022.mousemacros.app.manager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.samera2022.mousemacros.app.util.FileUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CacheManager {
    private static final String CACHE_PATH = FileUtil.getLocalStoragePath() + (FileUtil.getLocalStoragePath().toString().endsWith("/") || FileUtil.getLocalStoragePath().toString().endsWith("\\") ? "" : java.io.File.separator) + "cache.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Cache cache = loadCache();
    public static final String EXIT_ON_CLOSE = "exit_on_close";
    public static final String MINIMIZE_TO_TRAY = "minimize_to_tray";
    public static final String UNKNOWN = "";

    public static class Cache {
        public String lastLoadDirectory = "";
        public String lastSaveDirectory = "";
        public java.util.Map<String, String> windowSizeMap = new java.util.HashMap<>();
        public String defaultCloseOperation = "";
        public boolean hasReadScriptWarn = false;
    }

    public static void reloadCache() {
        cache = loadCache();
    }

    public static void saveCache() {
        saveCache(cache);
    }

    private static void saveCache(Cache c) {
        try {
            Path cachePath = Paths.get(CACHE_PATH);
            if (cachePath.getParent() != null) Files.createDirectories(cachePath.getParent());
            String json = gson.toJson(c);
            Files.write(cachePath, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Failed to save cache: " + e.getMessage());
        }
    }

    private static Cache loadCache() {
        try {
            Path cachePath = Paths.get(CACHE_PATH);
            if (Files.exists(cachePath)) {
                String json = new String(Files.readAllBytes(cachePath), StandardCharsets.UTF_8);
                return gson.fromJson(json, Cache.class);
            }
        } catch (Exception e) {
            System.err.println("Failed to load cache: " + e.getMessage());
        }
        return new Cache();
    }
}
package io.github.samera2022.mousemacros.app.manager;
import io.github.samera2022.mousemacros.api.event.events .*;
        import io.github.samera2022.mousemacros.app.action.MouseAction;
import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.constant.FileConsts;
import io.github.samera2022.mousemacros.app.event.EventManager;
import io.github.samera2022.mousemacros.app.ui.frame.MainFrame;
import javax.swing .*;
        import java.awt .*;
        import java.io .*;
        import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static io.github.samera2022.mousemacros.app.manager.LogManager.log;

public class MacroManager {
    private static boolean recording;
    private static boolean playing = false;
    private static boolean paused = false;
    private static final Object pauseLock = new Object();
    private static final List<MouseAction> actions = new ArrayList<>();
    private static long lastTime = 0;
    private static Thread playThread = null;
    private static int currentLoop = 0;
    private static int currentActionIndex = 0;

    private static void preciseSleep(long millis) throws InterruptedException {
        if (millis <= 0) {
            return;
        }
        long sleepFor = millis - 1;
        long spinFor = 1_000_000L;
        if (sleepFor > 0) {
            Thread.sleep(sleepFor);
        }
        long end = System.nanoTime() + spinFor;
        while (System.nanoTime() < end) {
        }
    }

    public static void startRecording() {
        long startTime = System.currentTimeMillis();
        Point initialMousePos = MouseInfo.getPointerInfo().getLocation();
        BeforeRecordStartEvent event = new BeforeRecordStartEvent(startTime, initialMousePos);
        EventManager.callEvent(event);
        if (event.isCancelled()) return;
        actions.clear();
        recording = true;
        lastTime = System.currentTimeMillis();
        log(Localizer.get("log.start_recording"));
    }

    public static void stopRecording() {
        recording = false;
        EventManager.callEvent(new AfterRecordStopEvent(new ArrayList<>(actions)));
        log(Localizer.get("log.stop_recording_msg1") + actions.size() + Localizer.get("log.stop_recording_msg2"));
    }

    public static void play() {
        if (paused) {
            resume();
            return;
        }
        if (playing) {
            return;
        }
        if (actions.isEmpty()) {
            log(Localizer.get("log.no_recorded_actions"));
            return;
        }
        int repeatTime = ConfigManager.getInt("repeat_times");
        if (repeatTime <= 0) repeatTime = 1;
        BeforePlaybackStartEvent event = new BeforePlaybackStartEvent(new ArrayList<>(actions), repeatTime);
        EventManager.callEvent(event);
        if (event.isCancelled()) return;
        log(Localizer.get("log.start_playback"));
        playing = true;
        paused = false;
        currentLoop = 0;
        currentActionIndex = 0;
        int finalRepeatTime = repeatTime;
        playThread = new Thread(() -> {
            String abortReason = "COMPLETED";
            try {
                for (int i = 0; i < finalRepeatTime; i++) {
                    currentLoop = i;
                    if (!playing || Thread.interrupted()) {
                        abortReason = "INTERRUPTED";
                        return;
                    }
                    for (int j = 0; j < actions.size(); j++) {
                        currentActionIndex = j;
                        MouseAction action = actions.get(j);
                        BeforeStepExecuteEvent stepEvent = new BeforeStepExecuteEvent(j, action, j == actions.size() - 1);
                        EventManager.callEvent(stepEvent);
                        if (stepEvent.isCancelled()) {
                            EventManager.callEvent(new AfterStepExecuteEvent(j, "SKIPPED"));
                            continue;
                        }
                        synchronized (pauseLock) {
                            while (paused) {
                                pauseLock.wait();
                            }
                        }
                        if (!playing || Thread.interrupted()) {
                            abortReason = "INTERRUPTED";
                            return;
                        }
                        long sleepTime = ConfigManager.getBoolean("enable_quick_mode") ? 0 : action.delay;
                        preciseSleep(sleepTime);
                        action.perform();
                        EventManager.callEvent(new AfterStepExecuteEvent(j, "SUCCESS"));
                    }
                    OnLoopCompleteEvent loopEvent = new OnLoopCompleteEvent(i + 1, finalRepeatTime - (i + 1));
                    EventManager.callEvent(loopEvent);
                }
            } catch (InterruptedException e) {
                abortReason = "INTERRUPTED";
                log(Localizer.get("log.macro_aborted"));
            } catch (Exception e) {
                abortReason = "EXCEPTION: " + e.getMessage();
                log(Localizer.get("log.playback_error") + e.getMessage());
            } finally {
                playing = false;
                paused = false;
                playThread = null;
                if (!abortReason.equals("COMPLETED")) {
                    EventManager.callEvent(new OnPlaybackAbortedEvent(abortReason, currentActionIndex));
                }
                SwingUtilities.invokeLater(MainFrame.MAIN_FRAME::refreshSpecialTexts);
            }
        });
        playThread.start();
    }

    public static boolean isRecording() {
        return recording;
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static boolean isPaused() {
        return paused;
    }

    public static void pause() {
        if (playing && !paused) {
            paused = true;
            log(Localizer.get("log.macro_paused"));
            SwingUtilities.invokeLater(MainFrame.MAIN_FRAME::refreshSpecialTexts);
        }
    }

    public static void resume() {
        if (playing && paused) {
            synchronized (pauseLock) {
                paused = false;
                pauseLock.notifyAll();
            }
            log(Localizer.get("log.macro_resumed"));
            SwingUtilities.invokeLater(MainFrame.MAIN_FRAME::refreshSpecialTexts);
        }
    }

    public static void abort() {
        playing = false;
        paused = false;
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
        if (playThread != null && playThread.isAlive()) {
            playThread.interrupt();
        }
        SwingUtilities.invokeLater(MainFrame.MAIN_FRAME::refreshSpecialTexts);
    }

    public static void recordAction(MouseAction action) {
        OnActionAddedEvent event = new OnActionAddedEvent(action, actions.size());
        EventManager.callEvent(event);
        if (event.isCancelled()) return;
        actions.add(action);
        lastTime = System.currentTimeMillis();
    }

    public static long getLastTime() {
        return lastTime;
    }

    public static void setLastTime(long t) {
        lastTime = t;
    }

    public static List<MouseAction> getActions() {
        return actions;
    }

    public static void saveToFile(Component parent) {
        JFileChooser chooser = new JFileChooser();
        if (ConfigManager.getBoolean("enable_default_storage")) {
            String defaultPath = ConfigManager.getString("default_mmc_storage_path");
            if (defaultPath != null && !defaultPath.isEmpty()) {
                File dir = new File(defaultPath);
                if (!dir.exists()) dir.mkdirs();
                chooser.setCurrentDirectory(dir);
            }
        } else {
            String lastSaveDir = CacheManager.cache.lastSaveDirectory;
            if (lastSaveDir != null && !lastSaveDir.isEmpty()) {
                File dir = new File(lastSaveDir);
                if (!dir.exists()) dir.mkdirs();
                chooser.setCurrentDirectory(dir);
            }
        }
        chooser.setFileFilter(FileConsts.MMC_FILTER);
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".mmc"))
                selectedFile = new File(selectedFile.getAbsolutePath() + ".mmc");
            CacheManager.cache.lastSaveDirectory = selectedFile.getParent();
            CacheManager.saveCache();
            if (CacheManager.cache.lastLoadDirectory == null || CacheManager.cache.lastLoadDirectory.isEmpty()) {
                CacheManager.cache.lastLoadDirectory = selectedFile.getParent();
                CacheManager.saveCache();
            }
            String content = actions.stream()
                    .map(a -> a.x + "," + a.y + "," + a.type + "," + a.button + "," + a.delay + "," + a.wheelAmount + "," + a.keyCode + "," + a.awtKeyCode)
                    .collect(Collectors.joining("\n"));
            OnMacroSaveEvent event = new OnMacroSaveEvent(selectedFile, content);
            EventManager.callEvent(event);
            try (PrintWriter out = new PrintWriter(selectedFile, StandardCharsets.UTF_8)) {
                out.print(event.getFormattedContent());
                log(Localizer.get("log.macro_saved") + chooser.getSelectedFile().getAbsolutePath());
            } catch (Exception ex) {
                log(Localizer.get("log.macro_saving_failed") + ex.getMessage());
            }
        }
    }

    public static void loadFromFile(Component parent) {
        JFileChooser chooser = new JFileChooser();
        if (ConfigManager.getBoolean("enable_default_storage")) {
            String defaultPath = ConfigManager.getString("default_mmc_storage_path");
            if (defaultPath != null && !defaultPath.isEmpty()) {
                File dir = new File(defaultPath);
                if (!dir.exists()) dir.mkdirs();
                chooser.setCurrentDirectory(dir);
            }
        } else {
            String lastLoadDir = CacheManager.cache.lastLoadDirectory;
            if (lastLoadDir != null && !lastLoadDir.isEmpty()) {
                File dir = new File(lastLoadDir);
                if (!dir.exists()) dir.mkdirs();
                chooser.setCurrentDirectory(dir);
            }
        }
        chooser.setFileFilter(FileConsts.MMC_FILTER);
        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            CacheManager.cache.lastLoadDirectory = selectedFile.getParent();
            CacheManager.saveCache();
            if (CacheManager.cache.lastSaveDirectory == null || CacheManager.cache.lastSaveDirectory.isEmpty()) {
                CacheManager.cache.lastSaveDirectory = selectedFile.getParent();
                CacheManager.saveCache();
            }
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile), StandardCharsets.UTF_8))) {
                String rawContent = in.lines().collect(Collectors.joining("\n"));
                OnMacroLoadEvent event = new OnMacroLoadEvent(selectedFile, rawContent);
                EventManager.callEvent(event);
                String[] lines = event.getRawContent().split("\n");
                actions.clear();
                int lineNum = 0;
                for (String line : lines) {
                    lineNum++;
                    try {
                        String[] arr = line.split(",");
                        if (arr.length == 8) {
                            int x = Integer.parseInt(arr[0]);
                            int y = Integer.parseInt(arr[1]);
                            int type = Integer.parseInt(arr[2]);
                            int button = Integer.parseInt(arr[3]);
                            long delay = Long.parseLong(arr[4]);
                            int wheelAmount = Integer.parseInt(arr[5]);
                            int keyCode = Integer.parseInt(arr[6]);
                            int awtKeyCode = Integer.parseInt(arr[7]);
                            actions.add(new MouseAction(x, y, type, button, delay, wheelAmount, keyCode, awtKeyCode));
                        } else if (arr.length == 7) {
                            int x = Integer.parseInt(arr[0]);
                            int y = Integer.parseInt(arr[1]);
                            int type = Integer.parseInt(arr[2]);
                            int button = Integer.parseInt(arr[3]);
                            long delay = Long.parseLong(arr[4]);
                            int wheelAmount = Integer.parseInt(arr[5]);
                            int keyCode = Integer.parseInt(arr[6]);
                            actions.add(new MouseAction(x, y, type, button, delay, wheelAmount, keyCode, 0));
                        } else if (arr.length == 6) {
                            int x = Integer.parseInt(arr[0]);
                            int y = Integer.parseInt(arr[1]);
                            int type = Integer.parseInt(arr[2]);
                            int button = Integer.parseInt(arr[3]);
                            long delay = Long.parseLong(arr[4]);
                            int wheelAmount = Integer.parseInt(arr[5]);
                            actions.add(new MouseAction(x, y, type, button, delay, wheelAmount, 0, 0));
                        } else if (arr.length == 5) {
                            int x = Integer.parseInt(arr[0]);
                            int y = Integer.parseInt(arr[1]);
                            int type = Integer.parseInt(arr[2]);
                            int button = Integer.parseInt(arr[3]);
                            long delay = Long.parseLong(arr[4]);
                            actions.add(new MouseAction(x, y, type, button, delay, 0, 0, 0));
                        }
                    } catch (Exception ex) {
                        log(Localizer.get("log.macro_loading_line_error") + lineNum + ": " + ex.getMessage());
                    }
                }
                log(Localizer.get("log.macro_loaded_msg1") + selectedFile.getAbsolutePath() + " (" + actions.size() + " " + Localizer.get("log.macro_loaded_msg2") + ")");
            } catch (Exception ex) {
                log(Localizer.get("log.macro_loading_failed") + ex.getMessage());
            }
        }
    }
}
package io.github.samera2022.mousemacros.app.script;
import io.github.samera2022.mousemacros.api.event.Event;
import io.github.samera2022.mousemacros.api.event.EventHandler;
import io.github.samera2022.mousemacros.api.event.Listener;
import io.github.samera2022.mousemacros.api.script.IScriptAPI;
import io.github.samera2022.mousemacros.app.event.EventManager;
import io.github.samera2022.mousemacros.app.manager.LogManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScriptAPI implements IScriptAPI {
    private final List<Listener> registeredListeners = new ArrayList<>();
    private final ScriptContext scriptContext;

    public ScriptAPI(ScriptContext scriptContext) {
        this.scriptContext = scriptContext;
    }

    @Override
    public void on(String eventClassName, Consumer<Event> callback) {
        try {
            Class<?> eventClass = Class.forName(eventClassName);
            if (Event.class.isAssignableFrom(eventClass)) {
                ScriptEventListener listener = new ScriptEventListener(eventClass, callback);
                EventManager.registerEvents(listener);
                registeredListeners.add(listener);
            } else {
                System.err.println("ScriptAPI Error: Class " + eventClassName + " is not a valid Event.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("ScriptAPI Error: Event class not found - " + eventClassName);
        }
    }

    @Override
    public void cleanup() {
        for (Listener listener : registeredListeners) {
            EventManager.unregisterEvents(listener);
        }
        registeredListeners.clear();
    }

    @Override
    public ScriptContext getContext() {
        return this.scriptContext;
    }

    @Override
    public void log(String message) {
        LogManager.log(message);
    }

    private static class ScriptEventListener implements Listener {
        private final Class<?> targetEventClass;
        private final Consumer<Event> callback;

        public ScriptEventListener(Class<?> targetEventClass, Consumer<Event> callback) {
            this.targetEventClass = targetEventClass;
            this.callback = callback;
        }

        @EventHandler
        public void onGenericEvent(Event event) {
            if (targetEventClass.isInstance(event)) {
                try {
                    callback.accept(event);
                } catch (Exception e) {
                    System.err.println("Error executing script callback for event: " + event.getClass().getSimpleName());
                    e.printStackTrace();
                }
            }
        }
    }
}
package io.github.samera2022.mousemacros.app.script;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.config.WhitelistManager;
import org.graalvm.polyglot .*;
        import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util .*;
        import java.util.concurrent.ConcurrentHashMap;
import static io.github.samera2022.mousemacros.app.manager.LogManager.log;

public class ScriptManager {
    private static final Map<ScriptPlugin, Context> scriptContexts = new ConcurrentHashMap<>();
    private static final List<ScriptPlugin> scripts = new ArrayList<>();
    public static String SCRIPT_PATH;

    static {
        SCRIPT_PATH = ConfigManager.CONFIG_DIR + "/scripts/";
        File dir = new File(SCRIPT_PATH);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Failed to create script directory: " + SCRIPT_PATH);
        }
    }

    public static synchronized void loadAndProcessScripts() {
        scriptContexts.values().forEach(Context::close);
        scriptContexts.clear();
        scripts.clear();
        File dir = new File(SCRIPT_PATH);
        if (!dir.exists()) return;
        File[] files = dir.listFiles((d, name) -> name.endsWith(".js") || name.endsWith(".js.disabled"));
        if (files == null) return;
        for (File file : files) {
            ScriptDescription metadata = readMetadataOnly(file);
            ScriptPlugin scriptPlugin = new ScriptPlugin(file, metadata);
            boolean whitelisted = WhitelistManager.isWhitelisted(metadata);
            if (metadata.isRequiresNativeAccess() && !whitelisted && !file.getName().endsWith(".disabled")) {
                System.out.println("Script '" + file.getName() + "' requires native access and is not whitelisted. Disabling by default.");
                File newFile = new File(file.getParent(), file.getName() + ".disabled");
                try {
                    Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    scriptPlugin.setFile(newFile);
                    scriptPlugin.setEnabled(false);
                } catch (IOException e) {
                    System.err.println("Failed to auto-disable script: " + file.getName());
                    e.printStackTrace();
                }
            }
            if (metadata.isRequiresNativeAccess() && !whitelisted) {
                scriptPlugin.addIssue(new ScriptIssue(ScriptProblem.REQUIRE_NATIVE_ACCESS_CONFIRMATION, null));
            }
            scripts.add(scriptPlugin);
        }
        checkDependencies();
        for (ScriptPlugin script : scripts) {
            if (script.isEnabled()) {
                if (script.getDescription().isRequiresNativeAccess()) {
                    if (WhitelistManager.isWhitelisted(script.getDescription())) {
                        enableScript(script, true);
                    } else {
                        System.err.println("Security Warning: Cannot auto-enable non-whitelisted native script: " + script.getName());
                    }
                } else {
                    enableScript(script, false);
                }
            }
        }
    }

    public static void checkDependencies() {
        Map<String, ScriptPlugin> scriptMap = new HashMap<>();
        for (ScriptPlugin script : scripts) {
            script.clearIssues();
            scriptMap.put(script.getRegisterName(), script);
        }
        Set<ScriptPlugin> visited = new HashSet<>();
        Set<ScriptPlugin> recursionStack = new HashSet<>();
        for (ScriptPlugin script : scripts) {
            checkScriptHealth(script, scriptMap, visited, recursionStack);
        }
    }

    private static void checkScriptHealth(ScriptPlugin script, Map<String, ScriptPlugin> scriptMap, Set<ScriptPlugin> visited, Set<ScriptPlugin> recursionStack) {
        if (recursionStack.contains(script)) {
            return;
        }
        if (visited.contains(script)) return;
        recursionStack.add(script);
        for (String depName : script.getDescription().getHardDependencies()) {
            ScriptPlugin dep = scriptMap.get(depName);
            if (dep == null) {
                script.addIssue(new ScriptIssue(ScriptProblem.H_DEP_MISSING, new String[]{depName}));
            } else {
                checkScriptHealth(dep, scriptMap, visited, recursionStack);
                if (dep.hasSevereIssue()) {
                    script.addIssue(new ScriptIssue(ScriptProblem.H_DEP_PROBLEM_SEVERE, new String[]{depName}));
                } else if (dep.hasIssue()) {
                    script.addIssue(new ScriptIssue(ScriptProblem.H_DEP_PROBLEM_NOT_SEVERE, new String[]{depName}));
                }
            }
        }
        for (String depName : script.getDescription().getSoftDependencies()) {
            ScriptPlugin dep = scriptMap.get(depName);
            if (dep != null) {
                checkScriptHealth(dep, scriptMap, visited, recursionStack);
            }
        }
        visited.add(script);
        recursionStack.remove(script);
    }

    String fileName = scriptFile.getName();
    String baseName = fileName.endsWith(".disabled") ? fileName.substring(0, fileName.length() - ".disabled".length()) : fileName;
    String registerNameDefault = baseName.lastIndexOf('.') > 0 ? baseName.substring(0, baseName.lastIndexOf('.')) : baseName;
    String author = "Unknown", version = "0.0.0", description = "No description", displayName = baseName, registerName = registerNameDefault, availableVersion = "0.0.0";
    boolean requiresNativeAccess = false;
    String nativeAccessDescription = "";
    String[] softDependencies = new String[0];
    String[] hardDependencies = new String[0];
        try(
    Context tempContext = Context.newBuilder("js")
            .option("engine.WarnInterpreterOnly", "false")
            .build())

    {
        Source source = Source.newBuilder("js", scriptFile).build();
        try {
            tempContext.eval(source);
        } catch (PolyglotException e) {
        }
        Value bindings = tempContext.getBindings("js");
        if (bindings.hasMember("display_name")) displayName = bindings.getMember("display_name").asString();
        if (bindings.hasMember("register_name")) registerName = bindings.getMember("register_name").asString();
        if (bindings.hasMember("registry_name")) registerName = bindings.getMember("registry_name").asString();
        if (bindings.hasMember("author")) author = bindings.getMember("author").asString();
        if (bindings.hasMember("version")) version = bindings.getMember("version").asString();
        if (bindings.hasMember("description")) description = bindings.getMember("description").asString();
        if (bindings.hasMember("available_version"))
            availableVersion = bindings.getMember("available_version").asString();
        if (bindings.hasMember("requireNativeAccess")) {
            requiresNativeAccess = bindings.getMember("requireNativeAccess").asBoolean();
        }
        if (bindings.hasMember("requireNativeAccessDescription")) {
            nativeAccessDescription = bindings.getMember("requireNativeAccessDescription").asString();
        }
        softDependencies = readStringArray(bindings, "soft_dependencies");
        hardDependencies = readStringArray(bindings, "hard_dependencies");
    } catch(
    IOException e)

    {
        System.err.println("Error reading script file for metadata: " + fileName);
        e.printStackTrace();
    }
        return new

    ScriptDescription(baseName, version, author, description, displayName, registerName, availableVersion, softDependencies, hardDependencies, requiresNativeAccess, nativeAccessDescription);
}

private static String[] readStringArray(Value bindings, String key) {
    if (bindings == null || !bindings.hasMember(key)) {
        return new String[0];
    }
    Value value = bindings.getMember(key);
    if (value == null || value.isNull()) {
        return new String[0];
    }
    if (value.hasArrayElements()) {
        int size = (int) value.getArraySize();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            Value element = value.getArrayElement(i);
            result[i] = element != null && element.isString() ? element.asString() : String.valueOf(element);
        }
        return result;
    }
    if (value.isString()) {
        return new String[]{value.asString()};
    }
    return new String[]{String.valueOf(value)};
}

private static void loadScriptContext(ScriptPlugin script, Context context) throws IOException, PolyglotException {
    ScriptContext scriptContext = new ScriptContext(script.getFile(), script.getDescription());
    ScriptAPI api = new ScriptAPI(scriptContext);
    script.setApiInstance(api);
    context.getBindings("js").putMember("mm", api);
    Source source = Source.newBuilder("js", script.getFile()).build();
    context.eval(source);
}

public static void enableScript(ScriptPlugin script) {
    if (script.getDescription().isRequiresNativeAccess()) {
        System.err.println("Cannot enable script '" + script.getName() + "' without explicit user consent for native access.");
        return;
    }
    enableScript(script, false);
}

public static void enableScript(ScriptPlugin script, boolean grantNativeAccess) {
    if (script.isEnabled() && scriptContexts.containsKey(script)) return;
    if (grantNativeAccess && !script.getDescription().isRequiresNativeAccess()) {
        System.err.println("Security Warning: Attempted to grant native access to a script that did not request it. Aborting.");
        return;
    }
    File currentFile = script.getFile();
    if (currentFile.getName().endsWith(".disabled")) {
        String newFileName = currentFile.getName().substring(0, currentFile.getName().length() - ".disabled".length());
        File newFile = new File(currentFile.getParent(), newFileName);
        try {
            Files.move(currentFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            script.setFile(newFile);
        } catch (IOException e) {
            System.err.println("Failed to rename file for enabling script: " + currentFile.getName());
            e.printStackTrace();
            return;
        }
    }
    try {
        log("[Script] Enabling ... " + script.getName() + (grantNativeAccess ? " [Native Access Granted]" : ""));
        Context context = Context.newBuilder("js")
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(className -> className.startsWith("io.github.samera2022.mousemacros.api"))
                .allowIO(true)
                .allowEnvironmentAccess(EnvironmentAccess.INHERIT)
                .allowNativeAccess(grantNativeAccess)
                .option("engine.WarnInterpreterOnly", "false")
                .build();
        scriptContexts.put(script, context);
        loadScriptContext(script, context);
        script.setLoaded(true);
        script.setEnabled(true);
        log("[Script] Enabled √ " + script.getName() + (grantNativeAccess ? " [Native Access Granted]" : ""));
    } catch (IOException | PolyglotException e) {
        log("Failed to enable script: " + script.getName());
        e.printStackTrace();
        script.setLoaded(false);
        script.setEnabled(false);
        Context context = scriptContexts.remove(script);
        if (context != null) context.close();
    }
}

public static void disableScript(ScriptPlugin script) {
    if (!script.isEnabled()) return;
    log("[Script] Disabling ... " + script.getName());
    if (script.getApiInstance() != null) {
        script.getApiInstance().cleanup();
        script.setApiInstance(null);
    }
    Context context = scriptContexts.remove(script);
    if (context != null) context.close();
    File currentFile = script.getFile();
    if (!currentFile.getName().endsWith(".disabled")) {
        String newFileName = currentFile.getName() + ".disabled";
        File newFile = new File(currentFile.getParent(), newFileName);
        try {
            Files.move(currentFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            script.setFile(newFile);
            log("[Script] Disabled √ " + script.getName());
        } catch (IOException e) {
            log("Failed to rename script: " + script.getName());
            e.printStackTrace();
        }
    }
    script.setEnabled(false);
    script.setLoaded(false);
}

public static List<ScriptPlugin> getScripts() {
    return new ArrayList<>(scripts);
}
}
        package io.github.samera2022.mousemacros.app.ui.frame;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;
import io.github.samera2022.mousemacros.app.listener.GlobalMouseListener;
import io.github.samera2022.mousemacros.app.manager.CacheManager;
import io.github.samera2022.mousemacros.app.manager.MacroManager;
import io.github.samera2022.mousemacros.app.ui.frame.settings.HotkeyDialog;
import io.github.samera2022.mousemacros.app.util.ComponentUtil;
import io.github.samera2022.mousemacros.app.util.OtherUtil;
import io.github.samera2022.mousemacros.app.util.SystemUtil;
import javax.swing .*;
        import javax.swing.text.DefaultStyledDocument;
import java.awt .*;
        import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import static io.github.samera2022.mousemacros.app.manager.LogManager.log;

public class MainFrame extends JFrame {
    public static JTextArea logArea;
    private static DefaultStyledDocument dsd = new DefaultStyledDocument();
    public static Point point;
    private final JButton startBtn, stopBtn, playBtn, saveBtn, loadBtn, settingsBtn, abortBtn;
    public static int keyRecord = NativeKeyEvent.VC_F2;
    public static int keyStop = NativeKeyEvent.VC_F3;
    public static int keyPlay = NativeKeyEvent.VC_F4;
    public static int keyAbort = NativeKeyEvent.VC_F5;
    public static final GlobalMouseListener GML = new GlobalMouseListener();
    public static MainFrame MAIN_FRAME = new MainFrame();
    private TrayIcon trayIcon;
    private SystemTray tray;

    public MainFrame() {
        if (ConfigManager.getBoolean("follow_system_settings")) {
            String[] availableLangs = ConfigManager.getAvailableLangs();
            ConfigManager.set("switch_lang", SystemUtil.getSystemLang(availableLangs));
            ConfigManager.set("enable_dark_mode", SystemUtil.isSystemDarkMode());
        }
        Localizer.load(ConfigManager.getString("switch_lang"));
        setTitle(Localizer.get("main_frame"));
        setName("main_frame");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage());
        boolean enableLangSwitch = true;
        Localizer.setRuntimeSwitch(enableLangSwitch);
        Map<String, String> keyMap = ConfigManager.getKeyMap();
        if (keyMap != null) {
            if (keyMap.containsKey("start_record")) {
                try {
                    keyRecord = Integer.parseInt(keyMap.get("start_record"));
                } catch (Exception ignored) {
                }
            }
            if (keyMap.containsKey("stop_record")) {
                try {
                    keyStop = Integer.parseInt(keyMap.get("stop_record"));
                } catch (Exception ignored) {
                }
            }
            if (keyMap.containsKey("play_macro")) {
                try {
                    keyPlay = Integer.parseInt(keyMap.get("play_macro"));
                } catch (Exception ignored) {
                }
            }
            if (keyMap.containsKey("abort_macro_operation")) {
                try {
                    keyAbort = Integer.parseInt(keyMap.get("abort_macro_operation"));
                } catch (Exception ignored) {
                }
            }
        }
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        logArea = new JTextArea(dsd);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        startBtn = new JButton(getStartBtnText());
        stopBtn = new JButton(getStopBtnText());
        playBtn = new JButton(getPlayBtnText());
        row1.add(startBtn);
        row1.add(stopBtn);
        row1.add(playBtn);
        abortBtn = new JButton(getAbortBtnText());
        saveBtn = new JButton(Localizer.get("main_frame.save_macro"));
        loadBtn = new JButton(Localizer.get("main_frame.load_macro"));
        settingsBtn = new JButton(Localizer.get("settings"));
        row2.add(abortBtn);
        row2.add(saveBtn);
        row2.add(loadBtn);
        row2.add(settingsBtn);
        panel.add(row1);
        panel.add(row2);
        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.add(separator, BorderLayout.NORTH);
        southContainer.add(panel, BorderLayout.CENTER);
        add(southContainer, BorderLayout.SOUTH);
        startBtn.addActionListener(e -> {
            if ((!MacroManager.isRecording()) && (!HotkeyDialog.inHotKeyDialog)) MacroManager.startRecording();
        });
        stopBtn.addActionListener(e -> {
            if ((MacroManager.isRecording()) && (!HotkeyDialog.inHotKeyDialog)) MacroManager.stopRecording();
            else log(Localizer.get("log.macro_not_recording"));
        });
        playBtn.addActionListener(e -> {
            if ((!MacroManager.isRecording()) && (!HotkeyDialog.inHotKeyDialog)) {
                if (MacroManager.isPaused()) {
                    MacroManager.resume();
                } else {
                    MacroManager.play();
                }
            }
        });
        abortBtn.addActionListener(e -> {
            if ((MacroManager.isPlaying()) && (!HotkeyDialog.inHotKeyDialog)) {
                if (MacroManager.isPaused()) {
                    MacroManager.abort();
                } else {
                    MacroManager.pause();
                }
            } else {
                log(Localizer.get("log.macro_not_running"));
            }
        });
        saveBtn.addActionListener(e -> {
            if (!MacroManager.isRecording()) MacroManager.saveToFile(this);
        });
        loadBtn.addActionListener(e -> {
            if (!MacroManager.isRecording()) MacroManager.loadFromFile(this);
        });
        settingsBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new SettingsDialog().setVisible(true)));
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            log(Localizer.get("log.hook_registration_failed") + e.getMessage());
        }
        GlobalScreen.addNativeKeyListener(GML);
        GlobalScreen.addNativeMouseListener(GML);
        GlobalScreen.addNativeMouseWheelListener(GML);
        GlobalScreen.addNativeMouseMotionListener(GML);
        readjust();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        if (point != null) setLocation(point);
        addWindowListener(new WindowClosingAdapter());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                switch (CacheManager.cache.defaultCloseOperation) {
                    case CacheManager.UNKNOWN:
                        new ExitDialog(MainFrame.this).setVisible(true);
                        break;
                    case CacheManager.EXIT_ON_CLOSE:
                        System.exit(0);
                        break;
                    case CacheManager.MINIMIZE_TO_TRAY:
                        minimizeToTray();
                        break;
                }
            }
        });
    }

    public void refreshMainFrameTexts() {
        setTitle(Localizer.get("main_frame"));
        refreshSpecialTexts();
        saveBtn.setText(Localizer.get("main_frame.save_macro"));
        loadBtn.setText(Localizer.get("main_frame.load_macro"));
        settingsBtn.setText(Localizer.get("settings"));
        if (trayIcon != null) {
            trayIcon.setToolTip(Localizer.get("main_frame"));
            if (trayIcon.getPopupMenu() != null && trayIcon.getPopupMenu().getItemCount() >= 2) {
                trayIcon.getPopupMenu().getItem(0).setLabel(Localizer.get("tray.show_main_menu"));
                trayIcon.getPopupMenu().getItem(2).setLabel(Localizer.get("tray.exit"));
            }
        }
    }

    public void refreshSpecialTexts() {
        startBtn.setText(getStartBtnText());
        stopBtn.setText(getStopBtnText());
        playBtn.setText(getPlayBtnText());
        abortBtn.setText(getAbortBtnText());
    }

    private String getStartBtnText() {
        return Localizer.get("main_frame.start_record") + " (" + OtherUtil.getNativeKeyDisplayText(keyRecord) + ")";
    }

    private String getStopBtnText() {
        return Localizer.get("main_frame.stop_record") + " (" + OtherUtil.getNativeKeyDisplayText(keyStop) + ")";
    }

    private String getPlayBtnText() {
        if (MacroManager.isPaused()) {
            return Localizer.get("main_frame.resume_macro") + " (" + OtherUtil.getNativeKeyDisplayText(keyPlay) + ")";
        }
        return Localizer.get("main_frame.play_macro") + " (" + OtherUtil.getNativeKeyDisplayText(keyPlay) + ")";
    }

    private String getAbortBtnText() {
        if (MacroManager.isPaused()) {
            return Localizer.get("main_frame.abort_macro_operation") + " (" + OtherUtil.getNativeKeyDisplayText(keyAbort) + ")";
        }
        return Localizer.get("main_frame.pause_macro") + " (" + OtherUtil.getNativeKeyDisplayText(keyAbort) + ")";
    }

    private void initTrayIcon() {
        if (!SystemTray.isSupported()) return;
        if (tray == null) {
            tray = SystemTray.getSystemTray();
        }
        if (trayIcon == null) {
            Image trayImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage();
            PopupMenu popupMenu = new PopupMenu();
            MenuItem showItem = new MenuItem(Localizer.get("tray.show_main_menu"));
            MenuItem exitItem = new MenuItem(Localizer.get("tray.exit"));
            popupMenu.add(showItem);
            popupMenu.addSeparator();
            popupMenu.add(exitItem);
            trayIcon = new TrayIcon(trayImage, Localizer.get("main_frame"), popupMenu);
            trayIcon.setImageAutoSize(true);
            showItem.addActionListener(e -> restoreFromTray());
            exitItem.addActionListener(e -> {
                tray.remove(trayIcon);
                System.exit(0);
            });
            trayIcon.addActionListener(e -> restoreFromTray());
        }
    }

    public void readjust() {
        ComponentUtil.adjustFrameWithCache(this, 0, new JComponent[]{logArea}, new JComponent[]{startBtn, stopBtn, playBtn}, new JComponent[]{abortBtn, saveBtn, loadBtn, settingsBtn});
    }

    public void minimizeToTray() {
        initTrayIcon();
        if (tray != null && trayIcon != null) {
            try {
                tray.add(trayIcon);
                setVisible(false);
            } catch (AWTException ex) {
                log(Localizer.get("log.adding_tray_failed") + ":" + ex.getMessage());
                dispose();
            }
        } else {
            dispose();
        }
    }

    private void restoreFromTray() {
        setVisible(true);
        setExtendedState(JFrame.NORMAL);
        if (tray != null && trayIcon != null) {
            tray.remove(trayIcon);
        }
        toFront();
    }
}
package io.github.samera2022.mousemacros.app.ui.frame;
import com.formdev.flatlaf.FlatLaf;
import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.adapter.CompMouseAdapter;
import io.github.samera2022.mousemacros.app.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.constant.IconConsts;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;
import io.github.samera2022.mousemacros.app.manager.CacheManager;
import io.github.samera2022.mousemacros.app.ui.component.DocumentInputFilter;
import io.github.samera2022.mousemacros.app.ui.frame.settings .*;
        import io.github.samera2022.mousemacros.app.util.ConsoleOutputCapturer;
import io.github.samera2022.mousemacros.app.util.FileUtil;
import io.github.samera2022.mousemacros.app.util.SystemUtil;
import javax.swing .*;
        import java.awt .*;
        import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import static io.github.samera2022.mousemacros.app.ui.frame.MainFrame.MAIN_FRAME;

public class SettingsDialog extends JDialog {
    public static final String REPORT_PATH = ConfigManager.CONFIG_DIR + "/reports";
    private static final File REPORTS_DIR = new File(REPORT_PATH);
    private final Map<String, JComponent> componentMap = new HashMap<>();

    static {
        if (!REPORTS_DIR.exists()) {
            REPORTS_DIR.mkdirs();
        }
    }

    public SettingsDialog() {
        setTitle(Localizer.get("settings"));
        setName("settings");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage());
        setModal(true);
        setLayout(new BorderLayout(10, 10));
        JPanel content = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        JPanel savePanel = createSavePanel();
        add(savePanel, BorderLayout.SOUTH);
        refreshUI(true);
        setPreferredSize(new Dimension(750, 500));
        pack();
        setLocationRelativeTo(null);
        addWindowListener(new WindowClosingAdapter());
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        JLabel settingTitle = new JLabel(Localizer.get("settings"));
        settingTitle.setFont(settingTitle.getFont().deriveFont(Font.BOLD, 22f));
        settingTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(settingTitle);
        content.add(Box.createVerticalStrut(10));
        for (SettingsRegistry setting : SettingsRegistry.values()) {
            if (setting.type == OtherConsts.SECTION) {
                content.add(Box.createVerticalStrut(10));
                content.add(new JSeparator());
                content.add(Box.createVerticalStrut(10));
                JLabel sectionTitle = new JLabel(Localizer.getS(setting.i18nKey));
                sectionTitle.setFont(settingTitle.getFont().deriveFont(Font.BOLD, 18f));
                sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                content.add(sectionTitle);
                content.add(Box.createVerticalStrut(3));
            } else {
                content.add(createSettingComponent(setting));
            }
        }
        content.add(Box.createVerticalStrut(10));
        content.add(createOtherButtonsPanel());
        return content;
    }

    private JComponent createSettingComponent(SettingsRegistry setting) {
        JComponent component;
        switch (setting.type) {
            case OtherConsts.CHECK_BOX:
                component = createCheckBox(setting);
                break;
            case OtherConsts.TEXT_FIELD:
                component = createTextField(setting);
                break;
            case OtherConsts.COMBO_BOX:
                component = createComboBox(setting);
                break;
            case OtherConsts.FILE_CHOOSER:
                component = createFileChooserComponent(setting);
                break;
            case OtherConsts.SPECIFIC_TEXT_FIELD:
                component = createSpecificTextField(setting);
                break;
            case OtherConsts.SECTION:
                return new JLabel();
            default:
                throw new IllegalArgumentException("Unsupported component type for " + setting.i18nKey);
        }
        componentMap.put(setting.i18nKey, component);
        return component;
    }

    private JComponent createCheckBox(SettingsRegistry setting) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel label = new JLabel(Localizer.getS(setting.i18nKey));
        JCheckBox checkBox = new JCheckBox(IconConsts.CHECK_BOX);
        checkBox.setName(setting.i18nKey);
        addTooltip(label, checkBox, setting);
        checkBox.addActionListener(e -> {
            ConfigManager.set(setting.i18nKey, checkBox.isSelected());
            refreshUI(false);
        });
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(checkBox);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (setting.relateTo != null) {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
        }
        return panel;
    }

    private JComponent createTextField(SettingsRegistry setting) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel label = new JLabel(Localizer.getS(setting.i18nKey));
        JTextField textField = new JTextField(setting.columns);
        textField.setName(setting.i18nKey);
        addTooltip(label, textField, setting);
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(textField);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (setting.relateTo != null) {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
        }
        return panel;
    }

    private JComponent createSpecificTextField(SettingsRegistry setting) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel label = new JLabel(Localizer.getS(setting.i18nKey));
        JTextField textField = new JTextField(setting.columns);
        textField.setName(setting.i18nKey);
        DocumentInputFilter filter;
        if (setting == SettingsRegistry.REPEAT_TIME) {
            filter = new DocumentInputFilter() {
                @Override
                public boolean isValidContent(String text) {
                    return text.equals("-1") || text.matches("\\d+") || text.isEmpty() || text.equals("-");
                }
            };
        } else if (setting == SettingsRegistry.REPEAT_DELAY) {
            filter = new DocumentInputFilter() {
                @Override
                public boolean isValidContent(String text) {
                    return text.matches("\\d*(\\.\\d{0,3})?") || text.isEmpty();
                }
            };
        } else {
            filter = new DocumentInputFilter() {
                @Override
                public boolean isValidContent(String text) {
                    return true;
                }
            };
        }
        ((javax.swing.text.AbstractDocument) textField.getDocument()).setDocumentFilter(filter);
        addTooltip(label, textField, setting);
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(textField);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (setting.relateTo != null) {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
        }
        return panel;
    }

    private JComponent createFileChooserComponent(SettingsRegistry setting) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel label = new JLabel(Localizer.getS(setting.i18nKey));
        JTextField textField = new JTextField(setting.columns);
        textField.setName(setting.i18nKey);
        JButton browseBtn = new JButton(Localizer.get("settings.browse"));
        addTooltip(label, textField, setting, browseBtn);
        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (!textField.getText().isEmpty())
                chooser.setCurrentDirectory(new java.io.File(textField.getText()));
            int ret = chooser.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                textField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(textField);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(browseBtn);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (setting.relateTo != null) {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
        }
        return panel;
    }

    private JComponent createComboBox(SettingsRegistry setting) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel label = new JLabel(Localizer.getS(setting.i18nKey));
        Vector<String> items = new Vector<>();
        if (setting.size == -1) {
            items.addAll(java.util.Arrays.asList(ConfigManager.getAvailableLangs()));
        } else {
            for (int i = 1; i <= setting.size; i++) {
                items.add(Localizer.getS(setting.i18nKey + "." + i));
            }
        }
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setName(setting.i18nKey);
        addTooltip(label, comboBox, setting);
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(comboBox);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (setting.relateTo != null) {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
        }
        return panel;
    }

    private JPanel createOtherButtonsPanel() {
        JButton hotkeyBtn = new JButton(Localizer.get("settings.custom_hotkey"));
        hotkeyBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new HotkeyDialog().setVisible(true)));
        JButton pluginBtn = new JButton(Localizer.get("settings.scripts_manager"));
        pluginBtn.addActionListener(e -> {
            if (!CacheManager.cache.hasReadScriptWarn) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(new JLabel(Localizer.get("settings.scripts_manager.warn.message")), BorderLayout.CENTER);
                JCheckBox checkBox = new JCheckBox(Localizer.get("settings.scripts_manager.warn.checkbox"));
                JPanel checkboxPanel = new JPanel(new GridLayout(1, 1));
                checkboxPanel.add(checkBox);
                panel.add(checkboxPanel, BorderLayout.SOUTH);
                int option = JOptionPane.showConfirmDialog(this, panel, Localizer.get("settings.scripts_manager.warn.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (option == JOptionPane.OK_OPTION) {
                    if (checkBox.isSelected()) {
                        CacheManager.cache.hasReadScriptWarn = true;
                        CacheManager.saveCache();
                    }
                    SwingUtilities.invokeLater(() -> new ScriptDialog().setVisible(true));
                }
            } else {
                SwingUtilities.invokeLater(() -> new ScriptDialog().setVisible(true));
            }
        });
        JButton aboutBtn = new JButton(Localizer.get("settings.about_author"));
        aboutBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new AboutDialog().setVisible(true)));
        JButton updateInfoBtn = new JButton(Localizer.get("settings.update_info"));
        updateInfoBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new UpdateInfoDialog().setVisible(true)));
        JButton exportLogBtn = new JButton(Localizer.get("settings.export_log"));
        exportLogBtn.addActionListener(e -> {
            try {
                String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                String fileName = "report-" + timeStamp + ".txt";
                String filePath = REPORTS_DIR.getAbsolutePath() + File.separator + fileName;
                FileUtil.writeFile(filePath, ConsoleOutputCapturer.getOutput());
                String message = "<html>" + Localizer.get("settings.export_log.success") + "<br>" + filePath + "</html>";
                JOptionPane.showMessageDialog(this, new JLabel(message), Localizer.get("settings.success"), JOptionPane.INFORMATION_MESSAGE);
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(REPORTS_DIR);
                    } catch (IOException openEx) {
                        System.err.println("Failed to open reports directory: " + openEx.getMessage());
                    }
                }
            } catch (IOException ex) {
                String message = "<html>" + Localizer.get("settings.export_log.failed") + "<br>" + ex.getMessage() + "</html>";
                JOptionPane.showMessageDialog(this, new JLabel(message), Localizer.getS("settings.error"), JOptionPane.ERROR_MESSAGE);
            }
        });
        JPanel hotkeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hotkeyPanel.add(hotkeyBtn);
        hotkeyPanel.add(Box.createHorizontalStrut(10));
        hotkeyPanel.add(pluginBtn);
        hotkeyPanel.add(Box.createHorizontalStrut(10));
        hotkeyPanel.add(aboutBtn);
        hotkeyPanel.add(Box.createHorizontalStrut(10));
        hotkeyPanel.add(updateInfoBtn);
        hotkeyPanel.add(Box.createHorizontalStrut(10));
        hotkeyPanel.add(exportLogBtn);
        hotkeyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return hotkeyPanel;
    }

    private JPanel createSavePanel() {
        JButton saveSettingsBtn = new JButton(Localizer.get("settings.save_settings"));
        saveSettingsBtn.addActionListener(e -> {
            saveSettings();
            ConfigManager.saveConfig();
            Localizer.load(ConfigManager.getString("switch_lang"));
            FlatLaf.updateUI();
            MAIN_FRAME.refreshMainFrameTexts();
            MAIN_FRAME.readjust();
            dispose();
        });
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        savePanel.add(saveSettingsBtn);
        savePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return savePanel;
    }

    private void addTooltip(JComponent label, JComponent control, SettingsRegistry setting, JComponent... otherControls) {
        boolean hasEnabledTooltip = Localizer.hasKey("settings." + setting.i18nKey + ".tooltip");
        boolean hasDisabledTooltip = Localizer.hasKey("settings." + setting.i18nKey + ".tooltip.disabled");
        int type;
        String enabledText = null;
        if (hasEnabledTooltip)
            enabledText = Localizer.getS(setting.i18nKey + ".tooltip");
        String disabledText = null;
        if (hasDisabledTooltip)
            disabledText = Localizer.getS(setting.i18nKey + ".tooltip.disabled");
        else if (setting.relateTo != null)
            disabledText = String.format(Localizer.get("lang.tooltip.disabled.default"), Localizer.getS(setting.relateTo));
        if (hasEnabledTooltip && (hasDisabledTooltip || setting.relateTo != null))
            type = CompMouseAdapter.BOTH;
        else if (hasEnabledTooltip)
            type = CompMouseAdapter.ENABLED;
        else
            type = CompMouseAdapter.DISABLED;
        CompMouseAdapter adapter = new CompMouseAdapter(enabledText, disabledText, type);
        label.addMouseListener(adapter);
        control.addMouseListener(adapter);
        for (JComponent other : otherControls) {
            other.addMouseListener(adapter);
        }
    }

    private void refreshUI(boolean forceUpdateFromConfig) {
        for (SettingsRegistry setting : SettingsRegistry.values()) {
            if (setting.type == OtherConsts.SECTION) continue;
            JComponent component = componentMap.get(setting.i18nKey);
            if (component == null) continue;
            boolean isEnabled = true;
            if (setting.relateTo != null) {
                boolean parentValue = ConfigManager.getBoolean(setting.relateTo);
                isEnabled = "follow_system_settings".equals(setting.relateTo) != parentValue;
            }
            component.setEnabled(isEnabled);
            for (Component c : ((JPanel) component).getComponents()) {
                c.setEnabled(isEnabled);
            }
            if (forceUpdateFromConfig || !isEnabled) {
                Object valueToSet;
                String configKey = setting.i18nKey;
                if (!isEnabled) {
                    if (setting == SettingsRegistry.SWITCH_LANG) {
                        valueToSet = SystemUtil.getSystemLang(ConfigManager.getAvailableLangs());
                    } else if (setting == SettingsRegistry.ENABLE_DARK_MODE) {
                        valueToSet = SystemUtil.isSystemDarkMode();
                    } else {
                        valueToSet = setting.defaultValue;
                    }
                } else {
                    if (setting.type == OtherConsts.CHECK_BOX) {
                        valueToSet = ConfigManager.getBoolean(configKey);
                    } else if (setting.type == OtherConsts.COMBO_BOX) {
                        if (setting.size == -1) {
                            valueToSet = ConfigManager.getString(configKey);
                        } else {
                            valueToSet = ConfigManager.getInt(configKey);
                        }
                    } else {
                        valueToSet = ConfigManager.getString(configKey);
                    }
                }
                applyValueToComponent(component, setting, valueToSet);
            }
        }
    }

    private void applyValueToComponent(JComponent component, SettingsRegistry setting, Object value) {
        switch (setting.type) {
            case OtherConsts.CHECK_BOX:
                JCheckBox checkBox = (JCheckBox) ((JPanel) component).getComponent(2);
                checkBox.setSelected((Boolean) value);
                break;
            case OtherConsts.TEXT_FIELD:
            case OtherConsts.FILE_CHOOSER:
            case OtherConsts.SPECIFIC_TEXT_FIELD:
                JTextField textField = (JTextField) ((JPanel) component).getComponent(2);
                textField.setText(String.valueOf(value));
                break;
            case OtherConsts.COMBO_BOX:
                JComboBox<?> comboBox = (JComboBox<?>) ((JPanel) component).getComponent(2);
                if (value instanceof Integer) {
                    comboBox.setSelectedIndex((Integer) value - 1);
                } else {
                    comboBox.setSelectedItem(value);
                }
                break;
        }
    }

    private void saveSettings() {
        for (SettingsRegistry setting : SettingsRegistry.values()) {
            if (setting.type == OtherConsts.SECTION) continue;
            JComponent component = componentMap.get(setting.i18nKey);
            if (component == null) continue;
            if (component.isEnabled()) {
                switch (setting.type) {
                    case OtherConsts.CHECK_BOX:
                        break;
                    case OtherConsts.TEXT_FIELD:
                    case OtherConsts.FILE_CHOOSER:
                    case OtherConsts.SPECIFIC_TEXT_FIELD:
                        JTextField textField = (JTextField) ((JPanel) component).getComponent(2);
                        ConfigManager.set(setting.i18nKey, textField.getText());
                        break;
                    case OtherConsts.COMBO_BOX:
                        JComboBox<?> comboBox = (JComboBox<?>) ((JPanel) component).getComponent(2);
                        if (setting.size != -1) {
                            ConfigManager.set(setting.i18nKey, comboBox.getSelectedIndex() + 1);
                        } else {
                            ConfigManager.set(setting.i18nKey, comboBox.getSelectedItem());
                        }
                        break;
                }
            }
        }
    }
}
package io.github.samera2022.mousemacros.app.ui.frame.settings;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;
import javax.swing .*;
        import java.awt .*;
        import java.awt.event .*;
        import java.util.Map;
import java.util.Objects;
import static io.github.samera2022.mousemacros.app.ui.frame.MainFrame .*;
        import static io.github.samera2022.mousemacros.app.util.OtherUtil.getNativeKeyDisplayText;

public class HotkeyDialog extends JDialog {
    public static boolean inHotKeyDialog = false;

    public HotkeyDialog() {
        inHotKeyDialog = true;
        setTitle(Localizer.get("settings.custom_hotkey"));
        setName("settings.custom_hotkey");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage());
        setModal(true);
        JLabel l1 = new JLabel(Localizer.get("main_frame.start_record") + ":");
        JLabel l2 = new JLabel(Localizer.get("main_frame.stop_record") + ":");
        JLabel l3 = new JLabel(Localizer.get("main_frame.play_macro") + ":");
        JLabel l4 = new JLabel(Localizer.get("main_frame.abort_macro_operation") + ":");
        JTextField t1 = new JTextField();
        JTextField t2 = new JTextField();
        JTextField t3 = new JTextField();
        JTextField t4 = new JTextField();
        t1.setText(getNativeKeyDisplayText(keyRecord));
        t2.setText(getNativeKeyDisplayText(keyStop));
        t3.setText(getNativeKeyDisplayText(keyPlay));
        t4.setText(getNativeKeyDisplayText(keyAbort));
        t1.setEditable(false);
        t2.setEditable(false);
        t3.setEditable(false);
        t4.setEditable(false);
        t1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JButton confirm = new JButton(Localizer.get("settings.custom_hotkey.confirm"));
        JPanel gridPanel = new JPanel(new GridLayout(4, 2, 5, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        gridPanel.add(l1);
        gridPanel.add(t1);
        gridPanel.add(l2);
        gridPanel.add(t2);
        gridPanel.add(l3);
        gridPanel.add(t3);
        gridPanel.add(l4);
        gridPanel.add(t4);
        JPanel confirmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        confirmPanel.add(confirm);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(gridPanel);
        mainPanel.add(confirmPanel);
        mainPanel.setFocusable(true);
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainPanel.requestFocusInWindow();
            }
        });
        setContentPane(mainPanel);
        final int[] tempRecord = {keyRecord};
        final int[] tempStop = {keyStop};
        final int[] tempPlay = {keyPlay};
        final int[] tempAbort = {keyAbort};
        NativeKeyListener keyListener = new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                if (t1.hasFocus()) {
                    tempRecord[0] = e.getKeyCode();
                    t1.setText(getNativeKeyDisplayText(tempRecord[0]));
                } else if (t2.hasFocus()) {
                    tempStop[0] = e.getKeyCode();
                    t2.setText(getNativeKeyDisplayText(tempStop[0]));
                } else if (t3.hasFocus()) {
                    tempPlay[0] = e.getKeyCode();
                    t3.setText(getNativeKeyDisplayText(tempPlay[0]));
                } else if (t4.hasFocus()) {
                    tempAbort[0] = e.getKeyCode();
                    t4.setText(getNativeKeyDisplayText(tempAbort[0]));
                }
            }
        };
        FocusAdapter textFocusAdapter = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                ((JTextField) e.getSource()).setBackground(UIManager.getColor("TextField.inactiveForeground"));
                GlobalScreen.addNativeKeyListener(keyListener);
            }

            @Override
            public void focusLost(FocusEvent e) {
                ((JTextField) e.getSource()).setBackground(UIManager.getColor("TextField.background"));
                GlobalScreen.removeNativeKeyListener(keyListener);
            }
        };
        JTextField[] fields = {t1, t2, t3, t4};
        for (JTextField f : fields) {
            f.addFocusListener(textFocusAdapter);
            f.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    f.requestFocusInWindow();
                }
            });
        }
        confirm.addActionListener(e -> {
            keyRecord = tempRecord[0];
            keyStop = tempStop[0];
            keyPlay = tempPlay[0];
            keyAbort = tempAbort[0];
            Map<String, String> keyMap = ConfigManager.getKeyMap();
            keyMap.put("start_record", String.valueOf(keyRecord));
            keyMap.put("stop_record", String.valueOf(keyStop));
            keyMap.put("play_macro", String.valueOf(keyPlay));
            keyMap.put("abort_macro_operation", String.valueOf(keyAbort));
            ConfigManager.set("keyMap", keyMap);
            ConfigManager.saveConfig();
            GlobalScreen.removeNativeKeyListener(GML);
            GlobalScreen.addNativeKeyListener(GML);
            GlobalScreen.removeNativeKeyListener(keyListener);
            inHotKeyDialog = false;
            dispose();
        });
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowClosingAdapter());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                inHotKeyDialog = false;
            }

            @Override
            public void windowClosing(WindowEvent e) {
                inHotKeyDialog = false;
            }
        });
    }
}
package io.github.samera2022.mousemacros.app.util;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;
import java.io .*;
        import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static String readFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) return null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    public static void writeFile(String path, String content) throws IOException {
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
    }

    public static String[] listFileNames(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) return new String[0];
        File[] files = dir.listFiles();
        if (files == null) return new String[0];
        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getName();
        }
        return names;
    }

    public static Path getLocalStoragePath() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            String localAppData = System.getenv("LOCALAPPDATA");
            return Paths.get(localAppData, OtherConsts.APP_NAME);
        } else if (os.contains("mac")) {
            return Paths.get(System.getProperty("user.home"), "Library", "Application Support", OtherConsts.APP_NAME);
        } else {
            return Paths.get(System.getProperty("user.home"), ".local", "share", OtherConsts.APP_NAME);
        }
    }
}
package io.github.samera2022.mousemacros.app.util;
import java.awt .*;

public class ScreenUtil {
    public static Point denormalizeFromVirtualOrigin(int x, int y) {
        Point virtualOrigin = getVirtualOrigin();
        int globalX = x + virtualOrigin.x;
        int globalY = y + virtualOrigin.y;
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        Rectangle primaryBounds = gc.getBounds();
        double scaleX = gc.getDefaultTransform().getScaleX();
        double scaleY = gc.getDefaultTransform().getScaleY();
        int robotX = (int) Math.round((globalX - primaryBounds.x) / scaleX);
        int robotY = (int) Math.round((globalY - primaryBounds.y) / scaleY);
        return new Point(robotX, robotY);
    }

    private static Point getVirtualOrigin() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = ge.getScreenDevices();
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        for (GraphicsDevice device : devices) {
            Rectangle bounds = device.getDefaultConfiguration().getBounds();
            if (bounds.x < minX) minX = bounds.x;
            if (bounds.y < minY) minY = bounds.y;
        }
        return new Point(minX, minY);
    }

    public static Point normalizeToVirtualOrigin(int x, int y) {
        Point origin = getVirtualOrigin();
        return new Point(x - origin.x, y - origin.y);
    }
}

```
