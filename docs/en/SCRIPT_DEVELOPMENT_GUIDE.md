# MouseMacros JavaScript Script Development Guide

Welcome to the MouseMacros Script Development Guide! This guide will help you create powerful JavaScript scripts that extend MouseMacros functionality.

## Table of Contents

1. [Overview](#overview)
2. [Script Structure](#script-structure)
3. [Metadata Declaration](#metadata-declaration)
4. [Script API Reference](#script-api-reference)
5. [Event System](#event-system)
6. [Available Events](#available-events)
7. [Script Dependencies](#script-dependencies)
8. [Native Access](#native-access)
9. [Error Handling](#error-handling)
10. [Best Practices](#best-practices)
11. [Examples](#examples)

## Overview

MouseMacros supports JavaScript scripting through GraalVM's Polyglot engine. Scripts can:

- Listen to and react to application events
- Simulate mouse and keyboard actions
- Access application configuration
- Capture screen pixel colors
- Display notifications to users
- Depend on other scripts with full dependency management
- With user permission, gain native access to perform advanced tasks

## Script Structure

Every script must have:

1. **Metadata Declaration** - Information about the script
2. **Code Logic** - Event handlers and functionality
3. **Optional Initialization** - Setup code that runs when the script loads

### Minimal Script Example

```javascript
const registry_name = 'my_script';
const display_name = 'My Script';
const version = '1.0.0';
const author = 'Your Name';
const description = 'What my script does';
const available_version = '1.0.0~2.3.*';
const requireNativeAccess = false; // Optional.
const requireNativeAccessDescription = "..." // Optional. this string will be displayed on the warning frame.
const soft_dependencies = []; // Optional.
const hard_dependencies = []; // Optional.

// Optional: Register event listeners
mm.on('io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent', (event) => {
    mm.log('My script has been initialized!');
});
```

## Metadata Declaration

All scripts must declare metadata as global variables at the top of the file.

### Required Fields

| Field | Type | Description |
|-------|------|-------------|
| `display_name` | string | Human-readable name shown in the UI. |
| `register_name` | string | Unique identifier for your script. Used for dependencies and internal references. Must be unique across all scripts. |
| `author` | string | Your name or nickname. |
| `version` | string | Current version of your script (semantic versioning recommended). |
| `description` | string | Brief description of what the script does, shown in the Scripts Manager. |
| `available_version` | string | The version of MouseMacros this script is compatible with (e.g., "2.0.0", "2.x", "1.0.0 ~ 2.0.0"). Use "*" for all versions. |

### Optional Fields

| Field | Type | Description |
|-------|------|-------------|
| `soft_dependencies` | string[] | Array of `register_name`s for scripts that enhance functionality but aren't required. |
| `hard_dependencies` | string[] | Array of `register_name`s for scripts that are required. The script will be disabled if these are missing. |
| `requireNativeAccess` | boolean | Set to `true` if your script needs native system access. **Requires explicit user approval.** |
| `nativeAccessDescription` | string | A clear explanation of why native access is needed. This is shown to the user in the security prompt. |

## Script API Reference

The script API is accessible through the global `mm` object. This object provides methods to interact with MouseMacros.

### `mm.on(eventClassName, callback)`

Registers a listener for a specific event type.

**Parameters:**
- `eventClassName` (string): Fully qualified name of the event class
- `callback` (function): Function to execute when the event is triggered

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent', (event) => {
    mm.log('App launched at: ' + new Date(event.getTimestamp()));
});
```

### `mm.getContext()`

Returns the script context object for accessing application features.

**Returns:** ScriptContext object with methods:
- `simulate(action)` - Simulate a mouse or keyboard action
- `getPixelColor(x, y)` - Get the RGB color of a pixel at screen coordinates
- `showToast(title, message)` - Display a notification to the user
- `getAppConfig()` - Access application configuration

**Example:**
```javascript
const context = mm.getContext();
context.showToast('Hello', 'Script is running!');
```

### `mm.log(message)`

Prints a message to the console with `[Script]` prefix.

**Parameters:**
- `message` (string): Message to log

**Example:**
```javascript
mm.log('This is a log message');
// Output: [Script] This is a log message
```

### `mm.cleanup()`

Called automatically when the script is disabled. Override this to clean up resources.

## Event System

MouseMacros events allow scripts to react to various application states and user actions. Events follow a pub-sub pattern.

### Event Lifecycle

1. Event is created by the application
2. Event is dispatched through the event system
3. All registered listeners are notified
4. For cancellable events, scripts can prevent further processing

### Event Object Methods

All events have:
- `getTimestamp()` - Returns the event creation time in milliseconds

Cancellable events have:
- `isCancelled()` - Check if the event has been cancelled
- `setCancelled(boolean)` - Cancel the event (prevent default action)

## Available Events

### Application Lifecycle Events

#### `OnAppLaunchedEvent`
Fired when the application launches.

**Properties:**
- `getApplicationVersion()` - Get MouseMacros version
- `getJavaVersion()` - Get Java version

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent', (event) => {
    mm.log('App version: ' + event.getApplicationVersion());
    mm.log('Java version: ' + event.getJavaVersion());
});
```

### Recording Events

#### `BeforeRecordStartEvent`
Fired before recording starts. Cancellable.

**Properties:**
- `isCancelled()` - Check if recording was prevented
- `setCancelled(boolean)` - Prevent recording from starting

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforeRecordStartEvent', (event) => {
    mm.log('About to start recording');
});
```

#### `AfterRecordStopEvent`
Fired after recording stops.

**Properties:**
- `getActionsCount()` - Number of actions recorded

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.AfterRecordStopEvent', (event) => {
    mm.log('Recorded ' + event.getActionsCount() + ' actions');
});
```

#### `OnInputCapturedEvent`
Fired when input (mouse or keyboard) is captured during recording. Cancellable.

**Properties:**
- `getInputType()` - Type of input (1=MousePress, 2=MouseRelease, 3=Wheel, 10=KeyPress, 11=KeyRelease)
- `getKeyCode()` - Key code (for keyboard events)
- `getX()` - X coordinate (for mouse events)
- `getY()` - Y coordinate (for mouse events)
- `getDelay()` - Time since last action in milliseconds
- `isCancelled()` / `setCancelled()` - Cancel input capture

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnInputCapturedEvent', (event) => {
    if (event.getInputType() === 1) { // Mouse press
        mm.log('Mouse pressed at (' + event.getX() + ', ' + event.getY() + ')');
    }
});
```

### Playback Events

#### `BeforePlaybackStartEvent`
Fired before macro playback starts. Cancellable.

**Properties:**
- `getMacroData()` - List of actions to be played
- `getRepeatCount()` - Number of times the macro will repeat
- `isCancelled()` / `setCancelled()` - Cancel playback

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    mm.log('Starting playback with ' + event.getRepeatCount() + ' repeats');
});
```

#### `BeforeStepExecuteEvent`
Fired before each individual action is executed. Cancellable.

**Properties:**
- `getAction()` - The action about to be executed
- `getActionIndex()` - Index of the action
- `isCancelled()` / `setCancelled()` - Skip this action

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforeStepExecuteEvent', (event) => {
    if (event.getActionIndex() % 10 === 0) {
        mm.log('Executing action ' + event.getActionIndex());
    }
});
```

#### `AfterStepExecuteEvent`
Fired after each action is executed.

**Properties:**
- `getAction()` - The action that was executed
- `getActionIndex()` - Index of the action

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.AfterStepExecuteEvent', (event) => {
    // Custom delay logic per action
});
```

#### `OnLoopCompleteEvent`
Fired when a loop iteration completes (one full playback of the macro).

**Properties:**
- `getLoopNumber()` - Which iteration just completed
- `getTotalLoops()` - Total number of iterations

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnLoopCompleteEvent', (event) => {
    mm.log('Completed loop ' + event.getLoopNumber() + ' of ' + event.getTotalLoops());
});
```

#### `OnPlaybackAbortedEvent`
Fired when playback is aborted by the user or script.

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnPlaybackAbortedEvent', (event) => {
    mm.log('Playback was aborted');
});
```

### File Operations

#### `OnMacroSaveEvent`
Fired when a macro is saved to file.

**Properties:**
- `getMacroName()` - Name of the macro being saved
- `getFilePath()` - Path where the macro is saved

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnMacroSaveEvent', (event) => {
    mm.log('Macro saved: ' + event.getMacroName());
});
```

#### `OnMacroLoadEvent`
Fired when a macro is loaded from file.

**Properties:**
- `getMacroName()` - Name of the loaded macro
- `getFilePath()` - Path of the loaded macro

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnMacroLoadEvent', (event) => {
    mm.log('Macro loaded: ' + event.getMacroName());
});
```

### Configuration Events

#### `OnConfigChangedEvent`
Fired when application configuration changes.

**Properties:**
- `getChangedKey()` - Configuration key that changed
- `getNewValue()` - New value of the configuration

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnConfigChangedEvent', (event) => {
    if ('dark_mode' === event.getChangedKey()) {
        mm.log('Dark mode changed to: ' + event.getNewValue());
    }
});
```

#### `OnMenuInitEvent`
Fired when the UI menu is initialized.

**Example:**
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnMenuInitEvent', (event) => {
    mm.log('Menu initialized');
});
```

### Additional Events

#### `OnActionAddedEvent`
Fired when an action is recorded/added to the macro.

**Properties:**
- `getAction()` - The action that was added

#### `OnTooltipDisplayEvent`
Fired when a tooltip is about to be displayed. Cancellable.

**Properties:**
- `getTooltipText()` - Text of the tooltip
- `setTooltipText(String)` - Modify the tooltip text

## Script Dependencies

Scripts can depend on other scripts to share functionality and avoid code duplication.

### Declaring Dependencies

```javascript
// Soft dependencies: nice to have, but optional
const soft_dependencies = ['helper_script', 'utils_script'];

// Hard dependencies: required for the script to work
const hard_dependencies = ['core_dependency'];
```

### Dependency Rules

- **Hard Dependencies**: If any hard dependency is missing or disabled, the script cannot be enabled
- **Soft Dependencies**: Missing soft dependencies don't prevent the script from running, but features may be limited
- **Circular Dependencies**: Not allowed. The system will detect and report circular dependency issues
- **Version Checking**: Dependencies are matched by `registry_name`

### Best Practices for Dependencies

1. Use hard dependencies only when absolutely necessary
2. Document what functionality requires each dependency
3. Check at runtime if optional features are available
4. Use meaningful dependency names that describe their purpose

## Native Access

Some scripts may need access to Java classes and system APIs beyond the safe script API.

### Requesting Native Access

```javascript
const requires_native_access = true;
const native_access_description = 'Needs to access system clipboard for advanced features';
```

### Using Native Access

Once approved, scripts can access Java classes:

```javascript
// Access Java classes
const File = Java.type('java.io.File');
const System = Java.type('java.lang.System');

// Use Java APIs
const home = System.getProperty('user.home');
mm.log('Home directory: ' + home);
```

### Security Considerations

- Users must explicitly approve native access
- Administrators can whitelist scripts/authors
- Scripts requesting native access will show warnings to users
- Only request native access when truly necessary

## Error Handling

Robust error handling ensures your scripts don't crash the application.

### Try-Catch Blocks

```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    try {
        // Risky operation
        const data = event.getMacroData();
        if (data.length === 0) {
            throw new Error('No macro data available');
        }
    } catch (error) {
        mm.log('Error: ' + error.message);
        event.setCancelled(true); // Prevent playback
    }
});
```

### Validation Patterns

```javascript
function validateInput(value, expectedType) {
    if (typeof value !== expectedType) {
        throw new TypeError('Expected ' + expectedType + ', got ' + typeof value);
    }
    return value;
}

mm.on('io.github.samera2022.mousemacros.api.event.events.OnInputCapturedEvent', (event) => {
    try {
        const inputType = validateInput(event.getInputType(), 'number');
        // Process validated input
    } catch (error) {
        mm.log('Validation error: ' + error.message);
    }
});
```

## Best Practices

### 1. Keep Scripts Focused
Each script should have a single, well-defined purpose.

### 2. Use Meaningful Names
```javascript
const registry_name = 'screenshot_capture_tool';  // Good
const registry_name = 'tool1';                    // Avoid
```

### 3. Log Important Events
```javascript
mm.log('Script initialized');
mm.log('Processing ' + actionCount + ' actions');
mm.log('Operation completed successfully');
```

### 4. Handle Edge Cases
```javascript
if (event.getActionsCount && event.getActionsCount() === 0) {
    mm.log('Warning: No actions to process');
    return;
}
```

### 5. Clean Up Resources
```javascript
mm.on('io.github.samera2022.mousemacros.api.event.events.OnAppLaunchedEvent', (event) => {
    // Initialize
    globalResources = {};
});

// Cleanup when disabled
function onScriptDisabled() {
    if (globalResources) {
        // Clean up code
        globalResources = null;
    }
}
```

### 6. Version Your Scripts
Use semantic versioning (MAJOR.MINOR.PATCH):
```javascript
const version = '1.0.0';        // Stable release
const version = '2.1.0-beta';   // Beta release
```

### 7. Provide Clear Documentation
Include comments explaining complex logic:
```javascript
// Check if macro has at least 10 actions to avoid running trivial macros
if (event.getMacroData().length < 10) {
    mm.log('Macro too short, skipping');
    return;
}
```

### 8. Test Thoroughly
- Test with different configurations
- Test with missing optional dependencies
- Test error scenarios
- Test with various event combinations

## Examples

### Example 1: Action Counter Script

A simple script that counts actions in macros:

```javascript
const registry_name = 'action_counter';
const display_name = 'Action Counter';
const version = '1.0.0';
const author = 'Developer';
const description = 'Counts and logs macro actions';
const available_version = '1.0.0';

var totalActions = 0;
var totalPlaybacks = 0;

mm.on('io.github.samera2022.mousemacros.api.event.events.AfterRecordStopEvent', (event) => {
    totalActions = event.getActionsCount();
    mm.log('Recorded ' + totalActions + ' actions');
});

mm.on('io.github.samera2022.mousemacros.api.event.events.OnLoopCompleteEvent', (event) => {
    totalPlaybacks++;
    mm.log('Completed playback ' + totalPlaybacks);
});

mm.on('io.github.samera2022.mousemacros.api.event.events.OnPlaybackAbortedEvent', (event) => {
    mm.log('Playback aborted after ' + totalPlaybacks + ' loops');
});
```

### Example 2: Notification Script

Display notifications for important events:

```javascript
const registry_name = 'notification_system';
const display_name = 'Notification System';
const version = '1.0.0';
const author = 'Developer';
const description = 'Shows notifications for macro events';
const available_version = '1.0.0';

const context = mm.getContext();

mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    context.showToast('Playback Started', 'Repeating ' + event.getRepeatCount() + ' times');
});

mm.on('io.github.samera2022.mousemacros.api.event.events.OnPlaybackAbortedEvent', (event) => {
    context.showToast('Playback Aborted', 'Macro execution was stopped');
});

mm.on('io.github.samera2022.mousemacros.api.event.events.OnLoopCompleteEvent', (event) => {
    var progress = Math.round((event.getLoopNumber() / event.getTotalLoops()) * 100);
    if (progress % 25 === 0) { // Show every 25%
        context.showToast('Progress', progress + '% complete');
    }
});
```

### Example 3: Input Filter Script

Filter certain inputs during recording:

```javascript
const registry_name = 'input_filter';
const display_name = 'Input Filter';
const version = '1.0.0';
const author = 'Developer';
const description = 'Filters out mouse movement events';
const available_version = '1.0.0';

// Constants for input types
const MOUSE_MOVE = 0;
const MOUSE_PRESS = 1;
const MOUSE_RELEASE = 2;
const MOUSE_WHEEL = 3;
const KEY_PRESS = 10;
const KEY_RELEASE = 11;

mm.on('io.github.samera2022.mousemacros.api.event.events.OnInputCapturedEvent', (event) => {
    // Skip recording mouse movement to reduce noise
    if (event.getInputType() === MOUSE_MOVE) {
        event.setCancelled(true);
        mm.log('Filtered out mouse movement at (' + event.getX() + ', ' + event.getY() + ')');
    }
});
```

### Example 4: Smart Macro Execution Script

Advanced macro execution with validation:

```javascript
const registry_name = 'smart_executor';
const display_name = 'Smart Macro Executor';
const version = '1.0.0';
const author = 'Developer';
const description = 'Validates and executes macros intelligently';
const available_version = '1.0.0';

var executionLog = [];

mm.on('io.github.samera2022.mousemacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    var actions = event.getMacroData();
    
    if (actions.length === 0) {
        mm.log('Error: No actions to play');
        event.setCancelled(true);
        return;
    }
    
    if (event.getRepeatCount() > 100) {
        mm.log('Warning: Very high repeat count (' + event.getRepeatCount() + ')');
    }
    
    executionLog = [];
    mm.log('Starting playback with ' + actions.length + ' actions');
});

mm.on('io.github.samera2022.mousemacros.api.event.events.BeforeStepExecuteEvent', (event) => {
    var action = event.getAction();
    executionLog.push({
        index: event.getActionIndex(),
        timestamp: new Date().getTime()
    });
});

mm.on('io.github.samera2022.mousemacros.api.event.events.OnLoopCompleteEvent', (event) => {
    mm.log('Loop ' + event.getLoopNumber() + '/' + event.getTotalLoops() + ' completed');
});

mm.on('io.github.samera2022.mousemacros.api.event.events.OnPlaybackAbortedEvent', (event) => {
    mm.log('Playback aborted after ' + executionLog.length + ' actions executed');
});
```

## Troubleshooting

### Script Not Loading
- Check metadata format (all required fields present)
- Verify file ends with `.js`
- Check console for parsing errors

### Events Not Firing
- Verify the event class name is correct
- Check that the script is enabled in the UI
- Review callback function syntax

### Native Access Issues
- Ensure `requires_native_access` is set to `true`
- Check that the author or script is whitelisted
- Use try-catch blocks when accessing Java APIs

### Dependency Issues
- Verify dependency `registry_name` matches exactly
- Check for circular dependencies
- Ensure hard dependencies are installed and enabled

## Getting Help

- Check the MouseMacros documentation
- Review example scripts in the script directory
- Check console output for detailed error messages
- Report issues with clear error descriptions

---

**Last Updated:** February 2026  
**MouseMacros Version:** 2.0.0+

