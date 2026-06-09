# Docker Compose

> **结论先行**:Docker 把"代码 + 运行环境"打包成**容器**,搬到哪都一样跑;Docker Compose 用一份文件把用户中心的**四个容器(前端、后端、MySQL、Redis)一次性编排启动**。

## 一句话

| 项 | 值 |
| --- | --- |
| 定位 | 容器化与多容器编排 |
| 本书版本 | Docker + Compose v2 |
| 在用户中心的角色 | 一键起齐整套服务 |

## 解决什么问题

**结论**:它解决两个老大难——"我电脑能跑你电脑跑不起来",和"起一个项目要手动装一堆东西"。

**根据**:
- **容器**:把 Java、依赖、代码封在一起,服务器装没装环境都不影响(见 [部署上线](/project/deployment))。
- **Compose**:用户中心有四个相互依赖的服务,手动一个个起又慢又容易漏;一份配置文件全搞定。

**例子**:新同学加入,不用照着文档装 MySQL、Redis、Java——`docker compose up` 一条命令,整套环境就起来了。

## 依赖关系

**结论**:Compose 站在最外层,编排其余所有服务容器。

```
docker-compose.yml
  ├─ frontend 容器(Nginx 托管 React 构建产物)
  ├─ backend  容器(Spring Boot 的 jar)
  ├─ mysql    容器
  └─ redis    容器
       (Compose 负责:谁先起、谁连谁、怎么传环境变量)
```

- **它依赖谁**:Docker 引擎。
- **谁依赖它**:整个项目的本地开发和部署。

## 版本与生命周期

**结论**:记住四条命令就够日常用。

| 命令 | 作用 |
| --- | --- |
| `docker compose up -d` | 后台启动全部服务 |
| `docker compose ps` | 看哪些在跑 |
| `docker compose logs -f backend` | 跟踪后端日志 |
| `docker compose down` | 停掉并清理(数据 volume 默认保留) |

::: tip 镜像要瘦身
后端用[多阶段构建](/project/deployment)——编译用带 Maven 的大镜像,运行只留带 Java 的小镜像。镜像越小,拉取越快、攻击面越小。
:::

## 在用户中心里的角色

**结论**:本地开发和上线都靠它把四个服务串起来。

**例子**:`docker compose up` 后,前端在 80 端口、后端在 8080、MySQL 和 Redis 在内部网络互通,整套用户中心立刻可访问。完整配置见 [部署上线](/project/deployment)。

## 对应资源

- 实战:[部署上线](/project/deployment)
- 相关卡:[MySQL 8](/stack/mysql) · [Redis](/stack/redis)
- 协作:[AI 协作工作流](/engineering/ai-workflow)(CI/CD)
