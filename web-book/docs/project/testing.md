# 测试

> **结论先行**:测试不是上线前的最后一道手续,而是**把需求一条条变成可自动验证的断言**。需求写"密码 ≥8 位",测试就有一条"输入 7 位应被拒绝"。

## 一、为什么要写测试

**结论**:测试让"它能用"从一句口头保证,变成一条**随时能自动重跑的证明**。

**根据**:人工点点点,改一次代码就要全点一遍,既慢又会漏。自动化测试一秒跑完,改完立刻知道有没有碰坏旧功能。

**例子**:你给认证模块加了新校验,跑一下测试,绿了就说明老的注册登录没被你改坏——这份安心,正是测试的价值。

## 二、三个层次,从小到大

**结论**:测试分三层,粒度从细到粗,作用各不同。

| 层次 | 测什么 | 用户中心例子 | 工具 |
| --- | --- | --- | --- |
| **单元测试** | 单个方法 | `userRegister` 对短密码报错 | JUnit 5 |
| **集成测试** | 多个组件配合 | 注册接口真的写进了数据库 | Testcontainers |
| **系统测试** | 整条用户流程 | 打开页面→注册→登录→进首页 | 浏览器自动化 |

**根据**:遵循"最左原则"——能用单元测试发现的问题,就别留到系统测试。越往左,定位问题越快、修复越便宜。

## 三、单元测试:对着需求写断言

**结论**:每条需求,对应一条测试。

```java
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void register_shouldReject_whenPasswordTooShort() {   // 需求:密码 ≥8 位
        assertThrows(BusinessException.class, () ->
            userService.userRegister("alice", "123", "123", "10086"));
    }

    @Test
    void register_shouldReject_whenPasswordsMismatch() {  // 需求:两次密码一致
        assertThrows(BusinessException.class, () ->
            userService.userRegister("alice", "12345678", "87654321", "10086"));
    }
}
```

**根据**:测试方法名直接写出它验证的需求,谁看都懂。一条需求一条测试,需求和测试就能一一对应、互相追溯。

## 四、集成测试:用真实依赖

**结论**:涉及数据库的测试,用 **Testcontainers** 起一个临时的真 MySQL,而不是假装。

**根据**:用假的内存数据库测,可能测过了上线却挂——因为真 MySQL 的行为不完全一样。Testcontainers 在测试时用 Docker 拉起一个真 MySQL,测完自动销毁,既真实又干净。

**例子**:测"注册后库里真的多了一行",就连真库验证,比只测 Service 返回值更可信。

## 五、前端测试

**结论**:前端用 **Vitest** 测组件逻辑,配合浏览器自动化测关键流程。

**例子**:测"密码框输入 7 位时,提交按钮给出错误提示"——这是前端版的"需求→断言"。

## 本章小结

- **结论**:测试是把需求变成可自动重跑的断言;
- **根据**:三层从细到粗,遵循最左原则;集成测试用真实依赖才可信;
- **例子**:`register_shouldReject_whenPasswordTooShort` 一眼能看出它守的是哪条需求。

## 本章的真实代码

| 内容 | 文件 |
| --- | --- |
| 单元测试(6 条,需求→断言) | [UserServiceTest.java](https://github.com/wohuishuo/user-center-team-project/blob/main/user-center/backend/src/test/java/com/usercenter/service/UserServiceTest.java) |
| 集成测试(MockMvc 全链路 3 条) | [UserApiIntegrationTest.java](https://github.com/wohuishuo/user-center-team-project/blob/main/user-center/backend/src/test/java/com/usercenter/UserApiIntegrationTest.java) |
| 已产出的课程文档 | [软件测试报告.docx](https://github.com/wohuishuo/user-center-team-project/tree/main/文档产出) |

> 运行:`cd user-center/backend && mvn test`(需本地 MySQL 在运行)。9 条全部通过。

## 对应资源

- 理论:[第5章 软件测试](/theory/05-testing)
- 实战:[认证模块](/project/auth) · [后端架构](/project/backend)
- 文档:[软件测试报告怎么写](/docs-workshop/)
