# 数据库设计

> **结论先行**:用户中心的数据库只有一张核心表——**user 表**。设计它时守住三条:**敏感字段够长、不真删只标记、每张表都带时间戳**。守住这三条,后面的代码和扩展都顺。

## 一、设计一张表要回答什么

**结论**:设计表 = 回答四个问题——有哪些字段、什么类型、要不要索引、和别的表怎么关联。

**根据**:这四个问题分别对应"存什么、占多大、查得快不快、数据之间什么关系"。想清楚再建表,比建完再改省事得多。

**例子**:用户中心目前只有一张 user 表,所以"表关联"暂时不涉及;重点在字段、类型和索引。

## 二、user 表字段

**结论**:user 表分四组字段——身份、个人信息、业务、系统。

```sql
CREATE TABLE user (
  -- 身份
  id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
  userAccount  VARCHAR(256)  COMMENT '登录账号',
  userPassword VARCHAR(512)  NOT NULL COMMENT '密码(BCrypt 密文)',
  -- 个人信息
  username     VARCHAR(256)  COMMENT '昵称',
  avatarUrl    VARCHAR(1024) COMMENT '头像链接',
  gender       TINYINT       COMMENT '性别',
  phone        VARCHAR(128)  COMMENT '电话',
  email        VARCHAR(256)  COMMENT '邮箱',
  -- 业务
  planetCode   VARCHAR(512)  COMMENT '星球编号(身份标识)',
  userRole     TINYINT  DEFAULT 0 COMMENT '角色:0 普通用户 / 1 管理员',
  userStatus   INT      DEFAULT 0 COMMENT '状态:0 正常',
  -- 系统
  createTime   DATETIME  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updateTime   DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  isDelete     TINYINT   DEFAULT 0 COMMENT '逻辑删除:0 有效 / 1 已删'
) COMMENT '用户表';
```

## 三、三条必须守住的设计原则

### 1. 敏感/不定长字段留足长度

**结论**:`userPassword` 用 `VARCHAR(512)`,远大于明文密码的长度。

**根据**:存进去的不是明文,而是 BCrypt 哈希串(`$2a$10$...`),长度固定但不短;留足空间,换更强的算法时也不用改表结构。

### 2. 不真删,只标记(逻辑删除)

**结论**:删用户不执行 `DELETE`,而是把 `isDelete` 置 1。

**根据**:数据删了就找不回,还可能牵连关联数据。逻辑删除既能"看起来删了",又保留了数据用于审计和恢复。

**例子**:[MyBatis-Plus](/stack/mybatis-plus) 配好逻辑删除后,查询会**自动**只返回 `isDelete = 0` 的行,你写代码时甚至感觉不到它的存在。

### 3. 每张表都带时间戳

**结论**:`createTime` 和 `updateTime` 是标配,几乎每张业务表都该有。

**根据**:它们几乎零成本(数据库自动维护),却在排查问题、做审计、按时间排序时反复救命。

## 四、索引:让查询变快

**结论**:给"经常用来查"的字段加索引,给"区分度低"的字段别加。

| 字段 | 加索引? | 为什么 |
| --- | --- | --- |
| `userAccount` | ✅ 加(唯一索引) | 登录时频繁按它查,且必须唯一 |
| `planetCode` | ✅ 加(唯一索引) | 校验时按它查,且不能重复 |
| `gender` | ❌ 不加 | 只有几种值,区分度太低,索引几乎没用 |

**根据**:索引像书的目录——能加速"查找",但维护它有成本。给只有两三种取值的字段(如性别)加索引,既占空间又提速有限,得不偿失。

**例子**:登录时 `WHERE userAccount = ?`,有了 `userAccount` 唯一索引,数据库直接定位到那一行,不用全表扫描。

## 五、为什么主键用自增 BIGINT

**结论**:用 `BIGINT AUTO_INCREMENT`,不用 UUID。

**根据**:自增整数占用小、做索引快、可读性好。UUID 虽然全局唯一,但长、无序、做主键索引效率低。`BIGINT` 比 `INT` 范围大得多,不怕用户量涨上去溢出。

## 本章小结

- **结论**:一张 user 表,守住"长度够、逻辑删、带时间戳"三条;
- **根据**:这三条分别保住了安全余量、数据可恢复、可追溯;
- **例子**:`userAccount` 加唯一索引让登录飞快,`gender` 不加索引避免浪费。

## 对应资源

- 实战:[需求分析](/project/requirements) · [认证模块](/project/auth)
- 技术卡:[MySQL 8](/stack/mysql) · [MyBatis-Plus](/stack/mybatis-plus)
- 文档:[数据设计说明书怎么写](/docs-workshop/)
