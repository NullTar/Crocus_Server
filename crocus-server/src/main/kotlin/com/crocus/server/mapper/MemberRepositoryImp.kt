package com.crocus.server.mapper

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.crocus.server.entity.entities.Member
import com.crocus.server.enum.Role
import com.crocus.server.mapper.inter.MemberMapper
import com.crocus.server.mapper.inter.MemberRepository
import com.crocus.server.utils.exception.E
import com.crocus.server.utils.response.Response
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.time.LocalDateTime
import kotlin.reflect.KProperty1

@Repository
class MemberRepositoryImp(private val mapper: MemberMapper) :
    MemberRepository {

    /**
     * 根据邮箱查询
     */
    override suspend fun queryByEmail(email: String): Member? {
        return mapper.selectOne(
            KtQueryWrapper(Member()).eq(Member::email, email)
        )
    }

    /**
     * 根据账户名查询
     */
    override suspend fun queryByAccount(account: String): Member? {
        return mapper.selectOne(
            KtQueryWrapper(Member()).eq(Member::account, account)
        )
    }


    /**
     * 根据UUID查询
     */
    override suspend fun queryByUUID(uuid: String): Member? {
        return mapper.selectOne(
            KtQueryWrapper(Member()).eq(Member::uuid, uuid)
        )
    }

    /**
     * 根据 id 查询
     */
    override suspend fun queryByIdentifier(identifier: BigInteger): Member? {
        return mapper.selectById(identifier)
    }

    /**
     * 根据 id 查询
     */
    override suspend fun queryByIdentifiers(identifier: List<BigInteger>): List<Member>? {
        if (identifier.isEmpty()) throw E(Response.QUERY_FAIL)
        return mapper.selectList(
            KtQueryWrapper(Member())
                .`in`(Member::id, identifier)
        )
    }


    /**
     * 新增
     */
    override suspend fun insertMember(member: Member) {
        val result = mapper.insert(member)
        if (result != 1) throw E(Response.INSERT_FAIL)
    }

    /**
     * 刷新用户状态 用户使用的
     * - 当用户登陆的时候, 刷新用户的删除记录为 Null
     */
    override suspend fun refreshMember(id: BigInteger) {
        val result = mapper.update(
            KtUpdateWrapper(Member())
                .eq(Member::id, id)
                .eq(Member::role, Role.User)
                .set(Member::lastLoginTime, LocalDateTime.now())
                .set(Member::deleteTime, null)
        )
        if (result != 1) throw E(Response.UPDATE_FAIL)
    }

    override suspend fun refreshMember(id: BigInteger, uuid: String) {
        val result = mapper.update(
            KtUpdateWrapper(Member())
                .eq(Member::id, id)
                .eq(Member::uuid, uuid)
                .set(Member::lastLoginTime, LocalDateTime.now())
        )
    }


    /**
     * 删除用户
     */
    override suspend fun deleteMember(member: Member) {
        if (member.role == Role.Admin || member.role == Role.SuperAdmin) {
            throw E(Response.DELETE_FAIL_CAUSE_ROLE)
        }
        val result = mapper.update(
            KtUpdateWrapper(Member()).eq(Member::uuid, member.uuid).eq(Member::email, member.email)
                .eq(Member::account, member.account).eq(Member::role, Role.User)
                .set(Member::deleteTime, LocalDateTime.now())
        )
        if (result != 1) throw E(Response.DELETE_FAIL)
    }

    /**
     * 修改
     */
    override suspend fun updateMember(member: Member) {
        member.apply {
            modifyTime = LocalDateTime.now()
        }
        val result = mapper.updateById(member)
        if (result != 1) throw E(Response.UPDATE_FAIL)
    }

    override suspend fun updateFieldsToNullById(id: BigInteger, fields: Set<KProperty1<Member, *>>) {
        val wrapper = KtUpdateWrapper(Member())
            .eq(Member::id, id)

        fields.forEach { f ->
            wrapper.set(f, null)
        }
        val result = mapper.update(null, wrapper)
        if (result != 1) throw E(Response.UPDATE_FAIL)
    }

    /**
     * 分页查询
     */
    override suspend fun getUsersPage(now: Long, size: Long): List<Member> {
        val wrapper = KtQueryWrapper(Member())
            .ne(Member::role, Role.SuperAdmin)
        return mapper.selectPage(Page(now, size), wrapper).records
    }

}