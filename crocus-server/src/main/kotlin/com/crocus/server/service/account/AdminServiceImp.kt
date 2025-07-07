package com.crocus.server.service.account

import com.crocus.server.entity.entities.Member
import com.crocus.server.entity.function.detect
import com.crocus.server.enum.Role
import com.crocus.server.service.inter.AdminService
import com.crocus.server.service.base.BaseAccountService
import com.crocus.server.utils.database.DataSource
import com.crocus.server.utils.database.SourceName
import com.crocus.server.utils.exception.E
import com.crocus.server.utils.response.Response
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@DataSource(SourceName.MAIN)
class AdminServiceImp : BaseAccountService(), AdminService {

    private val logger = KotlinLogging.logger(this::javaClass.name)

    @Transactional
    override suspend fun banAccount(admin: Member, uuid: String): Response {
        val query = admin.uuid?.let { repository.queryByUUID(it) } ?: throw E(Response.QUERY_FAIL)
        if (query.account != admin.account && query.email != admin.email) {
            throw E(Response.DO_NOT_CHANGE_DATA)
        }
        query.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return it }
        val queryUser = repository.queryByUUID(uuid)
            ?: throw E(Response.QUERY_FAIL)
        val role = if (query.role == Role.SuperAdmin) 1 else 2
        val bandRole = if (query.role == Role.SuperAdmin) 1 else 2

        /**
         * SuperAdmin 禁用 其他
         * Admin 禁用 用户
         */
        return if (queryUser.role == Role.User || (role != bandRole && role < bandRole)) {
            queryUser.disabled = 2
            repository.updateMember(queryUser)
            logger.warn {
                """
                Account ${query.account} operated, Role:${query.role}
                ${query.uuid} has been banned
                id: ${query.id}
                account: ${query.account}
                email: ${query.email}
            """.trimIndent()
            }
            Response.DELETE_SUCCESS
        } else Response.DELETE_FAIL
    }


    override suspend fun getUsersPage(member: Member, now: Long, size: Long): List<Member> {
        val query = member.uuid?.let { repository.queryByUUID(it) } ?: throw E(Response.QUERY_FAIL)
        if (query.account != member.account && query.email != member.email) {
            throw E(Response.DO_NOT_CHANGE_DATA)
        }
        logger.warn { "Account ${query.account}| ${query.email} has listed users" }
        return repository.getUsersPage(now, size)
    }


}