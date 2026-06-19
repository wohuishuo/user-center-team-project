$ErrorActionPreference = 'Stop'
& "$PSScriptRoot\lint.ps1"
& "$PSScriptRoot\test-backend.ps1"
$root = Split-Path -Parent $PSScriptRoot
Push-Location (Join-Path $root 'frontend')
npm run test
npm run build
Pop-Location
