# 2026 技术全景图

> 这是全书的"地图"。看懂这一页,你就知道用户中心由哪些技术组成、它们各自解决什么问题、彼此怎么协作。

## 一张图看懂分层

用户中心是一个**前后端分离**的全栈应用。一次请求的旅程是这样的:

```
┌─────────────┐    HTTP/JSON     ┌──────────────────────────┐
│   浏览器     │ ───────────────> │   前端(静态资源,Nginx)   │
│  (用户)     │ <─────────────── │   React 19 + Vite 构建    │
└─────────────┘                  └──────────────────────────┘
                                          │  axios + TanStack Query
                                          │  /api/** 请求
                                          ▼
                                 ┌──────────────────────────┐
                                 │   后端(Spring Boot 3)    │
                                 │  ┌────────────────────┐  │
                                 │  │ Controller 接收请求 │  │
                                 │  │ Spring Security 鉴权│  │
                                 │  │ Service 业务逻辑    │  │
                                 │  │ MyBatis-Plus 操作库 │  │
                                 │  └────────────────────┘  │
                                 └──────────────────────────┘
                                     │                  │
                              JDBC   │                  │  登录态/缓存
                                     ▼                  ▼
                          ┌──────────────┐      ┌──────────────┐
                          │   MySQL 8    │      │    Redis     │
                          │  (持久化)   │      │  (会话/缓存) │
                          └──────────────┘      └──────────────┘

         全部跑在 ── Docker Compose ── 里,由 GitHub Actions 自动构建部署
```

## 分层技术表

| 层 | 技术 | 解决什么问题 | 技术卡 |
| --- | --- | --- | --- |
| **前端框架** | React 19 + TypeScript | 组件化构建交互界面,类型安全 | [卡](/stack/react-vite) |
| **前端构建** | Vite | 秒级热更新、极快打包(替代 Umi/Webpack) | [卡](/stack/react-vite) |
| **UI 组件** | Ant Design 5 + Pro Components | 现成的高质量表格/表单/布局 | [卡](/stack/ant-design) |
| **数据请求** | TanStack Query + axios | 请求缓存、自动重试、加载态管理 | [卡](/stack/tanstack-query) |
| **后端框架** | Spring Boot 3 (Java 21) | 快速构建 Web 服务,自动配置 | [卡](/stack/spring-boot) |
| **认证授权** | Spring Security + BCrypt | 密码安全存储、接口鉴权 | [卡](/stack/spring-security) |
| **登录态** | JWT / Spring Session + Redis | 无状态登录,可水平扩展 | [卡](/stack/redis) |
| **持久层** | MyBatis-Plus | 不写 SQL 也能增删改查 | [卡](/stack/mybatis-plus) |
| **数据库** | MySQL 8 | 持久化存储用户数据 | [卡](/stack/mysql) |
| **接口文档** | springdoc-openapi (Swagger) | 自动生成可调试的 API 文档 | — |
| **容器编排** | Docker Compose | 一键起齐前端+后端+数据库 | [卡](/stack/docker) |
| **CI/CD** | GitHub Actions | 提交即自动构建、测试、部署 | — |
| **AI 协作** | Claude Code | 生成骨架、写测试、填文档 | [卡](/engineering/ai-workflow) |

## 依赖关系:谁离不开谁

理解技术栈的关键是看**依赖方向**(箭头表示"依赖于"):

```
React 应用
  ├─ 依赖 Vite        (构建/开发服务器)
  ├─ 依赖 Ant Design  (UI 组件)
  └─ 依赖 TanStack Query → 依赖 axios → 走 HTTP 调用后端

Spring Boot 应用
  ├─ 依赖 Spring Security  (鉴权,内部用 BCrypt 编码器)
  ├─ 依赖 MyBatis-Plus     (依赖 MyBatis → 依赖 JDBC → 连 MySQL)
  └─ 依赖 Redis 客户端     (存登录态/缓存)

Docker Compose
  └─ 编排上面所有服务 + MySQL + Redis 容器
```

::: tip 怎么读依赖图
"A 依赖 B"意味着:**没有 B,A 跑不起来**;升级 B 可能影响 A。比如 MyBatis-Plus 依赖 JDBC,所以换数据库(MySQL→PostgreSQL)时,改的是 JDBC 驱动那一层,上层业务代码基本不动——**这就是分层的价值**。
:::

## 2022 → 2026 升级对照

本书相对 2022 项目笔记做的关键升级,以及**为什么**:

| 维度 | 2022 旧版 | 2026 新版 | 为什么变 |
| --- | --- | --- | --- |
| JDK | Java 8 | **Java 21 LTS** | 虚拟线程、record、模式匹配;Java 8 已超期 |
| 后端 | Spring Boot 2.x | **Spring Boot 3.x** | 基于 Jakarta EE,支持原生镜像、新可观测性 |
| 密码 | MD5 + 固定盐 ⚠️ | **BCrypt** | MD5 可被彩虹表/碰撞破解;固定盐等于没盐。BCrypt 每条记录独立盐 + 慢哈希,专为存密码设计 |
| 登录态 | Session + Cookie | **JWT / Redis Session** | 单机 Session 无法水平扩展;无状态/共享会话才能多实例部署 |
| 前端构建 | Umi | **Vite** | Umi 配置重、启动慢;Vite 基于 ESM,冷启动秒级 |
| 数据请求 | umi-request | **TanStack Query** | 自动管理缓存、重试、loading,大幅减少样板代码 |
| 数据库 | MySQL 5.7 | **MySQL 8** | 默认 utf8mb4、窗口函数、CTE;5.7 已停止维护 |
| 部署 | 手动 Docker / 宝塔面板 | **Docker Compose + CI/CD** | 手动易错、不可复现;Compose 一键起全栈,CI/CD 自动化 |
| 开发 | 纯手写 | **Claude Code 辅助** | AI 生成骨架/测试/文档,人专注设计与审校 |

::: warning ⚠️ 必须淘汰的旧做法
**MD5 存密码**是 2022 笔记里最该改的一点。MD5 是快速哈希,2026 的硬件每秒可以试上百亿次,加上"固定盐"对所有用户都一样,一旦库泄露,密码几乎等于明文。本书在 [认证模块](/project/auth) 里用 BCrypt 重写了这一段,并讲清原理。
:::

## 下一步

- 想懂方法论 → [理论篇](/theory/)
- 想动手做 → [实战篇 · 认证模块](/project/auth)(已完整,推荐先看)
- 想查某个技术 → [技术图鉴](/stack/)
