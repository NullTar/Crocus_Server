package com.crocus.server.controller

import com.crocus.server.entity.function.detect
import com.crocus.server.entity.parameter.Login
import com.crocus.server.entity.parameter.Register
import com.crocus.server.entity.parameter.token_prefix
import com.crocus.server.enum.Role
import com.crocus.server.service.account.AdminServiceImp
import com.crocus.server.service.account.AuthorityServiceImp
import com.crocus.server.service.account.MemberServiceImp
import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import javax.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/crocus")
class AdminController(
    private val adminServiceImp: AdminServiceImp,
    private val memberService: MemberServiceImp,
    private val authorityService: AuthorityServiceImp
) {

    private val logger = KotlinLogging.logger(this.javaClass.name)

    @RequestMapping("/user/list")
    suspend fun listUser(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestParam(value = "page", defaultValue = "0") page: Long,
        @RequestParam(value = "limit", defaultValue = "10") limit: Long,
        request: HttpServletRequest
    ): ResponseDTO<*> {
        logger.warn {
            """
            请求 IP: ${request.remoteAddr}
            User-Agent: ${request.getHeader("User-Agent")}
            完整 URI: ${request.requestURI}
            请求参数: ${request.parameterMap}
        """.trimIndent()
        }
        val jwt = tokenRaw.removePrefix(token_prefix)
        val member = adminServiceImp.jwt.parsingJwt(jwt)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val result = adminServiceImp.getUsersPage(member, page, limit)
        return toDTO(data = result)
    }

    @RequestMapping("/user/ban")
    suspend fun banUser(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestParam(value = "uuid") uuid: String,
        request: HttpServletRequest
    ): ResponseDTO<*> {
        logger.warn {
            """
            请求 IP: ${request.remoteAddr}
            User-Agent: ${request.getHeader("User-Agent")}
            完整 URI: ${request.requestURI}
            请求参数: ${request.parameterMap}
        """.trimIndent()
        }
        val jwt = tokenRaw.removePrefix(token_prefix)
        val member = adminServiceImp.jwt.parsingJwt(jwt)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val result = adminServiceImp.banAccount(member, uuid)
        return toDTO(result)
    }

    @RequestMapping("/admin/login")
    suspend fun login(
        @RequestBody login: Login,
    ): ResponseDTO<*> {
        login.check()?.let { return toDTO(it) }
        val member = login.toMember()
        val account = member.account ?: return toDTO(Response.FORBIDDEN)
        if (!account.startsWith("@@")) {
            return toDTO(Response.FORBIDDEN)
        }
        val response = memberService.login(member)
        return toDTO(response)
    }

    @RequestMapping("/admin/register")
    suspend fun register(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestBody register: Register,
        @RequestParam(value = "code") code: String,
        request: HttpServletRequest
    ): ResponseDTO<*> {
        logger.warn {
            """
            请求 IP: ${request.remoteAddr}
            User-Agent: ${request.getHeader("User-Agent")}
            完整 URI: ${request.requestURI}
            请求参数: ${request.parameterMap}
        """.trimIndent()
        }
        register.check()?.let { return toDTO(it) }
        val registerAdmin = register.toMember()
        val jwt = tokenRaw.removePrefix(token_prefix)
        val member = adminServiceImp.jwt.parsingJwt(jwt)
        member.detect(setOf(Role.SuperAdmin))?.let { return toDTO(it) }

        val account = registerAdmin.account ?: return toDTO(Response.FORBIDDEN)
        if (!account.startsWith("@@")) {
            return toDTO(Response.REGISTER_FAIL)
        }
        val query = member.uuid?.let { adminServiceImp.repository.queryByUUID(it) }
            ?: return toDTO(Response.QUERY_FAIL)
        if (query.account != member.account || query.email != member.email || query.role != member.role) {
            return toDTO(Response.FORBIDDEN)
        }
        val result = authorityService.verifyCode(code, url = query.authenticator)
        if (!result) return toDTO(Response.FORBIDDEN)
        val response = memberService.register(registerAdmin)
        return toDTO(response)
    }

}