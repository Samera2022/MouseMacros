param([Parameter(Mandatory=$true)][string]$IconPath, [Parameter(Mandatory=$true)][string]$OutputDirectory)
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Drawing
$icon = [System.Drawing.Icon]::new($IconPath, 256, 256)
try {
    $source = $icon.ToBitmap()
    try {
        foreach ($asset in @(@('StoreLogo',50), @('Square44x44Logo',44), @('Square150x150Logo',150))) {
            $bitmap = [System.Drawing.Bitmap]::new([int]$asset[1], [int]$asset[1])
            try {
                $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
                try {
                    $graphics.Clear([System.Drawing.Color]::Transparent)
                    $graphics.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
                    $graphics.DrawImage($source, 0, 0, $bitmap.Width, $bitmap.Height)
                } finally { $graphics.Dispose() }
                $bitmap.Save((Join-Path $OutputDirectory ($asset[0] + '.png')), [System.Drawing.Imaging.ImageFormat]::Png)
            } finally { $bitmap.Dispose() }
        }
    } finally { $source.Dispose() }
} finally { $icon.Dispose() }
