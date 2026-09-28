# MouseMacros Installer
# Sets up registry entries and optional shortcuts

param(
    [switch]$CreateShortcuts = $true,
    [switch]$Silent = $false
)

$ErrorActionPreference = "Stop"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   MouseMacros Installer v2.0.0" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$InstallDir = $PSScriptRoot

# Verify JAR exists
if (-not (Test-Path "$InstallDir\MouseMacros.jar")) {
    Write-Host "[ERROR] MouseMacros.jar not found!" -ForegroundColor Red
    exit 1
}

# Register in Add/Remove Programs
function Register-Application {
    Write-Host "[1/3] Registering application..." -ForegroundColor Yellow
    
    $regPath = "HKCU:\Software\Microsoft\Windows\CurrentVersion\Uninstall\MouseMacros"
    
    try {
        New-Item -Path $regPath -Force | Out-Null
        
        New-ItemProperty -Path $regPath -Name "DisplayName" -Value "MouseMacros" -PropertyType String -Force | Out-Null
        New-ItemProperty -Path $regPath -Name "DisplayVersion" -Value "2.0.0" -PropertyType String -Force | Out-Null
        New-ItemProperty -Path $regPath -Name "Publisher" -Value "Samera2022" -PropertyType String -Force | Out-Null
        New-ItemProperty -Path $regPath -Name "InstallLocation" -Value $InstallDir -PropertyType String -Force | Out-Null
        New-ItemProperty -Path $regPath -Name "UninstallString" -Value "powershell.exe -ExecutionPolicy Bypass -File `"$InstallDir\scripts\uninstall.ps1`"" -PropertyType String -Force | Out-Null
        New-ItemProperty -Path $regPath -Name "DisplayIcon" -Value "$InstallDir\MouseMacros.jar,0" -PropertyType String -Force | Out-Null
        New-ItemProperty -Path $regPath -Name "NoModify" -Value 1 -PropertyType DWord -Force | Out-Null
        New-ItemProperty -Path $regPath -Name "NoRepair" -Value 1 -PropertyType DWord -Force | Out-Null
        
        Write-Host "      [OK] Registered in Add/Remove Programs" -ForegroundColor Green
    } catch {
        Write-Host "      [WARN] Could not register application: $_" -ForegroundColor Yellow
    }
}

# Create shortcuts
function Create-Shortcuts {
    if (-not $CreateShortcuts) {
        Write-Host "[2/3] Skipping shortcuts (disabled)" -ForegroundColor Gray
        return
    }
    
    Write-Host "[2/3] Creating shortcuts..." -ForegroundColor Yellow
    
    $WScriptShell = New-Object -ComObject WScript.Shell
    
    # Start Menu shortcut
    $startMenuPath = Join-Path $env:APPDATA "Microsoft\Windows\Start Menu\Programs"
    $startMenuShortcut = Join-Path $startMenuPath "MouseMacros.lnk"
    
    try {
        $shortcut = $WScriptShell.CreateShortcut($startMenuShortcut)
        $shortcut.TargetPath = "javaw.exe"
        $shortcut.Arguments = "-jar `"$InstallDir\MouseMacros.jar`""
        $shortcut.WorkingDirectory = $InstallDir
        $shortcut.Description = "MouseMacros - Keyboard and Mouse Macro Recorder"
        $shortcut.Save()
        Write-Host "      [OK] Start Menu shortcut created" -ForegroundColor Green
    } catch {
        Write-Host "      [WARN] Could not create Start Menu shortcut: $_" -ForegroundColor Yellow
    }
}

# Create config directory
function Initialize-Configuration {
    Write-Host "[3/3] Initializing configuration..." -ForegroundColor Yellow
    
    $configDir = Join-Path $env:USERPROFILE ".mousemacros"
    
    try {
        if (-not (Test-Path $configDir)) {
            New-Item -Path $configDir -ItemType Directory -Force | Out-Null
            Write-Host "      [OK] Configuration directory created" -ForegroundColor Green
        } else {
            Write-Host "      [OK] Configuration directory exists" -ForegroundColor Green
        }
    } catch {
        Write-Host "      [WARN] Could not create configuration directory: $_" -ForegroundColor Yellow
    }
}

# Main installation
try {
    Register-Application
    Create-Shortcuts
    Initialize-Configuration
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "   Installation Complete!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Launch MouseMacros:" -ForegroundColor Cyan
    Write-Host "  - From Start Menu: Search 'MouseMacros'" -ForegroundColor White
    Write-Host "  - Or run: java -jar `"$InstallDir\MouseMacros.jar`"" -ForegroundColor White
    Write-Host ""
    Write-Host "To uninstall:" -ForegroundColor Cyan
    Write-Host "  - Windows Settings > Apps > MouseMacros > Uninstall" -ForegroundColor White
    Write-Host "  - Or run: .\scripts\uninstall.ps1" -ForegroundColor White
    Write-Host ""
    
    if (-not $Silent) {
        Write-Host "Press any key to exit..."
        $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
    }
    
} catch {
    Write-Host ""
    Write-Host "[ERROR] Installation failed: $_" -ForegroundColor Red
    exit 1
}