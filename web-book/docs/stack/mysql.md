# MySQL 8

> **结论先行**:MySQL 是用户中心的**主数据库**——用户数据最终都可靠地存在它这里。它是关系型数据库,数据按"表"组织,靠**索引**查得快、靠**事务**保证不出错。

## 一句话

| 项 | 值 |
| --- | --- |
| 定位 | 关系型数据库 |
| 本书版本 | MySQL 8 |
| 在用户中心的角色 | 持久化存储 user 表 |

## 解决什么问题

**结论**:它解决"数据怎么可靠地存下来、还能快速查出来"。

**根据**:程序内存里的数据一关机就没了;MySQL 把数据写进硬盘并提供查询能力,还能在并发读写时保证一致(靠事务)。

**例子**:用户注册的信息存进 MySQL,服务器重启、断电再来,数据都还在。

## 依赖关系

**结论**:它是独立服务,后端通过驱动连它。

```
后端(MyBatis-Plus → MyBatis → JDBC)
  └─ 通过网络连接 → MySQL 服务(独立容器)
```

- **它依赖谁**:不依赖业务,自己独立运行。
- **谁依赖它**:[MyBatis-Plus](/stack/mybatis-plus) 经由 JDBC 连它;在 [Docker Compose](/stack/docker) 里作为独立容器。

## 版本与生命周期

**结论**:用官方 `mysql:8` 镜像,数据用 volume 持久化,别随容器一起删。

```yaml
mysql:
  image: mysql:8
  environment:
    MYSQL_DATABASE: user_center
    MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
  volumes: [mysql-data:/var/lib/mysql]   # 关键:数据存在 volume 里
```

::: tip 为什么是 8 而不是 5.7
MySQL 8 默认就是 `utf8mb4`(能存 emoji 等完整 Unicode),还支持窗口函数、CTE 等;5.7 已停止维护。新项目直接上 8。
:::

## 在用户中心里的角色

**结论**:存一张 user 表,守住三件事——索引、事务、字符集。

- **索引**:`userAccount` 加唯一索引,登录查得快(见 [数据库设计](/project/database));
- **事务**:涉及多步写入时,保证"要么都成功,要么都回滚";
- **字符集**:utf8mb4,昵称里有 emoji 也不报错。

## 对应资源

- 实战:[数据库设计](/project/database)
- 相关卡:[MyBatis-Plus](/stack/mybatis-plus) · [Redis](/stack/redis) · [Docker Compose](/stack/docker)
