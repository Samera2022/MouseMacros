# MouseMacros Uninstaller
# Removes application files, registry entries, and configuration data

param(
    [switch]$RemoveConfig = $false,
    [switch]$Silent = $false
)

$ErrorActionPreference = "Stop"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   MouseMacros Uninstaller v2.0.0" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Detect installation directory
$InstallDir = $PSScriptRoot
if (Test-Path "$InstallDir\MouseMacros.jar") {
    Write-Host "[OK] Installation found at: $InstallDir" -ForegroundColor Green
} else {
    Write-Host "[ERROR] MouseMacros.jar not found in: $InstallDir" -ForegroundColor Red
    Write-Host "Please run this script from the MouseMacros installation directory." -ForegroundColor Yellow
    exit 1
}

# Configuration directory
$ConfigDir = Join-Path $env:USERPROFILE ".mousemacros"

# Function to close running instances
function Stop-MouseMacros {
    Write-Host "[1/5] Checking for running instances..." -ForegroundColor Yellow
    
    $processes = Get-Process -Name "java" -ErrorAction SilentlyContinue | 
                 Where-Object { $_.MainWindowTitle -like "*MouseMacros*" }
    
    if ($processes) {
        Write-Host "      Found $($processes.Count) running instance(s)" -ForegroundColor Yellow
        if (-not $Silent) {
            $confirm = Read-Host "      Stop MouseMacros now? (Y/N)"
            if ($confirm -ne 'Y') {
                Write-Host "[ERROR] Cannot uninstall while application is running." -ForegroundColor Red
                exit 1
            }
        }
        
        foreach ($proc in $processes) {
            try {
                $proc.Kill()
                $proc.WaitForExit(5000)
                Write-Host "      [OK] Stopped process $($proc.Id)" -ForegroundColor Green
            } catch {
                Write-Host "      [WARN] Could not stop process $($proc.Id)" -ForegroundColor Yellow
            }
        }
    } else {
        Write-Host "      [OK] No running instances found" -ForegroundColor Green
    }
}

# Function to remove registry entries
function Remove-RegistryEntries {
    Write-Host "[2/5] Removing registry entries..." -ForegroundColor Yellow
    
    $regPath = "HKCU:\Software\Microsoft\Windows\CurrentVersion\Uninstall\MouseMacros"
    
    if (Test-Path $regPath) {
        try {
            Remove-Item -Path $regPath -Recurse -Force
            Write-Host "      [OK] Registry entries removed" -ForegroundColor Green
        } catch {
            Write-Host "      [WARN] Could not remove registry entries: $_" -ForegroundColor Yellow
        }
    } else {
        Write-Host "      [OK] No registry entries found" -ForegroundColor Green
    }
}

# Function to remove Start Menu shortcuts
function Remove-Shortcuts {
    Write-Host "[3/5] Removing shortcuts..." -ForegroundColor Yellow
    
    $startMenuPath = Join-Path $env:APPDATA "Microsoft\Windows\Start Menu\Programs\MouseMacros"
    $desktopShortcut = Join-Path ([Environment]::GetFolderPath("Desktop")) "MouseMacros.lnk"
    
    $removed = 0
    
    if (Test-Path $startMenuPath) {
        Remove-Item -Path $startMenuPath -Recurse -Force
        $removed++
    }
    
    if (Test-Path $desktopShortcut) {
        Remove-Item -Path $desktopShortcut -Force
        $removed++
    }
    
    if ($removed -gt 0) {
        Write-Host "      [OK] Removed $removed shortcut(s)" -ForegroundColor Green
    } else {
        Write-Host "      [OK] No shortcuts found" -ForegroundColor Green
    }
}

# Function to remove configuration
function Remove-Configuration {
    Write-Host "[4/5] Handling configuration files..." -ForegroundColor Yellow
    
    if (Test-Path $ConfigDir) {
        if ($RemoveConfig) {
            try {
                Remove-Item -Path $ConfigDir -Recurse -Force
                Write-Host "      [OK] Configuration removed: $ConfigDir" -ForegroundColor Green
            } catch {
                Write-Host "      [WARN] Could not remove configuration: $_" -ForegroundColor Yellow
            }
        } else {
            Write-Host "      [INFO] Configuration preserved: $ConfigDir" -ForegroundColor Cyan
            Write-Host "      To remove config, run: uninstall.ps1 -RemoveConfig" -ForegroundColor Cyan
        }
    } else {
        Write-Host "      [OK] No configuration found" -ForegroundColor Green
    }
}

# Function to remove application files
function Remove-ApplicationFiles {
    Write-Host "[5/5] Removing application files..." -ForegroundColor Yellow
    
    # List of files to remove
    $filesToRemove = @(
        "MouseMacros.jar",
        "MouseMacros.exe",
        "README.md",
        "LICENSE",
        "uninstall.ps1",
        "install.ps1"
    )
    
    $removed = 0
    foreach ($file in $filesToRemove) {
        $filePath = Join-Path $InstallDir $file
        if (Test-Path $filePath) {
            try {
                Remove-Item -Path $filePath -Force
                $removed++
            } catch {
                Write-Host "      [WARN] Could not remove: $file" -ForegroundColor Yellow
            }
        }
    }
    
    Write-Host "      [OK] Removed $removed file(s)" -ForegroundColor Green
    
    # Check if directory is empty
    $remainingFiles = Get-ChildItem -Path $InstallDir -File
    if ($remainingFiles.Count -eq 0) {
        Write-Host "      [INFO] Installation directory is now empty" -ForegroundColor Cyan
        Write-Host "      You can safely delete: $InstallDir" -ForegroundColor Cyan
    } else {
        Write-Host "      [INFO] $($remainingFiles.Count) file(s) remain in: $InstallDir" -ForegroundColor Cyan
    }
}

# Main uninstallation process
try {
    Stop-MouseMacros
    Remove-RegistryEntries
    Remove-Shortcuts
    Remove-Configuration
    Remove-ApplicationFiles
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "   Uninstallation Complete!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Thank you for using MouseMacros!" -ForegroundColor Cyan
    Write-Host ""
    
    if (-not $Silent) {
        Write-Host "Press any key to exit..."
        $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
    }
    
} catch {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "   Uninstallation Failed" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Error: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please try manual uninstallation:" -ForegroundColor Yellow
    Write-Host "1. Delete installation folder: $InstallDir" -ForegroundColor Yellow
    Write-Host "2. Delete configuration folder: $ConfigDir" -ForegroundColor Yellow
    Write-Host "3. Remove registry key: HKCU:\Software\Microsoft\Windows\CurrentVersion\Uninstall\MouseMacros" -ForegroundColor Yellow
    Write-Host ""
    
    exit 1
}