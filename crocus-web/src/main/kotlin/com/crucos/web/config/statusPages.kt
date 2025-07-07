package com.crucos.web.config

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.response
import com.crocus.server.utils.time.timeRecordingNow
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import mu.KotlinLogging

/**
 * @author junsilck
 * @date 2024/5/28 14:52
 * 全局状态页面
 */
fun Application.statusPages() {
    val logger = KotlinLogging.logger(Application::class.java.name + ".statusPages")

    install(StatusPages) {
        // limit回复
        status(HttpStatusCode.TooManyRequests) { call, status ->
            call.respondText(response(Response.ERROR_TOO_MANY_REQUEST), ContentType.Application.Json, status)
        }
        // Not_Found回复
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(response(Response.NOT_FOUND), ContentType.Application.Json, status)
        }
        //程序错误回复
        exception<Throwable> { call, cause ->
            call.respondText(response(Response.ERROR_REQUEST), status = HttpStatusCode.InternalServerError)
            logger.error {
                """ Message: ${cause.message} Time: ${timeRecordingNow()}
                    Cause: ${cause.cause}
                    Stack: ${cause.stackTraceToString()}
                 """.trimIndent()
            }
        }
    }
}