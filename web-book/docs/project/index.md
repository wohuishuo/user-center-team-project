# 用户中心实战篇(2026)

本篇带你**从零做出「用户中心」**,全程使用 2026 现代技术栈。顺序就是企业里真实的开发顺序:

```
需求分析 → 数据库设计 → 后端实现 → 前端实现 → 测试 → 部署上线
```

## 项目是什么

用户中心是一个**可独立部署的用户管理服务**——就像 QQ 号、支付宝账号那样,把"用户"这件事抽出来单独做一个系统,别的业务系统都来对接它。功能不多但五脏俱全:

- **注册 / 登录**:账号密码注册,登录后保持登录态
- **用户管理**:仅管理员可见,查询、删除用户
- **用户校验**:用"星球编号"等标识校验用户身份

## 本篇章节

| 章节 | 内容 |
| --- | --- |
| [需求分析](/project/requirements) | 功能清单、用例、边界 |
| [数据库设计](/project/database) | user 表字段、索引、逻辑删除 |
| [认证模块](/project/auth) | 注册/登录的安全实现(BCrypt + JWT) |
| [用户管理](/project/user-management) | 权限控制、分页查询 |
| [后端架构与分层](/project/backend) | Controller / Service / Mapper |
| [前端架构](/project/frontend) | React 19 + Vite + Ant Design 5 |
| [测试](/project/testing) | 单元测试、集成测试 |
| [部署上线](/project/deployment) | Docker Compose + CI/CD |

> [认证模块](/project/auth) 是实战篇结构最完整的一章,适合作为新增章节的范例。

::: tip 本篇的代码是真实存在的
本篇讲的项目**已经完整实现并跑通**,代码在仓库 [`user-center/`](https://github.com/wohuishuo/user-center-team-project/tree/main/user-center) 目录(前后端 + 数据库脚本 + 测试 + Docker 配置,CI 构建通过)。每章末尾的「本章的真实代码」区块直接链到对应文件——看完讲解,马上能看真实现。运行方法见仓库 [README](https://github.com/wohuishuo/user-center-team-project#-运行项目)。
:::

::: tip 先看全景再动手
动手前先扫一眼 [技术全景图](/guide/tech-landscape),知道每个技术解决什么问题、彼此怎么协作,再回来从需求开始做,会顺很多。
:::
