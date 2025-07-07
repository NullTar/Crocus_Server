package com.crocus.server.controller

import com.crocus.server.entity.function.detect
import com.crocus.server.entity.parameter.ModifyEmail
import com.crocus.server.entity.parameter.Verification
import com.crocus.server.entity.parameter.token_prefix
import com.crocus.server.enum.Role
import com.crocus.server.service.account.AuthorityServiceImp
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.response
import com.crocus.server.utils.response.toDTO
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/authority")
class AuthorityController(
    private val authorityService: AuthorityServiceImp
) {

    @RequestMapping("/check")
    suspend fun checkAuth(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
    ): ResponseDTO<*> {
        val jwt = tokenRaw.removePrefix(token_prefix)
        val member = authorityService.jwt.parsingJwt(jwt)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val response = authorityService.checkAuthority(jwt)
        return toDTO(response)
    }

    @RequestMapping("/resend")
    suspend fun resendCode(@RequestParam("key") key: String): ResponseDTO<*> {
        val response = authorityService.resendCode(key)
        return toDTO(response)
    }

    @RequestMapping("/verifyCode")
    suspend fun verifyCode(
        @RequestHeader(HttpHeaders.AUTHORIZATION, required = false) tokenRaw: String?,
        @RequestBody verification: Verification
    ): String {
        verification.checkManner()?.let { return response(it) }
        tokenRaw?.let {
            val jwt = tokenRaw.removePrefix(token_prefix)
            val member = authorityService.jwt.parsingJwt(jwt)
            member.detect(setOf(Role.User, Role.Admin, Role.SuperAdmin))?.let { return response(it) }
            return authorityService.verifyCode(verification, member)
        }
        return authorityService.verifyCode(verification)
    }

    @RequestMapping("/bind")
    suspend fun bindAuth(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestParam("password") password: String
    ): String {
        val jwt = tokenRaw.removePrefix(token_prefix)
        val member = authorityService.jwt.parsingJwt(jwt)
        member.detect(setOf(Role.User, Role.Admin, Role.SuperAdmin))?.let { return response(it) }
        return authorityService.bindAuth(member, password)
    }

    @RequestMapping("/alter")
    suspend fun alterEmail(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestBody valid: ModifyEmail
    ): ResponseDTO<*> {
        val jwt = tokenRaw.removePrefix(token_prefix)
        valid.check()?.let { return toDTO(it) }
        val member = authorityService.jwt.parsingJwt(jwt)
        member.detect(setOf(Role.User, Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val response = authorityService.modifyEmail(member, valid)
        return toDTO(response)
    }

    @RequestMapping("/unbind")
    suspend fun unbindAuth(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestBody verification: Verification
    ): ResponseDTO<*> {
        verification.check()?.let { return toDTO(it) }
        val jwt = tokenRaw.removePrefix(token_prefix)
        val member = authorityService.jwt.parsingJwt(jwt)
        member.detect(setOf(Role.User, Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val response = authorityService.resetAuth(member, verification)
        return toDTO(response)
    }


}