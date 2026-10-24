# Microsoft Store MSIX

The release workflow builds an **unsigned x64 Store submission package** as `output/MouseMacros-<tag>.msix`.
It does not upload to Partner Center or create a new Store submission automatically.

## Existing Store identity

These values come from the existing package and Partner Center. Keep them unchanged when updating the existing listing:

| Field | Value |
| --- | --- |
| Identity Name | `Samera2022.112369DBD06A7` |
| Publisher | `CN=838CC098-B19F-41F3-950A-6BAE1D71D1E5` |
| PublisherDisplayName | `Samera2022` |
| Application Id | `MOUSEMACROS` |
| Package family | `Samera2022.112369DBD06A7_3b0bajn5vqwd4` |
| Store product | `9MX3KFBKMN1H` |

The template is `.build/msix/AppxManifest.xml`. The existing desktop entry point is preserved:
`VFS\ProgramFilesX64\MouseMacros\MouseMacros.exe`, with `Windows.FullTrustApplication` and `runFullTrust`.
The declared minimum OS remains Windows 10 1809 (`10.0.17763.0`). This is a compatibility declaration, not proof of testing on every supported OS.

The old converted package declared Microsoft.WindowsAppRuntime.1.4. The current Java/Swing implementation does not use Windows App SDK, so that dependency is not added to the new template. Verify the newly built application on Windows before submission.

## Build locally on Windows

Prerequisites: PowerShell 7, x64 JDK 21 (including jpackage/jlink), Maven, and Windows SDK with x64 MakeAppx.exe. No Store credentials or signing certificate are required to build the unsigned package.

From the repository root:

```powershell
mvn -B -ntp clean package -DskipTests
$env:APP_DISPLAY_VERSION = '2.0.1'
$env:APP_SERIAL_VERSION = '2.0.1'
./.build/build.ps1 -Type msix
```

This creates a fresh jpackage app-image, includes Java, generates PNG icons from `.build/MouseMacros.ico`, writes the manifest, and runs MakeAppx with its normal validation enabled.
Only `MouseMacros.exe`, `app`, and `runtime` from that fresh image enter the application payload. The previous converted package, registry hives, machine configuration, shortcuts, and captured background-program files are not reused.

By default the first three numeric components of `APP_SERIAL_VERSION` become `Major.Minor.Build.0`. Microsoft Store reserves the fourth component; release/prerelease versions with the same first three components produce the same Store version. Bump the build number for a new Store update.
To override explicitly:

```powershell
$env:APP_MSIX_VERSION = '2.0.2.0'
```

Use a four-part version greater than all versions already submitted to Partner Center, not just the old 1.2.3.0 package. Each component must be <= 65535 and the major component must be positive.

The lower-level `.build/build-msix.ps1` accepts `-MakeAppxPath` for a nonstandard SDK installation, after the JAR and `custom-jre` have been prepared.
Delete `custom-jre` and rebuild it when changing the Java toolchain or Java module dependencies; a retained runtime is reused by the existing build script.

## CI

- `.github/workflows/release.yml`: tag releases build MSIX alongside JAR/ZIP/EXE/MSI and attach it to the GitHub release.
- `.github/workflows/msix.yml`: pull requests affecting packaging or application modules, and manual **Validate MSIX** runs, build an unsigned package on `windows-latest` with the real Windows SDK. The resulting package is available as a workflow artifact.
- `.build/test-msix.ps1`: fixture checks for identity, version rules, clean staging, invalid-version rejection and repeatable rebuilds. It mocks jpackage, Windows asset generation and MakeAppx; it is not SDK or installation validation.

## Store submission versus local installation

Upload the unsigned MSIX to the existing MouseMacros product in Partner Center. Store distribution signing is handled by Microsoft. Review any capability/certification requirements in the submission; building a package is not certification or publication.

An unsigned package cannot be installed normally for local testing. Sign a **copy** with a test certificate whose subject exactly matches the manifest Publisher, trust that certificate on the disposable Windows test machine, then install with `Add-AppxPackage`. Use the Windows SDK SignTool with `/fd SHA256`; keep private keys and passwords out of the repository. Never ask end users to trust a development certificate as a substitute for Store distribution.

Before submitting, test on Windows:

1. Clean install and first-launch wizard.
2. Recording, playback, global hotkeys, settings persistence and both themes.
3. Upgrade from the currently distributed Store build with settings/macros intact. First test any captured default settings from the old package; do not copy those old files into the new payload.
4. Launch from the Start Menu, close to tray, then uninstall via Windows Settings.
5. Check user data behavior under MSIX filesystem virtualization and optional user-data removal separately.

The MSIX edition uses Windows package deployment/uninstallation. It does not run the portable ZIP `install.ps1` or `uninstall.ps1`, and must not write into the installed package directory. No extra desktop shortcut is created; Windows registers the manifest application in Start.
