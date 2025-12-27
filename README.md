# MouseMacros

<p align="center">
  <img src="https://raw.githubusercontent.com/Samera2022/MqouseMacros/main/docs/images/MouseMacrosIcon.png" alt="MouseMacros Logo" width="120">
  <br>
  <b>A lightweight, cross-platform Java tool for recording and replaying mouse and keyboard macros.</b>
  <br>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-1.8%2B-orange.svg" alt="Java Version">
  <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License">
  <img src="https://img.shields.io/github/v/release/Samera2022/MouseMacros" alt="Latest Release">
</p>

---

## 📸 Preview

![Main Interface](https://via.placeholder.com/800x450?text=Place+Main+UI+Screenshot+Here)
*The clean and intuitive main interface of MouseMacros.*

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

## 🛠 Tech Stack

* **Language**: Java (Swing for GUI)
* **Core Libraries**:
    * [JNativeHook](https://github.com/kwasnevski/JNativeHook): For system-level global input listening.
    * [Gson](https://github.com/google/gson): For configuration and cache serialization.
* **Architecture**: Singleton pattern for UI management and Event-Dispatch-Thread (EDT) safety.

---

## 🚀 Getting Started

### Prerequisites
* Java Runtime Environment (JRE) or JDK 1.8 or higher.

### Installation
1.  Download the latest `.jar` file from the [Releases](https://github.com/Samera2022/MouseMacros/releases) page.
2.  Run the application:
    ```bash
    java -jar MouseMacros.jar
    ```

### Usage
1.  **Configure**: Open the Settings dialog and Macros Settings dialog to set your preferred hotkeys.
    ![Settings Dialog](https://via.placeholder.com/400x300?text=Place+Settings+Dialog+Screenshot+Here)
2.  **Record**: Press your "Start" hotkey and perform the actions.
3.  **Save**: Export your recording to a `.mmc` file.
4.  **Replay**: Load a file and press "Play".

---

## ⚙️ Configuration

The application stores settings in the user's AppData directory:
`%USERPROFILE%/AppData/MouseMacros/`

| File | Description |
| :--- | :--- |
| `config.cfg` | Stores UI language, theme mode, and key mappings. |
| `cache.json` | Stores recent file paths and window dimensions. |

---

## 📈 Version Evolution

| Version    | Highlights                                                             |
|:-----------|:-----------------------------------------------------------------------|
| **v1.0.0** | Added Keyboard/Middle-click/Wheel support; Implemented Playback Abort. |
| **v0.1.0** | Major architecture refactor; Fixed KeyMap persistence issues.          |
| **v0.0.2** | Introduced Settings dialog and configuration file system.              |
| **v0.0.1** | Initial release; Basic Mouse Left/Right click recording.               |

---

## 📄 Others

### 🤝 Contributing
Contributions are welcome! If you find a bug or have a feature request, please open an issue.
### 👤 Author
**Developer: Samera2022**
* **GitHub**: [@Samera2022](https://github.com/Samera2022)
### 📄 License
This project is licensed under the MIT License - see the `LICENSE` file for details.