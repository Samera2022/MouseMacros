# Unregister and remove an extracted Windows ZIP/JAR installation.
# MSI installations must be removed by Windows Installer (Settings > Apps).
param([switch]$RemoveConfig = $false, [switch]$Silent = $false)
$ErrorActionPreference = 'Stop'
try {
    . (Join-Path $PSScriptRoot 'package-common.ps1')
    $layout = Get-MouseMacrosLayout -ScriptDirectory $PSScriptRoot
    $InstallDir = $layout.Root
    $exe = Join-Path $InstallDir 'MouseMacros.exe'
    $jarPattern = '(?i)(?:^|\s)-jar\s+(?:"' + [regex]::Escape($layout.Jar) + '"|' + [regex]::Escape($layout.Jar) + '(?=\s|$))'
    # Match only this installation, including javaw and tray-hidden windows.
    $instances = @(Get-CimInstance Win32_Process -Filter "Name='java.exe' OR Name='javaw.exe' OR Name='MouseMacros.exe'" |
        Where-Object { ($_.ExecutablePath -and $_.ExecutablePath -eq $exe) -or
                      ($_.Name -in @('java.exe', 'javaw.exe') -and $_.CommandLine -match $jarPattern) })
    if ($instances.Count -gt 0 -and -not $Silent) {
        if ((Read-Host 'Close MouseMacros to uninstall? (Y/N)') -ne 'Y') { throw 'Uninstall cancelled.' }
    }
    foreach ($instance in $instances) {
        Stop-Process -Id $instance.ProcessId -ErrorAction Stop
        Wait-Process -Id $instance.ProcessId -Timeout 10 -ErrorAction SilentlyContinue
        if (Get-Process -Id $instance.ProcessId -ErrorAction SilentlyContinue) { throw 'MouseMacros is still running.' }
    }

    # Remove known application files; preserve unrelated files and user macros.
    $files = @('MouseMacros.jar', 'MouseMacros.exe')
    if ($layout.IsAppImage) { $files += @('app/MouseMacros.jar', 'app/MouseMacros.cfg', 'app/.jpackage.xml', 'MouseMacros.ico') }
    foreach ($file in $files) {
        $path = Join-Path $InstallDir $file
        if (Test-Path -LiteralPath $path) { Remove-Item -LiteralPath $path -Force -ErrorAction Stop }
    }
    if ($layout.IsAppImage) {
        $runtime = Join-Path $InstallDir 'runtime'
        if (Test-Path -LiteralPath $runtime) { Remove-Item -LiteralPath $runtime -Recurse -Force -ErrorAction Stop }
    }
    if ($RemoveConfig) {
        foreach ($directory in Get-MouseMacrosConfigDirectories) {
            if (Test-Path -LiteralPath $directory) { Remove-Item -LiteralPath $directory -Recurse -Force -ErrorAction Stop }
        }
    } else { Write-Host 'User settings and recorded macros were preserved.' }

    $startMenu = Join-Path $env:APPDATA 'Microsoft/Windows/Start Menu/Programs'
    $shortcuts = @((Join-Path $startMenu 'MouseMacros.lnk'), (Join-Path $startMenu 'MouseMacros/MouseMacros.lnk'))
    $desktop = [Environment]::GetFolderPath('Desktop')
    if ($desktop) { $shortcuts += Join-Path $desktop 'MouseMacros.lnk' }
    foreach ($path in $shortcuts) {
        if (Test-Path -LiteralPath $path) { Remove-Item -LiteralPath $path -Force -ErrorAction Stop }
    }
    $regPath = 'HKCU:\Software\Microsoft\Windows\CurrentVersion\Uninstall\MouseMacros'
    if (Test-Path $regPath) { Remove-Item -Path $regPath -Recurse -Force -ErrorAction Stop }
    foreach ($file in @('install.ps1', 'uninstall.ps1', 'package-common.ps1')) {
        $path = Join-Path $PSScriptRoot $file
        if (Test-Path -LiteralPath $path) { Remove-Item -LiteralPath $path -Force -ErrorAction Stop }
    }
    Write-Host "Uninstall complete. Any remaining user files in '$InstallDir' were preserved."
    if (-not $Silent) { Read-Host 'Press Enter to exit' | Out-Null }
} catch {
    Write-Error "Uninstallation failed: $_"
    exit 1
}
