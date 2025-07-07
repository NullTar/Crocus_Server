package com.crocus.server.entity.parameter

import com.crocus.server.entity.entities.Member
import com.crocus.server.utils.response.Response

interface BaseAccount {
    val password: String
    fun check(): Response?
    fun toMember(): Member

    fun checkPassword(): Response? {
        return if (password.isBlank()) Response.PASSWORD_IS_NULL_OR_BLACK
        else null
    }

}