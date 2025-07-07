package com.crocus.server.utils.exception

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import io.lettuce.core.RedisCommandExecutionException
import io.lettuce.core.RedisCommandTimeoutException
import io.lettuce.core.RedisConnectionException
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.data.redis.RedisConnectionFailureException
import org.springframework.data.redis.RedisSystemException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RedisExceptionHandler {

    @ExceptionHandler(RedisConnectionFailureException::class)
    fun handleRedisConnectionFailure(): ResponseDTO<*> = toDTO(Response.REDIS_CONNECTION_FAIL)

    @ExceptionHandler(RedisConnectionException::class)
    fun handleRedisConnectionException(): ResponseDTO<*> = toDTO(Response.REDIS_CONNECTION_FAIL)

    @ExceptionHandler(RedisSystemException::class)
    fun handleRedisSystemError(): ResponseDTO<*> = toDTO(Response.REDIS_SYSTEM_ERROR)

    @ExceptionHandler(RedisCommandExecutionException::class)
    fun handleRedisCommandError(): ResponseDTO<*> = toDTO(Response.REDIS_COMMAND_ERROR)

    @ExceptionHandler(RedisCommandTimeoutException::class)
    fun handleRedisTimeout(): ResponseDTO<*> = toDTO(Response.REDIS_TIMEOUT)

    @ExceptionHandler(InvalidDataAccessApiUsageException::class)
    fun handleRedisApiMisuse(): ResponseDTO<*> = toDTO(Response.REDIS_API_MISUSE)

}