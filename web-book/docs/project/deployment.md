# 部署上线

> **结论先行**:部署就是"让代码在别人也能访问的服务器上跑起来"。用 **Docker Compose** 把前端、后端、数据库、Redis 打包成一条命令启动;用 **GitHub Actions** 让"提交代码"自动变成"上线"。

## 一、部署到底在解决什么

**结论**:部署解决的是"在我电脑上能跑,到服务器上却跑不起来"。

**根据**:本地和服务器的系统、Java 版本、数据库配置常常不一样,差一点就跑不起来。容器把"代码 + 运行环境"打包在一起,搬到哪都一样。

**例子**:你把后端打成一个 Docker 镜像,这个镜像里自带 Java 21,服务器上装没装 Java 都无所谓——镜像里有就行。

## 二、Docker Compose:一条命令起全栈

**结论**:用一个 `docker-compose.yml` 描述四个服务,`docker compose up` 一次全起来。

```yaml
services:
  mysql:
    image: mysql:8
    environment:
      MYSQL_DATABASE: user_center
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
    volumes: [mysql-data:/var/lib/mysql]   # 数据持久化,容器删了数据还在

  redis:
    image: redis:7

  backend:
    build: ./backend          # 用后端目录的 Dockerfile 构建
    depends_on: [mysql, redis]
    environment:
      DB_PASSWORD: ${DB_PASSWORD}
    ports: ["8080:8080"]

  frontend:
    build: ./frontend         # 构建后由 Nginx 托管
    depends_on: [backend]
    ports: ["80:80"]

volumes:
  mysql-data:
```

**根据**:四个服务之间有依赖(后端要等数据库起来),`depends_on` 和一份配置文件把这些关系固定下来,谁来部署都是同一套,不会漏步骤。

::: warning 密码别写死在文件里
`${DB_PASSWORD}` 这种从环境变量读的写法,是为了**不把密码提交进 Git**。密码放在服务器的 `.env` 文件或 CI 的 Secrets 里。
:::

## 三、后端镜像怎么打(多阶段构建)

**结论**:用"两段式"Dockerfile——先用带 Maven 的镜像编译,再把成品 jar 放进只带 Java 的精简镜像。

```dockerfile
# 第一段:编译
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 第二段:运行(镜像更小,不含 Maven 和源码)
FROM eclipse-temurin:21-jre
COPY --from=build /app/target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**根据**:编译要 Maven,运行只要 Java。分两段,最终镜像只留运行所需,体积小、启动快、攻击面也小。

## 四、CI/CD:提交即上线

**结论**:用 **GitHub Actions**,让 push 到 main 自动触发"构建 → 测试 → 部署"。

```
push 到 main
  → GitHub Actions 启动
    → 跑测试(不过就停,不让坏代码上线)
    → 构建 Docker 镜像
    → 部署到服务器
```

**根据**:手动部署步骤多、容易漏、还经常半夜出错。自动化把这条流水线固定下来,每次都一模一样,且测试不过就不部署——这是质量的最后一道闸门。

**例子**:这本网页书自己就用了 GitHub Actions,push 后自动构建发布(配置见 `.github/workflows/deploy.yml`)。

## 本章小结

- **结论**:Docker Compose 一键起全栈,GitHub Actions 让提交自动上线;
- **根据**:容器消除"环境不一致",CI/CD 消除"手动易出错";
- **例子**:多阶段构建让后端镜像只留运行所需,小而稳。

## 对应资源

- 技术卡:[Docker Compose](/stack/docker)
- 协作:[AI 协作工作流](/engineering/ai-workflow) · [软件生命周期](/engineering/lifecycle)
- 实战:[后端架构](/project/backend) · [测试](/project/testing)
