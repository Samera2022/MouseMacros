# MouseMacros

<p align="center">
  <img src="https://raw.githubusercontent.com/Samera2022/MouseMacros/main/docs/images/MouseMacrosIcon.png" alt="MouseMacros Logo" width="120">
  <br>
  <b>A lightweight, cross-platform Java tool for recording and replaying mouse and keyboard macros.</b>
  <br>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-1.8%2B-orange.svg" alt="Java Version">
  <img src="https://img.shields.io/badge/License-GPL--3.0-blue.svg" alt="License">
  <img src="https://img.shields.io/github/v/release/Samera2022/MouseMacros" alt="Latest Release">
</p>

---

## 📸 Preview

![Main Interface](docs/images/MouseMacrosMainFrame.png)
*The main interface of MouseMacros.*

---

## ✨ Features

* **Comprehensive Recording**: Capture Mouse Left/Right/Middle clicks, Scroll Wheel movements, and Keyboard inputs seamlessly.
* **Global Hotkeys**: Control the application even when it's minimized. Fully customizable keys for:
    * Start/Stop Recording
    * Start Playback
    * **Force Abort** (Emergency stop for runaway macros)
* **Multi-Language Support**: Built-in localization for **English (US)** and **Simplified Chinese**.
* **Theme Engine**: Supports **Light** and **Dark** modes, with an option to follow system settings automatically.
* **Persistence**: Macros are saved as `.mmc` (CSV-formatted) files, allowing for easy sharing and manual editing.
* **Smart Memory**: Remembers window sizes, last-used directories, and custom configurations across sessions.

---

## 🚀 Getting Started

### Installation
I. Jar User
1. Make sure that you have installed JRE 1.8 or above. If not, you can download [here](https://www.oracle.com/technetwork/cn/java/javase/downloads/jre8-downloads-2133155-zhs.html). 
2. Download the latest `.jar` file from the [Releases](https://github.com/Samera2022/MouseMacros/releases) page. 
3. Double-click the jar file OR use cmd to run the application:
    ```bash
    java -jar MouseMacros.jar
    ```
II. Exe User
1. Download the latest `.exe` file from the [Releases](https://github.com/Samera2022/MouseMacros/releases) page.
2. Click to start! All environments are integrated into one `exe` file!

### Usage
![Settings Dialog](docs/images/MouseMacrosSettingsDialog.png)
1. **Adjust**: The choose of language will determine the words in the frame, thus resulting in some buttons not being displayed in the frame. 
In this case, you will need to adjust the frame to the appropriate size.
2. **Configure**: Open the Settings dialog and Macros Settings dialog to set your preferred hotkeys. For detailed configuration docs, please refer to [Configuration](#%EF%B8%8F-configuration)
3. **Record**: Press your "Start Recording" hotkey or press this button in the frame and perform the actions.
4. **Save**: Use "Save Macros" to export your recording to a `.mmc` file.
5. **Replay**: Use "Load Macro" to load a `.mmc` file and press "Play Macro".

---

## ⚙️ Configuration

The application stores settings in the user's AppData directory:
`%USERPROFILE%/AppData/MouseMacros/`

| File         | Description                                                             |
|:-------------|:------------------------------------------------------------------------|
| `config.cfg` | Stores UI language, theme mode, key mappings, and default storage path. |
| `cache.json` | Stores recent file paths and window dimensions.                         |

### More Detailed Configuration Options
| Name                             | Key                             | Description                                                                                                                                                                                                                                                                                                                           |
|:---------------------------------|:--------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Follow System Settings           | `followSystemSettings`(boolean) | Controls whether to follow System default settings or not.                                                                                                                                                                                                                                                                            |
| Switch Language                  | `lang`(String)                  | If `followSystemSettings` is false, you can use this combo box to choose another display language.                                                                                                                                                                                                                                    |
| Enable Dark Mode                 | `enableDarkMode`(boolean)       | If `followSystemSettings` is false, you can use this check box to choose whether to enable Dark Mode.                                                                                                                                                                                                                                 |
| Enable Default Storage           | `enableDefaultStorage`(boolean) | Controls whether to enable `defaultMmcStoragePath`. If it is true, the `lastSaveDirectory` and `lastLoadDirectory` in cache.json will be ignored. Every time you open the FileChooserDialog(in "Save Macro" and "Load Macro"), it will automatically open the folder with `defaultMmcStoragePath`. The same applies in reverse.       |
| Default MouseMacros Storage Path | `defaultMmcStoragePath`(String) | If `followSystemSettings` is true, it will determine the default folder everytime you open the FileChooserDialog(in "Save Macro" and "Load Macro"). If the folder in this option doesn't exist, the app will first attempt to create this folder, otherwise it will automatically open the default folder(Your User Document Folder). |


---

## 📄 Others

### 🤝 Contributing
Contributions are welcome! If you find a bug or have a feature request, please open an issue.
### 👤 Author
**Developer: Samera2022**
* **GitHub**: [@Samera2022](https://github.com/Samera2022)
### 📄 License
This project is licensed under the GPL-3.0 License - see the `LICENSE` file for details.