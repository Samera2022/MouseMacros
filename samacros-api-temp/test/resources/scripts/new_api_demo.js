// ==UserScript==
const registry_name = 'new_api_demo';
const display_name = 'New API Demo Script';
const version = '1.0.0';
const author = 'saMacros Team';
const description = 'Demonstrates the newly implemented API features in v2.1.0';
const available_version = '2.0.0';
const requireNativeAccess = true;
const requireNativeAccessDescription = 'Demonstrates the newly implemented API features in v2.1.0';
// ==/UserScript==

const context = mm.getContext();

// ============================================
// 1. System Information API
// ============================================
mm.log('=== System Information ===');
const system = context.getSystemInfo();

const scale = system.getScale();
mm.log('DPI Scale: X=' + scale[0] + ', Y=' + scale[1]);

mm.log('Dark Mode: ' + system.isSystemDarkMode());
mm.log('System Language: ' + system.getSystemLanguage());
mm.log('OS: ' + system.getOSName());
mm.log('Java Version: ' + system.getJavaVersion());

// ============================================
// 2. Screen Information API
// ============================================
mm.log('=== Screen Information ===');
const screen = context.getScreenInfo();

mm.log('Screen Size: ' + screen.getWidth() + 'x' + screen.getHeight());
mm.log('Screen Count: ' + screen.getScreenCount());

const origin = screen.getVirtualOrigin();
mm.log('Virtual Origin: (' + origin.x + ', ' + origin.y + ')');

// Coordinate conversion example
const normalized = screen.normalizeToVirtualOrigin(1920, 1080);
mm.log('Normalized (1920, 1080): (' + normalized.x + ', ' + normalized.y + ')');

// ============================================
// 3. Macro Information API
// ============================================
mm.log('=== Macro Information ===');
const macro = context.getMacroInfo();

mm.log('Recording: ' + macro.isRecording());
mm.log('Playing: ' + macro.isPlaying());
mm.log('Paused: ' + macro.isPaused());
mm.log('Actions Count: ' + macro.getActionsCount());

// Monitor macro state changes
mm.on('io.github.samera2022.samacros.api.event.events.BeforeRecordStartEvent', (event) => {
    mm.log('[Event] Recording started!');
    const info = context.getMacroInfo();
    mm.log('[Event] Is Recording: ' + info.isRecording());
});

mm.on('io.github.samera2022.samacros.api.event.events.AfterRecordStopEvent', (event) => {
    mm.log('[Event] Recording stopped!');
    const info = context.getMacroInfo();
    mm.log('[Event] Actions Count: ' + info.getActionsCount());
});

mm.on('io.github.samera2022.samacros.api.event.events.BeforePlaybackStartEvent', (event) => {
    mm.log('[Event] Playback started!');
    const info = context.getMacroInfo();
    mm.log('[Event] Is Playing: ' + info.isPlaying());
    mm.log('[Event] Total Actions: ' + info.getActionsCount());
});

// ============================================
// 4. Internationalization (i18n) API
// ============================================
mm.log('=== Internationalization ===');
const i18n = context.getI18n();

mm.log('Current Language: ' + i18n.getCurrentLanguage());
mm.log('Has "main_frame" key: ' + i18n.hasKey('main_frame'));

// Get translated strings
if (i18n.hasKey('main_frame')) {
    mm.log('Translation: ' + i18n.get('main_frame'));
}

// ============================================
// 5. Mouse Information API
// ============================================
mm.log('=== Mouse Information ===');
const mouse = context.getMouseInfo();

const pos = mouse.getPosition();
if (pos != null) {
    mm.log('Mouse Position: (' + pos.x + ', ' + pos.y + ')');
} else {
    mm.log('Mouse Position: Unavailable');
}

// Monitor mouse position during playback
mm.on('io.github.samera2022.samacros.api.event.events.BeforeStepExecuteEvent', (event) => {
    const action = event.getAction();
    if (action.type === 1) { // Mouse press
        const mousePos = context.getMouseInfo().getPosition();
        if (mousePos != null) {
            mm.log('[Event] Mouse at: (' + mousePos.x + ', ' + mousePos.y + ')');
        }
    }
});

// ============================================
// 6. Practical Example: Adaptive Behavior
// ============================================
mm.log('=== Adaptive Behavior Example ===');

// Adjust notification based on system dark mode
function showAdaptiveNotification(title, message) {
    const isDark = context.getSystemInfo().isSystemDarkMode();
    const prefix = isDark ? '🌙' : '☀️';
    context.showToast(prefix + ' ' + title, message);
}

// Show notification when script loads
showAdaptiveNotification('Script Loaded', 'New API Demo is ready!');

// Adjust behavior based on screen count
const screenCount = context.getScreenInfo().getScreenCount();
if (screenCount > 1) {
    mm.log('Multi-monitor setup detected (' + screenCount + ' screens)');
    mm.log('Using coordinate normalization for better compatibility');
}

// Show localized messages
const lang = context.getI18n().getCurrentLanguage();
if (lang.startsWith('zh')) {
    mm.log('检测到中文环境');
} else {
    mm.log('English environment detected');
}

mm.log('=== New API Demo Complete ===');

