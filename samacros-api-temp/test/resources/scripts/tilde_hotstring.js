// ==UserScript==
const registry_name = 'tilde_hotstring';
const display_name = 'Tilde Hotstring';
const version = '1.0.0';
const author = 'GitHub Copilot';
const description = 'Press ~ during input capture to type a preset string at cursor.';
const available_version = '2.0.0';
const requireNativeAccess = false;
// ==/UserScript==

const TRIGGER_KEY_CODE = 192; // ` / ~
const KEY_PRESS = 10;
const SHIFT_KEY_CODE = 16;
const DEFAULT_TEXT = 'hello samacros';
const CONFIG_KEY = 'script.tilde_hotstring.text';

let isTyping = false;

function readTargetText() {
    try {
        const ctx = mm.getContext();
        const config = ctx && typeof ctx.getAppConfig === 'function' ? ctx.getAppConfig() : null;
        const configured = config && typeof config.getString === 'function' ? config.getString(CONFIG_KEY) : null;
        if (configured && ('' + configured).trim().length > 0) {
            return '' + configured;
        }
    } catch (e) {
        mm.log('[tilde_hotstring] Failed to read config, fallback to default: ' + e);
    }
    return DEFAULT_TEXT;
}

function pressKey(keyCode) {
    if (typeof mm.keyPressViaJava === 'function') {
        mm.keyPressViaJava(keyCode);
        return;
    }

    const ctx = mm.getContext();
    if (ctx && typeof ctx.simulate === 'function') {
        ctx.simulate(KEY_PRESS, 0, 0, 0, keyCode);
    }
}

function releaseKey(keyCode) {
    if (typeof mm.keyReleaseViaJava === 'function') {
        mm.keyReleaseViaJava(keyCode);
        return;
    }

    const ctx = mm.getContext();
    if (ctx && typeof ctx.simulate === 'function') {
        ctx.simulate(11, 0, 0, 0, keyCode);
    }
}

function tapKey(keyCode) {
    pressKey(keyCode);
    releaseKey(keyCode);
}

function mapCharToKey(char) {
    const code = char.charCodeAt(0);

    if (char >= 'a' && char <= 'z') {
        return { keyCode: code - 32, shift: false };
    }
    if (char >= 'A' && char <= 'Z') {
        return { keyCode: code, shift: true };
    }
    if (char >= '0' && char <= '9') {
        return { keyCode: code, shift: false };
    }

    const map = {
        ' ': { keyCode: 32, shift: false },
        '-': { keyCode: 45, shift: false },
        '_': { keyCode: 45, shift: true },
        '=': { keyCode: 61, shift: false },
        '+': { keyCode: 61, shift: true },
        '[': { keyCode: 91, shift: false },
        '{': { keyCode: 91, shift: true },
        ']': { keyCode: 93, shift: false },
        '}': { keyCode: 93, shift: true },
        '\\': { keyCode: 92, shift: false },
        '|': { keyCode: 92, shift: true },
        ';': { keyCode: 59, shift: false },
        ':': { keyCode: 59, shift: true },
        "'": { keyCode: 39, shift: false },
        '"': { keyCode: 39, shift: true },
        ',': { keyCode: 44, shift: false },
        '<': { keyCode: 44, shift: true },
        '.': { keyCode: 46, shift: false },
        '>': { keyCode: 46, shift: true },
        '/': { keyCode: 47, shift: false },
        '?': { keyCode: 47, shift: true },
        '!': { keyCode: 49, shift: true },
        '@': { keyCode: 50, shift: true },
        '#': { keyCode: 51, shift: true },
        '$': { keyCode: 52, shift: true },
        '%': { keyCode: 53, shift: true },
        '^': { keyCode: 54, shift: true },
        '&': { keyCode: 55, shift: true },
        '*': { keyCode: 56, shift: true },
        '(': { keyCode: 57, shift: true },
        ')': { keyCode: 48, shift: true }
    };

    return map[char] || null;
}

function typeText(text) {
    if (!text) {
        return;
    }

    for (let i = 0; i < text.length; i++) {
        const char = text.charAt(i);
        const keyInfo = mapCharToKey(char);

        if (!keyInfo) {
            mm.log('[tilde_hotstring] Unsupported char skipped: ' + char);
            continue;
        }

        if (keyInfo.shift) {
            pressKey(SHIFT_KEY_CODE);
        }

        tapKey(keyInfo.keyCode);

        if (keyInfo.shift) {
            releaseKey(SHIFT_KEY_CODE);
        }
    }
}

mm.on('io.github.samera2022.samacros.api.event.events.OnInputCapturedEvent', (event) => {
    if (isTyping) {
        return;
    }

    if (event.getInputType() !== KEY_PRESS) {
        return;
    }

    if (event.getKeyCode() !== TRIGGER_KEY_CODE) {
        return;
    }

    event.setCancelled(true);

    isTyping = true;
    try {
        const text = readTargetText();
        typeText(text);
    } catch (e) {
        mm.log('[tilde_hotstring] Failed to type text: ' + e);
    } finally {
        isTyping = false;
    }
});

mm.log('[tilde_hotstring] Loaded. Trigger key: ~, config key: ' + CONFIG_KEY);
