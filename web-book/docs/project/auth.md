# 认证模块:注册与登录(2026 版)

> ★ 完整样板章节,也是全书最能体现"2022 → 2026 升级"价值的一章。看完你会明白:**为什么不能再用 MD5 存密码、不能再用单机 Session**。

## 这一章做什么

实现用户中心最核心的两个接口:

- **注册** `POST /api/user/register`
- **登录** `POST /api/user/login`

并讲清两处关键的安全升级:**密码用 BCrypt**、**登录态用 JWT**。

## 一、接口设计

| 接口 | 方法 | 路径 | 请求体 | 返回 |
| --- | --- | --- | --- | --- |
| 注册 | POST | `/api/user/register` | `{userAccount, userPassword, checkPassword, planetCode}` | 用户 id |
| 登录 | POST | `/api/user/login` | `{userAccount, userPassword}` | `{token, user(脱敏)}` |

::: tip 为什么用 POST 而不是 GET
密码是敏感信息。GET 的参数会出现在 URL 里,可能被浏览器历史、服务器日志、代理记录下来。POST 把数据放在请求体里,相对安全。**凡是带密码/隐私的请求都用 POST。**
:::

## 二、密码安全:为什么 BCrypt 取代 MD5

这是整个项目**最重要的一处升级**。

### ⚠️ 2022 旧做法的问题

2022 笔记里是这样存密码的:

```java
// ⚠️ 反面教材,不要这样做
final String SALT = "yupi";
String encrypted = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
```

三个致命问题:

1. **MD5 太快**:它是为校验文件设计的快速哈希。2026 的 GPU 每秒能试**上百亿**个 MD5,暴力破解轻而易举;
2. **固定盐 = 没盐**:所有用户用同一个盐 `"yupi"`,相同密码会得到相同哈希,黑客可以预先算好"彩虹表"批量破解;
3. **MD5 已被攻破**:存在已知的碰撞攻击,密码学上不再安全。

> 结论:数据库一旦泄露,用 MD5 存的密码几乎等于明文。

### ✅ 2026 做法:BCrypt

BCrypt 是**专门为存密码设计**的算法:

```java
// Spring Security 提供的 BCryptPasswordEncoder
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // 默认强度 10
}

// 注册时:加密
String hash = passwordEncoder.encode(rawPassword);
// 存进数据库的是类似:$2a$10$N9qo8uLOickgx2ZMRZoMy...(每次都不同)

// 登录时:校验(不解密,而是比对)
boolean ok = passwordEncoder.matches(rawPassword, hashFromDb);
```

BCrypt 解决了 MD5 的全部问题:

| 问题 | BCrypt 怎么解决 |
| --- | --- |
| 太快易爆破 | **故意设计得慢**(强度因子可调),爆破成本极高 |
| 固定盐 | **每条记录自动生成独立随机盐**,盐就存在哈希串里 |
| 彩虹表 | 盐不同 → 相同密码哈希也不同 → 彩虹表失效 |

::: tip 一个反直觉的点:慢是优点
普通算法追求快,密码哈希反而追求"适度慢"。慢到正常登录无感(几十毫秒),但让黑客逐个爆破变得不可承受。这叫 **work factor(工作因子)**,BCrypt 可以调。
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

### ⚠️ 旧做法:Session + Cookie

2022 用 Session 记录登录态:登录成功后 `session.setAttribute("userLoginState", user)`,登录信息存在**那一台服务器的内存里**。

问题:**无法水平扩展**。一旦后端部署多台机器(负载均衡),用户第一次登录在 A 机器,下次请求被转发到 B 机器,B 机器没有这个 Session,用户就"掉线"了。

### ✅ 新做法:JWT(无状态)

JWT(JSON Web Token)把登录信息**编码进一个签名字符串**,发给前端保存。之后每次请求带上它,后端**验签**就知道是谁,不需要在服务器存任何东西:

```
登录成功
  → 后端生成 JWT:header.payload.signature
    payload 里放 { userId, role, 过期时间 }
    signature 用服务器密钥签名,防篡改
  → 返回给前端,前端存 localStorage
  → 之后每次请求头带 Authorization: Bearer <token>
  → 后端验签 + 解出 userId,即可识别用户
```

| 对比 | Session | JWT |
| --- | --- | --- |
| 状态存哪 | 服务器内存 | 客户端(token 自带) |
| 多机部署 | ❌ 需共享 Session | ✅ 任意机器都能验 |
| 注销 | 删 Session 即可 | 需配合黑名单/短过期 |

::: tip 折中方案:Redis 共享 Session
如果团队更想用 Session 模型,也可以用 **Spring Session + Redis**:把 Session 存到独立的 Redis 而非单机内存,这样多台机器共享同一份会话,同样能水平扩展。JWT 和 Redis Session 各有取舍,详见 [Redis 技术卡](/stack/redis)。
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
