package com.crucos.web.route

import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.response
import com.crucos.web.service.AnnouncementWebService
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route


fun Route.announcementRouting() {

    val announcementWebService = AnnouncementWebService()

    route("/announcement") {

        get("/query") {
            val uuid = call.parameters["uuid"]
                ?: return@get call.respond(Response.ERROR_PARAMETER)
            call.respond(response(announcementWebService.queryByUUID(uuid)))
        }

    }
}