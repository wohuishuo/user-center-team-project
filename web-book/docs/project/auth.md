# 认证模块:注册与登录

> **结论先行**:做认证只需守住两条铁律——**密码用 BCrypt 存、登录态用 JWT**。守住这两条,注册登录就既能用又安全。

## 这一章做什么

实现用户中心最核心的两个接口:

- **注册** `POST /api/user/register`
- **登录** `POST /api/user/login`

重点讲清两处安全要害:**密码用 BCrypt**、**登录态用 JWT**。

## 一、接口设计

| 接口 | 方法 | 路径 | 请求体 | 返回 |
| --- | --- | --- | --- | --- |
| 注册 | POST | `/api/user/register` | `{userAccount, userPassword, checkPassword, planetCode}` | 用户 id |
| 登录 | POST | `/api/user/login` | `{userAccount, userPassword}` | `{token, user(脱敏)}` |

::: tip 为什么用 POST 而不是 GET
密码是敏感信息。GET 的参数会出现在 URL 里,可能被浏览器历史、服务器日志、代理记录下来。POST 把数据放在请求体里,相对安全。**凡是带密码/隐私的请求都用 POST。**
:::

## 二、密码用 BCrypt 存

**结论:密码用 `BCryptPasswordEncoder` 加密后存,永远不存明文,也不用 MD5 这类快哈希。**

```java
// Spring Security 提供的 BCryptPasswordEncoder
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // 默认强度 10
}

// 注册时:加密后再存库
String hash = passwordEncoder.encode(rawPassword);
// 存进数据库的是类似:$2a$10$N9qo8uLOickgx2ZMRZoMy...(同一密码每次都不同)

// 登录时:用 matches 比对,不是把输入再加密后比字符串
boolean ok = passwordEncoder.matches(rawPassword, hashFromDb);
```

**根据:BCrypt 是专门为存密码设计的,做对了三件事。**

| BCrypt 的特性 | 带来的好处 |
| --- | --- |
| **每条记录自动生成独立随机盐** | 相同密码也得到不同哈希,彩虹表失效 |
| **故意设计得慢(工作因子可调)** | 正常登录无感,但逐个暴力破解的成本高到不可承受 |
| 盐直接编码进哈希串 | 不用额外存盐,校验时自动取出 |

::: tip 一个反直觉的点:慢是优点
普通算法追求快,密码哈希反而追求"适度慢"——慢到正常登录只花几十毫秒无感知,却让黑客拿到库后逐个爆破变得不现实。这个"慢"的程度叫 **work factor(工作因子)**,可以随硬件变快而调高。
:::

## 三、注册逻辑

```
1. 校验参数
   ├─ 非空:account / password / checkPassword / planetCode
   ├─ account 长度 ≥ 4,无特殊字符
   ├─ password 长度 ≥ 8
   └─ password == checkPassword
2. 校验唯一性(查数据库)
   ├─ account 不能重复
   └─ planetCode 不能重复
3. 用 BCrypt 加密密码
4. 插入数据库
5. 返回新用户 id
```

参考实现(Spring Boot 3 + MyBatis-Plus):

```java
@Override
public long userRegister(String account, String password,
                         String checkPassword, String planetCode) {
    // 1. 参数校验
    if (StringUtils.isAnyBlank(account, password, checkPassword, planetCode)) {
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
    }
    if (account.length() < 4) {
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短");
    }
    if (password.length() < 8) {
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短");
    }
    if (!password.equals(checkPassword)) {
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
    }
    // 账号不含特殊字符
    if (!account.matches("^[a-zA-Z0-9_]+$")) {
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号含特殊字符");
    }
    // 2. 唯一性校验
    Long count = this.lambdaQuery().eq(User::getUserAccount, account).count();
    if (count > 0) {
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
    }
    // 3. BCrypt 加密
    String encrypted = passwordEncoder.encode(password);
    // 4. 插入
    User user = new User();
    user.setUserAccount(account);
    user.setUserPassword(encrypted);
    user.setPlanetCode(planetCode);
    this.save(user);
    // 5. 返回 id
    return user.getId();
}
```

## 四、登录逻辑与登录态:为什么 JWT 取代 Session

### 登录校验

```java
// 1. 校验参数(同注册的格式校验)
// 2. 查出该账号的用户
User user = this.lambdaQuery().eq(User::getUserAccount, account).one();
if (user == null) {
    throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码错误");
}
// 3. BCrypt 比对(注意:不是把输入再加密后比字符串,而是用 matches)
if (!passwordEncoder.matches(password, user.getUserPassword())) {
    throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码错误");
}
// 4. 生成 JWT,返回脱敏用户 + token
String token = jwtUtils.createToken(user.getId(), user.getUserRole());
return new LoginResult(token, getSafetyUser(user));
```

::: warning 安全细节:错误提示不要太"诚实"
账号不存在和密码错误,都返回**同一句**"账号或密码错误"。如果分开提示"账号不存在"/"密码错误",等于告诉攻击者哪些账号是真实存在的,方便他们针对性爆破。
:::

**结论:登录态用 JWT,把它做成"无状态"的,这样后端能部署多台机器。**

JWT(JSON Web Token)把登录信息编码进一个**带签名的字符串**,发给前端保存。之后每次请求带上它,后端**验签**就知道是谁,服务器自己不用存任何会话:

```
登录成功
  → 后端生成 JWT:header.payload.signature
    payload 里放 { userId, role, 过期时间 }
    signature 用服务器密钥签名,防篡改
  → 返回给前端,前端存起来
  → 之后每次请求头带 Authorization: Bearer <token>
  → 后端验签 + 解出 userId,即可识别用户
```

**根据:无状态才能水平扩展。** 如果把登录信息存在某一台服务器的内存里,一旦部署多台机器做负载均衡,用户在 A 机登录、下次请求被转到 B 机,B 机不认识他,就"掉线"了。JWT 自带身份,任意机器都能验,天然支持多机。

::: tip 另一种选择:Redis 共享 Session
如果更想用传统 Session 模型,可以用 **Spring Session + Redis**:把会话存到独立的 Redis 而不是单机内存,多台机器共享同一份会话,同样能水平扩展。JWT 与 Redis Session 各有取舍,详见 [Redis 技术卡](/stack/redis)。
:::

## 五、用户信息脱敏

返回给前端的用户对象,**绝不能包含密码等敏感字段**:

```java
private User getSafetyUser(User origin) {
    User safety = new User();
    safety.setId(origin.getId());
    safety.setUserAccount(origin.getUserAccount());
    safety.setUsername(origin.getUsername());
    safety.setAvatarUrl(origin.getAvatarUrl());
    safety.setUserRole(origin.getUserRole());
    // 注意:不复制 userPassword!
    return safety;
}
```

## 六、对应的测试

需求"密码 ≥8 位"直接对应一条单元测试:

```java
@Test
void register_shouldReject_whenPasswordTooShort() {
    assertThrows(BusinessException.class, () ->
        userService.userRegister("alice", "123", "123", "10086"));
}
```

完整测试思路见 [测试](/project/testing)。

## 本章小结

- **密码用 BCrypt**:自带独立盐 + 慢哈希,彻底取代不安全的 MD5;
- **登录态用 JWT**(或 Redis Session):无状态,支持多机部署;
- **错误提示要模糊**,不泄露账号是否存在;
- **返回必须脱敏**,密码永不出库。

## 对应资源

- 理论:[第2章 需求工程](/theory/02-requirements)(登录用例)
- 技术卡:[Spring Security](/stack/spring-security) · [Redis](/stack/redis)
- 文档:[系统设计说明书怎么写](/docs-workshop/)
