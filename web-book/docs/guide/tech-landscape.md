# 技术全景图

> **结论先行**:用户中心是一个**前后端分离**的全栈应用。前端用 React 显示界面,后端用 Spring Boot 处理逻辑,数据存在 MySQL,登录态放 Redis,全部装进 Docker 一键启动。看懂这一页,你就握住了全书的地图。

## 一次请求的旅程

**结论**:理解整个系统,最快的方式是跟着一次"登录请求"走一遍。

```
┌─────────────┐    HTTP / JSON   ┌──────────────────────────┐
│   浏览器     │ ───────────────> │   前端(React + Vite)     │
│  (用户)     │ <─────────────── │   构建后由 Nginx 托管     │
└─────────────┘                  └──────────────────────────┘
                                          │  axios 发 /api/** 请求
                                          ▼
                                 ┌──────────────────────────┐
                                 │   后端(Spring Boot)      │
                                 │  Controller 收请求         │
                                 │   → Security 鉴权          │
                                 │   → Service 业务逻辑       │
                                 │   → MyBatis-Plus 操作库    │
                                 └──────────────────────────┘
                                     │                  │
                              读写    │                  │  登录态 / 缓存
                                     ▼                  ▼
                          ┌──────────────┐      ┌──────────────┐
                          │   MySQL 8    │      │    Redis     │
                          └──────────────┘      └──────────────┘

         全部跑在 ── Docker Compose ── 里,提交代码后由 CI/CD 自动构建
```

**例子**:你输账号密码点登录 → 前端把它发到 `/api/user/login` → 后端校验、查库、比对密码 → 生成登录凭证存 Redis → 返回脱敏用户信息 → 前端跳转到首页。每一层只干自己那一段,这就是"分层"。

## 每个技术解决什么问题

**结论**:技术不是越多越好,每一个都在解决一个具体问题。下面这张表,记住"解决什么"那一列就够了。

| 层 | 技术 | 解决什么问题 | 技术卡 |
| --- | --- | --- | --- |
| 前端框架 | React 19 + TypeScript | 用组件搭界面,类型不出错 | [卡](/stack/react-vite) |
| 前端构建 | Vite | 改代码即时刷新、打包飞快 | [卡](/stack/react-vite) |
| UI 组件 | Ant Design 5 | 现成的表格 / 表单 / 布局 | [卡](/stack/ant-design) |
| 数据请求 | TanStack Query + axios | 请求缓存、自动重试、loading | [卡](/stack/tanstack-query) |
| 后端框架 | Spring Boot (Java 21) | 几行配置就跑起一个 Web 服务 | [卡](/stack/spring-boot) |
| 认证授权 | Spring Security + BCrypt | 密码安全存储、接口鉴权 | [卡](/stack/spring-security) |
| 登录态 | Redis(+ JWT / Session) | 登录状态可在多台机器间共享 | [卡](/stack/redis) |
| 持久层 | MyBatis-Plus | 不写 SQL 也能增删改查 | [卡](/stack/mybatis-plus) |
| 数据库 | MySQL 8 | 把用户数据可靠地存下来 | [卡](/stack/mysql) |
| 接口文档 | springdoc (Swagger) | 自动生成可调试的 API 文档 | — |
| 容器编排 | Docker Compose | 一条命令起齐前端+后端+库 | [卡](/stack/docker) |
| 自动化 | GitHub Actions | 提交即自动构建、测试、部署 | [AI 工作流](/engineering/ai-workflow) |

## 谁离不开谁:依赖关系

**结论**:看懂技术栈的关键,是看**谁依赖谁**——"A 依赖 B"意味着没有 B,A 就跑不起来。

```
React 应用
  ├─ 依赖 Vite        (开发服务器 / 打包)
  ├─ 依赖 Ant Design  (现成 UI 组件)
  └─ 依赖 axios → 通过 HTTP 调用后端

Spring Boot 应用
  ├─ 依赖 Spring Security  (鉴权,内部用 BCrypt 编码密码)
  ├─ 依赖 MyBatis-Plus     → MyBatis → JDBC → 连 MySQL
  └─ 依赖 Redis 客户端     (存登录态 / 缓存)

Docker Compose
  └─ 编排上面所有服务 + MySQL + Redis 容器
```

**根据**:分层和依赖让系统"可替换"——换数据库只动 JDBC 那一层,上层业务代码基本不变。

**例子**:有一天要把 MySQL 换成 PostgreSQL,改的是数据库驱动那一层;Service 里的业务逻辑一行都不用动。这就是依赖关系清晰带来的好处。

## 两条生命周期都要懂

**结论**:做项目要同时理解**项目生命周期**和**代码生命周期**——前者是"团队怎么把项目推完",后者是"一段代码/一个对象在程序里怎么生灭"。

**根据**:只懂项目流程,会写不出健壮代码;只懂代码细节,会迷失在团队协作里。两者各是一个金字塔。

**项目生命周期**(团队视角):

```
需求分析 → 设计 → 技术选型 → 编码 → 测试 → 代码评审 → 部署 → 上线维护
```

**代码生命周期**(程序视角,以 Spring Boot 为例):

```
应用启动 → 扫描组件 → 创建 Bean(依赖注入)→ 处理请求(Controller→Service→Mapper)→ 应用关闭
```

**例子**:理解 Spring Boot 的 Bean 生命周期,你才知道为什么 `@Service` 标注的类不用 `new` 就能用——是框架在启动时帮你创建好、并注入到需要它的地方。每个核心技术的"生命周期"都在它的[技术卡](/stack/)里讲。

## 下一步

- 想学怎么把话/文档写清楚 → [金字塔写作方法](/guide/writing-style)
- 想懂方法论 → [理论篇](/theory/)
- 想动手做 → [实战篇 · 认证模块](/project/auth)
- 想查某个技术 → [技术图鉴](/stack/)
