package com.crocus.server.controller

import com.crocus.server.entity.entities.CommonQuestion
import com.crocus.server.entity.function.detect
import com.crocus.server.entity.parameter.token_prefix
import com.crocus.server.enum.Role
import com.crocus.server.service.QuestionServiceImp
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/crocus/manager/question")
class QuestionController(private val questionService: QuestionServiceImp) {


    @PostMapping("/upload")
    suspend fun upload(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestBody question: CommonQuestion
    ): ResponseDTO<*> {
        val jwt = tokenRaw.removePrefix(token_prefix)
        val member = questionService.jwt.parsingJwt(jwt)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val result = questionService.insert(member, question)
        return toDTO(result)
    }

}