package com.crocus.server.utils.exception

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import com.sun.mail.util.MailConnectException
import org.springframework.mail.MailAuthenticationException
import org.springframework.mail.MailException
import org.springframework.mail.MailParseException
import org.springframework.mail.MailSendException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.mail.SendFailedException
import javax.mail.internet.AddressException

@RestControllerAdvice
class MailExceptionHandler {

    @ExceptionHandler(MailAuthenticationException::class)
    fun handleAuthError(): ResponseDTO<*> = toDTO(Response.MAIL_AUTH_FAIL)

    @ExceptionHandler(MailSendException::class)
    fun handleSendError(): ResponseDTO<*> = toDTO(Response.MAIL_SEND_FAIL)

    @ExceptionHandler(AddressException::class)
    fun handleInvalidAddress(): ResponseDTO<*> = toDTO(Response.MAIL_ADDRESS_ERROR)

    @ExceptionHandler(MailParseException::class)
    fun handleParseError(): ResponseDTO<*> = toDTO(Response.MAIL_FORMAT_ERROR)

    @ExceptionHandler(SendFailedException::class)
    fun handlePartialMailFail(): ResponseDTO<*> = toDTO(Response.MAIL_PARTIAL_FAIL)

    @ExceptionHandler(MailConnectException::class)
    fun handleMail(): ResponseDTO<*> = toDTO(Response.MAIL_CONNECTION_ERROR)

    @ExceptionHandler(MailException::class)
    fun handleGeneralMailError(): ResponseDTO<*> = toDTO(Response.MAIL_SYSTEM_ERROR)


}