$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
$bad = Get-ChildItem (Join-Path $root 'backend/src') -Recurse -File -Include *.java,*.yml,*.yaml | Select-String -Pattern "`t| +$"
if ($bad) { $bad | ForEach-Object { Write-Error "$($_.Path):$($_.LineNumber) invalid whitespace" } }
Push-Location (Join-Path $root 'backend')
mvn -q -DskipTests compile
Pop-Location
Push-Location (Join-Path $root 'frontend')
npm run lint
Pop-Location
