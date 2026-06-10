# 本地开发一键启动:MySQL + Redis + 后端
# 用法:cd user-center; ./start-dev.ps1
# 前端另起:cd frontend; npm run dev

$ErrorActionPreference = "Stop"
$myBase = "$env:USERPROFILE\scoop\apps\mysql-lts\current"
$rBase  = "$env:USERPROFILE\scoop\apps\redis\current"
$jar    = "$PSScriptRoot\backend\target\user-center-backend-0.0.1-SNAPSHOT.jar"

function Listening($port) {
  return (Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue | Measure-Object).Count -gt 0
}

if (-not (Listening 3306)) {
  Write-Host "启动 MySQL..."
  Start-Process "$myBase\bin\mysqld.exe" -ArgumentList "--defaults-file=$myBase\my.ini" -WindowStyle Hidden
} else { Write-Host "MySQL 已在运行" }

if (-not (Listening 6379)) {
  Write-Host "启动 Redis..."
  Start-Process "$rBase\redis-server.exe" -WindowStyle Hidden
} else { Write-Host "Redis 已在运行" }

if (-not (Test-Path $jar)) {
  Write-Host "后端 jar 不存在,先构建..."
  Push-Location "$PSScriptRoot\backend"; mvn -q -DskipTests package; Pop-Location
}

if (-not (Listening 8080)) {
  Write-Host "启动后端..."
  Start-Process "java" -ArgumentList "-jar", "`"$jar`"" -WindowStyle Hidden
} else { Write-Host "后端已在运行" }

Start-Sleep -Seconds 12
try {
  $h = (Invoke-RestMethod "http://localhost:8080/api/health" -TimeoutSec 8).message
  Write-Host "后端健康检查: $h"
  Write-Host "全部就绪。前端请另起:cd frontend; npm run dev  (http://localhost:5174)"
} catch {
  Write-Host "后端尚未就绪,请稍候重试或查看日志。"
}
