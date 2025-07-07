package com.crocus.server.service.inter

import com.crocus.server.entity.entities.Member
import com.crocus.server.entity.parameter.ModifyEmail
import com.crocus.server.entity.parameter.Verification
import com.crocus.server.utils.response.Response

/**
 * 邮箱 2FA
 */
interface AuthorityService {

    // 检查 Auth
    suspend fun checkAuthority(token: String): Response

    // 重新发送验证码
    suspend fun resendCode(key: String): Response

    // 注册 和 登陆 操作
    suspend fun verifyCode(verification: Verification): String

    // 带 Token 的验证
    suspend fun verifyCode(verification: Verification, member: Member): String

    // 绑定 2FA
    suspend fun bindAuth(member: Member, password: String): String

    // 重置 Auth
    suspend fun resetAuth(member: Member, verification: Verification): Response

    // 修改邮箱
    suspend fun modifyEmail(member: Member, modify: ModifyEmail): Response

}