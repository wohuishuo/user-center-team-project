$ErrorActionPreference = 'Stop'
& "$PSScriptRoot\lint.ps1"
Push-Location backend
mvn -B verify
Pop-Location
Push-Location frontend
npm run test
npm run build
Pop-Location
