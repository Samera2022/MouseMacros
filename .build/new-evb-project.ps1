param(
    [Parameter(Mandatory=$true)][string]$ImageRoot,
    [Parameter(Mandatory=$true)][string]$OutputFile,
    [Parameter(Mandatory=$true)][string]$ProjectFile,
    [string]$Template = (Join-Path $PSScriptRoot 'evb_settings.evb')
)
$ErrorActionPreference = 'Stop'
$ImageRoot = (Resolve-Path $ImageRoot).Path
# EVB uses an anonymous root tag; temporarily name it for XML manipulation.
$text = (Get-Content $Template -Raw).Replace('<>', '<EnigmaVirtualBox>').Replace('</>', '</EnigmaVirtualBox>')
[xml]$document = $text
$filePrototype = $document.SelectSingleNode('//File[Type="2"]').CloneNode($true)
$directoryPrototype = $document.SelectSingleNode('//File[Type="3"]').CloneNode($true)
function New-EvbDirectory([string]$Path, [string]$Name) {
    $node = $directoryPrototype.CloneNode($true)
    $node.SelectSingleNode('Name').InnerText = $Name
    $children = $node.SelectSingleNode('Files')
    $children.RemoveAll()
    foreach ($entry in Get-ChildItem -LiteralPath $Path -Force | Sort-Object Name) {
        if ($entry.PSIsContainer) {
            $child = New-EvbDirectory $entry.FullName $entry.Name
        } else {
            $child = $filePrototype.CloneNode($true)
            $child.SelectSingleNode('Name').InnerText = $entry.Name
            $child.SelectSingleNode('File').InnerText = $entry.FullName
        }
        $null = $children.AppendChild($child)
    }
    return ,$node
}
$document.EnigmaVirtualBox.InputFile = Join-Path $ImageRoot 'MouseMacros.exe'
$document.EnigmaVirtualBox.OutputFile = [IO.Path]::GetFullPath($OutputFile)
$files = $document.SelectSingleNode('/EnigmaVirtualBox/Files/Files')
$files.RemoveAll()
$null = $files.AppendChild((New-EvbDirectory $ImageRoot '%DEFAULT FOLDER%'))
$document.FirstChild.Encoding = 'utf-8'
$result = $document.OuterXml.Replace('<EnigmaVirtualBox>', '<>').Replace('</EnigmaVirtualBox>', '</>')
[IO.File]::WriteAllText($ProjectFile, $result, [Text.UTF8Encoding]::new($true))
