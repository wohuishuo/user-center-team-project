# 后端架构与分层

> **结论先行**:后端代码按**三层**组织——**Controller 收请求、Service 做业务、Mapper 操作数据库**。每层只干自己那一段,再配上"统一返回"和"全局异常处理"两个公共件,整个后端就清爽了。

## 一、为什么要分层

**结论**:分层是把"一次请求要做的事"切成职责清晰的几段,改一段不影响另一段。

**根据**:这正是[软件设计](/theory/03-design)里说的高内聚低耦合。混在一起写,改个校验逻辑可能碰坏数据库代码;分开后,各层互不干扰。

**例子**:登录请求进来,Controller 只管"接参数",Service 管"比对密码",Mapper 管"查库"。换数据库只动 Mapper,业务逻辑一行不改。

## 二、三层各干什么

```
请求 → [Controller] → [Service] → [Mapper] → 数据库
        接参/校验格式   业务逻辑     SQL/CRUD
```

| 层 | 职责 | 不该干什么 |
| --- | --- | --- |
| **Controller** | 接收请求、校验参数格式、返回结果 | 不写业务逻辑 |
| **Service** | 业务逻辑、事务、调用 Mapper | 不直接碰 HTTP |
| **Mapper** | 操作数据库 | 不写业务判断 |

**根据**:校验放在哪里有讲究——**格式校验**(账号是否为空、够不够长)放 Controller;**业务校验**(账号是否已存在)放 Service,因为它可能被 Controller 之外的代码调用。

**例子**:

```java
// Controller:只接参、只校验"格式"
@PostMapping("/register")
public BaseResponse<Long> register(@RequestBody UserRegisterRequest req) {
    if (req == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
    long id = userService.userRegister(   // 业务交给 Service
        req.getUserAccount(), req.getUserPassword(),
        req.getCheckPassword(), req.getPlanetCode());
    return ResultUtils.success(id);
}
```

## 三、公共件一:统一返回格式

**结论**:所有接口都返回同一个结构——`{ code, data, message }`,前端处理起来才省心。

```java
public class BaseResponse<T> {
    private int code;        // 业务状态码:0 成功
    private T data;          // 业务数据
    private String message;  // 提示信息
}
```

**根据**:如果每个接口各返回各的格式,前端要为每个接口写不同的解析。统一之后,前端一套逻辑通吃。

::: tip 业务状态码 ≠ HTTP 状态码
HTTP 状态码(200/404/500)说的是"网络层面成不成";业务状态码(0/40100…)说的是"业务层面对不对"。两者各管一层,别混用。
:::

## 四、公共件二:全局异常处理

**结论**:业务出错就**抛自定义异常**,由一个全局处理器统一转成标准返回,而不是在每个方法里 try-catch。

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> handle(BusinessException e) {
        return ResultUtils.error(e.getCode(), e.getMessage());
    }
}
```

**根据**:错误处理集中到一处(高内聚),业务代码里只管"抛",不用到处写重复的 catch。

**例子**:Service 里 `throw new BusinessException(PARAMS_ERROR, "账号已存在")`,前端收到的就是规整的 `{ code, message }`,中间不用你操心。

## 五、整体目录骨架

```
src/main/java/com/example/usercenter/
├── controller/   # 接口层
├── service/      # 业务接口
│   └── impl/     # 业务实现
├── mapper/       # 数据库操作
├── model/
│   ├── entity/   # 数据库实体(User)
│   ├── dto/      # 请求参数对象
│   └── vo/       # 返回给前端的对象(脱敏)
├── common/       # BaseResponse、ErrorCode、工具
└── exception/    # BusinessException、全局处理器
```

## 本章小结

- **结论**:Controller / Service / Mapper 三层各司其职;
- **根据**:分层落实高内聚低耦合,格式校验在 Controller、业务校验在 Service;
- **例子**:统一返回 + 全局异常,让前端一套逻辑通吃、业务代码只管抛错。

## 对应资源

- 理论:[软件设计](/theory/03-design)
- 实战:[认证模块](/project/auth) · [用户管理](/project/user-management) · [测试](/project/testing)
- 技术卡:[Spring Boot](/stack/spring-boot) · [MyBatis-Plus](/stack/mybatis-plus)
