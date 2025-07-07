package com.crocus.server.service.inter

import com.crocus.server.entity.parameter.Replaced
import com.crocus.server.entity.entities.Member
import com.crocus.server.utils.response.Response

interface AccountService {

    // 修改基础信息、邮箱、密码
    suspend fun modify(old: Member, new: Member): String

    // 修改密码
    suspend fun changePassword(account: Member, replaced: Replaced<String>): Response

}