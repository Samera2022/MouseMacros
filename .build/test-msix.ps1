# Linux fixture checks. jpackage/MakeAppx and Windows image generation are mocked.
# Real SDK validation is covered by .github/workflows/msix.yml on Windows.
$ErrorActionPreference = 'Stop'
$fixture = Join-Path ([IO.Path]::GetTempPath()) ('MouseMacros-msix-' + [guid]::NewGuid())
$repo = Split-Path $PSScriptRoot -Parent
function Assert($condition, $message) { if (-not $condition) { throw $message } }
try {
    foreach ($dir in @('.build/msix','target','custom-jre','output')) {
        New-Item -ItemType Directory -Path (Join-Path $fixture $dir) -Force | Out-Null
    }
    Copy-Item (Join-Path $PSScriptRoot 'msix/AppxManifest.xml') (Join-Path $fixture '.build/msix')
    @'
param($IconPath,$OutputDirectory)
foreach ($name in @('StoreLogo','Square44x44Logo','Square150x150Logo')) { Set-Content (Join-Path $OutputDirectory "$name.png") 'mock PNG' }
'@ | Set-Content (Join-Path $fixture '.build/msix/New-Assets.ps1')
    Set-Content (Join-Path $fixture 'target/MouseMacros.jar') 'current application'
    # Contamination outside the fresh image must never enter the package.
    New-Item -ItemType Directory -Path (Join-Path $fixture 'target/dist') -Force | Out-Null
    Set-Content (Join-Path $fixture 'target/dist/QuarkUpdater.exe') 'unrelated'
    function jpackage {
        param([Parameter(ValueFromRemainingArguments=$true)]$Arguments)
        $dest = $Arguments[[array]::IndexOf($Arguments,'--dest')+1]
        $inputPath = $Arguments[[array]::IndexOf($Arguments,'--input')+1]
        Assert (@(Get-ChildItem $inputPath).Count -eq 1) 'Contaminated jpackage input'
        $image = Join-Path $dest 'MouseMacros'
        New-Item -ItemType Directory -Path (Join-Path $image 'app') -Force | Out-Null
        New-Item -ItemType Directory -Path (Join-Path $image 'runtime/bin') -Force | Out-Null
        foreach ($name in @('MouseMacros.exe','app/MouseMacros.cfg','app/MouseMacros.jar','runtime/bin/java.dll')) { Set-Content (Join-Path $image $name) 'mock image' }
        Set-Content (Join-Path $image 'unrelated.log') 'must not be copied'
        $global:LASTEXITCODE = 0
    }
    $makeappx = Join-Path $fixture 'makeappx.ps1'
    @'
param([Parameter(ValueFromRemainingArguments=$true)]$Arguments)
$stage = $Arguments[[array]::IndexOf($Arguments,'/d')+1]
$output = $Arguments[[array]::IndexOf($Arguments,'/p')+1]
[IO.Compression.ZipFile]::CreateFromDirectory($stage,$output)
$global:LASTEXITCODE = 0
'@ | Set-Content $makeappx
    $env:APP_SERIAL_VERSION = '2.0.1.2'
    & (Join-Path $PSScriptRoot 'build-msix.ps1') -ProjectRoot $fixture -DisplayVersion '2.0.1-beta' -MakeAppxPath $makeappx
    $stage = Join-Path $fixture 'target/msix/package'
    [xml]$manifest = Get-Content (Join-Path $stage 'AppxManifest.xml')
    Assert ($manifest.Package.Identity.Name -eq 'Samera2022.112369DBD06A7') 'Store identity changed'
    Assert ($manifest.Package.Identity.Publisher -eq 'CN=838CC098-B19F-41F3-950A-6BAE1D71D1E5') 'Store publisher changed'
    Assert ($manifest.Package.Identity.Version -eq '2.0.1.0') 'Store version not normalized'
    Assert ($manifest.Package.Applications.Application.Id -eq 'MOUSEMACROS') 'Application identity changed'
    Assert (-not $manifest.Package.Dependencies.PackageDependency) 'Unexpected Windows App Runtime dependency'
    Assert (Test-Path (Join-Path $stage 'VFS/ProgramFilesX64/MouseMacros/runtime/bin/java.dll')) 'Runtime missing'
    Assert (-not (Get-ChildItem $stage -Recurse | Where-Object Name -match 'Quark|unrelated|Registry.dat|User.dat')) 'Contamination found'
    Assert (Test-Path (Join-Path $fixture 'output/MouseMacros-2.0.1-beta.msix')) 'MSIX not emitted'
    # Rebuilding must replace staging rather than include previous user/cache data.
    Set-Content (Join-Path $stage 'old-cache.json') 'stale'
    & (Join-Path $PSScriptRoot 'build-msix.ps1') -ProjectRoot $fixture -Version '2.0.2.0' -MakeAppxPath $makeappx
    Assert (-not (Test-Path (Join-Path $stage 'old-cache.json'))) 'Stale package data retained'
    foreach ($invalid in @('0.1.0.0','2.0.0.1','2.0.0','65536.0.0.0','2.0.0-beta')) {
        $failed = $false
        try { & (Join-Path $PSScriptRoot 'build-msix.ps1') -ProjectRoot $fixture -Version $invalid -MakeAppxPath $makeappx } catch { $failed = $true }
        Assert $failed "Accepted invalid version $invalid"
    }
    # SDK rejection must propagate, without publishing a success artifact.
    @'
param([Parameter(ValueFromRemainingArguments=$true)]$Arguments)
$global:LASTEXITCODE = 42
'@ | Set-Content $makeappx
    $failed = $false
    try {
        & (Join-Path $PSScriptRoot 'build-msix.ps1') -ProjectRoot $fixture -Version '2.0.3.0' -DisplayVersion 'rejected' -MakeAppxPath $makeappx
    } catch { $failed = $true }
    Assert $failed 'MakeAppx rejection was ignored'
    Assert (-not (Test-Path (Join-Path $fixture 'output/MouseMacros-rejected.msix'))) 'Rejected package published'
    Write-Host 'PASS: Store identity, version rules, fresh allowlisted staging, runtime, no unrelated dependency, repeatable rebuilds and invalid-version rejection.'
} finally { if (Test-Path $fixture) { Remove-Item -LiteralPath $fixture -Recurse -Force } }
