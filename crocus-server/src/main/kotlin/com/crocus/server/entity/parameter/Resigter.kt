package com.crocus.server.entity.parameter

import com.crocus.server.entity.entities.Member
import com.crocus.server.entity.function.verify
import com.crocus.server.utils.response.Response


data class Register(
    override val password: String,
    val account: String?,
    val email: String?,
) : BaseAccount {

    override fun check(): Response? {
        // 校验数据合法性
        return this.toMember().verify()
    }

    override fun toMember(): Member {
        return this.let { acc ->
            Member().apply {
                account = acc.account?.trim()
                email = acc.email?.trim()
                password = acc.password.trim()
            }
        }
    }
}


data class Valid(
    val account: String?,
    val email: String?,
) {
    fun check(): Response? {
        return if (account.isNullOrBlank() && email.isNullOrBlank()) Response.ERROR_BODY
        else null
    }
}
