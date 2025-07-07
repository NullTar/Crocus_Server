package com.crocus.server.service.inter

import com.crocus.server.entity.entities.Member
import com.crocus.server.utils.response.Response

/**
 * Spring 6.1 以下的版本 强一致性的操作，使用@Cacheable时候 不能用suspend
 * @date 2024/7/4 19:14
 */
interface MemberService {

    // 注册
    suspend fun register(member: Member): Response

    // 登录
    suspend fun login(member: Member): Response

    // 删除
    suspend fun markSelf(t: String): Response

}
