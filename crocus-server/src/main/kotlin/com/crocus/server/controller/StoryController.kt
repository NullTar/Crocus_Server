package com.crocus.server.controller

import com.crocus.server.entity.entities.Story
import com.crocus.server.entity.function.detect
import com.crocus.server.entity.parameter.token_prefix
import com.crocus.server.enum.Role
import com.crocus.server.service.StoryServiceImp
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/crocus/manager/story")
class StoryController(private val storyService: StoryServiceImp) {

    @PostMapping("/upload")
    suspend fun upload(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestBody story: Story
    ): ResponseDTO<*> {
        val token = tokenRaw.removePrefix(token_prefix)
        val member = storyService.jwt.parsingJwt(token)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        return toDTO(storyService.insert(member, story))
    }

}