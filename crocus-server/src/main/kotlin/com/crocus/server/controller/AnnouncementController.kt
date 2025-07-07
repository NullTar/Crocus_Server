package com.crocus.server.controller

import com.crocus.server.entity.entities.Announcement
import com.crocus.server.entity.function.detect
import com.crocus.server.entity.parameter.token_prefix
import com.crocus.server.enum.Role
import com.crocus.server.service.AnnouncementServiceImp
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/announcement")
class AnnouncementController(
    private val announcementService: AnnouncementServiceImp,
) {

    @RequestMapping("/news")
    suspend fun news(): List<Announcement> {
        return announcementService.getNew()
    }

    @RequestMapping("/crocus/manager/upload")
    suspend fun upload(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestBody announcement: Announcement,
    ): ResponseDTO<*> {
        val jwt = tokenRaw.removePrefix(token_prefix)
        val member = announcementService.jwt.parsingJwt(jwt)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val response = announcementService.insert(member, announcement)
        return toDTO(response)
    }

    @RequestMapping("/crocus/manager/update")
    suspend fun update(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestBody announcement: Announcement,
    ): ResponseDTO<*> {
        val jwt = tokenRaw.removePrefix(token_prefix)
        val member = announcementService.jwt.parsingJwt(jwt)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val response = announcementService.update(member, announcement)
        return toDTO(response)
    }

    @RequestMapping("/crocus/manager/delete")
    suspend fun delete(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestParam("uuid") uuid: String
    ): ResponseDTO<*> {
        val jwt = tokenRaw.removePrefix(token_prefix)
        val member = announcementService.jwt.parsingJwt(jwt)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val response = announcementService.delete(member, uuid)
        return toDTO(response)
    }


    @RequestMapping("/crocus/manager/query")
    suspend fun query(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestParam("uuid") uuid: String
    ): ResponseDTO<*> {
        val jwt = tokenRaw.removePrefix(token_prefix)
        val member = announcementService.jwt.parsingJwt(jwt)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val response = announcementService.queryByUUID(member, uuid)
        return toDTO(data = response)
    }

}