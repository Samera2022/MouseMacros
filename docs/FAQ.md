# FAQ - 项目开发与维护指南

本文档旨在说明项目的版本命名规则、更新日志维护方式以及 CI/CD 自动化流程，以便维护者快速上手。

---

## 1. 版本命名 (Versioning)

### Q: Version 与 SerVersion 有何区别？

本项目区分 **展示版号 (Version)** 与 **语义化版号 (SerVersion)**，以兼顾用户直观理解和 Windows 构建要求。

* **Version (V)**: 用于对外展示、Git Tag 以及 GitHub Release 标题。
    * **稳定版**: 三位版号，如 `1.2.0`。
    * **测试版**: 格式为 `a.b.c-YYmMMx`。
        * `YY`: 发布年份后两位。
        * `MM`: 发布月份。
        * `x`: 当月内该预期稳定版的测试序数（a, b, c...）。
        * *示例*: `1.2.0-26m01a` 表示 2026 年 1 月发布的第 1 个预期指向 1.2.0 的测试版。
* **SerVersion (SV)**: 用于 MSI、EXE 及系统识别的四位版号（`a.b.c.d`）。
    * **稳定版**: 第四位 `d` 补全为 `0`。
    * **测试版**: 第四位 `d` 对应 Version 中当月序数（a=1, b=2...）。

---

## 2. 更新日志 (ChangeLog)

### Q: 如何维护更新日志？

更新日志统一存储在 `src/main/resources/updates.json` 中。该文件不仅供程序内读取，也是 CI 生成 Release Body 的数据源。

* **数据结构**: `Map<String, Map<String, String>>`
* **格式要求**: 键名为 `Version`，值为一个包含 `releaseDate` 和 `description` 的对象。

> **注意**: CI/CD 流程会严格根据推送的 Tag 名（Version）在此文件中匹配信息，请确保键名与 Tag 完全一致。

**示例：**
```json
{
  "1.2.0-26m01a": {
    "releaseDate": "2026-01-01 01:01",
    "description": "第一个开发快照版，集成了基础 UI 框架。"
  },
  "1.2.0": {
    "releaseDate": "2026-01-02 13:02",
    "description": "1.2.0 正式版发布，修复了已知内存泄漏问题。"
  }
}
```
---

## 3. 自动化流程 (CI/CD)

### Q: CI/CD 的流程是什么样的？

按照以下流水线自动执行：

1.  **触发阶段**: 打开终端，输入 `git tag <Version>` 而后 `git push origin <Version>`。
2.  **拉取代码**: Tag 的推送触发 GitHub Actions，获取当前 Tag 对应的最新代码。
3.  **版号转换**: 脚本自动处理 `Version` 到 `SerVersion` 的转换逻辑。
4.  **环境准备**: 设置 JDK 环境，并自动下载安装 **WiX Toolset** 和 **Enigma** 打包工具。
5.  **属性覆写**: 自动覆写 `pom.xml` 中的 `<app.display.version>` 和 `<app.semantic.version>`。
    * *注：这两个属性目前仅用于云端提示当前版号。*
6.  **编译构建**: 调用 `.build/build.ps1` 脚本，生成不同分发格式的软件。
7.  **日志生成**: 从 `updates.json` 中以当前 `Version` 为键读取日期和描述，拼接后输出到 `release_body.txt`。
8.  **发布 Release**: 读取 `release_body.txt` 的内容作为发布说明，并将生成的文件上传至 GitHub Release。

---

## 4. 故障排除 (Troubleshooting)

### Q: 如果 CI/CD 流程出现异常，修复后应该如何处理？

**A:** 若因代码或配置问题导致构建失败，请按以下步骤操作：

1.  在本地修复相关问题并提交代码。
2.  修改项目中 `scripts/retag.ps1` 脚本中的 `$TagName` 参数为需要重新发布的版号。
3.  运行 `retag.ps1`。
    * 该脚本会自动删除本地旧 Tag与远程仓库中对应的旧 Tag。
    * 基于最新代码重新打标并推送，从而再次触发 CI/CD 流程。