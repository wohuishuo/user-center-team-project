# 本地开发环境说明

用户中心项目的本机依赖,均通过 **scoop** 安装。

## 已安装

| 组件 | 版本 | 说明 |
| --- | --- | --- |
| Java | 21 LTS | `scoop`(已有) |
| Maven | 3.9 | 已有 |
| Node | 24 | 已有 |
| MySQL | 9.7（8+ 系列） | `scoop install mysql-lts` |
| Redis | 8.8 | `scoop install redis` |
| VC++ 运行库 | 2022 | `scoop install extras/vcredist2022`(MySQL 依赖) |

## 重要:中文用户名导致的路径问题

本机用户名是中文(`艾莉`),MySQL 默认把数据目录放在 `scoop\persist`(含中文路径),
导致 `mysqld` 启动时路径被乱码化、无法启动。**解决办法:数据目录改到 ASCII 路径**。

- 数据目录:`C:\mysql-data`
- 错误消息文件:`C:\mysql-share\english\`
- 配置文件:`C:\Users\艾莉\scoop\apps\mysql-lts\current\my.ini`(已指向上面两个 ASCII 路径)

## 启动服务(每次开发前)

```powershell
# 启动 MySQL(分离后台)
$base = "$env:USERPROFILE\scoop\apps\mysql-lts\current"
Start-Process "$base\bin\mysqld.exe" -ArgumentList "--defaults-file=$base\my.ini" -WindowStyle Hidden

# 启动 Redis
Start-Process "$env:USERPROFILE\scoop\apps\redis\current\redis-server.exe" -WindowStyle Hidden
```

验证:

```powershell
& "$env:USERPROFILE\scoop\apps\mysql-lts\current\bin\mysql.exe" -u root -e "SELECT VERSION();"
& "$env:USERPROFILE\scoop\apps\redis\current\redis-cli.exe" ping   # 期望 PONG
```

## 数据库

| 项 | 值 |
| --- | --- |
| 库名 | `user_center`(utf8mb4) |
| root | 空密码(仅本地开发) |
| 应用账号 | `uc_app` / `uc_pass_2026`(仅本地,勿用于生产) |

建表脚本:[`backend/src/main/resources/sql/create_table.sql`](backend/src/main/resources/sql/create_table.sql)

```powershell
Get-Content backend\src\main\resources\sql\create_table.sql -Raw | & "$env:USERPROFILE\scoop\apps\mysql-lts\current\bin\mysql.exe" -u root
```

> 真实密码/机密走环境变量或 `application-local.yml`,不提交 Git(见 `.gitignore`)。
