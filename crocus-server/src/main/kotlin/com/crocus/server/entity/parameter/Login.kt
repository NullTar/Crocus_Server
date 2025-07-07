package com.crocus.server.entity.parameter

import com.crocus.server.entity.entities.Member
import com.crocus.server.utils.response.Response

data class Login(
    override val password: String,
    val email: String?,
    val account: String?,
) : BaseAccount {

    override fun check(): Response? {
        checkPassword()?.let { return it }
        return if (email.isNullOrBlank() && account.isNullOrBlank()) {
            return Response.ERROR_BODY
        } else null
    }

    override fun toMember(): Member {
        return this.let { log ->
            Member().apply {
                account = log.account?.trim()
                email = log.email?.trim()
                password = log.password.trim()
            }
        }
    }

}