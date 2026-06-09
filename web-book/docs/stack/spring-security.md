# Spring Security

> **结论先行**:Spring Security 是 Spring 生态的**保安**——管两件事:**确认你是谁(认证)**和**决定你能干什么(授权)**。在用户中心,它负责安全地存密码、并拦住没权限的请求。

## 一句话

| 项 | 值 |
| --- | --- |
| 定位 | 认证与授权框架 |
| 本书版本 | 随 Spring Boot 3.x(`spring-boot-starter-security`) |
| 在用户中心的角色 | BCrypt 存密码 + 接口鉴权 |

## 解决什么问题

**结论**:没有它,你得自己手写"密码怎么加密、请求怎么鉴权、谁能访问哪个接口"——既容易写错,又容易留下安全漏洞。

**根据**:安全是最不该自己造轮子的地方。一个细节没考虑到(比如盐没随机、鉴权能绕过)就是事故。Spring Security 把这些经过验证的做法打包好了。

**例子**:它直接给你一个 `BCryptPasswordEncoder`,你不用研究 BCrypt 的实现细节,`encode` 和 `matches` 两个方法就够用。

## 依赖关系

**结论**:它挂在 Spring Boot 上,被业务的认证逻辑依赖。

```
Spring Boot
  └─ 依赖 spring-boot-starter-security
       ├─ 提供 BCryptPasswordEncoder  ← 认证模块用它存/验密码
       └─ 提供 过滤器链(Filter Chain) ← 每个请求先过它,做鉴权
```

- **它依赖谁**:Spring Boot / Spring 核心。
- **谁依赖它**:[认证模块](/project/auth) 的密码处理、以及所有需要鉴权的接口。

## 版本与生命周期

**结论**:跟着 Spring Boot 3 走,配置写在一个 `SecurityFilterChain` Bean 里。

```java
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())               // 前后端分离,关掉 CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/user/login", "/api/user/register").permitAll()
                .anyRequest().authenticated());          // 其余接口需登录
        return http.build();
    }
}
```

::: tip Spring Boot 2 → 3 的写法变了
老版本用继承 `WebSecurityConfigurerAdapter` 配置,3.x 已移除,改成上面这种"注册 Bean"的写法。网上搜到老代码时注意区分。
:::

## 在用户中心里的角色

**结论**:它干两件具体的事。

1. **存密码**:注册时 `passwordEncoder.encode()`,登录时 `passwordEncoder.matches()`(见 [认证模块](/project/auth));
2. **拦请求**:登录/注册放行,其余接口要求已登录;管理员接口再加一层角色校验(见 [用户管理](/project/user-management))。

## 对应资源

- 实战:[认证模块](/project/auth) · [用户管理](/project/user-management)
- 相关卡:[Spring Boot](/stack/spring-boot) · [Redis](/stack/redis)
