$ErrorActionPreference = 'Stop'
$bad = Get-ChildItem backend/src -Recurse -File -Include *.java,*.yml,*.yaml | Select-String -Pattern "`t| +$"
if ($bad) { $bad | ForEach-Object { Write-Error "$($_.Path):$($_.LineNumber) invalid whitespace" } }
Push-Location backend
mvn -q -DskipTests compile
Pop-Location
Push-Location frontend
npm run lint
Pop-Location
