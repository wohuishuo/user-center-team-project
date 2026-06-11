<div align="center">

# 用户中心 · 软件工程实战书 + 完整实现

**一本书 + 一个真实跑通的全栈项目:通过「用户中心」,学会现代技术栈、软件工程方法论与团队协作。**

书里讲的每一步,仓库里都有对应的真实代码——理论与实现互相印证。

<br>

### 📖 [**→ 在线阅读网页书 ←**](https://wohuishuo.github.io/user-center-team-project/)

<br>

[![在线阅读](https://img.shields.io/badge/在线阅读-Read_Online-6c63ff?style=for-the-badge&logo=readthedocs&logoColor=white)](https://wohuishuo.github.io/user-center-team-project/)
[![Stars](https://img.shields.io/github/stars/wohuishuo/user-center-team-project?style=for-the-badge&logo=github&color=6c63ff)](https://github.com/wohuishuo/user-center-team-project/stargazers)
[![CI](https://img.shields.io/github/actions/workflow/status/wohuishuo/user-center-team-project/user-center-ci.yml?style=for-the-badge&label=CI&color=48cfad)](https://github.com/wohuishuo/user-center-team-project/actions/workflows/user-center-ci.yml)
[![Last Commit](https://img.shields.io/github/last-commit/wohuishuo/user-center-team-project?style=for-the-badge&color=fed766)](https://github.com/wohuishuo/user-center-team-project/commits/main)

[![Java](https://img.shields.io/badge/Java-21_LTS-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19-61DAFB?style=flat-square&logo=react&logoColor=black)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-6-646CFF?style=flat-square&logo=vite&logoColor=white)](https://vite.dev/)
[![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?style=flat-square&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-7-DC382D?style=flat-square&logo=redis&logoColor=white)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white)](https://docs.docker.com/compose/)
[![VitePress](https://img.shields.io/badge/Book-VitePress-6c63ff?style=flat-square&logo=vite)](https://vitepress.dev/)

</div>

---

## 这个仓库有什么

| 交付物 | 位置 | 状态 |
| --- | --- | --- |
| 📖 **网页书**(教程,六大篇全部写实) | [`web-book/`](web-book/) | ✅ 已上线 [在线阅读](https://wohuishuo.github.io/user-center-team-project/) |
| 🛠️ **用户中心完整实现**(前后端 + 数据库 + 缓存) | [`user-center/`](user-center/) | ✅ 全栈跑通,9 个测试绿灯,CI 通过 |
| 📄 **课程文档**(需求规格 / 数据设计 / 测试报告) | [`文档产出/`](文档产出/) | ✅ 按课程模板产出 |

> 《软件工程基础》课程团队项目。我们没有止步于交作业:先写了一本书讲清"该怎么做",再**严格照书把项目实现出来**——书中每章末尾都能找到对应的真实代码。

> 🛰️ **它还有续集**:本项目现在同时是 [书域 BookRealm](https://github.com/wohuishuo/book-realm) 电子书平台的**登录模块(MVP-0)**——平台里的 Android 阅读 App、书库、AI 问答等服务,都用这里的接口做统一登录。一个项目,两门课,真实复用。

## 功能一览

- **注册 / 登录**:BCrypt 加密存储密码,JWT 无状态登录态,错误提示不泄露账号是否存在
- **用户管理**(仅管理员):按用户名模糊分页查询、逻辑删除;前端隐藏入口 + 后端强制鉴权双保险
- **当前用户**:Redis 缓存,减少重复查询
- **统一工程化**:`/api` 前缀、统一返回 `{code,data,message}`、全局异常处理、Swagger 接口文档

## 🚀 运行项目

### 方式 A:Docker 一键起全栈(推荐,需 Docker)

```bash
cd user-center
cp .env.example .env          # 按需修改密码/密钥
docker compose up -d --build
# 浏览器打开 http://localhost
```

四个容器自动就绪:前端(Nginx :80)、后端(Spring Boot :8080)、MySQL 8(自动建表 + 演示账号)、Redis 7。

**内置演示账号,开箱即登录:**

| 账号 | 密码 | 角色 |
| --- | --- | --- |
| `root` | `12345678` | 管理员(可进用户管理) |
| `demo` | `12345678` | 普通用户 |

> ⚠️ 演示账号仅限本地/演示环境,部署生产前请从 `create_table.sql` 删除。

> Windows 首次装 Docker 见 [`user-center/DEPLOY.md`](user-center/DEPLOY.md)(需启用 WSL2 并重启)。

### 方式 B:本地开发(无需 Docker)

**前置依赖**:JDK 21、Maven、Node.js 22+、MySQL 8、Redis。Windows 用 scoop 一行装齐缺的:

```powershell
scoop install mysql-lts redis extras/vcredist2022
```

**1)初始化数据库**(仅首次):

```powershell
# 启动 MySQL 后执行建表脚本
mysql -u root < user-center/backend/src/main/resources/sql/create_table.sql
```

**2)启动后端三件套**(MySQL + Redis + Spring Boot,一键脚本):

```powershell
cd user-center
./start-dev.ps1               # 健康检查通过即就绪
```

**3)启动前端**:

```powershell
cd user-center/frontend
npm install                   # 首次
npm run dev                   # 打开 http://localhost:5174
```

用演示账号 `root / 12345678`(管理员)或 `demo / 12345678` 直接登录,也可自己注册。

> 常见坑(中文用户名路径、npm 缓存)与详细环境说明:[`user-center/DEV-SETUP.md`](user-center/DEV-SETUP.md)

### 运行测试

```powershell
cd user-center/backend
mvn test                      # 6 单元 + 3 集成,需本地 MySQL 在运行
```

### Swagger 接口文档

后端启动后访问 `http://localhost:8080/api/swagger-ui.html`,可直接调试所有接口。

## 📖 网页书

**六大篇**:① 开始(含金字塔写作方法)→ ② 理论篇(软件工程 6 章)→ ③ 实战篇(全生命周期)→ ④ 技术图鉴(每个依赖一张卡)→ ⑤ 团队管理(Git/评审/AI 工作流)→ ⑥ 文档工坊(作业对照表)。

- 🌐 在线阅读:<https://wohuishuo.github.io/user-center-team-project/>
- 💻 本地预览:

```bash
cd web-book
npm install
npm run docs:dev              # http://localhost:5173/user-center-team-project/
```

push 到 `main` 且改动 `web-book/` 时,GitHub Actions 自动重新发布。

## 技术栈

**后端** Java 21 · Spring Boot 3.3 · Spring Security(BCrypt)· JWT · MyBatis-Plus · MySQL 8 · Redis 7 · springdoc(Swagger)
**前端** React 19 · Vite 6 · TypeScript · Ant Design 5 · TanStack Query · zustand
**工程** Maven · JUnit 5(单元+集成测试)· Docker Compose · GitHub Actions CI

## 目录结构

```
团队项目/
├── web-book/                  # 📖 VitePress 网页书
├── user-center/               # 🛠️ 项目实现
│   ├── backend/               #    Spring Boot 3(controller/service/mapper 分层)
│   ├── frontend/              #    React 19 + Vite + Ant Design
│   ├── docker-compose.yml     #    一键起全栈
│   ├── start-dev.ps1          #    本地开发一键启动
│   ├── DEV-SETUP.md           #    环境搭建(含踩坑记录)
│   └── DEPLOY.md              #    部署说明
├── 文档产出/                  # 📄 课程文档(.docx)
├── 知识库/                    # 课程课件、参考笔记、文档模板
├── 结构化建模的文档提交/       # 已提交的课程文档
└── .github/workflows/         # 网页书发布 + 项目 CI
```

## 参与开发

1. 先读网页书对应章节(理论 → [`theory/`](web-book/docs/theory/),实现 → [`project/`](web-book/docs/project/));
2. 改代码走 feature 分支 + PR(见书:[Git 协作](https://wohuishuo.github.io/user-center-team-project/engineering/git-workflow));
3. 写作规范([金字塔方法](https://wohuishuo.github.io/user-center-team-project/guide/writing-style))与目录约定见 [`CLAUDE.md`](CLAUDE.md)。

---

<div align="center">

一本书 + 一个项目 · 2026 AI 时代版
**用户中心团队** · 《软件工程基础》课程项目

⭐ 如果对你有帮助,欢迎点个 Star ⭐

</div>
