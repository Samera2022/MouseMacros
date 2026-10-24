# Linux PowerShell fixture test: Windows registry and process APIs are mocked.
# Does not prove Windows MSI installation; use a disposable Windows VM for integration.
$ErrorActionPreference = 'Stop'
if ($IsWindows) { throw 'This mock harness requires Linux. Use a disposable Windows VM for real installation tests.' }
$source = $PSScriptRoot
$originalDirectory = Get-Location
$fixture = Join-Path ([IO.Path]::GetTempPath()) ('MouseMacros-tests-' + [guid]::NewGuid())
New-Item -Path $fixture -ItemType Directory | Out-Null
New-Item -Path (Join-Path $fixture 'registry') -ItemType Directory | Out-Null
New-PSDrive -Name HKCU -PSProvider FileSystem -Root (Join-Path $fixture 'registry') | Out-Null
$env:LOCALAPPDATA = Join-Path $fixture 'LocalAppData'
$env:APPDATA = Join-Path $fixture 'Roaming'
$env:USERPROFILE = Join-Path $fixture 'User'
$global:registryWrites = @{}
$global:mockStopped = @()
$global:mockProcesses = @()
function Assert($condition, $message) { if (-not $condition) { throw $message } }
function New-Item {
    [CmdletBinding()]param($Path, $ItemType, [switch]$Force)
    if ($Path -like 'HKCU:*') { $ItemType = 'Directory' }
    Microsoft.PowerShell.Management\New-Item -Path $Path -ItemType $ItemType -Force:$Force
}
function New-ItemProperty { param($Path,$Name,$Value,$PropertyType,[switch]$Force) $global:registryWrites[$Name] = $Value }
function Get-CimInstance { param($ClassName,$Filter) $global:mockProcesses }
function Stop-Process { [CmdletBinding()]param($Id) $global:mockStopped += $Id }
function Wait-Process { [CmdletBinding()]param($Id,$Timeout) }
function Get-Process { [CmdletBinding()]param($Id) }
function New-Fixture($name, [bool]$appImage) {
    $root = Join-Path $fixture $name
    New-Item -Path (Join-Path $root 'scripts') -ItemType Directory -Force | Out-Null
    foreach ($file in @('install.ps1','uninstall.ps1','package-common.ps1')) { Copy-Item (Join-Path $source $file) (Join-Path $root 'scripts') }
    if ($appImage) {
        New-Item -Path (Join-Path $root 'app') -ItemType Directory | Out-Null
        New-Item -Path (Join-Path $root 'runtime/bin') -ItemType Directory -Force | Out-Null
        foreach ($file in @('MouseMacros.exe','app/MouseMacros.jar','app/MouseMacros.cfg','app/.jpackage.xml','runtime/bin/jvm.dll')) {
            Set-Content -LiteralPath (Join-Path $root $file) 'fixture'
        }
    } else { Set-Content -LiteralPath (Join-Path $root 'MouseMacros.jar') 'fixture' }
    Set-Content -LiteralPath (Join-Path $root 'personal.mmc') 'user macro'
    return $root
}
try {
    foreach ($appImage in @($false,$true)) {
        $root = New-Fixture "layout-$appImage" $appImage
        $install = Join-Path $root 'scripts/install.ps1'
        & $install -CreateShortcuts:$false -Silent
        Assert ($global:registryWrites.InstallLocation -eq $root) 'Wrong installation root'
        Assert ($global:registryWrites.UninstallString.Contains((Join-Path $root 'scripts/uninstall.ps1'))) 'Broken uninstall registry command'
        Assert (Test-Path (Join-Path $env:LOCALAPPDATA 'MouseMacros')) 'Wrong configuration directory'
        $jar = if ($appImage) { Join-Path $root 'app/MouseMacros.jar' } else { Join-Path $root 'MouseMacros.jar' }
        $global:mockProcesses = @(
            [pscustomobject]@{Name='javaw.exe';ExecutablePath='javaw.exe';CommandLine="javaw.exe -jar `"$jar`"";ProcessId=101},
            [pscustomobject]@{Name='java.exe';ExecutablePath='java.exe';CommandLine='java.exe -jar other.jar';ProcessId=102},
            [pscustomobject]@{Name='java.exe';ExecutablePath='java.exe';CommandLine="java.exe -jar `"$jar.other`"";ProcessId=103}
        )
        if ($appImage) { $global:mockProcesses += [pscustomobject]@{Name='MouseMacros.exe';ExecutablePath=(Join-Path $root 'MouseMacros.exe');CommandLine='';ProcessId=104} }
        $global:mockStopped = @()
        & (Join-Path $root 'scripts/uninstall.ps1') -Silent
        Assert (101 -in $global:mockStopped) 'Did not stop this installation javaw process'
        Assert (102 -notin $global:mockStopped -and 103 -notin $global:mockStopped) 'Stopped an unrelated Java process'
        if ($appImage) { Assert (104 -in $global:mockStopped) 'Did not stop packaged launcher'; Assert (-not (Test-Path (Join-Path $root 'runtime'))) 'Runtime left behind' }
        Assert (-not (Test-Path $jar)) 'Application JAR left behind'
        Assert (Test-Path (Join-Path $root 'personal.mmc')) 'Deleted user macro'
        Assert (Test-Path (Join-Path $env:LOCALAPPDATA 'MouseMacros')) 'Default uninstall removed configuration'
        Assert (-not (Test-Path 'HKCU:\Software\Microsoft\Windows\CurrentVersion\Uninstall\MouseMacros')) 'Registry entry left behind'
    }
    $root = New-Fixture 'remove-config' $false
    New-Item -Path (Join-Path $env:USERPROFILE '.mousemacros') -ItemType Directory -Force | Out-Null
    $global:mockProcesses = @()
    & (Join-Path $root 'scripts/uninstall.ps1') -RemoveConfig -Silent
    Assert (-not (Test-Path (Join-Path $env:LOCALAPPDATA 'MouseMacros'))) 'Current config not removed with explicit flag'
    Assert (-not (Test-Path (Join-Path $env:USERPROFILE '.mousemacros'))) 'Legacy config not removed with explicit flag'
    # A locked application file must fail, and must not unregister the app.
    $root = New-Fixture 'locked-file' $false
    New-Item -Path 'HKCU:\Software\Microsoft\Windows\CurrentVersion\Uninstall\MouseMacros' -Force | Out-Null
    $child = Join-Path $fixture 'locked-test.ps1'
    @'
param($Uninstaller,$RegistryRoot)
New-PSDrive -Name HKCU -PSProvider FileSystem -Root $RegistryRoot | Out-Null
function Get-CimInstance { param($ClassName,$Filter) }
function Remove-Item {
    [CmdletBinding()]param($LiteralPath,$Path,[switch]$Force,[switch]$Recurse)
    if ($LiteralPath -like '*MouseMacros.jar') { throw 'Simulated locked file' }
    Microsoft.PowerShell.Management\Remove-Item @PSBoundParameters
}
& $Uninstaller -Silent
'@ | Set-Content -LiteralPath $child
    $PSNativeCommandUseErrorActionPreference = $false
    & (Join-Path $PSHOME 'pwsh') -NoProfile -File $child (Join-Path $root 'scripts/uninstall.ps1') (Join-Path $fixture 'registry') > $null 2>&1
    Assert ($LASTEXITCODE -ne 0) 'Locked file incorrectly reported successful uninstall'
    Assert (Test-Path (Join-Path $root 'MouseMacros.jar')) 'Failure test removed application file'
    Assert (Test-Path 'HKCU:\Software\Microsoft\Windows\CurrentVersion\Uninstall\MouseMacros') 'Failure test removed uninstall registration'
    # Exercise actual ZIP staging/compression; mock only the Windows jpackage executable.
    $project = Join-Path $fixture 'package-build'
    foreach ($dir in @('.build', 'target', 'custom-jre', 'scripts', 'docs')) {
        New-Item -Path (Join-Path $project $dir) -ItemType Directory -Force | Out-Null
    }
    Copy-Item (Join-Path (Split-Path $source -Parent) '.build/properties.json') (Join-Path $project '.build')
    Copy-Item (Join-Path (Split-Path $source -Parent) 'docs/MANUAL_UNINSTALL.md') (Join-Path $project 'docs')
    foreach ($file in @('install.ps1','uninstall.ps1','package-common.ps1')) { Copy-Item (Join-Path $source $file) (Join-Path $project 'scripts') }
    Set-Content (Join-Path $project 'target/MouseMacros.jar') 'fixture'
    function jpackage {
        param([Parameter(ValueFromRemainingArguments=$true)]$Arguments)
        $dest = $Arguments[[array]::IndexOf($Arguments, '--dest') + 1]
        $root = Join-Path $dest 'MouseMacros'
        New-Item -Path (Join-Path $root 'app') -ItemType Directory -Force | Out-Null
        New-Item -Path (Join-Path $root 'runtime') -ItemType Directory -Force | Out-Null
        foreach ($file in @('MouseMacros.exe','app/MouseMacros.jar','app/MouseMacros.cfg','app/.jpackage.xml','runtime/release')) {
            Set-Content -LiteralPath (Join-Path $root $file) 'fixture'
        }
        $global:LASTEXITCODE = 0
    }
    $env:GITHUB_WORKSPACE = $project
    $env:APP_DISPLAY_VERSION = '2.0.0'
    $env:APP_SERIAL_VERSION = '2.0.0'
    & (Join-Path (Split-Path $source -Parent) '.build/build.ps1') -Type zip
    $extract = Join-Path $fixture 'unpacked'
    [IO.Compression.ZipFile]::ExtractToDirectory((Join-Path $project 'output/MouseMacros-2.0.0.zip'), $extract)
    foreach ($file in @('scripts/install.ps1','scripts/uninstall.ps1','scripts/package-common.ps1','UNINSTALL.md','app/.jpackage.xml')) {
        Assert (Test-Path -LiteralPath (Join-Path $extract "MouseMacros/$file")) "ZIP missing $file"
    }
    Write-Host 'PASS: ZIP includes the installer, uninstaller, shared paths, manual, and application metadata.'
    Write-Host 'PASS: JAR/app-image registration, uninstall paths, process isolation, runtime cleanup, and config preservation/removal.'
} finally {
    Set-Location $originalDirectory
    Remove-PSDrive HKCU
    Remove-Item -LiteralPath $fixture -Recurse -Force
}
