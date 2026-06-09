# Ant Design 5

> **结论先行**:Ant Design 是一套**现成的高质量 UI 组件库**——表格、表单、按钮、布局都给你做好了,你直接用,不用从零画。配套的 Pro Components 还把"一整张管理页"都封装好了。

## 一句话

| 项 | 值 |
| --- | --- |
| 定位 | React 的企业级 UI 组件库 |
| 本书版本 | Ant Design 5 + Pro Components |
| 在用户中心的角色 | 登录表单、用户管理表格、整体布局 |

## 解决什么问题

**结论**:它解决"每个项目都要重新画一遍按钮、表格、表单"的浪费。

**根据**:这些组件不仅要好看,还要处理校验、加载、分页、无障碍等一堆细节。自己写既慢又容易有 bug,用成熟组件库省下大量时间。

**例子**:用户管理的表格,用 Ant Design 的 `ProTable`,分页、搜索、加载状态自动就有,你只需告诉它"列有哪些、数据怎么取"。

## 依赖关系

**结论**:它跑在 React 之上,是 React 的"零件库"。

```
React 应用
  └─ 依赖 Ant Design 5      (基础组件:Button/Form/Table...)
       └─ Pro Components    (业务级组件:ProTable/ProForm/整页布局)
```

- **它依赖谁**:[React](/stack/react-vite)。
- **谁依赖它**:用户中心的所有页面。

## 版本与生命周期

**结论**:用 v5,组件用 props 配置,样式走 CSS-in-JS(无需手动引样式文件)。

```tsx
// 登录表单:ProForm 自动管理表单状态和校验
<ProForm onFinish={handleLogin}>
  <ProFormText name="userAccount" rules={[{ required: true, min: 4 }]} />
  <ProFormText.Password name="userPassword" rules={[{ required: true, min: 8 }]} />
</ProForm>
```

::: tip 校验规则呼应需求
表单里的 `min: 4` / `min: 8` 直接对应 [需求分析](/project/requirements) 里"账号 ≥4 位、密码 ≥8 位"。前端校验提升体验,但**真正的校验仍在后端**——前端可被绕过。
:::

## 在用户中心里的角色

**结论**:三处用它。

- 登录/注册页:`ProForm` + `ProFormText`;
- 用户管理页:`ProTable`(自带分页、搜索);
- 整体框架:Pro 的布局组件 + 权限机制(非管理员不渲染管理菜单,见 [用户管理](/project/user-management))。

## 对应资源

- 实战:[前端架构](/project/frontend) · [用户管理](/project/user-management)
- 相关卡:[React 19 + Vite](/stack/react-vite) · [TanStack Query](/stack/tanstack-query)
