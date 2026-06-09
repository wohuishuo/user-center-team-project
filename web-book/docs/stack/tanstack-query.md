# TanStack Query

> **结论先行**:TanStack Query 替你管理"向后端要数据"这件事——**自动处理加载中、出错、缓存、重试**。你只说"我要哪份数据",剩下的状态它全包了。

## 一句话

| 项 | 值 |
| --- | --- |
| 定位 | 前端数据请求与缓存库 |
| 本书版本 | TanStack Query v5(配合 axios) |
| 在用户中心的角色 | 拉取当前用户、用户列表等数据 |

## 解决什么问题

**结论**:它解决"每次请求都要自己写一堆 loading / error / 缓存的样板代码"。

**根据**:发一个请求,你得管:正在加载吗?失败了吗?要不要重试?上次的数据能不能先用着?自己写这些既重复又易错。TanStack Query 把这套状态机做好了。

**例子**:拉用户列表,它自动给你 `isLoading`、`error`、`data` 三个状态,页面按状态显示"加载中 / 出错了 / 列表",不用自己维护。

## 依赖关系

**结论**:它跑在 React 上,底层用 axios 真正发请求。

```
React 组件
  └─ 用 TanStack Query 的 useQuery  (管状态/缓存)
       └─ 调 axios                   (真正发 HTTP)
            └─ 请求后端 /api/**
```

- **它依赖谁**:[React](/stack/react-vite)、axios。
- **谁依赖它**:需要展示后端数据的页面。

## 版本与生命周期(数据视角)

**结论**:一份数据在它手里经历"请求中 → 成功/失败 → 进缓存 → 过期重取"的循环。

```tsx
const { data, isLoading, error } = useQuery({
  queryKey: ['currentUser'],          // 这份数据的唯一标识
  queryFn: () => axios.get('/api/user/current').then(r => r.data),
});

if (isLoading) return <Spin />;       // 加载中
if (error) return <Empty />;          // 出错
return <Profile user={data} />;       // 成功
```

**根据**:同样 `queryKey` 的数据会被缓存,多个组件要同一份数据时只请求一次,这就是它省事又省流量的原因。

## 在用户中心里的角色

**结论**:凡是"从后端取数据来显示"的地方都用它。

- 取当前登录用户(决定显不显示管理菜单);
- 取用户管理列表(配合 [Ant Design](/stack/ant-design) 的 ProTable)。

## 对应资源

- 实战:[前端架构](/project/frontend) · [认证模块](/project/auth)
- 相关卡:[React 19 + Vite](/stack/react-vite) · [Ant Design 5](/stack/ant-design)
