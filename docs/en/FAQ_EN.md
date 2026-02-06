# FAQ - Project Development & Maintenance Guide

<div align="center">

| <sub>EN</sub> [English](../en/FAQ_EN.md) | <sub>ZH</sub> [中文](../zh/FAQ_ZH_CN.md) |
|------------------------------------------|----------------------------------------|

</div>

This document explains the project's versioning rules, changelog maintenance, and CI/CD automation process for maintainers to quickly get started.

---

## 1. Versioning

### Q: What is the difference between Version and SerVersion?

This project distinguishes between **Display Version (Version)** and **Semantic Version (SerVersion)** to balance user clarity and Windows build requirements.

* **Version (V)**: Used for public display, Git Tag, and GitHub Release titles.
    * **Stable**: Three-part version, e.g., `1.2.0`.
    * **Test**: Format is `a.b.c-YYmMMx`.
        * `YY`: Last two digits of release year.
        * `MM`: Release month.
        * `x`: Test sequence for the expected stable version in the month (a, b, c...).
        * *Example*: `1.2.0-26m01a` means the first test version for 1.2.0 released in January 2026.
* **SerVersion (SV)**: Four-part version (`a.b.c.d`) for MSI, EXE, and system recognition.
    * **Stable**: Fourth part `d` is `0`.
    * **Test**: Fourth part `d` matches the test sequence in Version (a=1, b=2...).

---

## 2. Changelog

### Q: How is the changelog maintained?

The changelog is stored in `src/main/resources/updates.json`. This file is used both for in-app reading and as the data source for CI to generate Release Body.

* **Data Structure**: `Map<String, Map<String, String>>`
* **Format**: Key is `Version`, value is an object with `releaseDate` and `description`.

> **Note**: The CI/CD process strictly matches the pushed Tag name (Version) with this file. Ensure the key matches the Tag exactly.

**Example:**
```json
{
  "1.2.0-26m01a": {
    "releaseDate": "2026-01-01 01:01",
    "description": "First development snapshot, integrated basic UI framework."
  },
  "1.2.0": {
    "releaseDate": "2026-01-02 13:02",
    "description": "Official 1.2.0 release, fixed known memory leak issues."
  }
}
```
---

## 3. Automation (CI/CD)

### Q: What is the CI/CD process?

The pipeline executes as follows:

1.  **Trigger**: Open terminal, run `git tag <Version>` then `git push origin <Version>`.
2.  **Fetch Code**: Tag push triggers GitHub Actions, fetching the latest code for the Tag.
3.  **Version Conversion**: Script automatically converts `Version` to `SerVersion`.
4.  **Environment Setup**: Sets up JDK and automatically downloads **WiX Toolset** and **Enigma** packaging tools.
5.  **Property Overwrite**: Automatically overwrites `<app.display.version>` and `<app.semantic.version>` in `pom.xml`.
    * *Note: These properties are currently only used for cloud prompts of the current version.*
6.  **Build**: Calls `.build/build.ps1` script to generate software in various distribution formats.
7.  **Log Generation**: Reads date and description from `updates.json` for the current `Version`, concatenates, and outputs to `release_body.txt`.
8.  **Release**: Uses `release_body.txt` as the release note and uploads generated files to GitHub Release.

---

## 4. Troubleshooting

### Q: If the CI/CD process fails, what should I do after fixing?

**A:** If build fails due to code or configuration issues, follow these steps:

1.  Fix the issues locally and commit the code.
2.  Edit the `$TagName` parameter in `scripts/retag.ps1` to the version you want to re-release.
3.  Run `retag.ps1`.
    * The script will automatically delete the old local Tag and the corresponding remote Tag.
    * It will re-tag based on the latest code and push, triggering the CI/CD process again.

