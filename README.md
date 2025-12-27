---

# MyWeb Backend (博客后端项目)

一个基于 **Spring Boot** 开发的博客后端服务。项目采用了经典的分层架构（Controller-Service-Dao），集成了 Spring Security 和 JWT 进行权限认证，使用 MyBatis 处理数据持久化，并实现了文件上传、Markdown 解析等功能。

## 功能特性

* **权限管理**: 集成 Spring Security + JWT，实现用户登录、注册及接口鉴权 (`JwtAuthenticationFilter`)。
* **博客管理**: 文章的增删改查 (CRUD)，支持 Markdown 格式内容的存储与解析 (`MarkdownUtils`)。
* **标签系统**: 文章标签分类管理，支持多对多关联。
* **数据统计**: 仪表盘数据统计功能 (`StatsController`)。
* **文件服务**: 图片/文件上传接口 (`UploadController`)。
* **系统增强**:
* **AOP 日志**: 全局请求日志记录 (`GlobalLogAspect`)。
* **全局异常处理**: 统一处理业务异常与运行时异常 (`GlobalExceptionHandler`)。
* **多环境配置**: 支持 Local、Docker部署、Server裸机部署多环境切换。

## 技术栈

* **开发语言**: Java
* **核心框架**: Spring Boot
* **持久层**: MyBatis + MySQL
* **安全框架**: Spring Security
* **认证机制**: JWT (JSON Web Token)
* **工具库**: Lombok (推测), Jackson

## 目录结构说明

```text
src/main/java/com/example
├── aspect/              # AOP 切面
│   └── GlobalLogAspect.java  # 全局请求日志记录
│
├── config/              # 配置类
│   ├── SecurityConfig.java   # Spring Security 安全配置
│   └── WebConfig.java        # Web MVC 配置 (如跨域)
│
├── controller/          # 控制层 (API 接口)
│   ├── BlogController.java   # 博客接口
│   ├── UserController.java   # 用户接口
│   └── ...                   # 其他接口 (Stats, Tag, Upload)
│
├── exception/           # 异常处理
│   ├── GlobalExceptionHandler.java # 全局异常捕获
│   └── ...                   # 自定义异常类
│
├── filter/              # 过滤器
│   └── JwtAuthenticationFilter.java # JWT 认证过滤器
│
├── mapper/              # 持久层 (DAO)
│   ├── *.java                # MyBatis Mapper 接口
│   └── *.xml                 # MyBatis XML SQL 映射文件
│
├── pojo/                # 实体对象
│   ├── dto/             # 数据传输对象 (前端 -> 后端)
│   ├── entity/          # 数据库实体 (对应数据库表)
│   ├── vo/              # 视图对象 (后端 -> 前端)
│   └── Result.java      # 统一 API 响应格式
│
├── service/             # 业务逻辑层
│   ├── Impl/            # 业务逻辑实现类
│   └── ...              # Service 接口
│
├── utils/               # 工具类
│   ├── JwtUtil.java     # JWT 生成与解析
│   ├── MarkdownUtils.java # Markdown 转换工具
│   └── SecurityUtils.java # 获取当前登录用户信息
│
└── resources/           # 资源文件
    ├── application.yml        # 主配置文件
    ├── application-local.yml  # 本地开发配置
    ├── application-docker.yml # Docker 环境配置
    └── application-server.yml # 生产服务器配置

```

## 环境配置

### 1. 环境准备

* JDK 17
* MySQL 8.0+
* Maven

### 2. 数据库配置

在 MySQL 中创建一个数据库，并导入初始化 SQL 脚本（如果有）。

修改 `src/main/resources/application-local.yml` (或对应激活的配置文件) 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/myblog_db?useSSL=false&serverTimezone=UTC
    username: root
    password: your_password

```

### 3. 运行项目

**使用 IDEA 运行:**
找到 `MywebBackendApplication.java`，右键点击 **Run**。

**使用命令行运行:**

```bash
mvn spring-boot:run

```

项目启动后，默认运行在端口 `8080` (取决于配置)。

### 4. 接口测试

推荐使用 Postman 或 Apifox 进行测试。

* **登录接口**: `POST /login` (获取 Token)
* **需要认证的接口**: 在 Header 中添加 `Authorization: Bearer <your_token>`

## Docker 部署

项目内置了 `application-docker.yml`，方便容器化部署。

1. **打包应用**:
```bash
mvn clean package -DskipTests

```


2. **构建镜像** (需编写 Dockerfile):
```dockerfile
FROM openjdk:17-jdk-alpine
COPY target/myweb-backend.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=docker"]

```


3. **运行容器**:
```bash
docker run -d -p 8080:8080 --name myblog-backend myweb-backend

```


**License**
MIT
