package com.crucos.web.route

import com.crocus.server.entity.parameter.Login
import com.crocus.server.entity.parameter.Register
import com.crocus.server.entity.parameter.Valid
import com.crocus.server.entity.parameter.token_prefix
import com.crocus.server.enum.Agree
import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.response
import com.crucos.web.service.MemberWebService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting() {
    route("/account") {

        val memberWebService = MemberWebService()

        /**
         * 验证账户名
         * MARK: DONE
         */
        post("validAccount") {
            val account = call.receive<Valid>()
            account.check()?.let {
                return@post call.respond(response(it))
            }
            val response = memberWebService.validAccount(account)
            call.respond(response(response))
        }

        /**
         * 创建用户 账户、密码、邮箱
         * MARK: DONE
         */
        post("register") {
            val account = call.receive<Register>()
            account.check()?.let {
                return@post call.respond(response(it))
            }
            val response = memberWebService.register(account)
            call.respond(response(response))
        }


        /**
         * 用户登陆  邮箱或账户、密码
         * MARK: DONE
         */
        post("login") {
            val account = call.receive<Login>()
            account.check()?.let {
                return@post call.respond(response(it))
            }
            val response = memberWebService.login(account)
            call.respond(response(response))
        }

        /**
         * 删除账号
         * MARK: DONE
         */
        post("delete") {
            val agreement = call.parameters["agreement"]
            if (agreement.isNullOrBlank()) {
                return@post call.respond(response(Response.ERROR_PARAMETER))
            }
            if (agreement != Agree.Agreement.value) {
                return@post call.respond(response(Response.DELETE_AGREEMENT_NOT_CORRECT))
            }
            val token = call.request.headers[HttpHeaders.Authorization]?.removePrefix(token_prefix)
            token?.let {
                call.respond(memberWebService.markSelf(token))
            } ?: call.respond(response(Response.ERROR_HEADER))
        }
    }
}
