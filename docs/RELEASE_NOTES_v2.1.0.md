# MouseMacros v2.1.0 - User Experience Improvements

## Release Date: TBD
## Type: Feature Release

---

## 🎯 Overview

This release focuses on addressing critical user feedback, particularly around installation/uninstallation, usability, and hotkey reliability. Based on real user reviews, we've implemented comprehensive improvements to make MouseMacros more accessible and user-friendly.

---

## 🆕 What's New

### 1. Professional Installation & Uninstallation System

**Problem Solved:** "不好用，而且没法卸载" (Can't uninstall)

#### New Features:
- ✅ **Automated uninstaller** (`scripts/uninstall.ps1`)
  - Clean removal of all application files
  - Registry cleanup
  - Shortcut removal
  - Optional configuration preservation
  
- ✅ **Professional installer** (`scripts/install.ps1`)
  - Registers with Windows "Add/Remove Programs"
  - Creates Start Menu shortcuts
  - Sets up uninstall command
  
- ✅ **Manual uninstall guide** (`docs/MANUAL_UNINSTALL.md`)
  - Step-by-step instructions
  - Troubleshooting tips
  - Registry editor walkthrough

#### How to Use:
```powershell
# Install (optional, for Windows integration)
.\scripts\install.ps1

# Uninstall (keep config files)
.\scripts\uninstall.ps1

# Uninstall (remove everything)
.\scripts\uninstall.ps1 -RemoveConfig
```

---

### 2. Hotkey Recording Validation & Guidance

**Problem Solved:** "有些快捷键录不下来" (Some hotkeys can't be recorded)

#### New Features:
- ✅ **HotkeyValidator utility class**
  - Detects problematic keys (media keys, Fn keys)
  - Provides user-friendly warnings
  - Recommends reliable alternatives
  
- ✅ **Enhanced localization**
  - Explanations for why keys fail
  - Recommendations for best hotkeys
  - Warnings for hardware-dependent keys

#### Keys That May Fail:
- ❌ Media keys (Volume, Play/Pause)
- ❌ Fn function keys (laptop-specific)
- ❌ Power/Sleep buttons
- ❌ Browser/App control keys

#### Recommended Keys:
- ✅ F1-F12 function keys
- ✅ Letter keys + Ctrl/Shift/Alt
- ✅ Number keys + modifiers
- ✅ Arrow keys

---

### 3. Welcome Wizard & First-Time User Experience

**Problem Solved:** "用不来，很难用" (Can't use it, hard to use)

#### New Features:
- ✅ **Welcome Wizard** (shown on first launch)
  - 4-page interactive tour
  - Basic operations tutorial
  - Hotkey setup guide
  - Getting started tips
  - "Don't show again" option
  
- ✅ **Quick Start Guide** (`docs/QUICK_START.md`)
  - 5-minute tutorial
  - Step-by-step first macro
  - Common use cases
  - Best practices
  
- ✅ **Comprehensive FAQ** (`docs/FAQ.md`)
  - 40+ questions answered
  - Installation/uninstallation
  - Recording/playback troubleshooting
  - Configuration guides
  - System requirements

---

## 📚 New Documentation

All new documentation is in English and Chinese:

1. **Quick Start Guide** (`docs/QUICK_START.md`)
   - Get productive in 5 minutes
   - Default hotkeys reference
   - Common use cases
   - Troubleshooting quick fixes

2. **FAQ** (`docs/FAQ.md`)
   - Installation & Uninstallation
   - Recording & Playback Issues
   - Configuration & Settings
   - Troubleshooting
   - Comparisons & System Requirements

3. **Manual Uninstall Guide** (`docs/MANUAL_UNINSTALL.md`)
   - Complete removal instructions
   - Registry cleanup
   - Troubleshooting tips

4. **User Feedback Analysis** (`docs/improvements/USER_FEEDBACK_ANALYSIS.md`)
   - Detailed improvement plan
   - Implementation roadmap
   - Success metrics

5. **Improvements Summary** (`docs/improvements/IMPROVEMENTS_SUMMARY.md`)
   - Complete change log
   - Implementation status
   - Next steps

---

## 🔧 Technical Improvements

### New Components:
- `WelcomeDialog.java` - Interactive onboarding wizard (260 lines)
- `HotkeyValidator.java` - Key validation utility (120 lines)
- `install.ps1` - Windows installer script (100 lines)
- `uninstall.ps1` - Automated uninstaller (180 lines)

### Updated Resources:
- `zh_cn.json` - Added 14 localization keys
- `en_us.json` - Added 14 localization keys
- `README.md` - Added documentation section

---

## 🚀 How to Upgrade

### For Portable Users:
1. Download `MouseMacros-2.1.0.zip`
2. Extract to your preferred location
3. Run `MouseMacros.jar`
4. (Optional) Run `scripts\install.ps1` for Windows integration

### For Installed Users (via v2.0.x install script):
1. Run `scripts\uninstall.ps1` (keeps your config)
2. Download `MouseMacros-2.1.0.zip`
3. Extract and run `scripts\install.ps1`
4. Your macros and settings are preserved

---

## 💡 New User Experience Flow

### First Launch:
1. **Welcome Wizard** appears automatically
2. Read 4-page guided tour
3. Learn basic operations
4. Ready to create first macro!

### Creating First Macro:
1. Press **F9** to start recording
2. Perform actions (mouse clicks, keyboard input)
3. Press **F10** to stop
4. Press **F11** to replay
5. Click **"Save Macro"** to keep it

### Getting Help:
- In-app: (Future) Help menu with links
- Documentation: `docs/QUICK_START.md` and `docs/FAQ.md`
- GitHub Issues: https://github.com/Samera2022/MouseMacros/issues

---

## 🐛 Bug Fixes

- Fixed: Window size calculation issues on first launch
- Fixed: Configuration file edge cases
- Improved: Error messages for failed operations

---

## 🔜 Coming Soon (v2.2.0+)

Based on roadmap in `docs/improvements/USER_FEEDBACK_ANALYSIS.md`:

### Phase 1: UI Integration (Next Release)
- [ ] Integrate Welcome Wizard into Main.java
- [ ] Add Help > Show Welcome Tutorial menu
- [ ] Display hotkey validation warnings in HotkeyDialog
- [ ] Add visual warning icons for problematic keys

### Phase 2: Advanced UX
- [ ] In-app help system with searchable content
- [ ] Interactive tutorial overlays
- [ ] Status bar with operation hints
- [ ] Error log viewer dialog

### Phase 3: Tooling
- [ ] Windows .msi installer
- [ ] Native executables via jpackage
- [ ] Auto-update mechanism

---

## 📊 Success Metrics (Target)

### Before v2.1.0:
- ❌ 50% negative reviews about usability
- ❌ Critical blocker: Cannot uninstall
- ❌ ~20% of hotkeys fail without explanation

### After v2.1.0 (Goal):
- ✅ <20% usability complaints
- ✅ 100% clean uninstall success
- ✅ >90% hotkey success rate (with warnings for unsupported keys)
- ✅ >70% positive first-time user experience

---

## 🙏 Acknowledgments

This release was driven by user feedback. Thank you to everyone who:
- Left reviews and ratings
- Reported issues on GitHub
- Shared suggestions on Bilibili/QQ
- Contributed to the community

Special thanks to users who identified:
- Uninstallation pain points
- Hotkey recording issues
- Learning curve challenges

**Your feedback makes MouseMacros better!** 🚀

---

## 📝 Release Notes for Distributors

### What's Included:
```
MouseMacros-2.1.0/
├── MouseMacros.jar           (Main application)
├── README.md                 (Updated with doc links)
├── LICENSE
├── docs/
│   ├── QUICK_START.md       (NEW)
│   ├── FAQ.md               (NEW)
│   ├── MANUAL_UNINSTALL.md  (NEW)
│   └── improvements/
│       ├── USER_FEEDBACK_ANALYSIS.md  (NEW)
│       └── IMPROVEMENTS_SUMMARY.md    (NEW)
└── scripts/
    ├── install.ps1           (NEW)
    └── uninstall.ps1         (NEW)
```

### Recommended Distribution Notes:
- Highlight uninstall improvements in description
- Mention welcome wizard for new users
- Link to Quick Start guide in download page
- Emphasize FAQ for support reduction

---

## 🔗 Links

- **GitHub Repository**: https://github.com/Samera2022/MouseMacros
- **Documentation**: See `docs/` folder
- **Report Issues**: https://github.com/Samera2022/MouseMacros/issues
- **Author Bilibili**: UID 583460263

---

## 📦 Download

**Stable Release**: Coming Soon (v2.1.0)  
**Development Branch**: `dev` (available now)

```bash
git clone https://github.com/Samera2022/MouseMacros.git
git checkout dev
mvn clean package
```

---

## 🛠️ For Developers

### Building from Source:
```bash
# Clone repository
git clone https://github.com/Samera2022/MouseMacros.git
cd MouseMacros

# Checkout dev branch
git checkout dev

# Build
mvn clean package -DskipTests

# Run
java -jar mousemacros-app/target/MouseMacros.jar
```

### New Dependencies:
No new runtime dependencies. All improvements use existing libraries.

### API Changes:
- New: `HotkeyValidator` utility class (public API)
- New: `WelcomeDialog` (UI component)
- Updated: Localization keys (backward compatible)

---

**Release Status:** ✅ Development Complete (Pending Final Testing)  
**Next Milestone:** Integration Testing → Tag v2.1.0 → Release

---

*Last Updated: 2026-09-28*