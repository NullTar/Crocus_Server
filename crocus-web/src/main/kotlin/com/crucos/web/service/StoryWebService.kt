package com.crucos.web.service

import com.crocus.server.entity.entities.Story
import com.crocus.server.service.StoryServiceImp
import com.crucos.web.SpringContext
import java.math.BigInteger

class StoryWebService {

    private val storyService = SpringContext.server.getBean(StoryServiceImp::class.java)

    suspend fun recommend(id: BigInteger?, size: Int): List<Story> {
        val random = id ?: (1..100).random().toBigInteger()
        return storyService.recommend(random, size)

    }

}