# 前端架构

> **结论先行**:用户中心前端用 **React + Vite + TypeScript + Ant Design** 搭成。组织上守两条——**按"页面 / 组件 / 请求 / 状态"分目录**,以及**用全局状态记住"当前是谁"**。

## 一、技术怎么搭配

**结论**:四个技术各管一段,拼起来就是完整前端。

| 技术 | 管什么 |
| --- | --- |
| [React + Vite](/stack/react-vite) | 组件化界面 + 飞快构建 |
| TypeScript | 类型检查,少踩 undefined 的坑 |
| [Ant Design 5](/stack/ant-design) | 现成的表单、表格、布局 |
| [TanStack Query](/stack/tanstack-query) | 向后端取数据 + 缓存/loading |

**例子**:登录页 = React 组件 + Ant Design 的 `ProForm` + 用 TanStack Query 把表单提交给后端。

## 二、目录怎么分

**结论**:按职责分目录,找东西不靠猜。

```
src/
├── pages/        # 页面(登录页、用户管理页)
├── components/   # 可复用组件
├── services/     # 调后端接口的函数(按模块分)
├── stores/       # 全局状态(当前用户等)
├── models/       # TypeScript 类型(和后端 VO 对应)
└── access.ts     # 权限规则
```

**根据**:页面、组件、请求、状态各归各的目录,新人一看就知道某段代码该在哪——这是前端版的[高内聚低耦合](/theory/03-design)。

## 三、全局状态:记住"当前是谁"

**结论**:登录后把当前用户存进全局状态,整个应用都能读到。

**根据**:很多地方都要知道"当前用户是谁、是不是管理员"——导航栏要显示头像、菜单要按角色显隐。存一份全局状态,各处直接读,不用层层传递。

**例子**:

```ts
// 用 TanStack Query 取当前用户,存进全局
const { data: currentUser } = useQuery({
  queryKey: ['currentUser'],
  queryFn: fetchCurrentUser,
});
```

## 四、权限:前端隐藏 + 后端鉴权

**结论**:`access.ts` 按角色决定"能看到哪些页面",但这只是体验层。

**根据**:前端隐藏菜单提升体验(普通用户看不到管理入口),但**真正的安全闸门在后端**——前端代码可被绕过。两者是双保险,缺一不可(见 [用户管理](/project/user-management))。

**例子**:

```ts
// access.ts:角色为管理员才放行管理页
export default (currentUser) => ({
  canAdmin: currentUser?.userRole === 1,
});
```

## 五、开发与上线

**结论**:开发 `npm run dev`(Vite 即时热更新),上线 `npm run build`(打包成静态文件交给 Nginx)。

**例子**:打包产物丢进 [Docker](/stack/docker) 的前端容器,由 Nginx 托管,详见 [部署上线](/project/deployment)。

## 本章小结

- **结论**:React + Vite + TS + Ant Design,按职责分目录;
- **根据**:全局状态记住当前用户,各处直接读;
- **例子**:`access.ts` 控制菜单显隐,但安全闸门在后端。

## 对应资源

- 技术卡:[React 19 + Vite](/stack/react-vite) · [Ant Design 5](/stack/ant-design) · [TanStack Query](/stack/tanstack-query)
- 实战:[认证模块](/project/auth) · [用户管理](/project/user-management) · [部署上线](/project/deployment)
