# MouseMacros Quick Start Guide

## 5-Minute Quick Start

### 1. Launch MouseMacros
- Double-click `MouseMacros.jar` (requires Java 11+)
- Or run: `java -jar MouseMacros.jar`

First launch will show a Welcome Wizard. Follow it for a guided tour!

### 2. Record Your First Macro

1. **Press F9** (default record hotkey)
2. Perform some simple actions:
   - Move your mouse
   - Click a few times
   - Type some text
3. **Press F10** to stop recording
4. You'll see recorded actions in the main window

### 3. Play Back Your Macro

1. **Press F11** (default play hotkey)
2. Watch your actions replay automatically!
3. **Press F12** to abort if needed

### 4. Save Your Macro

1. Click **"Save Macro"** button
2. Choose a filename (e.g., `my-first-macro.mmc`)
3. Click Save

### 5. Load a Saved Macro

1. Click **"Load Macro"** button
2. Select your `.mmc` file
3. Press F11 to play

**Congratulations!** 🎉 You've mastered the basics.

---

## Default Hotkeys

| Action | Hotkey | Description |
|--------|--------|-------------|
| **Start Recording** | F9 | Begin capturing mouse/keyboard actions |
| **Stop Recording** | F10 | End recording session |
| **Play Macro** | F11 | Execute the recorded/loaded macro |
| **Abort Macro** | F12 | Stop playback immediately |
| **Pause/Resume** | (dynamic) | Pause and resume during playback |

### Customize Hotkeys
Settings > Custom Hotkey > Click field > Press your key > Confirm

---

## Common Use Cases

### Automate Form Filling
```
1. Record: Navigate form, fill fields, click submit
2. Play: Instant form completion
3. Tip: Use Quick Mode (Settings) to skip delays
```

### Repetitive Clicking
```
1. Record: Click sequence (e.g., game actions)
2. Settings: Set Repeat Times (e.g., 100)
3. Play: Sits back and watch
4. Tip: Avoid mouse movement for precise clicks
```

### Keyboard Shortcuts Combo
```
1. Record: Press multiple shortcuts in sequence
   (e.g., Ctrl+C, Alt+Tab, Ctrl+V)
2. Play: One-click complex operations
```

### Testing & QA
```
1. Record: User interaction flow
2. Play: Reproduce bugs consistently
3. Tip: Enable "Quick Mode" for faster test runs
```

---

## Essential Settings

### Quick Mode
**Location:** Settings > Enable Quick Mode

- **ON**: Executes actions instantly (no delays)
- **OFF**: Preserves timing between actions (default)

**Use Quick Mode for:**
- Simple click sequences
- Form automation
- When timing doesn't matter

**Turn it OFF for:**
- Applications that need load time
- Animations that must complete
- Network-dependent actions

### Repeat Execution
**Location:** Settings > Repeat Times / Repeat Delay

- **Repeat Times**: How many times to execute (1-99999)
- **Repeat Delay**: Seconds to wait between repetitions

**Example:**
```
Repeat Times: 10
Repeat Delay: 2.5
Result: Macro runs 10 times, waiting 2.5 seconds between each
```

### Default Storage Path
**Location:** Settings > Default MMC Storage Path

Set a default folder for saving/loading macros. Useful if you organize macros in a specific directory.

---

## Tips & Best Practices

### ✅ DO:
- **Test first**: Record a simple macro to test hotkeys and playback
- **Use consistent windows**: Keep target applications in the same position
- **Single monitor**: Record and play on the same screen
- **Short macros**: Break complex workflows into smaller macros
- **Save often**: Use descriptive names (`excel-report-automation.mmc`)

### ❌ DON'T:
- **Move windows**: Absolute coordinates will break
- **Change resolution**: Between recording and playback
- **Use media keys**: Volume/playback keys don't record reliably
- **Record system shortcuts**: Ctrl+Alt+Del, Win+L cannot be captured
- **Overlap hotkeys**: Ensure your hotkeys don't conflict with other apps

---

## Keyboard Shortcut Reference

### Recommended Hotkey Combinations
✓ **Function Keys**: F1-F12 (most reliable)  
✓ **Ctrl combos**: Ctrl+Shift+A, Ctrl+Alt+Q  
✓ **Number Keys**: Ctrl+1, Shift+2, Alt+3  
✓ **Arrow Keys**: Ctrl+Up, Shift+Left  

### Avoid These Keys
✗ **Media keys**: Volume, Play, Pause, Next  
✗ **Fn keys**: Fn+F5 (laptop function layer)  
✗ **Power keys**: Power, Sleep  
✗ **System shortcuts**: Ctrl+Alt+Del, Win+L  

---

## Troubleshooting Quick Fixes

### Problem: "Hook registration failed"
**Solution:** 
1. Close other macro tools
2. Run MouseMacros as Administrator
3. Check antivirus settings

### Problem: Macro doesn't replay correctly
**Solution:**
1. Ensure target window is in the same position
2. Disable Quick Mode if timing is important
3. Use the same monitor as recording

### Problem: Hotkey doesn't work
**Solution:**
1. Check for conflicts (Settings > Custom Hotkey)
2. Try a different key (avoid media/Fn keys)
3. Ensure MouseMacros is running

### Problem: Can't uninstall
**Solution:**
Run `scripts\uninstall.ps1` or see `docs/MANUAL_UNINSTALL.md`

---

## Next Steps

### 📚 Learn More
- **Full FAQ**: `docs/FAQ.md`
- **User Feedback Improvements**: `docs/improvements/USER_FEEDBACK_ANALYSIS.md`
- **Manual Uninstall**: `docs/MANUAL_UNINSTALL.md`

### 🔧 Advanced Features
- **Repeat execution**: Automate repetitive tasks
- **Custom hotkeys**: Personalize your workflow
- **Dark mode**: Settings > Enable Dark Mode

### 🚀 Need More Power?
If you need scripting, variables, or conditional logic, check out our advanced tool:
**saMacros**: https://github.com/Samera2022/saMacros

---

## Getting Help

- **GitHub Issues**: https://github.com/Samera2022/MouseMacros/issues
- **Bilibili**: UID 583460263
- **QQ**: 1084262691

---

**Enjoy automating!** 🎯

If MouseMacros helps you, consider:
- ⭐ Starring the project on GitHub
- 🐛 Reporting bugs to help improve it
- 💡 Suggesting features you'd like to see