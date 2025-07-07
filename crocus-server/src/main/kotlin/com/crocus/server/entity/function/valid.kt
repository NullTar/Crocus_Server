package com.crocus.server.entity.function

import com.crocus.server.entity.base.UniversalDate
import com.crocus.server.entity.entities.Member
import com.crocus.server.utils.response.Response
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * 校验是否被删除或被禁用
 * - 激活状态 deleteTime
 * - 删除状态 adminId
 */
fun Member.valid(): Response? {
    this.isBanded()?.let { return it }
    this.isDeleted()?.let { return it }
    return null
}

/**
 * 有数据就是已被删除
 */
fun UniversalDate.isDeleted(): Response? {
    when (this) {
        is Member -> {
            val now = LocalDateTime.now()
            if (deleteTime == null) return null
            val days = ChronoUnit.DAYS.between(deleteTime, now)
            if (days > 10) {
                return Response.ACCOUNT_IS_DELETED
            }
        }
        else -> {
            if (deleteTime != null) {
                return Response.ALREADY_DELETED
            }
        }
    }

    return null
}

/**
 * 有数据就是已被禁用
 */
fun Member.isBanded(): Response? =
    if (disabled == 2) Response.ACCOUNT_IS_BANDED else null







