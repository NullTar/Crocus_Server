package com.crocus.server.service.base

import com.crocus.server.entity.entities.Member
import com.crocus.server.mapper.MemberRepositoryImp
import com.crocus.server.utils.email.EmailSender
import com.crocus.server.utils.encrypt.Encipher
import com.crocus.server.utils.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BaseAccountService : BaseService() {

    @Autowired
    lateinit var emailSender: EmailSender

    @Autowired
    lateinit var encipher: Encipher

    @Autowired
    lateinit var repository: MemberRepositoryImp

    /**
     * 发送验证码
     * - 有验证器就返回验证器
     * - 没有验证器就发邮件
     * MARK: DONE
     */
    suspend fun sendCode(query: Member): Response {
        return if (query.authenticator != null) {
            Response.REQUIRED_AUTH
        } else {
            query.applyCode()
            Response.REQUIRED_EMAIL.also {
                emailSender.pingEmail(query)
            }
        }
    }

    /**
     * 检查密码
     * @param passcode 输入的密码
     * @param member Member 对象
     * MARK: DONE
     * - redis 取出的盐需要二次对比 请使用 checkPassword(passcode: String, password: String, salt: String)
     */
    suspend fun checkPassword(passcode: String?, member: Member): Response? {
        if (passcode.isNullOrBlank() || member.password.isNullOrBlank()) return Response.PASSWORD_IS_NULL_OR_BLACK
        return if (encipher.decrypt(member.salt + passcode, member.password)) null
        else Response.PASSWORD_IS_WRONG
    }

    /**
     * 检查密码
     * @param passcode 输入的密码
     * @param password 加密的密码 (数据库)
     * @param salt 盐
     * MARK: DONE
     */
    suspend fun checkPassword(passcode: String?, password: String?, salt: String?): Response? {
        if (passcode.isNullOrBlank() || password.isNullOrBlank() || salt.isNullOrBlank()) return Response.PASSWORD_IS_NULL_OR_BLACK
        return if (encipher.decrypt(salt + passcode, password)) null
        else Response.PASSWORD_IS_WRONG
    }

}