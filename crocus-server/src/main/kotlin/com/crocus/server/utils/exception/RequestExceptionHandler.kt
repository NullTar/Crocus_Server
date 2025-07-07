package com.crocus.server.utils.exception

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class RequestExceptionHandler {

    // 缺少字段
    @ExceptionHandler(MissingServletRequestPartException::class)
    fun handleMissingPart(): ResponseDTO<*> = toDTO(Response.ERROR_BODY)

    // 异常处理：捕获文件上传超出大小的异常
    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleFileSizeException(): ResponseDTO<*> = toDTO(Response.FILESIZE_ERROR)

    // 请求参数错误   如类型不匹配、缺参数等
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(): ResponseDTO<*> = toDTO(Response.ERROR_PARAMETER)

    // 请求体错误
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleBodyReadError(): ResponseDTO<*> = toDTO(Response.ERROR_BODY)

    // 头部错误
    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleHeaderMissing(): ResponseDTO<*> = toDTO(Response.ERROR_HEADER)

    // 请求格式错误
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleUnsupportedMediaType(): ResponseDTO<*> = toDTO(Response.ERROR_FORM)

    // 资源不存在
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNotFound(): ResponseDTO<*> = toDTO(Response.NOT_FOUND)

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(): ResponseDTO<*> = toDTO(Response.UNAUTHORIZED)

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(): ResponseDTO<*> = toDTO(Response.FORBIDDEN)

    @ExceptionHandler(HttpMessageNotWritableException::class)
    fun handleHttpMessageNotWritable(): ResponseDTO<*> = toDTO(Response.ERROR_BODY)

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatch(): ResponseDTO<*> = toDTO(Response.ERROR_PARAMETER)

}