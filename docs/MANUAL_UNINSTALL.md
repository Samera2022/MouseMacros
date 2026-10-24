# Installation and Uninstallation

Choose the instructions for the file you downloaded. An EXE launcher is not necessarily an installer.

| Distribution | Installation | Removal |
| --- | --- | --- |
| Microsoft Store / `.msix` | Install through Store (or a trusted, signed MSIX) | Windows Settings → Apps → MouseMacros → Uninstall; do not run portable scripts |
| `.msi` | Run the installer | Windows Settings → Apps → MouseMacros → Uninstall (Windows Installer) |
| `.zip` | Extract the complete folder and run `MouseMacros.exe` | Close the app and delete the extracted folder; optional Windows registration is described below |
| Standalone `.exe` | Run the portable executable | Close the app (including its tray icon), then delete that executable |
| `.jar` | Run with Java 11+: `java -jar MouseMacros.jar` | Close the app and delete the JAR |

## Optional ZIP registration

The Windows ZIP includes `scripts/install.ps1`, `scripts/uninstall.ps1`, and `scripts/package-common.ps1`.
To add a per-user Start Menu shortcut and an entry in Windows Settings, run from the extracted folder:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File ".\scripts\install.ps1"
```

Keep the scripts folder with the application. After registration, use Windows Settings → Apps → MouseMacros → Uninstall, or:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File ".\scripts\uninstall.ps1"
```

These scripts also support a JAR at the parent of the scripts folder. They do not manage MSI installations.
Uninstall stops only processes belonging to that installation. Unrelated files and saved `.mmc` macros are preserved.
If a file cannot be removed, the script fails rather than reporting success.

## Optional settings removal

The application's current Windows settings folder is `%LOCALAPPDATA%\MouseMacros`.
Older registration scripts also created `%USERPROFILE%\.mousemacros`.
Both folders are preserved by default. To explicitly remove them during scripted uninstall:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File ".\scripts\uninstall.ps1" -RemoveConfig
```

Back up anything you need in those folders first. Macros saved elsewhere remain in their chosen locations.
For portable EXE/JAR removal or after MSI uninstall, these settings folders can instead be deleted manually.
On macOS the settings folder is `~/Library/Application Support/MouseMacros`; on Linux it is `~/.local/share/MouseMacros`.

## Troubleshooting

- Close MouseMacros via its tray menu before deleting portable application files. In Task Manager, identify this application's launcher or Java process; do not terminate every Java process.
- If the files are still locked, restart Windows and retry.
- For an MSI installation, use Windows Installer removal/repair rather than manually deleting its registry entry.
- For a ZIP/JAR registered by `install.ps1`, the per-user entry is `HKCU:\Software\Microsoft\Windows\CurrentVersion\Uninstall\MouseMacros`; its Start Menu link is `%APPDATA%\Microsoft\Windows\Start Menu\Programs\MouseMacros.lnk`.

## Validation status

The scripts have Linux PowerShell fixture tests in `scripts/test-package-scripts.ps1`, with mocked Windows registry/process APIs. A passing fixture test is not a Windows installation test.
Before releasing, test the actual MSI and ZIP on a disposable Windows VM: install, launch, close to tray, uninstall through Settings, verify shortcut and application/runtime removal, and verify that configuration remains unless removal was explicitly requested.
