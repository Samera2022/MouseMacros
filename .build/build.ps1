Param(
    [Parameter(Mandatory=$true)]
    [ValidateSet("msi", "exe", "jar", "zip")]
    [string]$Type
)

$PROJECT_ROOT = $env:GITHUB_WORKSPACE
if ($null -eq $PROJECT_ROOT) { $PROJECT_ROOT = Get-Location }
Set-Location $PROJECT_ROOT

$Version    = $env:APP_DISPLAY_VERSION
$SerVersion = $env:APP_SERIAL_VERSION

$props = Get-Content ".build/properties.json" | ConvertFrom-Json
$BaseName    = $props.name
$Vendor      = $props.vendor
$Description = $props.description
$Copyright   = $props.copyright

$FullName = "$BaseName-$Version"
$OUT_DIR = New-Item -ItemType Directory -Path "$PROJECT_ROOT\output" -Force
$IconPath = "$PROJECT_ROOT\.build\${BaseName}.ico"

$MAVEN_JAR = "$PROJECT_ROOT\target\${BaseName}.jar"
$CLEAN_INPUT = New-Item -ItemType Directory -Path "$PROJECT_ROOT\target\dist" -Force
if (Test-Path $MAVEN_JAR) {
    Copy-Item $MAVEN_JAR $CLEAN_INPUT
}

if (-not (Test-Path "custom-jre")) {
    Write-Host "[BUILD] Detecting Java Runtime version..." -ForegroundColor Cyan
    $javaSpecVersion = & java -XshowSettings:properties -version 2>&1 |
            Select-String "java.specification.version =" |
            ForEach-Object { $_.ToString().Split('=')[-1].Trim() }
    $majorVersion = if ($javaSpecVersion.StartsWith("1.")) { $javaSpecVersion.Split(".")[1] } else { $javaSpecVersion }

    Write-Host "[BUILD] Detected Java Major Version: $majorVersion" -ForegroundColor Green

    if (-not (Test-Path $MAVEN_JAR)) {
        Write-Host "[ERROR] Maven JAR not found at $MAVEN_JAR! Please run 'mvn package' first." -ForegroundColor Red
        exit 1
    }

    Write-Host "[BUILD] Analyzing dependencies with jdeps..." -ForegroundColor Cyan
    $detectedModules = jdeps --ignore-missing-deps --multi-release $majorVersion --print-module-deps $MAVEN_JAR
    if ([string]::IsNullOrWhiteSpace($detectedModules)) {
        $detectedModules = "java.base,java.desktop"
    } else {
        if ($detectedModules -notlike "*java.desktop*") { $detectedModules = "$detectedModules,java.desktop" }
    }

    Write-Host "[BUILD] Generating custom-jre via jlink..." -ForegroundColor Cyan
    jlink --module-path "$env:JAVA_HOME\jmods" --add-modules $detectedModules --output custom-jre --strip-debug --compress 2 --no-header-files --no-man-pages
}

switch ($Type) {

    "jar" {
        Copy-Item $MAVEN_JAR "$OUT_DIR\$FullName.jar"
    }

    "zip" {
        $ZIP_TEMP = "output/temp_zip"
        jpackage --type app-image --name $BaseName --app-version $SerVersion `
                 --vendor $Vendor --description $Description --icon "$IconPath" `
                 --input "$CLEAN_INPUT" --main-jar "${BaseName}.jar" `
                 --runtime-image "custom-jre" --dest "$ZIP_TEMP"

        $icoInZip = "$ZIP_TEMP/$BaseName/$BaseName.ico"
        if (Test-Path $icoInZip) { Remove-Item $icoInZip -Force }

        Compress-Archive -Path "$ZIP_TEMP/$BaseName" -DestinationPath "$OUT_DIR\$FullName.zip" -Force
    }

    "exe" {
        Write-Host "[EXE] Step 1: Generating app-image..." -ForegroundColor Cyan
        $EXE_TEMP = "output/temp_exe"
        jpackage --type app-image --name $BaseName --app-version $SerVersion `
                 --vendor $Vendor --description $Description --copyright $Copyright --icon "$IconPath" `
                 --input "$CLEAN_INPUT" --main-jar "${BaseName}.jar" `
                 --runtime-image "custom-jre" --dest "$EXE_TEMP"

        $IMAGE_ROOT = "$PROJECT_ROOT\$EXE_TEMP\$BaseName"
        $APP_DIR = "$IMAGE_ROOT\app"
        $newCfgName = "$FullName.cfg"
        $newCfgPath = "$APP_DIR\$newCfgName"
        $EVB_TEMPLATE = "$PROJECT_ROOT\.build\evb_settings.evb"
        $TEMP_EVB = "$PROJECT_ROOT\.build\temp_build.evb"
        $EVB_CONSOLE = "C:\Program Files (x86)\Enigma Virtual Box\enigmavbconsole.exe"

        if (Test-Path "$APP_DIR\${BaseName}.cfg") {
            Move-Item -Path "$APP_DIR\${BaseName}.cfg" -Destination $newCfgPath -Force
        }

        Copy-Item "$APP_DIR\.jpackage.xml" "$IMAGE_ROOT\.jpackage.xml" -Force
        Copy-Item "$APP_DIR\${BaseName}.jar" "$IMAGE_ROOT\${BaseName}.jar" -Force
        Copy-Item "$newCfgPath" "$IMAGE_ROOT\$newCfgName" -Force

        if (Test-Path $EVB_TEMPLATE) {
            $evbContent = Get-Content $EVB_TEMPLATE -Raw
            $finalExePath = "$OUT_DIR\$FullName.exe"
            $evbContent = $evbContent -replace '<OutputFile>.*?</OutputFile>', "<OutputFile>$finalExePath</OutputFile>"
            $evbContent = $evbContent -replace "$BaseName.cfg", "$newCfgName"
            $evbContent | Set-Content $TEMP_EVB -Encoding UTF8

            if (Test-Path $EVB_CONSOLE) {
                Push-Location "$PROJECT_ROOT\.build"
                & $EVB_CONSOLE "temp_build.evb"
                Pop-Location
            }
            Remove-Item $TEMP_EVB -ErrorAction SilentlyContinue
        }
    }

    "msi" {
        jpackage --type msi --name $BaseName --app-version $SerVersion `
                 --vendor $Vendor --description $Description --icon "$IconPath" `
                 --input "$CLEAN_INPUT" --main-jar "${BaseName}.jar" `
                 --runtime-image "custom-jre" --dest "output/temp_msi" `
                 --win-dir-chooser --win-menu --win-shortcut `
                 --win-upgrade-uuid "$env:MSI_WIN_UPGRADE_UUID"

        $GenMsi = Get-ChildItem "output/temp_msi/*.msi" | Select-Object -First 1
        Move-Item $GenMsi.FullName "$OUT_DIR\$FullName.msi" -Force
    }
}