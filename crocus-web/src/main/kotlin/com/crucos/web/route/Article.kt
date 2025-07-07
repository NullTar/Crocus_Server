package com.crucos.web.route

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.response
import com.crucos.web.service.ArticleWebService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * @author junsilck
 * @date 2024/7/5 16:58
 * @description TODO
 */
fun Route.articleRouting() {

    val articleWebService = ArticleWebService()

    route("/article") {

        get("/hot") {
            call.respond(articleWebService.queryByHot())
        }

        get("/recommend") {
            val id = call.parameters["id"]?.toBigInteger()
            val size: Int = call.parameters["size"]?.toInt() ?: 40
            call.respond(response(articleWebService.recommend(id, size)))
        }

        get("/queryList") {
            val now = call.parameters["now"]?.toLong() ?: 1
            val size = call.parameters["size"]?.toLong() ?: 10
            if (size + now > 50) {
                return@get call.respond(response(Response.ERROR_PARAMETER))
            }
            call.respond(articleWebService.queryList(now, size))
        }

        get("/query") {
            val uuid = call.parameters["uuid"]
                ?: return@get call.respond(Response.ERROR_PARAMETER)
            call.respond(response(articleWebService.queryByUUID(uuid)))
        }

    }
}