# Frequently Asked Questions (FAQ)

## Installation & Uninstallation

### Q: How do I install MouseMacros?
**A:** MouseMacros is portable and doesn't require installation. Simply:
1. Extract the ZIP file to any folder
2. Run `MouseMacros.jar` with Java (double-click or `java -jar MouseMacros.jar`)
3. *Optional*: Run `scripts\install.ps1` to register in Windows (adds to Add/Remove Programs)

### Q: How do I uninstall MouseMacros?
**A:** Three methods:
1. **Automated (Recommended)**: Run `scripts\uninstall.ps1` in PowerShell
2. **Windows Settings**: Settings > Apps > MouseMacros > Uninstall (if you ran install.ps1)
3. **Manual**: Delete the installation folder and `%USERPROFILE%\.mousemacros` directory

See `docs/MANUAL_UNINSTALL.md` for detailed manual uninstallation steps.

### Q: Will uninstalling remove my saved macros?
**A:** By default, NO. Configuration files (including saved macros) are preserved in `%USERPROFILE%\.mousemacros`. To also remove these files, run:
```powershell
.\scripts\uninstall.ps1 -RemoveConfig
```

---

## Recording Issues

### Q: Some hotkeys won't record. Why?
**A:** Certain keys are handled by hardware or the operating system and cannot be captured by Java-based applications:

**Keys that may fail:**
- ✗ Media keys (Volume Up/Down, Play, Pause, Stop)
- ✗ Fn function keys (Fn+F1, Fn+F2, etc.)
- ✗ Power/Sleep buttons
- ✗ Some laptop-specific keys

**Recommended keys:**
- ✓ F1-F12 function keys
- ✓ Letter keys (A-Z) + Ctrl/Shift/Alt
- ✓ Number keys (0-9)
- ✓ Arrow keys
- ✓ Numpad keys

### Q: My mouse movements aren't recording accurately
**A:** This can happen when:
- **High DPI/Scaling**: Windows display scaling affects coordinates. Try setting MouseMacros to run at 100% scaling or disable DPI awareness.
- **Multi-monitor**: Recording on one monitor and playing on another may cause issues. Record and play on the same monitor.
- **Window moved**: If you move application windows between recording and playback, coordinates won't match.

**Solution**: Use relative mouse movements or record with windows in consistent positions.

### Q: Keyboard shortcuts aren't being recorded
**A:** Check if:
1. The shortcut is a system-level hotkey (Ctrl+Alt+Del, Win+L, etc.) - these cannot be captured
2. Another application has registered that hotkey globally
3. The key is a Fn-dependent key on laptops

Try using different key combinations or check Settings > Custom Hotkey for conflicts.

---

## Playback Issues

### Q: Macro playback is too fast/slow
**A:** 
- **Too fast**: Disable "Quick Mode" in Settings. This preserves the timing between actions.
- **Too slow**: Enable "Quick Mode" to remove delays between actions.
- **Custom timing**: Use "Repeat Delay" setting to add fixed delays between macro repetitions.

### Q: Macro playback stops unexpectedly
**A:** Common causes:
1. **Window focus lost**: Some actions require specific windows to be active
2. **Screen layout changed**: Mouse coordinates are absolute. Moving windows or changing resolution breaks playback.
3. **Timing issues**: Application response times vary. Add manual delays in your macro if needed.

### Q: How do I stop a running macro?
**A:** Press the "Abort Macro" hotkey (default: F12). If unresponsive:
1. Press Ctrl+Alt+Del to open Task Manager
2. End the `javaw.exe` or `MouseMacros` process

---

## Configuration & Settings

### Q: Where are my settings/macros stored?
**A:** 
- **Settings**: `%USERPROFILE%\.mousemacros\config.json`
- **Saved macros**: Default location is `%USERPROFILE%\.mousemacros\macros\` (unless you changed it in Settings)
- **Logs**: `%USERPROFILE%\.mousemacros\logs\`

### Q: Can I backup my macros?
**A:** Yes! Your macros are stored as `.mmc` files. Simply:
1. Navigate to your macro storage folder (default: `%USERPROFILE%\.mousemacros\macros\`)
2. Copy the `.mmc` files to a backup location
3. To restore, copy them back and use "Load Macro" in the application

### Q: How do I reset to default settings?
**A:** 
1. Close MouseMacros
2. Delete `%USERPROFILE%\.mousemacros\config.json`
3. Restart MouseMacros - it will create a new config with defaults

---

## Features & Usage

### Q: What is "Quick Mode"?
**A:** Quick Mode ignores timing between recorded actions and executes them as fast as possible. Useful for:
- Simple click sequences
- Form filling
- Rapid repetitive actions

**Disable** Quick Mode when timing is important (e.g., waiting for applications to load).

### Q: Can I edit recorded macros?
**A:** Currently, MouseMacros doesn't have a built-in macro editor. Macro files (`.mmc`) are text-based and can be manually edited with a text editor, but this requires understanding the format. 

For advanced editing, scripting, and conditional logic, see our sister project **saMacros**: https://github.com/Samera2022/saMacros

### Q: Can I use variables or conditional logic?
**A:** No, MouseMacros focuses on simple recording and playback. For advanced features like:
- Variables
- Conditional statements (if/else)
- Loops
- JavaScript scripting

Use **saMacros** instead: https://github.com/Samera2022/saMacros

### Q: How many times can I repeat a macro?
**A:** Set in Settings > Repeat Times. Valid range: 1-99999. Set to 1 to execute once.

### Q: Can I pause macro playback?
**A:** Yes! Press the "Pause Macro" hotkey (appears when playing). Press "Resume Macro" to continue.

---

## Troubleshooting

### Q: MouseMacros won't start / shows blank window
**A:** 
1. **Check Java version**: Requires Java 11 or higher. Run `java -version` to check.
2. **Try command line**: Open CMD/PowerShell and run:
   ```
   java -jar MouseMacros.jar
   ```
   Check for error messages.
3. **Reset config**: Delete `%USERPROFILE%\.mousemacros\config.json` and try again.

### Q: "Hook registration failed" error
**A:** This means MouseMacros couldn't register global keyboard/mouse hooks. Causes:
- **Another application** is already using the same hooks (rare)
- **Antivirus/Security software** is blocking hook registration
- **Permissions issue**: Try running as Administrator (right-click > Run as administrator)

### Q: Interface is in the wrong language
**A:** Settings > Switch Language > Select your preferred language > Save Settings > Restart

### Q: Dark mode doesn't work / looks wrong
**A:** 
1. Toggle Settings > Enable Dark Mode
2. Click "Save and Apply All Settings"
3. If still wrong, try toggling "Follow System Settings"

### Q: Logs show errors I don't understand
**A:** Export logs (Settings > Export Log) and:
1. Check the exported log file for detailed error messages
2. Search GitHub Issues for similar problems
3. Create a new issue with the log excerpt

---

## System Requirements

### Q: What Java version do I need?
**A:** Java 11 or higher (Java 17+ recommended). Download from:
- **Oracle JDK**: https://www.oracle.com/java/technologies/downloads/
- **OpenJDK**: https://adoptium.net/

### Q: Does MouseMacros work on Linux/Mac?
**A:** MouseMacros is designed for Windows. While Java is cross-platform, the underlying JNativeHook library may have issues on Linux/Mac. For cross-platform macro support, try **saMacros**.

### Q: What Windows versions are supported?
**A:** Windows 7 SP1 and later (Windows 10/11 recommended).

---

## Support & Community

### Q: How do I report a bug?
**A:** 
1. Check [GitHub Issues](https://github.com/Samera2022/MouseMacros/issues) to see if it's already reported
2. If not, create a new issue with:
   - Windows version
   - Java version (`java -version`)
   - Steps to reproduce
   - Error messages (export logs if possible)

### Q: Can I request features?
**A:** Yes! Open a [Feature Request](https://github.com/Samera2022/MouseMacros/issues/new) on GitHub. Please search existing issues first to avoid duplicates.

### Q: Where can I get help?
**A:** 
- **GitHub Issues**: https://github.com/Samera2022/MouseMacros/issues
- **Bilibili**: UID 583460263
- **QQ**: 1084262691

### Q: How can I contribute?
**A:** Contributions are welcome! 
- Report bugs
- Suggest features
- Submit pull requests (see CONTRIBUTING.md if available)
- Star the project on GitHub!

---

## Comparisons

### Q: MouseMacros vs saMacros - which should I use?
**A:** 

**MouseMacros** (this project):
- ✓ Lightweight and fast
- ✓ Simple recording/playback
- ✓ Minimal learning curve
- ✗ No scripting
- ✗ No conditional logic
- ✗ Windows-focused

**saMacros**:
- ✓ JavaScript scripting support
- ✓ Variables and conditional logic
- ✓ Cross-platform (Windows/Linux/Mac)
- ✓ Advanced macro editing
- ✗ Heavier resource usage
- ✗ Steeper learning curve

**Recommendation**: Start with MouseMacros for simple tasks. Upgrade to saMacros if you need scripting.

### Q: Is MouseMacros better than [commercial tool]?
**A:** MouseMacros is:
- ✓ 100% Free and open-source
- ✓ No ads or bundled software
- ✓ Lightweight
- ✓ Active development

Trade-offs:
- ✗ Fewer features than premium tools
- ✗ Limited to Windows
- ✗ No GUI macro editor

---

## Privacy & Security

### Q: Does MouseMacros collect data?
**A:** **NO.** MouseMacros does not:
- Collect or transmit any user data
- Connect to the internet (except checking for updates if enabled)
- Include telemetry or analytics
- Display ads

All data stays on your computer.

### Q: Is it safe to use?
**A:** Yes, MouseMacros is open-source and auditable. However:
- Be careful with macros from untrusted sources
- Macros can perform any action you can, so review before running
- Antivirus software may flag it due to global hook usage (false positive)

### Q: Why does antivirus flag it?
**A:** Global keyboard/mouse hooks are used by both legitimate tools and malware. Some antivirus software flags *any* application using hooks. MouseMacros is safe - you can verify by:
- Reviewing the open-source code on GitHub
- Scanning with multiple antivirus engines (VirusTotal)
- Building from source yourself

---

## Licensing

### Q: Can I use MouseMacros commercially?
**A:** Check the LICENSE file in the repository. Typically, open-source licenses allow commercial use with attribution.

### Q: Can I modify and redistribute MouseMacros?
**A:** Yes, according to the project's open-source license. Please:
- Maintain attribution to the original author
- Follow the license terms
- Consider contributing improvements back to the main project

---

**Last Updated:** 2026-09-28  
**Version:** 2.0.0

For more help, visit: https://github.com/Samera2022/MouseMacros