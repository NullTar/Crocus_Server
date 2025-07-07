# Crocus Server

基于 Spring Boot 和 Ktor 构建的后端服务。

这是一个 Spring Boot 和 Kotlin 搭配的简单例子，同时包含 Ktor, 代码并不完整，可以作为参考。

[En](readme_en.md)

---

## 包含的功能

- 用户注册 / 登录 / 注销
- JWT 令牌签发与验证
- 二次验证支持（基于 One-Time Password）
- 使用 MinIO 进行对象存储
- 内容同步至 Elasticsearch, 以提供搜索服务
- Redis 缓存与数据加速,Ehcache 本地缓存优化
- 自定义异常统一处理机制
- 全局响应封装格式（支持 REST 与 JSON）

---

## 🛠️ 技术栈

### language

- Kotlin 2.1.0
- Java 8

### Base

- Logger: io.github.microutils:kotlin-logging
- Build: gradle 8.14

### Spring Boot

- Spring Boot 2.6.13
- MyBatis-Plus 3.5.6
- jjwt 0.12.6
- minio 8.4.6
- elasticsearch 7.5.2
- onetimepassword 2.4.1
- druid 1.2.23

### Ktor

- Ktor 2.3.12
- ehcache 3.10.8

---

## 🧾 项目结构简要

### crocus-server （Spring Boot 后端）

- `controller/`：HTTP 接口控制层，处理前端请求
- `entity/`：数据库实体类、请求/响应 DTO 及其扩展方法
- `enum/`：枚举类型定义
- `mapper/`：MyBatis-Plus 映射层，执行数据库操作
- `service/`：业务逻辑处理
- `utils/`：通用工具类
    - `config/`：核心配置类
    - `database/`：Druid 多数据源配置与封装
    - `elastic/`：Elasticsearch 搜索逻辑封装
    - `email/`：邮件发送支持
    - `encrypt/`：加密解密
    - `exception/`：统一异常处理
    - `files/`：MinIO 文件上传、下载、URL 生成等功能
    - `json/`：JSON 序列化/反序列化封装
    - `log/`：日志记录与过滤器
    - `permission/`：Token 生成, JWT 鉴权
    - `regex/`：正则工具类
    - `response/`：标准化响应体封装
    - `sql/`：SQL 过滤工具
    - `time/`：时间与日期工具
    - `type/`：反序列化辅助
    - `web/`：全局拦截器与请求处理器

### crocus-web （Ktor 接口服务）

- `config/`：Ktor 应用配置
- `route/`：Ktor 路由定义
- `service/`：应用服务层逻辑
- `utils/`：通用工具方法

---

## 启动
在 resources 中配置 application-prod.yml, 或拷贝一份为 application-dev.yml
1. 配置数据库
2. 配置 redis
3. 配置邮件服务
4. 配置 elasticsearch
5. 配置 minio

---

##  License

- ✅ 允许任何人 Fork 本项目用于学习、参考或研究用途。
- 📌 可以商用, 允许复制、粘贴、修改, 禁止直接拷贝的偷懒行为。
- ❌ 禁止直接抄袭、剽窃作为论文或公开成果。
- ❌ 禁止将本项目作为课程作业、比赛作品等进行提交。
- ⚠️ 本项目作为本人论文成果，请勿用于影响本人声誉、评价或学术诚信的行为。
