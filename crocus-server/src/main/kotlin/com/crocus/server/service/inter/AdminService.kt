package com.crocus.server.service.inter

import com.crocus.server.entity.entities.Member
import com.crocus.server.utils.response.Response

interface AdminService {

    // 禁用 仅限于 用户角色
    suspend fun banAccount(admin: Member, uuid: String): Response

    suspend fun getUsersPage(member: Member, now: Long, size: Long): List<Member>

}