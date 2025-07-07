package com.crocus.server.utils.exception

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class JWTExceptionHandler {

    // 捕获 token 过期的异常
    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(): ResponseDTO<*> =  toDTO(Response.JWT_EXPIRED)

    // 捕获 JWT 格式错误的异常
    @ExceptionHandler(MalformedJwtException::class)
    fun handleMalformedJwtException(): ResponseDTO<*> =  toDTO(Response.JWT_FORMAT_ERROR)

    // 捕获签名验证失败的异常
    @ExceptionHandler(SignatureException::class)
    fun handleSignatureException(): ResponseDTO<*> =  toDTO(Response.JWT_SIGNATURE_ERROR)

    // 捕获一般的 JWT 异常
    @ExceptionHandler(JwtException::class)
    fun handleJwtException(): ResponseDTO<*> =  toDTO(Response.JWT_PROCESSING_ERROR)

}