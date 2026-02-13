param (
    [Parameter(Mandatory=$true)]
    [string]$NewVersion
)

$pomFiles = @(
    "../pom.xml",
    "../mouse-macros-api/pom.xml",
    "../mouse-macros-app/pom.xml"
)

foreach ($pomFile in $pomFiles) {
    $fullPath = Join-Path $PSScriptRoot $pomFile
    if (Test-Path $fullPath) {
        $content = Get-Content $fullPath
        if ($content.Count -ge 9) {
            # Line 9 is index 8
            $lineIndex = 8
            $currentLine = $content[$lineIndex]

            # Check if line 9 contains <version> tag (ignoring whitespace)
            if ($currentLine.Trim() -match "^<version>.*</version>$") {
                # Capture indentation
                $indentation = ""
                if ($currentLine -match "^(\s+)") {
                    $indentation = $matches[1]
                }

                # Construct the new line completely
                $newLine = "${indentation}<version>${NewVersion}</version>"

                # Replace the line in the array
                $content[$lineIndex] = $newLine

                # Write back to file
                $content | Set-Content $fullPath -Encoding UTF8
                Write-Host "Updated $fullPath to version $NewVersion" -ForegroundColor Green
            } else {
                Write-Host "Warning: Line 9 in $fullPath does not appear to be a version tag. Content: '$currentLine'" -ForegroundColor Yellow
            }
        } else {
            Write-Host "File $fullPath has fewer than 9 lines." -ForegroundColor Red
        }
    } else {
        Write-Host "File $fullPath not found." -ForegroundColor Red
    }
}
