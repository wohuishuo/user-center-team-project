# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目背景

这是一个**软件工程基础**课程的团队作业目录，目标项目为「用户中心」系统（User Center）。目录中没有可运行的代码，内容为：

- 课程文档模板（报告、说明书）
- 目标项目的学习笔记与参考资料（2022年星球直播系列）
- 已提交的结构化建模文档

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
