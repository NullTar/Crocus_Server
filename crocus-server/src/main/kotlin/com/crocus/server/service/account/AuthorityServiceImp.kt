package com.crocus.server.service.account

import com.crocus.server.entity.entities.Member
import com.crocus.server.entity.function.filter
import com.crocus.server.entity.parameter.ModifyEmail
import com.crocus.server.entity.parameter.Verification
import com.crocus.server.enum.Operation
import com.crocus.server.enum.Role
import com.crocus.server.service.inter.AuthorityService
import com.crocus.server.service.base.BaseAccountService
import com.crocus.server.utils.database.DataSource
import com.crocus.server.utils.database.SourceName
import com.crocus.server.utils.encrypt.extractSecret
import com.crocus.server.utils.encrypt.generateUrl
import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.response
import com.crocus.server.utils.response.toDTO
import com.crocus.server.utils.time.timeRecordingNow
import com.crocus.server.utils.type.typeRef
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@DataSource(SourceName.MAIN)
class AuthorityServiceImp : BaseAccountService(), AuthorityService {

    private val limitSecond = 9 * 60

    private val logger = KotlinLogging.logger(AuthorityServiceImp::class.java.name)

    /**
     * 检查是否有 Auth
     * key: uuid
     * MARK: DONE
     */
    override suspend fun checkAuthority(token: String): Response {
        val token = jwt.parsingJwt(token)
        val query = token.uuid?.let { repository.queryByUUID(it) }
            ?: return Response.QUERY_FAIL
        return if (query.authenticator == null) {
            return Response.NOT_BIND_AUTH
        } else Response.AUTH_GENERATE_ERROR
    }

    /**
     * 重新发送验证码
     * key 为 email 为注册
     * key 为 account 为登陆
     * key 为 uuid 时为其他操作
     * MARK: DONE
     */
    override suspend fun resendCode(key: String): Response {
        val fetch = redis.fetch(key, Member::class.java)
            ?: return Response.DATA_NOT_AVAILABLE
        if (fetch.code == null) return Response.CANT_RETRY
        if (fetch.authenticator != null) return Response.REQUIRED_AUTH
        return if (redis.expireTime(key) <= limitSecond) {
            fetch.applyCode()
            redis.saveWithTransaction(key, fetch, 10 * 60)
            Response.RETRY_SUCCESS
        } else Response.CANT_RETRY
    }


    /**
     * 注册和登陆的验证
     * MARK: DONE
     */
    @Transactional
    override suspend fun verifyCode(verification: Verification): String {
        return when (verification.operation) {
            // 注册流程  key: email
            Operation.Register -> {
                val key = "Register:${verification.key}"
                val fetch = redis.fetch(key, Member::class.java)
                    ?: return response(Response.DATA_NOT_AVAILABLE)
                val result = compareCode(verification.code, fetch.code)
                when (result) {
                    true -> {
                        fetch.role = Role.User
                        repository.insertMember(fetch)
                        response(Response.REGISTER_SUCCESS).also {
                            redis.delete(key)
                        }
                    }

                    else -> response(Response.CODE_NOT_MATCHING)
                }
            }

            // 登陆流程 key: account
            Operation.Login -> {
                val key = "Login:${verification.key}"
                val fetch = redis.fetch(key, Member::class.java)
                    ?: return response(Response.DATA_NOT_AVAILABLE)
                val query = fetch.account?.let { repository.queryByAccount(it) }
                    ?: fetch.email?.let { repository.queryByEmail(it) }
                    ?: return response(Response.QUERY_FAIL)
                checkPassword(verification.password, query.password, fetch.salt)?.let { response(it) }
                val result = compareCode(verification.code, fetch.code, query.authenticator)
                when (result) {
                    true -> {
                        redis.delete(key)
                        // 标记为不删除 更新 last login time
                        if (query.role == Role.User) {
                            query.id?.let { repository.refreshMember(it) }
                        }
                        if (query.role == Role.Admin || query.role == Role.SuperAdmin) {
                            query.id?.let { id ->
                                query.uuid?.let { repository.refreshMember(id, it) }
                            }
                        }
                        // 登陆的时候 Role 登陆返回 JWT, 删除时入库并返回 Response
                        response(Response.LOGIN_SUCCESS, jwt.generateJwt(fetch.filter()))
                    }

                    else -> response(Response.CODE_NOT_MATCHING)
                }
            }

            else -> response(Response.ERROR_FORM)
        }.also {
            logger.info { "${verification.operation}: ${verification.key} ${timeRecordingNow()}" }
        }
    }

    /**
     * 包含 jwt 的验证
     * MARK: TODO
     */
    @Transactional
    override suspend fun verifyCode(verification: Verification, member: Member): String {
        return when (verification.operation) {
            // 删除流程 key: uuid
            Operation.Delete -> {
                val key = "Delete:${verification.key}"
                if (setOf(Role.Admin, Role.SuperAdmin).contains(member.role)) return response(Response.UNAUTHORIZED)
                val fetch = redis.fetch(key, Member::class.java)
                    ?: return response(Response.DATA_NOT_AVAILABLE)
                val query = member.uuid?.let { repository.queryByUUID(it) }
                    ?: return response(Response.QUERY_FAIL)

                val result = compareCode(verification.code, fetch.code, query.authenticator)
                when (result) {
                    true -> {
                        checkPassword(verification.password, query.password, fetch.salt)?.let { response(it) }
                        repository.deleteMember(member)
                        response(Response.DELETE_SUCCESS).also {
                            redis.delete(key)
                        }
                    }

                    else -> response(Response.CODE_NOT_MATCHING)
                }
            }

            Operation.BindAuth -> {
                val fetch = redis.fetch("BindAuth:${verification.key}", ref = typeRef<ResponseDTO<String>>())
                    ?: return response(Response.DATA_NOT_AVAILABLE)
                val query = member.uuid?.let { repository.queryByUUID(it) }
                    ?: return response(Response.QUERY_FAIL)
                val data = fetch.data ?: return response(Response.DATA_NOT_AVAILABLE)
                val result = compareCode(verification.code, url = data)
                when (result) {
                    true -> {
                        checkPassword(verification.password, query)?.let { response(it) }
                        query.authenticator = data
                        repository.updateMember(query)
                        response(Response.AUTH_BIND_SUCCESS).also {
                            redis.delete("BindAuth:${verification.key}")
                        }
                    }

                    else -> response(Response.CODE_NOT_MATCHING)
                }
            }

            Operation.ModifyEmail -> {
                val fetch = redis.fetch(verification.key, Member::class.java)
                    ?: return response(Response.DATA_NOT_AVAILABLE)
                val query = member.uuid?.let { repository.queryByUUID(it) }
                    ?: return response(Response.QUERY_FAIL)
                checkPassword(verification.password, query.password, fetch.salt)?.let { response(it) }
                val result = compareCode(verification.code, fetch.code, query.authenticator)
                when (result) {
                    true -> {
                        ""
                    }

                    else -> response(Response.CODE_NOT_MATCHING)
                }
            }

            else -> response(Response.ERROR_FORM)
        }.also {
            logger.info { "${verification.operation}: ${verification.key} ${timeRecordingNow()}" }
        }
    }

    /**
     * 绑定验证器    key: uuid
     * MARK: DONE
     */
    @Transactional
    override suspend fun bindAuth(member: Member, password: String): String {
        val query = member.uuid?.let { repository.queryByUUID(it) }
            ?: return response(Response.HAS_NO_UUID)
        if (query.email != member.email || query.account != member.account) {
            return response(Response.DO_NOT_CHANGE_DATA)
        }
        checkPassword(password, query)?.let { return response(it) }
        val response = Response.REQUIRED_AUTH
        val url = query.generateUrl()
        return response(response, url).also {
            redis.saveWithTransaction(
                "BindAuth:${query.uuid}",
                toDTO(response, data = url),
                10 * 60, false
            )
        }
    }

    /**
     * 重置验证器 需要密码
     * MARK: DONE
     */
    @Transactional
    override suspend fun resetAuth(member: Member, verification: Verification): Response {
        val query = member.uuid?.let { repository.queryByUUID(it) }
            ?: return Response.HAS_NO_UUID
        val queryId = query.id ?: return Response.QUERY_FAIL
        if (query.email != member.email || query.account != member.account) {
            return Response.DO_NOT_CHANGE_DATA
        }
        checkPassword(verification.password, query)?.let { return it }
        val result = compareCode(verification.code, url = query.authenticator)
        return when (result) {
            false -> Response.CODE_NOT_MATCHING
            true -> {
                repository.updateFieldsToNullById(queryId, setOf(Member::authenticator))
                repository.updateMember(query)
                logger.info { "resetAuth: $query" }
                Response.AUTH_RESET_SUCCESS
            }
        }
    }

    /**
     * 修改邮箱，需要密码    key: uuid
     * MARK: DONE
     */
    @Transactional
    override suspend fun modifyEmail(member: Member, modify: ModifyEmail): Response {
        val query = member.uuid?.let { repository.queryByUUID(it) }
            ?: return Response.HAS_NO_UUID
        if (query.email != member.email ||
            query.account != member.account ||
            query.role != member.role
        ) {
            return Response.DO_NOT_CHANGE_DATA
        }
        checkPassword(modify.password, query)?.let { return it }
        query.email = modify.email
        logger.info { "modifyEmail: $query" }
        return sendCode(query).also {
            redis.saveWithTransaction(query.uuid, query, 10 * 60)
        }
    }

    suspend fun verifyCode(code: String?, compare: String? = null, url: String? = null): Boolean {
        return this.compareCode(code, compare, url)
    }

    /**
     * 对比验证码
     * @param code 验证码
     * @param compare 对比的验证码
     * @param url authenticator 的 url
     * 1. 不是纯数字 长度为 8 是邮箱验证码
     * 2. 是纯数字 长度 > 8  是验证身份验证器
     * MARK: DONE
     */
    private fun compareCode(code: String?, compare: String? = null, url: String? = null): Boolean {
        if (code.isNullOrBlank()) return false

        if (compare.isNullOrBlank() && url.isNullOrBlank()) return false

        if (!url.isNullOrBlank()) {
            val secret = extractSecret(url) ?: return false
            val time = Date(System.currentTimeMillis())
            // 验证 身份验证器
            return code == GoogleAuthenticator(secret.toByteArray()).generate(time)
        }
        // 邮箱验证码（长度为 8）
        if (!compare.isNullOrBlank() && compare.length == 8) {
            return code == compare
        }
        return false
    }

}