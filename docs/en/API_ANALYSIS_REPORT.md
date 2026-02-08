# MouseMacros API Analysis Report

## Overview

Through detailed analysis of the MouseMacros app module source code, we have identified numerous practical APIs that can be exposed to script developers. This report summarizes the existing APIs, implemented but not exposed APIs, and recommended future APIs.

---

## I. Analyzed Module Structure

### Application Module Architecture

```
mouse-macros-app/src/main/java/io/github/samera2022/mousemacros/app/
├── manager/                    # Business logic managers
│   ├── MacroManager           # Macro operations and playback ✓ Partially exposed
│   ├── LogManager             # Logging management
│   └── CacheManager           # Cache management
├── script/                     # Script management
│   ├── ScriptAPI              # ✓ Exposed to scripts
│   ├── ScriptManager          # Script lifecycle
│   ├── ScriptContext          # ✓ Partially exposed
│   ├── ScriptDescription      # Script metadata
│   └── ScriptPlugin           # Script plugin wrapper
├── util/                       # Utility classes
│   ├── ScreenUtil             # Screen coordinates ⚠ Can be exposed
│   ├── SystemUtil             # System information ⚠ Can be exposed
│   ├── OtherUtil              # Other utilities
│   ├── FileUtil               # File operations
│   └── ComponentUtil          # UI component utilities
├── config/                     # Configuration management
│   ├── ConfigManager          # Configuration reading
│   └── WhitelistManager       # Permission whitelist
└── constant/                   # Constants
```

---

## II. Existing API Summary

### 2.1 Exposed APIs (via `mm` object)

#### Script API (`IScriptAPI`)

**Available Methods:**

| Method | Signature | Purpose |
|--------|-----------|---------|
| `on()` | `on(String eventClassName, Consumer<Event> callback)` | Register event listener |
| `getContext()` | `getContext(): IScriptContext` | Get script context |
| `log()` | `log(String message)` | Output log |
| `cleanup()` | `cleanup()` | Clean up resources (auto-called) |

#### Script Context API (`IScriptContext`)

**Available Methods:**

| Method | Signature | Purpose |
|--------|-----------|---------|
| `simulate()` | `simulate(IMouseAction action)` | Simulate mouse/keyboard action |
| `getPixelColor()` | `getPixelColor(int x, int y): Color` | Get screen pixel color |
| `showToast()` | `showToast(String title, String msg)` | Show notification |
| `getAppConfig()` | `getAppConfig(): IConfig` | Get app configuration |

#### App Configuration API (`IConfig`)

**Available Methods:**

| Method | Signature | Purpose |
|--------|-----------|---------|
| `getBoolean()` | `getBoolean(String key): boolean` | Read boolean config |
| `getInt()` | `getInt(String key): int` | Read integer config |
| `getDouble()` | `getDouble(String key): double` | Read float config |
| `getString()` | `getString(String key): String` | Read string config |
| `getKeyMap()` | `getKeyMap(): Map<String, String>` | Get all configs |

#### Event System (20+ Events)

Fully documented in the main development guide, including:
- Application lifecycle events
- Recording events
- Playback events
- File operation events
- Configuration change events

---

## III. Implemented But Not Fully Exposed APIs

### 3.1 MacroManager - Macro Management

**Current Implementation:**

```java
// State queries (accessible)
public static boolean isRecording()      // Is recording?
public static boolean isPlaying()        // Is playing?
public static boolean isPaused()         // Is paused?

// Macro control (accessible)
public static void startRecording()      // Start recording
public static void stopRecording()       // Stop recording
public static void play()                // Play
public static void pause()               // Pause
public static void resume()              // Resume
public static void abort()               // Abort

// Data access (partially accessible)
public static List<MouseAction> getActions()     // Get action list
public static long getLastTime()                 // Get last action time
public static void recordAction()                // Record action

// File operations (UI-bound)
public static void saveToFile()          // Save to file
public static void loadFromFile()        // Load from file
```

**Recommendation:** Expose `mm.macro` object with the following enhancements:

```javascript
mm.macro.isRecording()        // ✓ Detectable via events
mm.macro.isPlaying()          // ✓ Detectable via events
mm.macro.isPaused()           // ✓ Detectable via events
mm.macro.getActions()         // ✓ Accessible via events
mm.macro.getActionsCount()    // Recommended addition
mm.macro.getCurrentLoop()     // Recommended addition
mm.macro.getCurrentActionIndex() // Recommended addition
mm.macro.getLastSaveDirectory()  // Recommended addition
```

### 3.2 ScreenUtil - Screen Utility Class

**Current Implementation:**

```java
// Multi-monitor support and DPI scaling
public static Point denormalizeFromVirtualOrigin(int x, int y)
public static Point normalizeToVirtualOrigin(int x, int y)
private static Point getVirtualOrigin()
```

**Recommendation:** Expose via `mm.system` object

```javascript
mm.system.getScale()          // Get DPI scale factors
mm.system.getVirtualOrigin()  // Get virtual screen origin
mm.system.normalizeToVirtualOrigin(x, y)    // Coordinate conversion
mm.system.denormalizeFromVirtualOrigin(x, y) // Coordinate conversion
```

**Real-world Application:** Coordinate handling in multi-monitor and high DPI environments

### 3.3 SystemUtil - System Utility Class

**Current Implementation:**

```java
// Get system DPI scaling
public static double[] getScale()

// Get system language
public static String getSystemLang(String[] availableLangs)

// Check dark mode (Windows 10+ only)
public static boolean isSystemDarkMode()
```

**Recommendation:** Expose via `mm.system` object

```javascript
mm.system.getScale()          // Get scale factors
mm.system.getSystemLanguage() // Get system language
mm.system.isSystemDarkMode()  // Is dark mode?
mm.system.getOSName()         // Get operating system
mm.system.getJavaVersion()    // Get Java version
```

### 3.4 Localizer - Internationalization Support

**Current Implementation:**

```java
// Load language files and get translations
public static void load(String lang)
public static String get(String key)
public static boolean hasKey(String key)
private static Map<String, String> loadLanguageMap(String lang)
```

**Recommendation:** Expose via `mm.i18n` object

```javascript
mm.i18n.get(key)              // Get translated string
mm.i18n.hasKey(key)           // Check if key exists
mm.i18n.getCurrentLanguage()  // Get current language
mm.i18n.getAvailableLanguages() // Get available languages
mm.i18n.switchLanguage(lang)  // Switch language (recommended)
```

---

## IV. Recommended New APIs (Priority Order)

### Priority 1 (High) - v2.1.0

#### 1. Macro Management API
```javascript
mm.macro.getActionsCount()        // Get action count
mm.macro.getCurrentLoop()         // Current loop number
mm.macro.getCurrentActionIndex()  // Current action index
mm.macro.getLastSaveDirectory()   // Last save directory
```

**Use Cases:**
- Scripts need detailed macro execution state
- Progress monitoring and conditional execution

#### 2. System Information API
```javascript
mm.system.getScale()              // DPI scaling
mm.system.isSystemDarkMode()      // Dark mode enabled?
mm.system.getSystemLanguage()     // System language
mm.system.getOSName()             // Operating system
mm.system.getJavaVersion()        // Java version
```

**Use Cases:**
- Adjust macro behavior based on system config
- Handle multi-monitor and high DPI environments
- Platform-specific optimizations

### Priority 2 (Medium) - v2.2.0

#### 3. Internationalization API
```javascript
mm.i18n.get(key)                  // Get translation
mm.i18n.getCurrentLanguage()      // Current language
mm.i18n.getAvailableLanguages()   // Available languages
```

**Use Cases:**
- Multilingual script development
- Localized logs and notifications

#### 4. Clipboard API
```javascript
mm.clipboard.getText()            // Read text
mm.clipboard.setText(text)        // Set text
mm.clipboard.getFiles()           // Read file list
```

**Use Cases:**
- Integration with other applications
- Automatic copy/paste workflows

#### 5. File API
```javascript
mm.file.readText(path)            // Read text file
mm.file.writeText(path, content)  // Write text file
mm.file.exists(path)              // Check if file exists
mm.file.listDirectory(path)       // List directory contents
```

**Use Cases:**
- Read configuration from files
- Output execution results and logs
- Data sharing between scripts

### Priority 3 (Low) - v2.3.0+

#### 6. Window Management API
```javascript
mm.window.getActiveWindow()       // Get active window
mm.window.getAllWindows()         // Get all windows
mm.window.focusWindow(title)      // Focus window
```

**Use Cases:**
- Automation for specific applications
- Window state detection

#### 7. Network API
```javascript
mm.http.get(url)                  // GET request
mm.http.post(url, data)           // POST request
mm.http.download(url, path)       // Download file
```

**Use Cases:**
- Integration with online services
- Data upload and synchronization

#### 8. Timer API
```javascript
mm.timer.setTimeout(fn, delay)    // Execute with delay
mm.timer.setInterval(fn, delay)   // Execute periodically
mm.timer.delay(ms)                // Delay Promise
```

**Use Cases:**
- Asynchronous operation control
- Scheduled tasks

---

## V. API Design Principles

### 5.1 Security Considerations

| API Type | Security Level | Notes |
|----------|---------------|-------|
| Read-only config | High | Implemented, safe |
| Event listening | High | Implemented, safe |
| File operations | Medium | Needs permission control |
| Network operations | Low | Needs explicit approval |
| System operations | Low | Needs native access approval |

### 5.2 Implementation Pattern

**Recommended Object Structure:**

```javascript
// Core objects
mm.on()           // Event listening (existing)
mm.log()          // Logging (existing)
mm.getContext()   // Get context (existing)

// Extended objects (recommended)
mm.macro          // Macro management
mm.system         // System information
mm.i18n           // Internationalization
mm.clipboard      // Clipboard (needs permission)
mm.file           // File operations (needs permission)
mm.window         // Window management (needs permission)
mm.http           // Network (needs permission)
mm.timer          // Timer
mm.perf           // Performance monitoring
```

### 5.3 Error Handling

**Recommended Error Handling Pattern:**

```javascript
// 1. Feature detection
if (typeof mm.macro !== 'undefined') {
    // Use new API
}

// 2. Try-Catch
try {
    const scale = mm.system.getScale();
} catch (error) {
    mm.log('Error: ' + error.message);
}

// 3. Fallback mechanism
try {
    return mm.macro.getActionsCount();
} catch (e) {
    // Use event alternative
}
```

---

## VI. Implementation Cost Assessment

| API Module | Difficulty | Code Lines | Estimated Hours |
|----------|-----------|-----------|-----------------|
| `mm.macro` | Low | 200-300 | 2-4 hours |
| `mm.system` | Low | 100-150 | 1-2 hours |
| `mm.i18n` | Low | 50-100 | 1 hour |
| `mm.clipboard` | Medium | 150-250 | 3-5 hours |
| `mm.file` | Medium | 200-300 | 4-6 hours |
| `mm.window` | High | 300-500 | 6-10 hours |
| `mm.http` | Medium | 150-250 | 3-5 hours |
| `mm.timer` | Low | 100-150 | 1-2 hours |

**Total (All):** ~1100-2000 lines of code, 20-35 hours of work

---

## VII. Security and Privacy Considerations

### 7.1 Permission Model

```
┌─────────────────────────────────┐
│    App-level APIs (No permission)│
│  • Event listening                │
│  • Macro state queries            │
│  • Logging                        │
└─────────────────────────────────┘
        ↓
┌─────────────────────────────────┐
│  High-permission APIs (Explicit) │
│  • File operations                │
│  • Network operations             │
│  • Clipboard access               │
│  • Window operations              │
└─────────────────────────────────┘
        ↓
┌─────────────────────────────────┐
│  System APIs (Native access)      │
│  • System command execution       │
│  • Registry modification          │
│  • Process operations             │
└─────────────────────────────────┘
```

### 7.2 Permission Checking

**Recommended Permission Check Code:**

```java
// Add permission checks in ScriptAPI
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

## VIII. Use Cases and Examples

### Use Case 1: Adaptive Macro Execution

```javascript
// Auto-adjust macro based on system config
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

### Use Case 2: Multilingual Logging

```javascript
// Log in system language
mm.on('OnLoopCompleteEvent', (event) => {
    const lang = mm.i18n.getCurrentLanguage();
    const msg = mm.i18n.get('log.loop_complete');
    mm.log(msg + ' ' + event.getLoopNumber());
});
```

### Use Case 3: Progress Monitoring

```javascript
// Monitor execution progress
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

## IX. Recommendations and Conclusion

### Key Recommendations

1. **Implement Immediately (v2.1.0)**
   - `mm.macro` - Macro state and control interface
   - `mm.system` - System information interface
   - High priority, low risk, high benefit

2. **Plan Implementation (v2.2.0)**
   - `mm.i18n` - Internationalization support
   - `mm.clipboard` - Clipboard access
   - `mm.file` - File operations (restricted)

3. **Future Consideration (v2.3.0+)**
   - `mm.window` - Window management
   - `mm.http` - Network operations
   - High risk, needs comprehensive security design

### Key Security Recommendations

1. **Permission Whitelist**
   - Maintain approved scripts/authors list
   - Regular audit of permission usage

2. **Sandbox Isolation**
   - Restrict file system access scope
   - Restrict network access domains

3. **Audit Logging**
   - Log all sensitive operations
   - Provide user-visible permission usage reports

4. **Documentation Warnings**
   - Clearly mark permission requirements
   - Explain potential security risks

---

## Appendix: Reference Resources

- **Source File Locations**
  - ScriptAPI: `mouse-macros-app/src/main/java/.../script/ScriptAPI.java`
  - MacroManager: `mouse-macros-app/src/main/java/.../manager/MacroManager.java`
  - ScreenUtil: `mouse-macros-app/src/main/java/.../util/ScreenUtil.java`
  - SystemUtil: `mouse-macros-app/src/main/java/.../util/SystemUtil.java`
  - Localizer: `mouse-macros-app/src/main/java/.../Localizer.java`

- **Related Documentation**
  - SCRIPT_DEVELOPMENT_GUIDE.md - Main development guide
  - EXTENDED_API_REFERENCE.md - Extended API reference

---

**Report Completion Date:** February 12, 2026  
**Analysis Scope:** MouseMacros v2.0.0  
**Analysis Depth:** Source code level  
**Documentation Version:** 1.0

