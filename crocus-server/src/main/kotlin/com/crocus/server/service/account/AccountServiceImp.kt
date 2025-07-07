package com.crocus.server.service.account

import com.crocus.server.entity.entities.Member
import com.crocus.server.entity.function.detect
import com.crocus.server.entity.function.filter
import com.crocus.server.entity.function.verify
import com.crocus.server.entity.parameter.Replaced
import com.crocus.server.enum.Role
import com.crocus.server.service.inter.AccountService
import com.crocus.server.service.base.BaseAccountService
import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.response
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountServiceImp : BaseAccountService(), AccountService {

    private val logger = KotlinLogging.logger(AccountServiceImp::class.java.name)

    /**
     * 修改用户数据 用户名、年龄、性别
     * 需要密码二次验证
     * MARK: DONE
     */
    @Transactional
    override suspend fun modify(old: Member, new: Member): String {
        new.account?.let { a ->
            verify(a)?.let { return response(it) }
        }
        val query = old.uuid?.let { repository.queryByUUID(it) }
            ?: return response(Response.HAS_NO_UUID)
        if (old.role != query.role ||
            old.email != query.email
        ) {
            return response(Response.USER_DATA_NOT_MATCH)
        }

        checkPassword(new.password, query)?.let { return response(it) }
        query.apply {
            when {
                !new.account.isNullOrBlank() -> account = new.account
                new.age != null -> age = new.age
                new.gender != null -> gender = new.gender
            }
        }
        repository.updateMember(query)
        logger.info { "Modify: $query" }
        return response(Response.UPDATE_SUCCESS, jwt.generateJwt(query.filter()))
    }


    /**
     * 修改密码
     * 需要二次密码验证
     * MARK: DONE
     */
    @Transactional
    override suspend fun changePassword(account: Member, replaced: Replaced<String>): Response {
        verify(replaced.new)?.let { return it }
        val query = account.uuid?.let { repository.queryByUUID(it) }
            ?: return Response.HAS_NO_UUID

        if (account.role != query.role ||
            account.account != query.account ||
            account.email != query.email
        ) {
            return Response.DO_NOT_CHANGE_DATA
        }
        checkPassword(replaced.old, query)?.let { return it }
        query.password = encipher.encrypt(query.salt + replaced.new)
        repository.updateMember(query)
        logger.info { "Change Password: $query" }
        return Response.UPDATE_SUCCESS
    }



}