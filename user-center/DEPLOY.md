# 部署说明

两种运行方式:**A. 本地开发**(已验证可用),**B. Docker 一键部署**(配置就绪,需先装 Docker)。

## A. 本地开发(无需 Docker)

依赖:JDK 21、Maven、Node、MySQL 8、Redis(均已通过 scoop 安装,见 [DEV-SETUP.md](DEV-SETUP.md))。

一键启动后端三件套(MySQL + Redis + 后端):

```powershell
cd user-center
./start-dev.ps1
```

再起前端:

```powershell
cd user-center/frontend
npm install   # 首次
npm run dev   # 打开 http://localhost:5174
```

## B. Docker 一键部署(推荐用于上线/演示)

配置已全部就绪:[`docker-compose.yml`](docker-compose.yml)、前后端 `Dockerfile`、`frontend/nginx.conf`。
装好 Docker 后:

```powershell
cd user-center
copy .env.example .env     # 按需改密码/密钥
docker compose up -d --build
# 浏览器打开 http://localhost
```

四个容器:前端(Nginx,80)、后端(Spring Boot,8080)、MySQL 8、Redis 7。
MySQL 首次启动会自动执行 `create_table.sql` 建表。

### 本机安装 Docker 的前提(需你本人操作)

本机当前没有 Docker 引擎,且 WSL2 未启用。要在 Windows 跑 Docker,需:

1. **以管理员身份**启用 WSL2 并安装(会重启):
   ```powershell
   wsl --install
   ```
   重启后等待 Ubuntu 初始化完成。
2. 安装 **Docker Desktop**(官网 https://www.docker.com/products/docker-desktop/ 下载安装),
   首次启动**接受其许可协议**,并在设置里启用 "Use WSL 2 based engine"。
3. 验证:
   ```powershell
   docker version
   docker compose version
   ```
4. 回到上面的 `docker compose up -d --build`。

> 这几步涉及管理员权限、启用系统虚拟化功能、重启电脑、接受 Docker 许可协议,需由你本人完成。

## CI

[`.github/workflows/user-center-ci.yml`](../.github/workflows/user-center-ci.yml):push 改动 `user-center/` 时,
自动在云端验证前后端能否构建——"提交即构建"的质量闸门。
