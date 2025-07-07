package com.crucos.web.service

import com.crocus.server.entity.entities.Book
import com.crocus.server.entity.entities.CommonQuestion
import com.crocus.server.service.QuestionServiceImp
import com.crocus.server.utils.response.response
import com.crucos.web.SpringContext


class QuestionWebServer {

    private val questionService = SpringContext.server.getBean(QuestionServiceImp::class.java)

    suspend fun recommend(): List<CommonQuestion> = questionService.recommend()

    suspend fun queryList(now: Long, size: Long): List<CommonQuestion> = questionService.queryList(now, size)

    suspend fun queryByUUID(uuid: String): CommonQuestion = questionService.queryByUUID(uuid)

}