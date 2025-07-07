# Crocus Server

The back-end service built based on Spring Boot and Ktor.

This is a simple example of the combination of Spring Boot and Kotlin, including Ktor. The code is not complete and can
be used as a reference.

[中文](readme.md)

---

## Features

- User registration login
- JWT Token issuance and verification
- Verification support base on One-Time Password
- Use MinIO for object storage
- The content is synchronized to Elasticsearch to provide search services
- Redis cache and data acceleration,Ehcache local cache optimization
- Exception handling
- Global response format

---

## 🛠️Libraries

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

## 🧾 Project Structure

### crocus-server （Spring Boot backend）

- `controller/`：HTTP interface control layer
- `entity/`：Database entity classes, request/response Dtos and extension methods
- `enum/`：Definition of enumeration type
- `mapper/`：The MyBatis-Plus mapping layer
- `service/`：Logic processing
- `utils/`：Utility Tools
    - `config/`：Configuration
    - `database/`：Druid Multi-data source configuration
    - `elastic/`：Elasticsearch search logic
    - `email/`：Email sending support
    - `encrypt/`：Encryption and decryption
    - `exception/`：Exception Handler
    - `files/`：MinIO File upload、download、URL generate
    - `json/`：JSON serialization、deserialization
    - `log/`：log filter
    - `permission/`：Token Handling
    - `regex/`：Regular expression
    - `response/`：Standardized response body
    - `sql/`：SQL filter
    - `time/`：Data and time
    - `type/`：Deserialization assistance
    - `web/`：Interceptor and filter

### crocus-web （Ktor backend）

- `config/`：Ktor application config
- `route/`：Ktor route
- `service/`：Logic layer
- `utils/`：Utility Tools

---

## Launch

Configure application-prod.yml in resources, or make a copy as application-dev.yml

1. Configure database (Mysql)
2. Configure redis
3. Configure email
4. Configure elasticsearch
5. Configure minio

---

## License

- ✅ Anyone is allowed to fork this project for learning, reference, or research purposes.
- 📌 Commercial use is permitted; copying, pasting, and modification are allowed, but blatant plagiarism by directly
  copying without effort is prohibited.
- ❌ Direct plagiarism or passing this project off as someone else's thesis or published work is forbidden.
- ❌ This project must not be submitted as coursework, competition entries, or similar academic assignments.
- ⚠️ his project is the author's thesis work; it should not be used in ways that may affect the author's reputation,
  academic evaluation, or integrity.

