package com.crocus.server.utils.exception

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import io.minio.errors.ErrorResponseException
import io.minio.errors.InsufficientDataException
import io.minio.errors.InternalException
import io.minio.errors.XmlParserException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class MinioExceptionHandler {

    /**
     * MinIO：桶不存在或对象不存在等错误
     */
    @ExceptionHandler(ErrorResponseException::class)
    fun handleErrorResponse(): ResponseDTO<*> = toDTO(Response.MINIO_ERROR_RESPONSE)

    /**
     * MinIO：对象读取、写入异常
     */
    @ExceptionHandler(InsufficientDataException::class)
    fun handleInsufficientData(): ResponseDTO<*> = toDTO(Response.MINIO_INSUFFICIENT_DATA)

    /**
     * MinIO：网络连接失败或超时
     */
    @ExceptionHandler(InternalException::class)
    fun handleInternalError(): ResponseDTO<*> = toDTO(Response.MINIO_INTERNAL_ERROR)

    /**
     * MinIO：XML 解析错误（如响应格式问题）
     */
    @ExceptionHandler(XmlParserException::class)
    fun handleXmlParserError(): ResponseDTO<*> = toDTO(Response.MINIO_XML_PARSE_ERROR)

}