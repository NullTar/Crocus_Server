package com.crocus.server.mapper.inter


import com.crocus.server.entity.entities.*
import java.math.BigInteger
import kotlin.reflect.KProperty1

interface MemberRepository {

    /**
     * 根据邮箱查询
     */
    suspend fun queryByEmail(email: String): Member?

    /**
     * 根据账户名查询
     */
    suspend fun queryByAccount(account: String): Member?


    /**
     * 根据UUID查询
     */
    suspend fun queryByUUID(uuid: String): Member?

    /**
     * 根据 id 查询
     */
    suspend fun queryByIdentifier(identifier: BigInteger): Member?

    /**
     * 根据 id 查询
     */
    suspend fun queryByIdentifiers(identifier: List<BigInteger>): List<Member>?

    /**
     * 新增
     */
    suspend fun insertMember(member: Member)

    /**
     * 刷新用户状态
     * - 当用户登陆的时候, 刷新用户的删除记录为 Null
     */
    suspend fun refreshMember(id: BigInteger)

    suspend fun refreshMember(id: BigInteger, uuid: String)

    /**
     * 删除用户
     */
    suspend fun deleteMember(member: Member)

    /**
     * 修改用户
     */
    suspend fun updateMember(member: Member)


    suspend fun updateFieldsToNullById(id: BigInteger, fields: Set<KProperty1<Member, *>>)

    /**
     * 分页查询
     */
    suspend fun getUsersPage(now: Long, size: Long): List<Member>

}

