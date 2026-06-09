# React 19 + Vite

> **结论先行**:React 负责**用组件搭界面**,Vite 负责**把代码飞快地跑起来和打包**。一个管"界面怎么写",一个管"开发体验和构建",搭档起来就是用户中心的前端底座。

## 一句话

| 项 | 值 |
| --- | --- |
| 定位 | 前端框架(React)+ 构建工具(Vite) |
| 本书版本 | React 19 + Vite 5 + TypeScript |
| 在用户中心的角色 | 搭登录页、用户管理页等所有界面 |

## 解决什么问题

**结论**:React 解决"界面怎么拆得清楚、改得方便",Vite 解决"改一行代码要等很久才看到效果"。

**根据**:
- **React**:把界面拆成可复用的"组件"(按钮、表单、表格),哪块要改就改哪块,不用动整页。
- **Vite**:基于浏览器原生 ES 模块,开发时改代码**毫秒级**就刷新,不像老工具要等几秒甚至几十秒。

**例子**:登录页是一个 `LoginForm` 组件,用户管理页是一个 `UserTable` 组件;改登录页的样式,完全不影响用户管理页。

## 依赖关系

**结论**:React 是核心,Vite 是它的开发/构建工具,外面再挂 UI 库和数据请求库。

```
React 应用(TypeScript)
  ├─ 用 Vite        跑开发服务器 / 打包成静态文件
  ├─ 用 Ant Design  提供现成 UI 组件
  └─ 用 axios + TanStack Query  调后端接口
```

- **它依赖谁**:Vite(构建)、Node.js(运行环境)。
- **谁依赖它**:[Ant Design](/stack/ant-design)、[TanStack Query](/stack/tanstack-query) 都跑在 React 之上。

## 版本与生命周期(代码视角)

**结论**:理解一个 React 组件的"生命",就理解了前端怎么运转——**渲染 → 响应交互 → 更新 → 卸载**。

```
组件挂载(第一次显示)
  → 用户交互(点按钮、输入)→ 状态(state)变化
  → React 自动重新渲染变化的部分
  → 组件卸载(离开页面)
```

**例子**:登录表单里,你每输入一个字符,输入框的 state 变化,React 只更新那个输入框,不重绘整页——这就是"组件按需更新"。

## 在用户中心里的角色

**结论**:前端所有页面都用它搭。

- 开发时:`npm run dev`,Vite 起本地服务器,改代码即时可见;
- 上线时:`npm run build`,Vite 打包成静态文件,交给 Nginx 托管(见 [部署上线](/project/deployment))。

::: tip 为什么不再用 Umi
Vite 配置简单、启动快,直接用 React + Vite + Ant Design 就能满足用户中心的需要,不必再套一层重框架。少一层,少一份要维护的复杂度。
:::

## 对应资源

- 实战:[前端架构](/project/frontend) · [部署上线](/project/deployment)
- 相关卡:[Ant Design 5](/stack/ant-design) · [TanStack Query](/stack/tanstack-query)
