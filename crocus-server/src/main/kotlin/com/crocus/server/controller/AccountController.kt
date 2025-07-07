package com.crocus.server.controller

import com.crocus.server.entity.function.detect
import com.crocus.server.entity.parameter.ModifyProfile
import com.crocus.server.entity.parameter.Replaced
import com.crocus.server.entity.parameter.token_prefix
import com.crocus.server.enum.Role
import com.crocus.server.service.account.AccountServiceImp
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.response
import com.crocus.server.utils.response.toDTO
import com.google.common.net.HttpHeaders
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account")
class AccountController(private val accountService: AccountServiceImp) {


    /**
     * 修改资料 用户名、年龄、性别
     * MARK: DONE
     */
    @RequestMapping("/modify")
    suspend fun modifyProfile(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestBody modify: ModifyProfile
    ): String {
        modify.check()?.let { return response(it) }
        val jwt = tokenRaw.removePrefix(token_prefix)
        val account = accountService.jwt.parsingJwt(jwt)
        account.detect(setOf(Role.User, Role.Admin, Role.SuperAdmin))?.let { return response(it) }
        return accountService.modify(account, modify.toMember())
    }

    @RequestMapping("/change")
    suspend fun changePassword(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestBody replace: Replaced<String>
    ): ResponseDTO<*> {
        replace.check()?.let { toDTO(it) }
        val jwt = tokenRaw.removePrefix(token_prefix)
        val account = accountService.jwt.parsingJwt(jwt)
        account.detect(setOf(Role.User, Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val response = accountService.changePassword(account, replace)
        return toDTO(response)
    }


}