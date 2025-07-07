package com.crucos.web.route

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.response
import com.crucos.web.service.BookWebService
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route


fun Route.bookRouting() {

    val bookWebService = BookWebService()

    route("/book") {
        get("/recommend") {
            val id = call.parameters["id"]?.toBigInteger()
            val size: Int = call.parameters["size"]?.toInt() ?: 40
            call.respond(response(bookWebService.recommend(id, size)))
        }

        get("/queryList") {
            val now = call.parameters["now"]?.toLong() ?: 1
            val size = call.parameters["size"]?.toLong() ?: 10
            if (size + now > 50) {
                return@get call.respond(response(Response.ERROR_PARAMETER))
            }
            call.respond(response(bookWebService.queryList(now, size)))
        }

        get("/query") {
            val uuid = call.parameters["uuid"]
                ?: return@get call.respond(Response.ERROR_PARAMETER)
            call.respond(response(bookWebService.queryByUUID(uuid)))
        }

    }


}