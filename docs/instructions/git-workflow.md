# Git 工作流与提交规范

本项目的 Git 管理标准。所有开发者（含 AI 代理）必须遵守。

## 1. 分支模型

三类分支：

| 分支 | 作用 | 谁能推 |
| --- | --- | --- |
| `main` | 稳定版 + 开发版聚合，历史保持干净 | 仅通过 **squash merge** 从 `dev` 汇入 |
| `dev` | 集成分支，功能分支测试通过后合入 | 通过 PR / merge 从功能分支汇入 |
| `feat/*`, `fix/*`, `refactor/*`, `ci/*`, `docs/*`, `test/*`, `chore/*` | 功能分支，一次功能一支 | 直接开发提交 |

### 分支命名

`<type>/<short-kebab-topic>`，示例：

- `feat/todo-list-panel`
- `feat/dep-expr-builder`
- `fix/wayland-window-drag`
- `refactor/repo-trait`
- `ci/release-pipeline`
- `docs/roadmap-update`

## 2. 开发流程

```
main
 └── dev
      ├── feat/xxx  ──(测试通过后 merge)──▶ dev
      ├── feat/yyy  ──(测试通过后 merge)──▶ dev
      └── fix/zzz   ──(测试通过后 merge)──▶ dev
                                              │
                                              └── (稳定发布) squash merge ──▶ main
```

严格步骤：

1. 从最新 `dev` 切出功能分支：`git switch dev && git pull && git switch -c feat/xxx`
2. 在功能分支上小步提交，遵守下方 commit 规范
3. 完成后本地跑通全部测试
4. 合入 `dev`：使用 **普通 merge**（保留提交历史，便于回溯与二分定位）
5. 达到发布节点时，`dev` **squash merge** 到 `main`，合并 commit 使用发布版本信息，例如 `feat: v0.1.0 release`
6. `main` 打 tag：`git tag v0.1.0 && git push --tags`

约束：

- **禁止**在 `main` 上直接开发或直接 merge 功能分支
- **禁止**在 `dev` 上直接开发（除极小的合并冲突解决 commit）
- **禁止**在功能分支合入后长期保留；合并完成后删除
- **禁止** force-push `main` 与 `dev`；功能分支允许 `git push --force-with-lease`

## 3. Commit 消息规范

### 3.1 格式

```
<type>: <subject>
```

- `type` 取值：`feat` `fix` `refactor` `ci` `docs` `test` `chore` `perf` `style` `build`
- `subject`：**极短**、小写开头、不加句号；每段不超过 **5 个单词**
- 一次提交允许包含**至多 3 段**，段间用 ` & ` 分隔

### 3.2 合法示例

```
feat: advanced file system
feat: advanced file system & config system
feat: todo panel & drag support & theme sync
fix: wayland always-on-top
refactor: extract repo trait
ci: add windows build job
docs: dep expr semantics
test: deps proptest
```

### 3.3 非法示例

```
❌ feat: implement the advanced file system for cross-platform support
   (超过 5 个单词)

❌ feat: file system & config system & theme sync & drag support
   (超过 3 段)

❌ Feat: File System
   (type 与 subject 均需小写)

❌ add file system
   (缺少 type 前缀)

❌ feat: file system.
   (subject 末尾禁止句号)
```

### 3.4 判定「一段」的规则

- 每个 `&` 分隔的片段独立满足「≤ 5 个单词」
- 段与段之间用 ` & `（前后各一个空格），不使用逗号或分号
- 中文字符按语义计数，一个短语视为 1 个单词（例如 `feat: 依赖图 双击勾选` 合法，3 词）

## 4. Merge 策略速查

| 目标分支 | 来源 | 策略 |
| --- | --- | --- |
| `dev` | `feat/*` `fix/*` `refactor/*` … | **普通 merge**（保留分支提交历史） |
| `main` | `dev` | **squash merge**（一个发布一个 commit） |
| 功能分支 | `dev`（同步最新） | **rebase**（保持功能分支线性） |

## 5. 发布 tag 规范

- 遵循 SemVer：`v<MAJOR>.<MINOR>.<PATCH>`
- 仅在 `main` 上打 tag
- Tag 触发 CI 构建 Windows / Linux 产物并创建 GitHub Release

## 6. 与 AI 代理协作时的额外约束

- AI 代理**禁止**主动执行以下命令，除非用户显式许可：
  - `git commit`、`git commit --amend`
  - `git push`、`git push --force*`
  - `git reset --hard`、`git clean -f`
  - `git branch -D`、tag 推送
- AI 可以自由：`status` / `diff` / `log` / `switch` / `branch`（本地非破坏性）
- 每次准备提交前，AI 需列出：
  1. 变更文件清单
  2. 拟用的 commit 消息（须通过第 3 节校验）
  3. 目标分支
  等待用户确认后再执行
