<div align="center">

# 用户中心 · 软件工程实战书

**用一个全栈项目,同时学会现代技术栈、软件工程方法论与团队协作。**

一本以「用户中心」项目为主线的教学网页书 —— 把课程理论、项目实战、课程文档拧成一股绳。

<br>

[![Stars](https://img.shields.io/github/stars/wohuishuo/user-center-team-project?style=for-the-badge&logo=github&color=6c63ff)](https://github.com/wohuishuo/user-center-team-project/stargazers)
[![Forks](https://img.shields.io/github/forks/wohuishuo/user-center-team-project?style=for-the-badge&logo=github&color=48cfad)](https://github.com/wohuishuo/user-center-team-project/network/members)
[![Last Commit](https://img.shields.io/github/last-commit/wohuishuo/user-center-team-project?style=for-the-badge&color=fed766)](https://github.com/wohuishuo/user-center-team-project/commits/main)

[![VitePress](https://img.shields.io/badge/Built_with-VitePress-6c63ff?style=flat-square&logo=vite)](https://vitepress.dev/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19-61DAFB?style=flat-square&logo=react&logoColor=black)](https://react.dev/)
[![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?style=flat-square&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white)](https://docs.docker.com/compose/)

</div>

---

## 这是什么

《软件工程基础》课程团队作业,目标项目是「用户中心」(User Center)。我们没有止步于交作业,而是做了一本 **VitePress 网页书**:读者通过这一个项目,既学会全部现代技术(及其生命周期、依赖关系),又学会软件工程方法论和团队协作。

> 读完这本书,你应该能独立做出一个管理系统,并且说得清「为什么这么做」。

## 这本书有什么特别

- 📐 **理论 ↔ 实战 ↔ 文档 三网合一** —— 每个软件工程概念,旁边就是它在用户中心里的真实代码,以及对应的课程文档怎么写。
- 🎯 **金字塔写法** —— 全书结论先行(结论→根据→例子),一分钟能说清,读者中心。
- 🚀 **只讲还在用的技术** —— 不复述历史、不绕弯路,直接讲现在该怎么做。
- 🤖 **AI 时代工作流** —— 讲透 Claude Code 的 hooks、MCP、GitHub PR 审查等实战与坑。

## 六大篇

| 篇 | 内容 |
| --- | --- |
| ① 开始 | 序言、如何阅读、**金字塔写作方法**、技术全景图 |
| ② 理论篇 | 软件工程 6 章(概述 / 需求 / 设计 / 实现 / 测试 / 维护与管理) |
| ③ 实战篇 | 用户中心全生命周期:需求 → 数据库 → 认证 → 后端 → 前端 → 测试 → 部署 |
| ④ 技术图鉴 | 每个依赖一张卡:Spring Boot/Security、MyBatis-Plus、MySQL、Redis、React、Ant Design… |
| ⑤ 团队管理 | 生命周期、Git 协作、代码评审、AI 协作工作流 |
| ⑥ 文档工坊 | 6 类课程文档 → 书内章节对照表 |

## 技术栈

**后端** Java 21 · Spring Boot 3 · Spring Security(BCrypt)· MyBatis-Plus · MySQL 8 · Redis
**前端** React 19 · Vite · TypeScript · Ant Design 5 · TanStack Query
**部署** Docker Compose · GitHub Actions

## 本地预览

```bash
cd web-book
npm install
npm run docs:dev      # 打开 http://localhost:5173/user-center-team-project/
```

构建生产版本:

```bash
npm run docs:build    # 输出到 docs/.vitepress/dist
```

> 提示:若 `npm install` 因路径报错,可加 `--cache <ASCII路径>` 指定缓存目录。

## 目录结构

```
团队项目/
├── web-book/              # 📖 VitePress 网页书(本仓库的主角)
│   └── docs/              # 六大篇章节(guide / theory / project / stack / engineering / docs-workshop)
├── 知识库/                # 课程课件、项目参考笔记、文档模板
├── 结构化建模的文档提交/   # 已提交的课程文档
└── .github/workflows/     # GitHub Actions 自动部署
```

## 部署

仓库已配置 `.github/workflows/deploy.yml`:push 到 `main` 且改动 `web-book/` 时,自动构建并发布到 GitHub Pages。

> 首次启用需在 **Settings → Pages** 把 Source 设为「GitHub Actions」。注意:私有仓库的 Pages 站点在免费账户下是公开的,请按需取舍。

## 参与填充 / 修订

想新增或改写章节?照范例来:

- 理论 → [`theory/02-requirements.md`](web-book/docs/theory/02-requirements.md)
- 实战 → [`project/auth.md`](web-book/docs/project/auth.md)
- 技术卡 → [`stack/spring-boot.md`](web-book/docs/stack/spring-boot.md)

写作规范见 [`web-book/docs/guide/writing-style.md`](web-book/docs/guide/writing-style.md),目录与配色约定见根目录 [`CLAUDE.md`](CLAUDE.md)。

---

<div align="center">

一本通过项目学软件工程的书 · 2026 AI 时代版
**用户中心团队** · 《软件工程基础》课程项目

⭐ 如果这本书帮到了你,欢迎点个 Star ⭐

</div>
