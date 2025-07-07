package com.crocus.server.entity.function

import com.crocus.server.entity.entities.Member
import com.crocus.server.enum.Role
import com.crocus.server.utils.response.Response

/**
 * 传递一个 角色列表
 * - 如果不包含就返回 Response
 */
fun Member.detect(include: Set<Role>): Response? {
    return if (!include.contains(this.role)) Response.FORBIDDEN
    else null
}

