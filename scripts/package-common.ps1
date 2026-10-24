# Shared paths for the optional per-user Windows ZIP/JAR registration scripts.
function Get-MouseMacrosLayout {
    param([Parameter(Mandatory=$true)][string]$ScriptDirectory)
    foreach ($candidate in @((Split-Path -Parent $ScriptDirectory), $ScriptDirectory)) {
        $root = [IO.Path]::GetFullPath($candidate)
        $jar = Join-Path $root 'MouseMacros.jar'
        $imageJar = Join-Path $root 'app/MouseMacros.jar'
        if (Test-Path -LiteralPath $jar -PathType Leaf) {
            return [pscustomobject]@{ Root = $root; Jar = $jar; IsAppImage = $false }
        }
        if ((Test-Path -LiteralPath $imageJar -PathType Leaf) -and
            (Test-Path -LiteralPath (Join-Path $root 'MouseMacros.exe') -PathType Leaf) -and
            (Test-Path -LiteralPath (Join-Path $root 'app/.jpackage.xml') -PathType Leaf)) {
            return [pscustomobject]@{ Root = $root; Jar = $imageJar; IsAppImage = $true }
        }
    }
    throw 'MouseMacros installation not found. Keep the scripts folder inside the extracted application folder.'
}

function Get-MouseMacrosConfigDirectories {
    if (-not $env:LOCALAPPDATA -or -not $env:USERPROFILE) {
        throw 'LOCALAPPDATA and USERPROFILE must be available.'
    }
    # Current application data path, followed by the older installer-created path.
    Join-Path $env:LOCALAPPDATA 'MouseMacros'
    Join-Path $env:USERPROFILE '.mousemacros'
}
