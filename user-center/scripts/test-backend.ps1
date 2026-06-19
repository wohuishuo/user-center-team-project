$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
Push-Location $root
try {
    docker compose -f docker-compose.test.yml up -d --wait
    Push-Location backend
    try {
        mvn -B verify
        if ($LASTEXITCODE -ne 0) { throw "Backend verification failed with exit code $LASTEXITCODE" }
    } finally { Pop-Location }
} finally {
    docker compose -f docker-compose.test.yml down -v
    Pop-Location
}
