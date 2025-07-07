package com.crocus.server.utils.exception

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.io.IOException
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class SystemExceptionHandler {

    @ExceptionHandler(E::class)
    fun handleE(e: E): ResponseDTO<*> = toDTO(Response.INTERNAL_SERVER_ERROR, "${e.code}:${e.message}||${e.cause}")

    @ExceptionHandler(Exception::class)
    fun handleDefault(): ResponseDTO<*> = toDTO(Response.INTERNAL_SERVER_ERROR)

    // 处理 JSON 序列化/反序列化异常
    @ExceptionHandler(JsonProcessingException::class)
    fun handleJsonProcessingException(): ResponseDTO<*> = toDTO(Response.JSON_SERIALIZATION_ERROR)

    // 处理 JSON 映射异常
    @ExceptionHandler(JsonMappingException::class)
    fun handleJsonMappingException(): ResponseDTO<*> = toDTO(Response.JSON_DESERIALIZATION_ERROR)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(): ResponseDTO<*> = toDTO(Response.ERROR_PARAMETER)

    @ExceptionHandler(ClassCastException::class)
    fun handleClassCastException(): ResponseDTO<*> = toDTO(Response.CASE_ERROR)

    @ExceptionHandler(UnsupportedOperationException::class)
    fun handleUnsupportedOperationException(): ResponseDTO<*> = toDTO(Response.UNAVAILABLE)

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(): ResponseDTO<*> = toDTO(Response.ERROR_PARAMETER)

    // 处理 NullPointerException
    // 调用库的时候 兜底
    @ExceptionHandler(NullPointerException::class)
    fun handleNullPointerException(): ResponseDTO<*> = toDTO(Response.NULL_POINTER_ERROR)

    // 处理类未找到异常
    @ExceptionHandler(ClassNotFoundException::class)
    fun handleClassNotFoundException(): ResponseDTO<*> = toDTO(Response.CLASS_NOT_FOUND_ERROR)

    // 处理 IO 操作相关的异常
    @ExceptionHandler(IOException::class)
    fun handleIOException(): ResponseDTO<*> = toDTO(Response.IO_ERROR)

    // 处理不可知的异常
    @ExceptionHandler(UnknownError::class)
    fun handleUnknownError(): ResponseDTO<*> = toDTO(Response.SYSTEM_ERROR)

}