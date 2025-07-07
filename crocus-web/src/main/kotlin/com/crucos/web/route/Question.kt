package com.crucos.web.route

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.response
import com.crucos.web.service.QuestionWebServer
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route


fun Route.question() {
    val questionWebServer = QuestionWebServer()

    route("/question") {
        get("/recommend") {
            call.respond(response(questionWebServer.recommend()))
        }

        get("/queryList") {
            val now = call.parameters["now"]?.toLong() ?: 1
            val size = call.parameters["size"]?.toLong() ?: 10
            if (size + now > 50) {
                return@get call.respond(response(Response.ERROR_PARAMETER))
            }
            call.respond(response(questionWebServer.queryList(now, size)))
        }


        get("/query") {
            val uuid = call.parameters["uuid"]
                ?: return@get call.respond(Response.ERROR_PARAMETER)
            call.respond(response(questionWebServer.queryByUUID(uuid)))
        }

    }
}