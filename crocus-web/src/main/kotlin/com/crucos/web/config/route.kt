package com.crucos.web.config

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.response
import com.crocus.server.utils.time.timeRecordingNow
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mu.KotlinLogging

/**
 * @author junsilck
 * @date 2024/5/27 23:27
 * 全局路由拦截
 */
fun Application.globalRoute(){
    val logger = KotlinLogging.logger(Application::class.java.name + ".globalRoute")

    logger.info { "配置路由拦截 globalRoute: ${timeRecordingNow()}" }
    routing {
        get("*") { call.respondText {  response(Response.NOT_FOUND) } }
        post("*") { call.respondText {  response(Response.NOT_FOUND) } }
        delete("*") { call.respondText {  response(Response.NOT_FOUND) } }
        put("*"){ call.respondText {  response(Response.NOT_FOUND) } }
    }
}