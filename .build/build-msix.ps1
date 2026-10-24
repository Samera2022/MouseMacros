param(
    [string]$ProjectRoot = (Split-Path -Parent $PSScriptRoot),
    [string]$Version = $env:APP_MSIX_VERSION,
    [string]$DisplayVersion = $env:APP_DISPLAY_VERSION,
    [string]$MakeAppxPath
)
$ErrorActionPreference = 'Stop'
$PSNativeCommandUseErrorActionPreference = $true
$ProjectRoot = (Resolve-Path $ProjectRoot).Path
if (-not $Version) {
    # Store reserves the fourth component. Prerelease suffixes are not Store versions.
    if (-not $env:APP_SERIAL_VERSION) { throw 'Set APP_MSIX_VERSION or APP_SERIAL_VERSION before building.' }
    $parts = $env:APP_SERIAL_VERSION.Split('.')
    if ($parts.Count -lt 3 -or $parts.Count -gt 4) { throw 'APP_SERIAL_VERSION must have three or four numeric components.' }
    $Version = ($parts[0..2] -join '.') + '.0'
}
if ($Version -notmatch '^\d+\.\d+\.\d+\.0$') { throw 'MSIX version must be numeric Major.Minor.Build.0.' }
$numbers = @($Version.Split('.') | ForEach-Object { [int]$_ })
if ($numbers[0] -lt 1 -or @($numbers | Where-Object { $_ -gt 65535 }).Count) { throw 'Invalid MSIX version components (major > 0, each <= 65535).' }
if (-not $DisplayVersion) { $DisplayVersion = $Version }
if ($DisplayVersion -match '[/\\]' -or $DisplayVersion -in @('.', '..')) { throw 'Invalid display version for output filename.' }

if (-not $MakeAppxPath) {
    $sdkRoot = Join-Path ${env:ProgramFiles(x86)} 'Windows Kits/10/bin'
    $sdkTools = @(Get-ChildItem "$sdkRoot/*/x64/makeappx.exe" -ErrorAction SilentlyContinue |
        Sort-Object { [version]$_.Directory.Parent.Name } -Descending)
    if (-not $sdkTools.Count) { throw 'Windows SDK x64 MakeAppx.exe not found. Install the Windows SDK or pass -MakeAppxPath.' }
    $MakeAppxPath = $sdkTools[0].FullName
}
if (-not (Test-Path -LiteralPath $MakeAppxPath -PathType Leaf)) { throw "MakeAppx not found: $MakeAppxPath" }
$jar = Join-Path $ProjectRoot 'target/MouseMacros.jar'
$runtime = Join-Path $ProjectRoot 'custom-jre'
if (-not (Test-Path $jar -PathType Leaf) -or -not (Test-Path $runtime -PathType Container)) {
    throw 'Build target/MouseMacros.jar and custom-jre first (use .build/build.ps1 -Type msix).'
}

$work = Join-Path $ProjectRoot 'target/msix'
if (Test-Path $work) { Remove-Item -LiteralPath $work -Recurse -Force }
$inputDir = New-Item -ItemType Directory -Path (Join-Path $work 'input') -Force
Copy-Item -LiteralPath $jar -Destination $inputDir
$imageDir = Join-Path $work 'image'
& jpackage --type app-image --name MouseMacros --app-version ($numbers[0..2] -join '.') `
    --vendor Samera2022 --description 'Mouse and keyboard macro recorder' `
    --icon (Join-Path $ProjectRoot '.build/MouseMacros.ico') --input $inputDir.FullName `
    --main-jar MouseMacros.jar --runtime-image $runtime --dest $imageDir
if ($LASTEXITCODE -ne 0) { throw "jpackage failed: $LASTEXITCODE" }

# Stage only freshly generated program files. Never capture the build machine.
$stage = New-Item -ItemType Directory -Path (Join-Path $work 'package') -Force
$app = New-Item -ItemType Directory -Path (Join-Path $stage 'VFS/ProgramFilesX64/MouseMacros') -Force
foreach ($entry in @('MouseMacros.exe', 'app', 'runtime')) {
    $path = Join-Path $imageDir "MouseMacros/$entry"
    if (-not (Test-Path -LiteralPath $path)) { throw "Incomplete jpackage image: $entry" }
    Copy-Item -LiteralPath $path -Destination $app -Recurse -Force
}
$assets = New-Item -ItemType Directory -Path (Join-Path $stage 'Assets') -Force
& (Join-Path $ProjectRoot '.build/msix/New-Assets.ps1') -IconPath (Join-Path $ProjectRoot '.build/MouseMacros.ico') -OutputDirectory $assets.FullName
[xml]$manifest = Get-Content (Join-Path $ProjectRoot '.build/msix/AppxManifest.xml') -Raw
$manifest.Package.Identity.Version = $Version
$manifest.Save((Join-Path $stage 'AppxManifest.xml'))
$packed = Join-Path $work 'MouseMacros.msix'
& $MakeAppxPath pack /d $stage.FullName /p $packed /o
if ($LASTEXITCODE -ne 0 -or -not (Test-Path $packed -PathType Leaf)) { throw 'MakeAppx validation/packing failed.' }
$outputDir = New-Item -ItemType Directory -Path (Join-Path $ProjectRoot 'output') -Force
$output = Join-Path $outputDir "MouseMacros-$DisplayVersion.msix"
Copy-Item -LiteralPath $packed -Destination $output -Force
Write-Host "Store submission package (unsigned): $output; identity version: $Version"
