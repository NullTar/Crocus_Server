package com.crocus.server.entity.parameter

import com.crocus.server.entity.entities.Member
import com.crocus.server.enum.Age
import com.crocus.server.enum.Gender
import com.crocus.server.utils.response.Response


data class ModifyProfile(
    override val password: String,
    val account: String?,
    val age: Age?,
    val gender: Gender?
) : BaseAccount {

    override fun check(): Response? {
        checkPassword()?.let { return it }
        return if (account.isNullOrBlank() && age == null && gender == null) Response.ERROR_BODY
        else null
    }

    override fun toMember(): Member {
        return this.let { mod ->
            Member().apply {
                password = mod.password.trim()
                account = mod.account?.trim()
                age = mod.age
                gender = mod.gender
            }
        }
    }
}