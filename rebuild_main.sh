# ==========================================================
# 配置区
# ==========================================================
# 在这里按顺序填写你的版本 Tag，如果没有填写，脚本将尝试自动获取
# 建议手动填写以确保顺序 100% 正确
VERSION_TAGS=("0.0.1" "0.0.2" "0.1.0" "1.0.0" "1.0.1" "1.0.2" "1.0.3" "1.0.4" "1.1.0" "1.2.0-26m01a" "1.2.0" "1.2.1" "1.2.2" "1.2.3" "1.3.0" "2.0.0")

# 如果 VERSION_TAGS 为空，则自动获取以 'v' 开头的标签并按版本号排序
if [ ${#VERSION_TAGS[@]} -eq 0 ]; then
    VERSION_TAGS=($(git tag -l 'v*' | sort -V))
fi

NEW_MAIN="main-clean"
OLD_MAIN_BACKUP="main-old-$(date +%Y%m%d%H%M%S)"

# ==========================================================
# 执行逻辑
# ==========================================================

set -e # 遇到错误立即停止

echo "🚀 开始重建整洁的 main 分支..."

# 4. 循环同步每个 Tag 的内容
for TAG in "${VERSION_TAGS[@]}"
do
    echo "📂 正在处理版本: $TAG ..."

    # 清空当前工作区防止旧文件残留（尤其是被删除的文件）
    git rm -rf . --ignore-unmatch > /dev/null

    # 从对应的 Tag 中提取文件内容到当前分支
    git checkout "$TAG" -- .

    # 提交
    git add .
    # 获取原 Tag 的提交信息（可选），这里简单使用版本号
    git commit -m "Release $TAG"

    # 更新 Tag 到这个新的干净提交上
    echo "🏷️  更新标签 $TAG 到新提交..."
    git tag -f "$TAG"
done

# 5. 完成
echo "✅ 重建完成！"
echo "--------------------------------------------------"
echo "当前处于 $NEW_MAIN 分支，该分支历史非常整洁。"
echo "如果确认无误，请执行以下命令正式替换并推送到远程："
echo ""
echo "  git branch -M $NEW_MAIN main"
echo "  git push origin main --force"
echo "  git push origin --tags --force"
echo "--------------------------------------------------"