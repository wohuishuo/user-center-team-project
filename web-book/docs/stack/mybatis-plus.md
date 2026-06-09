# MyBatis-Plus

> **结论先行**:MyBatis-Plus 让你**不写 SQL 也能完成增删改查**。继承它的接口,常用数据库操作就自动有了;还顺手把逻辑删除、分页这些常见需求一并解决。

## 一句话

| 项 | 值 |
| --- | --- |
| 定位 | 数据持久层框架(MyBatis 的增强) |
| 本书版本 | MyBatis-Plus 3.5+ |
| 在用户中心的角色 | 操作 user 表的全部 CRUD |

## 解决什么问题

**结论**:它解决"简单的增删改查也要手写一堆重复 SQL"的麻烦。

**根据**:查一条、存一条、按 id 删——这些操作每张表都一样。MyBatis-Plus 把它们做成通用方法,你不用为每张表重写一遍。

**例子**:`userService.save(user)`、`userService.getById(1)`、`userService.removeById(1)`——一行搞定,没写一句 SQL。

## 依赖关系

**结论**:它建在 MyBatis 上,MyBatis 建在 JDBC 上,最终连到 MySQL。

```
你的 Service / Mapper
  └─ 依赖 MyBatis-Plus
       └─ 增强 MyBatis
            └─ 封装 JDBC
                 └─ 连接 MySQL
```

- **它依赖谁**:MyBatis → JDBC → 数据库驱动。
- **谁依赖它**:[后端](/project/backend) 的 Mapper 和 Service。

## 版本与生命周期

**结论**:让 Mapper 继承 `BaseMapper`、Service 继承 `IService`,CRUD 方法就自动有了。

```java
// Mapper 继承 BaseMapper,自动拥有 insert/selectById/deleteById...
public interface UserMapper extends BaseMapper<User> {}

// 实体上配逻辑删除
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableLogic               // 标记为逻辑删除字段
    private Integer isDelete;
}
```

配好 `@TableLogic` 后,`removeById` 自动变成 `UPDATE ... SET isDelete=1`,查询自动只返回未删除的行。

## 在用户中心里的角色

**结论**:user 表的所有读写都经它,并自动处理逻辑删除。

**例子**:
- 注册:`save(user)`;
- 登录:`lambdaQuery().eq(User::getUserAccount, account).one()`;
- 删除:`removeById(id)`(自动逻辑删除,见 [数据库设计](/project/database))。

## 对应资源

- 实战:[数据库设计](/project/database) · [后端架构](/project/backend) · [认证模块](/project/auth)
- 相关卡:[MySQL 8](/stack/mysql) · [Spring Boot](/stack/spring-boot)
