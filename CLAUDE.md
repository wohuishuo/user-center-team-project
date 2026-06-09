# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目背景

这是一个**软件工程基础**课程的团队作业目录，目标项目为「用户中心」系统（User Center）。内容为：

- 课程文档模板（报告、说明书）
- 目标项目的学习笔记与参考资料（2022年星球直播系列）
- 已提交的结构化建模文档
- **`web-book/`**：一本用 VitePress 做的「网页书」，把课程理论 + 用户中心项目 + 课程文档串成一体（详见下方「网页书」章节）

## 目录结构

```
团队项目/
├── 知识库/
│   ├── 报告模板及个人总结/   # 课程要求的各类说明书模板（.docx/.doc）
│   ├── 目标项目/             # 用户中心项目参考资料
│   │   ├── md/               # 完整版直播笔记（Markdown）
│   │   └── 笔记/             # AI 整理的分章节笔记（.txt）
│   └── 软件工程基础课件/     # 各章节课件（PDF）
├── 结构化建模的文档提交/     # 已提交文档（需求/设计/数据/分工）
└── 用户中心项目概览.html     # 项目幻灯片概览（浏览器打开）
```

## 目标系统：用户中心

### 技术栈

**前端**：HTML/CSS/JS + React + Ant Design + Umi + Ant Design Pro

**后端**：Java + Spring Boot + MyBatis-Plus + MySQL

**部署**：服务器 / Docker 容器

### 核心功能

1. 注册 / 登录（Session + Cookie 方案）
2. 用户管理（仅管理员：查询、删除）
3. 用户校验（特定用户组可见）

### 用户表关键字段

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| userAccount | varchar | 登录账号（≥4位，不含特殊字符） |
| userPassword | varchar | 加密存储（≥8位） |
| userRole | tinyint | 0=普通用户，1=管理员 |
| isDelete | tinyint | 逻辑删除（0/1） |

### 接口约定

- 全局路径前缀：`/api`（配置在 `application.yml` 的 `servlet.context-path`）
- 登录/注册接口：POST，请求体为 JSON
- Controller 层只做参数校验；业务逻辑校验在 Service 层

## 课程文档要求

需提交的报告（模板在 `知识库/报告模板及个人总结/`）：

1. 项目分工表
2. 数据设计说明书
3. 需求规格说明书（结构化 / 面向对象各一份）
4. 系统设计说明书（结构化 / 面向对象各一份）
5. 软件测试报告
6. 自我评价（个人填写）

参考示例见 `知识库/报告模板及个人总结/示例：《在线医患沟通》数据设计说明书 - 仅供参考.docx`。

## 网页书（web-book/）

用 VitePress 搭建的教学网站，定位:**通过用户中心一个项目，同时学会 2026 现代技术栈、软件工程方法论与团队协作**。技术年代为 **2026 新版**（不保留 2022 旧版，差异在书中标注，最典型的是密码存储 MD5 → BCrypt）。

### 本地预览与构建

```powershell
cd web-book
npm install          # 首次
npm run docs:dev     # 本地开发服务器(默认 http://localhost:5173)
npm run docs:build   # 生产构建,输出到 docs/.vitepress/dist
```

### 目录约定

```
web-book/docs/
├── .vitepress/config.mts      # 站点配置:导航 / 侧边栏 / 本地搜索
├── .vitepress/theme/custom.css # 配色(深色科技风,见下)
├── index.md                    # 首页
├── guide/                      # ① 开始(序言、如何阅读、技术全景图)
├── theory/                     # ② 理论篇(对应课程 11 章)
├── project/                    # ③ 实战篇(用户中心全生命周期)
├── stack/                      # ④ 技术图鉴(每个依赖一张卡)
├── engineering/                # ⑤ 团队管理
└── docs-workshop/              # ⑥ 课程文档工坊
```

### 新增 / 填充章节

1. 在对应目录新建 `.md` 文件；
2. **照样板写**：理论看 `theory/02-requirements.md`、实战看 `project/auth.md`、技术卡看 `stack/spring-boot.md`(均带 ★ 标记，质量最高)；
3. 在 `config.mts` 的 `sidebar` 对应分组里加一条 link；
4. 占位章节里的 `> TODO` 就是待填的空，填完删掉 TODO 和"待完成"提示框。

### 配色规范

复用自 `用户中心项目概览.html`：背景 `#0a0a14`、主色 `#6c63ff`(紫)、强调 `#48cfad`(青绿) / `#fed766`(黄)。改配色统一在 `custom.css` 覆盖 VitePress 主题变量。

### 部署

`.github/workflows/deploy.yml` 在 push 到 main 且改动 `web-book/` 时，自动构建并发布到 GitHub Pages。**首次需在 GitHub 仓库 Settings → Pages 把 Source 设为「GitHub Actions」。** `config.mts` 里的 `base` 为 `/user-center-team-project/`，与仓库名一致；改仓库名时需同步修改。
