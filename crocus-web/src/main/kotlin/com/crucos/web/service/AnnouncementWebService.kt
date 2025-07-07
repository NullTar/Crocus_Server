package com.crucos.web.service

import com.crocus.server.entity.entities.Announcement
import com.crocus.server.service.AnnouncementServiceImp
import com.crucos.web.SpringContext


class AnnouncementWebService {
    private val announcementService = SpringContext.server.getBean(AnnouncementServiceImp::class.java)

    suspend fun queryByUUID(uuid: String): Announcement = announcementService.queryByUUID(uuid)

}