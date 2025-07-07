package com.crocus.server.entity.parameter

import com.crocus.server.utils.response.Response

data class Replaced<T>(
    val old: T,
    val new: T
) {
    fun check(): Response? {
        return if (new == old) Response.ERROR_BODY
        else null
    }

}
