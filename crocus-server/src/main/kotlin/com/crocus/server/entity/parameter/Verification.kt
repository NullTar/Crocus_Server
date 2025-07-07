package com.crocus.server.entity.parameter

import com.crocus.server.enum.Operation
import com.crocus.server.utils.response.Response

/**
 * 1. 绑定和解绑 Auth 密码、code
 */
data class Verification(
    val code: String,
    val password: String?,
    val key: String?,
    val operation: Operation?,
) {
    // 解绑身份验证器
    fun check(): Response? {
        return if (code.isBlank()) Response.WHERE_THE_CODE
        else if (code.length < 6) Response.CODE_LENGTH_ERROR
        else if (operation != Operation.Register && password.isNullOrBlank()) Response.PASSWORD_IS_NULL_OR_BLACK
        else null
    }

    // 其他需要验证的操作
    fun checkManner(): Response? {
        check()?.let { return it }
        return if (key.isNullOrBlank() || operation == null) Response.ERROR_BODY
        else null
    }


}