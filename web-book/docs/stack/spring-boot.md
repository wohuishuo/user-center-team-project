# Spring Boot

> ★ 技术卡样板。新增任何技术卡,请照这五段式来写:**是什么 / 解决什么问题 / 依赖关系 / 版本与生命周期 / 在用户中心里的角色**。

## 一句话

Spring Boot 是 Java 生态里**最主流的后端框架**,让你"开箱即用"地快速搭出一个 Web 服务,而不用手动配置一大堆 XML。

| 项 | 值 |
| --- | --- |
| 定位 | 后端应用骨架 / 快速开发框架 |
| 语言 | Java(本书用 Java 21 LTS) |
| 本书版本 | Spring Boot **3.x** |
| 官网 | spring.io/projects/spring-boot |

## 解决什么问题

在 Spring Boot 出现之前,搭一个 Spring 项目要手写大量配置:配置 web 容器、配置数据源、整合各种框架……繁琐且容易出错。

Spring Boot 用三招解决:

1. **自动配置(Auto-configuration)**:它会根据你引入的依赖,自动猜测并完成配置。引入了 MySQL 驱动?它自动帮你配好数据源。
2. **起步依赖(Starter)**:`spring-boot-starter-web` 一个依赖,就把做 Web 服务需要的一整套(Spring MVC、内嵌 Tomcat、JSON 处理)都带齐了。
3. **内嵌服务器**:自带 Tomcat,`java -jar app.jar` 就能跑,不用单独装服务器。

> 没有它会怎样:你要花几天配环境、整合框架;有了它,几分钟就有一个能跑的服务。

## 依赖关系

```
Spring Boot 3
  ├─ 构建在 ── Spring Framework 6(核心:依赖注入 IoC)
  ├─ 包含 ──── Spring MVC(处理 HTTP 请求,提供 RESTful 接口)
  ├─ 内嵌 ──── Tomcat(Web 服务器)
  └─ 整合 ──── 各种 starter:
                ├─ spring-boot-starter-web        (Web 接口)
                ├─ spring-boot-starter-security    (鉴权 → Spring Security)
                ├─ spring-boot-starter-data-redis  (Redis)
                └─ mybatis-plus-boot-starter       (数据库 → MyBatis-Plus)
```

- **它依赖谁**:底层是 Spring Framework(依赖注入)。
- **谁依赖它 / 跟谁配合**:几乎所有后端组件都通过 starter 挂在它上面——[Spring Security](/stack/spring-security)、[MyBatis-Plus](/stack/mybatis-plus)、[Redis](/stack/redis) 全都是它的"插件"。

::: tip 它就是后端的"主板"
可以把 Spring Boot 理解成电脑主板:CPU(Spring 核心)、各种插槽(starter)都在它上面。你换内存条(换数据库)、加显卡(加缓存),主板不变,改的是插上去的卡。
:::

## 版本与生命周期

| 版本 | JDK 要求 | 状态(2026 视角) |
| --- | --- | --- |
| Spring Boot 2.x | Java 8+ | ⚠️ 已停止维护,2022 笔记用的是这个 |
| Spring Boot 3.x | **Java 17+**(本书用 21) | ✅ 当前主流,基于 Jakarta EE 9+ |

::: warning 2.x → 3.x 不是平滑升级
最大的坑是 **`javax.*` 全部改成了 `jakarta.*`**(命名空间迁移)。从 2022 的 Spring Boot 2 升到 3,所有 `javax.servlet`、`javax.persistence` 的 import 都要改成 `jakarta.*`。这是本书统一用 3.x 全新写、而非"升级老代码"的原因之一。
:::

## 在用户中心里的角色

Spring Boot 是用户中心**整个后端的骨架**。具体承担:

- 提供 `/api/user/**` 这些 RESTful 接口(配合 Spring MVC);
- 通过 `application.yml` 统一配置端口、数据库、`/api` 前缀;
- 用自动配置把 [Spring Security](/stack/spring-security)、[MyBatis-Plus](/stack/mybatis-plus)、Redis 串起来;
- 打包成一个 `app.jar`,丢进 [Docker](/stack/docker) 就能跑。

最小启动类:

```java
@SpringBootApplication
public class UserCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }
}
```

关键配置(`application.yml`):

```yaml
server:
  port: 8080
  servlet:
    context-path: /api    # 所有接口统一加 /api 前缀
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_center
    username: root
    password: ${DB_PASSWORD}   # 用环境变量,别写死
```

## 对应资源

- 实战:[后端架构与分层](/project/backend) · [认证模块](/project/auth)
- 相关卡:[Spring Security](/stack/spring-security) · [MyBatis-Plus](/stack/mybatis-plus)
- 理论:[第3章 软件设计基础](/theory/03-design)(分层架构)
