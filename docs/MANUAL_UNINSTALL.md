# Manual Uninstallation Guide

If the automated uninstall script fails, follow these steps to manually remove MouseMacros.

## Step 1: Close Running Application

1. Press `Ctrl+Shift+Esc` to open Task Manager
2. Find `java.exe` or `javaw.exe` with "MouseMacros" in the description
3. Right-click > End Task

## Step 2: Remove Application Files

Delete the MouseMacros installation folder, typically located at:
- `C:\Program Files\MouseMacros\` or
- `D:\Programs\MouseMacros\` or
- Your custom installation directory

## Step 3: Remove Configuration Files (Optional)

Delete the configuration folder:
```
%USERPROFILE%\.mousemacros
```

To access this folder:
1. Press `Win+R`
2. Type: `%USERPROFILE%\.mousemacros`
3. Press Enter
4. Delete the folder

## Step 4: Remove Registry Entries

### Using Registry Editor (regedit)

1. Press `Win+R`
2. Type: `regedit` and press Enter
3. Navigate to: `HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Uninstall`
4. Look for `MouseMacros` folder
5. Right-click > Delete

### Using PowerShell (Recommended)

Run this command in PowerShell:
```powershell
Remove-Item -Path "HKCU:\Software\Microsoft\Windows\CurrentVersion\Uninstall\MouseMacros" -Recurse -Force
```

## Step 5: Remove Shortcuts

### Start Menu
Delete: `%APPDATA%\Microsoft\Windows\Start Menu\Programs\MouseMacros.lnk`

### Desktop
Delete: `%USERPROFILE%\Desktop\MouseMacros.lnk`

## Verification

After completing all steps, verify removal:

1. Open Windows Settings > Apps
2. Search for "MouseMacros"
3. Should show "No results"

## Troubleshooting

### "Access Denied" Errors
- Run PowerShell or Command Prompt as Administrator
- Retry deletion

### Files Still Locked
- Restart your computer
- Try deletion again

### Registry Key Won't Delete
- Export the key first (File > Export) as backup
- Close all applications
- Try deletion again

## Need Help?

If you continue to experience issues:
1. Check the project issues page on GitHub
2. Create a new issue with:
   - Windows version
   - Error messages
   - Steps you've already tried

## Automated Uninstall Script

For future reference, the automated uninstall script is located at:
```
<installation-directory>\scripts\uninstall.ps1
```

Run with PowerShell:
```powershell
powershell.exe -ExecutionPolicy Bypass -File "path\to\uninstall.ps1"

# To also remove configuration:
powershell.exe -ExecutionPolicy Bypass -File "path\to\uninstall.ps1" -RemoveConfig
```