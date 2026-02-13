# MouseMacros Extended API Reference Guide

This document supplements the Script Development Guide with additional practical API interfaces that can be exposed to scripts.

## Table of Contents

1. [Macro Management API](#macro-management-api)
2. [System and Screen API](#system-and-screen-api)
3. [Internationalization API](#internationalization-api)
4. [Recommended Future APIs](#recommended-future-apis)

---

## Macro Management API

### `mm.macro` Object (Recommended Addition)

Provides access to the current macro state and operations.

#### Properties and Methods

**State Queries:**

```javascript
// Get the list of actions in the current macro
const actions = mm.macro.getActions();
console.log('Total actions: ' + actions.length);

// Get current loop information (valid during playback)
const currentLoop = mm.macro.getCurrentLoop();
const currentActionIndex = mm.macro.getCurrentActionIndex();
mm.log('Playing loop ' + (currentLoop + 1) + ', action ' + currentActionIndex);

// Query macro state
if (mm.macro.isRecording()) {
    mm.log('Recording in progress');
}

if (mm.macro.isPlaying()) {
    mm.log('Playback in progress');
}

if (mm.macro.isPaused()) {
    mm.log('Playback is paused');
}
```

**Macro Control (Already Available):**

```javascript
// Start recording
mm.macro.startRecording();

// Stop recording
mm.macro.stopRecording();

// Play macro
mm.macro.play();

// Pause playback
mm.macro.pause();

// Resume playback
mm.macro.resume();

// Abort playback
mm.macro.abort();

// Clear actions list
mm.macro.clear();
```

**Macro Data Access:**

```javascript
// Get all actions in the macro
const actions = mm.macro.getActions();
for (var i = 0; i < actions.length; i++) {
    const action = actions[i];
    mm.log('Action ' + i + ': type=' + action.getType() + 
            ', x=' + action.getX() + ', y=' + action.getY());
}

// Get action count
mm.log('Total action count: ' + mm.macro.getActionsCount());

// Get timestamp of last action
const lastActionTime = mm.macro.getLastTime();
```

**Macro File Operations (Recommended Addition):**

```javascript
// Save macro to file (user selects location in UI)
mm.macro.saveToFile();

// Load macro from file
mm.macro.loadFromFile();

// Get last save directory
const lastFilePath = mm.macro.getLastSaveDirectory();
```

### Example: Dynamic Macro Modification

```javascript
const registry_name = 'macro_modifier';
const display_name = 'Macro Modifier';
const version = '1.0.0';
const author = 'Developer';
const description = 'Modifies macros before playback';
const available_version = '1.0.0';

mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    var actions = event.getMacroData();
    
    // Analyze macro
    mm.log('Macro has ' + actions.length + ' actions');
    mm.log('Will repeat ' + event.getRepeatCount() + ' times');
    
    // Check macro length
    if (actions.length > 1000) {
        mm.log('Warning: Macro is very long (' + actions.length + ' actions)');
    }
});
```

---

## System and Screen API

### `mm.system` Object (Recommended Addition)

Provides access to system information and screen configuration.

#### Screen Coordinates and Scaling

```javascript
// Get DPI scale factors
const scale = mm.system.getScale();
mm.log('Screen scale: X=' + scale[0] + ', Y=' + scale[1]);

// Get virtual screen origin (for multi-monitor setups)
const origin = mm.system.getVirtualOrigin();
mm.log('Virtual origin: (' + origin.x + ', ' + origin.y + ')');

// Coordinate conversion
const normalizedPos = mm.system.normalizeToVirtualOrigin(1920, 1080);
mm.log('Normalized position: (' + normalizedPos.x + ', ' + normalizedPos.y + ')');

const robotPos = mm.system.denormalizeFromVirtualOrigin(100, 100);
mm.log('Robot coordinates: (' + robotPos.x + ', ' + robotPos.y + ')');
```

#### System Information

```javascript
// Get system language
const systemLang = mm.system.getSystemLanguage();
mm.log('System language: ' + systemLang);

// Check dark mode
const isDarkMode = mm.system.isSystemDarkMode();
mm.log('Dark mode enabled: ' + isDarkMode);

// Get operating system
const osName = mm.system.getOSName();
mm.log('Operating system: ' + osName);

// Get Java version
const javaVersion = mm.system.getJavaVersion();
mm.log('Java version: ' + javaVersion);
```

### Example: Screen-Aware Macro Execution

```javascript
const registry_name = 'screen_aware_executor';
const display_name = 'Screen Aware Executor';
const version = '1.0.0';
const author = 'Developer';
const description = 'Adjusts macro based on screen configuration';
const available_version = '1.0.0';

mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    const scale = mm.system.getScale();
    
    // Adjust delays based on scale factor
    if (scale[0] > 1.5 || scale[1] > 1.5) {
        mm.log('High DPI detected, increasing delays');
        // Can modify action delays
    }
    
    // Check dark mode for color detection adjustment
    if (mm.system.isSystemDarkMode()) {
        mm.log('Dark mode detected, using dark theme colors');
    }
});
```

---

## Internationalization API

### `mm.i18n` Object (Recommended Addition)

Provides access to application localization strings.

#### Localized Strings

```javascript
// Get localized string
const startText = mm.i18n.get('main_frame.start_recording');
mm.log('Start button text: ' + startText);

// Get settings-related localized string
const darkModeText = mm.i18n.getSettings('dark_mode');
mm.log('Dark mode label: ' + darkModeText);

// Check if a key exists
if (mm.i18n.hasKey('custom_key')) {
    mm.log(mm.i18n.get('custom_key'));
}

// Get current language
const currentLang = mm.i18n.getCurrentLanguage();
mm.log('Current language: ' + currentLang);

// Get available languages
const availableLangs = mm.i18n.getAvailableLanguages();
mm.log('Available languages: ' + availableLangs.join(', '));
```

#### Dynamic Language Switching

```javascript
// Switch language (recommended addition)
mm.i18n.switchLanguage('en_us');
mm.log('Language switched to: en_us');

// Reload language files
mm.i18n.reloadLanguage();
```

### Example: Multilingual Script

```javascript
const registry_name = 'multilingual_logger';
const display_name = 'Multilingual Logger';
const version = '1.0.0';
const author = 'Developer';
const description = 'Logs events in system language';
const available_version = '1.0.0';

mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    var playText = mm.i18n.get('log.start_playback');
    var repeatTimes = event.getRepeatCount();
    mm.log(playText + ' (x' + repeatTimes + ')');
    
    // Adjust log format based on language
    var currentLang = mm.i18n.getCurrentLanguage();
    if (currentLang.startsWith('zh')) {
        mm.log('Chinese user: Starting playback');
    } else if (currentLang.startsWith('en')) {
        mm.log('English user: Starting playback');
    }
});
```

---

## Recommended Future APIs

### 1. Clipboard API

```javascript
// Get clipboard content
const clipboardText = mm.clipboard.getText();

// Set clipboard content
mm.clipboard.setText('some text');

// Get files from clipboard
const files = mm.clipboard.getFiles();
```

### 2. File API

```javascript
// Read file
const content = mm.file.readText('/path/to/file.txt');

// Write file
mm.file.writeText('/path/to/file.txt', 'content');

// List directory
const files = mm.file.listDirectory('/path/to/dir');

// Check if file exists
if (mm.file.exists('/path/to/file.txt')) {
    mm.log('File exists');
}
```

### 3. Network API

```javascript
// Send HTTP GET request
const response = mm.http.get('https://api.example.com/data');
mm.log('Response: ' + response.status + ' ' + response.body);

// POST request
const result = mm.http.post('https://api.example.com/data', 
    { key: 'value' });
```

### 4. Timer and Time API

```javascript
// Set timeout
mm.timer.setTimeout(() => {
    mm.log('Timeout executed');
}, 1000);

// Set interval
const intervalId = mm.timer.setInterval(() => {
    mm.log('Interval executed');
}, 2000);

// Clear interval
mm.timer.clearInterval(intervalId);

// Delay execution (async/await style)
mm.timer.delay(1000).then(() => {
    mm.log('1 second passed');
});
```

### 5. Window and UI API

```javascript
// Get active window info
const activeWindow = mm.window.getActiveWindow();
mm.log('Active window title: ' + activeWindow.getTitle());
mm.log('Active window class: ' + activeWindow.getClassName());

// Get window list
const windows = mm.window.getAllWindows();
mm.log('Total windows: ' + windows.length);

// Focus window
mm.window.focusWindow('notepad.exe');

// Get window position and size
const rect = activeWindow.getRect();
mm.log('Window position: (' + rect.x + ', ' + rect.y + ')');
mm.log('Window size: ' + rect.width + ' x ' + rect.height);
```

### 6. Performance Monitoring API

```javascript
// Start performance measurement
mm.perf.startMeasure('my_operation');

// Execute operation...

// Stop measuring and get elapsed time
const elapsed = mm.perf.endMeasure('my_operation');
mm.log('Operation took ' + elapsed + ' ms');

// Get memory information
const memInfo = mm.perf.getMemoryInfo();
mm.log('Memory used: ' + memInfo.usedMB + ' MB');
```

---

## Existing but Enhanceable APIs

### Suggested ScriptContext Enhancements

The object returned by `mm.getContext()` currently provides:

```javascript
const context = mm.getContext();

// Existing methods
context.simulate(action);           // Simulate mouse/keyboard action
context.getPixelColor(x, y);        // Get pixel color
context.showToast(title, msg);      // Show notification
context.getAppConfig();              // Get app config
```

**Suggested Enhancements:**

```javascript
// Get mouse position
const mousePos = context.getMousePosition();
mm.log('Mouse at: (' + mousePos.x + ', ' + mousePos.y + ')');

// Get active window info
const windowInfo = context.getActiveWindowInfo();
mm.log('Window: ' + windowInfo.title);

// Get screen size
const screenSize = context.getScreenSize();
mm.log('Screen: ' + screenSize.width + 'x' + screenSize.height);

// Set clipboard
context.setClipboardText('text');

// Play sound
context.playSound('notification.wav');
```

---

## Best Practices

### 1. Feature Detection

```javascript
// Check if API is available
if (typeof mm.macro !== 'undefined') {
    mm.log('Macro API available');
    mm.macro.play();
} else {
    mm.log('Macro API not available');
}
```

### 2. Graceful Degradation

```javascript
// Try new API, fallback to events
try {
    const actions = mm.macro.getActions();
} catch (e) {
    mm.log('Using event-based approach instead');
    mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', 
        (event) => {
            const actions = event.getMacroData();
        });
}
```

### 3. Error Handling

```javascript
try {
    const scale = mm.system.getScale();
    if (scale[0] < 1.0) {
        mm.log('Warning: Scale is less than 1.0');
    }
} catch (error) {
    mm.log('Error getting system scale: ' + error.message);
}
```

---

## API Implementation Roadmap

| API | Priority | Status | Target Version |
|-----|----------|--------|-----------------|
| `mm.macro.*` | High | Planning | 2.1.0 |
| `mm.system.*` | High | Planning | 2.1.0 |
| `mm.i18n.*` | Medium | Planning | 2.2.0 |
| `mm.clipboard.*` | Medium | Planning | 2.2.0 |
| `mm.file.*` | Medium | Planning | 2.2.0 |
| `mm.window.*` | Low | Research | 2.3.0 |
| `mm.http.*` | Low | Research | 2.3.0 |
| `mm.timer.*` | Low | Research | 2.3.0 |

---

## Summary

MouseMacros' scripting API is designed to be flexible and extensible. By gradually adding the recommended interfaces above, developers can be provided with powerful tools to create complex automation scripts while maintaining security and stability.

Each new API introduction should:
1. Have clear purposes and use cases
2. Provide clear error handling mechanisms
3. Include comprehensive documentation and examples
4. Be thoroughly tested in production environments

---

**Created:** February 2026  
**Version:** 1.0  
**Last Updated:** February 12, 2026

