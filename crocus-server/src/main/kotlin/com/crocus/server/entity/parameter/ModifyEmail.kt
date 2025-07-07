package com.crocus.server.entity.parameter

import com.crocus.server.entity.entities.Member
import com.crocus.server.utils.response.Response


data class ModifyEmail(
    override val password: String,
    val email: String
) : BaseAccount {

    override fun check(): Response? {
        checkPassword()?.let { return it }
        return if (email.isBlank()) Response.ERROR_BODY
        else null
    }

    override fun toMember(): Member {
        return this.let { acc ->
            Member().apply {
                email = acc.email.trim()
                password = acc.password.trim()
            }
        }
    }

}
