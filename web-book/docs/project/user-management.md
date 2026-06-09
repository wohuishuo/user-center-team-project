# 用户管理(管理员)

> **结论先行**:用户管理只对管理员开放,做两件事——**查询**和**删除**。安全要害只有一句话:**每个接口都要先确认"你是不是管理员"**。

## 一、最重要的事:鉴权

**结论**:管理类接口,第一步永远是校验角色,不通过直接拒绝。

**根据**:用户管理能看到、删除别人的数据。如果不鉴权,普通用户拼个 URL 就能删人,这是严重越权事故。

**例子**:

```java
private void ensureAdmin(HttpServletRequest request) {
    User login = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
    if (login == null || login.getUserRole() != ADMIN_ROLE) {
        throw new BusinessException(ErrorCode.NO_AUTH, "无管理员权限");
    }
}
```

每个管理接口进来先调它,这就是[认证模块](/project/auth)里"鉴权"在管理功能上的落地。

## 二、查询用户

**结论**:支持按用户名**模糊查询**,返回的列表必须**脱敏**。

```java
@GetMapping("/search")
public BaseResponse<List<User>> search(String username, HttpServletRequest request) {
    ensureAdmin(request);                       // 1. 先鉴权
    List<User> list = userService.lambdaQuery()
        .like(StringUtils.isNotBlank(username),  // 2. 用户名非空才加条件
              User::getUsername, username)
        .list();
    // 3. 每条都脱敏后再返回
    return ResultUtils.success(list.stream().map(userService::getSafetyUser).toList());
}
```

**根据**:返回前脱敏,密码等敏感字段才不会泄露到前端——这条规矩在每个返回用户的接口都成立。

## 三、删除用户

**结论**:删除走**逻辑删除**,不真删。

```java
@PostMapping("/delete")
public BaseResponse<Boolean> delete(@RequestBody long id, HttpServletRequest request) {
    ensureAdmin(request);                        // 先鉴权
    boolean ok = userService.removeById(id);     // MyBatis-Plus 自动转成 isDelete=1
    return ResultUtils.success(ok);
}
```

**根据**:配好逻辑删除后,`removeById` 实际执行的是 `UPDATE ... SET isDelete = 1`,数据还在、可恢复(见 [数据库设计](/project/database))。

## 四、前端怎么配合

**结论**:前端用全局状态记住当前用户角色,**非管理员根本看不到管理菜单**。

**根据**:前端隐藏 + 后端鉴权是"双保险"。前端隐藏是体验(普通用户看不到入口),后端鉴权是安全(就算硬拼 URL 也进不来)。**只靠前端隐藏是危险的**,真正的闸门在后端。

**例子**:用 [Ant Design Pro 的权限机制](/stack/ant-design),配置 `access: 'canAdmin'`,角色不符的路由直接不渲染。

## 本章小结

- **结论**:用户管理只做查询和删除,每个接口先鉴权;
- **根据**:不鉴权就越权;返回脱敏、删除走逻辑删;
- **例子**:前端隐藏菜单 + 后端校验角色,双保险缺一不可。

## 对应资源

- 实战:[认证模块](/project/auth) · [数据库设计](/project/database) · [后端架构](/project/backend)
- 技术卡:[Spring Security](/stack/spring-security) · [Ant Design 5](/stack/ant-design)
