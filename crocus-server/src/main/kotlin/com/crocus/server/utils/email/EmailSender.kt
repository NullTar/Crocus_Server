package com.crocus.server.utils.email

import com.crocus.server.entity.entities.Member
import com.crocus.server.utils.response.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class EmailSender(
    private val javaMailSender: JavaMailSender,
    private val templateEngine: TemplateEngine
) {

    @Value("\${spring.mail.username}")
    private lateinit var fromEmail: String

    fun pingEmail(member: Member): Response? {

        if (member.account.isNullOrBlank() || member.email.isNullOrBlank() || member.code.isNullOrBlank()) return null

        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        helper.setFrom(fromEmail)
        helper.setTo(member.email!!)
        helper.setSubject("Code")
        val context = Context().apply {
            setVariable("account", member.account)
            setVariable("code", member.code)
        }
        val htmlContent = templateEngine.process("email", context)
        helper.setText(htmlContent, true)
        javaMailSender.send(message)
        return Response.EMAIL_SENT_SUCCESS
    }

//    fun pingEmailWithAttachment(receiver: String, account: String,subject: String, code: String, url: String) {
//        val message = javaMailSender.createMimeMessage()
//        val helper = MimeMessageHelper(message, true)
//        helper.setFrom(fromEmail)
//        helper.setTo(receiver)
//        helper.setSubject(subject)
//        val context = Context().apply {
//            setVariable("url", url)
//        }
//        val htmlContent = templateEngine.process("email", context)
//        helper.setText(htmlContent, true)
//        javaMailSender.send(message)
//    }


}