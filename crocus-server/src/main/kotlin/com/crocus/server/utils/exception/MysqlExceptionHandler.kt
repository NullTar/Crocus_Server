package com.crocus.server.utils.exception

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import org.apache.ibatis.binding.BindingException
import org.apache.ibatis.exceptions.PersistenceException
import org.apache.ibatis.exceptions.TooManyResultsException
import org.springframework.dao.DataAccessException
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.dao.TypeMismatchDataAccessException
import org.springframework.jdbc.BadSqlGrammarException
import org.springframework.transaction.UnexpectedRollbackException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.DataTruncation
import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException

@RestControllerAdvice
class MysqlExceptionHandler {

    // SQL执行失败
    @ExceptionHandler(SQLException::class)
    fun handleSQLException(): ResponseDTO<*> = toDTO(Response.DATABASE_ERROR)

    // 插入或更新时违反唯一约束
    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKey(): ResponseDTO<*> = toDTO(Response.DATABASE_CONFLICT)

    // Duplicate entry
    @ExceptionHandler(SQLIntegrityConstraintViolationException::class)
    fun handleEntry(): ResponseDTO<*> = toDTO(Response.DATABASE_CONFLICT)

    // 数据访问异常，Spring 封装的通用异常（例如字段为空、类型不匹配等）
    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccess(): ResponseDTO<*> = toDTO(Response.DATABASE_ACCESS_ERROR)

    // 事务相关异常
    @ExceptionHandler(UnexpectedRollbackException::class)
    fun handleTransactionError(): ResponseDTO<*> = toDTO(Response.DATABASE_TRANSACTION_ERROR)

    // 类型转换错误（如映射数据库字段类型失败）
    @ExceptionHandler(TypeMismatchDataAccessException::class)
    fun handleTypeMismatch(): ResponseDTO<*> = toDTO(Response.DATABASE_TYPE_ERROR)

    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handleEmptyResult(): ResponseDTO<*> = toDTO(Response.DATABASE_DATA_NOT_FOUND)

    @ExceptionHandler(DataTruncation::class)
    fun handleDataTruncation(): ResponseDTO<*> = toDTO(Response.DATABASE_TYPE_ERROR)

    @ExceptionHandler(BadSqlGrammarException::class)
    fun handleBadSqlGrammar(): ResponseDTO<*> = toDTO(Response.DATABASE_SYNTAX_ERROR)


    // ********************************* Mybatis Plus
    // MyBatis SQL 执行错误（比如映射失败、参数不匹配等）
    @ExceptionHandler(BindingException::class)
    fun handleBindingException(): ResponseDTO<*> = toDTO(Response.DATABASE_BINDING_ERROR)

    // MyBatis-Plus 执行 SQL 错误的封装（一般包含在 PersistenceException 中）
    @ExceptionHandler(TooManyResultsException::class)
    fun handleTooManyResultsException(): ResponseDTO<*> = toDTO(Response.TOO_MANY_RESULT_ERROR)

    @ExceptionHandler(PersistenceException::class)
    fun handlePersistenceException(): ResponseDTO<*> = toDTO(Response.DATABASE_EXECUTION_ERROR)
}