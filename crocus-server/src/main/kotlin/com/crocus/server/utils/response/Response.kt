package com.crocus.server.utils.response

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Response(val code: Int, val message: String) {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 操作失败
     */
    FAIL(500, "操作失败"),

    /**
     * 上传成功
     */
    UPLOAD_SUCCESS(11010, "上传成功"),

    /**
     * 上传失败
     */
    UPLOAD_FAILED(11011, "上传失败"),

    /**
     * 下载失败
     */
    DOWNLOAD_FAILED(11012, "下载失败"),

    /**
     * 生成文件 URL 失败
     */
    FILE_URL_GENERATOR_FAILED(11013, "生成文件 URL 失败"),

    /**
     * 文件删除成功
     */
    FILE_DELETE_SUCCESS(11021, "文件删除成功"),

    /**
     * 文件删除失败
     */
    FILE_DELETE_FAILED(11022, "文件删除失败"),

    /** ---------------------------------------------------------------------------- */

    /**
     * 请求处理错误
     */
    ERROR_REQUEST(400, "请求处理错误"),

    /**
     * 资源未找到 || 请求地址错误
     */
    NOT_FOUND(404, "资源未找到 || 请求地址错误"),

    /**
     * 请求过于频繁
     */
    ERROR_TOO_MANY_REQUEST(429, "请求过于频繁"),

    /**
     * 请求参数错误
     */
    ERROR_PARAMETER(400, "请求参数错误"),

    /**
     * 请求头错误
     */
    ERROR_HEADER(400, "请求头错误"),

    /**
     * 请求体错误
     */
    ERROR_BODY(400, "请求体错误"),

    /**
     * 请求格式错误
     */
    ERROR_FORM(400, "请求格式错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 服务器维护
     */
    UNAVAILABLE(503, "服务器维护"),

    /**
     * 文件大小超过最大限制
     */
    FILESIZE_ERROR(400, "文件大小超过最大限制"),

    /**
     * 重试成功
     */
    RETRY_SUCCESS(10501, "重试成功"),

    /**
     * 无法重试
     */
    CANT_RETRY(10502, "无法重试"),


    /** ---------------------------------------------------------------------------- */

    /**
     * 此用户已存在
     */
    USER_IS_EXISTED(10012, "此用户已存在"),

    /**
     * 此邮箱已注册
     */
    EMAIL_IS_EXISTED(10013, "此邮箱已注册"),

    /**
     * 此用户的邮箱未验证
     */
    USER_IS_NOT_ACTIVATED(10014, "此用户的邮箱未验证, 请验证"),

    /**
     * 此用户不存在
     */
    USER_NOT_EXISTED(10015, "此用户不存在"),

    /**
     * UUID为空
     */
    HAS_NO_UUID(10016, "UUID为空"),

    /**
     * 不要串改请求数据
     */
    DO_NOT_CHANGE_DATA(10017, "不要串改请求数据"),

    /**
     * 数据已过期
     */
    DATA_NOT_AVAILABLE(10018, "数据已过期"),

    /**
     * 用户数据不匹配
     */
    USER_DATA_NOT_MATCH(10019, "用户数据不匹配"),

    /** ---------------------------------------------------------------------------- */

    /**
     * 邮件已发送
     */
    EMAIL_SENT_SUCCESS(10020, "邮件已发送"),

    /**
     * 邮件发送失败
     */
    EMAIL_SENT_ERROR(10021, "邮件发送失败"),

    /**
     * 请验证邮箱
     */
    REQUIRED_EMAIL(10022, "请验证邮箱"),

    /**
     * 请验证Auth
     */
    REQUIRED_AUTH(100025, "请验证Auth"),

    /**
     * 验证码不见啦
     */
    WHERE_THE_CODE(10026, "验证码不见啦"),

    /**
     * 验证码不匹配
     */
    CODE_NOT_MATCHING(10027, "验证码不匹配"),


    /**
     * 没有绑定身份验证器
     */
    NOT_BIND_AUTH(10027, "没有绑定身份验证器"),

    /**
     * 身份验证器重制成功
     */
    AUTH_RESET_SUCCESS(10028, "身份验证器重制成功"),

    /**
     * 身份验证器 创建失败
     */
    AUTH_GENERATE_ERROR(10029, "URL 创建失败"),

    /**
     * 身份验证器 绑定成功
     */
    AUTH_BIND_SUCCESS(10030, "AUTH_BIND_SUCCESS"),

    /** ---------------------------------------------------------------------------- */

    /**
     * 账户为空
     */
    ACCOUNT_IS_NULL_OR_BLACK(10031, "账户为空"),

    /**
     * 邮箱为空
     */
    EMAIL_IS_NULL_OR_BLACK(10032, "邮箱为空"),

    /**
     * 账户已被删除
     */
    ACCOUNT_IS_DELETED(10033, "账户已被删除"),

    /**
     * 账户已被禁用
     */
    ACCOUNT_IS_BANDED(10034, "账户已被禁用"),

    /**
     * 账户错误
     */
    ACCOUNT_IS_WRONG(10035, "账户错误"),

    /**
     * 账户已存在
     */
    ACCOUNT_IS_EXISTED(10036, "账户已存在"),


    /** ---------------------------------------------------------------------------- */

    /**
     * 密码为空
     */
    PASSWORD_IS_NULL_OR_BLACK(10041, "密码为空"),

    /**
     * 密码不对
     */
    PASSWORD_IS_WRONG(10042, "密码不正确"),

    /**
     * 密码不能与旧密码相同
     */
    PASSWORD_IS_NOT_CHANGE(10043, "密码不能与旧密码相同"),

    /**
     * 账户没有变化
     */
    ACCOUNT_IS_NOT_CHANGE(10044, "账户没有变化"),

    /**
     * 邮箱没有变化
     */
    EMAIL_IS_NOT_CHANGE(10045, "邮箱没有变化"),

    /**
     * 账户不能和密码一致
     */
    ACCOUNT_EQUALS_PASSWORD(10046, "账户不能和密码一致"),

    /**
     * 密码长度小于8
     */
    PASSWORD_LENGTH_UNDER_EIGHT(10047, "密码长度小于8"),

    /**
     * 账户长度小于8
     */
    ACCOUNT_LENGTH_UNDER_EIGHT(10048, "账户长度小于8"),

    /**
     * 验证码长度
     */
    CODE_LENGTH_ERROR(10049, "验证码长度不正确"),

    /** ---------------------------------------------------------------------------- */

    /**
     * 只能包含英文字母、数字、@、- 和 _ 符号
     */
    REGEX_NOT_MATCH(10061, "只能包含英文字母、数字、@、- 和 _ 符号"),

    /**
     * 邮箱格式不正确
     */
    EMAIL_REGEX_NOT_MATCH(10062, "邮箱格式不正确"),

    /**
     * 不允许数据库关键字
     */
    NOT_ALLOW_INCLUDE_SQL_KEYWORDS(10063, "不允许数据库关键字"),

    /** ---------------------------------------------------------------------------- */

    /**
     * 注册成功
     */
    REGISTER_SUCCESS(30001, "注册成功"),

    /**
     * 注册失败
     */
    REGISTER_FAIL(30002, "注册失败"),

    /**
     * 登陆成功
     */
    LOGIN_SUCCESS(30003, "登陆成功"),

    /**
     * 登陆失败
     */
    LOGIN_FAIL(30004, "登陆失败"),

    /**
     * 注销成功
     */
    MARK_SUCCESS(30005, "注销成功"),

    /**
     * 注销失败
     */
    MARK_FAIL(30006, "注销失败"),

    /** ---------------------------------------------------------------------------- */

    /**
     * TO JSON 错误
     */
    TO_JSON_ERROR_CAUSE_STRING(40000, "JSON 序列化错误 cause: String"),

    /**
     * JSON 序列化错误
     */
    JSON_SERIALIZATION_ERROR(40000, "JSON 序列化错误"),

    /**
     * JSON 转换错误
     */
    JSON_DESERIALIZATION_ERROR(40001, "JSON 反序列化错误"),

    /**
     * 删除失败
     */
    DELETE_AGREEMENT_NOT_CORRECT(40102, "删除同意声明错误!!!"),

    /**
     * 删除失败
     */
    DELETE_FAIL_CAUSE_ROLE(40103, "删除失败! 不允许删除管理员"),

    /**
     * 查询失败
     */
    QUERY_FAIL(40601, "查询失败"),

    /**
     * 新增失败
     */
    INSERT_FAIL(40602, "新增失败"),

    /**
     * 修改失败
     */
    UPDATE_FAIL(40603, "修改失败"),

    /**
     * 删除失败
     */
    DELETE_FAIL(30604, "删除失败"),

    /**
     * 新增成功
     */
    INSERT_SUCCESS(40801, "新增成功"),

    /**
     * 修改成功
     */
    UPDATE_SUCCESS(40802, "修改成功"),

    /**
     * 删除成功
     */
    DELETE_SUCCESS(40803, "删除成功"),

    /**
     * 获取列表失败
     */
    ERROR_GET_LIST(41003, "获取列表失败"),


    /** ---------------------------------------------------------------------------- */

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(50001, "服务器内部错误"),

    /**
     * IO 错误
     */
    IO_ERROR(50002, "IO 错误"),

    /**
     * 未知错误
     */
    SYSTEM_ERROR(50003, "未知错误"),

    /**
     * 未找到类
     */
    CLASS_NOT_FOUND_ERROR(50004, "未找到类"),

    /**
     * 空指针异常
     */
    NULL_POINTER_ERROR(50005, "空指针异常"),

    /**
     * Redis连接失败（服务不可达、端口错误等）
     */
    REDIS_CONNECTION_FAIL(50010, "Redis 连接失败"),

    /**
     * Redis操作超时
     */
    REDIS_TIMEOUT(50011, "Redis 操作超时"),

    /**
     * Redis系统异常（未捕获的系统级错误）
     */
    REDIS_SYSTEM_ERROR(50012, "Redis 系统异常"),

    /**
     * Redis命令执行失败（如权限不足、语法错误）
     */
    REDIS_COMMAND_ERROR(50013, "Redis 命令执行失败"),

    /**
     * Redis API 使用错误（非 Redis 协议错误）
     */
    REDIS_API_MISUSE(50014, "Redis API 使用错误"),

    // 数据库操作错误
    DATABASE_ERROR(60000, "数据库操作异常"),
    DATABASE_CONFLICT(60001, "违反唯一约束"),
    DATABASE_ACCESS_ERROR(60002, "数据库访问异常"),
    DATABASE_TRANSACTION_ERROR(60003, "数据库事务异常"),
    DATABASE_TYPE_ERROR(60004, "数据库字段类型不匹配"),
    DATABASE_DATA_NOT_FOUND(60005, "数据不存在"),
    DATABASE_SYNTAX_ERROR(60006, "SQL语法错误"),
    DATABASE_BINDING_ERROR(60007, "参数或结果绑定失败"),
    TOO_MANY_RESULT_ERROR(60008, "结果太多"),
    DATABASE_EXECUTION_ERROR(60009, "SQL 执行异常"),

    // 邮箱
    /**
     * 邮件认证失败（如账号、密码错误）
     */
    MAIL_AUTH_FAIL(60100, "邮件认证失败"),

    /**
     * 邮件发送失败（如 SMTP 服务不可用、连接失败）
     */
    MAIL_SEND_FAIL(60101, "邮件发送失败"),

    /**
     * 邮件格式错误（如标题/正文/附件设置异常）
     */
    MAIL_FORMAT_ERROR(60102, "邮件格式错误"),

    /**
     * 邮件地址非法（如邮件地址语法错误）
     */
    MAIL_ADDRESS_ERROR(60103, "邮件地址非法"),

    /**
     * 邮件部分收件人发送失败
     */
    MAIL_PARTIAL_FAIL(60104, "部分收件人发送失败"),

    /**
     * 邮件连接失败
     */
    MAIL_CONNECTION_ERROR(60105, "邮件服务连接失败"),

    /**
     * 邮件服务系统异常（未知错误）
     */
    MAIL_SYSTEM_ERROR(60106, "邮件服务系统异常"),


    /**
     * MinIO 错误响应（如对象不存在）
     */
    MINIO_ERROR_RESPONSE(60200, "MinIO 错误响应"),

    /**
     * MinIO 数据不完整
     */
    MINIO_INSUFFICIENT_DATA(60201, "MinIO 数据不完整"),

    /**
     * MinIO 内部异常
     */
    MINIO_INTERNAL_ERROR(60202, "MinIO 内部异常"),

    /**
     * MinIO 响应解析失败
     */
    MINIO_XML_PARSE_ERROR(60203, "MinIO 响应解析失败"),


    JWT_EXPIRED(60300, "JWT Token 已过期"),
    JWT_FORMAT_ERROR(60301, "JWT Token 格式错误"),
    JWT_SIGNATURE_ERROR(60302, "JWT Token 签名验证失败"),
    JWT_PROCESSING_ERROR(60303, "JWT 处理异常"),

    ALREADY_DELETED(60401, "已被删除"),
    CASE_ERROR(60402, "转换错误"),
    FUNCTION_PARAMETER_ERROR(60403, "方法参数错误"),

}