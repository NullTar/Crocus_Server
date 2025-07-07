package com.crocus.server.service.account

import com.crocus.server.entity.entities.Member
import com.crocus.server.entity.function.*
import com.crocus.server.entity.parameter.Valid
import com.crocus.server.enum.Role
import com.crocus.server.service.base.BaseAccountService
import com.crocus.server.service.inter.MemberService
import com.crocus.server.utils.database.DataSource
import com.crocus.server.utils.database.SourceName
import com.crocus.server.utils.response.Response
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@DataSource(SourceName.MAIN)
class MemberServiceImp : BaseAccountService(), MemberService {

    private val logger = KotlinLogging.logger(this::class.java.name)

    /**
     * 输入用户名后需要校验用户名是否被注册
     * MARK: DONE
     */
    suspend fun validAccount(member: Valid): Response {
        // 要校验
        member.account?.let { a ->
            verify(a)?.let { return it }
        }
        return when {
            member.account?.let { repository.queryByAccount(it) } != null -> Response.USER_IS_EXISTED
            member.email?.let { repository.queryByEmail(it) } != null -> Response.EMAIL_IS_EXISTED
            else -> return Response.USER_NOT_EXISTED
        }
    }

    /**
     * 邮箱注册 key: email
     * MARK: DONE
     */
    override suspend fun register(member: Member): Response {
        // 按照邮箱查询
        val query = member.email?.let { repository.queryByEmail(it) }
            ?: member.account?.let { repository.queryByAccount(it) }

        // 确保邮箱唯一性
        query?.email?.let {
            return Response.EMAIL_IS_EXISTED
        }
        // 确保账户唯一性
        query?.account?.let {
            return Response.ACCOUNT_IS_EXISTED
        }
        // 加密并应用属性
        val apply = member.apply()
        // 生成验证码
        apply.applyCode()
        logger.info { "Register: $apply" }
        return emailSender.pingEmail(apply)?.let {
            it.also {
                redis.saveWithTransaction("Register:${apply.email}", apply, 10 * 60)
            }
        } ?: Response.EMAIL_SENT_ERROR
    }

    /**
     * 登陆 邮箱或用户名    key: email / account
     * MARK: DONE
     */
    override suspend fun login(member: Member): Response {

        // 邮箱未验证 注册了但还没验证
        val fetch = redis.fetch("Register:${member.email}", Member::class.java)
        fetch?.let {
            return Response.USER_IS_NOT_ACTIVATED
        }
        var key = member.email
        // 查询用户
        val query = member.email?.let { repository.queryByEmail(it) }
            ?: member.account?.let {
                key = it
                repository.queryByAccount(it)
            }
            ?: return Response.QUERY_FAIL
        // 校验用户状态
        query.valid()?.let { return it }
        // 校验密码
        checkPassword(member.password, query)?.let { return it }
        logger.info { "Login: $query" }
        // 验证账户是否有auth 优先用auth， 没有就校验邮箱
        return sendCode(query).also {
            redis.saveWithTransaction("Login:$key", query, 10 * 60)
        }
    }

    /**
     * 删除账号 key: uuid
     * MARK: DONE
     */
    override suspend fun markSelf(t: String): Response {
        val member = jwt.parsingJwt(t)
        member.detect(setOf(Role.User))?.let { return it }
        // 查数据库
        val query = member.uuid?.let {
            repository.queryByUUID(it)
        } ?: return Response.HAS_NO_UUID
        check(query.role != Role.User || member.account != query.account || member.email != query.email) {
            return Response.USER_DATA_NOT_MATCH
        }
        checkPassword(member.password, query)?.let { return it }
        logger.info { "Delete: $query" }
        return sendCode(query).also {
            redis.saveWithTransaction("Delete:${query.uuid}", query, 10 * 60)
        }
    }

}