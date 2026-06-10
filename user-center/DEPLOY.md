# 部署说明

两种运行方式:**A. 本地开发**,**B. Docker 一键部署**。两种均已在本机实跑验证通过。

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

### 常用 Docker 命令速查

```powershell
docker compose up -d --build   # 构建并后台启动全部服务
docker compose ps              # 看哪些容器在跑(STATUS 应为 Up)
docker compose logs -f backend # 跟踪后端日志(Ctrl+C 退出)
docker compose restart backend # 重启某个服务
docker compose down            # 停止并移除容器(数据 volume 保留)
docker compose down -v         # ⚠️ 连数据一起删(慎用)
```

> 改了代码想生效:重新 `docker compose up -d --build`(只重建有改动的镜像)。
> 拉镜像偶发网络 EOF 失败:重试同一条命令即可。

## CI

[`.github/workflows/user-center-ci.yml`](../.github/workflows/user-center-ci.yml):push 改动 `user-center/` 时,
自动在云端验证前后端能否构建——"提交即构建"的质量闸门。
