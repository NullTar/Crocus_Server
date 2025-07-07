package com.crucos.web.route

import com.crocus.server.utils.response.response
import com.crucos.web.service.StoryWebService
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.storyRouting() {
    route("/story") {
        val storyWebService = StoryWebService()
        get("/recommend") {
            val id = call.parameters["id"]?.toBigInteger()
            val size = call.parameters["size"]?.toInt() ?: 9
            call.respond(response(storyWebService.recommend(id, size)))
        }
    }
}