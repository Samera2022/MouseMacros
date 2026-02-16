param ([string]$TagName = "1.3.0")

if ($TagName -eq "") {
    $TagName = git describe --tags --abbrev=0
}

Write-Host "--- Re-pushing Tag: [$TagName] ---" -ForegroundColor Cyan

# Delete remote and local tags
git push origin --delete $TagName
git tag -d $TagName

# Create and push new tag
git tag $TagName
git push origin $TagName

Write-Host "Done! GitHub Action triggered." -ForegroundColor Green