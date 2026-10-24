# Optional per-user registration for the extracted Windows ZIP or JAR layout.
# MSI packages use Windows Installer and must not use this script.
param([switch]$CreateShortcuts = $true, [switch]$Silent = $false)
$ErrorActionPreference = 'Stop'
try {
    . (Join-Path $PSScriptRoot 'package-common.ps1')
    $layout = Get-MouseMacrosLayout -ScriptDirectory $PSScriptRoot
    $InstallDir = $layout.Root
    $uninstaller = Join-Path $PSScriptRoot 'uninstall.ps1'
    if (-not (Test-Path -LiteralPath $uninstaller -PathType Leaf)) { throw 'Uninstaller is missing.' }

    if ($CreateShortcuts) {
        $shell = New-Object -ComObject WScript.Shell
        $startMenu = Join-Path $env:APPDATA 'Microsoft/Windows/Start Menu/Programs'
        New-Item -Path $startMenu -ItemType Directory -Force | Out-Null
        $shortcut = $shell.CreateShortcut((Join-Path $startMenu 'MouseMacros.lnk'))
        if ($layout.IsAppImage) {
            $shortcut.TargetPath = Join-Path $InstallDir 'MouseMacros.exe'
        } else {
            $shortcut.TargetPath = (Get-Command javaw.exe -ErrorAction Stop).Source
            $shortcut.Arguments = "-jar `"$($layout.Jar)`""
        }
        $shortcut.WorkingDirectory = $InstallDir
        $shortcut.Description = 'MouseMacros - Keyboard and Mouse Macro Recorder'
        $shortcut.Save()
    }

    $configDir = @(Get-MouseMacrosConfigDirectories)[0]
    New-Item -Path $configDir -ItemType Directory -Force | Out-Null
    $regPath = 'HKCU:\Software\Microsoft\Windows\CurrentVersion\Uninstall\MouseMacros'
    New-Item -Path $regPath -Force | Out-Null
    $properties = @{
        DisplayName = 'MouseMacros'; DisplayVersion = '2.1.0'; Publisher = 'Samera2022'
        InstallLocation = $InstallDir
        UninstallString = "powershell.exe -NoProfile -ExecutionPolicy Bypass -File `"$uninstaller`" -Silent"
        DisplayIcon = $(if ($layout.IsAppImage) { Join-Path $InstallDir 'MouseMacros.exe' } else { $layout.Jar })
    }
    foreach ($name in $properties.Keys) {
        New-ItemProperty -Path $regPath -Name $name -Value $properties[$name] -PropertyType String -Force | Out-Null
    }
    foreach ($name in @('NoModify', 'NoRepair')) {
        New-ItemProperty -Path $regPath -Name $name -Value 1 -PropertyType DWord -Force | Out-Null
    }
    Write-Host 'MouseMacros registered. Uninstall via Windows Settings > Apps > MouseMacros.'
    if (-not $Silent) { Read-Host 'Press Enter to exit' | Out-Null }
} catch {
    Write-Error "Installation failed: $_"
    exit 1
}
