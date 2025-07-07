package com.crucos.web.service

import com.crocus.server.entity.parameter.Login
import com.crocus.server.entity.parameter.Register
import com.crocus.server.entity.parameter.Valid
import com.crocus.server.service.account.MemberServiceImp
import com.crocus.server.utils.response.Response
import com.crucos.web.SpringContext

class MemberWebService{

    private val memberService by lazy {
        SpringContext.Companion.server.getBean(MemberServiceImp::class.java)
    }

    /**
     * 验证账户名
     * MARK: DONE
     */
    suspend fun validAccount(member: Valid): Response = memberService.validAccount(member)

    /**
     * 注册
     * MARK: DONE
     */
    suspend fun register(member: Register): Response = memberService.register(member.toMember())

    /**
     * 登录
     * MARK: DONE
     */
    suspend fun login(member: Login): Response = memberService.login(member.toMember())

    /**
     * 删除 只有 user 角色有
     * MARK: DONE
     */
    suspend fun markSelf(token: String): Response = memberService.markSelf(token)

}